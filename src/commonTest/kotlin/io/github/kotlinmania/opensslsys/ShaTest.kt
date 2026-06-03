package io.github.kotlinmania.opensslsys

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@OptIn(ExperimentalUnsignedTypes::class)
class ShaTest {
    @Test
    fun sha1OfAbcMatchesPublishedVector() {
        assertDigestEquals("a9993e364706816aba3e25717850c26c9cd0d89d", sha1("abc".encodeToByteArray()))
    }

    @Test
    fun sha224OfAbcMatchesPublishedVector() {
        assertDigestEquals("23097d223405d8228642a477bda255b32aadbce4bda0b3f7e36c9da7", sha224("abc".encodeToByteArray()))
    }

    @Test
    fun sha256OfAbcMatchesPublishedVector() {
        assertDigestEquals(
            "ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad",
            sha256("abc".encodeToByteArray()),
        )
    }

    @Test
    fun sha384OfAbcMatchesPublishedVector() {
        assertDigestEquals(
            "cb00753f45a35e8bb5a03d699ac65007272c32ab0eded163" +
                "1a8b605a43ff5bed8086072ba1e7cc2358baeca134c825a7",
            sha384("abc".encodeToByteArray()),
        )
    }

    @Test
    fun sha512OfAbcMatchesPublishedVector() {
        assertDigestEquals(
            "ddaf35a193617abacc417349ae20413112e6fa4e89a97ea2" +
                "0a9eeee64b55d39a2192992a274fc1a836ba3c23a3feebbd" +
                "454d4423643ce80e2a9ac94fa54ca49f",
            sha512("abc".encodeToByteArray()),
        )
    }

    @Test
    fun shaConstantsTrackUpstream() {
        assertEquals(16, SHA_LBLOCK)
        val long: ShaLong = 0xffff_ffffu
        val long64: ShaLong64 = 0xffff_ffff_ffff_ffffuL
        assertEquals(0xffff_ffffu, long)
        assertEquals(0xffff_ffff_ffff_ffffuL, long64)
    }

    private fun assertDigestEquals(
        expectedHex: String,
        actualDigest: ByteArray?,
    ) {
        assertNotNull(actualDigest)
        assertContentEquals(hexToBytes(expectedHex), actualDigest)
    }

    private fun hexToBytes(hex: String): ByteArray =
        ByteArray(hex.length / 2) { index ->
            hex.substring(index * 2, index * 2 + 2).toInt(16).toByte()
        }
}
