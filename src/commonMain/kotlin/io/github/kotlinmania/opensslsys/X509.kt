// port-lint: source openssl-sys/src/x509.rs
package io.github.kotlinmania.opensslsys

public const val X509_FILETYPE_PEM: Int = 1
public const val X509_FILETYPE_ASN1: Int = 2
public const val X509_FILETYPE_DEFAULT: Int = 3

public const val ASN1_R_HEADER_TOO_LONG: Int = 123

public const val X509_LU_FAIL: Int = 0
public const val X509_LU_X509: Int = 1
public const val X509_LU_CRL: Int = 2
