@file:OptIn(kotlin.js.ExperimentalWasmJsInterop::class)

package io.github.kotlinmania.opensslsys.internal

@JsFun(
    """() => {
    if (globalThis.__kotlinmania_openssl_native === undefined) {
        const proc = globalThis.process;
        if (!proc || typeof proc.getBuiltinModule !== "function") {
            globalThis.__kotlinmania_openssl_native = null;
            return false;
        }
        const modMod = proc.getBuiltinModule("module");
        const path = proc.getBuiltinModule("path");
        const fs = proc.getBuiltinModule("fs");
        const req = modMod.createRequire(path.join(proc.cwd(), "package.json"));
        const copiedMod = path.resolve(proc.cwd(), "../../../js/node_modules/@kotlinmania/openssl-native-bindings");
        const sourceMod = path.resolve(proc.cwd(), "../../../../native/node-openssl");
        globalThis.__kotlinmania_openssl_native = req(fs.existsSync(copiedMod) ? copiedMod : sourceMod);
    }
    return globalThis.__kotlinmania_openssl_native !== null;
}""",
)
internal external fun ensureOpensslAddonLoaded(): Boolean

@JsFun("() => globalThis.__kotlinmania_openssl_native.openSslVersionNum()")
internal external fun wasmOpenSslVersionNum(): Double

@JsFun("(t) => globalThis.__kotlinmania_openssl_native.openSslVersion(t)")
internal external fun wasmOpenSslVersion(t: Int): String?

@JsFun("(opts, settings) => globalThis.__kotlinmania_openssl_native.opensslInitCrypto(opts, settings)")
internal external fun wasmOpensslInitCrypto(
    opts: Double,
    settings: Double,
): Int

@JsFun("(opts, settings) => globalThis.__kotlinmania_openssl_native.opensslInitSsl(opts, settings)")
internal external fun wasmOpensslInitSsl(
    opts: Double,
    settings: Double,
): Int

@JsFun("(num, file, line) => globalThis.__kotlinmania_openssl_native.cryptoMalloc(num, file, line)")
internal external fun wasmCryptoMalloc(
    num: Double,
    file: String,
    line: Int,
): Double

@JsFun("(addr, file, line) => globalThis.__kotlinmania_openssl_native.cryptoFree(addr, file, line)")
internal external fun wasmCryptoFree(
    addr: Double,
    file: String,
    line: Int,
)

@JsFun("(hex) => globalThis.__kotlinmania_openssl_native.sha1Hex(hex)")
internal external fun wasmSha1Hex(hex: String): String?

@JsFun("(hex) => globalThis.__kotlinmania_openssl_native.sha224Hex(hex)")
internal external fun wasmSha224Hex(hex: String): String?

@JsFun("(hex) => globalThis.__kotlinmania_openssl_native.sha256Hex(hex)")
internal external fun wasmSha256Hex(hex: String): String?

@JsFun("(hex) => globalThis.__kotlinmania_openssl_native.sha384Hex(hex)")
internal external fun wasmSha384Hex(hex: String): String?

@JsFun("(hex) => globalThis.__kotlinmania_openssl_native.sha512Hex(hex)")
internal external fun wasmSha512Hex(hex: String): String?

@JsFun("() => globalThis.__kotlinmania_openssl_native.bioSMem()")
internal external fun wasmBioSMem(): Double

@JsFun("(method) => globalThis.__kotlinmania_openssl_native.bioNew(method)")
internal external fun wasmBioNew(method: Double): Double

@JsFun("(bio) => globalThis.__kotlinmania_openssl_native.bioFreeAll(bio)")
internal external fun wasmBioFreeAll(bio: Double)

@JsFun("(bio, hex, len) => globalThis.__kotlinmania_openssl_native.bioWriteHex(bio, hex, len)")
internal external fun wasmBioWriteHex(
    bio: Double,
    hex: String,
    len: Int,
): Int

@JsFun("(bio, len) => globalThis.__kotlinmania_openssl_native.bioReadHex(bio, len)")
internal external fun wasmBioReadHex(
    bio: Double,
    len: Int,
): String

@JsFun("(bio, cmd, larg, parg) => globalThis.__kotlinmania_openssl_native.bioCtrl(bio, cmd, larg, parg)")
internal external fun wasmBioCtrl(
    bio: Double,
    cmd: Int,
    larg: Double,
    parg: Double,
): Double

@JsFun("(bio) => globalThis.__kotlinmania_openssl_native.bioGetMemDataHex(bio)")
internal external fun wasmBioGetMemDataHex(bio: Double): String

@JsFun("() => globalThis.__kotlinmania_openssl_native.evpSha1()")
internal external fun wasmEvpSha1(): Double

@JsFun("() => globalThis.__kotlinmania_openssl_native.evpSha224()")
internal external fun wasmEvpSha224(): Double

@JsFun("() => globalThis.__kotlinmania_openssl_native.evpSha256()")
internal external fun wasmEvpSha256(): Double

@JsFun("() => globalThis.__kotlinmania_openssl_native.evpSha384()")
internal external fun wasmEvpSha384(): Double

@JsFun("() => globalThis.__kotlinmania_openssl_native.evpSha512()")
internal external fun wasmEvpSha512(): Double

@JsFun("(name) => globalThis.__kotlinmania_openssl_native.evpGetDigestbyname(name)")
internal external fun wasmEvpGetDigestbyname(name: String): Double

