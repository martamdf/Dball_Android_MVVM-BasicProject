package com.example.fundamentosandroid.heroes.herodetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.fundamentosandroid.databinding.FragmentHeroDetailBinding
import com.example.fundamentosandroid.datafiles.Hero
import com.example.fundamentosandroid.heroes.Clicked
import com.example.fundamentosandroid.heroes.HeroesViewModel
import com.squareup.picasso.Picasso


class HeroDetailFragment(val callback: Clicked) : Fragment() { //, heroe: Hero

    //private val hero: Hero = heroe
    private lateinit var binding: FragmentHeroDetailBinding
    private val viewModel: HeroesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHeroDetailBinding.inflate(inflater)
        configureHero(viewModel.hero)
        configureListeners()

        return binding.root
    }

    private fun configureListeners() {
        binding.button.setOnClickListener {
            callback.goBack()
        }
        binding.buttonDamage.setOnClickListener {
            viewModel.damagedHero()
            configureHero(viewModel.hero)
        }
        binding.buttonAdd.setOnClickListener {
            viewModel.partialHealingHero()
            configureHero(viewModel.hero)
        }
    }

    private fun configureHero(hero: Hero){
        binding.tvName.text = hero.name
        binding.progressBar3.progress = hero.hitPoints
        binding.tvHP.text = "${hero.hitPoints}%"
        binding.tvDesc.text = hero.description
        Picasso.get().load(hero.photo).into(binding.imageView3)
    }
}