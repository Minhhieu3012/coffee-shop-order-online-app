plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    // KSP (Thay thế cho KAPT - Tối ưu cho Room & Hilt)
    id("com.google.devtools.ksp")

    // Hilt (Dependency Injection)
    id("com.google.dagger.hilt.android")

    // Firebase (Quan trọng để đọc file google-services.json)
    id("com.google.gms.google-services")
}

android {
    namespace = "vn.edu.ut.hieupm9898.customermobile"
    compileSdk = 34 // Android 14

    defaultConfig {
        applicationId = "vn.edu.ut.hieupm9898.customermobile"
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

    // Cấu hình Java 17 (Bắt buộc cho Hilt và compileSdk 34)
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true // Bật tính năng này nếu bạn cần dùng BuildConfig.DEBUG
    }

    composeOptions {
        // Phiên bản này phải khớp với version Kotlin bạn đang dùng
        // Kotlin 1.9.0 -> 1.5.2
        // Kotlin 1.9.20 -> 1.5.4
        kotlinCompilerExtensionVersion = "1.5.2"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // --- Core Android ---
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")

    // --- Compose (Sử dụng BOM) ---
    // BOM giúp đồng bộ phiên bản các thư viện Compose
    val composeBom = platform("androidx.compose:compose-bom:2024.02.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // Icon mở rộng (Google Icons)
    implementation("androidx.compose.material:material-icons-extended")

    // --- Navigation ---
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // --- Coil (Load ảnh từ URL) ---
    implementation("io.coil-kt:coil-compose:2.6.0")

    // --- Hilt (Dependency Injection) ---
    implementation("com.google.dagger:hilt-android:2.48")
    ksp("com.google.dagger:hilt-compiler:2.48") // Dùng ksp thay vì kapt
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // --- Coroutines ---
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // --- Firebase ---
    implementation(platform("com.google.firebase:firebase-bom:32.7.4"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")

    // Google Sign-In
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    // Activity Result API
    implementation("androidx.activity:activity-compose:1.8.2")

    // --- Room Database ---
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    // --- Testing ---
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Pager (Carousel)
    implementation("com.google.accompanist:accompanist-pager:0.30.1")
}