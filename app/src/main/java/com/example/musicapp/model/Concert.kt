package com.example.musicapp.model

import com.google.firebase.Timestamp

data class Concert(
    val id: String? = null,
    val name: String? = null,
    val date: Timestamp? = null,
    val location: String? = null,
    val banner: String? = null,
    val capacity: Int? = null,
    val ticket: Int? = null,
    val description: String? = null,
    val artist: List<String>? = null,
)
