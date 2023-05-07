package com.example.fundamentosandroid.heroes

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.fundamentosandroid.databinding.ActivityHeroesBinding
import com.example.fundamentosandroid.heroes.herodetail.HeroDetailFragment
import kotlinx.coroutines.launch


class HeroesActivity : AppCompatActivity(), Clicked {
    private lateinit var binding: ActivityHeroesBinding
    private val viewModel: HeroesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHeroesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //viewModel.token = getToken() // TODO: sustituir token hardcodeado
        lifecycleScope.launch{
            viewModel.uiState.collect{
                when (it){
                    is HeroesViewModel.UiState.Idle ->  viewModel.downloadHeroes()
                    is HeroesViewModel.UiState.OnHeroesReceived -> loadRecyclerView()
                    is HeroesViewModel.UiState.OnHeroReceived -> onClick()
                    is HeroesViewModel.UiState.OnHeroDamaged -> Unit
                    is HeroesViewModel.UiState.OnHeroRecovered -> Unit
                    is HeroesViewModel.UiState.Error -> showError(viewModel.uiState.value.toString())
                }
            }
        }
        loadRecyclerView()
    }

    override fun onClick(){//hero: Hero) {
        loadHeroDetail()//hero)
    }

    override fun goBack() {
        loadRecyclerView()
    }

    private fun showError(error: String) {
        Toast.makeText(this, "$error", Toast.LENGTH_SHORT).show()
    }

    private fun loadRecyclerView() {
        supportFragmentManager
            .beginTransaction()
            .replace(binding.fFragment.id, ListHeroesFragment(this))
            .commitNow()
    }

    private fun loadHeroDetail() {
        supportFragmentManager
            .beginTransaction()
            .replace(binding.fFragment.id, HeroDetailFragment(this))
            .commitNow()
    }
    private fun getToken(): String {
        var token:String
        getPreferences(Context.MODE_PRIVATE).apply {
            token = getString("Token", "").toString()
            return token
        }
    }
}