package com.miapp.kairos24h

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp  // Asegúrate de importar 'sp'
import androidx.compose.ui.platform.LocalConfiguration // Para detectar la orientación
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.text.input.ImeAction
import com.example.kairos24h.ui.theme.Kairos24hTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Kairos24hTheme {
                DisplayLogo(
                    onSubmit = { usuario, password ->
                        if (usuario.isNotEmpty() && password.isNotEmpty()) {
                            Toast.makeText(
                                this,
                                "Usuario: $usuario\nContraseña: $password",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                this,
                                "Por favor, completa ambos campos",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    onForgotPassword = {
                        val url =
                            "https://www.controlhorario.kairos24h.es/index.php?r=site/solicitudRestablecerClave"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                    }
                )
            }
        }
    }
}
//prueba
@Composable
fun DisplayLogo(
    onSubmit: (String, String) -> Unit,
    onForgotPassword: () -> Unit
) {
    val usuario = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    // Obtener configuración actual para detectar orientación
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .shadow(10.dp, shape = RoundedCornerShape(4.dp), ambientColor = Color.Black, spotColor = Color.Black)
                .padding(24.dp) // Reducción del padding
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // Ajustar el tamaño de la imagen
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "logo",
                    modifier = Modifier
                        .width(200.dp)  // Reducido tamaño de la imagen
                        .height(60.dp)  // Reducido tamaño de la imagen
                )

                Spacer(modifier = Modifier.height(8.dp)) // Reducir el espacio

                // Usuario Input
                OutlinedTextField(
                    value = usuario.value,
                    onValueChange = { usuario.value = it },
                    label = { Text("Usuario") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp), // Reducido padding
                    textStyle = TextStyle(color = Color.Black),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions.Default
                )

                Spacer(modifier = Modifier.height(8.dp)) // Reducir espacio

                // Contraseña Input
                OutlinedTextField(
                    value = password.value,
                    onValueChange = { password.value = it },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp), // Reducido padding
                    textStyle = TextStyle(color = Color.Black),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions.Default
                )

                Spacer(modifier = Modifier.height(16.dp)) // Espacio más pequeño

                // Botón de Acceso
                Button(
                    onClick = { onSubmit(usuario.value, password.value) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7599B6)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp) // Reducido padding
                        .height(45.dp) // Reducir altura del botón
                ) {
                    Text("Acceso", color = Color.White)
                }

                Spacer(modifier = Modifier.height(8.dp)) // Reducir espacio

                // Enlace de Olvido de Contraseña
                Text(
                    text = "¿Olvidaste la contraseña?",
                    color = Color(0xFF7599B6),
                    style = TextStyle(textDecoration = TextDecoration.Underline),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .align(Alignment.Start)
                        .clickable { onForgotPassword() }
                )

                Spacer(modifier = Modifier.height(8.dp)) // Reducir espacio

                // Texto adicional más compacto
                Text(
                    text = "Para control de calidad y aumentar la seguridad de nuestro sistema, todos los accesos, acciones, consultas o cambios (Trazabilidad) que realice dentro de Kairos24h serán almacenados.\nLes recordamos que la Empresa podrá auditar los medios técnicos que pone a disposición del Trabajador para el desempeño de sus funciones.",
                    color = Color(0xFF447094),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    style = TextStyle(fontSize = 10.sp)  // Reducir tamaño de fuente
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Kairos24hTheme {
        DisplayLogo(onSubmit = { _, _ -> }, onForgotPassword = {})
    }
}
