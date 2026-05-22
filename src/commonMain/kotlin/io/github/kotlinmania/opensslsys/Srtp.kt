// port-lint: source src/srtp.rs
package io.github.kotlinmania.opensslsys

public const val SRTP_AES128_CM_SHA1_80: ULong = 0x0001uL
public const val SRTP_AES128_CM_SHA1_32: ULong = 0x0002uL
public const val SRTP_AES128_F8_SHA1_80: ULong = 0x0003uL
public const val SRTP_AES128_F8_SHA1_32: ULong = 0x0004uL
public const val SRTP_NULL_SHA1_80: ULong = 0x0005uL
public const val SRTP_NULL_SHA1_32: ULong = 0x0006uL

/* AEAD SRTP protection profiles from RFC 7714 */
// Upstream gate: cfg(ossl110)
public const val SRTP_AEAD_AES_128_GCM: ULong = 0x0007uL

// Upstream gate: cfg(ossl110)
public const val SRTP_AEAD_AES_256_GCM: ULong = 0x0008uL
