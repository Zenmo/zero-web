package com.zenmo.ztor.user

import com.auth0.jwt.JWT
import com.zenmo.orm.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.datetime.Instant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.*

object UnixTimeSerializer : KSerializer<Instant> {
    override val descriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder)
        = Instant.fromEpochSeconds(decoder.decodeLong())

    override fun serialize(encoder: Encoder, value: Instant)
        = encoder.encodeLong(value.epochSeconds)
}

@Serializable
data class AccessTokenPayload(
    @Serializable(with = UUIDSerializer::class)
    val sub: UUID,
    val preferred_username: String,
    val email: String?,
    @Serializable(with = UnixTimeSerializer::class)
    val iat: Instant,
    @Serializable(with = UnixTimeSerializer::class)
    val exp: Instant,
)

fun decodeAccessToken(accessToken: String): AccessTokenPayload {
    val decodedJwt = JWT.decode(accessToken)
    val decodedPayload = Base64.getDecoder().decode(decodedJwt.payload)

    // to inspect all fields:
    // val root = Json.parseToJsonElement(String(decodedPayload)).jsonObject
    val jsonDecoder = Json { ignoreUnknownKeys = true }

    val accessTokenPayload = jsonDecoder.decodeFromString<AccessTokenPayload>(String(decodedPayload))
    return accessTokenPayload
}

data class UserSession(
    val state: String,
    val token: String,
) {
    /**
     * Contains user info
     */
    fun getDecodedAccessToken() = decodeAccessToken(token)

    fun getUserId() = getDecodedAccessToken().sub
}
