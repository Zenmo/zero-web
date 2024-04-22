plugins {
    kotlin("multiplatform")
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

            }
        }
    }
}