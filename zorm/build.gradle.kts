
plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.serialization") version "1.9.23"
}

group = "com.zenmo.orm"
version = "0.0.1"

repositories {
    mavenCentral()
}

// for AnyLogic build
//kotlin {
//    jvmToolchain(11)
//}

val exposed_version = "0.48.0"

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    implementation("org.postgresql:postgresql:42.5.1")
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:$exposed_version")
    // We are using Kotlin datetimes because they support serialization out-of-the-box.
    // If convenient for AnyLogic we can switch to Java datetimes.
//    implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.9.20")
}

tasks.withType<Test> {
    this.testLogging {
        this.showStandardStreams = true
    }
}

tasks {
    shadowJar {

    }
}
