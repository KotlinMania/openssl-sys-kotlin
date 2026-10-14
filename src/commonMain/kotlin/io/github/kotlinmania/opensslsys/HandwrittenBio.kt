// port-lint: source handwritten/bio.rs
package io.github.kotlinmania.opensslsys

import io.github.kotlinmania.libc.CInt
import io.github.kotlinmania.libc.CLong

internal typealias BioInfoCb = (bio: Bio, oper: CInt, argp: String?, arglen: CInt, argi: CLong, ret: CLong) -> Unit

public fun interface OpensslBioInfoCb {
    public fun invoke(
        bio: Bio,
        oper: CInt,
        argp: String?,
        arglen: CInt,
        argi: CLong,
        ret: CLong,
    )
}

public class BioMethod(
    public val address: Long = 0L,
)
