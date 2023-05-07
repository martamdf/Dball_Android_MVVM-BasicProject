package com.example.fundamentosandroid.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.Credentials
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState : StateFlow<UiState> = _uiState
    val BASE_URL = "https://dragonball.keepcoding.education/api/"
    var token: String = ""
    init {

    }
    fun login(email:String, password:String) {
        viewModelScope.launch(Dispatchers.IO) {
            val client = OkHttpClient()
            val url = "${BASE_URL}auth/login"
            val credentials = Credentials.basic("$email", "$password")
            val formBody = FormBody.Builder() // Esto hace que la request sea de tipo POST
                .build()
            val request = Request.Builder()
                .url(url)
                .addHeader("Authorization", "$credentials")
                .post(formBody)
                .build()
            val call = client.newCall(request)
            val response = call.execute()

            if (response.code == 200){
                response.body?.let { responseBody ->
                    try {
                        val token = responseBody.string()
                        _uiState.value = UiState.OnTokenReceived(responseBody.toString())
                    } catch(ex: Exception ) {
                        _uiState.value = UiState.Error("Something went wrong in the response")
                    }
                } ?: run { _uiState.value = UiState.Error("Something went wrong in the request") }
            }
            else {
                _uiState.value = UiState.Error("Error en usuario o contraseña")
            }
        }
    }

    sealed class UiState {
        object Idle : UiState()
        data class Error(val error: String) : UiState()
        data class OnTokenReceived(val token:String) : UiState()
    }
}
