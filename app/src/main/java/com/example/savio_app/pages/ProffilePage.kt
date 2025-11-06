package com.example.savio_app.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.savio_app.R
import com.example.savio_app.data.local.SessionManager
import kotlinx.coroutines.launch

@Composable
fun ProfilePage(
    modifier: Modifier = Modifier,
    isDarkMode: Boolean,
    toggleTheme: () -> Unit,
    navController: NavController,
    sessionManager: SessionManager
) {
    val scrollState = rememberScrollState()

    val backgroundColor =
        if (isDarkMode) Color(0xFF121212) else Color(0xFFF1F1F1)
    val textColor = if (isDarkMode) Color.White else Color.Black
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val nombreNullable by sessionManager.nombreFlow.collectAsState(initial = null)
    val nombre = nombreNullable ?: "Usuario"

    val emailNullable by sessionManager.emailFlow.collectAsState(initial = null)
    val email = emailNullable ?: "correo@ejemplo.com"


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier.fillMaxSize(),
        bottomBar = { /* No agregamos la barra de navegación aquí */ }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_person_24),
                    contentDescription = "Perfil",
                    modifier = Modifier.size(70.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = nombre,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = email,
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Opciones Rápidas",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { /* Acción para editar perfil */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                ) {
                    Text("Editar Perfil")
                }

                Button(
                    onClick = { toggleTheme() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                ) {
                    Text(if (isDarkMode) "Modo Claro" else "Modo Oscuro")
                }

                Button(
                    onClick = {

                        navController.navigate("loginPage") {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                        coroutineScope.launch {
                            sessionManager.clearSession()
                            snackbarHostState.showSnackbar("Sesión cerrada correctamente")
                        }
                    }                    ,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                ) {
                    Text("Cerrar Sesión")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Configuración de la App",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { /* Acción para activar/desactivar notificaciones */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                ) {
                    Text("Notificaciones")
                }

                Button(
                    onClick = { /* Acción para cambiar idioma */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                ) {
                    Text("Idioma")
                }

                Button(
                    onClick = { /* Acción para configurar privacidad */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                ) {
                    Text("Privacidad")
                }

                Button(
                    onClick = { /* Acción para contactar con soporte */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                ) {
                    Text("Soporte")
                }
            }
        }
    }
}
