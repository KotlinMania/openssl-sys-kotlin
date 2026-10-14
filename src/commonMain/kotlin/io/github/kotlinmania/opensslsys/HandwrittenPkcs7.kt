// port-lint: source handwritten/pkcs7.rs
package io.github.kotlinmania.opensslsys

import io.github.kotlinmania.libc.CInt
import io.github.kotlinmania.libc.CLong

public class Pkcs7Ctx(
    public val libctx: OsslLibCtx? = null,
    public val propq: String? = null,
)

public class Pkcs7Signed(
    public val version: Asn1Integer? = null,
    public val mdAlgs: StackStX509Algor? = null,
    public val cert: StackStX509? = null,
    public val crl: StackStX509Crl? = null,
    public val signerInfo: StackStPkcs7SignerInfo? = null,
    public val contents: Pkcs7? = null,
)

public class Pkcs7EncContent(
    public val contentType: Asn1Object? = null,
    public val algorithm: X509Algor? = null,
    public val encData: Asn1OctetString? = null,
    public val cipher: EvpCipher? = null,
    public val ctx: Pkcs7Ctx? = null,
)

public class Pkcs7Envelope(
    public val version: Asn1Integer? = null,
    public val recipientinfo: StackStPkcs7RecipInfo? = null,
    public val encData: Pkcs7EncContent? = null,
)

public class Pkcs7SignEnvelope(
    public val version: Asn1Integer? = null,
    public val mdAlgs: StackStX509Algor? = null,
    public val cert: StackStX509? = null,
    public val crl: StackStX509Crl? = null,
    public val signerInfo: StackStPkcs7SignerInfo? = null,
    public val encData: Pkcs7EncContent? = null,
    public val recipientinfo: StackStPkcs7RecipInfo? = null,
)

public class Pkcs7Digest(
    public val version: Asn1Integer? = null,
    public val md: X509Algor? = null,
    public val contents: Pkcs7? = null,
    public val digest: Asn1OctetString? = null,
)

public class Pkcs7Encrypt(
    public val version: Asn1Integer? = null,
    public val encData: Pkcs7EncContent? = null,
)

public class Pkcs7Data(
    public val ptr: Long = 0L,
    public val data: Asn1OctetString? = null,
    public val sign: Pkcs7Signed? = null,
    public val enveloped: Pkcs7Envelope? = null,
    public val signedAndEnveloped: Pkcs7SignEnvelope? = null,
    public val digest: Pkcs7Digest? = null,
    public val encrypted: Pkcs7Encrypt? = null,
    public val other: Asn1Type? = null,
)

public class Pkcs7(
    public val asn1: Long = 0L,
    public val length: CLong = 0L,
    public val state: CInt = 0,
    public val detached: CInt = 0,
    public val typeTag: Asn1Object? = null,
    public val d: Pkcs7Data = Pkcs7Data(),
    public val ctx: Pkcs7Ctx = Pkcs7Ctx(),
)

public class Pkcs7IssuerAndSerial(
    public val issuer: X509Name? = null,
    public val serial: Asn1Integer? = null,
)

public class Pkcs7SignerInfo(
    public val version: Asn1Integer? = null,
    public val issuerAndSerial: Pkcs7IssuerAndSerial? = null,
    public val digestAlg: X509Algor? = null,
    public val authAttr: StackStX509Attribute? = null,
    public val digestEncAlg: X509Algor? = null,
    public val encDigest: Asn1OctetString? = null,
    public val unauthAttr: StackStX509Attribute? = null,
    public val pkey: EvpPkey? = null,
    public val ctx: Pkcs7Ctx? = null,
)

public class StackStPkcs7SignerInfo(
    override val address: Long = 0L,
) : OpenSslStackHandle

public class Pkcs7RecipInfo(
    public val version: Asn1Integer? = null,
    public val issuerAndSerial: Pkcs7IssuerAndSerial? = null,
    public val keyEncAlgor: X509Algor? = null,
    public val encKey: Asn1OctetString? = null,
    public val cert: X509? = null,
    public val ctx: Pkcs7Ctx? = null,
)

public class StackStPkcs7RecipInfo(
    override val address: Long = 0L,
) : OpenSslStackHandle
