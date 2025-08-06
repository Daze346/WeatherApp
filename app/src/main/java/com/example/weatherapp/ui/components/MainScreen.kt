package com.example.weatherapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
import com.example.weatherapp.ViewModel
import com.example.weatherapp.constants.Constants
import com.example.weatherapp.data.local.database.AppDatabase
import com.example.weatherapp.data.local.entity.Weather
import com.example.weatherapp.ui.theme.LightBlack
import com.example.weatherapp.ui.theme.LightGray
import java.util.Locale

@Composable
fun MainScreen(
    viewModel: ViewModel?,
    database: AppDatabase
){

    SetSystemColor(
        systemBarsColor = LightBlack,
        statusBarColor = LightBlack,
        navigationBarColor = LightBlack,
        darkIcons = false
    )


    var currentWeather by remember { mutableStateOf(
        value = Weather(
            tempC = 0.toDouble(),
            condition = "Loading...",
            windKph = 0.toDouble(),
            windMs = 0.toDouble(),
            humidity = 0,
            windDegree = 0,
            pressureMb = 0.toDouble(),
            precipMm = 0.toDouble(),
            cloud = 0,
            feelslikeC = 0.toDouble(),
            lastUpdated = "Loading...",
        )
    ) }




    var shortInfo = getInfoTextValues(currentWeather = currentWeather, getFullInfo = false)

    var fullInfo = getInfoTextValues(currentWeather = currentWeather, getFullInfo = true)


    var showFullInfo by remember { mutableStateOf(false) }
    var infoText by remember { mutableStateOf( value = shortInfo ) }
    var mainTemp by remember { mutableDoubleStateOf( value = currentWeather.tempC ) }

    infoText = if (showFullInfo) {fullInfo} else {shortInfo}

    LaunchedEffect(viewModel) {
        viewModel?.getLastWeather(database) { lastWeatherList ->
            if (lastWeatherList.isNotEmpty()) {
                val lastWeather = lastWeatherList.last()
                currentWeather = lastWeather.copy()

                infoText = if (showFullInfo) fullInfo else shortInfo
                mainTemp = currentWeather.tempC
            }
        }

        viewModel?.getCurrentWeather(
            location = Constants.MOSCOW,
            responseLanguage = Constants.RU
        ) { receivedCurrentWeather ->
            currentWeather.windMs = "%.2f".format(Locale.US, receivedCurrentWeather.current.windKph / 3.6).toDouble()
            currentWeather.tempC = receivedCurrentWeather.current.tempC
            currentWeather.condition = receivedCurrentWeather.current.condition.text
            currentWeather.humidity = receivedCurrentWeather.current.humidity
            currentWeather.windDegree = receivedCurrentWeather.current.windDegree
            currentWeather.pressureMb = receivedCurrentWeather.current.pressureMb
            currentWeather.precipMm = receivedCurrentWeather.current.precipMm
            currentWeather.cloud = receivedCurrentWeather.current.cloud
            currentWeather.feelslikeC = receivedCurrentWeather.current.feelslikeC
            currentWeather.lastUpdated = receivedCurrentWeather.current.lastUpdated

            shortInfo = getInfoTextValues(currentWeather = currentWeather, getFullInfo = false)
            fullInfo = getInfoTextValues(currentWeather = currentWeather, getFullInfo = true)

            infoText = if (showFullInfo) fullInfo else shortInfo
            mainTemp = currentWeather.tempC

            viewModel.updateLastWeather(
                database = database,
                newWeather = currentWeather
            )
        }
    }


    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(LightBlack)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ){
                Image(
                    modifier = Modifier.padding(end = 15.dp),
                    painter = painterResource(R.drawable.ic_settings_white),
                    contentDescription = null
                )
            }

            Column(
                modifier = Modifier.padding(start = 5.dp)
            ) {
                Text(
                    text = Constants.MOSCOW,
                    fontSize = 25.sp,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.nunito_bold))
                )
                Text(
                    text = "$mainTemp°C",
                    fontSize = 70.sp,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.rubik_regular))
                )

                Text(
                    text = infoText,
                    fontSize = 20.sp,
                    color = LightGray,
                    fontFamily = FontFamily(Font(R.font.nunito_bold))
                )

                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    text = "Данные актуальны на: ${currentWeather.lastUpdated}",
                    fontSize = 15.sp,
                    color = LightGray,
                    fontFamily = FontFamily(Font(R.font.nunito_bold))
                )


                Text(
                    modifier = Modifier
                        .clickable(onClick = { showFullInfo = !showFullInfo }),
                    text = "More...",
                    fontSize = 20.sp,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.nunito_bold)),
                    fontWeight = FontWeight(900)
                )

            }

        }
    }

}

private fun getInfoTextValues(currentWeather: Weather, getFullInfo: Boolean): String{
    val shortInfo =
        "Погода: ${currentWeather.condition}\n" +
        "Ветер: ${currentWeather.windMs} м/с\n" +
        "Влажность: ${currentWeather.humidity} %"

    val fullInfo =
        shortInfo+ "\n" +
        "Направление ветра: ${currentWeather.windDegree}°\n" +
        "Давление: ${currentWeather.pressureMb} мбар\n" +
        "Осадки: ${currentWeather.precipMm} мм\n" +
        "Облачность: ${currentWeather.cloud} %\n" +
        "Ощущается как: ${currentWeather.feelslikeC}°C"

    return if (getFullInfo) fullInfo else shortInfo
}