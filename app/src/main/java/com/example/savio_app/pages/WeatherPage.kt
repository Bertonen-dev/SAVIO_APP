package com.example.savio_app.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.savio_app.weather.WeatherResponse
import com.example.savio_app.weather.WeatherService
import kotlinx.coroutines.launch

@Composable
fun WeatherPage() {
    var city by remember { mutableStateOf("") }
    var weatherInfo by remember { mutableStateOf<WeatherResponse?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("Introduce una ciudad") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(
                    onClick = {
                        scope.launch {
                            isLoading = true
                            errorMessage = null
                            weatherInfo = null
                            val result = WeatherService.getWeatherByCity(city)
                            if (result != null) {
                                weatherInfo = result
                            } else {
                                errorMessage = "No se pudo obtener el clima"
                            }
                            isLoading = false
                        }
                    }
                ) {
                    Icon(Icons.Default.Search, contentDescription = "Buscar")
                }
            }
        )

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        errorMessage?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }

        weatherInfo?.let { weather ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Ciudad: ${weather.name}", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(12.dp))
                    WeatherRow(Icons.Default.Thermostat, "Temperatura", "${weather.main.temp} °C")
                    WeatherRow(Icons.Default.WaterDrop, "Humedad", "${weather.main.humidity} %")
                    WeatherRow(Icons.Default.Air, "Viento", "${weather.wind.speed} m/s")
                    WeatherRow(Icons.Default.Info, "Descripción", weather.weather.firstOrNull()?.description ?: "Sin descripción")
                }
            }
        }
    }
}

@Composable
fun WeatherRow(icon: ImageVector, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 6.dp)
    ) {
        Icon(icon, contentDescription = label, modifier = Modifier.size(28.dp))
        Spacer(modifier = Modifier.width(14.dp))
        Text(text = "$label: $value", style = MaterialTheme.typography.bodyLarge)
    }
}
