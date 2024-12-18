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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.compose.rememberNavController
import com.example.kairos24h.ui.theme.Kairos24hTheme

@Composable
fun FicharScreen(usuario: String, password: String, navController: NavController) {
    val url = "https://controlhorario.kairos24h.es/"

    // Pantalla principal de Fichar
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // WebView para mostrar el contenido de la URL
        WebViewContainer(url = url, usuario = usuario, password = password, navController = navController)
    }
}

@Composable
fun WebViewContainer(url: String, usuario: String, password: String, navController: NavController) {
    val context = LocalContext.current

    AndroidView(
        factory = {
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = object : WebViewClient() {
                    // Sobrescribir el método para interceptar las URLs cargadas
                    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                        val url = request?.url.toString()

                        // Verificar si la URL es la de "Salir"
                        if (url.contains("/index.php?r=site/logout")) {
                            // Si se hace clic en "Salir", navega a MainActivity
                            navController.popBackStack() // Si estás usando NavController
                            // También puedes usar un Intent para volver a MainActivity
                            // val intent = Intent(context, MainActivity::class.java)
                            // context.startActivity(intent)
                            return true  // No cargar la URL en el WebView
                        }

                        return super.shouldOverrideUrlLoading(view, request)
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)

                        // Inyectar JavaScript después de que la página haya cargado completamente
                        val script = """
                            document.getElementById('LoginForm_username').value = '$usuario';
                            document.getElementById('LoginForm_password').value = '$password';
                            // Simular el clic en el botón "Acceso"
                            var button = document.querySelector('input[type="submit"][value="Acceso"]');
                            if (button) {
                                button.click();
                            }
                        """
                        // Ejecutar el JavaScript
                        view?.evaluateJavascript(script, null)
                    }
                }
                loadUrl(url)
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun FicharPreview() {
    Kairos24hTheme {
        FicharScreen(usuario = "UsuarioEjemplo", password = "ContraseñaEjemplo", navController = rememberNavController()) // Corregido aquí
    }
}
