package com.example.fundamentosandroid.heroes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundamentosandroid.datafiles.Hero
import com.example.fundamentosandroid.datafiles.HeroDTO
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.random.Random

class HeroesViewModel: ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState : StateFlow<UiState> = _uiState
    var token = "//eyJhbGciOiJIUzI1NiIsImtpZCI6InByaXZhdGUiLCJ0eXAiOiJKV1QifQ.eyJlbWFpbCI6Im1hcnRhLm1hcXVlZGFub0BnbWFpbC5lcyIsImlkZW50aWZ5IjoiNUJBNDA1N0EtMDhCMi00NkYzLTk0NUEtQzIwMUVFOEZEOUQ4IiwiZXhwaXJhdGlvbiI6NjQwOTIyMTEyMDB9._Pnu84PfFsTcJMzb5AoKC42sWbcULIKauWHbtebc81E"
    var list : List<Hero> = listOf()
    lateinit var hero : Hero

    init {
        if (list.isEmpty()){
            downloadHeroes()
        }
        else{

        }
    }

    fun configureDetails (hero: Hero){
        this.hero = hero
        _uiState.value = UiState.OnHeroReceived(this.hero)
    }

    fun partialHealingHero (){
        this.hero.hitPoints += 20
        if (this.hero.hitPoints > 100) {
            this.hero.hitPoints = 100
        }
        _uiState.value = UiState.OnHeroRecovered
    }

    fun healingAllHeroes (){
        list.map {
            it.hitPoints=100
        }
        _uiState.value = UiState.OnHeroesReceived(list)
    }

    fun damagedHero(){
        val num = Random.nextInt(10, 60,)
        hero.hitPoints -= num
        if (hero.hitPoints <= 0) {
            hero.hitPoints = 0
            _uiState.value = UiState.OnHeroesReceived(list)
        }
        _uiState.value = UiState.OnHeroDamaged
    }

    fun downloadHeroes() {
        viewModelScope.launch(Dispatchers.IO) {
            val client = OkHttpClient()
            val url = "https://dragonball.keepcoding.education/api/heros/all"
            val body = FormBody.Builder()
                .add("name", "")
                .build()
            val request = Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer $token")
                .post(body)
                .build()
            val call = client.newCall(request)
            val response = call.execute()

            response.body?.let { responseBody ->
                val gson = Gson()
                try {
                    val heroDtoArray = gson.fromJson(responseBody.string(), Array<HeroDTO>::class.java)
                    if(list.isEmpty()){
                        list = heroDtoArray.toList()
                            .map { Hero(it.name, it.description, it.favorite, it.photo, it.id,) }
                        _uiState.value = UiState.OnHeroesReceived(list)
                    }
                    else{
                        _uiState.value = UiState.OnHeroesReceived(list)
                    }
                } catch(ex: Exception ) {
                    _uiState.value = UiState.Error("Something went wrong in the response")
                }
            } ?: run { _uiState.value = UiState.Error("Something went wrong in the request") }
        }
    }


    sealed class UiState {
        object Idle : UiState()
        data class Error(val error: String) : UiState()
        data class OnHeroesReceived(val heroList: List<Hero>) : UiState()
        data class OnHeroReceived(val hero: Hero) : UiState()
        object OnHeroDamaged : UiState()
        object OnHeroRecovered: UiState()
    }
}