plugins {
    kotlin("js")
    id("tz.co.asoft.library")
    id("io.codearte.nexus-staging")
    signing
}

kotlin {
    js(IR) { library(forNodeJs = false) }
    sourceSets {
        val main by getting {
            dependencies {
                api(project(":paging-core"))
                api(asoft("reakt-tables", vers.asoft.reakt))
                api(asoft("reakt-feedback", vers.asoft.reakt))
            }
        }
    }
}

aSoftOSSLibrary(
    version = vers.asoft.paging,
    description = "A react specific implementation for the paging-core library"
)
