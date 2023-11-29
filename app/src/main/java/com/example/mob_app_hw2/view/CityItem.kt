package com.example.mob_app_hw2.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun CityItem(city: String, navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("cityDetails/$city")
            }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .offset(y = 30.dp) // Add a vertical offset to move the city item down
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "City",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = city,
                style = TextStyle(fontSize = 20.sp),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}