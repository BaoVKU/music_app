package com.example.musicapp.model

data class Favorite(
    val id : String? = null,
    val songs: List<String>? = null,
    val albums: List<String>? = null
)