// port-lint: source handwritten/x509v3.rs
package io.github.kotlinmania.opensslsys

import io.github.kotlinmania.libc.CInt

public class ConfMethod(
    public val address: Long = 0L,
)

public class AccessDescription(
    public val method: Asn1Object? = null,
    public val location: GeneralName? = null,
)

public class StackStAccessDescription(
    override val address: Long = 0L,
) : OpenSslStackHandle

public class AuthorityKeyid(
    public val keyid: Asn1OctetString? = null,
    public val issuer: StackStGeneralName? = null,
    public val serial: Asn1Integer? = null,
)

public class DistPointNameStAnonUnion(
    public val fullname: StackStGeneralName? = null,
    public val relativename: StackStX509NameEntry? = null,
)

public class DistPointName(
    public val typeTag: CInt = 0,
    public val name: DistPointNameStAnonUnion = DistPointNameStAnonUnion(),
    public val dpname: X509Name? = null,
)

public class DistPoint(
    public val distpoint: DistPointName? = null,
    public val reasons: Asn1BitString? = null,
    public val crlIssuer: StackStGeneralName? = null,
    public val dpReasons: CInt = 0,
)

public class StackStDistPoint(
    override val address: Long = 0L,
) : OpenSslStackHandle
