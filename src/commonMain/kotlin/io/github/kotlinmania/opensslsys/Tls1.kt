// port-lint: source tls1.rs
package io.github.kotlinmania.opensslsys

import io.github.kotlinmania.libc.CInt
import io.github.kotlinmania.libc.CLong

public const val TLS1_VERSION: Int = 0x301
public const val TLS1_1_VERSION: Int = 0x302
public const val TLS1_2_VERSION: Int = 0x303
public const val TLS1_3_VERSION: Int = 0x304

public const val DTLS1_VERSION: Int = 0xFEFF
public const val DTLS1_2_VERSION: Int = 0xFEFD

public const val TLS1_AD_DECODE_ERROR: Int = 50
public const val TLS1_AD_UNRECOGNIZED_NAME: Int = 112

public const val TLSEXT_NAMETYPE_host_name: Int = 0
public const val TLSEXT_STATUSTYPE_ocsp: Int = 1

public fun interface OpensslTlsextServernameCallback {
    public fun invoke(
        ssl: Ssl,
        al: IntArray,
        arg: Long,
    ): CInt
}

public fun interface OpensslTlsextStatusCallback {
    public fun invoke(
        ssl: Ssl,
        arg: Long,
    ): CInt
}

public fun sslSetTlsextHostName(
    s: Ssl,
    name: String,
): CLong = sslCtrl(s, SSL_CTRL_SET_TLSEXT_HOSTNAME, TLSEXT_NAMETYPE_host_name.toLong(), name.hashCode().toLong())

public fun sslSetTlsextStatusType(
    s: Ssl,
    type: CInt,
): CLong = sslCtrl(s, SSL_CTRL_SET_TLSEXT_STATUS_REQ_TYPE, type.toLong(), 0L)

public fun sslGetTlsextStatusOcspResp(
    ssl: Ssl,
    resp: Long,
): CLong = sslCtrl(ssl, SSL_CTRL_GET_TLSEXT_STATUS_REQ_OCSP_RESP, 0L, resp)

public fun sslSetTlsextStatusOcspResp(
    ssl: Ssl,
    resp: Long,
    len: CLong,
): CLong = sslCtrl(ssl, SSL_CTRL_SET_TLSEXT_STATUS_REQ_OCSP_RESP, len, resp)

public fun sslCtxSetTlsextServernameCallback(
    ctx: SslCtx,
    cb: OpensslTlsextServernameCallback? = null,
): CLong = sslCtxCtrl(ctx, SSL_CTRL_SET_TLSEXT_SERVERNAME_CB, 0L, if (cb != null) 1L else 0L)

public fun sslCtxSetTlsextServernameCallbackFixedRust(
    ctx: SslCtx,
    cb: OpensslTlsextServernameCallback? = null,
): CLong = sslCtxCtrl(ctx, SSL_CTRL_SET_TLSEXT_SERVERNAME_CB, 0L, if (cb != null) 1L else 0L)

public const val SSL_TLSEXT_ERR_OK: Int = 0
public const val SSL_TLSEXT_ERR_ALERT_WARNING: Int = 1
public const val SSL_TLSEXT_ERR_ALERT_FATAL: Int = 2
public const val SSL_TLSEXT_ERR_NOACK: Int = 3

public fun sslCtxSetTlsextServernameArg(
    ctx: SslCtx,
    arg: Long,
): CLong = sslCtxCtrl(ctx, SSL_CTRL_SET_TLSEXT_SERVERNAME_ARG, 0L, arg)

public fun sslCtxSetTlsextStatusCb(
    ctx: SslCtx,
    cb: OpensslTlsextStatusCallback? = null,
): CLong = sslCtxCtrl(ctx, SSL_CTRL_SET_TLSEXT_STATUS_REQ_CB, 0L, if (cb != null) 1L else 0L)

public fun sslCtxSetTlsextStatusArg(
    ctx: SslCtx,
    arg: Long,
): CLong = sslCtxCtrl(ctx, SSL_CTRL_SET_TLSEXT_STATUS_REQ_CB_ARG, 0L, arg)
