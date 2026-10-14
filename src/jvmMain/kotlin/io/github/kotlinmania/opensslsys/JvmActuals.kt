// port-lint: source lib.rs crypto.rs sha.rs bio.rs evp.rs ssl.rs err.rs handwritten/rand.rs handwritten/object.rs
package io.github.kotlinmania.opensslsys

import io.github.kotlinmania.libc.CInt
import io.github.kotlinmania.libc.CLong
import io.github.kotlinmania.libc.CULong
import io.github.kotlinmania.opensslsys.internal.OpensslJni

public actual fun openSslVersionNum(): CULong = OpensslJni.openSslVersionNum().toULong()

public actual fun openSslVersion(key: CInt): String? = OpensslJni.openSslVersion(key)

public actual fun opensslInitCrypto(
    opts: ULong,
    settings: OpensslInitSettings?,
): CInt = OpensslJni.opensslInitCrypto(opts.toLong(), settings?.address ?: 0L)

public actual fun cryptoMalloc(
    num: ULong,
    file: String,
    line: CInt,
): Long = OpensslJni.cryptoMalloc(num.toLong(), file, line)

public actual fun cryptoFree(
    addr: Long,
    file: String,
    line: CInt,
) {
    if (addr != 0L) {
        OpensslJni.cryptoFree(addr, file, line)
    }
}

public actual fun sha1(data: ByteArray): ByteArray? = OpensslJni.sha1(data)

public actual fun sha224(data: ByteArray): ByteArray? = OpensslJni.sha224(data)

public actual fun sha256(data: ByteArray): ByteArray? = OpensslJni.sha256(data)

public actual fun sha384(data: ByteArray): ByteArray? = OpensslJni.sha384(data)

public actual fun sha512(data: ByteArray): ByteArray? = OpensslJni.sha512(data)

public actual fun bioSMem(): BioMethod? {
    val ptr = OpensslJni.bioSMem()
    return if (ptr != 0L) BioMethod(ptr) else null
}

public actual fun bioNew(type: BioMethod): Bio? {
    val ptr = OpensslJni.bioNew(type.address)
    return if (ptr != 0L) Bio(ptr) else null
}

public actual fun bioFreeAll(b: Bio?) {
    if (b != null && b.address != 0L) {
        OpensslJni.bioFreeAll(b.address)
    }
}

public actual fun bioRead(
    b: Bio,
    buf: ByteArray,
    len: CInt,
): CInt = OpensslJni.bioRead(b.address, buf, len)

public actual fun bioWrite(
    b: Bio,
    buf: ByteArray,
    len: CInt,
): CInt = OpensslJni.bioWrite(b.address, buf, len)

public actual fun bioCtrl(
    b: Bio,
    cmd: CInt,
    larg: CLong,
    parg: Long,
): CLong = OpensslJni.bioCtrl(b.address, cmd, larg, parg)

public actual fun bioGetMemData(b: Bio): ByteArray = OpensslJni.bioGetMemData(b.address)

public actual fun evpSha1(): EvpMd? {
    val ptr = OpensslJni.evpSha1()
    return if (ptr != 0L) EvpMd(ptr) else null
}

public actual fun evpSha224(): EvpMd? {
    val ptr = OpensslJni.evpSha224()
    return if (ptr != 0L) EvpMd(ptr) else null
}

public actual fun evpSha256(): EvpMd? {
    val ptr = OpensslJni.evpSha256()
    return if (ptr != 0L) EvpMd(ptr) else null
}

public actual fun evpSha384(): EvpMd? {
    val ptr = OpensslJni.evpSha384()
    return if (ptr != 0L) EvpMd(ptr) else null
}

public actual fun evpSha512(): EvpMd? {
    val ptr = OpensslJni.evpSha512()
    return if (ptr != 0L) EvpMd(ptr) else null
}

public actual fun evpGetDigestbyname(name: String): EvpMd? {
    val ptr = OpensslJni.evpGetDigestbyname(name)
    return if (ptr != 0L) EvpMd(ptr) else null
}

public actual fun evpMdGetSize(md: EvpMd): CInt = OpensslJni.evpMdGetSize(md.address)

public actual fun evpMdCtxNew(): EvpMdCtx? {
    val ptr = OpensslJni.evpMdCtxNew()
    return if (ptr != 0L) EvpMdCtx(ptr) else null
}

