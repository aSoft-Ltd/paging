plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("tz.co.asoft.library")
    id("io.codearte.nexus-staging")
    signing
}


kotlin {
    multiplatformLib()
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(asoft("later-ktx", vers.asoft.later))
                api("org.jetbrains.kotlinx:kotlinx-serialization-core:${vers.kotlinx.serialization}")
            }
        }
    }
}

aSoftOSSLibrary(
    version = vers.asoft.paging,
    description = "A Platform agnostic paging library"
)