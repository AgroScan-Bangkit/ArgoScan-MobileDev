package com.example.agroscan.ui.new_password

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.agroscan.data.remote.request.ResetPasswordRequest
import com.example.agroscan.data.remote.retrofit.ApiConfig
import com.example.agroscan.data.repository.NewPasswordRepository
import com.example.agroscan.databinding.ActivityNewPasswordBinding
import com.example.agroscan.ui.login.LoginActivity
import com.google.android.material.snackbar.Snackbar

class NewPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewPasswordBinding
    private lateinit var newPasswordViewModel: NewPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        val tempToken = intent.getStringExtra("tempToken") ?: ""

        if (tempToken.isEmpty()) {
            Toast.makeText(this, "Token is empty", Toast.LENGTH_SHORT).show()
            return
        }

        // Initialize ViewModel with Factory
        val repository = NewPasswordRepository(ApiConfig().getAuthApiService()) // Pastikan ini benar
        val factory = NewPasswordViewModelFactory(repository)
        newPasswordViewModel = ViewModelProvider(this, factory)[NewPasswordViewModel::class.java]

        // Observe loading state
//        newPasswordViewModel.isLoading.observe(this) { isLoading ->
//            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
//        }

        // Observe error message
        newPasswordViewModel.errorMessage.observe(this) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        // Handle submit password button click
        binding.btnResetPassword.setOnClickListener {
            Snackbar.make(
                binding.root,
                "Fitur sedang dikembangkan",
                Snackbar.LENGTH_SHORT
            ).show()

//            val newPassword = binding.etNewPassword.text.toString()
//
//            if (newPassword.isNotEmpty()) {
//                // Call ViewModel to update password
//                val resetPasswordRequest = ResetPasswordRequest(newPassword, tempToken)
//                newPasswordViewModel.updatePassword(resetPasswordRequest, tempToken)
//            } else {
//                Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show()
//            }
        }

        // Observe successful password change response
        newPasswordViewModel.newPasswordResponse.observe(this) { response ->
            if (response != null && response.message == "Password updated successfully") {
                // Navigate to LoginActivity after password change
                Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show()

                // Redirect to login page
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } else {
                // Handle failure response
                Toast.makeText(this, "Failed to update password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
