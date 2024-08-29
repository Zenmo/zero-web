
rootProject.name = "zero"
include("zorm", "ztor", "zummon", "vallum", "excel-read-named")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("exposed", "0.53.0")
        }
    }
}
