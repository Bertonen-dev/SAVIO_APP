package com.example.savio_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.savio_app.data.local.SessionManager
import com.example.savio_app.data.model.ShoppingListRequest
import com.example.savio_app.data.network.createShoppingList
import com.example.savio_app.data.network.deleteShoppingList
import com.example.savio_app.data.network.getShoppingLists
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.savio_app.data.model.ShoppingListData


class ShoppingListViewModel(private val sessionManager: SessionManager) : ViewModel() {

    private val _creationResult = MutableStateFlow<String?>(null)
    val creationResult: StateFlow<String?> = _creationResult

    private val _shoppingLists = MutableStateFlow<List<ShoppingListData>>(emptyList())
    val shoppingLists: StateFlow<List<ShoppingListData>> = _shoppingLists

    fun createList(title: String) {
        viewModelScope.launch {
            val token = sessionManager.getToken()
            val userId = sessionManager.getIdUsuario()

            if (token != null && userId != null) {
                val request = ShoppingListRequest(titulo = title, idusuario = userId)
                val result = createShoppingList(token, request)

                result
                    .onSuccess {
                        _creationResult.value = "Lista creada: ${it.lista.titulo}"
                        loadShoppingLists() // Recarga listas después de crear una nueva
                    }
                    .onFailure { _creationResult.value = "Error: ${it.message}" }
            } else {
                _creationResult.value = "Token o usuario no disponible"
            }
        }
    }

    fun loadShoppingLists() {
        viewModelScope.launch {
            val token = sessionManager.getToken()

            if (token != null) {
                val result = getShoppingLists(token)

                result
                    .onSuccess { _shoppingLists.value = it }
                    .onFailure { _creationResult.value = "Error cargando listas: ${it.message}" }
            } else {
                _creationResult.value = "Token no disponible"
            }
        }
    }

    fun deleteList(id: Int) {
        viewModelScope.launch {
            val token = sessionManager.getToken()
            if (token != null) {
                val result = deleteShoppingList(token, id)

                result
                    .onSuccess {
                        _shoppingLists.value = _shoppingLists.value.filterNot { it.idlista == id }
                        _creationResult.value = "Lista eliminada correctamente"
                    }
                    .onFailure { _creationResult.value = "Error al eliminar: ${it.message}" }
            } else {
                _creationResult.value = "Token no disponible"
            }
        }
    }

}
