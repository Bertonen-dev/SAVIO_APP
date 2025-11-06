package com.example.savio_app.pages

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppPage(
    navController: NavController,
    isDarkMode: Boolean,
    toggleTheme: () -> Unit

) {
    val backgroundColor = if (isDarkMode) Color(0xFF121212) else Color(0xFFF2F2F2)
    val contentColor = if (isDarkMode) Color.White else Color.Black
    val surfaceColor = if (isDarkMode) Color(0xFF1E1E1E) else Color.White
    val searchQuery = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Funciones de la Aplicación",
            style = MaterialTheme.typography.headlineMedium,
            color = contentColor,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
                .padding(bottom = 16.dp)
                .wrapContentWidth(Alignment.CenterHorizontally)
        )

        OutlinedTextField(
            value = searchQuery.value,
            onValueChange = { searchQuery.value = it },
            label = { Text("Buscar función") },
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Buscar") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = MaterialTheme.colorScheme.primary
            )
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            MenuButton(
                texto = "Notas",
                icono = Icons.Default.Edit,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .padding(8.dp),
                contentColor = contentColor,
                surfaceColor = surfaceColor,
                onClick = { navController.navigate("createNotePage") }
            )
            MenuButton(
                texto = "Eventos",
                icono = Icons.Default.DateRange,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .padding(8.dp),
                contentColor = contentColor,
                surfaceColor = surfaceColor,
                onClick = { navController.navigate("eventListPage") }
            )
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            MenuButton(
                texto = "To-Do",
                icono = Icons.Default.Check,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .padding(8.dp),
                contentColor = contentColor,
                surfaceColor = surfaceColor,
                onClick = { navController.navigate("todoPage") }
            )

            MenuButton(
                texto = "Lista de Compras",
                icono = Icons.Default.ShoppingCart,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .padding(8.dp),
                contentColor = contentColor,
                surfaceColor = surfaceColor,
                onClick = { navController.navigate("shoppingListPage") }
            )
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            MenuButton(
                texto = "Clima",
                icono = Icons.Default.WbSunny,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .padding(8.dp),
                contentColor = contentColor,
                surfaceColor = surfaceColor,
                onClick = { navController.navigate("weatherPage") }
            )

            MenuButton(
                texto = "SAVIO",
                icono = Icons.Default.ChatBubbleOutline,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .padding(8.dp),
                contentColor = contentColor,
                surfaceColor = surfaceColor,
                onClick = { navController.navigate("chatPage") }
            )

        }
    }
}

@Composable
fun MenuButton(
    texto: String,
    icono: ImageVector,
    modifier: Modifier = Modifier,
    contentColor: Color,
    surfaceColor: Color,
    onClick: () -> Unit = {}
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = surfaceColor,
        tonalElevation = 8.dp,
        modifier = modifier
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icono,
                contentDescription = texto,
                tint = contentColor,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = texto,
                color = contentColor,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
        }
    }
}
