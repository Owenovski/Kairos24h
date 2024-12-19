package com.miapp.kairos24h

import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.kairos24h.ui.theme.Kairos24hTheme
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.clickable



@Composable
fun FicharScreen(usuario: String, password: String, navController: NavController) {
    val url = "https://controlhorario.kairos24h.es/"
    var showError by remember { mutableStateOf(false) }
    var showButtons by remember { mutableStateOf(false) }

    // Mostrar alerta si el login falla
    if (showError) {
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(text = "Error de Login")
            },
            text = {
                Text(text = "Usuario o Password incorrectos")
            },
            confirmButton = {
                TextButton(onClick = {
                    // Volver al inicio de MainActivity
                    navController.popBackStack()
                }) {
                    Text("Entendido")
                }
            }
        )
    }

    // Pantalla principal de Fichar
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // WebView para mostrar el contenido de la URL
        WebViewContainer(
            url = url,
            usuario = usuario,
            password = password,
            navController = navController,
            onLoginError = { showError = true }, // Manejar el error de login
            onUserDetected = { showButtons = it } // Detectar cuando el usuario está presente
        )

        // Mostrar las imágenes solo cuando el usuario está detectado
        if (showButtons) {
            Surface(
                color = Color(android.graphics.Color.parseColor("#e2e4e5")),
                modifier = Modifier
                    .align(Alignment.BottomCenter)  // Alinea las imágenes en la parte inferior
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Imagen para Fichaje Entrada
                    Image(
                        painter = painterResource(id = R.drawable.entrada), // Cambia "entrada" por el nombre de la imagen
                        contentDescription = "entrada",
                        modifier = Modifier
                            .clickable { /* Acción de fichaje entrada */ }
                            .size(100.dp) // Ajusta el tamaño de la imagen
                    )

                    // Imagen para Fichaje Salida
                    Image(
                        painter = painterResource(id = R.drawable.salida), // Cambia "salida" por el nombre de la imagen
                        contentDescription = "salida",
                        modifier = Modifier
                            .clickable { /* Acción de fichaje salida */ }
                            .size(100.dp) // Ajusta el tamaño de la imagen
                    )
                }
            }
        }
    }
}

@Composable
fun WebViewContainer(
    url: String,
    usuario: String,
    password: String,
    navController: NavController,
    onLoginError: (Boolean) -> Unit,
    onUserDetected: (Boolean) -> Unit
) {
    val context = LocalContext.current
    var loginAttempted by remember { mutableStateOf(false) } // Control para evitar reintentos
    var isMonitoringError by remember { mutableStateOf(false) } // Control para evitar múltiples revisiones
    var isMonitoringUser by remember { mutableStateOf(false) } // Control para evitar múltiples revisiones del usuario

    AndroidView(
        factory = {
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                        val url = request?.url.toString()

                        if (url.contains("/index.php?r=site/logout")) {
                            // Limpiar datos de navegación y cookies
                            view?.apply {
                                clearHistory()
                                clearCache(true)
                                clearFormData()
                            }

                            // Limpiar cookies globalmente
                            android.webkit.CookieManager.getInstance().apply {
                                removeAllCookies(null)
                                flush()
                            }

                            // Si se hace clic en "Salir", navega a MainActivity
                            navController.popBackStack()
                            return true
                        }

                        return super.shouldOverrideUrlLoading(view, request)
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)

                        // Si no se ha intentado login previamente, ejecutar la automatización una sola vez
                        if (!loginAttempted) {
                            val script = """
                                document.getElementById('LoginForm_username').value = '$usuario';
                                document.getElementById('LoginForm_password').value = '$password';
                                var button = document.querySelector('input[type="submit"][value="Acceso"]');
                                if (button) {
                                    button.click();
                                }
                            """
                            view?.evaluateJavascript(script, null)
                            loginAttempted = true // Marcar que ya se intentó el login
                        }

                        // Iniciar monitoreo del DOM para detectar al usuario
                        if (!isMonitoringUser) {
                            isMonitoringUser = true
                            monitorUser(view, onUserDetected)
                        }

                        // Iniciar monitoreo periódico para detectar el mensaje de error
                        if (!isMonitoringError) {
                            isMonitoringError = true
                            monitorError(view, onLoginError)
                        }
                    }
                }
                loadUrl(url)
            }
        },
        modifier = Modifier
            .fillMaxSize() // Eliminar padding para usar todo el espacio disponible
    )
}

/**
 * Monitorea periódicamente el DOM para detectar al usuario.
 */
fun monitorUser(view: WebView?, onUserDetected: (Boolean) -> Unit) {
    view?.postDelayed({
        view.evaluateJavascript(""" 
            (function() {
                var userElement = document.querySelector('#dUsuarioHeader');
                return userElement ? 'found' : 'not_found';
            })();
        """) { result ->
            if (result.contains("found")) {
                onUserDetected(true) // Usuario detectado, mostrar botones
            } else {
                onUserDetected(false) // Usuario no detectado, ocultar botones
                // Continuar monitoreando si no se encuentra al usuario
                monitorUser(view, onUserDetected)
            }
        }
    }, 1000) // Reintentar cada segundo
}

/**
 * Monitorea periódicamente el DOM para detectar el mensaje de error.
 */
fun monitorError(view: WebView?, onLoginError: (Boolean) -> Unit) {
    view?.postDelayed({
        view.evaluateJavascript(""" 
            (function() {
                var errorElement = document.querySelector('#LoginForm_password_em_');
                return errorElement ? 'error' : 'success';
            })();
        """) { result ->
            if (result.contains("error")) {
                onLoginError(true) // Activar el error
            } else {
                // Si no se detectó el error, continuar monitoreando
                monitorError(view, onLoginError)
            }
        }
    }, 1000) // Reintentar cada segundo
}

@Preview(showBackground = true)
@Composable
fun FicharPreview() {
    Kairos24hTheme {
        // Usamos un NavController ficticio para la vista previa
        FicharScreen(usuario = "UsuarioEjemplo", password = "ContraseñaEjemplo", navController = rememberNavController())
    }
}
