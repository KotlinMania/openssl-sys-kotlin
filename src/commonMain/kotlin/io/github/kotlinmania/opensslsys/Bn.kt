// port-lint: source openssl-sys/src/bn.rs
package io.github.kotlinmania.opensslsys

public typealias BnUlong = ULong

public const val BN_FLG_MALLOCED: Int = 0x01
public const val BN_FLG_STATIC_DATA: Int = 0x02
public const val BN_FLG_CONSTTIME: Int = 0x04
public const val BN_FLG_SECURE: Int = 0x08
