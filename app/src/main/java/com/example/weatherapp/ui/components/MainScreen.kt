package com.example.weatherapp.ui.components

import android.util.Log
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
import androidx.compose.runtime.mutableIntStateOf
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
import kotlin.Double
import kotlin.String

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


    var tempC by remember { mutableDoubleStateOf(0.toDouble()) }
    var condition by remember { mutableStateOf("Loading...") }
    var windMs by remember { mutableDoubleStateOf(0.toDouble()) }
    var humidity by remember { mutableIntStateOf(0) }
    var windDegree by remember { mutableIntStateOf(0) }
    var pressureMb by remember { mutableDoubleStateOf(0.toDouble()) }
    var precipMm by remember { mutableDoubleStateOf(0.toDouble()) }
    var cloud by remember { mutableIntStateOf(0) }
    var feelslikeC by remember { mutableDoubleStateOf(0.toDouble()) }
    var lastUpdated by remember { mutableStateOf("Loading...") }


    val shortInfo =
        "Погода: ${condition}\n" +
        "Ветер: $windMs м/с\n" +
        "Влажность: $humidity %"

    val fullInfo =
        shortInfo+ "\n" +
        "Направление ветра: $windDegree°\n" +
        "Давление: $pressureMb мбар\n" +
        "Осадки: $precipMm мм\n" +
        "Облачность: $cloud %\n" +
        "Ощущается как: $feelslikeC°C"


    var showFullInfo by remember { mutableStateOf(false) }
    var infoText by remember { mutableStateOf( value = shortInfo ) }
    var mainTemp by remember { mutableDoubleStateOf( value = tempC ) }

    infoText = if (showFullInfo) {fullInfo} else {shortInfo}

    LaunchedEffect(viewModel) {
        viewModel?.getLastWeather(database) { lastWeather ->
            if (lastWeather.isNotEmpty()) {
                windMs = "%.2f".format(Locale.US, lastWeather.last().windKph / 3.6).toDouble()
                tempC = lastWeather.last().tempC
                condition = lastWeather.last().condition
                humidity = lastWeather.last().humidity
                windDegree = lastWeather.last().windDegree
                pressureMb = lastWeather.last().pressureMb
                precipMm = lastWeather.last().precipMm
                cloud = lastWeather.last().cloud
                feelslikeC = lastWeather.last().feelslikeC
                lastUpdated = lastWeather.last().lastUpdated

                infoText = if (showFullInfo) fullInfo else shortInfo
                mainTemp = tempC
            }
        }

        viewModel?.getCurrentWeather(
            location = Constants.MOSCOW,
            responseLanguage = Constants.RU
        ) { receivedCurrentWeather ->
            windMs = "%.2f".format(Locale.US, receivedCurrentWeather.current.windKph / 3.6).toDouble()
            tempC = receivedCurrentWeather.current.tempC
            condition = receivedCurrentWeather.current.condition.text
            humidity = receivedCurrentWeather.current.humidity
            windDegree = receivedCurrentWeather.current.windDegree
            pressureMb = receivedCurrentWeather.current.pressureMb
            precipMm = receivedCurrentWeather.current.precipMm
            cloud = receivedCurrentWeather.current.cloud
            feelslikeC = receivedCurrentWeather.current.feelslikeC
            lastUpdated = receivedCurrentWeather.current.lastUpdated

            infoText = if (showFullInfo) fullInfo else shortInfo
            mainTemp = tempC

            viewModel.updateLastWeather(
                database = database,
                newWeather = Weather(
                    id = 0,
                    tempC = tempC,
                    condition = condition,
                    windKph = windMs * 3.6,
                    humidity = humidity,
                    windDegree = windDegree,
                    pressureMb = pressureMb,
                    precipMm = precipMm,
                    cloud = cloud,
                    feelslikeC = feelslikeC,
                    lastUpdated = lastUpdated,
                    windMs = windMs,
                )
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
                    text = "Данные актуальны на: $lastUpdated",
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