package com.zenmo.ztor.user

import com.nimbusds.jose.Payload
import com.nimbusds.jwt.SignedJWT
import com.zenmo.orm.UUIDSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import java.util.*

object UnixTimeSerializer : KSerializer<Instant> {
    override val descriptor = PrimitiveSerialDescriptor("UnixTime", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder)
            = Instant.fromEpochSeconds(decoder.decodeLong())

    override fun serialize(encoder: Encoder, value: Instant)
            = encoder.encodeLong(value.epochSeconds)
}

@Serializable
data class AccessTokenPayload(
    @Serializable(with = UUIDSerializer::class)
    val sub: UUID,
    val preferred_username: String? = null,
    val email: String? = null,
    @Serializable(with = UnixTimeSerializer::class)
    val iat: Instant,
    @Serializable(with = UnixTimeSerializer::class)
    val exp: Instant,
)

fun decodeAccessToken(accessToken: String): AccessTokenPayload {
    val payload = SignedJWT.parse(accessToken).payload

    return decodePayload(payload)
}

fun decodePayload(payload: Payload): AccessTokenPayload {
    // to inspect all fields:
    // val root = Json.parseToJsonElement(String(decodedPayload)).jsonObject
    val jsonDecoder = Json {
        ignoreUnknownKeys = true
    }
    return jsonDecoder.decodeFromString<AccessTokenPayload>(payload.toString())
}
