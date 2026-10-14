// port-lint: tests sha.rs bio.rs evp.rs ssl.rs err.rs handwritten/rand.rs handwritten/object.rs
package io.github.kotlinmania.opensslsys

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

private val HEX_CHARS = "0123456789abcdef".toCharArray()

private fun ByteArray.toHexString(): String {
    val out = CharArray(size * 2)
    for (i in indices) {
        val b = this[i].toInt() and 0xFF
        out[i * 2] = HEX_CHARS[b ushr 4]
        out[i * 2 + 1] = HEX_CHARS[b and 0x0F]
    }
    return out.concatToString()
}

class OpensslAbiParityTest {
    @Test
    fun testOpenSslVersionAndInit() {
        opensslInit()
        val versionNum = openSslVersionNum()
        assertTrue(versionNum >= 0x10101000uL, "Expected OpenSSL >= 1.1.1, got 0x${versionNum.toString(16)}")
        val versionStr = openSslVersion(OPENSSL_VERSION)
        assertNotNull(versionStr)
        assertTrue(
            versionStr.contains("OpenSSL", ignoreCase = true) ||
                versionStr.contains("LibreSSL", ignoreCase = true) ||
                versionStr.contains("BoringSSL", ignoreCase = true),
            "Unexpected OpenSSL_version(0): $versionStr",
        )
    }

    @Test
    fun testOneShotShaDigests() {
        val abc = "abc".encodeToByteArray()

        val d1 = sha1(abc)
        assertNotNull(d1)
        assertEquals(SHA_DIGEST_LENGTH, d1.size)
        assertEquals("a9993e364706816aba3e25717850c26c9cd0d89d", d1.toHexString())

        val d224 = sha224(abc)
        assertNotNull(d224)
        assertEquals(SHA224_DIGEST_LENGTH, d224.size)
        assertEquals("23097d223405d8228642a477bda255b32aadbce4bda0b3f7e36c9da7", d224.toHexString())

        val d256 = sha256(abc)
        assertNotNull(d256)
        assertEquals(SHA256_DIGEST_LENGTH, d256.size)
        assertEquals(
            "ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad",
            d256.toHexString(),
        )

        val d384 = sha384(abc)
        assertNotNull(d384)
        assertEquals(SHA384_DIGEST_LENGTH, d384.size)
        assertEquals(
            "cb00753f45a35e8bb5a03d699ac65007272c32ab0eded1631a8b605a43ff5bed" +
                "8086072ba1e7cc2358baeca134c825a7",
            d384.toHexString(),
        )

        val d512 = sha512(abc)
        assertNotNull(d512)
        assertEquals(SHA512_DIGEST_LENGTH, d512.size)
        assertEquals(
            "ddaf35a193617abacc417349ae20413112e6fa4e89a97ea20a9eeee64b55d39a" +
                "2192992a274fc1a836ba3c23a3feebbd454d4423643ce80e2a9ac94fa54ca49f",
            d512.toHexString(),
        )
    }

    @Test
    fun testBioMemRoundTripAndControls() {
        val method = bioSMem()
        assertNotNull(method)
        val bio = bioNew(method)
        assertNotNull(bio)
        try {
            val payload = "kotlinmania-openssl-sys-bio".encodeToByteArray()
            val written = bioWrite(bio, payload, payload.size)
            assertEquals(payload.size, written)
            assertEquals(payload.size.toLong(), bioPending(bio))
            assertEquals(0, bioEof(bio))

            val snapshot = bioGetMemData(bio)
            assertContentEquals(payload, snapshot)

            val readBuf = ByteArray(payload.size)
            val readCount = bioRead(bio, readBuf, readBuf.size)
            assertEquals(payload.size, readCount)
            assertContentEquals(payload, readBuf)
            assertEquals(0L, bioPending(bio))
            assertEquals(1, bioReset(bio))
        } finally {
            bioFreeAll(bio)
        }
    }

