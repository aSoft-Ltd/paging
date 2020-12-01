plugins {
    kotlin("js")
    id("tz.co.asoft.library")
}

kotlin {
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
