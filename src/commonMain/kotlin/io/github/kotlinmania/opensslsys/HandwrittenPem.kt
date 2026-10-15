// port-lint: source handwritten/pem.rs
package io.github.kotlinmania.opensslsys

internal typealias PemPasswordCb = (buf: ByteArray, size: Int, rwflag: Int, userData: Long) -> Int

public fun interface OpensslPemPasswordCb {
    public fun invoke(
        buf: ByteArray,
        size: Int,
        rwflag: Int,
        userData: Long,
    ): Int
}
