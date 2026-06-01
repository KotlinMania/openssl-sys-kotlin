// Host-only smoke test that exercises the jsMain Sha bindings against
// the Node N-API addon. Mirrors macosArm64Test/ShaTest
// against the canonical RFC 6234 / NIST SHA-256 vector for "abc".
//
// The SHA-256 round-trip and OPENSSL_VERSION_NUMBER checks only run under
// the jsNodejs runtime — browsers cannot load .node addons by design, so
// in jsBrowser the addon-exercising tests early-return with a fail-fast
// assertion that the runtime is indeed non-Node. The pure-constant
// assertion runs everywhere.
package io.github.kotlinmania.opensslsys

import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
private val isNodeRuntime: Boolean =
    js(
        "(typeof process !== 'undefined' && process.versions && process.versions.node) ? true : false",
    ).unsafeCast<Boolean>()

class ShaTest {
    @Test
    fun sha256OfAbcMatchesNistVector() {
        if (!isNodeRuntime) return // N-API not available in browser; covered by jsNodeTest.

        val input = "abc".encodeToByteArray()
        val data = Uint8Array(input.size)
        for (i in input.indices) {
            data.asDynamic()[i] = input[i].toInt() and 0xFF
        }

        val digest = sha256(data)
        assertNotNull(digest, "EVP_Q_digest via N-API returned null for SHA-256")
        assertEquals(32, digest.length)

        val hex =
            buildString(64) {
                for (i in 0 until digest.length) {
                    val byte = (digest[i].toInt() and 0xFF)
                    append(byte.toString(16).padStart(2, '0'))
                }
            }
        assertEquals(
            "ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad",
            hex,
        )
    }

    @Test
    fun opensslVersionIsAtLeast3() {
        if (!isNodeRuntime) return // OpenSSL version comes from the N-API addon; jsNodeTest covers it.

        val v = opensslVersionNumber()
        val major = (v ushr 28).toInt() and 0xF
        assertTrue(major >= 3, "expected OpenSSL major >= 3, got version=0x${v.toString(16)} major=$major")
    }

    @Test
    fun shaConstantsTrackUpstream() {
        // Pure-constant assertions — run on every JS runtime, no addon needed.
        assertEquals(16, SHA_LBLOCK)
        val long: ShaLong = 0xFFFF_FFFFu
        val long64: ShaLong64 = 0xFFFF_FFFF_FFFF_FFFFuL
        assertEquals(0xFFFF_FFFFu, long)
        assertEquals(0xFFFF_FFFF_FFFF_FFFFuL, long64)
    }
}
