package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.data.local.database.AppDatabase
import com.example.weatherapp.ui.components.MainScreen
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        viewModel = ViewModelProvider(this)[ViewModel::class.java]

        val database = AppDatabase.getDatabase(this)



        setContent {

            MainScreen(viewModel, database)

        }
    }
}





//@Composable
//@Preview(showBackground = true, showSystemUi = true)
//fun PreviewMainScreen(){ MainScreen(null) }