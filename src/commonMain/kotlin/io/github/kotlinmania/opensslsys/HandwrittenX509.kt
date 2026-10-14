// port-lint: source handwritten/x509.rs
package io.github.kotlinmania.opensslsys

public class X509Val(
    public val notBefore: Asn1Time? = null,
    public val notAfter: Asn1Time? = null,
)

public class X509NameEntry(
    public val address: Long = 0L,
)

public class StackStX509NameEntry(
    override val address: Long = 0L,
) : OpenSslStackHandle

public class StackStX509Name(
    override val address: Long = 0L,
) : OpenSslStackHandle

public class X509Extension(
    public val address: Long = 0L,
)

public class StackStX509Extension(
    override val address: Long = 0L,
) : OpenSslStackHandle

public class X509Attribute(
    public val address: Long = 0L,
)

public class StackStX509Attribute(
    override val address: Long = 0L,
) : OpenSslStackHandle

public class X509ReqInfo(
    public val address: Long = 0L,
)

public class X509Crl(
    public val address: Long = 0L,
)

public class StackStX509Crl(
    override val address: Long = 0L,
) : OpenSslStackHandle

public class X509CrlInfo(
    public val address: Long = 0L,
)

public class X509Revoked(
    public val address: Long = 0L,
)

public class StackStX509Revoked(
    override val address: Long = 0L,
) : OpenSslStackHandle

public class X509Req(
    public val address: Long = 0L,
)

public class X509Cinf(
    public val address: Long = 0L,
)

public class StackStX509(
    override val address: Long = 0L,
) : OpenSslStackHandle

public class StackStX509Object(
    override val address: Long = 0L,
) : OpenSslStackHandle

public class StackStX509Lookup(
    override val address: Long = 0L,
) : OpenSslStackHandle

public class X509SigInfo(
    public val address: Long = 0L,
)
