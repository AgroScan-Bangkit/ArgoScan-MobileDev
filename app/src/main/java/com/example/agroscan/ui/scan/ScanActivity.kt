package com.example.agroscan.ui.scan

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.agroscan.data.local.response.UserPreferences
import com.example.agroscan.data.remote.retrofit.ApiConfig
import com.example.agroscan.databinding.ActivityScanBinding
import com.example.agroscan.ui.scan_result.ScanResultActivity
import com.example.agroscan.utils.reduceFileImage
import com.example.agroscan.utils.uriToFile
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException

class ScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanBinding
    private var currentImageUri: Uri? = null
    private val userPreferences = UserPreferences(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUriString = intent.getStringExtra("imageUri")
        if (imageUriString != null) {
            currentImageUri = Uri.parse(imageUriString)
            // show image
            currentImageUri?.let {
                binding.ivPreviewImage.setImageURI(it)
            }
        } else {
            Toast.makeText(this, "Image not found", Toast.LENGTH_SHORT).show()
            finish()
        }

        // upload image
        binding.btnUploadFoto.setOnClickListener {
            uploadImage()
        }
    }

    private fun uploadImage() {
        lifecycleScope.launch {

            val mainApiService = ApiConfig(userPreferences).getMainApiService()
            val token = userPreferences.getToken()
            Log.d("Token", "Token retrieved: $token")

            // Pastikan token tidak null sebelum melanjutkan
            if (token.isNullOrEmpty()) {
                showToast("Authorization token is missing.")
                return@launch
            }

            currentImageUri?.let { uri ->
                // Convert URI to File
                val imageFile = uriToFile(uri, this@ScanActivity).reduceFileImage()
                showLoading(true)
                // Prepare the RequestBody for uploading
                val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
                val multipartBody = MultipartBody.Part.createFormData(
                    "image",
                    imageFile.name,
                    requestImageFile
                )

                try {
                    val response = mainApiService.predictImage(multipartBody)

                    // Log the response for debugging
                    Log.d("Prediction Response", "Response Body: ${Gson().toJson(response)}")

                    // Validasi response, cek apakah prediction ada
                    if (!response.prediction.isNullOrEmpty()) {
                        val intent = Intent(this@ScanActivity, ScanResultActivity::class.java)
                        intent.putExtra("image_url", response.imageUrl)
                        intent.putExtra("prediction", response.prediction)
                        intent.putExtra("recommended_action", response.recommendedAction)
                        startActivity(intent)
                        showToast("Prediction Success!")
                    } else {
                        showToast("Prediction Failed. No result returned.")
                    }
                } catch (e: HttpException) {
                    val statusCode = e.code()
                    val errorBody = e.response()?.errorBody()?.string()
                    Log.e("API Error", "Status Code: $statusCode")
                    Log.e("API Error", "Error Body: $errorBody")

                    when (statusCode) {
                        400 -> showToast("Bad request")
                        401 -> showToast("Unauthorized")
                        500 -> showToast("Server error")
                        else -> showToast("Unexpected Error: $statusCode")
                    }
                } catch (e: Exception) {
                    showToast("Unexpected error: ${e.localizedMessage}")
                } finally {
                    showLoading(false)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBarScan.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
