// port-lint: source lib.rs
package io.github.kotlinmania.opensslsys

import io.github.kotlinmania.libc.CChar
import io.github.kotlinmania.libc.CInt
import io.github.kotlinmania.libc.CLong
import io.github.kotlinmania.libc.CULong

public typealias CInt = io.github.kotlinmania.libc.CInt
public typealias CLong = io.github.kotlinmania.libc.CLong
public typealias CULong = io.github.kotlinmania.libc.CULong
public typealias CChar = io.github.kotlinmania.libc.CChar

public typealias PasswordCallback = (
    buf: ByteArray,
    size: CInt,
    rwflag: CInt,
    userData: Any?,
) -> CInt

public fun opensslInit() {
    // OpenSSL library initialization hook
}

public fun assumeInit() {
    // OpenSSL explicit initialization skip hook
}
