package com.zenmo.ztor.blob

import kotlin.test.Test
import kotlin.test.assertEquals

class RemoveSpecialCharsTest {
    @Test
    fun testRemoveSpecialChars() {
        assertEquals("a_b-c3e", removeSpecialChars("a_b-c³\uD83D\uDD12è"))
    }
}
