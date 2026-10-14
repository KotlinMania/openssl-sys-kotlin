// port-lint: source handwritten/object.rs
package io.github.kotlinmania.opensslsys

import io.github.kotlinmania.libc.CInt

public expect fun objNid2sn(n: CInt): String?

public expect fun objNid2ln(n: CInt): String?

public expect fun objSn2nid(sn: String): CInt

public expect fun objLn2nid(ln: String): CInt

public expect fun objTxt2nid(s: String): CInt
