// port-lint: source bn.rs
package io.github.kotlinmania.opensslsys

import io.github.kotlinmania.libc.CInt
import io.github.kotlinmania.libc.CUlonglong

public typealias BnUlong = CUlonglong

public const val BN_FLG_MALLOCED: CInt = 0x01
public const val BN_FLG_STATIC_DATA: CInt = 0x02
public const val BN_FLG_CONSTTIME: CInt = 0x04
public const val BN_FLG_SECURE: CInt = 0x08
