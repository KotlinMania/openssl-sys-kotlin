// port-lint: source handwritten/aes.rs
package io.github.kotlinmania.opensslsys

import io.github.kotlinmania.libc.CInt

public class AesKey(
    public val rdKey: UIntArray = UIntArray(4 * (AES_MAXNR + 1)),
    public val rounds: CInt = 0,
)
