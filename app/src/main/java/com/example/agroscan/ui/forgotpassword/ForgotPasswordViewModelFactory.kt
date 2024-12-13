package com.example.agroscan.ui.forgotpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.agroscan.data.repository.ForgotPasswordRepository

@Suppress("UNCHECKED_CAST")
class ForgotPasswordViewModelFactory(private val repository: ForgotPasswordRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ForgotPasswordViewModel::class.java)) {
            ForgotPasswordViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}