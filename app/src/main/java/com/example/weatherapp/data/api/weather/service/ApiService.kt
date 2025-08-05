package com.example.weatherapp.data.api.weather.service

import com.example.weatherapp.constants.Constants
import com.example.weatherapp.data.api.weather.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {


    @GET("v1/current.json")
    suspend fun getCurrentWeather(
        @Query("q") location: String,
        @Query("key") apiKey: String = Constants.API_KEY,
        @Query("lang") responseLanguage: String = Constants.EN
    ): WeatherResponse




}