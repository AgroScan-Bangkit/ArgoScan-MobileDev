package com.example.agroscan.data.local.response

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class UserPreferences(private val context: Context) {

    companion object {
        private val Context.dataStore by preferencesDataStore(name = "user_preferences")
        private val TOKEN = stringPreferencesKey("token")
        private val USERNAME = stringPreferencesKey("username")
        private val TEMP_TOKEN = stringPreferencesKey("temp_token")

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: UserPreferences? = null

        fun getInstance(context: Context): UserPreferences =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserPreferences(context).also { INSTANCE = it }
            }
    }


    // token
    private val token: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[TOKEN]
        }

    // temp token
    private val tempToken: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[TEMP_TOKEN]
        }

    // username
    private val username: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USERNAME]
        }

    // save token
    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN] = token
        }
        Log.d("UserPreferences", "Token saved: $token")
    }

    // get token
    suspend fun getToken(): String? {
        val tokenValue = token.firstOrNull()
        Log.d("UserPreferences", "Retrieved token: $tokenValue")
        return tokenValue
    }

    // check token
    suspend fun hasToken(): Boolean {
        return !getToken().isNullOrEmpty()
    }

    // save username
    suspend fun saveUsername(username: String) {
        context.dataStore.edit { preferences ->
            preferences[USERNAME] = username
        }
        Log.d("UserPreferences", "Username saved: $username")
    }

    // get username
    suspend fun getUsername(): String? {
        return username.firstOrNull()
    }

    // clear token
    suspend fun clearToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(TOKEN)
        }
        Log.d("UserPreferences", "Token cleared")
    }

    // save tempToken
    suspend fun saveTempToken(tempToken: String) {
        context.dataStore.edit { preferences ->
            preferences[TEMP_TOKEN] = tempToken
        }
        Log.d("UserPreferences", "TempToken saved: $tempToken")
    }

    // get tempToken
    suspend fun getTempToken(): String? {
        return tempToken.firstOrNull()
    }
}