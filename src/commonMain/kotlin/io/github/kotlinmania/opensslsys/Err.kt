// port-lint: source openssl-sys/src/err.rs
package io.github.kotlinmania.opensslsys

public const val ERR_TXT_MALLOCED: Int = 0x01
public const val ERR_TXT_STRING: Int = 0x02

public const val ERR_LIB_SYS: Int = 2
public const val ERR_LIB_PEM: Int = 9
public const val ERR_LIB_ASN1: Int = 13

public const val ERR_SYSTEM_FLAG: ULong = 0x80000000uL
public const val ERR_SYSTEM_MASK: ULong = 0x7FFFFFFFuL

public const val ERR_LIB_OFFSET: ULong = 23uL
public const val ERR_LIB_MASK: ULong = 0xffuL
public const val ERR_RFLAGS_OFFSET: ULong = 18uL
public const val ERR_RFLAGS_MASK: ULong = 0x1fuL
public const val ERR_REASON_MASK: ULong = 0x7FFFFFuL

public val ERR_RFLAG_FATAL: ULong = 0x1uL shl ERR_RFLAGS_OFFSET.toInt()

public fun errSystemError(errcode: ULong): Boolean =
    (errcode and ERR_SYSTEM_FLAG) != 0uL

public fun errGetLib(errcode: ULong): Int =
    if (errSystemError(errcode)) {
        ERR_LIB_SYS
    } else {
        ((errcode shr ERR_LIB_OFFSET.toInt()) and ERR_LIB_MASK).toInt()
    }

public fun errGetFunc(errcode: ULong): Int = 0

public fun errGetReason(errcode: ULong): Int =
    if (errSystemError(errcode)) {
        (errcode and ERR_SYSTEM_MASK).toInt()
    } else {
        (errcode and ERR_REASON_MASK).toInt()
    }

public fun errPack(lib: Int, function: Int, reason: Int): ULong =
    ((lib.toULong() and ERR_LIB_MASK) shl ERR_LIB_OFFSET.toInt()) or
        (reason.toULong() and ERR_REASON_MASK)
