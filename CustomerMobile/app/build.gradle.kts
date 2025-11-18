plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.services)
}

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
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Sử dụng BOM cho các thư viện Compose cốt lõi
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    // Cập nhật: 'foundation-layout' đã hợp nhất vào 'androidx.compose.foundation'
    implementation("androidx.compose.foundation:foundation")

    // Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.7.5")
    implementation("androidx.compose.material:material-icons-extended") // Icons mở rộng

    // FIREBASE
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.messaging)

    // Thư viện bên thứ ba
    implementation("io.coil-kt:coil-compose:2.7.0")

    // TEST IMPLEMENTATION
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Testing Navigation (Cần sửa phiên bản, ví dụ 2.7.5)
    testImplementation("androidx.navigation:navigation-testing:2.7.5")
}