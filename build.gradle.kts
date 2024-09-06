plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.tales_app_sim"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.tales_app_sim"
        minSdk = 24
        targetSdk = 34
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
// Core Kotlin extensions for Android
    implementation("androidx.core:core-ktx:1.13.1")

// Lifecycle-aware components with Kotlin coroutines support
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")

// Jetpack Compose integration with activities
    implementation("androidx.activity:activity-compose:1.9.1")

// Bill of Materials (BOM) for Compose dependencies
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))

// Compose UI core library
    implementation("androidx.compose.ui:ui")

// Compose Graphics library for drawing and animations
    implementation("androidx.compose.ui:ui-graphics")

// Tooling support for Compose UI previews
    implementation("androidx.compose.ui:ui-tooling-preview")

// Material Design 3 components for Compose
    implementation("androidx.compose.material3:material3")

// JUnit for unit testing
    testImplementation("junit:junit:4.13.2")

// AndroidX Test extensions for JUnit
    androidTestImplementation("androidx.test.ext:junit:1.2.1")

// Espresso for UI testing
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

// BOM for Compose testing dependencies
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))

// Compose UI testing with JUnit 4
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

// Debug implementation of Compose UI tooling
    debugImplementation("androidx.compose.ui:ui-tooling")

// Debug implementation for Compose UI test manifest
    debugImplementation("androidx.compose.ui:ui-test-manifest")

// Routing
    implementation("androidx.navigation:navigation-compose:2.8.0")

// Lottie for Android
    implementation("com.airbnb.android:lottie-compose:6.4.0")
}