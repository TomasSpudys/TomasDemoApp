package com.example.tomasdemoapp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {

    // IP address where the server is running:
    val BASE_URL = "http://172.16.1.244:8080/api/users"

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    fun onUsernameChanged(newUsername: String) {
        _username.value = newUsername
    }

    fun onPasswordChanged(newPassword: String) {
        _password.value = newPassword
    }

    suspend fun login() {
        val postData = "{\"username\":\"${_username.value}\",\"password\":\"${_password.value}\"}"
        val url = "$BASE_URL/login"
        HttpPostTask { response ->
            // Handle the response here
            println("Response: $response")
        }.execute(url, postData)
    }

    suspend fun register() {
        val postData = "{\"username\":\"${_username.value}\",\"password\":\"${_password.value}\"}"
        val url = "$BASE_URL/register"
        HttpPostTask { response ->
            // Handle the response here
            println("Response: $response")
        }.execute(url, postData)
    }
}