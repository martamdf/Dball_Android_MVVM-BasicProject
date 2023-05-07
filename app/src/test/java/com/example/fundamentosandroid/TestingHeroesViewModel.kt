package com.example.fundamentosandroid

import com.example.fundamentosandroid.datafiles.Hero
import com.example.fundamentosandroid.heroes.HeroesViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class TestingHeroesViewModel {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()
    private val viewModel = HeroesViewModel()
    private var hero = Hero("Goku", "description", true, "", "1234",100,100)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `check heroes download`() = runTest {
        launch {
            viewModel.uiState.collect {
                when (it) {
                    is HeroesViewModel.UiState.OnHeroReceived -> { assert(it.hero.maxHitPoints == 100)
                        }
                    is HeroesViewModel.UiState.OnHeroesReceived -> { assert(it.heroList.isNotEmpty())
                        }
                    is HeroesViewModel.UiState.OnHeroDamaged -> {
                        assert(viewModel.hero.hitPoints<101)
                        }
                    is HeroesViewModel.UiState.OnHeroRecovered -> {assert(viewModel.hero.hitPoints in 1..99)
                        cancel() }
                    is HeroesViewModel.UiState.Idle -> {assert(1==1)}
                    else -> {
                        assert(1==1) // TODO ver como gestionar esto bien
                    }
                }
            }
        }
        viewModel.downloadHeroes()
        viewModel.hero = hero
        HeroesViewModel.UiState.OnHeroReceived(viewModel.hero)
        viewModel.damagedHero()
        viewModel.partialHealingHero()
    }
}