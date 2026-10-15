// port-lint: source lib.rs crypto.rs sha.rs bio.rs evp.rs ssl.rs err.rs handwritten/rand.rs handwritten/object.rs
package io.github.kotlinmania.opensslsys

import io.github.kotlinmania.getrandom.getrandom
import io.github.kotlinmania.opensslsys.internal.ensureOpensslAddonLoaded
import io.github.kotlinmania.opensslsys.internal.wasmBioCtrl
import io.github.kotlinmania.opensslsys.internal.wasmBioFreeAll
import io.github.kotlinmania.opensslsys.internal.wasmBioGetMemDataHex
import io.github.kotlinmania.opensslsys.internal.wasmBioNew
import io.github.kotlinmania.opensslsys.internal.wasmBioReadHex
import io.github.kotlinmania.opensslsys.internal.wasmBioSMem
import io.github.kotlinmania.opensslsys.internal.wasmBioWriteHex
import io.github.kotlinmania.opensslsys.internal.wasmCryptoFree
import io.github.kotlinmania.opensslsys.internal.wasmCryptoMalloc
import io.github.kotlinmania.opensslsys.internal.wasmErrClearError
import io.github.kotlinmania.opensslsys.internal.wasmErrErrorStringN
import io.github.kotlinmania.opensslsys.internal.wasmErrGetError
import io.github.kotlinmania.opensslsys.internal.wasmErrPeekError
import io.github.kotlinmania.opensslsys.internal.wasmErrPeekLastError
import io.github.kotlinmania.opensslsys.internal.wasmEvpDigestFinalExHex
import io.github.kotlinmania.opensslsys.internal.wasmEvpDigestInitEx
import io.github.kotlinmania.opensslsys.internal.wasmEvpDigestUpdateHex
import io.github.kotlinmania.opensslsys.internal.wasmEvpGetDigestbyname
import io.github.kotlinmania.opensslsys.internal.wasmEvpMdCtxFree
import io.github.kotlinmania.opensslsys.internal.wasmEvpMdCtxNew
import io.github.kotlinmania.opensslsys.internal.wasmEvpMdGetSize
import io.github.kotlinmania.opensslsys.internal.wasmEvpSha1
import io.github.kotlinmania.opensslsys.internal.wasmEvpSha224
import io.github.kotlinmania.opensslsys.internal.wasmEvpSha256
import io.github.kotlinmania.opensslsys.internal.wasmEvpSha384
import io.github.kotlinmania.opensslsys.internal.wasmEvpSha512
import io.github.kotlinmania.opensslsys.internal.wasmObjLn2nid
import io.github.kotlinmania.opensslsys.internal.wasmObjNid2ln
import io.github.kotlinmania.opensslsys.internal.wasmObjNid2sn
import io.github.kotlinmania.opensslsys.internal.wasmObjSn2nid
import io.github.kotlinmania.opensslsys.internal.wasmObjTxt2nid
import io.github.kotlinmania.opensslsys.internal.wasmOpenSslVersion
import io.github.kotlinmania.opensslsys.internal.wasmOpenSslVersionNum
import io.github.kotlinmania.opensslsys.internal.wasmOpensslInitCrypto
import io.github.kotlinmania.opensslsys.internal.wasmOpensslInitSsl
import io.github.kotlinmania.opensslsys.internal.wasmRandBytesHex
import io.github.kotlinmania.opensslsys.internal.wasmRandStatus
import io.github.kotlinmania.opensslsys.internal.wasmSha1Hex
import io.github.kotlinmania.opensslsys.internal.wasmSha224Hex
import io.github.kotlinmania.opensslsys.internal.wasmSha256Hex
import io.github.kotlinmania.opensslsys.internal.wasmSha384Hex
import io.github.kotlinmania.opensslsys.internal.wasmSha512Hex
import io.github.kotlinmania.opensslsys.internal.wasmSslCtrl
import io.github.kotlinmania.opensslsys.internal.wasmSslCtxClearOptions
import io.github.kotlinmania.opensslsys.internal.wasmSslCtxCtrl
import io.github.kotlinmania.opensslsys.internal.wasmSslCtxFree
import io.github.kotlinmania.opensslsys.internal.wasmSslCtxGetOptions
import io.github.kotlinmania.opensslsys.internal.wasmSslCtxNew
import io.github.kotlinmania.opensslsys.internal.wasmSslCtxSetOptions
import io.github.kotlinmania.opensslsys.internal.wasmSslFree
import io.github.kotlinmania.opensslsys.internal.wasmSslNew
import io.github.kotlinmania.opensslsys.internal.wasmTlsClientMethod
import io.github.kotlinmania.opensslsys.internal.wasmTlsMethod
import io.github.kotlinmania.opensslsys.internal.wasmTlsServerMethod
import io.github.kotlinmania.sha1.Sha1
import io.github.kotlinmania.sha2.Sha224
import io.github.kotlinmania.sha2.Sha256
import io.github.kotlinmania.sha2.Sha384
import io.github.kotlinmania.sha2.Sha512