    @Test
    fun testEvpDigestLifecycle() {
        opensslInit()
        val md = evpSha256()
        assertNotNull(md)
        assertEquals(SHA256_DIGEST_LENGTH, evpMdGetSize(md))

        val byName = evpGetDigestbyname("SHA256")
        assertNotNull(byName)
        assertEquals(SHA256_DIGEST_LENGTH, evpMdGetSize(byName))

        val ctx = evpMdCtxNew()
        assertNotNull(ctx)
        try {
            assertEquals(1, evpDigestInitEx(ctx, md, null))
            assertEquals(1, evpDigestUpdate(ctx, "ab".encodeToByteArray()))
            assertEquals(1, evpDigestUpdate(ctx, "c".encodeToByteArray()))
            val out = ByteArray(EVP_MAX_MD_SIZE.toInt())
            val outLen = IntArray(1)
            assertEquals(1, evpDigestFinalEx(ctx, out, outLen))
            assertEquals(SHA256_DIGEST_LENGTH, outLen[0])
            assertEquals(
                "ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad",
                out.copyOf(outLen[0]).toHexString(),
            )
        } finally {
            evpMdCtxFree(ctx)
        }
    }

    @Test
    fun testSslCtxAndSslLifecycle() {
        opensslInit()
        val method = tlsMethod()
        assertNotNull(method)
        assertNotNull(tlsClientMethod())
        assertNotNull(tlsServerMethod())

        val ctx = sslCtxNew(method)
        assertNotNull(ctx)
        try {
            val added = sslCtxSetOptions(ctx, SSL_OP_NO_SSLv2 or SSL_OP_NO_SSLv3)
            assertTrue((added and SSL_OP_NO_SSLv3) == SSL_OP_NO_SSLv3)
            assertTrue((sslCtxGetOptions(ctx) and SSL_OP_NO_SSLv3) == SSL_OP_NO_SSLv3)

            assertEquals(1, sslCtxSetMinProtoVersion(ctx, TLS1_2_VERSION))
            assertEquals(TLS1_2_VERSION, sslCtxGetMinProtoVersion(ctx))

            assertEquals(1, sslCtxSetMaxProtoVersion(ctx, TLS1_3_VERSION))
            assertEquals(TLS1_3_VERSION, sslCtxGetMaxProtoVersion(ctx))

            val mode = sslCtxSetMode(ctx, SSL_MODE_AUTO_RETRY)
            assertTrue((mode and SSL_MODE_AUTO_RETRY) == SSL_MODE_AUTO_RETRY)

            val ssl = sslNew(ctx)
            assertNotNull(ssl)
            try {
                assertEquals(1, sslSetMinProtoVersion(ssl, TLS1_3_VERSION))
                assertEquals(TLS1_3_VERSION, sslGetMinProtoVersion(ssl))
            } finally {
                sslFree(ssl)
            }
        } finally {
            sslCtxFree(ctx)
        }
    }

    @Test
    fun testErrAndRandAndObjNidResolution() {
        errClearError()
        assertEquals(0uL, errGetError())
        assertEquals(0uL, errPeekError())
        assertEquals(0uL, errPeekLastError())

        val packed = errPack(ERR_LIB_PEM, 0, 0x99)
        assertEquals(ERR_LIB_PEM, errGetLib(packed))
        assertEquals(0x99, errGetReason(packed))
        val msg = errErrorStringN(packed, 128)
        assertTrue(msg.isNotEmpty())

        assertEquals(1, randStatus())
        val randomBytes = ByteArray(32)
        assertEquals(1, randBytes(randomBytes, randomBytes.size))
        assertTrue(randomBytes.any { it != 0.toByte() })

        assertEquals(NID_sha256, objSn2nid("SHA256"))
        assertEquals(NID_sha256, objLn2nid("sha256"))
        assertEquals(NID_sha256, objTxt2nid("2.16.840.1.101.3.4.2.1"))
        assertEquals("SHA256", objNid2sn(NID_sha256))
        assertEquals("sha256", objNid2ln(NID_sha256))
    }
}
