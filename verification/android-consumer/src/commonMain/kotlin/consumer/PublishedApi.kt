package consumer

import io.github.kotlinmania.opensslsys.Bio
import io.github.kotlinmania.opensslsys.ERR_LIB_PEM
import io.github.kotlinmania.opensslsys.NID_sha256
import io.github.kotlinmania.opensslsys.OPENSSL_VERSION
import io.github.kotlinmania.opensslsys.Ssl
import io.github.kotlinmania.opensslsys.SslCtx
import io.github.kotlinmania.opensslsys.StackStX509
import io.github.kotlinmania.opensslsys.assumeInit
import io.github.kotlinmania.opensslsys.bioCtrlPending
import io.github.kotlinmania.opensslsys.bioFreeAll
import io.github.kotlinmania.opensslsys.bioNew
import io.github.kotlinmania.opensslsys.bioRead
import io.github.kotlinmania.opensslsys.bioSMem
import io.github.kotlinmania.opensslsys.bioWrite
import io.github.kotlinmania.opensslsys.cryptoMemcmp
import io.github.kotlinmania.opensslsys.errGetLib
import io.github.kotlinmania.opensslsys.errGetReason
import io.github.kotlinmania.opensslsys.errPack
import io.github.kotlinmania.opensslsys.evpGetDigestbynid
import io.github.kotlinmania.opensslsys.evpMdSize
import io.github.kotlinmania.opensslsys.objSn2nid
import io.github.kotlinmania.opensslsys.openSslVersion
import io.github.kotlinmania.opensslsys.openSslVersionNum
import io.github.kotlinmania.opensslsys.opensslCleanse
import io.github.kotlinmania.opensslsys.opensslFree
import io.github.kotlinmania.opensslsys.opensslInit
import io.github.kotlinmania.opensslsys.opensslMalloc
import io.github.kotlinmania.opensslsys.randStatus
import io.github.kotlinmania.opensslsys.sha1
import io.github.kotlinmania.opensslsys.sha256
import io.github.kotlinmania.opensslsys.sslCtxFree
import io.github.kotlinmania.opensslsys.sslCtxNew
import io.github.kotlinmania.opensslsys.sslFree
import io.github.kotlinmania.opensslsys.sslNew
import io.github.kotlinmania.opensslsys.tlsMethod

public fun initAndReadVersion(): Pair<Long, String?> {
    opensslInit()
    assumeInit()
    return openSslVersionNum() to openSslVersion(OPENSSL_VERSION)
}

public fun allocateAndFreeBuffer(size: ULong): ULong {
    val ptr = opensslMalloc(size)
    if (ptr != 0uL) {
        opensslFree(ptr)
    }
    return ptr
}

public fun computeSha1Hex(input: ByteArray): String =
    sha1(input).joinToString("") { (it.toInt() and 0xff).toString(16).padStart(2, '0') }

public fun computeSha256Hex(input: ByteArray): String =
    sha256(input).joinToString("") { (it.toInt() and 0xff).toString(16).padStart(2, '0') }

public fun cleanseAndCompare(buf: ByteArray): Pair<Int, Boolean> {
    val cmpSame = cryptoMemcmp(buf, buf.copyOf(), buf.size.toULong())
    opensslCleanse(buf)
    return cmpSame to buf.all { it == 0.toByte() }
}

public fun bioRoundTrip(payload: ByteArray): ByteArray {
    val method = bioSMem() ?: error("bioSMem returned null")
    val bio: Bio = bioNew(method) ?: error("bioNew returned null")
    try {
        val written = bioWrite(bio, payload)
        check(written == payload.size)
        check(bioCtrlPending(bio) == payload.size.toULong())
        val out = ByteArray(payload.size)
        val read = bioRead(bio, out)
        check(read == payload.size)
        return out
    } finally {
        bioFreeAll(bio)
    }
}

public fun verifyPublicTypesAndErrRandNid(): Boolean {
    val packed = errPack(ERR_LIB_PEM, 0, 0x42)
    val errOk = errGetLib(packed) == ERR_LIB_PEM && errGetReason(packed) == 0x42
    val nidOk = objSn2nid("SHA256") == NID_sha256
    val md = evpGetDigestbynid(NID_sha256)
    val mdOk = md != null && evpMdSize(md) == 32
    val randOk = randStatus() == 1
    val stack = StackStX509(123uL)
    val ctx: SslCtx? = tlsMethod()?.let { sslCtxNew(it) }
    val ssl: Ssl? = ctx?.let { sslNew(it) }
    if (ssl != null) sslFree(ssl)
    if (ctx != null) sslCtxFree(ctx)
    return errOk && nidOk && mdOk && randOk && stack.address == 123uL && ctx != null && ssl != null
}
