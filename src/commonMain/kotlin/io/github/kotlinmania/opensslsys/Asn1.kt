// port-lint: source asn1.rs
package io.github.kotlinmania.opensslsys

// ASN.1 tag values
public const val V_ASN1_EOC: Int = 0
public const val V_ASN1_BOOLEAN: Int = 1
public const val V_ASN1_INTEGER: Int = 2
public const val V_ASN1_BIT_STRING: Int = 3
public const val V_ASN1_OCTET_STRING: Int = 4
public const val V_ASN1_NULL: Int = 5
public const val V_ASN1_OBJECT: Int = 6
public const val V_ASN1_OBJECT_DESCRIPTOR: Int = 7
public const val V_ASN1_EXTERNAL: Int = 8
public const val V_ASN1_REAL: Int = 9
public const val V_ASN1_ENUMERATED: Int = 10
public const val V_ASN1_UTF8STRING: Int = 12
public const val V_ASN1_SEQUENCE: Int = 16
public const val V_ASN1_SET: Int = 17
public const val V_ASN1_NUMERICSTRING: Int = 18
public const val V_ASN1_PRINTABLESTRING: Int = 19
public const val V_ASN1_T61STRING: Int = 20
public const val V_ASN1_TELETEXSTRING: Int = 20 // alias
public const val V_ASN1_VIDEOTEXSTRING: Int = 21
public const val V_ASN1_IA5STRING: Int = 22
public const val V_ASN1_UTCTIME: Int = 23
public const val V_ASN1_GENERALIZEDTIME: Int = 24
public const val V_ASN1_GRAPHICSTRING: Int = 25
public const val V_ASN1_ISO64STRING: Int = 26
public const val V_ASN1_VISIBLESTRING: Int = 26 // alias
public const val V_ASN1_GENERALSTRING: Int = 27
public const val V_ASN1_UNIVERSALSTRING: Int = 28
public const val V_ASN1_BMPSTRING: Int = 30

public const val MBSTRING_FLAG: Int = 0x1000
public const val MBSTRING_UTF8: Int = MBSTRING_FLAG
public val MBSTRING_ASC: Int = MBSTRING_FLAG or 1
public val MBSTRING_BMP: Int = MBSTRING_FLAG or 2
public val MBSTRING_UNIV: Int = MBSTRING_FLAG or 4
