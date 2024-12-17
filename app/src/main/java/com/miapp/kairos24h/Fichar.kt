package com.miapp.kairos24h

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.material3.MaterialTheme
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun WelcomePage(usuario: String) {
    val horaEntrada = remember { mutableStateOf("") }
    val horaSalida = remember { mutableStateOf("") }

    // Función para obtener la hora actual con zona horaria correcta
    fun obtenerHoraActual(): String {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        val formatoHora = SimpleDateFormat("HH:mm", Locale.getDefault())
        formatoHora.timeZone = TimeZone.getDefault()
        return formatoHora.format(calendar.time)
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
                            .clickable {
                                horaEntrada.value = "Has fichado a las ${obtenerHoraActual()}"
                            }
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
                            .clickable {
                                horaSalida.value = "Has salido a las ${obtenerHoraActual()}"
                            }
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
            }

            // Mostrar mensaje cuando se haga clic en Salida
            if (horaSalida.value.isNotEmpty()) {
                Text(
                    text = horaSalida.value,
                    color = Color.Red,
                    modifier = Modifier.padding(8.dp),
                    style = TextStyle(fontSize = 14.sp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomePagePreview() {
    MaterialTheme {
        WelcomePage(usuario = "Juan Pérez")
    }
}
