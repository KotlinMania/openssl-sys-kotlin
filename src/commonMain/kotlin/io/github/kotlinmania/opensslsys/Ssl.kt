// port-lint: source ssl.rs
package io.github.kotlinmania.opensslsys

public const val SSL_MAX_KRB5_PRINCIPAL_LENGTH: Int = 256
public const val SSL_MAX_SSL_SESSION_ID_LENGTH: Int = 32
public const val SSL_MAX_SID_CTX_LENGTH: Int = 32
public const val SSL_MAX_KEY_ARG_LENGTH: Int = 8
public const val SSL_MAX_MASTER_KEY_LENGTH: Int = 48

public const val SSL_SENT_SHUTDOWN: Int = 1
public const val SSL_RECEIVED_SHUTDOWN: Int = 2

public const val SSL_FILETYPE_PEM: Int = X509_FILETYPE_PEM
public const val SSL_FILETYPE_ASN1: Int = X509_FILETYPE_ASN1

public const val SSL_EXT_TLS_ONLY: UInt = 0x0001u
public const val SSL_EXT_DTLS_ONLY: UInt = 0x0002u
public const val SSL_EXT_TLS_IMPLEMENTATION_ONLY: UInt = 0x0004u
public const val SSL_EXT_SSL3_ALLOWED: UInt = 0x0008u
public const val SSL_EXT_TLS1_2_AND_BELOW_ONLY: UInt = 0x0010u
public const val SSL_EXT_TLS1_3_ONLY: UInt = 0x0020u
public const val SSL_EXT_IGNORE_ON_RESUMPTION: UInt = 0x0040u
public const val SSL_EXT_CLIENT_HELLO: UInt = 0x0080u
public const val SSL_EXT_TLS1_2_SERVER_HELLO: UInt = 0x0100u
public const val SSL_EXT_TLS1_3_SERVER_HELLO: UInt = 0x0200u
public const val SSL_EXT_TLS1_3_ENCRYPTED_EXTENSIONS: UInt = 0x0400u
public const val SSL_EXT_TLS1_3_HELLO_RETRY_REQUEST: UInt = 0x0800u
public const val SSL_EXT_TLS1_3_CERTIFICATE: UInt = 0x1000u
public const val SSL_EXT_TLS1_3_NEW_SESSION_TICKET: UInt = 0x2000u
public const val SSL_EXT_TLS1_3_CERTIFICATE_REQUEST: UInt = 0x4000u

public const val SSL_OP_LEGACY_SERVER_CONNECT: ULong = 0x00000004uL
public const val SSL_OP_TLSEXT_PADDING: ULong = 0x10uL
public const val SSL_OP_SAFARI_ECDHE_ECDSA_BUG: ULong = 0x00000040uL
public const val SSL_OP_DONT_INSERT_EMPTY_FRAGMENTS: ULong = 0x00000800uL
public const val SSL_OP_NO_QUERY_MTU: ULong = 0x00001000uL
public const val SSL_OP_COOKIE_EXCHANGE: ULong = 0x00002000uL
public const val SSL_OP_NO_TICKET: ULong = 0x00004000uL
public const val SSL_OP_CISCO_ANYCONNECT: ULong = 0x00008000uL
public const val SSL_OP_NO_SESSION_RESUMPTION_ON_RENEGOTIATION: ULong = 0x00010000uL
public const val SSL_OP_NO_COMPRESSION: ULong = 0x00020000uL
public const val SSL_OP_ALLOW_UNSAFE_LEGACY_RENEGOTIATION: ULong = 0x00040000uL
public const val SSL_OP_ENABLE_MIDDLEBOX_COMPAT: ULong = 0x00100000uL
public const val SSL_OP_PRIORITIZE_CHACHA: ULong = 0x00200000uL
public const val SSL_OP_CIPHER_SERVER_PREFERENCE: ULong = 0x00400000uL
public const val SSL_OP_TLS_ROLLBACK_BUG: ULong = 0x00800000uL
public const val SSL_OP_NO_SSLV2: ULong = 0x00000000uL
public const val SSL_OP_NO_SSLV3: ULong = 0x02000000uL
public const val SSL_OP_NO_TLSV1: ULong = 0x04000000uL
public const val SSL_OP_NO_TLSV1_2: ULong = 0x08000000uL
public const val SSL_OP_NO_TLSV1_1: ULong = 0x10000000uL
public const val SSL_OP_NO_TLSV1_3: ULong = 0x20000000uL
public const val SSL_OP_NO_DTLSV1: ULong = 0x04000000uL
public const val SSL_OP_NO_DTLSV1_2: ULong = 0x08000000uL
public const val SSL_OP_NO_RENEGOTIATION: ULong = 0x40000000uL
public const val SSL_OP_CRYPTOPRO_TLSEXT_BUG: ULong = 0x80000000uL
public const val SSL_OP_ALL: ULong = 0x80000BFFuL

