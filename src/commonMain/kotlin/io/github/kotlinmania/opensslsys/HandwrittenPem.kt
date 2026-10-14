// port-lint: source handwritten/pem.rs
package io.github.kotlinmania.opensslsys

import io.github.kotlinmania.libc.CInt

internal typealias PemPasswordCb = (buf: ByteArray, size: CInt, rwflag: CInt, userData: Long) -> CInt

public fun interface OpensslPemPasswordCb {
    public fun invoke(
        buf: ByteArray,
        size: CInt,
        rwflag: CInt,
        userData: Long,
    ): CInt
}
