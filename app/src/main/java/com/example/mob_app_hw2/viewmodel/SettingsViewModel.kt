package com.example.mob_app_hw2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mob_app_hw2.model.TemperatureUnit

class SettingsViewModel : ViewModel() {
    private val _temperatureUnit = MutableLiveData<TemperatureUnit>()
    val temperatureUnit: LiveData<TemperatureUnit> get() = _temperatureUnit

    init {
        // Initialize with a default unit, e.g., Celsius
        _temperatureUnit.value = TemperatureUnit.CELSIUS
    }

    fun setTemperatureUnit(unit: TemperatureUnit) {
        _temperatureUnit.value = unit
    }

    fun convertTemperature(value: Double): Double {
        return when (_temperatureUnit.value) {
            TemperatureUnit.CELSIUS -> value
            TemperatureUnit.FAHRENHEIT -> (value * 9 / 5) + 32
            else -> {0.0}
        }
    }
}
