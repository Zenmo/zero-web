import java.net.URI

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    `maven-publish`
}

group = "com.zenmo"
version = System.getenv("VERSION_TAG") ?: "dev"

repositories {
    mavenCentral()
}

kotlin {
    jvm {
    }
    js(IR) {
        useEsModules()
        generateTypeScriptDefinitions()
        binaries.library()
        compilations["main"].packageJson {
            // hack hack hack
            types = "kotlin/zero-zummon.d.ts"
        }
        browser {
        }
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:${libs.versions.kotlinx.serialization.json.get()}")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${libs.versions.kotlinx.serialization.json.get()}")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")
                implementation("com.benasher44:uuid:0.8.4")
            }
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.zenmo"
            artifactId = "libzummon"
            version = System.getenv("VERSION_TAG") ?: "dev"
        }
        repositories {
            maven {
                name = "GitHubPackages"
                url = URI("https://maven.pkg.github.com/zenmo/zero")
                credentials {
                    username = System.getenv("GITHUB_ACTOR")
                    password = System.getenv("GITHUB_TOKEN")
                }
            }
        }
    }
}