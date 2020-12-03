plugins {
    id("com.android.library")
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("tz.co.asoft.library")
}


kotlin {
    universalLib()
    sourceSets {
        val commonMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:${versions.kotlinx.coroutines}")
                api("org.jetbrains.kotlinx:kotlinx-serialization-core:${versions.kotlinx.serialization}")
            }
        }

        val androidMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-coroutines-android:${versions.kotlinx.coroutines}")
            }
        }
    }
}
