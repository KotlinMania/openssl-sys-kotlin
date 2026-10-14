// port-lint: source handwritten/types.rs
package io.github.kotlinmania.opensslsys

import io.github.kotlinmania.libc.CInt
import io.github.kotlinmania.libc.CUInt
import io.github.kotlinmania.libc.SizeT

public class Asn1Object(
    public val address: Long = 0L,
)

public class Asn1Value(
    public val address: Long = 0L,
)

public typealias Asn1Boolean = CInt

public class Asn1Integer(
    public val address: Long = 0L,
)

public class Asn1Enumerated(
    public val address: Long = 0L,
)

public class Asn1Generalizedtime(
    public val address: Long = 0L,
)

public class Asn1String(
    public val address: Long = 0L,
)

public class Asn1BitString(
    public val address: Long = 0L,
)

public class Asn1Time(
    public val address: Long = 0L,
)

public class Asn1OctetString(
    public val address: Long = 0L,
)

public class Asn1Null(
    public val address: Long = 0L,
)

public class Asn1Printablestring(
    public val address: Long = 0L,
)

public class Asn1T61string(
    public val address: Long = 0L,
)

public class Asn1Ia5string(
    public val address: Long = 0L,
)

public class Asn1Generalstring(
    public val address: Long = 0L,
)

public class Asn1Bmpstring(
    public val address: Long = 0L,
)

public class Asn1Universalstring(
    public val address: Long = 0L,
)

public class Asn1Utctime(
    public val address: Long = 0L,
)

public class Asn1Visiblestring(
    public val address: Long = 0L,
)

public class Asn1Utf8string(
    public val address: Long = 0L,
)

public class BioSt(
    public val address: Long = 0L,
)

public class Bio(
    public val address: Long = 0L,
)

public class BioAddr(
    public val address: Long = 0L,
)

public class BioPollDescriptor(
    public val address: Long = 0L,
)

public class BioMsg(
    public val data: Long = 0L,
    public val dataLen: SizeT = 0uL,
    public val peer: BioAddr? = null,
    public val local: BioAddr? = null,
    public val flags: ULong = 0uL,
)

public class Bignum(
    public val address: Long = 0L,
)

public class BnBlinding(
    public val address: Long = 0L,
)

public class BnMontCtx(
    public val address: Long = 0L,
)

public class BnCtx(
    public val address: Long = 0L,
)

public class BnGencb(
    public val address: Long = 0L,
)

public class EvpCipher(
    public val address: Long = 0L,
)

public class EvpCipherCtx(
    public val address: Long = 0L,
)

public class EvpMd(
    public val address: Long = 0L,
)

public class EvpMdCtx(
    public val address: Long = 0L,
)

public class Pkcs8PrivKeyInfo(
    public val address: Long = 0L,
)

public class EvpPkeyAsn1Method(
    public val address: Long = 0L,
)

public class EvpPkeyCtx(
    public val address: Long = 0L,
)

public class CmacCtx(
    public val address: Long = 0L,
)

public class HmacCtx(
    public val address: Long = 0L,
)

public class Dh(
    public val address: Long = 0L,
)

public class DhMethod(
    public val address: Long = 0L,
)

public class Dsa(
    public val address: Long = 0L,
)

public class DsaMethod(
    public val address: Long = 0L,
)

public class Rsa(
    public val address: Long = 0L,
)

public class RsaMethod(
    public val address: Long = 0L,
)

public class EcKey(
    public val address: Long = 0L,
)

public class X509(
    public val address: Long = 0L,
)

public class X509Algor(
    public val address: Long = 0L,
)

public class StackStX509Algor(
    override val address: Long = 0L,
) : OpenSslStackHandle

public class X509LookupMethod(
    public val address: Long = 0L,
)

public class X509Name(
    public val address: Long = 0L,
)

public class X509Store(
    public val address: Long = 0L,
)

public class X509StoreCtx(
    public val address: Long = 0L,
)

public class X509VerifyParam(
    public val address: Long = 0L,
)

public class X509Object(
    public val address: Long = 0L,
)

public class X509Lookup(
    public val address: Long = 0L,
)

public class X509v3Ctx(
    public val flags: CInt = 0,
    public val issuerCert: Long = 0L,
    public val subjectCert: Long = 0L,
    public val subjectReq: Long = 0L,
    public val crl: Long = 0L,
    public val dbMeth: Long = 0L,
    public val db: Long = 0L,
    public val issuerPkey: Long = 0L,
)

public class Conf(
    public val address: Long = 0L,
)

public class OpensslInitSettings(
    public val address: Long = 0L,
)

public class Engine(
    public val address: Long = 0L,
)

public class Ssl(
    public val address: Long = 0L,
)

public class SslCtx(
    public val address: Long = 0L,
)

public class SrpCtx(
    public val address: Long = 0L,
)

public class SslConnCloseInfo(
    public val errorCode: ULong = 0uL,
    public val frameType: ULong = 0uL,
    public val reason: String? = null,
    public val reasonLen: SizeT = 0uL,
    public val flags: UInt = 0u,
)

public class SslShutdownExArgs(
    public val quicErrorCode: ULong = 0uL,
    public val quicReason: String? = null,
)

public class SslStreamResetArgs(
    public val quicErrorCode: ULong = 0uL,
)

public class CompCtx(
    public val address: Long = 0L,
)

public class CompMethod(
    public val address: Long = 0L,
)

public class CryptoExData(
    public val address: Long = 0L,
)

public class OcspResponse(
    public val address: Long = 0L,
)

public class OsslProvider(
    public val address: Long = 0L,
)

public class OsslLibCtx(
    public val address: Long = 0L,
)

public class OsslParam(
    public val key: Long = 0L,
    public val dataType: CUInt = 0u,
    public val data: Long = 0L,
    public val dataSize: SizeT = 0uL,
    public val returnSize: SizeT = 0uL,
)

public class OsslParamBld(
    public val address: Long = 0L,
)

public class EvpKdf(
    public val address: Long = 0L,
)

public class EvpKdfCtx(
    public val address: Long = 0L,
)

public class OsslEncoderCtx(
    public val address: Long = 0L,
)

public class OsslDecoderCtx(
    public val address: Long = 0L,
)

internal typealias OsslPassphraseCallback = (pass: ByteArray, passSize: SizeT, passLen: LongArray, params: OsslParam?, arg: Long) -> CInt

public fun interface OpensslPassphraseCallback {
    public fun invoke(
        pass: ByteArray,
        passSize: SizeT,
        passLen: LongArray,
        params: OsslParam?,
        arg: Long,
    ): CInt
}

public class EvpMac(
    public val address: Long = 0L,
)

public class EvpMacCtx(
    public val address: Long = 0L,
)
