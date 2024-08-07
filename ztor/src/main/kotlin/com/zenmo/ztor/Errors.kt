package com.zenmo.ztor

fun errorMessageToJson(message: String?): Any {
    return mapOf(
        "error" to mapOf(
            "message" to message
        )
    )
}
