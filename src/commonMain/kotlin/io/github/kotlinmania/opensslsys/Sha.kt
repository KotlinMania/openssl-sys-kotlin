// port-lint: source sha.rs
package io.github.kotlinmania.opensslsys

// Upstream gate: cfg(not(osslconf = "OPENSSL_NO_DEPRECATED_3_0"))
public const val SHA_LBLOCK: Int = 16

public const val SHA_DIGEST_LENGTH: Int = 20
public const val SHA224_DIGEST_LENGTH: Int = 28
public const val SHA256_DIGEST_LENGTH: Int = 32
public const val SHA384_DIGEST_LENGTH: Int = 48
public const val SHA512_DIGEST_LENGTH: Int = 64

// Upstream gate: cfg(not(osslconf = "OPENSSL_NO_DEPRECATED_3_0"))
public typealias ShaLong = UInt

// Upstream gate: cfg(not(osslconf = "OPENSSL_NO_DEPRECATED_3_0"))
public typealias ShaLong64 = ULong

public expect fun sha1(data: ByteArray): ByteArray?

public expect fun sha224(data: ByteArray): ByteArray?

public expect fun sha256(data: ByteArray): ByteArray?

public expect fun sha384(data: ByteArray): ByteArray?

public expect fun sha512(data: ByteArray): ByteArray?
