// port-lint: source openssl-sys/src/rsa.rs
package io.github.kotlinmania.opensslsys

public const val RSA_F4: Long = 0x10001L

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
