package com.example.agroscan.ui.new_password

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agroscan.data.remote.request.ResetPasswordRequest
import com.example.agroscan.data.remote.respone.NewPasswordResponse
import com.example.agroscan.data.repository.NewPasswordRepository
import kotlinx.coroutines.launch

class NewPasswordViewModel(private val newPasswordRepository: NewPasswordRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _newPasswordResponse = MutableLiveData<NewPasswordResponse>()
    val newPasswordResponse: LiveData<NewPasswordResponse> = _newPasswordResponse

    fun updatePassword(newPasswordRequest: ResetPasswordRequest, tempToken: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = newPasswordRepository.resetPassword(newPasswordRequest, tempToken)

                // Check if the response message contains "Password berhasil direset"
                if (response.message?.isNotEmpty() == true && response.message.contains(
                        "Password berhasil direset",
                        ignoreCase = true
                    )
                ) {
                    _newPasswordResponse.postValue(null)
                } else {
                    _errorMessage.postValue("Failed to reset password: ${response.message}")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Error: ${e.message}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}