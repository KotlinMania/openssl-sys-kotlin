package io.github.kotlinmania.opensslsys

import io.github.kotlinmania.getrandom.getrandom
import io.github.kotlinmania.sha1.Sha1
import io.github.kotlinmania.sha2.Sha224
import io.github.kotlinmania.sha2.Sha256
import io.github.kotlinmania.sha2.Sha384
import io.github.kotlinmania.sha2.Sha512

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

private class AndroidBioState {
    var data: ByteArray = ByteArray(0)
    var readPos: Int = 0
    var eofReturn: Int = -1

    val pending: Int
        get() = (data.size - readPos).coerceAtLeast(0)
}

private class AndroidEvpMdCtxState {
    var mdHandle: Long = 0L
    val buffer = ArrayList<Byte>()
}

private class AndroidSslCtxState(
    val methodHandle: Long,
) {
    var options: ULong = 0uL
    var mode: Long = 0L
    var minProtoVersion: Long = 0L
    var maxProtoVersion: Long = 0L
}

private class AndroidSslState(
    val ctxHandle: Long,
) {
    var options: ULong = 0uL
    var mode: Long = 0L
    var minProtoVersion: Long = 0L
    var maxProtoVersion: Long = 0L
}

private val bioTable = mutableMapOf<Long, AndroidBioState>()
private val evpCtxTable = mutableMapOf<Long, AndroidEvpMdCtxState>()
private val sslCtxTable = mutableMapOf<Long, AndroidSslCtxState>()
private val sslTable = mutableMapOf<Long, AndroidSslState>()
private val errorQueue = ArrayDeque<ULong>()

private data class NidEntry(
    val nid: Int,
    val sn: String,
    val ln: String,
    val oid: String,
)

private val knownNids =
    listOf(
        NidEntry(NID_UNDEF, "UNDEF", "undefined", "0.0"),
        NidEntry(NID_SHA1, "SHA1", "sha1", "1.3.14.3.2.26"),
        NidEntry(NID_SHA224, "SHA224", "sha224", "2.16.840.1.101.3.4.2.4"),
        NidEntry(NID_SHA256, "SHA256", "sha256", "2.16.840.1.101.3.4.2.1"),
        NidEntry(NID_SHA384, "SHA384", "sha384", "2.16.840.1.101.3.4.2.2"),
        NidEntry(NID_SHA512, "SHA512", "SHA512", "2.16.840.1.101.3.4.2.3"),
        NidEntry(NID_X9_62_PRIME256V1, "prime256v1", "X9.62 prime256v1", "1.2.840.10045.3.1.7"),
        NidEntry(NID_SECP384R1, "secp384r1", "secp384r1", "1.3.132.0.34"),
        NidEntry(NID_SECP521R1, "secp521r1", "secp521r1", "1.3.132.0.35"),
        NidEntry(NID_ED25519, "ED25519", "ED25519", "1.3.101.112"),
    )

public actual fun openSslVersionNum(): ULong = 0x30000000uL

public actual fun openSslVersion(key: Int): String? =
    when (key) {
        OPENSSL_VERSION -> "OpenSSL 3.0.0 (Android)"
        OPENSSL_CFLAGS -> "compiler: android"
        OPENSSL_BUILT_ON -> "built on: Android"
        OPENSSL_PLATFORM -> "platform: android"
        OPENSSL_DIR -> "OPENSSLDIR: \"/system/etc/security\""
        OPENSSL_ENGINES_DIR -> "ENGINESDIR: \"/system/lib64/engines-3\""
        OPENSSL_VERSION_STRING -> "3.0.0"
        OPENSSL_FULL_VERSION_STRING -> "OpenSSL 3.0.0 (Android)"
        OPENSSL_MODULES_DIR -> "MODULESDIR: \"/system/lib64/ossl-modules\""
        OPENSSL_CPU_INFO -> "CPUINFO: android"
        else -> null
    }

public actual fun opensslInitCrypto(
    opts: ULong,
    settings: OpensslInitSettings?,
): Int = 1

public actual fun cryptoMalloc(
    num: ULong,
    file: String,
    line: Int,
): Long {
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
    allocatedBlocks.remove(addr)
}

public actual fun sha1(data: ByteArray): ByteArray? = Sha1.digest(data)

public actual fun sha224(data: ByteArray): ByteArray? = Sha224.digest(data)

public actual fun sha256(data: ByteArray): ByteArray? = Sha256.digest(data)

public actual fun sha384(data: ByteArray): ByteArray? = Sha384.digest(data)

public actual fun sha512(data: ByteArray): ByteArray? = Sha512.digest(data)

public actual fun bioSMem(): BioMethod? = BioMethod(BIO_S_MEM_HANDLE)

public actual fun bioNew(type: BioMethod): Bio? {
    if (type.address != BIO_S_MEM_HANDLE) return null
    val handle = nextHandleId++
    bioTable[handle] = AndroidBioState()
    return Bio(handle)
}

public actual fun bioFreeAll(b: Bio?) {
    if (b != null) {
        bioTable.remove(b.address)
    }
}

public actual fun bioRead(
    b: Bio,
    buf: ByteArray,
    len: Int,
): Int {
    val state = bioTable[b.address] ?: return -1
    val actualLen = len.coerceIn(0, buf.size)
    if (actualLen == 0) return 0
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
    val state = bioTable[b.address] ?: return -1
    val actualLen = len.coerceIn(0, buf.size)
    if (actualLen == 0) return 0
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
    val state = bioTable[b.address] ?: return ByteArray(0)
    val avail = state.pending
    return if (avail <= 0) {
        ByteArray(0)
    } else {
        state.data.copyOfRange(state.readPos, state.data.size)
    }
}

public actual fun evpSha1(): EvpMd? = EvpMd(EVP_MD_SHA1_HANDLE)

