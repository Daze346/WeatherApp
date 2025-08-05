package com.example.weatherapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.weatherapp.data.local.entity.Weather

@Dao
interface WeatherDao {
    @Insert
    fun insertWeather(weather: Weather)

    @Query(value = "SELECT * FROM weather ORDER BY id DESC LIMIT 1")
    fun getLastWeather(): List<Weather>

    @Query(value = "DELETE FROM weather;")
    fun clearWeatherList()

}