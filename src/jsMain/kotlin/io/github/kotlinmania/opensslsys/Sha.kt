// port-lint: source src/sha.rs
package io.github.kotlinmania.opensslsys

import org.khronos.webgl.Uint8Array

// Upstream gate: cfg(not(osslconf = "OPENSSL_NO_DEPRECATED_3_0"))
public const val SHA_LBLOCK: Int = 16

// Upstream gate: cfg(not(osslconf = "OPENSSL_NO_DEPRECATED_3_0"))
public typealias SHA_LONG = UInt

// Upstream gate: cfg(not(osslconf = "OPENSSL_NO_DEPRECATED_3_0"))
public typealias SHA_LONG64 = ULong

// Upstream gate: cfg(ossl300). Upstream notes "Ideally we'd macro define
// these, but that crashes ctest" — the Rust port hand-writes wrappers
// over EVP_Q_digest. The Kotlin/JS port routes those wrappers through
// the Node N-API addon at node/src/openssl_shim.cc, which calls the same
// EVP_Q_digest from the host's OpenSSL 3 install. Browser callers raise
// UnsupportedOperationException via opensslShimOrThrow().

public fun SHA1(data: Uint8Array): Uint8Array? =
    opensslShimOrThrow().evpQDigest("SHA1", data)

public fun SHA224(data: Uint8Array): Uint8Array? =
    opensslShimOrThrow().evpQDigest("SHA224", data)

public fun SHA256(data: Uint8Array): Uint8Array? =
    opensslShimOrThrow().evpQDigest("SHA256", data)

public fun SHA384(data: Uint8Array): Uint8Array? =
    opensslShimOrThrow().evpQDigest("SHA384", data)

public fun SHA512(data: Uint8Array): Uint8Array? =
    opensslShimOrThrow().evpQDigest("SHA512", data)
