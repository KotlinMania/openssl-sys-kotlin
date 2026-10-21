package io.github.kotlinmania.opensslsys

public external interface OpensslNativeBindings {
    public fun openSslVersionNum(): Double

    public fun openSslVersion(t: Int): String?

    public fun opensslInitCrypto(
        opts: Double,
        settings: Double,
    ): Int

    public fun opensslInitSsl(
        opts: Double,
        settings: Double,
    ): Int

    public fun cryptoMalloc(
        num: Double,
        file: String,
        line: Int,
    ): Double

    public fun cryptoFree(
        addr: Double,
        file: String,
        line: Int,
    )

    public fun sha1Hex(hex: String): String?

    public fun sha224Hex(hex: String): String?

    public fun sha256Hex(hex: String): String?

    public fun sha384Hex(hex: String): String?

    public fun sha512Hex(hex: String): String?

    public fun bioSMem(): Double

    public fun bioNew(method: Double): Double

    public fun bioFreeAll(bio: Double)

    public fun bioWriteHex(
        bio: Double,
        hex: String,
        len: Int,
    ): Int

    public fun bioReadHex(
        bio: Double,
        len: Int,
    ): String

    public fun bioCtrl(
        bio: Double,
        cmd: Int,
        larg: Double,
        parg: Double,
    ): Double

    public fun bioGetMemDataHex(bio: Double): String

    public fun evpSha1(): Double

    public fun evpSha224(): Double

    public fun evpSha256(): Double

    public fun evpSha384(): Double

    public fun evpSha512(): Double

    public fun evpGetDigestbyname(name: String): Double

    public fun evpMdGetSize(md: Double): Int

    public fun evpMdCtxNew(): Double

    public fun evpMdCtxFree(ctx: Double)

    public fun evpDigestInitEx(
        ctx: Double,
        type: Double,
        impl: Double,
    ): Int

    public fun evpDigestUpdateHex(
        ctx: Double,
        hex: String,
    ): Int

    public fun evpDigestFinalExHex(ctx: Double): String

    public fun tlsMethod(): Double

    public fun tlsClientMethod(): Double

    public fun tlsServerMethod(): Double

    public fun sslCtxNew(method: Double): Double

    public fun sslCtxFree(ctx: Double)

    public fun sslCtxCtrl(
        ctx: Double,
        cmd: Int,
        larg: Double,
        parg: Double,
    ): Double

    public fun sslCtxGetOptions(ctx: Double): Double

    public fun sslCtxSetOptions(
        ctx: Double,
        op: Double,
    ): Double

    public fun sslCtxClearOptions(
        ctx: Double,
        op: Double,
    ): Double

    public fun sslNew(ctx: Double): Double

    public fun sslFree(ssl: Double)

    public fun sslCtrl(
        ssl: Double,
        cmd: Int,
        larg: Double,
        parg: Double,
    ): Double

    public fun errGetError(): Double

    public fun errPeekError(): Double

    public fun errPeekLastError(): Double

    public fun errClearError()

    public fun errErrorStringN(
        e: Double,
        maxLen: Int,
    ): String

    public fun randBytesHex(num: Int): String

    public fun randStatus(): Int

    public fun objNid2sn(n: Int): String?

    public fun objNid2ln(n: Int): String?

    public fun objSn2nid(s: String): Int

    public fun objLn2nid(s: String): Int

    public fun objTxt2nid(s: String): Int
}

internal val OpensslNative: OpensslNativeBindings? =
    js(
        """(() => {
            const proc = globalThis.process;
            if (!proc || typeof proc.getBuiltinModule !== "function") {
                return null;
            }
            const modMod = proc.getBuiltinModule("module");
            const path = proc.getBuiltinModule("path");
            const fs = proc.getBuiltinModule("fs");
            const req = modMod.createRequire(path.join(proc.cwd(), "package.json"));
            const copiedMod = path.resolve(proc.cwd(), "../../node_modules/@kotlinmania/openssl-native-bindings");
            const sourceMod = path.resolve(proc.cwd(), "../../../../native/node-openssl");
            return req(fs.existsSync(copiedMod) ? copiedMod : sourceMod);
        })()""",
    ).unsafeCast<OpensslNativeBindings?>()