public actual fun evpSha224(): EvpMd? = EvpMd(EVP_MD_SHA224_HANDLE)

public actual fun evpSha256(): EvpMd? = EvpMd(EVP_MD_SHA256_HANDLE)

public actual fun evpSha384(): EvpMd? = EvpMd(EVP_MD_SHA384_HANDLE)

public actual fun evpSha512(): EvpMd? = EvpMd(EVP_MD_SHA512_HANDLE)

public actual fun evpGetDigestbyname(name: String): EvpMd? =
    when (name.uppercase().replace("-", "")) {
        "SHA1" -> evpSha1()
        "SHA224" -> evpSha224()
        "SHA256" -> evpSha256()
        "SHA384" -> evpSha384()
        "SHA512" -> evpSha512()
        else -> null
    }

public actual fun evpMdGetSize(md: EvpMd): Int =
    when (md.address) {
        EVP_MD_SHA1_HANDLE -> SHA_DIGEST_LENGTH
        EVP_MD_SHA224_HANDLE -> SHA224_DIGEST_LENGTH
        EVP_MD_SHA256_HANDLE -> SHA256_DIGEST_LENGTH
        EVP_MD_SHA384_HANDLE -> SHA384_DIGEST_LENGTH
        EVP_MD_SHA512_HANDLE -> SHA512_DIGEST_LENGTH
        else -> 0
    }

public actual fun evpMdCtxNew(): EvpMdCtx? {
    val handle = nextHandleId++
    evpCtxTable[handle] = AndroidEvpMdCtxState()
    return EvpMdCtx(handle)
}

public actual fun evpMdCtxFree(ctx: EvpMdCtx?) {
    if (ctx != null) {
        evpCtxTable.remove(ctx.address)
    }
}

public actual fun evpDigestInitEx(
    ctx: EvpMdCtx,
    type: EvpMd,
    impl: Engine?,
): Int {
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
): Int = 1

public actual fun tlsMethod(): SslMethod? = SslMethod(TLS_METHOD_HANDLE)

public actual fun tlsClientMethod(): SslMethod? = SslMethod(TLS_CLIENT_METHOD_HANDLE)

public actual fun tlsServerMethod(): SslMethod? = SslMethod(TLS_SERVER_METHOD_HANDLE)

public actual fun sslCtxNew(method: SslMethod): SslCtx? {
    if (method.address == 0L) return null
    val handle = nextHandleId++
    sslCtxTable[handle] = AndroidSslCtxState(method.address)
    return SslCtx(handle)
}

public actual fun sslCtxFree(ctx: SslCtx?) {
    if (ctx != null) {
        sslCtxTable.remove(ctx.address)
    }
}

public actual fun sslCtxCtrl(
    ctx: SslCtx,
    cmd: Int,
    larg: Long,
    parg: Long,
): Long {
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

public actual fun sslCtxGetOptions(ctx: SslCtx): ULong = sslCtxTable[ctx.address]?.options ?: 0uL

public actual fun sslCtxSetOptions(
    ctx: SslCtx,
    op: ULong,
): ULong {
    val state = sslCtxTable[ctx.address] ?: return 0uL
    state.options = state.options or op
    return state.options
}

public actual fun sslCtxClearOptions(
    ctx: SslCtx,
    op: ULong,
): ULong {
    val state = sslCtxTable[ctx.address] ?: return 0uL
    state.options = state.options and op.inv()
    return state.options
}

public actual fun sslNew(ctx: SslCtx): Ssl? {
    val ctxState = sslCtxTable[ctx.address] ?: return null
    val handle = nextHandleId++
    val sslState = AndroidSslState(ctx.address)
    sslState.options = ctxState.options
    sslState.mode = ctxState.mode
    sslState.minProtoVersion = ctxState.minProtoVersion
    sslState.maxProtoVersion = ctxState.maxProtoVersion
    sslTable[handle] = sslState
    return Ssl(handle)
}

public actual fun sslFree(ssl: Ssl?) {
    if (ssl != null) {
        sslTable.remove(ssl.address)
    }
}

public actual fun sslCtrl(
    ssl: Ssl,
    cmd: Int,
    larg: Long,
    parg: Long,
): Long {
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

public actual fun errGetError(): ULong = if (errorQueue.isEmpty()) 0uL else errorQueue.removeFirst()

public actual fun errPeekError(): ULong = errorQueue.firstOrNull() ?: 0uL

public actual fun errPeekLastError(): ULong = errorQueue.lastOrNull() ?: 0uL

public actual fun errClearError() {
    errorQueue.clear()
}

public actual fun errErrorStringN(
    e: ULong,
    maxLen: Int,
): String {
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
    val tmp = ByteArray(actual)
    return if (getrandom(tmp).isOk) {
        tmp.copyInto(buf, 0, 0, actual)
        1
    } else {
        0
    }
}

public actual fun randStatus(): Int = 1

public actual fun objNid2sn(n: Int): String? = knownNids.firstOrNull { it.nid == n }?.sn

public actual fun objNid2ln(n: Int): String? = knownNids.firstOrNull { it.nid == n }?.ln

public actual fun objSn2nid(sn: String): Int =
    knownNids.firstOrNull { it.sn.equals(sn, ignoreCase = true) }?.nid ?: NID_UNDEF

public actual fun objLn2nid(ln: String): Int =
    knownNids.firstOrNull { it.ln.equals(ln, ignoreCase = true) }?.nid ?: NID_UNDEF

public actual fun objTxt2nid(s: String): Int =
    knownNids
        .firstOrNull {
            it.sn.equals(s, ignoreCase = true) ||
                it.ln.equals(s, ignoreCase = true) ||
                it.oid == s
        }?.nid ?: NID_UNDEF
