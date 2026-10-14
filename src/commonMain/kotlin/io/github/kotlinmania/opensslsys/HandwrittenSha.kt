// port-lint: source handwritten/sha.rs
package io.github.kotlinmania.opensslsys

import io.github.kotlinmania.libc.CUInt

public class ShaCtx(
    public val h0: ShaLong = 0u,
    public val h1: ShaLong = 0u,
    public val h2: ShaLong = 0u,
    public val h3: ShaLong = 0u,
    public val h4: ShaLong = 0u,
    public val nl: ShaLong = 0u,
    public val nh: ShaLong = 0u,
    public val data: UIntArray = UIntArray(SHA_LBLOCK),
    public val num: CUInt = 0u,
)

public class Sha256Ctx(
    public val h: UIntArray = UIntArray(8),
    public val nl: ShaLong = 0u,
    public val nh: ShaLong = 0u,
    public val data: UIntArray = UIntArray(SHA_LBLOCK),
    public val num: CUInt = 0u,
    public val mdLen: CUInt = 0u,
)

public class Sha512Ctx(
    public val h: ULongArray = ULongArray(8),
    public val nl: ShaLong64 = 0uL,
    public val nh: ShaLong64 = 0uL,
    public val u: ULongArray = ULongArray(SHA_LBLOCK),
    public val num: CUInt = 0u,
    public val mdLen: CUInt = 0u,
)
