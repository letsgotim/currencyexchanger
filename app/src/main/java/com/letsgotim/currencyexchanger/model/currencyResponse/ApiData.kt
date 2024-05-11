package com.letsgotim.currencyexchanger.model.currencyResponse

data class ApiData(
    val base: String,
    val date: String,
    val rates: Map<String, Double>
)