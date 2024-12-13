package com.example.agroscan.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.agroscan.MainActivity
import com.example.agroscan.data.local.response.UserPreferences
import com.example.agroscan.databinding.ActivityLoginBinding
import com.example.agroscan.di.Injection
import com.example.agroscan.ui.custom.edittext.EmailEditText
import com.example.agroscan.ui.custom.edittext.PasswordEditText
import com.example.agroscan.ui.custom.button.LoginButton
import com.example.agroscan.ui.forgotpassword.ForgotPasswordActivity
import com.example.agroscan.ui.register.RegisterActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var userPreferences: UserPreferences

    private lateinit var emailEditText: EmailEditText
    private lateinit var passwordEditText: PasswordEditText
    private lateinit var loginButton: LoginButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        // Initialize UserPreferences
        userPreferences = Injection.provideUserPreferences(this)

        // Initialize ViewModel
        viewModel = ViewModelProvider(
            this, LoginViewModelFactory(
                Injection.provideAuthRepository(this),
                userPreferences
            )
        )[LoginViewModel::class.java]

        // set button disable
        binding.btnLogin.isEnabled = false

        // teks edit dan button binding
        emailEditText = binding.edLoginEmail
        passwordEditText = binding.edLoginPassword
        loginButton = binding.btnLogin

        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            // show progress
            binding.progressBarLogin.visibility = View.VISIBLE
            // set button
            viewModel.login(email, password)
        }

        binding.tvResetPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        // Button register
        binding.edRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        // Set listeners untuk tiap field
        emailEditText.addTextChangedListener { setButtonEnable() }
        passwordEditText.addTextChangedListener { setButtonEnable() }

        setObserver()
    }

    private fun setButtonEnable() {
        val email = binding.edLoginEmail.text.toString()
        val password = binding.edLoginPassword.text.toString()

        // set button enable
        binding.btnLogin.isEnabled = email.isNotEmpty() && password.isNotEmpty()
    }

    private fun setObserver() {
        viewModel.loginResult.observe(this) { result ->
            // Hide progress bar
            binding.progressBarLogin.visibility = View.GONE

            result.onSuccess { token ->
                Log.d("LoginActivity", "Login success, token: $token")
                lifecycleScope.launch {
                    // Save token
                    userPreferences.saveToken(token)
                    Log.d("UserPreferences", "Token saved: $token")

                    // Redirect to MainActivity
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                    Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()
                }
            }

            result.onFailure { throwable ->
                Log.e("LoginActivity", "Login failed: ${throwable.message}")
                val errorMessage = throwable.message ?: "Login failed"
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()

                // Enable login button again after error
                binding.btnLogin.isEnabled = true
            }
        }
    }
}