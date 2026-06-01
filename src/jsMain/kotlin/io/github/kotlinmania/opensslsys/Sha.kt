// port-lint: source src/sha.rs
package io.github.kotlinmania.opensslsys

import org.khronos.webgl.Uint8Array

// Upstream gate: cfg(not(osslconf = "OPENSSL_NO_DEPRECATED_3_0"))
public const val SHA_LBLOCK: Int = 16

// Upstream gate: cfg(not(osslconf = "OPENSSL_NO_DEPRECATED_3_0"))
public typealias ShaLong = UInt

// Upstream gate: cfg(not(osslconf = "OPENSSL_NO_DEPRECATED_3_0"))
public typealias ShaLong64 = ULong

// Upstream gate: cfg(ossl300). Upstream notes "Ideally we'd macro define
// these, but that crashes ctest" — the Rust port hand-writes wrappers
// over EVP_Q_digest. The Kotlin/JS port routes those wrappers through
// the Node N-API addon at node/src/openssl_shim.cc, which calls the same
// EVP_Q_digest from the host's OpenSSL 3 install. Browser callers raise
// UnsupportedOperationException via opensslShimOrThrow().

public fun sha1(data: Uint8Array): Uint8Array? = opensslShimOrThrow().evpQDigest("SHA1", data)

public fun sha224(data: Uint8Array): Uint8Array? = opensslShimOrThrow().evpQDigest("SHA224", data)

public fun sha256(data: Uint8Array): Uint8Array? = opensslShimOrThrow().evpQDigest("SHA256", data)

public fun sha384(data: Uint8Array): Uint8Array? = opensslShimOrThrow().evpQDigest("SHA384", data)

public fun sha512(data: Uint8Array): Uint8Array? = opensslShimOrThrow().evpQDigest("SHA512", data)
