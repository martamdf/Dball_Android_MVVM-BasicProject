package com.example.fundamentosandroid.datafiles

data class Hero(
    var name: String,
    var description: String,
    var favorite: Boolean,
    var photo: String,
    var id: String,
    var maxHitPoints: Int = 100,
    var hitPoints: Int = 100
)
