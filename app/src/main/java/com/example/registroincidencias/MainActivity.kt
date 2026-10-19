package com.example.registroincidencias

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.registroincidencias.ui.theme.RegistroIncidenciasTheme
import kotlin.math.sqrt

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RegistroIncidenciasTheme {
                RegistroIncidenciasApp()
            }
        }
    }
}

// Representa una incidencia registrada
data class Incidencia(
    val titulo: String,
    val descripcion: String
)

@Composable
fun RegistroIncidenciasApp() {

    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    // Lista de TODAS las incidencias registradas (en memoria, sin base de datos aún)
    val incidencias = remember { mutableStateListOf<Incidencia>() }

    // Mensaje de error simple si intentan registrar campos vacíos
    var mostrarError by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val sensorManager = remember {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
    val acelerometro = remember {
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    val lifecycleOwner = context as ComponentActivity

    var ejeX by remember { mutableStateOf(0f) }
    var ejeY by remember { mutableStateOf(0f) }
    var ejeZ by remember { mutableStateOf(0f) }
    var hayLectura by remember { mutableStateOf(false) }
    var escuchando by remember { mutableStateOf(false) }

    DisposableEffect(acelerometro, lifecycleOwner) {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                ejeX = event.values[0]
                ejeY = event.values[1]
                ejeZ = event.values[2]
                hayLectura = true
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
        val observer = LifecycleEventObserver { _, evento ->
            if (acelerometro != null) {
                when (evento) {
                    Lifecycle.Event.ON_RESUME -> {
                        sensorManager.registerListener(
                            listener,
                            acelerometro,
                            SensorManager.SENSOR_DELAY_NORMAL
                        )
                        escuchando = true
                    }
                    Lifecycle.Event.ON_PAUSE -> {
                        sensorManager.unregisterListener(listener)
                        escuchando = false
                    }
                    else -> {}
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            sensorManager.unregisterListener(listener)
            escuchando = false
        }
    }

    val magnitud = sqrt(ejeX * ejeX + ejeY * ejeY + ejeZ * ejeZ)
    val mensajeSensor = when {
        !hayLectura -> "Sin lectura disponible aún"
        magnitud > 15f -> "Movimiento fuerte detectado"
        magnitud > 11f || magnitud < 8.5f -> "Movimiento moderado"
        else -> "Dispositivo en reposo"
    }

    val coloresCampo = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black,
        focusedBorderColor = Color.Black,
        unfocusedBorderColor = Color.Gray,
        focusedLabelColor = Color.Black,
        unfocusedLabelColor = Color.DarkGray,
        cursorColor = Color.Black
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Registro de Incidencias",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Reporta problemas relacionados con equipos, " +
                    "infraestructura o servicios."
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "Diagnóstico del dispositivo",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                if (acelerometro == null) {
                    Text(text = "Acelerómetro: no disponible")
                    Text(
                        text = "Este dispositivo no tiene acelerómetro, no hay lectura disponible.",
                        color = Color(0xFFC62828)
                    )
                } else {
                    Text(text = "Acelerómetro: disponible")
                    Text(text = "Escucha: " + if (escuchando) "activa" else "detenida")
                    Text(text = "X: %.2f   Y: %.2f   Z: %.2f".format(ejeX, ejeY, ejeZ))
                    Text(text = mensajeSensor, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = titulo,
            onValueChange = {
                titulo = it
                mostrarError = false
            },
            label = { Text("Título de la incidencia") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = coloresCampo
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = descripcion,
            onValueChange = {
                descripcion = it
                mostrarError = false
            },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth(),
            colors = coloresCampo
        )

        if (mostrarError) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Completa el título y la descripción antes de registrar.",
                color = Color(0xFFC62828),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (titulo.isBlank() || descripcion.isBlank()) {
                    mostrarError = true
                } else {
                    incidencias.add(0, Incidencia(titulo, descripcion)) // la más nueva primero
                    titulo = ""
                    descripcion = ""
                    mostrarError = false
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Registrar incidencia")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Incidencias registradas (${incidencias.size})",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (incidencias.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
            ) {
                Text(
                    text = "Aún no hay incidencias registradas",
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(incidencias) { incidencia ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = incidencia.titulo,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = incidencia.descripcion,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Prototipo inicial — Unidad 1",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegistroIncidenciasPreview() {
    RegistroIncidenciasTheme {
        RegistroIncidenciasApp()
    }
}