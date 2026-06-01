// Host-only smoke test that exercises the macosArm64 cinterop binding by
// computing a known SHA-256 digest and comparing it against the published
// RFC 6234 / NIST test vector for input "abc".
package io.github.kotlinmania.opensslsys

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.UByteVar
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.toCValues
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@OptIn(ExperimentalForeignApi::class)
class ShaTest {
    @Test
    fun sha256OfAbcMatchesNistVector() {
        val input = "abc".encodeToByteArray().asUByteArray()
        val expectedHex =
            "ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad"

        memScoped {
            val data = input.toCValues().ptr
            val md = allocArray<UByteVar>(32)
            val result = sha256(data, input.size.toULong(), md)
            assertNotNull(result, "EVP_Q_digest returned NULL for SHA-256")

            val bytes = md.reinterpret<ByteVar>().readBytes(32)
            val digestHex =
                buildString(64) {
                    for (b in bytes) {
                        val byte = b.toInt() and 0xFF
                        append(byte.toString(16).padStart(2, '0'))
                    }
                }
            assertEquals(expectedHex, digestHex)
        }
    }

    @Test
    fun shaConstantsTrackUpstream() {
        // SHA_LBLOCK / ShaLong / ShaLong64 are openssl/sha.h surface bits
        // re-exported from src/sha.rs. Asserting them here makes drift
        // against the C ABI detectable.
        assertEquals(16, SHA_LBLOCK)
        val long: ShaLong = 0xFFFF_FFFFu
        val long64: ShaLong64 = 0xFFFF_FFFF_FFFF_FFFFuL
        assertEquals(0xFFFF_FFFFu, long)
        assertEquals(0xFFFF_FFFF_FFFF_FFFFuL, long64)
    }
}
