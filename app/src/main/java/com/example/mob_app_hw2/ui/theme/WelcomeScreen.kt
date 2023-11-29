package com.example.mob_app_hw2.ui.theme

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mob_app_hw2.constants.CITY_LIST_ROUTE
import com.example.mob_app_hw2.constants.WELCOME_TEXT
import com.example.mob_app_hw2.model.TemperatureUnit
import com.example.mob_app_hw2.viewmodel.SettingsViewModel
import com.example.mob_app_hw2.viewmodel.WeatherApiViewModel
import com.example.mob_app_hw2.viewmodel.WeatherApiViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale

@Composable
fun WelcomeScreen(navController: NavHostController, settingsViewModel: SettingsViewModel = viewModel()) {
    val context = LocalContext.current
    var cityName by remember { mutableStateOf("") }
    var cityInfo by remember { mutableStateOf("") }

    val fusedLocationClient: FusedLocationProviderClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    // Use the requestPermission contract to request location permission
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                println("inside granted")
            } else {
                // Permission denied, handle accordingly
                // TODO: Add your logic for denied permission
            }
        }

    // Request location permission on app startup
    DisposableEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    // Use reverse geocoding to get the city name from the location
                    val geocoder = Geocoder(context, Locale.getDefault())
                    val addresses = geocoder.getFromLocation(
                        location.latitude,
                        location.longitude,
                        1
                    )

                    if (addresses != null) {
                        if (addresses.isNotEmpty()) {
                            cityName = addresses[0].locality
                            cityInfo = addresses[0].countryName + ", " + addresses[0].adminArea + ", " + cityName
                            // Do something with the city name
                        }
                    }
                }
            }
        } else {
            // Request location permission
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        // Dispose the effect to run only once on app startup
        onDispose { }
    }

    val viewModelFactory = WeatherApiViewModelFactory(city = cityName)
    val viewModel = ViewModelProvider(ViewModelStore(), viewModelFactory)[WeatherApiViewModel::class.java]
    val weatherInfo by viewModel.weatherInfo.observeAsState()
    val temp = weatherInfo?.current?.temp_c
    val convertedTemp = temp?.let { settingsViewModel.convertTemperature(it) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = WELCOME_TEXT,
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Get started by clicking the button below!",
            style = TextStyle(fontSize = 16.sp),
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                navController.navigate(CITY_LIST_ROUTE)
            }
        ) {
            Text(text = "Get Started")
        }
        if (convertedTemp != null) {
            val temperatureText = buildString {
                append("Temperature: ")
                append(convertedTemp)
                if (settingsViewModel.temperatureUnit.value == TemperatureUnit.FAHRENHEIT) {
                    append(" °F")
                } else {
                    append(" °C")
                }
            }
            Text(
                text = temperatureText,
                style = TextStyle(fontSize = 20.sp),
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
            )
        }

        // Show current location information
        CurrentLocationSection(cityInfo = cityInfo)

        Button(
            onClick = {
                navController.navigate("settings")
            }
        ) {
            Text(text = "Go to Settings")
        }
    }
}

@Composable
fun CurrentLocationSection(cityInfo: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Your Current Location:",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = cityInfo.takeIf { it.isNotBlank() } ?: "Location not available",
            style = TextStyle(fontSize = 16.sp),
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
        )
    }
}
