package com.example.mob_app_hw2.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mob_app_hw2.constants.CITY_LIST_ROUTE
import com.example.mob_app_hw2.constants.DEFAULT_CITY_DESCRIPTION
import com.example.mob_app_hw2.data.cityDescriptionMap
import com.example.mob_app_hw2.data.cityImageMap
import com.example.mob_app_hw2.viewmodel.WeatherApiViewModel

@Composable
fun CityDetailsScreen(city: String, navController: NavHostController, viewModel: WeatherApiViewModel) {
    // For simplicity, we'll display a static description and image for demonstration purposes.
    val imageResource = cityImageMap[city]
    val cityDescription = cityDescriptionMap[city]
    val weatherInfo by viewModel.weatherInfo.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    navController.navigate(CITY_LIST_ROUTE)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back to City List",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "Welcome to $city",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (imageResource != null) {
            val imagePainter = painterResource(id = imageResource)
            Image(
                painter = imagePainter,
                contentDescription = "City Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (cityDescription != null) {
            Text(
                text = cityDescription,
                style = TextStyle(fontSize = 16.sp),
                color = MaterialTheme.colorScheme.onSurface
            )
        } else {
            Text(
                text = DEFAULT_CITY_DESCRIPTION,
                style = TextStyle(fontSize = 16.sp),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        if (weatherInfo != null) {
            val temp = weatherInfo!!.current.temp_c
            Text(
                text = "Temperature: $temp °C",
                style = TextStyle(fontSize = 20.sp),
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
            )
        }

        BackHandler {
            navController.popBackStack()
        }
    }
}