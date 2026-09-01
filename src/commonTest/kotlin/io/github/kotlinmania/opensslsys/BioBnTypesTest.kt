// port-lint: tests bio.rs bn.rs evp.rs
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
    fun testEvpConstants() {
        assertEquals(64u, EVP_MAX_MD_SIZE)
        assertEquals(8, PKCS5_SALT_LEN)
        assertEquals(2048, PKCS12_DEFAULT_ITER)
        assertEquals(EVP_PKEY_ALG_CTRL + 1, EVP_PKEY_CTRL_DH_PARAMGEN_PRIME_LEN)
        assertEquals(EVP_PKEY_ALG_CTRL + 2, EVP_PKEY_CTRL_DH_PARAMGEN_GENERATOR)
    }

    @Test
    fun testTypes() {
        val pkey = EvpPkey()
        val sig = EvpSignature()
        assertNotNull(pkey)
        assertNotNull(sig)
    }
}
