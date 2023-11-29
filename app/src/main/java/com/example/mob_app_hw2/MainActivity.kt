package com.example.mob_app_hw2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mob_app_hw2.constants.CITY_LIST_ROUTE
import com.example.mob_app_hw2.constants.WELCOME_SCREEN_ROUTE
import com.example.mob_app_hw2.ui.theme.CityListScreen
import com.example.mob_app_hw2.ui.theme.SettingsScreen
import com.example.mob_app_hw2.ui.theme.WelcomeScreen
import com.example.mob_app_hw2.view.CityDetailsScreen
import com.example.mob_app_hw2.viewmodel.SettingsViewModel
import com.example.mob_app_hw2.viewmodel.WeatherApiViewModel
import com.example.mob_app_hw2.viewmodel.WeatherApiViewModelFactory


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }

    @Composable
    fun MyApp() {

        val navController = rememberNavController()

        val settingsViewModel: SettingsViewModel = viewModel()

        NavHost(
            navController = navController,
            startDestination = WELCOME_SCREEN_ROUTE
        ) {
            composable(WELCOME_SCREEN_ROUTE) {
                WelcomeScreen(navController, settingsViewModel)
            }
            composable(CITY_LIST_ROUTE) {
                CityListScreen(navController)
            }
            composable("cityDetails/{city}") { // Make sure the route includes the parameter
                    backStackEntry ->
                val city = backStackEntry.arguments?.getString("city")
                val viewModelFactory = WeatherApiViewModelFactory(city = city.toString())
                val viewModel = ViewModelProvider(ViewModelStore(), viewModelFactory)[WeatherApiViewModel::class.java]
                if (city != null) {
                    CityDetailsScreen(city = city, navController, viewModel, settingsViewModel)
                }
            }
            composable("settings") {
                SettingsScreen(navController, settingsViewModel)
            }
        }
    }
}