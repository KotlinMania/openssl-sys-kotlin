// port-lint: source handwritten/err.rs
package io.github.kotlinmania.opensslsys

import io.github.kotlinmania.libc.CULong

public class ErrStringData(
    public val error: CULong = 0uL,
    public val string: String? = null,
)
