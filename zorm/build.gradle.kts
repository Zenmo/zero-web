import java.net.URI

plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    `maven-publish`
    kotlin("jvm")
    kotlin("plugin.serialization") // dont think we need this in the ORM
}

group = "com.zenmo.orm"
version = "dev"

repositories {
    mavenCentral()
}

val exposed_version = libs.versions.exposed.get()

dependencies {
    implementation(project(":zummon"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    implementation("org.postgresql:postgresql:42.7.4")
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:$exposed_version")
    // We are using Kotlin datetimes because they support serialization out-of-the-box.
    // If convenient for AnyLogic we can switch to Java datetimes.
//    implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")

    implementation("com.benasher44:uuid:0.8.4")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:2.0.20")
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

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.zenmo"
            artifactId = "libzorm"
            version = System.getenv("VERSION_TAG") ?: "dev"

            artifact(tasks["shadowJar"])
        }
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
