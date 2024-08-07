plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.zenmo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.pkg.github.com/zenmo/zero")
        credentials {
            username = "erikvv"
            // not a secret, just gives access to a public package
            // expires july 2025
            password = "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"
        }
    }
}

dependencies {
    testImplementation(kotlin("test"))

    //implementation("org.apache.poi:poi:5.2.5")
    //implementation("org.apache.poi:poi-ooxml:5.2.5")
    implementation("org.apache.poi:poi:3.17")
    implementation("org.apache.poi:poi-ooxml:3.17")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
//    implementation("com.zenmo:zummon-jvm:main-231-993b9e2")
    implementation(project(":zummon"))
}
//    implementation("org.apache.logging.log4j:log4j-core:2.14.1")
//    implementation("org.apache.logging.log4j:log4j-api:2.14.1")
//    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.14.1")


tasks.test {
    useJUnitPlatform()
}

tasks {
// Configuring Filtering for Relocation
    shadowJar {
        //relocate("org.apache", "zenmo")
        //relocate("org.openxmlformats.schemas.drawingml.x2006.main.ThemeDocument", "zenmo.org.openxmlformats.schemas.drawingml.x2006.main.ThemeDocument")
    }

}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.zenmo.MainKt"
    }
    from (
        configurations
            .runtimeClasspath
            .get()
            .filter { it.name.endsWith("jar") }
            .map { zipTree(it) }
    ) {
        exclude("META-INF/versions/9/module-info.class")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}

tasks.register<JavaExec>("run") {
    group = "application"
    description = "Runs the Kotlin application."
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("com.zenmo.MainKt")
}

kotlin {
    jvmToolchain(17)
}