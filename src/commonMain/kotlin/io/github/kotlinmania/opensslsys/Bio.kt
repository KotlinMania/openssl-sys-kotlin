// port-lint: source bio.rs
package io.github.kotlinmania.opensslsys

public const val BIO_TYPE_NONE: Int = 0

public const val BIO_CTRL_RESET: Int = 1
public const val BIO_CTRL_EOF: Int = 2
public const val BIO_CTRL_INFO: Int = 3
public const val BIO_CTRL_PENDING: Int = 10
public const val BIO_CTRL_FLUSH: Int = 11
public const val BIO_CTRL_WPENDING: Int = 13
public const val BIO_CTRL_DGRAM_QUERY_MTU: Int = 40
public const val BIO_C_SET_BUF_MEM_EOF_RETURN: Int = 130

public fun bioSetFlags(
    b: Bio,
    flags: Int,
) {
    bioCtrl(b, BIO_CTRL_INFO, flags.toLong(), 0L)
}

public fun bioClearFlags(
    b: Bio,
    flags: Int,
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

public const val BIO_FLAGS_READ: Int = 0x01
public const val BIO_FLAGS_WRITE: Int = 0x02
public const val BIO_FLAGS_IO_SPECIAL: Int = 0x04
public val BIO_FLAGS_RWS: Int = BIO_FLAGS_READ or BIO_FLAGS_WRITE or BIO_FLAGS_IO_SPECIAL
public const val BIO_FLAGS_SHOULD_RETRY: Int = 0x08

public const val BIO_CTRL_DGRAM_GET_MTU: Int = 41
public const val BIO_CTRL_DGRAM_SET_MTU: Int = 42
public const val BIO_CTRL_DGRAM_GET_LOCAL_ADDR_CAP: Int = 82
public const val BIO_CTRL_DGRAM_GET_LOCAL_ADDR_ENABLE: Int = 83
public const val BIO_CTRL_DGRAM_SET_LOCAL_ADDR_ENABLE: Int = 84
public const val BIO_CTRL_DGRAM_GET_CAPS: Int = 86
public const val BIO_CTRL_DGRAM_SET_CAPS: Int = 87
public const val BIO_CTRL_DGRAM_GET_NO_TRUNC: Int = 88
public const val BIO_CTRL_DGRAM_SET_NO_TRUNC: Int = 89

public expect fun bioSMem(): BioMethod?

public expect fun bioNew(type: BioMethod): Bio?

public expect fun bioFreeAll(b: Bio?)

public expect fun bioRead(
    b: Bio,
    buf: ByteArray,
    len: Int = buf.size,
): Int

public expect fun bioWrite(
    b: Bio,
    buf: ByteArray,
    len: Int = buf.size,
): Int

public expect fun bioCtrl(
    b: Bio,
    cmd: Int,
    larg: Long,
    parg: Long = 0L,
): Long

public expect fun bioGetMemData(b: Bio): ByteArray

public fun bioGetMemData(
    b: Bio,
    pp: Long,
): Long = bioCtrl(b, BIO_CTRL_INFO, 0L, pp)

public fun bioPending(b: Bio): Long = bioCtrl(b, BIO_CTRL_PENDING, 0L, 0L)

public fun bioWpending(b: Bio): Long = bioCtrl(b, BIO_CTRL_WPENDING, 0L, 0L)

public fun bioFlush(b: Bio): Int = bioCtrl(b, BIO_CTRL_FLUSH, 0L, 0L).toInt()

public fun bioReset(b: Bio): Int = bioCtrl(b, BIO_CTRL_RESET, 0L, 0L).toInt()

public fun bioEof(b: Bio): Int = bioCtrl(b, BIO_CTRL_EOF, 0L, 0L).toInt()

public fun bioSetMemEofReturn(
    b: Bio,
    v: Int,
): Long = bioCtrl(b, BIO_C_SET_BUF_MEM_EOF_RETURN, v.toLong(), 0L)

public fun bioDgramGetNoTrunc(bio: Bio): Int =
    bioCtrl(bio, BIO_CTRL_DGRAM_GET_NO_TRUNC, 0L, 0L).toInt()

public fun bioDgramSetNoTrunc(
    bio: Bio,
    enable: Int,
): Int = bioCtrl(bio, BIO_CTRL_DGRAM_SET_NO_TRUNC, enable.toLong(), 0L).toInt()

public fun bioDgramGetCap(bio: Bio): UInt =
    bioCtrl(bio, BIO_CTRL_DGRAM_GET_CAPS, 0L, 0L).toUInt()

public fun bioDgramSetCap(
    bio: Bio,
    cap: UInt,
): Int = bioCtrl(bio, BIO_CTRL_DGRAM_SET_CAPS, cap.toLong(), 0L).toInt()

public fun bioDgramGetLocalAddrCap(bio: Bio): Int =
    bioCtrl(bio, BIO_CTRL_DGRAM_GET_LOCAL_ADDR_CAP, 0L, 0L).toInt()

public fun bioDgramGetLocalAddrEnable(
    bio: Bio,
    enable: Long,
): Int = bioCtrl(bio, BIO_CTRL_DGRAM_GET_LOCAL_ADDR_ENABLE, 0L, enable).toInt()

public fun bioDgramSetLocalAddrEnable(
    bio: Bio,
    enable: Int,
): Int = bioCtrl(bio, BIO_CTRL_DGRAM_SET_LOCAL_ADDR_ENABLE, enable.toLong(), 0L).toInt()

public fun bioDgramGetMtu(bio: Bio): UInt =
    bioCtrl(bio, BIO_CTRL_DGRAM_GET_MTU, 0L, 0L).toUInt()

public fun bioDgramSetMtu(
    bio: Bio,
    mtu: UInt,
): Int = bioCtrl(bio, BIO_CTRL_DGRAM_SET_MTU, mtu.toLong(), 0L).toInt()
