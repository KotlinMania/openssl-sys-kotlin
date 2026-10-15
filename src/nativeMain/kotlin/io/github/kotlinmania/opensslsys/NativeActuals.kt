// port-lint: source lib.rs crypto.rs sha.rs bio.rs evp.rs ssl.rs err.rs handwritten/rand.rs handwritten/object.rs
@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package io.github.kotlinmania.opensslsys

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.UIntVar
import kotlinx.cinterop.ULongVar
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.alloc
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.toCPointer
import kotlinx.cinterop.toKString
import kotlinx.cinterop.usePinned
import kotlinx.cinterop.value
import openssl.cinterop.openssl_bio_ctrl
import openssl.cinterop.openssl_bio_free_all
import openssl.cinterop.openssl_bio_get_mem_data_ptr
import openssl.cinterop.openssl_bio_new
import openssl.cinterop.openssl_bio_read
import openssl.cinterop.openssl_bio_s_mem
import openssl.cinterop.openssl_bio_write
import openssl.cinterop.openssl_crypto_free
import openssl.cinterop.openssl_crypto_malloc
import openssl.cinterop.openssl_err_clear_error
import openssl.cinterop.openssl_err_error_string_n
import openssl.cinterop.openssl_err_get_error
import openssl.cinterop.openssl_err_peek_error
import openssl.cinterop.openssl_err_peek_last_error
import openssl.cinterop.openssl_evp_digest_final_ex
import openssl.cinterop.openssl_evp_digest_init_ex
import openssl.cinterop.openssl_evp_digest_update
import openssl.cinterop.openssl_evp_get_digestbyname
import openssl.cinterop.openssl_evp_md_ctx_free
import openssl.cinterop.openssl_evp_md_ctx_new
import openssl.cinterop.openssl_evp_md_get_size
import openssl.cinterop.openssl_evp_sha1
import openssl.cinterop.openssl_evp_sha224
import openssl.cinterop.openssl_evp_sha256
import openssl.cinterop.openssl_evp_sha384
import openssl.cinterop.openssl_evp_sha512
import openssl.cinterop.openssl_init_crypto
import openssl.cinterop.openssl_init_ssl
import openssl.cinterop.openssl_obj_ln2nid
import openssl.cinterop.openssl_obj_nid2ln
import openssl.cinterop.openssl_obj_nid2sn
import openssl.cinterop.openssl_obj_sn2nid
import openssl.cinterop.openssl_obj_txt2nid
import openssl.cinterop.openssl_rand_bytes
import openssl.cinterop.openssl_rand_status
import openssl.cinterop.openssl_sha1
import openssl.cinterop.openssl_sha224
import openssl.cinterop.openssl_sha256
import openssl.cinterop.openssl_sha384
import openssl.cinterop.openssl_sha512
import openssl.cinterop.openssl_ssl_ctrl
import openssl.cinterop.openssl_ssl_ctx_clear_options
import openssl.cinterop.openssl_ssl_ctx_ctrl
import openssl.cinterop.openssl_ssl_ctx_free
import openssl.cinterop.openssl_ssl_ctx_get_options
import openssl.cinterop.openssl_ssl_ctx_new
import openssl.cinterop.openssl_ssl_ctx_set_options
import openssl.cinterop.openssl_ssl_free
import openssl.cinterop.openssl_ssl_new
import openssl.cinterop.openssl_tls_client_method
import openssl.cinterop.openssl_tls_method
import openssl.cinterop.openssl_tls_server_method
import openssl.cinterop.openssl_version
import openssl.cinterop.openssl_version_num

private inline fun <R> ByteArray.usePinnedPtr(block: (COpaquePointer?) -> R): R =
    if (isEmpty()) {
        block(null)
    } else {
        usePinned { pinned -> block(pinned.addressOf(0)) }
    }

public actual fun openSslVersionNum(): ULong = openssl_version_num()

public actual fun openSslVersion(key: Int): String? = openssl_version(key)?.toKString()

public actual fun opensslInitCrypto(
    opts: ULong,
    settings: OpensslInitSettings?,
): Int = openssl_init_crypto(opts, (settings?.address ?: 0L).toULong())

public actual fun cryptoMalloc(
    num: ULong,
    file: String,
    line: Int,
): Long = openssl_crypto_malloc(num, file, line).toLong()

public actual fun cryptoFree(
    addr: Long,
    file: String,
    line: Int,
) {
    if (addr != 0L) {
        openssl_crypto_free(addr.toULong(), file, line)
    }
}

private inline fun nativeShaDigest(
    data: ByteArray,
    digestLen: Int,
    call: (COpaquePointer?, ULong, COpaquePointer?) -> Int,
): ByteArray? {
    val out = ByteArray(digestLen)
    val ok =
        data.usePinnedPtr { inPtr ->
            out.usePinnedPtr { outPtr ->
                call(inPtr, data.size.toULong(), outPtr)
            }
        }
    return if (ok == 1) out else null
}

