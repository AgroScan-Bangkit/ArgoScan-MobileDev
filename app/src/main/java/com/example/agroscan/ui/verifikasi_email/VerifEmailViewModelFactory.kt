package com.example.agroscan.ui.verifikasi_email

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.agroscan.data.local.response.UserPreferences
import com.example.agroscan.data.repository.VerifEmailRepository

class VerifEmailViewModelFactory(
    private val repository: VerifEmailRepository,
    private val userPreferences: UserPreferences
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VerifEmailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VerifEmailViewModel(repository, userPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}