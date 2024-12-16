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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .shadow(10.dp, shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp), ambientColor = Color.Black, spotColor = Color.Black)
                .padding(45.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "logo",
                    modifier = Modifier
                        .width(300.dp)
                        .height(100.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = usuario.value,
                    onValueChange = { usuario.value = it },
                    label = { Text("Usuario") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    textStyle = TextStyle(color = Color.Black)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password.value,
                    onValueChange = { password.value = it },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    textStyle = TextStyle(color = Color.Black)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { onSubmit(usuario.value, password.value) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7599B6)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text("Acceso", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Enlace para "¿Olvidaste la contraseña?" alineado a la izquierda
                Text(
                    text = "¿Olvidaste la contraseña?",
                    color = Color(0xFF7599B6),
                    style = TextStyle(textDecoration = TextDecoration.Underline),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .align(Alignment.Start)
                        .clickable { onForgotPassword() }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Texto adicional debajo del enlace
                Text(
                    text = "Para control de calidad y aumentar la seguridad de nuestro sistema, todos las accesos, acciones, consultas o cambios (Trazabilidad) que realice dentro de Kairos24h serán almacenados.\nLes recordamos que la Empresa podrá auditar los medios técnicos que pone a disposición del Trabajador para el desempeño de sus funciones.",
                    color = Color(0xFF447094),  // Color del texto
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    style = TextStyle(fontSize = 12.sp)  // Tamaño de la fuente
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
