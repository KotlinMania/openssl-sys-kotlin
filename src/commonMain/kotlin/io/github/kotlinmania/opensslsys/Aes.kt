// port-lint: source aes.rs
package io.github.kotlinmania.opensslsys

// Upstream gate: cfg(not(osslconf = "OPENSSL_NO_DEPRECATED_3_0"))
public const val AES_ENCRYPT: Int = 1

// Upstream gate: cfg(not(osslconf = "OPENSSL_NO_DEPRECATED_3_0"))
public const val AES_DECRYPT: Int = 0

// Upstream gate: cfg(not(osslconf = "OPENSSL_NO_DEPRECATED_3_0"))
public const val AES_MAXNR: Int = 14

public const val AES_BLOCK_SIZE: Int = 16
