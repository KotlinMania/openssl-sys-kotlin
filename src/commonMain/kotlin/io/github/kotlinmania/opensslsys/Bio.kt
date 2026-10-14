// port-lint: source bio.rs
package io.github.kotlinmania.opensslsys

import io.github.kotlinmania.libc.CInt

public const val BIO_TYPE_NONE: CInt = 0

public const val BIO_CTRL_EOF: CInt = 2
public const val BIO_CTRL_INFO: CInt = 3
public const val BIO_CTRL_FLUSH: CInt = 11
public const val BIO_CTRL_DGRAM_QUERY_MTU: CInt = 40
public const val BIO_C_SET_BUF_MEM_EOF_RETURN: CInt = 130

public const val BIO_FLAGS_READ: CInt = 0x01
public const val BIO_FLAGS_WRITE: CInt = 0x02
public const val BIO_FLAGS_IO_SPECIAL: CInt = 0x04
public val BIO_FLAGS_RWS: CInt = BIO_FLAGS_READ or BIO_FLAGS_WRITE or BIO_FLAGS_IO_SPECIAL
public const val BIO_FLAGS_SHOULD_RETRY: CInt = 0x08

// OpenSSL 3.2+ datagram constants
public const val BIO_CTRL_DGRAM_GET_MTU: CInt = 41
public const val BIO_CTRL_DGRAM_SET_MTU: CInt = 42
public const val BIO_CTRL_DGRAM_GET_LOCAL_ADDR_CAP: CInt = 82
public const val BIO_CTRL_DGRAM_GET_LOCAL_ADDR_ENABLE: CInt = 83
public const val BIO_CTRL_DGRAM_SET_LOCAL_ADDR_ENABLE: CInt = 84
public const val BIO_CTRL_DGRAM_GET_CAPS: CInt = 86
public const val BIO_CTRL_DGRAM_SET_CAPS: CInt = 87
public const val BIO_CTRL_DGRAM_GET_NO_TRUNC: CInt = 88
public const val BIO_CTRL_DGRAM_SET_NO_TRUNC: CInt = 89
