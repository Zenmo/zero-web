package com.zenmo.ztor.user

// found at https://keycloak.zenmo.com/realms/zenmo/protocol/openid-connect/certs
const val defaultJwk = """
{
    "kid": "hN_HaBnYl-n3wae9APMVa9RaR6BdN-3eHAHvVZT_3So",
    "kty": "OKP",
    "alg": "EdDSA",
    "use": "sig",
    "crv": "Ed25519",
    "x": "jmcE7tddPunTe6SbvuaNaeMQJk0bOcdYey2YeU_8lyM"
}
"""

fun resolveJwk(jwk: String?): String {
    if (jwk != null) {
        return jwk
    }

    val envJwk = System.getenv("ACCESS_TOKEN_JWK")
    if (envJwk != null) {
        return envJwk
    }

    return defaultJwk
}