// port-lint: source rsa.rs
package io.github.kotlinmania.opensslsys

public const val RSA_F4: Long = 0x10001L

public fun evpPkeyCtxSetRsaKeygenBits(
    ctx: EvpPkeyCtx,
    bits: Int,
): Int =
    evpPkeyCtxCtrl(
        ctx,
        EVP_PKEY_RSA,
        EVP_PKEY_OP_KEYGEN,
        EVP_PKEY_CTRL_RSA_KEYGEN_BITS,
        bits,
        0L,
    )

public fun evpPkeyCtxSetRsaKeygenPubexp(
    ctx: EvpPkeyCtx,
    pubexp: Bignum,
): Int =
    evpPkeyCtxCtrl(
        ctx,
        EVP_PKEY_RSA,
        EVP_PKEY_OP_KEYGEN,
        EVP_PKEY_CTRL_RSA_KEYGEN_PUBEXP,
        0,
        pubexp.address,
    )

public fun evpPkeyCtxSetRsaPadding(
    ctx: EvpPkeyCtx,
    pad: Int,
): Int =
    evpPkeyCtxCtrl(
        ctx,
        EVP_PKEY_RSA,
        -1,
        EVP_PKEY_CTRL_RSA_PADDING,
        pad,
        0L,
    )

public fun evpPkeyCtxGetRsaPadding(
    ctx: EvpPkeyCtx,
    ppad: Long,
): Int =
    evpPkeyCtxCtrl(
        ctx,
        EVP_PKEY_RSA,
        -1,
        EVP_PKEY_CTRL_GET_RSA_PADDING,
        0,
        ppad,
    )

public fun evpPkeyCtxSetRsaPssSaltlen(
    ctx: EvpPkeyCtx,
    len: Int,
): Int =
    evpPkeyCtxCtrl(
        ctx,
        EVP_PKEY_RSA,
        EVP_PKEY_OP_SIGN or EVP_PKEY_OP_VERIFY,
        EVP_PKEY_CTRL_RSA_PSS_SALTLEN,
        len,
        0L,
    )

public fun evpPkeyCtxSetRsaMgf1Md(
    ctx: EvpPkeyCtx,
    md: EvpMd,
): Int =
    evpPkeyCtxCtrl(
        ctx,
        EVP_PKEY_RSA,
        EVP_PKEY_OP_TYPE_SIG or EVP_PKEY_OP_TYPE_CRYPT,
        EVP_PKEY_CTRL_RSA_MGF1_MD,
        0,
        md.address,
    )

public fun evpPkeyCtxSetRsaOaepMd(
    ctx: EvpPkeyCtx,
    md: EvpMd,
): Int =
    evpPkeyCtxCtrl(
        ctx,
        EVP_PKEY_RSA,
        EVP_PKEY_OP_TYPE_CRYPT,
        EVP_PKEY_CTRL_RSA_OAEP_MD,
        0,
        md.address,
    )

public fun evpPkeyCtxSet0RsaOaepLabel(
    ctx: EvpPkeyCtx,
    label: Long,
    len: Int,
): Int =
    evpPkeyCtxCtrl(
        ctx,
        EVP_PKEY_RSA,
        EVP_PKEY_OP_TYPE_CRYPT,
        EVP_PKEY_CTRL_RSA_OAEP_LABEL,
        len,
        label,
    )

public const val EVP_PKEY_CTRL_RSA_PADDING: Int = EVP_PKEY_ALG_CTRL + 1
public const val EVP_PKEY_CTRL_RSA_PSS_SALTLEN: Int = EVP_PKEY_ALG_CTRL + 2
public const val EVP_PKEY_CTRL_RSA_KEYGEN_BITS: Int = EVP_PKEY_ALG_CTRL + 3
public const val EVP_PKEY_CTRL_RSA_KEYGEN_PUBEXP: Int = EVP_PKEY_ALG_CTRL + 4
public const val EVP_PKEY_CTRL_RSA_MGF1_MD: Int = EVP_PKEY_ALG_CTRL + 5
public const val EVP_PKEY_CTRL_GET_RSA_PADDING: Int = EVP_PKEY_ALG_CTRL + 6
public const val EVP_PKEY_CTRL_RSA_OAEP_MD: Int = EVP_PKEY_ALG_CTRL + 9
public const val EVP_PKEY_CTRL_RSA_OAEP_LABEL: Int = EVP_PKEY_ALG_CTRL + 10

public const val RSA_PKCS1_PADDING: Int = 1
public const val RSA_SSLV23_PADDING: Int = 2
public const val RSA_NO_PADDING: Int = 3
public const val RSA_PKCS1_OAEP_PADDING: Int = 4
public const val RSA_X931_PADDING: Int = 5
public const val RSA_PKCS1_PSS_PADDING: Int = 6
