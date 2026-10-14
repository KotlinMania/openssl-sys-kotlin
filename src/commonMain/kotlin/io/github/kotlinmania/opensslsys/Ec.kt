// port-lint: source ec.rs
package io.github.kotlinmania.opensslsys

import io.github.kotlinmania.libc.CInt

public const val OPENSSL_EC_EXPLICIT_CURVE: Int = 0
public const val OPENSSL_EC_NAMED_CURVE: Int = 1

public fun evpPkeyCtxSetEcParamgenCurveNid(
    ctx: EvpPkeyCtx,
    nid: CInt,
): CInt =
    evpPkeyCtxCtrl(
        ctx,
        EVP_PKEY_EC,
        EVP_PKEY_OP_PARAMGEN or EVP_PKEY_OP_KEYGEN,
        EVP_PKEY_CTRL_EC_PARAMGEN_CURVE_NID,
        nid,
        0L,
    )

public fun evpEcGen(curve: String): EvpPkey? {
    val nid = objSn2nid(curve)
    return if (nid != NID_undef) EvpPkey(nid.toLong()) else null
}

public const val EVP_PKEY_CTRL_EC_PARAMGEN_CURVE_NID: Int = EVP_PKEY_ALG_CTRL + 1
