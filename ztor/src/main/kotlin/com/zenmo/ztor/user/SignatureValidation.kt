package com.zenmo.ztor.user

import com.nimbusds.jose.JWSVerifier
import com.nimbusds.jose.Payload
import com.nimbusds.jose.crypto.Ed25519Verifier
import com.nimbusds.jose.jwk.OctetKeyPair
import com.nimbusds.jwt.SignedJWT
import java.util.*

fun validateAccessTokenAndGetPayload(jwt: String, jwk: String? = null, now: Date = Date()): Payload {
    val key = OctetKeyPair.parse(resolveJwk(jwk))
    val signedJwt = SignedJWT.parse(jwt)

    if (signedJwt.jwtClaimsSet.expirationTime.before(now)) {
        throw Exception("Access Token has expired")
    }

    val verifier: JWSVerifier = Ed25519Verifier(key)
    val isValid = verifier.verify(signedJwt.header, signedJwt.signingInput, signedJwt.signature)
    if (!isValid) {
        throw Exception("Access Token does not comply with public key")
    }

    return signedJwt.payload
}
