// port-lint: source openssl-sys/src/x509v3.rs
package io.github.kotlinmania.opensslsys

public const val GEN_OTHERNAME: Int = 0
public const val GEN_EMAIL: Int = 1
public const val GEN_DNS: Int = 2
public const val GEN_X400: Int = 3
public const val GEN_DIRNAME: Int = 4
public const val GEN_EDIPARTY: Int = 5
public const val GEN_URI: Int = 6
public const val GEN_IPADD: Int = 7
public const val GEN_RID: Int = 8

public const val X509_CHECK_FLAG_ALWAYS_CHECK_SUBJECT: UInt = 0x1u
public const val X509_CHECK_FLAG_NO_WILDCARDS: UInt = 0x2u
public const val X509_CHECK_FLAG_NO_PARTIAL_WILDCARDS: UInt = 0x4u
public const val X509_CHECK_FLAG_MULTI_LABEL_WILDCARDS: UInt = 0x8u
public const val X509_CHECK_FLAG_SINGLE_LABEL_SUBDOMAINS: UInt = 0x10u
public const val X509_CHECK_FLAG_NEVER_CHECK_SUBJECT: UInt = 0x20u

public const val X509V3_ADD_DEFAULT: ULong = 0uL
public const val X509V3_ADD_APPEND: ULong = 1uL
public const val X509V3_ADD_REPLACE: ULong = 2uL
public const val X509V3_ADD_REPLACE_EXISTING: ULong = 3uL
public const val X509V3_ADD_KEEP_EXISTING: ULong = 4uL
public const val X509V3_ADD_DELETE: ULong = 5uL
public const val X509V3_ADD_SILENT: ULong = 0x10uL

public const val EXFLAG_BCONS: UInt = 0x1u
public const val EXFLAG_KUSAGE: UInt = 0x2u
public const val EXFLAG_XKUSAGE: UInt = 0x4u
public const val EXFLAG_NSCERT: UInt = 0x8u
public const val EXFLAG_CA: UInt = 0x10u
public const val EXFLAG_SI: UInt = 0x20u
public const val EXFLAG_V1: UInt = 0x40u
public const val EXFLAG_INVALID: UInt = 0x80u
public const val EXFLAG_SET: UInt = 0x100u
public const val EXFLAG_CRITICAL: UInt = 0x200u
public const val EXFLAG_PROXY: UInt = 0x400u
public const val EXFLAG_INVALID_POLICY: UInt = 0x800u
public const val EXFLAG_FRESHEST: UInt = 0x1000u
public const val EXFLAG_SS: UInt = 0x2000u

public const val X509v3_KU_DIGITAL_SIGNATURE: UInt = 0x0080u
public const val X509v3_KU_NON_REPUDIATION: UInt = 0x0040u
public const val X509v3_KU_KEY_ENCIPHERMENT: UInt = 0x0020u
public const val X509v3_KU_DATA_ENCIPHERMENT: UInt = 0x0010u
public const val X509v3_KU_KEY_AGREEMENT: UInt = 0x0008u
public const val X509v3_KU_KEY_CERT_SIGN: UInt = 0x0004u
public const val X509v3_KU_CRL_SIGN: UInt = 0x0002u
public const val X509v3_KU_ENCIPHER_ONLY: UInt = 0x0001u
public const val X509v3_KU_DECIPHER_ONLY: UInt = 0x8000u
public const val X509v3_KU_UNDEF: UInt = 0xffffu

public const val XKU_SSL_SERVER: UInt = 0x1u
public const val XKU_SSL_CLIENT: UInt = 0x2u
public const val XKU_SMIME: UInt = 0x4u
public const val XKU_CODE_SIGN: UInt = 0x8u
public const val XKU_SGC: UInt = 0x10u
public const val XKU_OCSP_SIGN: UInt = 0x20u
public const val XKU_TIMESTAMP: UInt = 0x40u
public const val XKU_DVCS: UInt = 0x80u
public const val XKU_ANYEKU: UInt = 0x100u

public const val X509_PURPOSE_SSL_CLIENT: Int = 1
public const val X509_PURPOSE_SSL_SERVER: Int = 2
public const val X509_PURPOSE_NS_SSL_SERVER: Int = 3
public const val X509_PURPOSE_SMIME_SIGN: Int = 4
public const val X509_PURPOSE_SMIME_ENCRYPT: Int = 5
public const val X509_PURPOSE_CRL_SIGN: Int = 6
public const val X509_PURPOSE_ANY: Int = 7
public const val X509_PURPOSE_OCSP_HELPER: Int = 8
public const val X509_PURPOSE_TIMESTAMP_SIGN: Int = 9
public const val X509_PURPOSE_CODE_SIGN: Int = 10
public const val X509_PURPOSE_MIN: Int = 1
public const val X509_PURPOSE_MAX: Int = 10

public const val CRL_REASON_UNSPECIFIED: Int = 0
public const val CRL_REASON_KEY_COMPROMISE: Int = 1
public const val CRL_REASON_CA_COMPROMISE: Int = 2
public const val CRL_REASON_AFFILIATION_CHANGED: Int = 3
public const val CRL_REASON_SUPERSEDED: Int = 4
public const val CRL_REASON_CESSATION_OF_OPERATION: Int = 5
public const val CRL_REASON_CERTIFICATE_HOLD: Int = 6
public const val CRL_REASON_REMOVE_FROM_CRL: Int = 8
public const val CRL_REASON_PRIVILEGE_WITHDRAWN: Int = 9
public const val CRL_REASON_AA_COMPROMISE: Int = 10

public class GeneralName(
    public val type: Int = 0,
)
