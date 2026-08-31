// port-lint: source openssl-sys/src/ec.rs
package io.github.kotlinmania.opensslsys

public const val OPENSSL_EC_EXPLICIT_CURVE: Int = 0
public const val OPENSSL_EC_NAMED_CURVE: Int = 1

public const val EVP_PKEY_CTRL_EC_PARAMGEN_CURVE_NID: Int = EVP_PKEY_ALG_CTRL + 1