private val HEX_DIGITS = "0123456789abcdef".toCharArray()

private fun ByteArray.toHex(limit: Int = size): String {
    val n = limit.coerceIn(0, size)
    val chars = CharArray(n * 2)
    for (i in 0 until n) {
        val v = this[i].toInt() and 0xFF
        chars[i * 2] = HEX_DIGITS[v ushr 4]
        chars[i * 2 + 1] = HEX_DIGITS[v and 0x0F]
    }
    return chars.concatToString()
}

private fun hexVal(c: Char): Int =
    when (c) {
        in '0'..'9' -> c - '0'
        in 'a'..'f' -> c - 'a' + 10
        in 'A'..'F' -> c - 'A' + 10
        else -> 0
    }

private fun String.decodeHex(): ByteArray {
    val n = length / 2
    val out = ByteArray(n)
    for (i in 0 until n) {
        out[i] = ((hexVal(this[i * 2]) shl 4) or hexVal(this[i * 2 + 1])).toByte()
    }
    return out
}

private var nextHandleId: Long = 0x1000L
private val allocatedBlocks = mutableMapOf<Long, Int>()

private const val BIO_S_MEM_HANDLE: Long = 0x10L
private const val EVP_MD_SHA1_HANDLE: Long = 0x21L
private const val EVP_MD_SHA224_HANDLE: Long = 0x22L
private const val EVP_MD_SHA256_HANDLE: Long = 0x23L
private const val EVP_MD_SHA384_HANDLE: Long = 0x24L
private const val EVP_MD_SHA512_HANDLE: Long = 0x25L
private const val TLS_METHOD_HANDLE: Long = 0x31L
private const val TLS_CLIENT_METHOD_HANDLE: Long = 0x32L
private const val TLS_SERVER_METHOD_HANDLE: Long = 0x33L

private class WasmBrowserBioState {
    var data: ByteArray = ByteArray(0)
    var readPos: Int = 0
    var eofReturn: Int = -1

    val pending: Int
        get() = (data.size - readPos).coerceAtLeast(0)
}

private class WasmBrowserEvpMdCtxState {
    var mdHandle: Long = 0L
    val buffer = ArrayList<Byte>()
}

private class WasmBrowserSslCtxState(
    val methodHandle: Long,
) {
    var options: ULong = 0uL
    var mode: Long = 0L
    var minProtoVersion: Long = 0L
    var maxProtoVersion: Long = 0L
}

private class WasmBrowserSslState(
    val ctxHandle: Long,
) {
    var options: ULong = 0uL
    var mode: Long = 0L
    var minProtoVersion: Long = 0L
    var maxProtoVersion: Long = 0L
}

private val bioTable = mutableMapOf<Long, WasmBrowserBioState>()
private val evpCtxTable = mutableMapOf<Long, WasmBrowserEvpMdCtxState>()
private val sslCtxTable = mutableMapOf<Long, WasmBrowserSslCtxState>()
private val sslTable = mutableMapOf<Long, WasmBrowserSslState>()
private val errorQueue = ArrayDeque<ULong>()

private data class NidEntry(
    val nid: Int,
    val sn: String,
    val ln: String,
    val oid: String,
)

