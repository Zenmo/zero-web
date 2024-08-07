plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.zenmo.excelreadnamed.v5"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.poi:poi-ooxml:5.3.0")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    implementation(project(":zummon"))

    testImplementation(kotlin("test"))
}
//    implementation("org.apache.logging.log4j:log4j-core:2.14.1")
//    implementation("org.apache.logging.log4j:log4j-api:2.14.1")
//    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.14.1")


tasks {
    shadowJar {
        // Need to rename to prevent conflicts with other versions of Apache POI in AnyLogic
        //relocate("org.apache", "zenmo")
        //relocate("org.openxmlformats.schemas.drawingml.x2006.main.ThemeDocument", "zenmo.org.openxmlformats.schemas.drawingml.x2006.main.ThemeDocument"
    }

    jar {
        manifest {
            attributes["Main-Class"] = "com.zenmo.excelreadnamed.MainKt"
        }
        from(
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
}
