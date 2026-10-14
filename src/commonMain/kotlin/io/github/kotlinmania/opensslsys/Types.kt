// port-lint: source types.rs handwritten/types.rs
package io.github.kotlinmania.opensslsys

/**
 * Public key algorithm context / structure (`EVP_PKEY`).
 */
public class EvpPkey(
    public val address: Long = 0L,
)

/**
 * Signature algorithm context / structure (`EVP_SIGNATURE`).
 */
public class EvpSignature(
    public val address: Long = 0L,
)

/**
 * Opaque OpenSSL I/O abstraction (`BIO`).
 */
public class Bio(
    public val address: Long = 0L,
)

/**
 * Opaque OpenSSL BIO method dispatch table (`BIO_METHOD`).
 */
public class BioMethod(
    public val address: Long = 0L,
)

/**
 * Opaque OpenSSL digest algorithm descriptor (`EVP_MD`).
 */
public class EvpMd(
    public val address: Long = 0L,
)

/**
 * Opaque OpenSSL digest context (`EVP_MD_CTX`).
 */
public class EvpMdCtx(
    public val address: Long = 0L,
)

/**
 * Opaque OpenSSL engine handle (`ENGINE`).
 */
public class Engine(
    public val address: Long = 0L,
)

/**
 * Opaque OpenSSL initialization settings (`OPENSSL_INIT_SETTINGS`).
 */
public class OpensslInitSettings(
    public val address: Long = 0L,
)

/**
 * Opaque OpenSSL library context (`OSSL_LIB_CTX`).
 */
public class OsslLibCtx(
    public val address: Long = 0L,
)

/**
 * Opaque OpenSSL TLS/SSL method descriptor (`SSL_METHOD`).
 */
public class SslMethod(
    public val address: Long = 0L,
)

/**
 * Opaque OpenSSL TLS/SSL context (`SSL_CTX`).
 */
public class SslCtx(
    public val address: Long = 0L,
)

/**
 * Opaque OpenSSL TLS/SSL connection state (`SSL`).
 */
public class Ssl(
    public val address: Long = 0L,
)

/**
 * Opaque OpenSSL ASN.1 object identifier (`ASN1_OBJECT`).
 */
public class Asn1Object(
    public val address: Long = 0L,
)
