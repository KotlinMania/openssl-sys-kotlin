// port-lint: source handwritten/ssl.rs
package io.github.kotlinmania.opensslsys

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
    public val id: ULong = 0uL,
)

public class StackStSrtpProtectionProfile(
    override val address: Long = 0L,
) : OpenSslStackHandle

internal typealias TlsSessionTicketExtCbFn = (ssl: Ssl, data: ByteArray, len: Int, arg: Long) -> Int

public fun interface OpensslTlsSessionTicketExtCb {
    public fun invoke(
        ssl: Ssl,
        data: ByteArray,
        len: Int,
        arg: Long,
    ): Int
}

internal typealias TlsSessionSecretCbFn = (ssl: Ssl, secret: Long, secretLen: IntArray, peerCiphers: StackStSslCipher?, cipher: SslCipher?, arg: Long) -> Int

public fun interface OpensslTlsSessionSecretCb {
    public fun invoke(
        ssl: Ssl,
        secret: Long,
        secretLen: IntArray,
        peerCiphers: StackStSslCipher?,
        cipher: SslCipher?,
        arg: Long,
    ): Int
}

internal typealias SslCustomExtAddCbEx = (ssl: Ssl, extType: UInt, context: UInt, out: LongArray, outlen: ULongArray, x: X509?, chainidx: ULong, al: IntArray, addArg: Long) -> Int

public fun interface OpensslSslCustomExtAddCbEx {
    public fun invoke(
        ssl: Ssl,
        extType: UInt,
        context: UInt,
        out: LongArray,
        outlen: ULongArray,
        x: X509?,
        chainidx: ULong,
        al: IntArray,
        addArg: Long,
    ): Int
}

internal typealias SslCustomExtFreeCbEx = (ssl: Ssl, extType: UInt, context: UInt, out: Long, addArg: Long) -> Unit

public fun interface OpensslSslCustomExtFreeCbEx {
    public fun invoke(
        ssl: Ssl,
        extType: UInt,
        context: UInt,
        out: Long,
        addArg: Long,
    )
}

internal typealias SslCustomExtParseCbEx = (ssl: Ssl, extType: UInt, context: UInt, input: ByteArray, inlen: ULong, x: X509?, chainidx: ULong, al: IntArray, parseArg: Long) -> Int

public fun interface OpensslSslCustomExtParseCbEx {
    public fun invoke(
        ssl: Ssl,
        extType: UInt,
        context: UInt,
        input: ByteArray,
        inlen: ULong,
        x: X509?,
        chainidx: ULong,
        al: IntArray,
        parseArg: Long,
    ): Int
}

internal typealias GenSessionCb = (ssl: Ssl, id: ByteArray, idLen: UIntArray) -> Int

public fun interface OpensslGenSessionCb {
    public fun invoke(
        ssl: Ssl,
        id: ByteArray,
        idLen: UIntArray,
    ): Int
}

internal typealias SslCtxKeylogCbFunc = (ssl: Ssl, line: String) -> Unit

public fun interface OpensslSslCtxKeylogCb {
    public fun invoke(
        ssl: Ssl,
        line: String,
    )
}

internal typealias SslClientHelloCbFn = (s: Ssl, al: IntArray, arg: Long) -> Int

public fun interface OpensslSslClientHelloCb {
    public fun invoke(
        s: Ssl,
        al: IntArray,
        arg: Long,
    ): Int
}
