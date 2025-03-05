package com.zenmo.zummon

import kotlinx.serialization.json.Json

// We want to be a bit lenient when decoding.
// Encoding settings depend on the usecase
val jsonDecoder = Json {
    allowSpecialFloatingPointValues = true
}
