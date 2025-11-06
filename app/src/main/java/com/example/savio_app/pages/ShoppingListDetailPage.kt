package com.example.savio_app.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.example.savio_app.data.network.ProductoLista
import com.example.savio_app.data.network.getProductosDeLista
import com.example.savio_app.data.network.addProductoALista
import com.example.savio_app.data.network.updateProductoComprado
import com.example.savio_app.data.network.deleteProducto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListDetailPage(
    idlista: Int,
    listTitle: String,
    token: String,
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var productos by remember { mutableStateOf<List<ProductoLista>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var showAddDialog by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    fun cargarProductos() {
        loading = true
        errorMsg = null
        scope.launch {
            try {
                if (token.isEmpty()) {
                    errorMsg = "Error: No hay sesión activa"
                    loading = false
                    return@launch
                }

                val result = getProductosDeLista(token, idlista)
                result.onSuccess {
                    productos = it
                    loading = false
                    errorMsg = null
                }.onFailure { e ->
                    errorMsg = when {
                        e.message?.contains("401") == true -> "Sesión expirada. Por favor, vuelve a iniciar sesión"
                        e.message?.contains("404") == true -> "No se encontraron productos en esta lista"
                        else -> "Error al cargar productos: ${e.message}"
                    }
                    println("Error en cargarProductos: ${e.message}")
                    e.printStackTrace()
                    loading = false
                }
            } catch (e: Exception) {
                errorMsg = "Error inesperado al cargar productos"
                println("Excepción en cargarProductos: ${e.message}")
                e.printStackTrace()
                loading = false
            }
        }
    }

    LaunchedEffect(idlista) {
        cargarProductos()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = listTitle) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Añadir producto")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                errorMsg != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = errorMsg ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        if (errorMsg?.contains("Sesión expirada") == true) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = onBack) {
                                Text("Volver")
                            }
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(productos, key = { it.idproducto }) { producto ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(4.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = producto.comprado,
                                        onCheckedChange = { checked ->
                                            scope.launch {
                                                updateProductoComprado(token, producto.idproducto, checked)
                                                cargarProductos()
                                            }
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = producto.nombre_producto, style = MaterialTheme.typography.titleMedium)
                                        Text(text = "Cantidad: ${producto.cantidad}", style = MaterialTheme.typography.bodySmall)
                                    }
                                    IconButton(onClick = {
                                        scope.launch {
                                            deleteProducto(token, producto.idproducto)
                                            cargarProductos()
                                        }
                                    }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Eliminar producto")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddProductDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { name, qty ->
                scope.launch {
                    addProductoALista(token, idlista, name, qty)
                    cargarProductos()
                }
                showAddDialog = false
            }
        )
    }
}

@Composable
fun AddProductDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var qty by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Añadir producto") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre del producto") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = qty,
                    onValueChange = { qty = it },
                    label = { Text("Cantidad") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank() && qty.isNotBlank()) {
                        onAdd(name, qty)
                    }
                }
            ) {
                Text("Añadir")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}