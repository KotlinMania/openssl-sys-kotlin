// port-lint: source src/core_dispatch.rs
package io.github.kotlinmania.opensslsys

// OpenSSL 3.* only

public const val OSSL_KEYMGMT_SELECT_PRIVATE_KEY: Int = 0x01
public const val OSSL_KEYMGMT_SELECT_PUBLIC_KEY: Int = 0x02
public const val OSSL_KEYMGMT_SELECT_DOMAIN_PARAMETERS: Int = 0x04
public const val OSSL_KEYMGMT_SELECT_OTHER_PARAMETERS: Int = 0x80
public val OSSL_KEYMGMT_SELECT_ALL_PARAMETERS: Int =
    OSSL_KEYMGMT_SELECT_DOMAIN_PARAMETERS or OSSL_KEYMGMT_SELECT_OTHER_PARAMETERS
