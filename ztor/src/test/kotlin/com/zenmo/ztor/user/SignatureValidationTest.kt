package com.zenmo.ztor.user

import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

// found at https://keycloak.zenmo.com/realms/testrealm/protocol/openid-connect/certs
val testRealmJwk = """
{
    "kid": "n-X5BeXFe6pIXhDBsDu2mQ2VYc23RSZCFRU5Y6IuzC8",
    "kty": "OKP",
    "alg": "EdDSA",
    "use": "sig",
    "crv": "Ed25519",
    "x": "ILokSbk8EKH-Q1aWo_TguuDRccoSVfEbmGSbm5gcn5I"
}
"""

class SignatureValidationTest {
    @Test
    fun testValidToken() {
        val validToken = "eyJhbGciOiJFZERTQSIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJuLVg1QmVYRmU2cElYaERCc0R1Mm1RMlZZYzIzUlNaQ0ZSVTVZNkl1ekM4In0.eyJleHAiOjE3MjI4NzAwMjMsImlhdCI6MTcyMjg2OTcyMywianRpIjoiZTZkNDg4MzMtNTgxZC00YjJiLTg0YTgtNWRlZjRhY2E1OTQ4IiwiaXNzIjoiaHR0cHM6Ly9rZXljbG9hay56ZW5tby5jb20vcmVhbG1zL3Rlc3RyZWFsbSIsInN1YiI6ImY5ZWVlNTU5LTdiZDgtNGE1My05OWNmLWFhZWYzNmRhZDhhMSIsInR5cCI6IkJlYXJlciIsImF6cCI6InRlc3QtY2xpZW50LXp0b3ItYXBpIiwic2NvcGUiOiJvcGVuaWQiLCJjbGllbnRIb3N0IjoiNjIuMTk1LjE4OC4xNTUiLCJjbGllbnRBZGRyZXNzIjoiNjIuMTk1LjE4OC4xNTUiLCJjbGllbnRfaWQiOiJ0ZXN0LWNsaWVudC16dG9yLWFwaSJ9.lwdSPZKbJBXdmMVLJOWwisjhVyTQkls5ftY08_p5WVtviXqHxyftnt7j-6di9Qa1JkDSduHaUrj470D_ex47Cw"

        val payload = validateAccessTokenAndGetPayload(validToken, testRealmJwk, Date(1722869722L * 1000L))
        assertEquals("f9eee559-7bd8-4a53-99cf-aaef36dad8a1", decodePayload(payload).sub.toString())
    }
}