public const val SSL_MODE_ENABLE_PARTIAL_WRITE: Long = 0x1L
public const val SSL_MODE_ACCEPT_MOVING_WRITE_BUFFER: Long = 0x2L
public const val SSL_MODE_AUTO_RETRY: Long = 0x4L
public const val SSL_MODE_NO_AUTO_CHAIN: Long = 0x8L
public const val SSL_MODE_RELEASE_BUFFERS: Long = 0x10L
public const val SSL_MODE_SEND_CLIENTHELLO_TIME: Long = 0x20L
public const val SSL_MODE_SEND_SERVERHELLO_TIME: Long = 0x40L
public const val SSL_MODE_SEND_FALLBACK_SCSV: Long = 0x80L

public const val SSL_COOKIE_LENGTH: Int = 4096

public const val SSL_SESS_CACHE_OFF: Long = 0x0L
public const val SSL_SESS_CACHE_CLIENT: Long = 0x1L
public const val SSL_SESS_CACHE_SERVER: Long = 0x2L
public val SSL_SESS_CACHE_BOTH: Long = SSL_SESS_CACHE_CLIENT or SSL_SESS_CACHE_SERVER
public const val SSL_SESS_CACHE_NO_AUTO_CLEAR: Long = 0x80L
public const val SSL_SESS_CACHE_NO_INTERNAL_LOOKUP: Long = 0x100L
public const val SSL_SESS_CACHE_NO_INTERNAL_STORE: Long = 0x200L
public val SSL_SESS_CACHE_NO_INTERNAL: Long = SSL_SESS_CACHE_NO_INTERNAL_LOOKUP or SSL_SESS_CACHE_NO_INTERNAL_STORE

public const val OPENSSL_NPN_UNSUPPORTED: Int = 0
public const val OPENSSL_NPN_NEGOTIATED: Int = 1
public const val OPENSSL_NPN_NO_OVERLAP: Int = 2

public const val SSL_AD_ILLEGAL_PARAMETER: Int = SSL3_AD_ILLEGAL_PARAMETER
public const val SSL_AD_DECODE_ERROR: Int = TLS1_AD_DECODE_ERROR
public const val SSL_AD_UNRECOGNIZED_NAME: Int = TLS1_AD_UNRECOGNIZED_NAME

public const val SSL_ERROR_NONE: Int = 0
public const val SSL_ERROR_SSL: Int = 1
public const val SSL_ERROR_WANT_READ: Int = 2
public const val SSL_ERROR_WANT_WRITE: Int = 3
public const val SSL_ERROR_WANT_X509_LOOKUP: Int = 4
public const val SSL_ERROR_SYSCALL: Int = 5
public const val SSL_ERROR_ZERO_RETURN: Int = 6
public const val SSL_ERROR_WANT_CONNECT: Int = 7
public const val SSL_ERROR_WANT_ACCEPT: Int = 8
public const val SSL_ERROR_WANT_ASYNC: Int = 9
public const val SSL_ERROR_WANT_ASYNC_JOB: Int = 10
public const val SSL_ERROR_WANT_CLIENT_HELLO_CB: Int = 11

public const val SSL_VERIFY_NONE: Int = 0
public const val SSL_VERIFY_PEER: Int = 1
public const val SSL_VERIFY_FAIL_IF_NO_PEER_CERT: Int = 2
public const val SSL_VERIFY_CLIENT_ONCE: Int = 4
public const val SSL_VERIFY_POST_HANDSHAKE: Int = 8

