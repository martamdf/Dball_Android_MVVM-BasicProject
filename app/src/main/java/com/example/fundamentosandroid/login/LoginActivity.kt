package com.example.fundamentosandroid.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.fundamentosandroid.heroes.HeroesActivity
import com.example.fundamentosandroid.databinding.LoginMainBinding
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginMainBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            viewModel.uiState.collect{
                when (it){
                    is LoginViewModel.UiState.OnTokenReceived -> saveToken(it.token)
                    is LoginViewModel.UiState.Idle -> Unit
                    is LoginViewModel.UiState.Error -> showError(viewModel.uiState.value.toString())
                }
            }
        }

        binding.buttonLogin.setOnClickListener {
            tryLogin()
        }
    }

    private fun showError(error: String) {
        Toast.makeText(this, "$error", Toast.LENGTH_SHORT).show()
    }

    private fun saveToken(token: String){
        getPreferences(Context.MODE_PRIVATE).edit().apply {
            putString("Email", binding.editTextTextEmailAddress2.text.trim().toString())
            putString("Password", binding.editTextTextPassword2.text.trim().toString())
            putString("Token", token)

            apply()
        }
        launchActivity()
    }

    private fun launchActivity(){
        val intent = Intent(this, HeroesActivity::class.java)
        startActivity(intent)
    }

    private fun tryLogin(){
        val email = binding.editTextTextEmailAddress2.text.trim().toString()
        val password = binding.editTextTextPassword2.text.trim().toString()
        viewModel.login(email, password)
    }
}