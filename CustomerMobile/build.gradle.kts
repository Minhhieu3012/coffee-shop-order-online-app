plugins {
    // Android Application Plugin - Phiên bản ổn định 8.1.0
    id("com.android.application") version "8.1.0" apply false

    // Android Library Plugin (Cần thiết nếu sau này tách module)
    id("com.android.library") version "8.1.0" apply false

    // Kotlin Android Plugin - Phiên bản 1.9.0 (Ổn định)
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false

    // KSP (Kotlin Symbol Processing) - Phải khớp chính xác với Kotlin 1.9.0
    id("com.google.devtools.ksp") version "1.9.0-1.0.13" apply false

    // Hilt (Dependency Injection) - Phiên bản mới ổn định
    id("com.google.dagger.hilt.android") version "2.48" apply false

    // Google Services (Firebase)
    id("com.google.gms.google-services") version "4.4.1" apply false

}