public const val SSL_CTRL_SET_TMP_DH: Int = 3
public const val SSL_CTRL_SET_TMP_ECDH: Int = 4
public const val SSL_CTRL_GET_SESSION_REUSED: Int = 8
public const val SSL_CTRL_EXTRA_CHAIN_CERT: Int = 14
public const val SSL_CTRL_SET_MTU: Int = 17
public const val SSL_CTRL_OPTIONS: Int = 32
public const val SSL_CTRL_MODE: Int = 33
public const val SSL_CTRL_SET_READ_AHEAD: Int = 41
public const val SSL_CTRL_SET_SESS_CACHE_SIZE: Int = 42
public const val SSL_CTRL_GET_SESS_CACHE_SIZE: Int = 43
public const val SSL_CTRL_SET_SESS_CACHE_MODE: Int = 44
public const val SSL_CTRL_SET_TLSEXT_SERVERNAME_CB: Int = 53
public const val SSL_CTRL_SET_TLSEXT_SERVERNAME_ARG: Int = 54
public const val SSL_CTRL_SET_TLSEXT_HOSTNAME: Int = 55
public const val SSL_CTRL_SET_TLSEXT_STATUS_REQ_CB: Int = 63
public const val SSL_CTRL_SET_TLSEXT_STATUS_REQ_CB_ARG: Int = 64
public const val SSL_CTRL_SET_TLSEXT_STATUS_REQ_TYPE: Int = 65
public const val SSL_CTRL_GET_TLSEXT_STATUS_REQ_OCSP_RESP: Int = 70
public const val SSL_CTRL_SET_TLSEXT_STATUS_REQ_OCSP_RESP: Int = 71
public const val SSL_CTRL_CLEAR_OPTIONS: Int = 77
public const val SSL_CTRL_GET_EXTRA_CHAIN_CERTS: Int = 82
public const val SSL_CTRL_CHAIN_CERT: Int = 89
public const val SSL_CTRL_SET_GROUPS_LIST: Int = 92
public const val SSL_CTRL_SET_ECDH_AUTO: Int = 94
public const val SSL_CTRL_SET_SIGALGS_LIST: Int = 98
public const val SSL_CTRL_SET_VERIFY_CERT_STORE: Int = 106
public const val SSL_CTRL_GET_PEER_TMP_KEY: Int = 109
public const val SSL_CTRL_SET_DH_AUTO: Int = 118
public const val SSL_CTRL_GET_EXTMS_SUPPORT: Int = 122
public const val SSL_CTRL_SET_MIN_PROTO_VERSION: Int = 123
public const val SSL_CTRL_SET_MAX_PROTO_VERSION: Int = 124
public const val SSL_CTRL_GET_MIN_PROTO_VERSION: Int = 130
public const val SSL_CTRL_GET_MAX_PROTO_VERSION: Int = 131
public const val SSL_CTRL_GET_TMP_KEY: Int = 133

public fun sslCtxSetTmpDh(
    ctx: SslCtx,
    dh: Dh,
): Long = sslCtxCtrl(ctx, SSL_CTRL_SET_TMP_DH, 0L, dh.address)

public fun sslCtxSetTmpEcdh(
    ctx: SslCtx,
    key: EcKey,
): Long = sslCtxCtrl(ctx, SSL_CTRL_SET_TMP_ECDH, 0L, key.address)

public fun sslSetTmpDh(
    ssl: Ssl,
    dh: Dh,
): Long = sslCtrl(ssl, SSL_CTRL_SET_TMP_DH, 0L, dh.address)

public fun sslSetTmpEcdh(
    ssl: Ssl,
    key: EcKey,
): Long = sslCtrl(ssl, SSL_CTRL_SET_TMP_ECDH, 0L, key.address)

public fun sslCtxSetDhAuto(
    ctx: SslCtx,
    onoff: Int,
): Long = sslCtxCtrl(ctx, SSL_CTRL_SET_DH_AUTO, onoff.toLong(), 0L)

public fun sslSetDhAuto(
    ssl: Ssl,
    onoff: Int,
): Long = sslCtrl(ssl, SSL_CTRL_SET_DH_AUTO, onoff.toLong(), 0L)

public fun sslCtxAddExtraChainCert(
    ctx: SslCtx,
    x509: X509,
): Long = sslCtxCtrl(ctx, SSL_CTRL_EXTRA_CHAIN_CERT, 0L, x509.address)

public fun sslCtxGetExtraChainCerts(
    ctx: SslCtx,
    chain: Long,
): Long = sslCtxCtrl(ctx, SSL_CTRL_GET_EXTRA_CHAIN_CERTS, 0L, chain)

