// port-lint: source openssl-sys/src/bio.rs
package io.github.kotlinmania.opensslsys

public const val BIO_TYPE_NONE: Int = 0

public const val BIO_CTRL_EOF: Int = 2
public const val BIO_CTRL_INFO: Int = 3
public const val BIO_CTRL_FLUSH: Int = 11
public const val BIO_CTRL_DGRAM_QUERY_MTU: Int = 40
public const val BIO_C_SET_BUF_MEM_EOF_RETURN: Int = 130

public const val BIO_FLAGS_READ: Int = 0x01
public const val BIO_FLAGS_WRITE: Int = 0x02
public const val BIO_FLAGS_IO_SPECIAL: Int = 0x04
public val BIO_FLAGS_RWS: Int = BIO_FLAGS_READ or BIO_FLAGS_WRITE or BIO_FLAGS_IO_SPECIAL
public const val BIO_FLAGS_SHOULD_RETRY: Int = 0x08

// OpenSSL 3.2+ datagram constants
public const val BIO_CTRL_DGRAM_GET_MTU: Int = 41
public const val BIO_CTRL_DGRAM_SET_MTU: Int = 42
public const val BIO_CTRL_DGRAM_GET_LOCAL_ADDR_CAP: Int = 82
public const val BIO_CTRL_DGRAM_GET_LOCAL_ADDR_ENABLE: Int = 83
public const val BIO_CTRL_DGRAM_SET_LOCAL_ADDR_ENABLE: Int = 84
public const val BIO_CTRL_DGRAM_GET_CAPS: Int = 86
public const val BIO_CTRL_DGRAM_SET_CAPS: Int = 87
public const val BIO_CTRL_DGRAM_GET_NO_TRUNC: Int = 88
public const val BIO_CTRL_DGRAM_SET_NO_TRUNC: Int = 89
