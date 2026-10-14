// port-lint: source types.rs
package io.github.kotlinmania.opensslsys

/**
 * Public key algorithm context and structure (`EVP_PKEY`).
 */
public class EvpPkey(
    public val address: Long = 0L,
)

/**
 * Signature algorithm context and structure (`EVP_SIGNATURE`).
 */
public class EvpSignature(
    public val address: Long = 0L,
)
