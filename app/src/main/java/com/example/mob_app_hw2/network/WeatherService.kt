package com.example.mob_app_hw2.network

import com.example.mob_app_hw2.model.WeatherInfo
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("current.json")
    suspend fun getWeatherInfo(@Query("q") city: String): WeatherInfo
}
