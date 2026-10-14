// port-lint: source crypto.rs
package io.github.kotlinmania.opensslsys

import io.github.kotlinmania.constanttimeeq.constantTimeEq
import io.github.kotlinmania.libc.CInt
import io.github.kotlinmania.libc.CLong
import io.github.kotlinmania.libc.CULong
import io.github.kotlinmania.zeroize.zeroize

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
public const val OPENSSL_ENGINES_DIR: CInt = 5
public const val OPENSSL_VERSION_STRING: CInt = 6
public const val OPENSSL_FULL_VERSION_STRING: CInt = 7
public const val OPENSSL_MODULES_DIR: CInt = 8
public const val OPENSSL_CPU_INFO: CInt = 9

public const val CRYPTO_LOCK: CInt = 1

internal typealias CryptoExNew = (parent: Any?, ptr: Any?, ad: Any?, idx: CInt, argl: CLong, argp: Any?) -> Unit
internal typealias CryptoExDup = (to: Any?, from: Any?, fromD: Any?, idx: CInt, argl: CLong, argp: Any?) -> CInt
internal typealias CryptoExFree = (parent: Any?, ptr: Any?, ad: Any?, idx: CInt, argl: CLong, argp: Any?) -> Unit

public fun interface OpensslCryptoExNew {
    public fun invoke(
        parent: Long,
        ptr: Long,
        ad: CryptoExData?,
        idx: CInt,
        argl: CLong,
        argp: Long,
    )
}

public fun interface OpensslCryptoExDup {
    public fun invoke(
        to: CryptoExData?,
        from: CryptoExData?,
        fromD: Long,
        idx: CInt,
        argl: CLong,
        argp: Long,
    ): CInt
}

public fun interface OpensslCryptoExFree {
    public fun invoke(
        parent: Long,
        ptr: Long,
        ad: CryptoExData?,
        idx: CInt,
        argl: CLong,
        argp: Long,
    )
}

public expect fun openSslVersionNum(): CULong

public expect fun openSslVersion(key: CInt): String?

public expect fun opensslInitCrypto(
    opts: ULong,
    settings: OpensslInitSettings? = null,
): CInt

public expect fun cryptoMalloc(
    num: ULong,
    file: String = "openssl-sys-kotlin",
    line: CInt = 0,
): Long

public expect fun cryptoFree(
    addr: Long,
    file: String = "openssl-sys-kotlin",
    line: CInt = 0,
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
): CInt {
    if (len < 0 || a.size < len || b.size < len) return 1
    if (len == 0) return 0
    val sliceA = if (a.size == len) a else a.copyOfRange(0, len)
    val sliceB = if (b.size == len) b else b.copyOfRange(0, len)
    return if (constantTimeEq(sliceA, sliceB)) 0 else 1
}

public fun opensslCleanse(buf: ByteArray) {
    buf.zeroize()
}
