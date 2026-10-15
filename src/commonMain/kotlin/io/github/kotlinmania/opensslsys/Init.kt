// port-lint: source lib.rs
package io.github.kotlinmania.opensslsys

import kotlin.concurrent.atomics.AtomicBoolean

internal val INIT: AtomicBoolean = AtomicBoolean(false)

internal typealias PasswordCallback = (buf: ByteArray, size: Int, rwflag: Int, userData: Long) -> Int

public fun interface OpensslPasswordCallback {
    public fun invoke(
        buf: ByteArray,
        size: Int,
        rwflag: Int,
        userData: Long,
    ): Int
}

/**
 * Initializes the OpenSSL library once.
 */
public fun opensslInit() {
    if (INIT.compareAndSet(expectedValue = false, newValue = true)) {
        val initOptions = OPENSSL_INIT_LOAD_SSL_STRINGS or OPENSSL_INIT_NO_ATEXIT
        opensslInitSsl(initOptions, null)
    }
}

internal fun init() {
    opensslInit()
}

internal fun bioGetMemData(
    b: Bio,
    pp: LongArray,
): Long =
    bioCtrl(b, BIO_CTRL_INFO, 0L, if (pp.isNotEmpty()) pp[0] else 0L)
