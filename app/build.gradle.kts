plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.miapp.kairos24h"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.miapp.kairos24h"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Dependencias principales de Compose UI
    implementation("androidx.compose.ui:ui:1.5.1")  // Esta es la versión más reciente de Compose UI
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.1")  // Herramientas de previsualización
    implementation("androidx.compose.ui:ui-graphics:1.5.1")  // Para gráficos y renderizado de UI

    // Navegación Compose
    implementation("androidx.navigation:navigation-compose:2.5.3")

    // Dependencias de Material3 (ya estás usando la versión más reciente de material3)
    implementation(libs.androidx.material3)

    // Fused Location Provider (Google Play Services)
    implementation ("com.google.android.gms:play-services-location:18.0.0")

    // Dependencia explícita para Foundation
    implementation("androidx.compose.foundation:foundation:1.5.1")

    // Dependencias básicas y ciclo de vida
    implementation(libs.androidx.core.ktx)  // Utilidades de AndroidX
    implementation(libs.androidx.lifecycle.runtime.ktx)  // Para manejar ciclo de vida con Kotlin
    implementation(libs.androidx.activity.compose)  // Para trabajar con actividades y Compose

    // Dependencia para la versión BOM de Compose (esto asegura versiones consistentes)
    implementation(platform(libs.androidx.compose.bom))  // BOM para Compose (mantiene consistencia en las versiones)

    // Dependencias de pruebas y depuración
    debugImplementation("androidx.compose.ui:ui-tooling:1.5.1")  // Herramientas para depuración y previsualización en tiempo real
    debugImplementation(libs.androidx.ui.test.manifest)  // Manifesto de pruebas UI para depuración
    androidTestImplementation(libs.androidx.ui.test.junit4)  // Dependencias para pruebas en Android UI
}

