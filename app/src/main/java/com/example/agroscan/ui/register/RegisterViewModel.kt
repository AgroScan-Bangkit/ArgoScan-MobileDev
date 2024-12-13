package com.example.agroscan.ui.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agroscan.data.remote.request.RegisterRequest
import com.example.agroscan.data.remote.respone.ErrorResponse
import com.example.agroscan.data.repository.AuthRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _registerResult = MutableLiveData<Result<String>>()
    val registerResult: LiveData<Result<String>> = _registerResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun register(username: String, email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val registerRequest = RegisterRequest(username, email, password)
                val response = authRepository.register(registerRequest)
                _registerResult.postValue(Result.success(response.message ?: "Register Success"))
            } catch (e: HttpException) {
                val errorMessage = handleError(e)
                Log.e("RegisterViewModel", "HttpException: $errorMessage")
                _registerResult.postValue(Result.failure(Exception(errorMessage)))
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Unexpected error"
                Log.e("RegisterViewModel", "Exception: $errorMessage")
                _registerResult.postValue(Result.failure(Exception(errorMessage)))
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun handleError(e: HttpException): String {
        val jsonInString = e.response()?.errorBody()?.string()
        val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
        return errorBody.message ?: "Server error"
    }
}
