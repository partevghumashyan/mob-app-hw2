package com.example.mob_app_hw2.network

import com.example.mob_app_hw2.constants.API_KEY
import com.example.mob_app_hw2.constants.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val original = chain.request()
                        val requestBuilder = original.newBuilder()
                            .header("key", API_KEY)
                        val request = requestBuilder.build()
                        chain.proceed(request)
                    }
                    .build()
            )
            .build()
    }

    val weatherApiService: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }
}