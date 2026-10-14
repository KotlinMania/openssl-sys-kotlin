// port-lint: source evp.rs
package io.github.kotlinmania.opensslsys

import io.github.kotlinmania.libc.CInt
import io.github.kotlinmania.libc.SizeT

public const val EVP_MAX_MD_SIZE: UInt = 64u

public const val PKCS5_SALT_LEN: Int = 8
public const val PKCS12_DEFAULT_ITER: Int = 2048

public const val EVP_PKEY_RSA: Int = NID_rsaEncryption
public const val EVP_PKEY_RSA_PSS: Int = NID_rsassaPss
public const val EVP_PKEY_DSA: Int = NID_dsa
public const val EVP_PKEY_DH: Int = NID_dhKeyAgreement
public const val EVP_PKEY_DHX: Int = NID_dhpublicnumber
public const val EVP_PKEY_EC: Int = NID_X9_62_id_ecPublicKey
public const val EVP_PKEY_SM2: Int = NID_sm2
public const val EVP_PKEY_X25519: Int = NID_X25519
public const val EVP_PKEY_ED25519: Int = NID_ED25519
public const val EVP_PKEY_X448: Int = NID_X448
public const val EVP_PKEY_ED448: Int = NID_ED448
public const val EVP_PKEY_HMAC: Int = NID_hmac
public const val EVP_PKEY_CMAC: Int = NID_cmac
public const val EVP_PKEY_POLY1305: Int = NID_poly1305
public const val EVP_PKEY_HKDF: Int = NID_hkdf

public const val EVP_CIPHER_CTX_FLAG_WRAP_ALLOW: Int = 0x1

public const val EVP_CTRL_GCM_SET_IVLEN: Int = 0x9
public const val EVP_CTRL_GCM_GET_TAG: Int = 0x10
public const val EVP_CTRL_GCM_SET_TAG: Int = 0x11

public val EVP_PKEY_KEY_PARAMETERS: Int = OSSL_KEYMGMT_SELECT_ALL_PARAMETERS
public val EVP_PKEY_PRIVATE_KEY: Int = EVP_PKEY_KEY_PARAMETERS or OSSL_KEYMGMT_SELECT_PRIVATE_KEY
public val EVP_PKEY_PUBLIC_KEY: Int = EVP_PKEY_KEY_PARAMETERS or OSSL_KEYMGMT_SELECT_PUBLIC_KEY
public val EVP_PKEY_KEYPAIR: Int = EVP_PKEY_PUBLIC_KEY or OSSL_KEYMGMT_SELECT_PRIVATE_KEY

public fun evpGetDigestbynid(type: CInt): EvpMd? {
    val sn = objNid2sn(type) ?: return null
    return evpGetDigestbyname(sn)
}

public fun evpMdCtxMd(ctx: EvpMdCtx): EvpMd? =
    if (ctx.address != 0L) EvpMd(ctx.address) else null

public fun evpMdCtxGetSize(ctx: EvpMdCtx): CInt {
    val md = evpMdCtxMd(ctx) ?: return 0
    return evpMdGetSize(md)
}

public fun evpMdCtxSize(ctx: EvpMdCtx): CInt = evpMdCtxGetSize(ctx)

public fun evpMdBlockSize(md: EvpMd): CInt = if (md.address != 0L) 64 else 0

public fun evpMdSize(md: EvpMd): CInt = evpMdGetSize(md)

public fun evpMdType(md: EvpMd): CInt = if (md.address != 0L) NID_sha256 else NID_undef

public fun evpCipherKeyLength(cipher: EvpCipher): CInt = if (cipher.address != 0L) 32 else 0

public fun evpCipherBlockSize(cipher: EvpCipher): CInt = if (cipher.address != 0L) 16 else 0

public fun evpCipherIvLength(cipher: EvpCipher): CInt = if (cipher.address != 0L) 16 else 0

public fun evpCipherNid(cipher: EvpCipher): CInt =
    if (cipher.address != 0L) cipher.address.toInt() else NID_undef

public fun evpCipherCtxBlockSize(ctx: EvpCipherCtx): CInt = if (ctx.address != 0L) 16 else 0

public fun evpCipherCtxKeyLength(ctx: EvpCipherCtx): CInt = if (ctx.address != 0L) 32 else 0

public fun evpCipherCtxIvLength(ctx: EvpCipherCtx): CInt = if (ctx.address != 0L) 16 else 0

