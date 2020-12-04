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
                implementation(project(":paging-react"))
                implementation(asoft("reakt-media", vers.asoft.reakt))
                implementation(devNpm("file-loader", vers.npm.file_loader))
            }
        }
    }
}
