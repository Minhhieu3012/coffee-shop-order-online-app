plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.services)
<<<<<<< HEAD

    // --- BỔ SUNG CHO HILT/KAPT ---
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
=======
    id("com.google.devtools.ksp") version "1.9.20-1.0.14"
>>>>>>> 82c972aeb76df1145086620cb76bc46b256bba72
}

// Khối hằng số (constants) để quản lý phiên bản dễ hơn
val hiltVersion = "2.51.1"
val coroutinesVersion = "1.7.3"
val composeVersion = "1.6.0"
val navVersion = "2.7.5"


android {
    namespace = "vn.edu.ut.hieupm9898.customermobile"
    compileSdk = 36

    defaultConfig {
        applicationId = "vn.edu.ut.hieupm9898.customermobile"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // --- ANDROIDX & COMPOSE CƠ BẢN ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Sử dụng BOM cho các thư viện Compose cốt lõi
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    implementation("androidx.compose.foundation:foundation:$composeVersion")

    // Navigation Compose
    implementation("androidx.navigation:navigation-compose:$navVersion")
    implementation("androidx.compose.material:material-icons-extended")

    // --- HILT ---
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-compiler:$hiltVersion") // Dòng này là đúng
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    // --- KOTLIN COROUTINES ---
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")

    // --- FIREBASE ---
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.messaging)

    // --- THƯ VIỆN BÊN THỨ BA ---
    implementation("io.coil-kt:coil-compose:2.7.0")

    // --- TEST IMPLEMENTATION ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    testImplementation("androidx.navigation:navigation-testing:$navVersion")
}

<<<<<<< HEAD
// Khối KAPT bị lỗi đã được XÓA hoàn toàn để khắc phục lỗi cú pháp
// Nếu cần cấu hình, phải sử dụng cú pháp: configure<KaptExtension> { ... }
=======
    // Testing Navigation (Cần sửa phiên bản, ví dụ 2.7.5)
    testImplementation("androidx.navigation:navigation-testing:2.7.5")

    // --- ROOM DATABASE ---
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version") // Hỗ trợ Coroutines/Flow
    ksp("androidx.room:room-compiler:$room_version")      // Trình biên dịch
}
>>>>>>> 82c972aeb76df1145086620cb76bc46b256bba72
