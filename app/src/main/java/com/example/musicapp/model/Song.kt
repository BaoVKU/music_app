package com.example.musicapp.model

data class Song(
    val id: String = "",
    val name: String ?= null,
    val artists: List<String>? = null,
    val audio: String ?= null,
    val thumbnail: String ?= null,
    val isFavorite: Boolean = false,
)
