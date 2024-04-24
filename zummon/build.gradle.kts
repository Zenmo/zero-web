plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

group = "com.zenmo"
version = "0.0.1"

repositories {
    mavenCentral()
}



kotlin {
    jvm {
    }
    js(IR) {
        generateTypeScriptDefinitions()
        binaries.executable()
        browser {

        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0-RC.2")
                implementation("com.benasher44:uuid:0.8.4")
            }
        }
    }
}
