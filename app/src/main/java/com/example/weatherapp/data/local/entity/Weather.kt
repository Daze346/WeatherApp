package com.example.weatherapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class Weather(
    @PrimaryKey (autoGenerate = true) val id: Long = 0,

    val tempC: Double,
    val condition: String,
    val windKph: Double,
    val humidity: Int,
    val windDegree: Int,
    val pressureMb: Double,
    val precipMm: Double,
    val cloud: Int,
    val feelslikeC: Double,
    val lastUpdated: String,
    val windMs: Double,
)