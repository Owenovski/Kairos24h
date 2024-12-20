package com.miapp.kairos24h

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kairos24h.ui.theme.Kairos24hTheme

class MainActivity : ComponentActivity() {
    // Gestor de permisos
    private val requestLocationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Toast.makeText(this, "Permiso de ubicación concedido", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado. Es necesario para continuar", Toast.LENGTH_SHORT).show()
                solicitarPermisoUbicacion() // Volver a pedir permiso si se rechaza
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Kairos24hTheme {
                val navController = rememberNavController()

                LaunchedEffect(Unit) {
                    solicitarPermisoUbicacion() // Solicitar permisos al iniciar
                }

                NavHost(navController = navController, startDestination = "login") {
                    composable("login") {
                        DisplayLogo(
                            onSubmit = { usuario, password ->
                                if (usuario.isNotEmpty() && password.isNotEmpty()) {
                                    // Navegar a la pantalla de fichaje pasando ambos parámetros
                                    navController.navigate("fichar/$usuario/$password")
                                } else {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Por favor, completa ambos campos",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },
                            onForgotPassword = {
                                val url =
                                    "https://www.controlhorario.kairos24h.es/index.php?r=site/solicitudRestablecerClave"
                                startActivity(android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(url)))
                            },
                            navController = navController
                        )
                    }
                    composable("fichar/{usuario}/{password}") { backStackEntry ->
                        val usuario = backStackEntry.arguments?.getString("usuario") ?: ""
                        val password = backStackEntry.arguments?.getString("password") ?: ""
                        // Pasamos el navController a la pantalla de fichar
                        FicharScreen(usuario = usuario, password = password, navController = navController)
                    }
                }
            }
        }
    }

    // Función para solicitar permisos de ubicación
    private fun solicitarPermisoUbicacion() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                Toast.makeText(this, "Permiso de ubicación ya concedido", Toast.LENGTH_SHORT).show()
            }
            else -> {
                requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }
}

@Composable
fun DisplayLogo(
    onSubmit: (String, String) -> Unit,
    onForgotPassword: () -> Unit,
    navController: NavController
) {
    val usuario = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

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
                .shadow(10.dp, shape = RoundedCornerShape(4.dp))
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .width(200.dp)
                        .height(60.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = usuario.value,
                    onValueChange = { usuario.value = it },
                    label = { Text("Usuario") },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = password.value,
                    onValueChange = { password.value = it },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onSubmit(usuario.value, password.value) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7599B6)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Acceso", color = Color.White)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "¿Olvidaste la contraseña?",
                    color = Color(0xFF7599B6),
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { onForgotPassword() }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Kairos24hTheme {
        DisplayLogo(onSubmit = { _, _ -> }, onForgotPassword = {}, navController = rememberNavController())
    }
}