public actual fun sha1(data: ByteArray): ByteArray? =
    nativeShaDigest(data, SHA_DIGEST_LENGTH, ::openssl_sha1)

public actual fun sha224(data: ByteArray): ByteArray? =
    nativeShaDigest(data, SHA224_DIGEST_LENGTH, ::openssl_sha224)

public actual fun sha256(data: ByteArray): ByteArray? =
    nativeShaDigest(data, SHA256_DIGEST_LENGTH, ::openssl_sha256)

public actual fun sha384(data: ByteArray): ByteArray? =
    nativeShaDigest(data, SHA384_DIGEST_LENGTH, ::openssl_sha384)

public actual fun sha512(data: ByteArray): ByteArray? =
    nativeShaDigest(data, SHA512_DIGEST_LENGTH, ::openssl_sha512)

public actual fun bioSMem(): BioMethod? {
    val ptr = openssl_bio_s_mem().toLong()
    return if (ptr != 0L) BioMethod(ptr) else null
}

public actual fun bioNew(type: BioMethod): Bio? {
    val ptr = openssl_bio_new(type.address.toULong()).toLong()
    return if (ptr != 0L) Bio(ptr) else null
}

public actual fun bioFreeAll(b: Bio?) {
    if (b != null && b.address != 0L) {
        openssl_bio_free_all(b.address.toULong())
    }
}

public actual fun bioRead(
    b: Bio,
    buf: ByteArray,
    len: Int,
): Int {
    val actualLen = len.coerceIn(0, buf.size)
    if (actualLen == 0) return 0
    return buf.usePinnedPtr { ptr ->
        openssl_bio_read(b.address.toULong(), ptr, actualLen)
    }
}

public actual fun bioWrite(
    b: Bio,
    buf: ByteArray,
    len: Int,
): Int {
    val actualLen = len.coerceIn(0, buf.size)
    if (actualLen == 0) return 0
    return buf.usePinnedPtr { ptr ->
        openssl_bio_write(b.address.toULong(), ptr, actualLen)
    }
}

public actual fun bioCtrl(
    b: Bio,
    cmd: Int,
    larg: Long,
    parg: Long,
): Long = openssl_bio_ctrl(b.address.toULong(), cmd, larg, parg.toULong())

public actual fun bioGetMemData(b: Bio): ByteArray =
    memScoped {
        val ptrVar = alloc<ULongVar>()
        val len = openssl_bio_get_mem_data_ptr(b.address.toULong(), ptrVar.ptr).toInt()
        val rawAddr = ptrVar.value.toLong()
        if (len <= 0 || rawAddr == 0L) {
            ByteArray(0)
        } else {
            val cptr = rawAddr.toCPointer<ByteVar>() ?: return ByteArray(0)
            cptr.readBytes(len)
        }
    }

public actual fun evpSha1(): EvpMd? {
    val ptr = openssl_evp_sha1().toLong()
    return if (ptr != 0L) EvpMd(ptr) else null
}

public actual fun evpSha224(): EvpMd? {
    val ptr = openssl_evp_sha224().toLong()
    return if (ptr != 0L) EvpMd(ptr) else null
}

public actual fun evpSha256(): EvpMd? {
    val ptr = openssl_evp_sha256().toLong()
    return if (ptr != 0L) EvpMd(ptr) else null
}

public actual fun evpSha384(): EvpMd? {
    val ptr = openssl_evp_sha384().toLong()
    return if (ptr != 0L) EvpMd(ptr) else null
}

public actual fun evpSha512(): EvpMd? {
    val ptr = openssl_evp_sha512().toLong()
    return if (ptr != 0L) EvpMd(ptr) else null
}

public actual fun evpGetDigestbyname(name: String): EvpMd? {
    val ptr = openssl_evp_get_digestbyname(name).toLong()
    return if (ptr != 0L) EvpMd(ptr) else null
}

public actual fun evpMdGetSize(md: EvpMd): Int = openssl_evp_md_get_size(md.address.toULong())

public actual fun evpMdCtxNew(): EvpMdCtx? {
    val ptr = openssl_evp_md_ctx_new().toLong()
    return if (ptr != 0L) EvpMdCtx(ptr) else null
}

public actual fun evpMdCtxFree(ctx: EvpMdCtx?) {
    if (ctx != null && ctx.address != 0L) {
        openssl_evp_md_ctx_free(ctx.address.toULong())
    }
}

public actual fun evpDigestInitEx(
    ctx: EvpMdCtx,
    type: EvpMd,
    impl: Engine?,
): Int = openssl_evp_digest_init_ex(ctx.address.toULong(), type.address.toULong(), (impl?.address ?: 0L).toULong())

