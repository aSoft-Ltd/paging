plugins {
    kotlin("js")
    id("tz.co.asoft.application")
}

konfig {
    debug()
    release()
}

kotlin {
    js(IR) {
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
                outputFileName = "main.js"
            }
        }
        binaries.executable()
    }

    sourceSets {
        val main by getting {
            dependencies {
                api(project(":paging-react"))
            }
        }
    }
}
