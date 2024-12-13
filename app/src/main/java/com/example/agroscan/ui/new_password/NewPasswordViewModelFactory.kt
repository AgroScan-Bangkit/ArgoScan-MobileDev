package com.example.agroscan.ui.new_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.agroscan.data.repository.NewPasswordRepository

class NewPasswordViewModelFactory(private val repository: NewPasswordRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewPasswordViewModel::class.java)) {
            return NewPasswordViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}