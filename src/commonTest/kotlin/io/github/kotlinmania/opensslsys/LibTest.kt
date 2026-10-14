// port-lint: tests lib.rs crypto.rs
package io.github.kotlinmania.opensslsys

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class LibTest {
    @Test
    fun testInitAndAssumeInit() {
        opensslInit()
        assumeInit()
    }

    @Test
    fun testPasswordCallback() {
        val callback: PasswordCallback = { buf, size, rwflag, _ ->
            assertEquals(16, size)
            assertEquals(0, rwflag)
            buf[0] = 42
            1
        }
        val buffer = ByteArray(16)
        val result = callback(buffer, 16, 0, null)
        assertEquals(1, result)
        assertEquals(42, buffer[0])
    }

    @Test
    fun testOpensslMallocAndFree() {
        val mem = opensslMalloc(32)
        assertNotNull(mem)
        assertEquals(32, mem.size)
        opensslFree(mem)
    }
}