private val knownNids =
    listOf(
        NidEntry(NID_undef, "UNDEF", "undefined", "0.0"),
        NidEntry(NID_sha1, "SHA1", "sha1", "1.3.14.3.2.26"),
        NidEntry(NID_sha224, "SHA224", "sha224", "2.16.840.1.101.3.4.2.4"),
        NidEntry(NID_sha256, "SHA256", "sha256", "2.16.840.1.101.3.4.2.1"),
        NidEntry(NID_sha384, "SHA384", "sha384", "2.16.840.1.101.3.4.2.2"),
        NidEntry(NID_sha512, "SHA512", "sha512", "2.16.840.1.101.3.4.2.3"),
        NidEntry(NID_X9_62_prime256v1, "prime256v1", "X9.62 prime256v1", "1.2.840.10045.3.1.7"),
        NidEntry(NID_secp384r1, "secp384r1", "secp384r1", "1.3.132.0.34"),
        NidEntry(NID_secp521r1, "secp521r1", "secp521r1", "1.3.132.0.35"),
        NidEntry(NID_ED25519, "ED25519", "ED25519", "1.3.101.112"),
    )

public actual fun openSslVersionNum(): ULong =
    if (ensureOpensslAddonLoaded()) {
        wasmOpenSslVersionNum().toLong().toULong()
    } else {
        0x30000000uL
    }

public actual fun openSslVersion(key: Int): String? =
    if (ensureOpensslAddonLoaded()) {
        wasmOpenSslVersion(key)
    } else {
        when (key) {
            OPENSSL_VERSION -> "OpenSSL 3.0.0 (WasmJS)"
            OPENSSL_CFLAGS -> "compiler: wasm-js"
            OPENSSL_BUILT_ON -> "built on: WasmJS"
            OPENSSL_PLATFORM -> "platform: wasm-js"
            OPENSSL_DIR -> "OPENSSLDIR: \"/ssl\""
            OPENSSL_ENGINES_DIR -> "ENGINESDIR: \"/ssl/engines\""
            OPENSSL_VERSION_STRING -> "3.0.0"
            OPENSSL_FULL_VERSION_STRING -> "OpenSSL 3.0.0 (WasmJS)"
            OPENSSL_MODULES_DIR -> "MODULESDIR: \"/ssl/ossl-modules\""
            OPENSSL_CPU_INFO -> "CPUINFO: wasm32"
            else -> null
        }
    }

public actual fun opensslInitCrypto(
    opts: ULong,
    settings: OpensslInitSettings?,
): Int =
    if (ensureOpensslAddonLoaded()) {
        wasmOpensslInitCrypto(opts.toDouble(), (settings?.address ?: 0L).toDouble())
    } else {
        1
    }

public actual fun cryptoMalloc(
    num: ULong,
    file: String,
    line: Int,
): Long {
    if (ensureOpensslAddonLoaded()) {
        return wasmCryptoMalloc(num.toDouble(), file, line).toLong()
    }
    if (num == 0uL) return 0L
    val handle = nextHandleId++
    allocatedBlocks[handle] = num.toInt()
    return handle
}

public actual fun cryptoFree(
    addr: Long,
    file: String,
    line: Int,
) {
    if (ensureOpensslAddonLoaded()) {
        if (addr != 0L) {
            wasmCryptoFree(addr.toDouble(), file, line)
        }
    } else {
        allocatedBlocks.remove(addr)
    }
}

public actual fun sha1(data: ByteArray): ByteArray? =
    if (ensureOpensslAddonLoaded()) {
        wasmSha1Hex(data.toHex())?.decodeHex()
    } else {
        Sha1.digest(data)
    }

public actual fun sha224(data: ByteArray): ByteArray? =
    if (ensureOpensslAddonLoaded()) {
        wasmSha224Hex(data.toHex())?.decodeHex()
    } else {
        Sha224.digest(data)
    }

public actual fun sha256(data: ByteArray): ByteArray? =
    if (ensureOpensslAddonLoaded()) {
        wasmSha256Hex(data.toHex())?.decodeHex()
    } else {
        Sha256.digest(data)
    }

public actual fun sha384(data: ByteArray): ByteArray? =
    if (ensureOpensslAddonLoaded()) {
        wasmSha384Hex(data.toHex())?.decodeHex()
    } else {
        Sha384.digest(data)
    }

