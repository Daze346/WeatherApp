package com.example.weatherapp.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun SetSystemColor(
    systemBarsColor: Color = Color.White,
    statusBarColor: Color = Color.White,
    navigationBarColor: Color = Color.White,
    darkIcons: Boolean = true
){
    val systemUiController = rememberSystemUiController()


    SideEffect {
        systemUiController.setSystemBarsColor(
            color = systemBarsColor,
            darkIcons = darkIcons
        )

        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = darkIcons
        )

        systemUiController.setNavigationBarColor(
            color = navigationBarColor,
            darkIcons = darkIcons
        )
    }
}