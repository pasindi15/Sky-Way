package com.example.skyway.model

data class SearchDestination(
    val country: String,
    val city: String,
    val imageRes: Int,
    val rating: String,
    var isFavorite: Boolean = false
)
