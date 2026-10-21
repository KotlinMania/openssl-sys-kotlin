package consumer

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class PublicationConsumerTest {
    @Test
    fun publishedVersionAndMemoryLifecycle() {
        val (verNum, verStr) = initAndReadVersion()
        assertTrue(verNum >= 0x10101000L)
        assertNotNull(verStr)
        assertTrue(verStr.isNotEmpty())
        assertNotEquals(0uL, allocateAndFreeBuffer(64uL))
    }

    @Test
    fun publishedShaDigestsAndConstantTimeHelpers() {
        val abc = "abc".encodeToByteArray()
        assertEquals("a9993e364706816aba3e25717850c26c9cd0d89d", computeSha1Hex(abc))
        assertEquals(
            "ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad",
            computeSha256Hex(abc),
        )
        val secret = byteArrayOf(1, 2, 3, 4)
        val (cmp, zeroed) = cleanseAndCompare(secret)
        assertEquals(0, cmp)
        assertTrue(zeroed)
    }

    @Test
    fun publishedBioErrRandNidAndPublicTypes() {
        val payload = "openssl-sys-android-aar-consumer".encodeToByteArray()
        assertContentEquals(payload, bioRoundTrip(payload))
        assertTrue(verifyPublicTypesAndErrRandNid())
    }
}
