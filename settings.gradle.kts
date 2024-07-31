
rootProject.name = "zero"
include("zorm", "ztor", "zummon")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("exposed", "0.53.0")
        }
    }
}
