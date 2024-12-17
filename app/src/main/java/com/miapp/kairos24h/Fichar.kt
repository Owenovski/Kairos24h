package com.miapp.kairos24h

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun WelcomePage(usuario: String) {
    val context = LocalContext.current // Se obtiene el contexto aquí dentro del composable
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val horaEntrada = remember { mutableStateOf("") }
    val horaSalida = remember { mutableStateOf("") }
    val location = remember { mutableStateOf<Location?>(null) }

    // Función para obtener la hora actual con zona horaria correcta
    fun obtenerHoraActual(): String {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        val formatoHora = SimpleDateFormat("HH:mm", Locale.getDefault())
        formatoHora.timeZone = TimeZone.getDefault()
        return formatoHora.format(calendar.time)
    }

    // Función para obtener la ubicación actual del dispositivo
    fun obtenerUbicacion() {
        // Verificar si el permiso de ubicación está concedido
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        // Obtener la ubicación del dispositivo
        fusedLocationClient.lastLocation.addOnSuccessListener { locationResult: Location? ->
            location.value = locationResult
        }
    }

    // Función para manejar clic en Entrada
    fun onEntradaClick() {
        horaEntrada.value = "Has fichado a las ${obtenerHoraActual()}"
        obtenerUbicacion() // Obtener la ubicación cuando se hace clic en "Entrada"
    }

    // Función para manejar clic en Salida
    fun onSalidaClick() {
        horaSalida.value = "Has salido a las ${obtenerHoraActual()}"
        obtenerUbicacion() // Obtener la ubicación cuando se hace clic en "Salida"
    }

    // Función para abrir la ubicación en Google Maps
    fun abrirGoogleMaps(lat: Double, lon: Double) {
        val gmmIntentUri = "geo:$lat,$lon?q=$lat,$lon"
        val mapIntent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse(gmmIntentUri))
        mapIntent.setPackage("com.google.android.apps.maps")
        context.startActivity(mapIntent)
    }

    LaunchedEffect(Unit) {
        // Inicializa la ubicación solo si el usuario interactúa
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(10.dp, shape = RoundedCornerShape(4.dp), ambientColor = Color.Black, spotColor = Color.Black)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "logo",
                modifier = Modifier
                    .width(200.dp)
                    .height(60.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Texto de bienvenida
            Text(
                text = "¡Bienvenido $usuario!",
                color = Color.Black,
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Fila para los botones de entrada y salida
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Imagen para Fichar Entrada
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.entrada), // Cambia "entrada" por el ID correcto
                        contentDescription = "Entrada",
                        modifier = Modifier
                            .size(80.dp) // Tamaño reducido para ajustarse a la fila
                            .clickable { onEntradaClick() }
                    )
                    Text(
                        text = "Entrada",
                        color = Color(0xFF0A569A),
                        style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal),
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .offset(x = 4.dp)
                    )
                }

                // Imagen para Fichar Salida
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.salida), // Cambia "salida" por el ID correcto
                        contentDescription = "Salida",
                        modifier = Modifier
                            .size(80.dp) // Tamaño reducido para ajustarse a la fila
                            .clickable { onSalidaClick() }
                    )
                    Text(
                        text = "Salida",
                        color = Color(0xFF0A569A),
                        style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal),
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .offset(x = (-4).dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mostrar mensaje cuando se haga clic en Entrada
            if (horaEntrada.value.isNotEmpty()) {
                Text(
                    text = horaEntrada.value,
                    color = Color.Green,
                    modifier = Modifier.padding(8.dp),
                    style = TextStyle(fontSize = 14.sp)
                )

                // Mostrar ubicación debajo del mensaje de Entrada
                location.value?.let {
                    Text(
                        text = "Ubicación entrada: Lat: ${it.latitude}, Lon: ${it.longitude}",
                        color = Color.Black,
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { abrirGoogleMaps(it.latitude, it.longitude) } // Hacer clic en la ubicación para abrir Google Maps
                    )
                }
            }

            // Mostrar mensaje cuando se haga clic en Salida
            if (horaSalida.value.isNotEmpty()) {
                Text(
                    text = horaSalida.value,
                    color = Color.Red,
                    modifier = Modifier.padding(8.dp),
                    style = TextStyle(fontSize = 14.sp)
                )

                // Mostrar ubicación debajo del mensaje de Salida
                location.value?.let {
                    Text(
                        text = "Ubicación salida: Lat: ${it.latitude}, Lon: ${it.longitude}",
                        color = Color.Black,
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { abrirGoogleMaps(it.latitude, it.longitude) } // Hacer clic en la ubicación para abrir Google Maps
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomePagePreview() {
    // Para fines de preview, no es necesario pasar el FusedLocationProviderClient
    WelcomePage(usuario = "Juan Pérez")
}
