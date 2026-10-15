// port-lint: source handwritten/object.rs
package io.github.kotlinmania.opensslsys

public expect fun objNid2sn(n: Int): String?

public expect fun objNid2ln(n: Int): String?

public expect fun objSn2nid(sn: String): Int

public expect fun objLn2nid(ln: String): Int

public expect fun objTxt2nid(s: String): Int
