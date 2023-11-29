package com.example.mob_app_hw2.model

data class WeatherInfo(
    val current: CurrentWeather,
)

data class CurrentWeather(
    val temp_c: Double,
)