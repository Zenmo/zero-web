plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "zero"
include("zorm", "ztor", "zummon", "vallum", "excel-read-named", "diagram")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("exposed", "0.53.0")
        }
    }
}
include("diagram")
