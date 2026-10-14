// port-lint: source handwritten/asn1.rs
package io.github.kotlinmania.opensslsys

import io.github.kotlinmania.libc.CInt
import io.github.kotlinmania.libc.CLong

public class Asn1Encoding(
    public val enc: Long = 0L,
    public val len: CLong = 0L,
    public val modified: CInt = 0,
)

public class StackStAsn1Object(
    override val address: Long = 0L,
) : OpenSslStackHandle

public class Asn1TypeValue(
    public val ptr: Long = 0L,
    public val boolean: Asn1Boolean = 0,
    public val asn1String: Asn1String? = null,
    public val objectValue: Asn1Object? = null,
    public val integer: Asn1Integer? = null,
    public val enumerated: Asn1Enumerated? = null,
    public val bitString: Asn1BitString? = null,
    public val octetString: Asn1OctetString? = null,
    public val printablestring: Asn1Printablestring? = null,
    public val t61string: Asn1T61string? = null,
    public val ia5string: Asn1Ia5string? = null,
    public val generalstring: Asn1Generalstring? = null,
    public val bmpstring: Asn1Bmpstring? = null,
    public val universalstring: Asn1Universalstring? = null,
    public val utctime: Asn1Utctime? = null,
    public val generalizedtime: Asn1Generalizedtime? = null,
    public val visiblestring: Asn1Visiblestring? = null,
    public val utf8string: Asn1Utf8string? = null,
    public val set: Asn1String? = null,
    public val sequence: Asn1String? = null,
    public val asn1Value: Asn1Value? = null,
)

public class Asn1Type(
    public val typeTag: CInt = 0,
    public val value: Asn1TypeValue = Asn1TypeValue(),
)
