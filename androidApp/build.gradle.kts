plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.compose.compiler)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.lsphysio.android"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.lifesparktech.lsphysio.android"
        minSdk = 24
        targetSdk = 34
        versionCode = 4
        versionName = "1.0.$versionCode"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(projects.shared)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.firebase.common.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.google.services)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.firebase.functions.ktx)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.androidx.activity)
    implementation(project(":unityLibrary"))
    debugImplementation(libs.compose.ui.tooling)
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("com.benasher44:uuid:0.7.0")
    implementation("com.google.accompanist:accompanist-flowlayout:0.36.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation("com.juul.kable:core:0.32.0")
    implementation( files("/Users/madhavsingh/Documents/projects/Physio_backup/walk_physio_app/unityLibrary/libs/unity-classes.jar"))
}
