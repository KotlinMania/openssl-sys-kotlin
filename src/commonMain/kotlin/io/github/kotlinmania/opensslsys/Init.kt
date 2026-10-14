// port-lint: source lib.rs
package io.github.kotlinmania.opensslsys

import io.github.kotlinmania.libc.CInt
import kotlin.concurrent.atomics.AtomicBoolean

internal val INIT: AtomicBoolean = AtomicBoolean(false)

internal typealias PasswordCallback = (buf: ByteArray, size: CInt, rwflag: CInt, userData: Long) -> CInt

public fun interface OpensslPasswordCallback {
    public fun invoke(
        buf: ByteArray,
        size: CInt,
        rwflag: CInt,
        userData: Long,
    ): CInt
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
): io.github.kotlinmania.libc.CLong =
    bioCtrl(b, BIO_CTRL_INFO, 0L, if (pp.isNotEmpty()) pp[0] else 0L)
