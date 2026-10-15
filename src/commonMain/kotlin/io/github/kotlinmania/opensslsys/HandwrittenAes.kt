// port-lint: source handwritten/aes.rs
package io.github.kotlinmania.opensslsys

public class AesKey(
    public val rdKey: UIntArray = UIntArray(4 * (AES_MAXNR + 1)),
    public val rounds: Int = 0,
)
