// port-lint: source sha.rs
@file:OptIn(ExperimentalUnsignedTypes::class)

package io.github.kotlinmania.opensslsys

import io.github.kotlinmania.sha2.sha256.compress256
import io.github.kotlinmania.sha2.sha512.compress512

// Upstream gate: cfg(not(osslconf = "OPENSSL_NO_DEPRECATED_3_0"))
public const val SHA_LBLOCK: Int = 16

// Upstream gate: cfg(not(osslconf = "OPENSSL_NO_DEPRECATED_3_0"))
public typealias ShaLong = UInt

// Upstream gate: cfg(not(osslconf = "OPENSSL_NO_DEPRECATED_3_0"))
public typealias ShaLong64 = ULong

public fun sha1(data: ByteArray): ByteArray? {
    val blocks = paddedBlocks(data, blockSize = 64, lengthFieldSize = 8)
    var h0 = 0x67452301u
    var h1 = 0xEFCDAB89u
    var h2 = 0x98BADCFEu
    var h3 = 0x10325476u
    var h4 = 0xC3D2E1F0u

    val w = UIntArray(80)
    for (block in blocks) {
        for (i in 0 until 16) {
            val offset = i * 4
            w[i] = ((block[offset].toUInt() and 0xffu) shl 24) or
                ((block[offset + 1].toUInt() and 0xffu) shl 16) or
                ((block[offset + 2].toUInt() and 0xffu) shl 8) or
                (block[offset + 3].toUInt() and 0xffu)
        }
        for (i in 16 until 80) {
            val v = w[i - 3] xor w[i - 8] xor w[i - 14] xor w[i - 16]
            w[i] = (v shl 1) or (v shr 31)
        }

        var a = h0
        var b = h1
        var c = h2
        var d = h3
        var e = h4

        for (i in 0 until 80) {
            val (f, k) = when (i) {
                in 0..19 -> Pair((b and c) or (b.inv() and d), 0x5A827999u)
                in 20..39 -> Pair(b xor c xor d, 0x6ED9EBA1u)
                in 40..59 -> Pair((b and c) or (b and d) or (c and d), 0x8F1BBCDCu)
                else -> Pair(b xor c xor d, 0xCA62C1D6u)
            }
            val temp = ((a shl 5) or (a shr 27)) + f + e + k + w[i]
            e = d
            d = c
            c = (b shl 30) or (b shr 2)
            b = a
            a = temp
        }

        h0 += a
        h1 += b
        h2 += c
        h3 += d
        h4 += e
    }

    val state = uintArrayOf(h0, h1, h2, h3, h4)
    return state.toBigEndianBytes(wordBytes = 4)
}

public fun sha224(data: ByteArray): ByteArray? =
    sha256FamilyDigest(
        data = data,
        initialState =
            uintArrayOf(
                0xc1059ed8u,
                0x367cd507u,
                0x3070dd17u,
                0xf70e5939u,
                0xffc00b31u,
                0x68581511u,
                0x64f98fa7u,
                0xbefa4fa4u,
            ),
        outputSize = 28,
    )

public fun sha256(data: ByteArray): ByteArray? =
    sha256FamilyDigest(
        data = data,
        initialState =
            uintArrayOf(
                0x6a09e667u,
                0xbb67ae85u,
                0x3c6ef372u,
                0xa54ff53au,
                0x510e527fu,
                0x9b05688cu,
                0x1f83d9abu,
                0x5be0cd19u,
            ),
        outputSize = 32,
    )

public fun sha384(data: ByteArray): ByteArray? =
    sha512FamilyDigest(
        data = data,
        initialState =
            ulongArrayOf(
                0xcbbb9d5dc1059ed8uL,
                0x629a292a367cd507uL,
                0x9159015a3070dd17uL,
                0x152fecd8f70e5939uL,
                0x67332667ffc00b31uL,
                0x8eb44a8768581511uL,
                0xdb0c2e0d64f98fa7uL,
                0x47b5481dbefa4fa4uL,
            ),
        outputSize = 48,
    )

public fun sha512(data: ByteArray): ByteArray? =
    sha512FamilyDigest(
        data = data,
        initialState =
            ulongArrayOf(
                0x6a09e667f3bcc908uL,
                0xbb67ae8584caa73buL,
                0x3c6ef372fe94f82buL,
                0xa54ff53a5f1d36f1uL,
                0x510e527fade682d1uL,
                0x9b05688c2b3e6c1fuL,
                0x1f83d9abfb41bd6buL,
                0x5be0cd19137e2179uL,
            ),
        outputSize = 64,
    )

private fun sha256FamilyDigest(
    data: ByteArray,
    initialState: UIntArray,
    outputSize: Int,
): ByteArray {
    val blocks = paddedBlocks(data, blockSize = 64, lengthFieldSize = 8)
    val state = initialState.copyOf()
    compress256(state, blocks)
    return state.toBigEndianBytes(wordBytes = 4).copyOf(outputSize)
}

private fun sha512FamilyDigest(
    data: ByteArray,
    initialState: ULongArray,
    outputSize: Int,
): ByteArray {
    val blocks = paddedBlocks(data, blockSize = 128, lengthFieldSize = 16)
    val state = initialState.copyOf()
    compress512(state, blocks)
    return state.toBigEndianBytes(wordBytes = 8).copyOf(outputSize)
}

private fun paddedBlocks(
    data: ByteArray,
    blockSize: Int,
    lengthFieldSize: Int,
): Array<ByteArray> {
    val paddedSize = ((data.size + 1 + lengthFieldSize + blockSize - 1) / blockSize) * blockSize
    val padded = ByteArray(paddedSize)
    data.copyInto(padded)
    padded[data.size] = 0x80.toByte()
    val bitLength = data.size.toULong() * 8uL
    writeU64BigEndian(padded, padded.size - 8, bitLength)
    return Array(padded.size / blockSize) { index ->
        padded.copyOfRange(index * blockSize, (index + 1) * blockSize)
    }
}

private fun writeU64BigEndian(
    target: ByteArray,
    offset: Int,
    value: ULong,
) {
    for (i in 0 until 8) {
        target[offset + i] = ((value shr ((7 - i) * 8)) and 0xffuL).toByte()
    }
}

private fun UIntArray.toBigEndianBytes(wordBytes: Int): ByteArray {
    val out = ByteArray(size * wordBytes)
    forEachIndexed { index, word ->
        val offset = index * wordBytes
        out[offset] = ((word shr 24) and 0xffu).toByte()
        out[offset + 1] = ((word shr 16) and 0xffu).toByte()
        out[offset + 2] = ((word shr 8) and 0xffu).toByte()
        out[offset + 3] = (word and 0xffu).toByte()
    }
    return out
}

private fun ULongArray.toBigEndianBytes(wordBytes: Int): ByteArray {
    val out = ByteArray(size * wordBytes)
    forEachIndexed { index, word ->
        val offset = index * wordBytes
        for (i in 0 until wordBytes) {
            out[offset + i] = ((word shr ((wordBytes - 1 - i) * 8)) and 0xffuL).toByte()
        }
    }
    return out
}
