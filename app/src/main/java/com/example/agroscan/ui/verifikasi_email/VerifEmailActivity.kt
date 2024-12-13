package com.example.agroscan.ui.verifikasi_email

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.agroscan.data.local.response.UserPreferences
import com.example.agroscan.data.remote.request.VerifikasiEmailRequest
import com.example.agroscan.data.remote.retrofit.ApiConfig
import com.example.agroscan.data.repository.VerifEmailRepository
import com.example.agroscan.databinding.ActivityVerifEmailBinding
import com.example.agroscan.ui.new_password.NewPasswordActivity
import kotlinx.coroutines.launch

class VerifEmailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerifEmailBinding
    private lateinit var verifyEmailViewModel: VerifEmailViewModel
    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityVerifEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize UserPrefrences
        userPreferences = UserPreferences.getInstance(applicationContext)


        // Initialize ViewModel
        verifyEmailViewModel = ViewModelProvider(
            this,
            VerifEmailViewModelFactory(VerifEmailRepository(ApiConfig().getAuthApiService()), userPreferences)
        )[VerifEmailViewModel::class.java]

        // Observe loading state
        verifyEmailViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Observe error message
        verifyEmailViewModel.errorMessage.observe(this) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        // Observe the OTP verification response
        verifyEmailViewModel.verifEmailResponse.observe(this) { response ->
            if (response != null && response.message?.contains("Kode verifikasi valid", ignoreCase = true) == true) {
                // Handle the response here
                Toast.makeText(this, "OTP verified successfully", Toast.LENGTH_SHORT).show()

                // Save the tempToken
                response.tempToken?.let { tempToken ->
                    val intent = Intent(this, NewPasswordActivity::class.java).apply {
                        putExtra("tempToken", tempToken)
                    }
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                } ?: run {
                    Toast.makeText(this, "Failed to retrieve tempToken", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "OTP verification failed", Toast.LENGTH_SHORT).show()
                clearOtpFields()
            }
        }

        // Set up the submit button click listener
        binding.btnSubmitOtp.setOnClickListener {
            val otpCode =
                "${binding.etOtp1.text}${binding.etOtp2.text}${binding.etOtp3.text}${binding.etOtp4.text}${binding.etOtp5.text}${binding.etOtp6.text}"

            // Check if the OTP code is not empty
            if (otpCode.length == 6) {
                val verifikasiEmailRequest = VerifikasiEmailRequest(otpCode)
                verifyEmailViewModel.verifyOTP(verifikasiEmailRequest)
            } else {
                Toast.makeText(this, "Masukkan kode OTP terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearOtpFields() {
        binding.etOtp1.text?.clear()
        binding.etOtp2.text?.clear()
        binding.etOtp3.text?.clear()
        binding.etOtp4.text?.clear()
        binding.etOtp5.text?.clear()
        binding.etOtp6.text?.clear()
        binding.etOtp1.requestFocus()
    }
}
