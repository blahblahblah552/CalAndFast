plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp") version "2.1.0-1.0.29"
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.example.calandfast"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.calandfast"
        minSdk = 33
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Import the Compose BOM
    implementation(platform(libs.androidx.compose.bom.v20241100))
    implementation(libs.androidx.activity.compose.v193)
    implementation(libs.material3)
    implementation(libs.ui)
    implementation(libs.ui.tooling)
    implementation(libs.ui.tooling.preview)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose.v284)

    //Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.core.ktx)
    ksp("androidx.room:room-compiler:${rootProject.extra["room_version"]}")
    implementation(libs.androidx.room.ktx)

    // Testing
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}