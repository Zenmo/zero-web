
val ktor_version = "3.0.3"

plugins {
    kotlin("jvm")
    application

    kotlin("plugin.serialization")
}

group = "com.zenmo"
version = "0.0.1"

application {
    mainClass.set("com.zenmo.waspik.ApplicationKt")

    // These arguments are only applied when running through Gradle (= in development),
    // not when building and running a Fat Jar (= in production).
    applicationDefaultJvmArgs = listOf(
        "-Dio.ktor.development=true",
        // uncomment to let ztor application listen for debugger
        "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005",
        // uncomment to let ztor application connect to debugger on startup
//        "-agentlib:jdwp=transport=dt_socket,server=n,address=172.27.0.1:5005,suspend=y"
    )
}

repositories {
    mavenCentral()
    // Running a pre-release version of Ktor because of a version conflict with serialization.
    // HTTP4K seems better anyway.
    maven("https://maven.pkg.jetbrains.space/public/p/ktor/eap/")
}

dependencies {
    implementation(project(":fudura-client"))
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:${libs.versions.kotlinx.datetime.get()}")
    implementation(project(":zummon"))
}

tasks.withType<Test> {
    this.testLogging {
        this.showStandardStreams = true
    }
}

kotlin {
    sourceSets {
        all {
            languageSettings.optIn("kotlin.uuid.ExperimentalUuidApi")
        }
    }
}
