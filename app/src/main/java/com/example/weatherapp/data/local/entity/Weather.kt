package com.example.weatherapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class Weather(
    @PrimaryKey (autoGenerate = true) val id: Long = 0,

    var tempC: Double,
    var condition: String,
    var windKph: Double,
    var humidity: Int,
    var windDegree: Int,
    var pressureMb: Double,
    var precipMm: Double,
    var cloud: Int,
    var feelslikeC: Double,
    var lastUpdated: String,
    var windMs: Double,
)