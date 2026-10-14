// port-lint: source lib.rs
package io.github.kotlinmania.opensslsys

import io.github.kotlinmania.libc.CInt
import kotlin.concurrent.atomics.AtomicBoolean

private val initOnce = AtomicBoolean(false)

public typealias PasswordCallback = (
    buf: ByteArray,
    size: CInt,
    rwflag: CInt,
    userData: Any?,
) -> CInt

/**
 * Initializes the OpenSSL library once.
 */
public fun opensslInit() {
    if (initOnce.compareAndSet(expectedValue = false, newValue = true)) {
        val initOptions = OPENSSL_INIT_LOAD_SSL_STRINGS or OPENSSL_INIT_NO_ATEXIT
        opensslInitSsl(initOptions, null)
    }
}

/**
 * Disables explicit initialization of the OpenSSL libraries.
 */
public fun assumeInit() {
    initOnce.store(true)
}
