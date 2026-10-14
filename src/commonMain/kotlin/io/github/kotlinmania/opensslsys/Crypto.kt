// port-lint: source crypto.rs
package io.github.kotlinmania.opensslsys

import io.github.kotlinmania.libc.CInt
import io.github.kotlinmania.libc.CLong

public const val CRYPTO_LOCK_X509: CInt = 3
public const val CRYPTO_LOCK_EVP_PKEY: CInt = 10
public const val CRYPTO_LOCK_SSL_CTX: CInt = 12
public const val CRYPTO_LOCK_SSL_SESSION: CInt = 14

public const val CRYPTO_EX_INDEX_SSL: CInt = 0
public const val CRYPTO_EX_INDEX_SSL_CTX: CInt = 1

public const val OPENSSL_VERSION: CInt = 0
public const val OPENSSL_CFLAGS: CInt = 1
public const val OPENSSL_BUILT_ON: CInt = 2
public const val OPENSSL_PLATFORM: CInt = 3
public const val OPENSSL_DIR: CInt = 4

public const val CRYPTO_LOCK: CInt = 1

public typealias CryptoExNew = (parent: Any?, ptr: Any?, ad: Any?, idx: CInt, argl: CLong, argp: Any?) -> Unit
public typealias CryptoExDup = (to: Any?, from: Any?, fromD: Any?, idx: CInt, argl: CLong, argp: Any?) -> CInt
public typealias CryptoExFree = (parent: Any?, ptr: Any?, ad: Any?, idx: CInt, argl: CLong, argp: Any?) -> Unit

public fun opensslMalloc(num: Int): ByteArray = ByteArray(num)

public fun opensslFree(addr: Any?) {
    // Managed memory reclamation is handled by the garbage collector
}
