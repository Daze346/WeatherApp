package com.example.weatherapp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.constants.Constants
import com.example.weatherapp.data.api.weather.model.WeatherResponse
import com.example.weatherapp.data.api.weather.retrofitclient.RetrofitClient
import com.example.weatherapp.data.local.database.AppDatabase
import com.example.weatherapp.data.local.entity.Weather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewModel : ViewModel() {
    fun getCurrentWeather(
        location: String,
        responseLanguage: String = Constants.EN,
        setValues: (WeatherResponse) -> Unit,
    ) {
        viewModelScope.launch {
            try {
                val currentWeather = withContext(Dispatchers.IO) {
                    RetrofitClient.apiService.getCurrentWeather(
                        location = location,
                        responseLanguage = responseLanguage
                    )
                }
                setValues( currentWeather )
            } catch (e: Exception) {
                Log.e("!!!", "Error: ${e.message}")
            }
        }

    }

    fun updateLastWeather(
        database: AppDatabase,
        newWeather: Weather
    ) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    database.weatherDao().clearWeatherList()
                    database.weatherDao().insertWeather(newWeather)
                }
            } catch (e: Exception) {
                Log.e("!!!", "Error: ${e.message}")
            }
        }
    }
    fun getLastWeather(
        database: AppDatabase,
        setValues: (List<Weather>) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val lastWeatherFromDatabase = withContext(Dispatchers.IO) {
                    database.weatherDao().getLastWeather()
                }
                setValues(lastWeatherFromDatabase)
            } catch (e: Exception) {
                Log.e("!!!", "Error: ${e.message}")
            }
        }
    }
}