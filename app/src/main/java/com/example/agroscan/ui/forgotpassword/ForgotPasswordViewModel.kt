package com.example.agroscan.ui.forgotpassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agroscan.data.remote.request.ForgotPasswordRequest
import com.example.agroscan.data.remote.respone.ForgotPasswordResponse
import com.example.agroscan.data.repository.ForgotPasswordRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ForgotPasswordViewModel(private val repository: ForgotPasswordRepository) : ViewModel() {
    private val _forgotPasswordResponse = MutableLiveData<ForgotPasswordResponse>()
    val forgotPasswordResponse: LiveData<ForgotPasswordResponse> = _forgotPasswordResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun forgotPassword(forgotPasswordRequest: ForgotPasswordRequest) {
        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                val response = repository.forgotPassword(forgotPasswordRequest)
                _forgotPasswordResponse.postValue(response)
            } catch (e: HttpException) {
                // Menangani error HTTP (misalnya error 404, 500)
                when (e.code()) {
                    404 -> _errorMessage.postValue("Username atau email tidak ditemukan")
                    500 -> _errorMessage.postValue("Server sedang bermasalah, coba lagi nanti")
                    else -> _errorMessage.postValue("Terjadi kesalahan: ${e.message()}")
                }
            } catch (e: IOException) {
                // Menangani error jaringan (misalnya tidak ada koneksi internet)
                _errorMessage.postValue("Terjadi masalah jaringan. Periksa koneksi Anda")
            } catch (e: Exception) {
                // Menangani pengecualian umum
                _errorMessage.postValue("Terjadi kesalahan: ${e.message}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}