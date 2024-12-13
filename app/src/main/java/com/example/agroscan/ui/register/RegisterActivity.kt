package com.example.agroscan.ui.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.example.agroscan.databinding.ActivityRegisterBinding
import com.example.agroscan.di.Injection
import com.example.agroscan.ui.custom.edittext.EmailEditText
import com.example.agroscan.ui.custom.edittext.NameEditText
import com.example.agroscan.ui.custom.edittext.PasswordEditText
import com.example.agroscan.ui.custom.button.RegisterButton
import com.example.agroscan.ui.login.LoginActivity
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel
    private lateinit var nameEditText: NameEditText
    private lateinit var emailEditText: EmailEditText
    private lateinit var passwordEditText: PasswordEditText
    private lateinit var registerButton: RegisterButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnRegister.isEnabled = false

        // Initialize ViewModel
        lifecycleScope.launch {
            viewModel = Injection.provideRegisterViewModel(this@RegisterActivity)
        }

        // Observe loading state
        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.progressBarRegister.visibility = View.VISIBLE
            } else {
                binding.progressBarRegister.visibility = View.GONE
            }
        }
        // teks edit dan button binding
        nameEditText = binding.edRegisterName
        emailEditText = binding.edRegisterEmail
        passwordEditText = binding.edRegisterPassword
        registerButton = binding.btnRegister


        binding.btnRegister.setOnClickListener {
            val username = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            viewModel.register(username, email, password)
        }

        binding.loginTextView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Set listeners untuk tiap field
        nameEditText.addTextChangedListener { setButtonEnable() }
        emailEditText.addTextChangedListener { setButtonEnable() }
        passwordEditText.addTextChangedListener { setButtonEnable() }

        setObserver()
    }

    private fun setButtonEnable() {
        val name = nameEditText.text.toString()
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        // set button enable
        binding.btnRegister.isEnabled = name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()
    }

    private fun setObserver() {
        // Observe register result
        viewModel.registerResult.observe(this) { result ->
            // hide progress
            binding.progressBarRegister.visibility = View.GONE

            // Handle result
            result.onSuccess { message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            // Handle error
            result.onFailure { throwable ->
                Log.e("RegisterActivity", "Error: ${throwable.message}")
                val errorMessage = throwable.message ?: "Register failed"
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                // enable button after error
                binding.btnRegister.isEnabled = true
            }
        }
    }
}