plugins {
    val vers = object {
        val agp = "4.1.0"
        val kotlin = "1.4.20"
        val asoft_builder = "1.1.1"
        val nexus_stagin = "0.22.0"
    }
    id("com.android.library") version vers.agp apply false
    kotlin("multiplatform") version vers.kotlin apply false
    kotlin("plugin.serialization") version vers.kotlin apply false
    id("tz.co.asoft.library") version vers.asoft_builder apply false
    id("tz.co.asoft.applikation") version vers.asoft_builder apply false
    id("io.codearte.nexus-staging") version vers.nexus_stagin apply false
}