@JsFun("(md) => globalThis.__kotlinmania_openssl_native.evpMdGetSize(md)")
internal external fun wasmEvpMdGetSize(md: Double): Int

@JsFun("() => globalThis.__kotlinmania_openssl_native.evpMdCtxNew()")
internal external fun wasmEvpMdCtxNew(): Double

@JsFun("(ctx) => globalThis.__kotlinmania_openssl_native.evpMdCtxFree(ctx)")
internal external fun wasmEvpMdCtxFree(ctx: Double)

@JsFun("(ctx, type, impl) => globalThis.__kotlinmania_openssl_native.evpDigestInitEx(ctx, type, impl)")
internal external fun wasmEvpDigestInitEx(
    ctx: Double,
    type: Double,
    impl: Double,
): Int

@JsFun("(ctx, hex) => globalThis.__kotlinmania_openssl_native.evpDigestUpdateHex(ctx, hex)")
internal external fun wasmEvpDigestUpdateHex(
    ctx: Double,
    hex: String,
): Int

@JsFun("(ctx) => globalThis.__kotlinmania_openssl_native.evpDigestFinalExHex(ctx)")
internal external fun wasmEvpDigestFinalExHex(ctx: Double): String

@JsFun("() => globalThis.__kotlinmania_openssl_native.tlsMethod()")
internal external fun wasmTlsMethod(): Double

@JsFun("() => globalThis.__kotlinmania_openssl_native.tlsClientMethod()")
internal external fun wasmTlsClientMethod(): Double

@JsFun("() => globalThis.__kotlinmania_openssl_native.tlsServerMethod()")
internal external fun wasmTlsServerMethod(): Double

@JsFun("(method) => globalThis.__kotlinmania_openssl_native.sslCtxNew(method)")
internal external fun wasmSslCtxNew(method: Double): Double

@JsFun("(ctx) => globalThis.__kotlinmania_openssl_native.sslCtxFree(ctx)")
internal external fun wasmSslCtxFree(ctx: Double)

@JsFun("(ctx, cmd, larg, parg) => globalThis.__kotlinmania_openssl_native.sslCtxCtrl(ctx, cmd, larg, parg)")
internal external fun wasmSslCtxCtrl(
    ctx: Double,
    cmd: Int,
    larg: Double,
    parg: Double,
): Double

@JsFun("(ctx) => globalThis.__kotlinmania_openssl_native.sslCtxGetOptions(ctx)")
internal external fun wasmSslCtxGetOptions(ctx: Double): Double

@JsFun("(ctx, op) => globalThis.__kotlinmania_openssl_native.sslCtxSetOptions(ctx, op)")
internal external fun wasmSslCtxSetOptions(
    ctx: Double,
    op: Double,
): Double

@JsFun("(ctx, op) => globalThis.__kotlinmania_openssl_native.sslCtxClearOptions(ctx, op)")
internal external fun wasmSslCtxClearOptions(
    ctx: Double,
    op: Double,
): Double

@JsFun("(ctx) => globalThis.__kotlinmania_openssl_native.sslNew(ctx)")
internal external fun wasmSslNew(ctx: Double): Double

@JsFun("(ssl) => globalThis.__kotlinmania_openssl_native.sslFree(ssl)")
internal external fun wasmSslFree(ssl: Double)

@JsFun("(ssl, cmd, larg, parg) => globalThis.__kotlinmania_openssl_native.sslCtrl(ssl, cmd, larg, parg)")
internal external fun wasmSslCtrl(
    ssl: Double,
    cmd: Int,
    larg: Double,
    parg: Double,
): Double

@JsFun("() => globalThis.__kotlinmania_openssl_native.errGetError()")
internal external fun wasmErrGetError(): Double

@JsFun("() => globalThis.__kotlinmania_openssl_native.errPeekError()")
internal external fun wasmErrPeekError(): Double

@JsFun("() => globalThis.__kotlinmania_openssl_native.errPeekLastError()")
internal external fun wasmErrPeekLastError(): Double

@JsFun("() => globalThis.__kotlinmania_openssl_native.errClearError()")
internal external fun wasmErrClearError()

@JsFun("(e, maxLen) => globalThis.__kotlinmania_openssl_native.errErrorStringN(e, maxLen)")
internal external fun wasmErrErrorStringN(
    e: Double,
    maxLen: Int,
): String

@JsFun("(num) => globalThis.__kotlinmania_openssl_native.randBytesHex(num)")
internal external fun wasmRandBytesHex(num: Int): String

@JsFun("() => globalThis.__kotlinmania_openssl_native.randStatus()")
internal external fun wasmRandStatus(): Int

@JsFun("(n) => globalThis.__kotlinmania_openssl_native.objNid2sn(n)")
internal external fun wasmObjNid2sn(n: Int): String?

@JsFun("(n) => globalThis.__kotlinmania_openssl_native.objNid2ln(n)")
internal external fun wasmObjNid2ln(n: Int): String?

@JsFun("(s) => globalThis.__kotlinmania_openssl_native.objSn2nid(s)")
internal external fun wasmObjSn2nid(s: String): Int

@JsFun("(s) => globalThis.__kotlinmania_openssl_native.objLn2nid(s)")
internal external fun wasmObjLn2nid(s: String): Int

@JsFun("(s) => globalThis.__kotlinmania_openssl_native.objTxt2nid(s)")
internal external fun wasmObjTxt2nid(s: String): Int
