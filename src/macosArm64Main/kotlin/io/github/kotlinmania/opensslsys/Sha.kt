// port-lint: source src/sha.rs
package io.github.kotlinmania.opensslsys

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.UByteVar
import openssl.EVP_Q_digest
import platform.posix.size_t

// Upstream gate: cfg(not(osslconf = "OPENSSL_NO_DEPRECATED_3_0"))
public const val SHA_LBLOCK: Int = 16

// Upstream gate: cfg(not(osslconf = "OPENSSL_NO_DEPRECATED_3_0"))
public typealias ShaLong = UInt

// Upstream gate: cfg(not(osslconf = "OPENSSL_NO_DEPRECATED_3_0"))
public typealias ShaLong64 = ULong

// Upstream gate: cfg(ossl300). Ideally these would be macros, but that
// crashes ctest in upstream Rust, so they are hand-written wrappers over
// EVP_Q_digest. Kotlin/Native cinterop auto-converts the const char *
// algorithm-name argument from a Kotlin String, so the explicit
// null-terminator the Rust version appends manually is unnecessary here.
@OptIn(ExperimentalForeignApi::class)
public fun sha1(
    d: CPointer<UByteVar>?,
    n: size_t,
    md: CPointer<UByteVar>?,
): CPointer<UByteVar>? = if (EVP_Q_digest(null, "SHA1", null, d, n, md, null) != 0) md else null

@OptIn(ExperimentalForeignApi::class)
public fun sha224(
    d: CPointer<UByteVar>?,
    n: size_t,
    md: CPointer<UByteVar>?,
): CPointer<UByteVar>? = if (EVP_Q_digest(null, "SHA224", null, d, n, md, null) != 0) md else null

@OptIn(ExperimentalForeignApi::class)
public fun sha256(
    d: CPointer<UByteVar>?,
    n: size_t,
    md: CPointer<UByteVar>?,
): CPointer<UByteVar>? = if (EVP_Q_digest(null, "SHA256", null, d, n, md, null) != 0) md else null

@OptIn(ExperimentalForeignApi::class)
public fun sha384(
    d: CPointer<UByteVar>?,
    n: size_t,
    md: CPointer<UByteVar>?,
): CPointer<UByteVar>? = if (EVP_Q_digest(null, "SHA384", null, d, n, md, null) != 0) md else null

@OptIn(ExperimentalForeignApi::class)
public fun sha512(
    d: CPointer<UByteVar>?,
    n: size_t,
    md: CPointer<UByteVar>?,
): CPointer<UByteVar>? = if (EVP_Q_digest(null, "SHA512", null, d, n, md, null) != 0) md else null
