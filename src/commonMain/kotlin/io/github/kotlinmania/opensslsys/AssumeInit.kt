package io.github.kotlinmania.opensslsys

/**
 * Disables explicit initialization of the OpenSSL libraries.
 *
 * This is only appropriate to use if the OpenSSL library is being consumed by an application
 * that will be performing the initialization explicitly.
 */
public fun assumeInit() {
    INIT.store(true)
}
