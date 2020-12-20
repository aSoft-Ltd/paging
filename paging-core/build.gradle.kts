plugins {
    id("com.android.library")
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("tz.co.asoft.library")
    id("io.codearte.nexus-staging")
    signing
}


kotlin {
    universalLib()
    sourceSets {
        val commonMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:${vers.kotlinx.coroutines}")
                api("org.jetbrains.kotlinx:kotlinx-serialization-core:${vers.kotlinx.serialization}")
            }
        }

        val androidMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-coroutines-android:${vers.kotlinx.coroutines}")
            }
        }
    }
}

aSoftOSSLibrary(
    version = vers.asoft.paging,
    description = "A Platform agnostic paging library"
)