public fun sslCtxSet0VerifyCertStore(
    ctx: SslCtx,
    st: X509Store,
): Long = sslCtxCtrl(ctx, SSL_CTRL_SET_VERIFY_CERT_STORE, 0L, st.address)

public fun sslSet0VerifyCertStore(
    ssl: Ssl,
    st: X509Store,
): Long = sslCtrl(ssl, SSL_CTRL_SET_VERIFY_CERT_STORE, 0L, st.address)

public fun sslSet1GroupsList(
    ctx: Ssl,
    s: String,
): Long = sslCtrl(ctx, SSL_CTRL_SET_GROUPS_LIST, 0L, s.hashCode().toLong())

public fun sslCtxSet1GroupsList(
    ctx: SslCtx,
    s: String,
): Long = sslCtxCtrl(ctx, SSL_CTRL_SET_GROUPS_LIST, 0L, s.hashCode().toLong())

public fun sslAdd0ChainCert(
    ssl: Ssl,
    ptr: X509,
): Long = sslCtrl(ssl, SSL_CTRL_CHAIN_CERT, 0L, ptr.address)

public fun sslCtxSet1SigalgsList(
    ctx: SslCtx,
    s: String,
): Long = sslCtxCtrl(ctx, SSL_CTRL_SET_SIGALGS_LIST, 0L, s.hashCode().toLong())

public fun sslCtxSetEcdhAuto(
    ctx: SslCtx,
    onoff: Int,
): Int = sslCtxCtrl(ctx, SSL_CTRL_SET_ECDH_AUTO, onoff.toLong(), 0L).toInt()

public fun sslSetEcdhAuto(
    ssl: Ssl,
    onoff: Int,
): Int = sslCtrl(ssl, SSL_CTRL_SET_ECDH_AUTO, onoff.toLong(), 0L).toInt()

public fun sslGetPeerTmpKey(
    ssl: Ssl,
    key: Long,
): Long = sslCtrl(ssl, SSL_CTRL_GET_PEER_TMP_KEY, 0L, key)

public fun sslGetTmpKey(
    ssl: Ssl,
    key: Long,
): Long = sslCtrl(ssl, SSL_CTRL_GET_TMP_KEY, 0L, key)

public fun sslGetExNewIndex(
    l: Long,
    p: Long = 0L,
    newf: OpensslCryptoExNew? = null,
    dupf: OpensslCryptoExDup? = null,
    freef: OpensslCryptoExFree? = null,
): Int = if (l >= 0L && p >= 0L && (newf == null || dupf == null || freef == null)) CRYPTO_EX_INDEX_SSL else 0

public fun sslCtxGetExNewIndex(
    l: Long,
    p: Long = 0L,
    newf: OpensslCryptoExNew? = null,
    dupf: OpensslCryptoExDup? = null,
    freef: OpensslCryptoExFree? = null,
): Int = if (l >= 0L && p >= 0L && (newf == null || dupf == null || freef == null)) CRYPTO_EX_INDEX_SSL_CTX else 1

public const val SSL_CLIENT_HELLO_SUCCESS: Int = 1
public const val SSL_CLIENT_HELLO_ERROR: Int = 0
public const val SSL_CLIENT_HELLO_RETRY: Int = -1

public const val SSL_READ_EARLY_DATA_ERROR: Int = 0
public const val SSL_READ_EARLY_DATA_SUCCESS: Int = 1
public const val SSL_READ_EARLY_DATA_FINISH: Int = 2

public const val OPENSSL_INIT_LOAD_CRYPTO_STRINGS: ULong = 0x00000002uL
public const val OPENSSL_INIT_ADD_ALL_CIPHERS: ULong = 0x00000004uL
public const val OPENSSL_INIT_ADD_ALL_DIGESTS: ULong = 0x00000008uL
public const val OPENSSL_INIT_NO_ATEXIT: ULong = 0x00080000uL
public const val OPENSSL_INIT_LOAD_SSL_STRINGS: ULong = 0x00200000uL

public expect fun opensslInitSsl(
    opts: ULong,
    settings: OpensslInitSettings? = null,
): Int

public expect fun tlsMethod(): SslMethod?

public expect fun tlsClientMethod(): SslMethod?

public expect fun tlsServerMethod(): SslMethod?

