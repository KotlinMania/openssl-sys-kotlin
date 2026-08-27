// port-lint: source openssl-sys/src/tls1.rs
package io.github.kotlinmania.opensslsys

public const val TLS1_VERSION: Int = 0x301
public const val TLS1_1_VERSION: Int = 0x302
public const val TLS1_2_VERSION: Int = 0x303
public const val TLS1_3_VERSION: Int = 0x304

public const val DTLS1_VERSION: Int = 0xFEFF
public const val DTLS1_2_VERSION: Int = 0xFEFD

public const val TLS1_AD_DECODE_ERROR: Int = 50
public const val TLS1_AD_UNRECOGNIZED_NAME: Int = 112

public const val TLSEXT_NAMETYPE_host_name: Int = 0
public const val TLSEXT_STATUSTYPE_ocsp: Int = 1

public const val SSL_TLSEXT_ERR_OK: Int = 0
public const val SSL_TLSEXT_ERR_ALERT_WARNING: Int = 1
public const val SSL_TLSEXT_ERR_ALERT_FATAL: Int = 2
public const val SSL_TLSEXT_ERR_NOACK: Int = 3
