// port-lint: tests lib.rs crypto.rs
package io.github.kotlinmania.opensslsys

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

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
        val result = callback(buffer, 16, 0, 0L)
        assertEquals(1, result)
        assertEquals(42, buffer[0])

        val samCallback =
            OpensslPasswordCallback { buf, size, rwflag, _ ->
                assertEquals(16, size)
                assertEquals(1, rwflag)
                buf[1] = 99
                2
            }
        assertEquals(2, samCallback.invoke(buffer, 16, 1, 0L))
        assertEquals(99, buffer[1])
    }

    @Test
    fun testOpensslMallocAndFree() {
        val mem = opensslMalloc(32)
        assertNotEquals(0L, mem)
        opensslFree(mem)
    }

    @Test
    fun testCryptoMemcmpAndOpensslCleanse() {
        val a = byteArrayOf(1, 2, 3, 4)
        val b = byteArrayOf(1, 2, 3, 4)
        val c = byteArrayOf(1, 2, 3, 5)
        assertEquals(0, cryptoMemcmp(a, b, 4))
        assertNotEquals(0, cryptoMemcmp(a, c, 4))
        assertEquals(0, cryptoMemcmp(a, c, 3))

        opensslCleanse(a)
        assertEquals(0, a[0].toInt())
        assertEquals(0, a[1].toInt())
        assertEquals(0, a[2].toInt())
        assertEquals(0, a[3].toInt())
    }
}
