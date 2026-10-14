// port-lint: source handwritten/rand.rs
package io.github.kotlinmania.opensslsys

import io.github.kotlinmania.libc.CInt

public expect fun randBytes(
    buf: ByteArray,
    num: CInt = buf.size,
): CInt

public expect fun randStatus(): CInt
