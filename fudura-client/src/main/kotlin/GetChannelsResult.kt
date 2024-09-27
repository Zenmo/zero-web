package com.zenmo.fudura

import kotlinx.serialization.Serializable

@Serializable
data class GetChannelsResult(
    val channels: List<String>,
)