public actual fun evpMdCtxFree(ctx: EvpMdCtx?) {
    if (ctx != null && ctx.address != 0L) {
        OpensslJni.evpMdCtxFree(ctx.address)
    }
}

public actual fun evpDigestInitEx(
    ctx: EvpMdCtx,
    type: EvpMd,
    impl: Engine?,
): CInt = OpensslJni.evpDigestInitEx(ctx.address, type.address, impl?.address ?: 0L)

public actual fun evpDigestUpdate(
    ctx: EvpMdCtx,
    d: ByteArray,
): CInt = OpensslJni.evpDigestUpdate(ctx.address, d)

public actual fun evpDigestFinalEx(
    ctx: EvpMdCtx,
    md: ByteArray,
    s: IntArray?,
): CInt = OpensslJni.evpDigestFinalEx(ctx.address, md, s)

public actual fun opensslInitSsl(
    opts: ULong,
    settings: OpensslInitSettings?,
): CInt = OpensslJni.opensslInitSsl(opts.toLong(), settings?.address ?: 0L)

public actual fun tlsMethod(): SslMethod? {
    val ptr = OpensslJni.tlsMethod()
    return if (ptr != 0L) SslMethod(ptr) else null
}

public actual fun tlsClientMethod(): SslMethod? {
    val ptr = OpensslJni.tlsClientMethod()
    return if (ptr != 0L) SslMethod(ptr) else null
}

public actual fun tlsServerMethod(): SslMethod? {
    val ptr = OpensslJni.tlsServerMethod()
    return if (ptr != 0L) SslMethod(ptr) else null
}

public actual fun sslCtxNew(method: SslMethod): SslCtx? {
    val ptr = OpensslJni.sslCtxNew(method.address)
    return if (ptr != 0L) SslCtx(ptr) else null
}

public actual fun sslCtxFree(ctx: SslCtx?) {
    if (ctx != null && ctx.address != 0L) {
        OpensslJni.sslCtxFree(ctx.address)
    }
}

public actual fun sslCtxCtrl(
    ctx: SslCtx,
    cmd: CInt,
    larg: CLong,
    parg: Long,
): CLong = OpensslJni.sslCtxCtrl(ctx.address, cmd, larg, parg)

public actual fun sslCtxGetOptions(ctx: SslCtx): ULong = OpensslJni.sslCtxGetOptions(ctx.address).toULong()

public actual fun sslCtxSetOptions(
    ctx: SslCtx,
    op: ULong,
): ULong = OpensslJni.sslCtxSetOptions(ctx.address, op.toLong()).toULong()

public actual fun sslCtxClearOptions(
    ctx: SslCtx,
    op: ULong,
): ULong = OpensslJni.sslCtxClearOptions(ctx.address, op.toLong()).toULong()

public actual fun sslNew(ctx: SslCtx): Ssl? {
    val ptr = OpensslJni.sslNew(ctx.address)
    return if (ptr != 0L) Ssl(ptr) else null
}

public actual fun sslFree(ssl: Ssl?) {
    if (ssl != null && ssl.address != 0L) {
        OpensslJni.sslFree(ssl.address)
    }
}

public actual fun sslCtrl(
    ssl: Ssl,
    cmd: CInt,
    larg: CLong,
    parg: Long,
): CLong = OpensslJni.sslCtrl(ssl.address, cmd, larg, parg)

public actual fun errGetError(): CULong = OpensslJni.errGetError().toULong()

public actual fun errPeekError(): CULong = OpensslJni.errPeekError().toULong()

public actual fun errPeekLastError(): CULong = OpensslJni.errPeekLastError().toULong()

public actual fun errClearError() {
    OpensslJni.errClearError()
}

public actual fun errErrorStringN(
    e: CULong,
    maxLen: Int,
): String = OpensslJni.errErrorStringN(e.toLong(), maxLen)

public actual fun randBytes(
    buf: ByteArray,
    num: CInt,
): CInt = OpensslJni.randBytes(buf, num)

public actual fun randStatus(): CInt = OpensslJni.randStatus()

public actual fun objNid2sn(n: CInt): String? = OpensslJni.objNid2sn(n)

public actual fun objNid2ln(n: CInt): String? = OpensslJni.objNid2ln(n)

public actual fun objSn2nid(sn: String): CInt = OpensslJni.objSn2nid(sn)

public actual fun objLn2nid(ln: String): CInt = OpensslJni.objLn2nid(ln)

public actual fun objTxt2nid(s: String): CInt = OpensslJni.objTxt2nid(s)
