package com.zenmo.blob

enum class BlobPurpose {
    NATURAL_GAS_VALUES,
    ELECTRICITY_VALUES,
    ELECTRICITY_AUTHORIZATION;

    fun toNamePart(): String {
        return this.toString().lowercase().replace("_", "-")
    }
}