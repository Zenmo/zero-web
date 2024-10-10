plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.zenmo.excelreadnamed.v3"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    //implementation("org.apache.poi:poi:5.2.5")
    //implementation("org.apache.poi:poi-ooxml:5.2.5")
    implementation("org.apache.poi:poi:3.17")
    implementation("org.apache.poi:poi-ooxml:3.17")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")
    implementation(project(":zummon"))
}
//    implementation("org.apache.logging.log4j:log4j-core:2.14.1")
//    implementation("org.apache.logging.log4j:log4j-api:2.14.1")
//    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.14.1")

tasks.test {
    useJUnitPlatform()
}

tasks {
    shadowJar {
        // Need to rename to prevent conflicts with other versions of Apache POI in AnyLogic
        //relocate("org.apache", "zenmo")
        //relocate("org.openxmlformats.schemas.drawingml.x2006.main.ThemeDocument", "zenmo.org.openxmlformats.schemas.drawingml.x2006.main.ThemeDocument"
    }
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.zenmo.excelreadnamed.MainKt"
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
    mainClass.set("com.zenmo.excelreadnamed.MainKt")
}

kotlin {
    jvmToolchain(17)
}
