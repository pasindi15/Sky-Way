package com.example.skyway.model

data class Ticket(
    val depTime: String,
    val depCode: String,
    val depCity: String,
    val depAirport: String,
    val arrTime: String,
    val arrCode: String,
    val arrCity: String,
    val arrAirport: String,
    val airline: String,
    val price: String
)
