// port-lint: source bio.rs
package io.github.kotlinmania.opensslsys

import io.github.kotlinmania.libc.CInt
import io.github.kotlinmania.libc.CLong

public const val BIO_TYPE_NONE: CInt = 0

public const val BIO_CTRL_RESET: CInt = 1
public const val BIO_CTRL_EOF: CInt = 2
public const val BIO_CTRL_INFO: CInt = 3
public const val BIO_CTRL_PENDING: CInt = 10
public const val BIO_CTRL_FLUSH: CInt = 11
public const val BIO_CTRL_WPENDING: CInt = 13
public const val BIO_CTRL_DGRAM_QUERY_MTU: CInt = 40
public const val BIO_C_SET_BUF_MEM_EOF_RETURN: CInt = 130

public fun bioSetFlags(
    b: Bio,
    flags: CInt,
) {
    bioCtrl(b, BIO_CTRL_INFO, flags.toLong(), 0L)
}

public fun bioClearFlags(
    b: Bio,
    flags: CInt,
) {
    bioCtrl(b, BIO_CTRL_INFO, flags.toLong(), 0L)
}

public fun bioSetRetryRead(b: Bio) {
    bioSetFlags(b, BIO_FLAGS_READ or BIO_FLAGS_SHOULD_RETRY)
}

public fun bioSetRetryWrite(b: Bio) {
    bioSetFlags(b, BIO_FLAGS_WRITE or BIO_FLAGS_SHOULD_RETRY)
}

public fun bioClearRetryFlags(b: Bio) {
    bioClearFlags(b, BIO_FLAGS_RWS or BIO_FLAGS_SHOULD_RETRY)
}

public const val BIO_FLAGS_READ: CInt = 0x01
public const val BIO_FLAGS_WRITE: CInt = 0x02
public const val BIO_FLAGS_IO_SPECIAL: CInt = 0x04
public val BIO_FLAGS_RWS: CInt = BIO_FLAGS_READ or BIO_FLAGS_WRITE or BIO_FLAGS_IO_SPECIAL
public const val BIO_FLAGS_SHOULD_RETRY: CInt = 0x08

public const val BIO_CTRL_DGRAM_GET_MTU: CInt = 41
public const val BIO_CTRL_DGRAM_SET_MTU: CInt = 42
public const val BIO_CTRL_DGRAM_GET_LOCAL_ADDR_CAP: CInt = 82
public const val BIO_CTRL_DGRAM_GET_LOCAL_ADDR_ENABLE: CInt = 83
public const val BIO_CTRL_DGRAM_SET_LOCAL_ADDR_ENABLE: CInt = 84
public const val BIO_CTRL_DGRAM_GET_CAPS: CInt = 86
public const val BIO_CTRL_DGRAM_SET_CAPS: CInt = 87
public const val BIO_CTRL_DGRAM_GET_NO_TRUNC: CInt = 88
public const val BIO_CTRL_DGRAM_SET_NO_TRUNC: CInt = 89

public expect fun bioSMem(): BioMethod?

public expect fun bioNew(type: BioMethod): Bio?

public expect fun bioFreeAll(b: Bio?)

public expect fun bioRead(
    b: Bio,
    buf: ByteArray,
    len: CInt = buf.size,
): CInt

public expect fun bioWrite(
    b: Bio,
    buf: ByteArray,
    len: CInt = buf.size,
): CInt

public expect fun bioCtrl(
    b: Bio,
    cmd: CInt,
    larg: CLong,
    parg: Long = 0L,
): CLong

public expect fun bioGetMemData(b: Bio): ByteArray

public fun bioGetMemData(
    b: Bio,
    pp: Long,
): CLong = bioCtrl(b, BIO_CTRL_INFO, 0L, pp)

public fun bioPending(b: Bio): CLong = bioCtrl(b, BIO_CTRL_PENDING, 0L, 0L)

public fun bioWpending(b: Bio): CLong = bioCtrl(b, BIO_CTRL_WPENDING, 0L, 0L)

public fun bioFlush(b: Bio): CInt = bioCtrl(b, BIO_CTRL_FLUSH, 0L, 0L).toInt()

public fun bioReset(b: Bio): CInt = bioCtrl(b, BIO_CTRL_RESET, 0L, 0L).toInt()

public fun bioEof(b: Bio): CInt = bioCtrl(b, BIO_CTRL_EOF, 0L, 0L).toInt()

public fun bioSetMemEofReturn(
    b: Bio,
    v: CInt,
): CLong = bioCtrl(b, BIO_C_SET_BUF_MEM_EOF_RETURN, v.toLong(), 0L)

public fun bioDgramGetNoTrunc(bio: Bio): CInt =
    bioCtrl(bio, BIO_CTRL_DGRAM_GET_NO_TRUNC, 0L, 0L).toInt()

public fun bioDgramSetNoTrunc(
    bio: Bio,
    enable: CInt,
): CInt = bioCtrl(bio, BIO_CTRL_DGRAM_SET_NO_TRUNC, enable.toLong(), 0L).toInt()

public fun bioDgramGetCap(bio: Bio): UInt =
    bioCtrl(bio, BIO_CTRL_DGRAM_GET_CAPS, 0L, 0L).toUInt()

public fun bioDgramSetCap(
    bio: Bio,
    cap: UInt,
): CInt = bioCtrl(bio, BIO_CTRL_DGRAM_SET_CAPS, cap.toLong(), 0L).toInt()

public fun bioDgramGetLocalAddrCap(bio: Bio): CInt =
    bioCtrl(bio, BIO_CTRL_DGRAM_GET_LOCAL_ADDR_CAP, 0L, 0L).toInt()

public fun bioDgramGetLocalAddrEnable(
    bio: Bio,
    enable: Long,
): CInt = bioCtrl(bio, BIO_CTRL_DGRAM_GET_LOCAL_ADDR_ENABLE, 0L, enable).toInt()

public fun bioDgramSetLocalAddrEnable(
    bio: Bio,
    enable: CInt,
): CInt = bioCtrl(bio, BIO_CTRL_DGRAM_SET_LOCAL_ADDR_ENABLE, enable.toLong(), 0L).toInt()

public fun bioDgramGetMtu(bio: Bio): UInt =
    bioCtrl(bio, BIO_CTRL_DGRAM_GET_MTU, 0L, 0L).toUInt()

public fun bioDgramSetMtu(
    bio: Bio,
    mtu: UInt,
): CInt = bioCtrl(bio, BIO_CTRL_DGRAM_SET_MTU, mtu.toLong(), 0L).toInt()
