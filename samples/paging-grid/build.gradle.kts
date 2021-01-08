plugins {
    kotlin("js")
    id("tz.co.asoft.applikation")
}

applikation {
    debug()
    staging()
    release()
}

kotlin {
    js(IR) { application() }

    sourceSets {
        val main by getting {
            dependencies {
                implementation(project(":paging-react"))
                implementation(asoft("reakt-media", vers.asoft.reakt))
            }
        }
    }
}