public actual fun sha512(data: ByteArray): ByteArray? =
    if (ensureOpensslAddonLoaded()) {
        wasmSha512Hex(data.toHex())?.decodeHex()
    } else {
        Sha512.digest(data)
    }

public actual fun bioSMem(): BioMethod? =
    if (ensureOpensslAddonLoaded()) {
        val ptr = wasmBioSMem().toLong()
        if (ptr != 0L) BioMethod(ptr) else null
    } else {
        BioMethod(BIO_S_MEM_HANDLE)
    }

public actual fun bioNew(type: BioMethod): Bio? {
    if (ensureOpensslAddonLoaded()) {
        val ptr = wasmBioNew(type.address.toDouble()).toLong()
        return if (ptr != 0L) Bio(ptr) else null
    }
    if (type.address != BIO_S_MEM_HANDLE) return null
    val handle = nextHandleId++
    bioTable[handle] = WasmBrowserBioState()
    return Bio(handle)
}

public actual fun bioFreeAll(b: Bio?) {
    if (ensureOpensslAddonLoaded()) {
        if (b != null && b.address != 0L) {
            wasmBioFreeAll(b.address.toDouble())
        }
    } else if (b != null) {
        bioTable.remove(b.address)
    }
}

public actual fun bioRead(
    b: Bio,
    buf: ByteArray,
    len: Int,
): Int {
    val actualLen = len.coerceIn(0, buf.size)
    if (actualLen == 0) return 0
    if (ensureOpensslAddonLoaded()) {
        val packed = wasmBioReadHex(b.address.toDouble(), actualLen)
        val colon = packed.indexOf(':')
        if (colon < 0) return -1
        val rc = packed.substring(0, colon).toIntOrNull() ?: -1
        if (rc > 0) {
            val bytes = packed.substring(colon + 1).decodeHex()
            bytes.copyInto(buf, 0, 0, bytes.size.coerceAtMost(buf.size))
        }
        return rc
    }
    val state = bioTable[b.address] ?: return -1
    val avail = state.pending
    if (avail <= 0) return state.eofReturn
    val toRead = actualLen.coerceAtMost(avail)
    state.data.copyInto(buf, 0, state.readPos, state.readPos + toRead)
    state.readPos += toRead
    if (state.readPos >= state.data.size) {
        state.data = ByteArray(0)
        state.readPos = 0
    }
    return toRead
}

public actual fun bioWrite(
    b: Bio,
    buf: ByteArray,
    len: Int,
): Int {
    val actualLen = len.coerceIn(0, buf.size)
    if (actualLen == 0) return 0
    if (ensureOpensslAddonLoaded()) {
        return wasmBioWriteHex(b.address.toDouble(), buf.toHex(actualLen), actualLen)
    }
    val state = bioTable[b.address] ?: return -1
    val unread = state.pending
    val next = ByteArray(unread + actualLen)
    if (unread > 0) {
        state.data.copyInto(next, 0, state.readPos, state.data.size)
    }
    buf.copyInto(next, unread, 0, actualLen)
    state.data = next
    state.readPos = 0
    return actualLen
}

public actual fun bioCtrl(
    b: Bio,
    cmd: Int,
    larg: Long,
    parg: Long,
): Long {
    if (ensureOpensslAddonLoaded()) {
        return wasmBioCtrl(b.address.toDouble(), cmd, larg.toDouble(), parg.toDouble()).toLong()
    }
    val state = bioTable[b.address] ?: return -1L
    return when (cmd) {
        BIO_CTRL_RESET -> {
            state.data = ByteArray(0)
            state.readPos = 0
            1L
        }

        BIO_CTRL_EOF -> {
            if (state.pending == 0) 1L else 0L
        }

        BIO_CTRL_INFO -> {
            state.pending.toLong()
        }

        BIO_CTRL_PENDING -> {
            state.pending.toLong()
        }

        BIO_CTRL_WPENDING -> {
            0L
        }

        BIO_CTRL_FLUSH -> {
            1L
        }

        BIO_C_SET_BUF_MEM_EOF_RETURN -> {
            state.eofReturn = larg.toInt()
            1L
        }

        else -> {
            0L
        }
    }
}

