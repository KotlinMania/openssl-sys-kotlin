// port-lint: source crypto.rs
package io.github.kotlinmania.opensslsys

import io.github.kotlinmania.constanttimeeq.constantTimeEq
import io.github.kotlinmania.zeroize.zeroize

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
public const val OPENSSL_ENGINES_DIR: Int = 5
public const val OPENSSL_VERSION_STRING: Int = 6
public const val OPENSSL_FULL_VERSION_STRING: Int = 7
public const val OPENSSL_MODULES_DIR: Int = 8
public const val OPENSSL_CPU_INFO: Int = 9

public const val CRYPTO_LOCK: Int = 1

internal typealias CryptoExNew = (parent: Any?, ptr: Any?, ad: Any?, idx: Int, argl: Long, argp: Any?) -> Unit
internal typealias CryptoExDup = (to: Any?, from: Any?, fromD: Any?, idx: Int, argl: Long, argp: Any?) -> Int
internal typealias CryptoExFree = (parent: Any?, ptr: Any?, ad: Any?, idx: Int, argl: Long, argp: Any?) -> Unit

public fun interface OpensslCryptoExNew {
    public fun invoke(
        parent: Long,
        ptr: Long,
        ad: CryptoExData?,
        idx: Int,
        argl: Long,
        argp: Long,
    )
}

public fun interface OpensslCryptoExDup {
    public fun invoke(
        to: CryptoExData?,
        from: CryptoExData?,
        fromD: Long,
        idx: Int,
        argl: Long,
        argp: Long,
    ): Int
}

public fun interface OpensslCryptoExFree {
    public fun invoke(
        parent: Long,
        ptr: Long,
        ad: CryptoExData?,
        idx: Int,
        argl: Long,
        argp: Long,
    )
}

public expect fun openSslVersionNum(): ULong

public expect fun openSslVersion(key: Int): String?

public expect fun opensslInitCrypto(
    opts: ULong,
    settings: OpensslInitSettings? = null,
): Int

public expect fun cryptoMalloc(
    num: ULong,
    file: String = "openssl-sys-kotlin",
    line: Int = 0,
): Long

public expect fun cryptoFree(
    addr: Long,
    file: String = "openssl-sys-kotlin",
    line: Int = 0,
)

public fun opensslMalloc(num: ULong): Long = cryptoMalloc(num, "Crypto.kt", 0)

public fun opensslMalloc(num: Int): Long = cryptoMalloc(num.toULong(), "Crypto.kt", 0)

public fun opensslFree(addr: Long) {
    cryptoFree(addr, "Crypto.kt", 0)
}

public fun cryptoMemcmp(
    a: ByteArray,
    b: ByteArray,
    len: Int = a.size.coerceAtMost(b.size),
): Int {
    if (len < 0 || a.size < len || b.size < len) return 1
    if (len == 0) return 0
    val sliceA = if (a.size == len) a else a.copyOfRange(0, len)
    val sliceB = if (b.size == len) b else b.copyOfRange(0, len)
    return if (constantTimeEq(sliceA, sliceB)) 0 else 1
}

public fun opensslCleanse(buf: ByteArray) {
    buf.zeroize()
}