public expect fun sslCtxNew(method: SslMethod): SslCtx?

public expect fun sslCtxFree(ctx: SslCtx?)

public expect fun sslCtxCtrl(
    ctx: SslCtx,
    cmd: Int,
    larg: Long,
    parg: Long = 0L,
): Long

public expect fun sslCtxGetOptions(ctx: SslCtx): ULong

public expect fun sslCtxSetOptions(
    ctx: SslCtx,
    op: ULong,
): ULong

public expect fun sslCtxClearOptions(
    ctx: SslCtx,
    op: ULong,
): ULong

public expect fun sslNew(ctx: SslCtx): Ssl?

public expect fun sslFree(ssl: Ssl?)

public expect fun sslCtrl(
    ssl: Ssl,
    cmd: Int,
    larg: Long,
    parg: Long = 0L,
): Long

public fun sslCtxSetMode(
    ctx: SslCtx,
    op: Long,
): Long = sslCtxCtrl(ctx, SSL_CTRL_MODE, op, 0L)

public fun sslCtxGetMode(ctx: SslCtx): Long = sslCtxCtrl(ctx, SSL_CTRL_MODE, 0L, 0L)

public fun sslCtxSetMinProtoVersion(
    ctx: SslCtx,
    version: Int,
): Int = sslCtxCtrl(ctx, SSL_CTRL_SET_MIN_PROTO_VERSION, version.toLong(), 0L).toInt()

public fun sslCtxSetMaxProtoVersion(
    ctx: SslCtx,
    version: Int,
): Int = sslCtxCtrl(ctx, SSL_CTRL_SET_MAX_PROTO_VERSION, version.toLong(), 0L).toInt()

public fun sslCtxGetMinProtoVersion(ctx: SslCtx): Int =
    sslCtxCtrl(ctx, SSL_CTRL_GET_MIN_PROTO_VERSION, 0L, 0L).toInt()

public fun sslCtxGetMaxProtoVersion(ctx: SslCtx): Int =
    sslCtxCtrl(ctx, SSL_CTRL_GET_MAX_PROTO_VERSION, 0L, 0L).toInt()

public fun sslSetMinProtoVersion(
    s: Ssl,
    version: Int,
): Int = sslCtrl(s, SSL_CTRL_SET_MIN_PROTO_VERSION, version.toLong(), 0L).toInt()

public fun sslSetMaxProtoVersion(
    s: Ssl,
    version: Int,
): Int = sslCtrl(s, SSL_CTRL_SET_MAX_PROTO_VERSION, version.toLong(), 0L).toInt()

public fun sslGetMinProtoVersion(s: Ssl): Int =
    sslCtrl(s, SSL_CTRL_GET_MIN_PROTO_VERSION, 0L, 0L).toInt()

public fun sslGetMaxProtoVersion(s: Ssl): Int =
    sslCtrl(s, SSL_CTRL_GET_MAX_PROTO_VERSION, 0L, 0L).toInt()

public fun sslSessionReused(ssl: Ssl): Int =
    sslCtrl(ssl, SSL_CTRL_GET_SESSION_REUSED, 0L, 0L).toInt()

public fun sslCtxSessSetCacheSize(
    ctx: SslCtx,
    t: Long,
): Long = sslCtxCtrl(ctx, SSL_CTRL_SET_SESS_CACHE_SIZE, t, 0L)

public fun sslCtxSessGetCacheSize(ctx: SslCtx): Long =
    sslCtxCtrl(ctx, SSL_CTRL_GET_SESS_CACHE_SIZE, 0L, 0L)

public fun sslCtxSetSessionCacheMode(
    ctx: SslCtx,
    m: Long,
): Long = sslCtxCtrl(ctx, SSL_CTRL_SET_SESS_CACHE_MODE, m, 0L)

public fun sslCtxSetReadAhead(
    ctx: SslCtx,
    m: Long,
): Long = sslCtxCtrl(ctx, SSL_CTRL_SET_READ_AHEAD, m, 0L)

public fun sslSetMtu(
    ssl: Ssl,
    mtu: Long,
): Long = sslCtrl(ssl, SSL_CTRL_SET_MTU, mtu, 0L)

public fun sslGetExtmsSupport(ssl: Ssl): Long =
    sslCtrl(ssl, SSL_CTRL_GET_EXTMS_SUPPORT, 0L, 0L)
