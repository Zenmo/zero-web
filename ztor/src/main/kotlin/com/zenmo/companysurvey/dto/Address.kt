package com.zenmo.companysurvey.dto

import kotlinx.serialization.Serializable

@Serializable
data class Address(
    val street: String,
    val houseNumber: Int,
    val houseLetter: String, // A-Z allowed
    val houseNumberSuffix: String,
    val postalCode: String,
    val city: String,
)