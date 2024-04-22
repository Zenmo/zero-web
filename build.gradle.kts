plugins {
    // need to specify version here because this plugin in used in multiple subprojects
    id("org.jetbrains.kotlin.jvm") version "1.9.23" apply false
    kotlin("plugin.serialization") version "1.9.23" apply false
}