package com.example.fundamentosandroid.heroes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.fundamentosandroid.R
import com.example.fundamentosandroid.databinding.HeroItem2Binding
import com.example.fundamentosandroid.datafiles.Hero
import com.squareup.picasso.Picasso

interface HeroClicked {
    fun heroClicked(hero: Hero)
}

class HeroesListAdapter(
    private val heroesList: List<Hero>,
    private val callback: HeroClicked,
): RecyclerView.Adapter<HeroesListAdapter.HeroesListViewHolder>() {

    class HeroesListViewHolder(private var item: HeroItem2Binding, private val callback: HeroClicked) : RecyclerView.ViewHolder(item.root) {

        fun showHero(hero: Hero, par: Boolean) {
            item.heroTitle.text = hero.name
            item.heroDescription.text = hero.description
            Picasso.get().load(hero.photo).into(item.featuredImage)
            item.progressBar.progress = hero.hitPoints
            val color = if (hero.hitPoints>50) {
            //val color = if (par) {
                ContextCompat.getColor(item.root.context, R.color.white)
            }
            else if (hero.hitPoints==0) {
                item.featuredImage.imageAlpha= 90
                ContextCompat.getColor(item.root.context, R.color.inactive)
            }
            else if (hero.hitPoints<25) {
                ContextCompat.getColor(item.root.context, R.color.danger)
            }
            else {
                ContextCompat.getColor(item.root.context, R.color.white)
            }
            item.lBackground.setBackgroundColor(color)
            item.lBackground.setOnClickListener {
                callback.heroClicked(hero)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroesListViewHolder {
        val binding = HeroItem2Binding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HeroesListViewHolder(binding, callback)
    }

    override fun getItemCount(): Int {
        return heroesList.size
    }

    override fun onBindViewHolder(holder: HeroesListViewHolder, position: Int) {
        holder.showHero(heroesList[position], position % 2 == 0)
    }
}