public actual fun evpDigestUpdate(
    ctx: EvpMdCtx,
    d: ByteArray,
): Int =
    d.usePinnedPtr { ptr ->
        openssl_evp_digest_update(ctx.address.toULong(), ptr, d.size.toULong())
    }

public actual fun evpDigestFinalEx(
    ctx: EvpMdCtx,
    md: ByteArray,
    s: IntArray?,
): Int =
    memScoped {
        val lenVar = alloc<UIntVar>()
        val rc =
            md.usePinnedPtr { mdPtr ->
                openssl_evp_digest_final_ex(ctx.address.toULong(), mdPtr, lenVar.ptr)
            }
        if (s != null && s.isNotEmpty()) {
            s[0] = lenVar.value.toInt()
        }
        rc
    }

public actual fun opensslInitSsl(
    opts: ULong,
    settings: OpensslInitSettings?,
): Int = openssl_init_ssl(opts, (settings?.address ?: 0L).toULong())

public actual fun tlsMethod(): SslMethod? {
    val ptr = openssl_tls_method().toLong()
    return if (ptr != 0L) SslMethod(ptr) else null
}

public actual fun tlsClientMethod(): SslMethod? {
    val ptr = openssl_tls_client_method().toLong()
    return if (ptr != 0L) SslMethod(ptr) else null
}

public actual fun tlsServerMethod(): SslMethod? {
    val ptr = openssl_tls_server_method().toLong()
    return if (ptr != 0L) SslMethod(ptr) else null
}

public actual fun sslCtxNew(method: SslMethod): SslCtx? {
    val ptr = openssl_ssl_ctx_new(method.address.toULong()).toLong()
    return if (ptr != 0L) SslCtx(ptr) else null
}

public actual fun sslCtxFree(ctx: SslCtx?) {
    if (ctx != null && ctx.address != 0L) {
        openssl_ssl_ctx_free(ctx.address.toULong())
    }
}

public actual fun sslCtxCtrl(
    ctx: SslCtx,
    cmd: Int,
    larg: Long,
    parg: Long,
): Long = openssl_ssl_ctx_ctrl(ctx.address.toULong(), cmd, larg, parg.toULong())

public actual fun sslCtxGetOptions(ctx: SslCtx): ULong = openssl_ssl_ctx_get_options(ctx.address.toULong())

public actual fun sslCtxSetOptions(
    ctx: SslCtx,
    op: ULong,
): ULong = openssl_ssl_ctx_set_options(ctx.address.toULong(), op)

public actual fun sslCtxClearOptions(
    ctx: SslCtx,
    op: ULong,
): ULong = openssl_ssl_ctx_clear_options(ctx.address.toULong(), op)

public actual fun sslNew(ctx: SslCtx): Ssl? {
    val ptr = openssl_ssl_new(ctx.address.toULong()).toLong()
    return if (ptr != 0L) Ssl(ptr) else null
}

public actual fun sslFree(ssl: Ssl?) {
    if (ssl != null && ssl.address != 0L) {
        openssl_ssl_free(ssl.address.toULong())
    }
}

public actual fun sslCtrl(
    ssl: Ssl,
    cmd: Int,
    larg: Long,
    parg: Long,
): Long = openssl_ssl_ctrl(ssl.address.toULong(), cmd, larg, parg.toULong())

public actual fun errGetError(): ULong = openssl_err_get_error()

public actual fun errPeekError(): ULong = openssl_err_peek_error()

public actual fun errPeekLastError(): ULong = openssl_err_peek_last_error()

public actual fun errClearError() {
    openssl_err_clear_error()
}

public actual fun errErrorStringN(
    e: ULong,
    maxLen: Int,
): String =
    memScoped {
        val size = if (maxLen > 0) maxLen else 256
        val buf = allocArray<ByteVar>(size + 1)
        openssl_err_error_string_n(e, buf, size.toULong())
        buf.toKString()
    }

public actual fun randBytes(
    buf: ByteArray,
    num: Int,
): Int {
    val actual = num.coerceIn(0, buf.size)
    if (actual == 0) return 1
    return buf.usePinnedPtr { ptr ->
        openssl_rand_bytes(ptr, actual)
    }
}

public actual fun randStatus(): Int = openssl_rand_status()

public actual fun objNid2sn(n: Int): String? = openssl_obj_nid2sn(n)?.toKString()

public actual fun objNid2ln(n: Int): String? = openssl_obj_nid2ln(n)?.toKString()

public actual fun objSn2nid(sn: String): Int = openssl_obj_sn2nid(sn)

public actual fun objLn2nid(ln: String): Int = openssl_obj_ln2nid(ln)

public actual fun objTxt2nid(s: String): Int = openssl_obj_txt2nid(s)
