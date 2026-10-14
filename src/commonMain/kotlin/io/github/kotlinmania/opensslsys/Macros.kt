// port-lint: source macros.rs
package io.github.kotlinmania.opensslsys

/**
 * Marker interface for typed OpenSSL stack handles (`STACK_OF(T)`).
 */
public interface OpenSslStackHandle {
    public val address: Long
}
