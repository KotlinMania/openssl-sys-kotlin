package io.github.kotlinmania.opensslsys.internal

internal object OpensslJni {
    init {
        System.loadLibrary("openssl_jni")
    }

    @JvmStatic external fun openSslVersionNum(): Long

    @JvmStatic external fun openSslVersion(t: Int): String?

    @JvmStatic external fun opensslInitCrypto(
        opts: Long,
        settings: Long,
    ): Int

    @JvmStatic external fun opensslInitSsl(
        opts: Long,
        settings: Long,
    ): Int

    @JvmStatic external fun cryptoMalloc(
        num: Long,
        file: String,
        line: Int,
    ): Long

    @JvmStatic external fun cryptoFree(
        addr: Long,
        file: String,
        line: Int,
    )

    @JvmStatic external fun sha1(data: ByteArray): ByteArray?

    @JvmStatic external fun sha224(data: ByteArray): ByteArray?

    @JvmStatic external fun sha256(data: ByteArray): ByteArray?

    @JvmStatic external fun sha384(data: ByteArray): ByteArray?

    @JvmStatic external fun sha512(data: ByteArray): ByteArray?

    @JvmStatic external fun bioSMem(): Long

    @JvmStatic external fun bioNew(method: Long): Long

    @JvmStatic external fun bioFreeAll(bio: Long)

    @JvmStatic external fun bioRead(
        bio: Long,
        buf: ByteArray,
        len: Int,
    ): Int

    @JvmStatic external fun bioWrite(
        bio: Long,
        buf: ByteArray,
        len: Int,
    ): Int

    @JvmStatic external fun bioCtrl(
        bio: Long,
        cmd: Int,
        larg: Long,
        parg: Long,
    ): Long

    @JvmStatic external fun bioGetMemData(bio: Long): ByteArray

    @JvmStatic external fun evpSha1(): Long

    @JvmStatic external fun evpSha224(): Long

    @JvmStatic external fun evpSha256(): Long

    @JvmStatic external fun evpSha384(): Long

    @JvmStatic external fun evpSha512(): Long

    @JvmStatic external fun evpGetDigestbyname(name: String): Long

    @JvmStatic external fun evpMdGetSize(md: Long): Int

    @JvmStatic external fun evpMdCtxNew(): Long

    @JvmStatic external fun evpMdCtxFree(ctx: Long)

    @JvmStatic external fun evpDigestInitEx(
        ctx: Long,
        type: Long,
        impl: Long,
    ): Int

    @JvmStatic external fun evpDigestUpdate(
        ctx: Long,
        data: ByteArray,
    ): Int

    @JvmStatic external fun evpDigestFinalEx(
        ctx: Long,
        md: ByteArray,
        outLen: IntArray?,
    ): Int

    @JvmStatic external fun tlsMethod(): Long

    @JvmStatic external fun tlsClientMethod(): Long

    @JvmStatic external fun tlsServerMethod(): Long

    @JvmStatic external fun sslCtxNew(method: Long): Long

    @JvmStatic external fun sslCtxFree(ctx: Long)

    @JvmStatic external fun sslCtxCtrl(
        ctx: Long,
        cmd: Int,
        larg: Long,
        parg: Long,
    ): Long

    @JvmStatic external fun sslCtxGetOptions(ctx: Long): Long

    @JvmStatic external fun sslCtxSetOptions(
        ctx: Long,
        op: Long,
    ): Long

    @JvmStatic external fun sslCtxClearOptions(
        ctx: Long,
        op: Long,
    ): Long

    @JvmStatic external fun sslNew(ctx: Long): Long

    @JvmStatic external fun sslFree(ssl: Long)

    @JvmStatic external fun sslCtrl(
        ssl: Long,
        cmd: Int,
        larg: Long,
        parg: Long,
    ): Long

    @JvmStatic external fun errGetError(): Long

    @JvmStatic external fun errPeekError(): Long

    @JvmStatic external fun errPeekLastError(): Long

    @JvmStatic external fun errClearError()

    @JvmStatic external fun errErrorStringN(
        e: Long,
        maxLen: Int,
    ): String

    @JvmStatic external fun randBytes(
        buf: ByteArray,
        num: Int,
    ): Int

    @JvmStatic external fun randStatus(): Int

    @JvmStatic external fun objNid2sn(n: Int): String?

    @JvmStatic external fun objNid2ln(n: Int): String?

    @JvmStatic external fun objSn2nid(s: String): Int

    @JvmStatic external fun objLn2nid(s: String): Int

    @JvmStatic external fun objTxt2nid(s: String): Int
}
