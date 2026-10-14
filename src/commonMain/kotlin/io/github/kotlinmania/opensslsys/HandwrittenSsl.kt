// port-lint: source handwritten/ssl.rs
package io.github.kotlinmania.opensslsys

import io.github.kotlinmania.libc.CInt
import io.github.kotlinmania.libc.CUInt
import io.github.kotlinmania.libc.CULong
import io.github.kotlinmania.libc.SizeT

public class SslMethod(
    public val address: Long = 0L,
)

public class SslCipher(
    public val address: Long = 0L,
)

public class SslSession(
    public val address: Long = 0L,
)

public class StackStSslCipher(
    override val address: Long = 0L,
) : OpenSslStackHandle

public class SrtpProtectionProfile(
    public val name: String? = null,
    public val id: CULong = 0uL,
)

public class StackStSrtpProtectionProfile(
    override val address: Long = 0L,
) : OpenSslStackHandle

internal typealias TlsSessionTicketExtCbFn = (ssl: Ssl, data: ByteArray, len: CInt, arg: Long) -> CInt

public fun interface OpensslTlsSessionTicketExtCb {
    public fun invoke(
        ssl: Ssl,
        data: ByteArray,
        len: CInt,
        arg: Long,
    ): CInt
}

internal typealias TlsSessionSecretCbFn = (ssl: Ssl, secret: Long, secretLen: IntArray, peerCiphers: StackStSslCipher?, cipher: SslCipher?, arg: Long) -> CInt

public fun interface OpensslTlsSessionSecretCb {
    public fun invoke(
        ssl: Ssl,
        secret: Long,
        secretLen: IntArray,
        peerCiphers: StackStSslCipher?,
        cipher: SslCipher?,
        arg: Long,
    ): CInt
}

internal typealias SslCustomExtAddCbEx = (ssl: Ssl, extType: CUInt, context: CUInt, out: LongArray, outlen: ULongArray, x: X509?, chainidx: SizeT, al: IntArray, addArg: Long) -> CInt

public fun interface OpensslSslCustomExtAddCbEx {
    public fun invoke(
        ssl: Ssl,
        extType: CUInt,
        context: CUInt,
        out: LongArray,
        outlen: ULongArray,
        x: X509?,
        chainidx: SizeT,
        al: IntArray,
        addArg: Long,
    ): CInt
}

internal typealias SslCustomExtFreeCbEx = (ssl: Ssl, extType: CUInt, context: CUInt, out: Long, addArg: Long) -> Unit

public fun interface OpensslSslCustomExtFreeCbEx {
    public fun invoke(
        ssl: Ssl,
        extType: CUInt,
        context: CUInt,
        out: Long,
        addArg: Long,
    )
}

internal typealias SslCustomExtParseCbEx = (ssl: Ssl, extType: CUInt, context: CUInt, input: ByteArray, inlen: SizeT, x: X509?, chainidx: SizeT, al: IntArray, parseArg: Long) -> CInt

public fun interface OpensslSslCustomExtParseCbEx {
    public fun invoke(
        ssl: Ssl,
        extType: CUInt,
        context: CUInt,
        input: ByteArray,
        inlen: SizeT,
        x: X509?,
        chainidx: SizeT,
        al: IntArray,
        parseArg: Long,
    ): CInt
}

internal typealias GenSessionCb = (ssl: Ssl, id: ByteArray, idLen: UIntArray) -> CInt

public fun interface OpensslGenSessionCb {
    public fun invoke(
        ssl: Ssl,
        id: ByteArray,
        idLen: UIntArray,
    ): CInt
}

internal typealias SslCtxKeylogCbFunc = (ssl: Ssl, line: String) -> Unit

public fun interface OpensslSslCtxKeylogCb {
    public fun invoke(
        ssl: Ssl,
        line: String,
    )
}

internal typealias SslClientHelloCbFn = (s: Ssl, al: IntArray, arg: Long) -> CInt

public fun interface OpensslSslClientHelloCb {
    public fun invoke(
        s: Ssl,
        al: IntArray,
        arg: Long,
    ): CInt
}
