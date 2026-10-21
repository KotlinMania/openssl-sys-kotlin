// port-lint: tests bio.rs
package io.github.kotlinmania.opensslsys

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class BioBnTypesTest {
    @Test
    fun testBioConstants() {
        assertEquals(0, BIO_TYPE_NONE)
        assertEquals(2, BIO_CTRL_EOF)
        assertEquals(3, BIO_CTRL_INFO)
        assertEquals(11, BIO_CTRL_FLUSH)
        assertEquals(40, BIO_CTRL_DGRAM_QUERY_MTU)
        assertEquals(130, BIO_C_SET_BUF_MEM_EOF_RETURN)
        assertEquals(0x01, BIO_FLAGS_READ)
        assertEquals(0x02, BIO_FLAGS_WRITE)
        assertEquals(0x04, BIO_FLAGS_IO_SPECIAL)
        assertEquals(0x07, BIO_FLAGS_RWS)
        assertEquals(0x08, BIO_FLAGS_SHOULD_RETRY)
        assertEquals(41, BIO_CTRL_DGRAM_GET_MTU)
        assertEquals(42, BIO_CTRL_DGRAM_SET_MTU)
        assertEquals(82, BIO_CTRL_DGRAM_GET_LOCAL_ADDR_CAP)
        assertEquals(83, BIO_CTRL_DGRAM_GET_LOCAL_ADDR_ENABLE)
        assertEquals(84, BIO_CTRL_DGRAM_SET_LOCAL_ADDR_ENABLE)
        assertEquals(86, BIO_CTRL_DGRAM_GET_CAPS)
        assertEquals(87, BIO_CTRL_DGRAM_SET_CAPS)
        assertEquals(88, BIO_CTRL_DGRAM_GET_NO_TRUNC)
        assertEquals(89, BIO_CTRL_DGRAM_SET_NO_TRUNC)
    }

    @Test
    fun testBnConstants() {
        val ulong: BnUlong = 42uL
        assertEquals(42uL, ulong)
        assertEquals(0x01, BN_FLG_MALLOCED)
        assertEquals(0x02, BN_FLG_STATIC_DATA)
        assertEquals(0x04, BN_FLG_CONSTTIME)
        assertEquals(0x08, BN_FLG_SECURE)
    }

    @Test
    fun testEvpConstantsAndHelpers() {
        assertEquals(64u, EVP_MAX_MD_SIZE)
        assertEquals(8, PKCS5_SALT_LEN)
        assertEquals(2048, PKCS12_DEFAULT_ITER)
        assertEquals(EVP_PKEY_ALG_CTRL + 1, EVP_PKEY_CTRL_DH_PARAMGEN_PRIME_LEN)
        assertEquals(EVP_PKEY_ALG_CTRL + 2, EVP_PKEY_CTRL_DH_PARAMGEN_GENERATOR)

        val sha256ByNid = evpGetDigestbynid(NID_SHA256)
        assertNotNull(sha256ByNid)
        assertEquals(32, evpMdSize(sha256ByNid))

        val pkeyCtx = EvpPkeyCtx(1L)
        assertEquals(1, evpPkeyCtxSetRsaKeygenBits(pkeyCtx, 2048))
        assertEquals(1, evpPkeyCtxSetRsaPadding(pkeyCtx, RSA_PKCS1_PSS_PADDING))
        assertEquals(1, evpPkeyCtxSetEcParamgenCurveNid(pkeyCtx, NID_X9_62_PRIME256V1))
        assertEquals(1, evpPkeyCtxSetHkdfMode(pkeyCtx, EVP_PKEY_HKDEF_MODE_EXTRACT_AND_EXPAND))

        val ecKey = evpEcGen("prime256v1")
        assertNotNull(ecKey)
        assertEquals(NID_X9_62_PRIME256V1, evpPkeyId(ecKey))
    }

    @Test
    fun testTypesAndStacks() {
        val pkey = EvpPkey(10L)
        val sig = EvpSignature(20L)
        val aesKey = AesKey()
        val asn1Type = Asn1Type(V_ASN1_UTF8STRING)
        val cms = CmsContentInfo(30L)
        val pkcs7 = Pkcs7(length = 128L)
        val pkcs12 = Pkcs12(40L)
        val x509Stack = StackStX509(50L)
        val lookup = X509Lookup(60L)

        assertNotNull(pkey)
        assertNotNull(sig)
        assertEquals(0, aesKey.rounds)
        assertEquals(V_ASN1_UTF8STRING, asn1Type.typeTag)
        assertEquals(30L, cms.address)
        assertEquals(128L, pkcs7.length)
        assertEquals(40L, pkcs12.address)
        assertEquals(50L, x509Stack.address)
        assertEquals(1, x509LookupAddDir(lookup, "/etc/ssl/certs", X509_FILETYPE_PEM))
        assertEquals(2, PointConversionFormT.POINT_CONVERSION_COMPRESSED.value)
        assertEquals(4, PointConversionFormT.POINT_CONVERSION_UNCOMPRESSED.value)
        assertEquals(6, PointConversionFormT.POINT_CONVERSION_HYBRID.value)
    }
}