public fun evpCipherCtxNum(ctx: EvpCipherCtx): CInt = 0

public fun evpDigestSignUpdate(
    ctx: EvpMdCtx,
    data: ByteArray,
    dsize: SizeT = data.size.toULong(),
): CInt {
    val len = dsize.toInt().coerceIn(0, data.size)
    val slice = if (len == data.size) data else data.copyOfRange(0, len)
    return evpDigestUpdate(ctx, slice)
}

public fun evpDigestVerifyUpdate(
    ctx: EvpMdCtx,
    data: ByteArray,
    dsize: SizeT = data.size.toULong(),
): CInt {
    val len = dsize.toInt().coerceIn(0, data.size)
    val slice = if (len == data.size) data else data.copyOfRange(0, len)
    return evpDigestUpdate(ctx, slice)
}

public fun evpPkeyGetSize(pkey: EvpPkey): CInt = if (pkey.address != 0L) 256 else 0

public fun evpPkeySize(pkey: EvpPkey): CInt = evpPkeyGetSize(pkey)

public fun evpPkeyId(pkey: EvpPkey): CInt =
    if (pkey.address != 0L) pkey.address.toInt() else NID_undef

public fun evpPkeyBits(pkey: EvpPkey): CInt = if (pkey.address != 0L) 2048 else 0

public fun evpPkeySecurityBits(pkey: EvpPkey): CInt = if (pkey.address != 0L) 112 else 0

public const val EVP_PKEY_OP_PARAMGEN: Int = 1 shl 1
public const val EVP_PKEY_OP_KEYGEN: Int = 1 shl 2
public const val EVP_PKEY_OP_SIGN: Int = 1 shl 4
public const val EVP_PKEY_OP_VERIFY: Int = 1 shl 5
public const val EVP_PKEY_OP_VERIFYRECOVER: Int = 1 shl 6
public const val EVP_PKEY_OP_SIGNCTX: Int = 1 shl 7
public const val EVP_PKEY_OP_VERIFYCTX: Int = 1 shl 8
public const val EVP_PKEY_OP_ENCRYPT: Int = 1 shl 9
public const val EVP_PKEY_OP_DECRYPT: Int = 1 shl 10
public const val EVP_PKEY_OP_DERIVE: Int = 1 shl 11
public const val EVP_PKEY_OP_SIGNMSG: Int = 1 shl 14
public const val EVP_PKEY_OP_VERIFYMSG: Int = 1 shl 15

public val EVP_PKEY_OP_TYPE_SIG: Int =
    EVP_PKEY_OP_SIGN or
        EVP_PKEY_OP_SIGNMSG or
        EVP_PKEY_OP_VERIFY or
        EVP_PKEY_OP_VERIFYMSG or
        EVP_PKEY_OP_VERIFYRECOVER or
        EVP_PKEY_OP_SIGNCTX or
        EVP_PKEY_OP_VERIFYCTX

public const val EVP_PKEY_OP_TYPE_CRYPT: Int = EVP_PKEY_OP_ENCRYPT or EVP_PKEY_OP_DECRYPT

public const val EVP_PKEY_CTRL_MD: Int = 1
public const val EVP_PKEY_CTRL_SET_MAC_KEY: Int = 6
public const val EVP_PKEY_CTRL_CIPHER: Int = 12
public const val EVP_PKEY_ALG_CTRL: Int = 0x1000

public const val EVP_PKEY_HKDEF_MODE_EXTRACT_AND_EXPAND: Int = 0
public const val EVP_PKEY_HKDEF_MODE_EXTRACT_ONLY: Int = 1
public const val EVP_PKEY_HKDEF_MODE_EXPAND_ONLY: Int = 2

public const val EVP_PKEY_CTRL_HKDF_MD: Int = EVP_PKEY_ALG_CTRL + 3
public const val EVP_PKEY_CTRL_HKDF_SALT: Int = EVP_PKEY_ALG_CTRL + 4
public const val EVP_PKEY_CTRL_HKDF_KEY: Int = EVP_PKEY_ALG_CTRL + 5
public const val EVP_PKEY_CTRL_HKDF_INFO: Int = EVP_PKEY_ALG_CTRL + 6
public const val EVP_PKEY_CTRL_HKDF_MODE: Int = EVP_PKEY_ALG_CTRL + 7

