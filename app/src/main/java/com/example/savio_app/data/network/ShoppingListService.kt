package com.example.savio_app.data.network

import com.example.savio_app.data.model.ShoppingListData
import com.example.savio_app.data.model.ShoppingListRequest
import com.example.savio_app.data.model.ShoppingListListResponse
import com.example.savio_app.data.model.ShoppingListResponse
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import com.example.savio_app.data.network.HttpClientProvider.client as httpClient

suspend fun createShoppingList(token: String, request: ShoppingListRequest): Result<ShoppingListResponse> {
    return try {
        val response: HttpResponse = httpClient.post("https://savio-api.onrender.com/listas_compras") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
                contentType(ContentType.Application.Json)
            }
            setBody(request)
        }

        when (response.status) {
            HttpStatusCode.Created -> {
                val listResponse = response.body<ShoppingListResponse>()
                Result.success(listResponse)
            }
            else -> {
                val errorBody = response.bodyAsText()
                Result.failure(Exception("Error ${response.status.value}: $errorBody"))
            }
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}

suspend fun getShoppingLists(token: String): Result<List<ShoppingListData>> {
    return try {
        val response: HttpResponse = httpClient.get("https://savio-api.onrender.com/listas_compras") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
                accept(ContentType.Application.Json)
            }
        }

        when (response.status) {
            HttpStatusCode.OK -> {
                val listResponse = response.body<ShoppingListListResponse>()
                Result.success(listResponse.listas)
            }
            else -> {
                val errorBody = response.bodyAsText()
                Result.failure(Exception("Error ${response.status.value}: $errorBody"))
            }
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}

@Serializable
data class ProductoLista(
    val idproducto: Int,
    val nombre_producto: String,
    val cantidad: String,
    val comprado: Boolean,
    val idlista: Int,
    val idusuario: String
)

@Serializable
data class ShoppingListWithProducts(
    val idlista: Int,
    val titulo: String,
    val productos: List<ProductoLista>
)

@Serializable
data class ShoppingListWithProductos(
    val idlista: Int,
    val titulo: String,
    val productos: List<ProductoLista>
)

@Serializable
data class ShoppingListListWithProductosResponse(
    val listas: List<ShoppingListWithProductos>
)

suspend fun getProductosDeLista(token: String, idlista: Int): Result<List<ProductoLista>> {
    return try {
        val response: HttpResponse = httpClient.get("https://savio-api.onrender.com/listas_compras") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
                accept(ContentType.Application.Json)
            }
        }
        if (response.status == HttpStatusCode.OK) {
            val listResponse = response.body<ShoppingListListWithProductosResponse>()
            val lista = listResponse.listas.find { it.idlista == idlista }
            Result.success(lista?.productos ?: emptyList())
        } else {
            val errorBody = response.bodyAsText()
            Result.failure(Exception("Error ${response.status.value}: $errorBody"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}

@Serializable
data class AddProductoRequest(
    val idlista: Int,
    val nombre_producto: String,
    val cantidad: String
)

suspend fun addProductoALista(token: String, idlista: Int, nombre: String, cantidad: String): Result<ProductoLista> {
    return try {
        val response: HttpResponse = httpClient.post("https://savio-api.onrender.com/productos_lista") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
                contentType(ContentType.Application.Json)
            }
            setBody(AddProductoRequest(
                idlista = idlista,
                nombre_producto = nombre,
                cantidad = cantidad
            ))
        }

        when (response.status) {
            HttpStatusCode.Created -> {
                val responseBody = response.body<JsonObject>()
                val producto = responseBody["producto"]?.jsonObject
                if (producto != null) {
                    Result.success(
                        ProductoLista(
                            idproducto = producto["idproducto"]?.jsonPrimitive?.content?.toIntOrNull() ?: 0,
                            nombre_producto = producto["nombre_producto"]?.jsonPrimitive?.content ?: "",
                            cantidad = producto["cantidad"]?.jsonPrimitive?.content ?: "",
                            comprado = producto["comprado"]?.jsonPrimitive?.content?.toBoolean() ?: false,
                            idlista = producto["idlista"]?.jsonPrimitive?.content?.toIntOrNull() ?: 0,
                            idusuario = producto["idusuario"]?.jsonPrimitive?.content ?: ""
                        )
                    )
                } else {
                    Result.failure(Exception("Error: Respuesta inválida del servidor"))
                }
            }
            else -> {
                val errorBody = response.bodyAsText()
                Result.failure(Exception("Error ${response.status.value}: $errorBody"))
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }
}

suspend fun updateProductoComprado(token: String, idproducto: Int, comprado: Boolean): Result<Unit> {
    return try {
        val response: HttpResponse = httpClient.put("https://savio-api.onrender.com/productos_lista/$idproducto") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
                contentType(ContentType.Application.Json)
            }
            setBody(mapOf("comprado" to comprado))
        }
        if (response.status == HttpStatusCode.OK) {
            Result.success(Unit)
        } else {
            val errorBody = response.bodyAsText()
            Result.failure(Exception("Error ${response.status.value}: $errorBody"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}

suspend fun deleteProducto(token: String, idproducto: Int): Result<Unit> {
    return try {
        val response: HttpResponse = httpClient.delete("https://savio-api.onrender.com/productos_lista/$idproducto") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
        if (response.status == HttpStatusCode.OK) {
            Result.success(Unit)
        } else {
            val errorBody = response.bodyAsText()
            Result.failure(Exception("Error ${response.status.value}: $errorBody"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}