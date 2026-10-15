// port-lint: source handwritten/rand.rs
package io.github.kotlinmania.opensslsys

public expect fun randBytes(
    buf: ByteArray,
    num: Int = buf.size,
): Int

public expect fun randStatus(): Int
