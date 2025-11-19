// File: build.gradle.kts (Project: Customer_Mobile)

plugins {
    alias(libs.plugins.android.application) apply false

    // --- SỬA DÒNG NÀY (Bỏ chữ jetbrains đi) ---
    alias(libs.plugins.kotlin.android) apply false

    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.google.services) apply false

    // --- HILT (Giữ nguyên dòng này) ---
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
}