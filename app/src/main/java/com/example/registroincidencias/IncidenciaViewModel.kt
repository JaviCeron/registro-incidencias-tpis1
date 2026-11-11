package com.example.registroincidencias

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class IncidenciaViewModel : ViewModel() {

    val prioridades = listOf("Baja", "Media", "Alta")

    var titulo by mutableStateOf("")
        private set

    var descripcion by mutableStateOf("")
        private set

    var prioridad by mutableStateOf("Media")
        private set
    var mostrarError by mutableStateOf(false)
        private set
    val incidencias = mutableStateListOf<Incidencia>()
    fun actualizarTitulo(valor: String) {
        titulo = valor
        mostrarError = false
    }
    fun actualizarDescripcion(valor: String) {
        descripcion = valor
        mostrarError = false
    }

    fun seleccionarPrioridad(valor: String) {
        prioridad = valor
    }

    fun registrar() {
        if (titulo.isBlank() || descripcion.isBlank()) {
            mostrarError = true
        } else {
            incidencias.add(0, Incidencia(titulo, descripcion, prioridad)) // la más nueva primero
            limpiar()
        }
    }

    fun limpiar() {
        titulo = ""
        descripcion = ""
        prioridad = "Media"
        mostrarError = false
    }
}