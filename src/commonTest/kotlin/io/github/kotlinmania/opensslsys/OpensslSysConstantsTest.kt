// port-lint: tests err.rs ssl.rs x509.rs
package io.github.kotlinmania.opensslsys

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class OpensslSysConstantsTest {
    @Test
    fun testErrConstantsAndFunctions() {
        assertEquals(0x01, ERR_TXT_MALLOCED)
        assertEquals(0x02, ERR_TXT_STRING)
        assertEquals(2, ERR_LIB_SYS)
        assertEquals(9, ERR_LIB_PEM)
        assertEquals(13, ERR_LIB_ASN1)

        val packed = errPack(ERR_LIB_PEM, 0, 108)
        assertFalse(errSystemError(packed))
        assertEquals(ERR_LIB_PEM, errGetLib(packed))
        assertEquals(108, errGetReason(packed))
        assertEquals(0, errGetFunc(packed))

        val sysErr = ERR_SYSTEM_FLAG or 5uL
        assertTrue(errSystemError(sysErr))
        assertEquals(ERR_LIB_SYS, errGetLib(sysErr))
    }

    @Test
    fun testSslConstants() {
        assertEquals(1, SSL_SENT_SHUTDOWN)
        assertEquals(2, SSL_RECEIVED_SHUTDOWN)
        assertEquals(0, SSL_ERROR_NONE)
        assertEquals(1, SSL_ERROR_SSL)
        assertEquals(2, SSL_ERROR_WANT_READ)
        assertEquals(3, SSL_ERROR_WANT_WRITE)
        assertEquals(4, SSL_ERROR_WANT_X509_LOOKUP)
        assertEquals(5, SSL_ERROR_SYSCALL)
        assertEquals(6, SSL_ERROR_ZERO_RETURN)
        assertEquals(0x301, TLS1_VERSION)
        assertEquals(0x302, TLS1_1_VERSION)
        assertEquals(0x303, TLS1_2_VERSION)
        assertEquals(0x304, TLS1_3_VERSION)
        assertEquals(0x300, SSL3_VERSION)
    }

    @Test
    fun testX509Constants() {
        assertEquals(0, X509_V_OK)
        assertEquals(1, X509_V_ERR_UNSPECIFIED)
        assertEquals(2, X509_V_ERR_UNABLE_TO_GET_ISSUER_CERT)
        assertEquals(1, X509_FILETYPE_PEM)
        assertEquals(2, X509_FILETYPE_ASN1)
        assertEquals(3, X509_FILETYPE_DEFAULT)
        val name = GeneralName(GEN_DNS)
        assertEquals(GEN_DNS, name.type)
    }

    @Test
    fun testOcspAndPkcs7AndCms() {
        assertEquals(0, OCSP_RESPONSE_STATUS_SUCCESSFUL)
        assertEquals(1, PKCS7_TEXT)
        assertEquals(2, PKCS7_NOCERTS)
        assertEquals(0x1u, CMS_TEXT)
        assertEquals(0x2u, CMS_NOCERTS)
    }

    @Test
    fun testObjMacAndCrypto() {
        assertEquals(0, NID_undef)
        assertEquals(116, NID_dsa)
        assertEquals(0, OPENSSL_VERSION)
        assertEquals(1, OPENSSL_CFLAGS)
    }
}
