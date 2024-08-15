plugins {
    kotlin("jvm")
    application
}

group = "com.zenmo"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":zummon"))
    implementation("io.github.kelvindev15:Kotlin2PlantUML:3.0.15")
}

application {
    mainClass.set("com.zenmo.diagram.GenerateDiagramKt")
}

kotlin {
    jvmToolchain(22)
}