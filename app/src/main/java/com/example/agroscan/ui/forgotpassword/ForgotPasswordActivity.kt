package com.example.agroscan.ui.forgotpassword

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.agroscan.data.remote.request.ForgotPasswordRequest
import com.example.agroscan.data.remote.retrofit.ApiConfig
import com.example.agroscan.data.repository.ForgotPasswordRepository
import com.example.agroscan.databinding.ActivityForgotPasswordBinding
import com.example.agroscan.ui.verifikasi_email.VerifEmailActivity
import com.google.android.material.snackbar.Snackbar

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var forgotPasswordViewModel: ForgotPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set button disable
        binding.btnForgotPassword.isEnabled = false

        // Initialize ViewModel using ViewModelProvider
        forgotPasswordViewModel = ViewModelProvider(
            this,
            ForgotPasswordViewModelFactory(ForgotPasswordRepository(ApiConfig(null).getAuthApiService()))
        )[ForgotPasswordViewModel::class.java]

        // Observasi status loading
        forgotPasswordViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            // Enable/disable button based on form validity and loading status
            binding.btnForgotPassword.isEnabled = !isLoading && isFormValid()
        }

        // Set up button click listener
        binding.btnForgotPassword.setOnClickListener {
            val email = binding.emailEditText.text.toString()

            // Call forgotPassword API
            forgotPasswordViewModel.forgotPassword(ForgotPasswordRequest(email))
        }

        // Observe the result of forgotPassword API
        forgotPasswordViewModel.forgotPasswordResponse.observe(this) { response ->
            response?.message?.let {
                if (it.contains("Kode reset password telah dikirim ke email Anda", ignoreCase = true)) {
                    // Show success message
                    Snackbar.make(
                        binding.root,
                        "Kode reset sudah dikirim ke email anda",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    // Email sent successfully, redirect to VerifEmailActivity
                    val intent = Intent(this, VerifEmailActivity::class.java)
                    startActivity(intent)
                } else {
                    // Show error message
                    Toast.makeText(this, "Error: $it", Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                // Tangani jika message null
                Toast.makeText(this, "Terjadi kesalahan, coba lagi nanti", Toast.LENGTH_SHORT).show()
            }
        }

        // Observe the error message
        forgotPasswordViewModel.errorMessage.observe(this, { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        })

        // Update button status when input changes
        binding.emailEditText.addTextChangedListener { _ ->
            binding.btnForgotPassword.isEnabled = isFormValid()
        }
    }

    // Fungsi untuk memeriksa apakah form valid
    private fun isFormValid(): Boolean {
        val emailError = binding.emailEditText.error
        return emailError == null &&
                binding.emailEditText.text.toString().isNotEmpty()
    }
}

