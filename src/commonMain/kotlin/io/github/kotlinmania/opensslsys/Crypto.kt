// port-lint: source openssl-sys/src/crypto.rs
package io.github.kotlinmania.opensslsys

public const val CRYPTO_LOCK_X509: Int = 3
public const val CRYPTO_LOCK_EVP_PKEY: Int = 10
public const val CRYPTO_LOCK_SSL_CTX: Int = 12
public const val CRYPTO_LOCK_SSL_SESSION: Int = 14

public const val CRYPTO_EX_INDEX_SSL: Int = 0
public const val CRYPTO_EX_INDEX_SSL_CTX: Int = 1

public const val OPENSSL_VERSION: Int = 0
public const val OPENSSL_CFLAGS: Int = 1
public const val OPENSSL_BUILT_ON: Int = 2
public const val OPENSSL_PLATFORM: Int = 3
public const val OPENSSL_DIR: Int = 4

public const val CRYPTO_LOCK: Int = 1

public typealias CryptoExNew = (parent: Any?, ptr: Any?, ad: Any?, idx: Int, argl: Long, argp: Any?) -> Unit
public typealias CryptoExDup = (to: Any?, from: Any?, fromD: Any?, idx: Int, argl: Long, argp: Any?) -> Int
public typealias CryptoExFree = (parent: Any?, ptr: Any?, ad: Any?, idx: Int, argl: Long, argp: Any?) -> Unit
