package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.data.local.database.AppDatabase
import com.example.weatherapp.ui.components.MainScreen

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        viewModel = ViewModelProvider(this)[ViewModel::class.java]

        val database = AppDatabase.getDatabase(this)



        setContent {

            MainScreen(viewModel = viewModel, database = database)

        }
    }
}