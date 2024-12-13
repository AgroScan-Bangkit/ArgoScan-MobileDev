package com.example.agroscan.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agroscan.data.local.response.UserPreferences
import com.example.agroscan.data.remote.respone.ErrorResponse
import com.example.agroscan.data.remote.request.LoginRequest
import com.example.agroscan.data.repository.AuthRepository
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _loginResult = MutableLiveData<Result<String>>()
    val loginResult: MutableLiveData<Result<String>> = _loginResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val loginRequest = LoginRequest(email, password)
                val result = authRepository.login(loginRequest)

                Log.d("LoginViewModel", "Login response: $result")
                // Handling login response
                val token = result.token
                // Save token
                if (!token.isNullOrEmpty()) {
                    Log.d("LoginViewModel", "Token received: $token")
                    userPreferences.saveToken(token)
                    _loginResult.postValue(Result.success(token))
                } else {
                    Log.e("LoginViewModel", "Token is null or empty")
                    _loginResult.postValue(Result.failure(Exception("Token is null or empty")))
                }
            } catch (e: HttpException) {
                val errorMessage = handleError(e)
                Log.e("LoginViewModel", "HttpException: $errorMessage")
                _loginResult.postValue(Result.failure(Exception(errorMessage)))
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Unexpected error"
                Log.e("LoginViewModel", "Exception: $errorMessage")
                _loginResult.postValue(Result.failure(Exception(errorMessage)))
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    private fun handleError(e: HttpException): String {
        val jsonInString = e.response()?.errorBody()?.string()
        val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
        return errorBody.message ?: "Server error"
    }
}
