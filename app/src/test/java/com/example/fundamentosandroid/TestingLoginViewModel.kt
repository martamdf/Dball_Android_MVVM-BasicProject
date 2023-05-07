package com.example.fundamentosandroid

import com.example.fundamentosandroid.login.LoginViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TestingLoginViewModel {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    val viewModel = LoginViewModel()

    @Test
    fun `check login`() = runTest {
        val expected = "Incorrect User"
        launch {
            viewModel.uiState.collect {
                when (it) {
                    is LoginViewModel.UiState.Error -> assert(it.error == "Error en usuario o contraseña")
                    is LoginViewModel.UiState.OnTokenReceived -> {
                        assert(it.token != "")
                    }
                    is LoginViewModel.UiState.Idle -> {
                        assertEquals("", "") // TODO ver como gestionar esto bien
                        cancel()
                    }
                }
            }
        }
        viewModel.login("j","i")
    }
}