public actual fun bioGetMemData(b: Bio): ByteArray {
    if (ensureOpensslAddonLoaded()) {
        return wasmBioGetMemDataHex(b.address.toDouble()).decodeHex()
    }
    val state = bioTable[b.address] ?: return ByteArray(0)
    val avail = state.pending
    return if (avail <= 0) {
        ByteArray(0)
    } else {
        state.data.copyOfRange(state.readPos, state.data.size)
    }
}

public actual fun evpSha1(): EvpMd? =
    if (ensureOpensslAddonLoaded()) {
        val ptr = wasmEvpSha1().toLong()
        if (ptr != 0L) EvpMd(ptr) else null
    } else {
        EvpMd(EVP_MD_SHA1_HANDLE)
    }

public actual fun evpSha224(): EvpMd? =
    if (ensureOpensslAddonLoaded()) {
        val ptr = wasmEvpSha224().toLong()
        if (ptr != 0L) EvpMd(ptr) else null
    } else {
        EvpMd(EVP_MD_SHA224_HANDLE)
    }

public actual fun evpSha256(): EvpMd? =
    if (ensureOpensslAddonLoaded()) {
        val ptr = wasmEvpSha256().toLong()
        if (ptr != 0L) EvpMd(ptr) else null
    } else {
        EvpMd(EVP_MD_SHA256_HANDLE)
    }

public actual fun evpSha384(): EvpMd? =
    if (ensureOpensslAddonLoaded()) {
        val ptr = wasmEvpSha384().toLong()
        if (ptr != 0L) EvpMd(ptr) else null
    } else {
        EvpMd(EVP_MD_SHA384_HANDLE)
    }

public actual fun evpSha512(): EvpMd? =
    if (ensureOpensslAddonLoaded()) {
        val ptr = wasmEvpSha512().toLong()
        if (ptr != 0L) EvpMd(ptr) else null
    } else {
        EvpMd(EVP_MD_SHA512_HANDLE)
    }

public actual fun evpGetDigestbyname(name: String): EvpMd? =
    if (ensureOpensslAddonLoaded()) {
        val ptr = wasmEvpGetDigestbyname(name).toLong()
        if (ptr != 0L) EvpMd(ptr) else null
    } else {
        when (name.uppercase().replace("-", "")) {
            "SHA1" -> evpSha1()
            "SHA224" -> evpSha224()
            "SHA256" -> evpSha256()
            "SHA384" -> evpSha384()
            "SHA512" -> evpSha512()
            else -> null
        }
    }

public actual fun evpMdGetSize(md: EvpMd): Int =
    if (ensureOpensslAddonLoaded()) {
        wasmEvpMdGetSize(md.address.toDouble())
    } else {
        when (md.address) {
            EVP_MD_SHA1_HANDLE -> SHA_DIGEST_LENGTH
            EVP_MD_SHA224_HANDLE -> SHA224_DIGEST_LENGTH
            EVP_MD_SHA256_HANDLE -> SHA256_DIGEST_LENGTH
            EVP_MD_SHA384_HANDLE -> SHA384_DIGEST_LENGTH
            EVP_MD_SHA512_HANDLE -> SHA512_DIGEST_LENGTH
            else -> 0
        }
    }

public actual fun evpMdCtxNew(): EvpMdCtx? =
    if (ensureOpensslAddonLoaded()) {
        val ptr = wasmEvpMdCtxNew().toLong()
        if (ptr != 0L) EvpMdCtx(ptr) else null
    } else {
        val handle = nextHandleId++
        evpCtxTable[handle] = WasmBrowserEvpMdCtxState()
        EvpMdCtx(handle)
    }

public actual fun evpMdCtxFree(ctx: EvpMdCtx?) {
    if (ensureOpensslAddonLoaded()) {
        if (ctx != null && ctx.address != 0L) {
            wasmEvpMdCtxFree(ctx.address.toDouble())
        }
    } else if (ctx != null) {
        evpCtxTable.remove(ctx.address)
    }
}

