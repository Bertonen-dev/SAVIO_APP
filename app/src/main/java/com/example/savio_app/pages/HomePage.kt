package com.example.savio_app.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun HomePage(
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InfoCard(
                title = "Próxima Pastilla",
                titleColor = MaterialTheme.colorScheme.primary,
                content = "Toma la pastilla 'Ibuprofeno' a las 15:00"
            )

            InfoCard(
                title = "Hidratación",
                titleColor = MaterialTheme.colorScheme.secondary,
                content = "Bebido hasta ahora: 500 ml\nMeta de hoy: 2L"
            )

            InfoCard(
                title = "El Tiempo",
                titleColor = MaterialTheme.colorScheme.tertiary,
                content = "Ciudad: Madrid\nTemperatura: 22°C\nCondiciones: Soleado"
            )

            InfoCard(
                title = "To-Do List",
                titleColor = MaterialTheme.colorScheme.primary,
                content = "1. Llamar a Juan\n2. Comprar alimentos\n3. Enviar informe de trabajo"
            )

            InfoCard(
                title = "Agenda de Eventos",
                titleColor = MaterialTheme.colorScheme.secondary,
                content = "1. Reunión de trabajo a las 10:00 AM\n2. Cena con amigos a las 8:00 PM"
            )

            InfoCard(
                title = "Notas Rápidas",
                titleColor = MaterialTheme.colorScheme.primary,
                content = "1. Recordar comprar leche\n2. Llamar al dentista"
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun InfoCard(title: String, titleColor: androidx.compose.ui.graphics.Color, content: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shadowElevation = 12.dp,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = titleColor
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
