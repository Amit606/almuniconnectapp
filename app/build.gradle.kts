plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.firebase.crashlytics")
    id("kotlin-parcelize")


    id("com.google.gms.google-services") // ✅ MUST

}

android {
    namespace = "com.kwh.almuniconnect"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.kwh.almuniconnect"
        minSdk = 24
        targetSdk = 35
        versionCode = 14
        versionName = "1.1.4"

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    buildFeatures {
        buildConfig = true
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("io.coil-kt:coil-compose:2.7.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("androidx.compose.material:material-icons-extended:1.7.5")
    implementation("androidx.navigation:navigation-compose:2.8.2")
    implementation("androidx.documentfile:documentfile:1.0.1")
    implementation ("androidx.media3:media3-exoplayer:1.3.1")
    implementation ("androidx.media3:media3-ui:1.3.1")
    implementation ("androidx.core:core-ktx:1.12.0")

// Coroutines for background tasks
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

// Jetpack Compose (if not already added)
    implementation ("androidx.compose.material3:material3")
    implementation("androidx.compose.foundation:foundation:1.4.0+")

    implementation(platform("com.google.firebase:firebase-bom:34.5.0"))

    // Add the dependencies for the Remote Config and Analytics libraries
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-config")
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    implementation ("androidx.compose.material3:material3:1.3.0")

    implementation ("androidx.compose.foundation:foundation:1.7.0")
    implementation ("androidx.compose.ui:ui-text:1.7.0")
    implementation ("androidx.compose.ui:ui:1.7.0")
    // Optional - Kotlin extensions and Coroutines support

    // (If using Flow with Room — which this code does)
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")

    // For ViewModel
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6")

    // For navigation (if not already)
    implementation ("androidx.navigation:navigation-compose:2.8.3")

    implementation ("com.squareup.retrofit2:retrofit:2.11.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.14")

    implementation("com.google.firebase:firebase-auth-ktx:22.3.1")
    implementation("com.google.android.gms:play-services-auth:21.0.0")
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("com.google.firebase:firebase-messaging:25.0.1")

    implementation ("org.osmdroid:osmdroid-android:6.1.16")

    implementation ("com.google.android.play:app-update:2.1.0")
    implementation("com.airbnb.android:lottie-compose:6.3.0")
   // implementation(platform("com.google.firebase:firebase-bom:33.7.0"))

    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-perf")
    implementation ("com.github.bumptech.glide:glide:5.0.5")
    implementation ("com.android.billingclient:billing-ktx:6.2.1")

    implementation ("androidx.paging:paging-runtime-ktx:3.2.1")
    implementation ("androidx.paging:paging-compose:3.2.1")

    implementation("com.google.firebase:firebase-firestore-ktx:24.10.3")


    //kapt ("com.github.bumptech.glide:compiler:5.0.5") // if using kapt
}