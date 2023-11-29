package com.example.mob_app_hw2.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.example.mob_app_hw2.R
import com.example.mob_app_hw2.constants.WELCOME_SCREEN_ROUTE
import com.example.mob_app_hw2.model.TemperatureUnit
import com.example.mob_app_hw2.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(navController: NavHostController, settingsViewModel: SettingsViewModel) {
    val temperatureUnit by settingsViewModel.temperatureUnit.observeAsState()

    IconButton(
        onClick = {
            navController.navigate(WELCOME_SCREEN_ROUTE)
        }
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back to Welcome",
            tint = MaterialTheme.colorScheme.onSurface
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Settings",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Temperature Unit:",
            style = TextStyle(fontSize = 20.sp),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Celsius icon
            val celsiusIcon = painterResource(id = R.drawable.ic_celsius)
            Icon(
                painter = celsiusIcon,
                contentDescription = "Celsius",
                tint = if (temperatureUnit == TemperatureUnit.CELSIUS) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(8.dp))

            // Switch
            Switch(
                checked = temperatureUnit == TemperatureUnit.FAHRENHEIT,
                onCheckedChange = { checked ->
                    settingsViewModel.setTemperatureUnit(if (checked) TemperatureUnit.FAHRENHEIT else TemperatureUnit.CELSIUS)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                    uncheckedThumbColor = MaterialTheme.colorScheme.primary
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Fahrenheit icon
            val fahrenheitIcon = painterResource(id = R.drawable.ic_fahrenheit)
            Icon(
                painter = fahrenheitIcon,
                contentDescription = "Fahrenheit",
                tint = if (temperatureUnit == TemperatureUnit.FAHRENHEIT) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Print temperatureUnit for debugging
        temperatureUnit?.let {
            Text(
                text = "Temperature Unit: $it",
                style = TextStyle(fontSize = 16.sp),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}