public actual fun evpDigestInitEx(
    ctx: EvpMdCtx,
    type: EvpMd,
    impl: Engine?,
): Int {
    if (ensureOpensslAddonLoaded()) {
        return wasmEvpDigestInitEx(
            ctx.address.toDouble(),
            type.address.toDouble(),
            (impl?.address ?: 0L).toDouble(),
        )
    }
    val state = evpCtxTable[ctx.address] ?: return 0
    if (evpMdGetSize(type) <= 0) return 0
    state.mdHandle = type.address
    state.buffer.clear()
    return 1
}

public actual fun evpDigestUpdate(
    ctx: EvpMdCtx,
    d: ByteArray,
): Int {
    if (ensureOpensslAddonLoaded()) {
        return wasmEvpDigestUpdateHex(ctx.address.toDouble(), d.toHex())
    }
    val state = evpCtxTable[ctx.address] ?: return 0
    if (state.mdHandle == 0L) return 0
    for (b in d) {
        state.buffer.add(b)
    }
    return 1
}

public actual fun evpDigestFinalEx(
    ctx: EvpMdCtx,
    md: ByteArray,
    s: IntArray?,
): Int {
    if (ensureOpensslAddonLoaded()) {
        val packed = wasmEvpDigestFinalExHex(ctx.address.toDouble())
        val colon = packed.indexOf(':')
        if (colon < 0) return 0
        val rc = packed.substring(0, colon).toIntOrNull() ?: 0
        if (rc == 1) {
            val bytes = packed.substring(colon + 1).decodeHex()
            bytes.copyInto(md, 0, 0, bytes.size.coerceAtMost(md.size))
            if (s != null && s.isNotEmpty()) {
                s[0] = bytes.size
            }
        }
        return rc
    }
    val state = evpCtxTable[ctx.address] ?: return 0
    val input = state.buffer.toByteArray()
    val digest =
        when (state.mdHandle) {
            EVP_MD_SHA1_HANDLE -> sha1(input)
            EVP_MD_SHA224_HANDLE -> sha224(input)
            EVP_MD_SHA256_HANDLE -> sha256(input)
            EVP_MD_SHA384_HANDLE -> sha384(input)
            EVP_MD_SHA512_HANDLE -> sha512(input)
            else -> null
        } ?: return 0
    digest.copyInto(md, 0, 0, digest.size.coerceAtMost(md.size))
    if (s != null && s.isNotEmpty()) {
        s[0] = digest.size
    }
    state.buffer.clear()
    return 1
}

public actual fun opensslInitSsl(
    opts: ULong,
    settings: OpensslInitSettings?,
): Int =
    if (ensureOpensslAddonLoaded()) {
        wasmOpensslInitSsl(opts.toDouble(), (settings?.address ?: 0L).toDouble())
    } else {
        1
    }

public actual fun tlsMethod(): SslMethod? =
    if (ensureOpensslAddonLoaded()) {
        val ptr = wasmTlsMethod().toLong()
        if (ptr != 0L) SslMethod(ptr) else null
    } else {
        SslMethod(TLS_METHOD_HANDLE)
    }

public actual fun tlsClientMethod(): SslMethod? =
    if (ensureOpensslAddonLoaded()) {
        val ptr = wasmTlsClientMethod().toLong()
        if (ptr != 0L) SslMethod(ptr) else null
    } else {
        SslMethod(TLS_CLIENT_METHOD_HANDLE)
    }

public actual fun tlsServerMethod(): SslMethod? =
    if (ensureOpensslAddonLoaded()) {
        val ptr = wasmTlsServerMethod().toLong()
        if (ptr != 0L) SslMethod(ptr) else null
    } else {
        SslMethod(TLS_SERVER_METHOD_HANDLE)
    }

public actual fun sslCtxNew(method: SslMethod): SslCtx? {
    if (ensureOpensslAddonLoaded()) {
        val ptr = wasmSslCtxNew(method.address.toDouble()).toLong()
        return if (ptr != 0L) SslCtx(ptr) else null
    }
    if (method.address == 0L) return null
    val handle = nextHandleId++
    sslCtxTable[handle] = WasmBrowserSslCtxState(method.address)
    return SslCtx(handle)
}

