// port-lint: source openssl-sys/src/evp.rs
package io.github.kotlinmania.opensslsys

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
