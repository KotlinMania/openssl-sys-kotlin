// port-lint: source handwritten/bio.rs
package io.github.kotlinmania.opensslsys

internal typealias BioInfoCb = (bio: Bio, oper: Int, argp: String?, arglen: Int, argi: Long, ret: Long) -> Unit

public fun interface OpensslBioInfoCb {
    public fun invoke(
        bio: Bio,
        oper: Int,
        argp: String?,
        arglen: Int,
        argi: Long,
        ret: Long,
    )
}

public class BioMethod(
    public val address: Long = 0L,
)
