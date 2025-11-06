package com.example.savio_app.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.savio_app.R
import com.example.savio_app.data.local.SessionManager
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    val tokenState = sessionManager.tokenFlow.collectAsState(initial = null)

    LaunchedEffect(tokenState.value) {
        delay(2000L)
        if (tokenState.value != null) {
            navController.navigate("homePage") {
                popUpTo("splashScreen") { inclusive = true }
            }
        } else {
            navController.navigate("loginPage") {
                popUpTo("splashScreen") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.savio_logo_text),
                contentDescription = "Logo de Savio",
                modifier = Modifier
                    .size(200.dp) // Ajusta el tamaño según lo necesites
            )

            Spacer(modifier = Modifier.height(24.dp))

            CircularProgressIndicator()
        }
    }
}