public actual fun sslCtxFree(ctx: SslCtx?) {
    if (ensureOpensslAddonLoaded()) {
        if (ctx != null && ctx.address != 0L) {
            wasmSslCtxFree(ctx.address.toDouble())
        }
    } else if (ctx != null) {
        sslCtxTable.remove(ctx.address)
    }
}

public actual fun sslCtxCtrl(
    ctx: SslCtx,
    cmd: Int,
    larg: Long,
    parg: Long,
): Long {
    if (ensureOpensslAddonLoaded()) {
        return wasmSslCtxCtrl(ctx.address.toDouble(), cmd, larg.toDouble(), parg.toDouble()).toLong()
    }
    val state = sslCtxTable[ctx.address] ?: return 0L
    return when (cmd) {
        SSL_CTRL_MODE -> {
            state.mode = state.mode or larg
            state.mode
        }

        SSL_CTRL_SET_MIN_PROTO_VERSION -> {
            state.minProtoVersion = larg
            1L
        }

        SSL_CTRL_SET_MAX_PROTO_VERSION -> {
            state.maxProtoVersion = larg
            1L
        }

        SSL_CTRL_GET_MIN_PROTO_VERSION -> {
            state.minProtoVersion
        }

        SSL_CTRL_GET_MAX_PROTO_VERSION -> {
            state.maxProtoVersion
        }

        else -> {
            1L
        }
    }
}

public actual fun sslCtxGetOptions(ctx: SslCtx): ULong =
    if (ensureOpensslAddonLoaded()) {
        wasmSslCtxGetOptions(ctx.address.toDouble()).toLong().toULong()
    } else {
        sslCtxTable[ctx.address]?.options ?: 0uL
    }

public actual fun sslCtxSetOptions(
    ctx: SslCtx,
    op: ULong,
): ULong {
    if (ensureOpensslAddonLoaded()) {
        return wasmSslCtxSetOptions(ctx.address.toDouble(), op.toDouble()).toLong().toULong()
    }
    val state = sslCtxTable[ctx.address] ?: return 0uL
    state.options = state.options or op
    return state.options
}

public actual fun sslCtxClearOptions(
    ctx: SslCtx,
    op: ULong,
): ULong {
    if (ensureOpensslAddonLoaded()) {
        return wasmSslCtxClearOptions(ctx.address.toDouble(), op.toDouble()).toLong().toULong()
    }
    val state = sslCtxTable[ctx.address] ?: return 0uL
    state.options = state.options and op.inv()
    return state.options
}

public actual fun sslNew(ctx: SslCtx): Ssl? {
    if (ensureOpensslAddonLoaded()) {
        val ptr = wasmSslNew(ctx.address.toDouble()).toLong()
        return if (ptr != 0L) Ssl(ptr) else null
    }
    val ctxState = sslCtxTable[ctx.address] ?: return null
    val handle = nextHandleId++
    val sslState = WasmBrowserSslState(ctx.address)
    sslState.options = ctxState.options
    sslState.mode = ctxState.mode
    sslState.minProtoVersion = ctxState.minProtoVersion
    sslState.maxProtoVersion = ctxState.maxProtoVersion
    sslTable[handle] = sslState
    return Ssl(handle)
}

public actual fun sslFree(ssl: Ssl?) {
    if (ensureOpensslAddonLoaded()) {
        if (ssl != null && ssl.address != 0L) {
            wasmSslFree(ssl.address.toDouble())
        }
    } else if (ssl != null) {
        sslTable.remove(ssl.address)
    }
}

public actual fun sslCtrl(
    ssl: Ssl,
    cmd: Int,
    larg: Long,
    parg: Long,
): Long {
    if (ensureOpensslAddonLoaded()) {
        return wasmSslCtrl(ssl.address.toDouble(), cmd, larg.toDouble(), parg.toDouble()).toLong()
    }
    val state = sslTable[ssl.address] ?: return 0L
    return when (cmd) {
        SSL_CTRL_MODE -> {
            state.mode = state.mode or larg
            state.mode
        }

        SSL_CTRL_SET_MIN_PROTO_VERSION -> {
            state.minProtoVersion = larg
            1L
        }

        SSL_CTRL_SET_MAX_PROTO_VERSION -> {
            state.maxProtoVersion = larg
            1L
        }

        SSL_CTRL_GET_MIN_PROTO_VERSION -> {
            state.minProtoVersion
        }

        SSL_CTRL_GET_MAX_PROTO_VERSION -> {
            state.maxProtoVersion
        }

        else -> {
            1L
        }
    }
}

