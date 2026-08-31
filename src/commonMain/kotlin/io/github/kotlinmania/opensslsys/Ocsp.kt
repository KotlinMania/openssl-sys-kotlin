// port-lint: source openssl-sys/src/ocsp.rs
package io.github.kotlinmania.opensslsys

public const val OCSP_REVOKED_STATUS_NOSTATUS: Int = -1
public const val OCSP_REVOKED_STATUS_UNSPECIFIED: Int = 0
public const val OCSP_REVOKED_STATUS_KEYCOMPROMISE: Int = 1
public const val OCSP_REVOKED_STATUS_CACOMPROMISE: Int = 2
public const val OCSP_REVOKED_STATUS_AFFILIATIONCHANGED: Int = 3
public const val OCSP_REVOKED_STATUS_SUPERSEDED: Int = 4
public const val OCSP_REVOKED_STATUS_CESSATIONOFOPERATION: Int = 5
public const val OCSP_REVOKED_STATUS_CERTIFICATEHOLD: Int = 6
public const val OCSP_REVOKED_STATUS_REMOVEFROMCRL: Int = 8

public const val OCSP_NOCERTS: ULong = 0x1uL
public const val OCSP_NOINTERN: ULong = 0x2uL
public const val OCSP_NOSIGS: ULong = 0x4uL
public const val OCSP_NOCHAIN: ULong = 0x8uL
public const val OCSP_NOVERIFY: ULong = 0x10uL
public const val OCSP_NOEXPLICIT: ULong = 0x20uL
public const val OCSP_NOCASIGN: ULong = 0x40uL
public const val OCSP_NODELEGATED: ULong = 0x80uL
public const val OCSP_NOCHECKS: ULong = 0x100uL
public const val OCSP_TRUSTOTHER: ULong = 0x200uL
public const val OCSP_RESPID_KEY: ULong = 0x400uL
public const val OCSP_NOTIME: ULong = 0x800uL

public const val OCSP_RESPONSE_STATUS_SUCCESSFUL: Int = 0
public const val OCSP_RESPONSE_STATUS_MALFORMEDREQUEST: Int = 1
public const val OCSP_RESPONSE_STATUS_INTERNALERROR: Int = 2
public const val OCSP_RESPONSE_STATUS_TRYLATER: Int = 3
public const val OCSP_RESPONSE_STATUS_SIGREQUIRED: Int = 5
public const val OCSP_RESPONSE_STATUS_UNAUTHORIZED: Int = 6

public const val V_OCSP_CERTSTATUS_GOOD: Int = 0
public const val V_OCSP_CERTSTATUS_REVOKED: Int = 1
public const val V_OCSP_CERTSTATUS_UNKNOWN: Int = 2
