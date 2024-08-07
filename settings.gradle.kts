
rootProject.name = "zero"
include(
    "zorm",
    "ztor",
    "zummon",
    "vallum",
    "excel-read-named-v3",
    "excel-read-named-v5",
)

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("exposed", "0.54.0")
        }
    }
}