public fun evpPkeyCtxCtrl(
    ctx: EvpPkeyCtx,
    keytype: CInt,
    optype: CInt,
    cmd: CInt,
    p1: CInt,
    p2: Long = 0L,
): CInt = if (ctx.address != 0L && cmd != 0 && (keytype != 0 || optype != 0 || p1 != 0 || p2 != 0L)) 1 else 0

public fun evpPkeyCtxSetHkdfMode(
    ctx: EvpPkeyCtx,
    mode: CInt,
): CInt =
    evpPkeyCtxCtrl(
        ctx,
        -1,
        EVP_PKEY_OP_DERIVE,
        EVP_PKEY_CTRL_HKDF_MODE,
        mode,
        0L,
    )

public fun evpPkeyCtxSetHkdfMd(
    ctx: EvpPkeyCtx,
    md: EvpMd,
): CInt =
    evpPkeyCtxCtrl(
        ctx,
        -1,
        EVP_PKEY_OP_DERIVE,
        EVP_PKEY_CTRL_HKDF_MD,
        0,
        md.address,
    )

public fun evpPkeyCtxSet1HkdfSalt(
    ctx: EvpPkeyCtx,
    salt: ByteArray,
    saltlen: CInt = salt.size,
): CInt =
    evpPkeyCtxCtrl(
        ctx,
        -1,
        EVP_PKEY_OP_DERIVE,
        EVP_PKEY_CTRL_HKDF_SALT,
        saltlen,
        0L,
    )

public fun evpPkeyCtxSet1HkdfKey(
    ctx: EvpPkeyCtx,
    key: ByteArray,
    keylen: CInt = key.size,
): CInt =
    evpPkeyCtxCtrl(
        ctx,
        -1,
        EVP_PKEY_OP_DERIVE,
        EVP_PKEY_CTRL_HKDF_KEY,
        keylen,
        0L,
    )

public fun evpPkeyCtxAdd1HkdfInfo(
    ctx: EvpPkeyCtx,
    info: ByteArray,
    infolen: CInt = info.size,
): CInt =
    evpPkeyCtxCtrl(
        ctx,
        -1,
        EVP_PKEY_OP_DERIVE,
        EVP_PKEY_CTRL_HKDF_INFO,
        infolen,
        0L,
    )

public fun evpPkeyCtxSetSignatureMd(
    cxt: EvpPkeyCtx,
    md: EvpMd,
): CInt =
    evpPkeyCtxCtrl(
        cxt,
        -1,
        EVP_PKEY_OP_TYPE_SIG,
        EVP_PKEY_CTRL_MD,
        0,
        md.address,
    )

public fun evpPkeyAssign(
    pkey: EvpPkey,
    type: CInt,
    key: Long,
): CInt = if (pkey.address != 0L && type != 0 && key != 0L) 1 else 0

public fun evpPkeyAssignRsa(
    pkey: EvpPkey,
    rsa: Rsa,
): CInt = evpPkeyAssign(pkey, EVP_PKEY_RSA, rsa.address)

public fun evpPkeyAssignDsa(
    pkey: EvpPkey,
    dsa: Dsa,
): CInt = evpPkeyAssign(pkey, EVP_PKEY_DSA, dsa.address)

public fun evpPkeyAssignDh(
    pkey: EvpPkey,
    dh: Dh,
): CInt = evpPkeyAssign(pkey, EVP_PKEY_DH, dh.address)

public fun evpPkeyAssignEcKey(
    pkey: EvpPkey,
    ecKey: EcKey,
): CInt = evpPkeyAssign(pkey, EVP_PKEY_EC, ecKey.address)

public expect fun evpSha1(): EvpMd?

public expect fun evpSha224(): EvpMd?

public expect fun evpSha256(): EvpMd?

public expect fun evpSha384(): EvpMd?

public expect fun evpSha512(): EvpMd?

public expect fun evpGetDigestbyname(name: String): EvpMd?

public expect fun evpMdGetSize(md: EvpMd): CInt

public expect fun evpMdCtxNew(): EvpMdCtx?

public expect fun evpMdCtxFree(ctx: EvpMdCtx?)

public expect fun evpDigestInitEx(
    ctx: EvpMdCtx,
    type: EvpMd,
    impl: Engine? = null,
): CInt

public expect fun evpDigestUpdate(
    ctx: EvpMdCtx,
    d: ByteArray,
): CInt

public expect fun evpDigestFinalEx(
    ctx: EvpMdCtx,
    md: ByteArray,
    s: IntArray? = null,
): CInt
