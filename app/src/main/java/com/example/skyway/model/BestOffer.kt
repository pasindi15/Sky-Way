package com.example.skyway.model

data class BestOffer(
	val country: String,
	val city: String,
	val price: String, // Keep formatted (e.g., $1200) for direct display
	val imageRes: Int,
	var isFavorite: Boolean = false
)
