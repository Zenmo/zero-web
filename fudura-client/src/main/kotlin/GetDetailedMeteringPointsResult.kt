package com.zenmo.fudura

import kotlinx.serialization.Serializable

@Serializable
data class GetDetailedMeteringPointsResult(
    val meteringPoints: List<DetailedMeteringPoint>,
)

@Serializable
data class DetailedMeteringPoint(
    val ean: String,
    val meteringPointId: String,
    val authorizations: List<Authorization>, // seems to be a single one always
)

@Serializable
data class Authorization (
    val customerId: String,
    val periods: List<AuthorizationPeriod>, // seems to be a single one always
)