public actual fun errGetError(): ULong =
    if (ensureOpensslAddonLoaded()) {
        wasmErrGetError().toLong().toULong()
    } else {
        if (errorQueue.isEmpty()) 0uL else errorQueue.removeFirst()
    }

public actual fun errPeekError(): ULong =
    if (ensureOpensslAddonLoaded()) {
        wasmErrPeekError().toLong().toULong()
    } else {
        errorQueue.firstOrNull() ?: 0uL
    }

public actual fun errPeekLastError(): ULong =
    if (ensureOpensslAddonLoaded()) {
        wasmErrPeekLastError().toLong().toULong()
    } else {
        errorQueue.lastOrNull() ?: 0uL
    }

public actual fun errClearError() {
    if (ensureOpensslAddonLoaded()) {
        wasmErrClearError()
    } else {
        errorQueue.clear()
    }
}

public actual fun errErrorStringN(
    e: ULong,
    maxLen: Int,
): String {
    if (ensureOpensslAddonLoaded()) {
        return wasmErrErrorStringN(e.toDouble(), maxLen)
    }
    val lib = errGetLib(e)
    val reason = errGetReason(e)
    val full = "error:${e.toString(16).padStart(8, '0')}:lib($lib)::reason($reason)"
    return if (maxLen > 0 && full.length > maxLen) full.substring(0, maxLen) else full
}

public actual fun randBytes(
    buf: ByteArray,
    num: Int,
): Int {
    if (num < 0) return 0
    val actual = num.coerceIn(0, buf.size)
    if (actual == 0) return 1
    if (ensureOpensslAddonLoaded()) {
        val packed = wasmRandBytesHex(actual)
        val colon = packed.indexOf(':')
        if (colon < 0) return 0
        val rc = packed.substring(0, colon).toIntOrNull() ?: 0
        if (rc == 1) {
            val bytes = packed.substring(colon + 1).decodeHex()
            bytes.copyInto(buf, 0, 0, bytes.size.coerceAtMost(buf.size))
        }
        return rc
    }
    val tmp = ByteArray(actual)
    return if (getrandom(tmp).isOk) {
        tmp.copyInto(buf, 0, 0, actual)
        1
    } else {
        0
    }
}

public actual fun randStatus(): Int = if (ensureOpensslAddonLoaded()) wasmRandStatus() else 1

public actual fun objNid2sn(n: Int): String? =
    if (ensureOpensslAddonLoaded()) {
        wasmObjNid2sn(n)
    } else {
        knownNids.firstOrNull { it.nid == n }?.sn
    }

public actual fun objNid2ln(n: Int): String? =
    if (ensureOpensslAddonLoaded()) {
        wasmObjNid2ln(n)
    } else {
        knownNids.firstOrNull { it.nid == n }?.ln
    }

public actual fun objSn2nid(sn: String): Int =
    if (ensureOpensslAddonLoaded()) {
        wasmObjSn2nid(sn)
    } else {
        knownNids.firstOrNull { it.sn.equals(sn, ignoreCase = true) }?.nid ?: NID_undef
    }

public actual fun objLn2nid(ln: String): Int =
    if (ensureOpensslAddonLoaded()) {
        wasmObjLn2nid(ln)
    } else {
        knownNids.firstOrNull { it.ln.equals(ln, ignoreCase = true) }?.nid ?: NID_undef
    }

public actual fun objTxt2nid(s: String): Int =
    if (ensureOpensslAddonLoaded()) {
        wasmObjTxt2nid(s)
    } else {
        knownNids
            .firstOrNull {
                it.sn.equals(s, ignoreCase = true) ||
                    it.ln.equals(s, ignoreCase = true) ||
                    it.oid == s
            }?.nid ?: NID_undef
    }
