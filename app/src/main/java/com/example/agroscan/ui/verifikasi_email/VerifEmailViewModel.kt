package com.example.agroscan.ui.verifikasi_email

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.agroscan.data.local.response.UserPreferences
import com.example.agroscan.data.remote.request.VerifikasiEmailRequest
import com.example.agroscan.data.remote.respone.VerifikasiEmailResponse
import com.example.agroscan.data.repository.VerifEmailRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class VerifEmailViewModel(
    private val verifEmailRepository: VerifEmailRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _verifEmailResponse = MutableLiveData<VerifikasiEmailResponse>()
    val verifEmailResponse: LiveData<VerifikasiEmailResponse> = _verifEmailResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun verifyOTP(verifikasiEmailRequest: VerifikasiEmailRequest) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = verifEmailRepository.verifyOTP(verifikasiEmailRequest)
                // Handle response succes
                if (response.tempToken != null){
                    _verifEmailResponse.postValue(response)
                    userPreferences.saveTempToken(response.tempToken)
                } else{
                    _errorMessage.postValue("Token is null")
                }

            } catch (e: HttpException) {
                // Menangani error HTTP (misalnya error 404, 500)
                when (e.code()) {
                    404 -> _errorMessage.postValue("Error 404: Not Found")
                    500 -> _errorMessage.postValue("Error 500: Internal Server Error")
                    else -> _errorMessage.postValue("Error: ${e.message}")
                }
            } catch (e: IOException) {
                // Tangani error koneksi
                _errorMessage.postValue("Terjadi masalah jaringan. Periksa koneksi Anda")
            } catch (e: Exception) {
                // Tangani pengecualian umum
                _errorMessage.postValue("Error: ${e.message}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}