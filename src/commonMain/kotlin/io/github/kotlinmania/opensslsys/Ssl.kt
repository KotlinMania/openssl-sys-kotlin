// port-lint: source ssl.rs
package io.github.kotlinmania.opensslsys

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
public const val SSL_OP_NO_SSLv2: ULong = 0x00000000uL
public const val SSL_OP_NO_SSLv3: ULong = 0x02000000uL
public const val SSL_OP_NO_TLSv1: ULong = 0x04000000uL
public const val SSL_OP_NO_TLSv1_2: ULong = 0x08000000uL
public const val SSL_OP_NO_TLSv1_1: ULong = 0x10000000uL
public const val SSL_OP_NO_TLSv1_3: ULong = 0x20000000uL
public const val SSL_OP_NO_DTLSv1: ULong = 0x04000000uL
public const val SSL_OP_NO_DTLSv1_2: ULong = 0x08000000uL
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

public const val SSL_CLIENT_HELLO_SUCCESS: Int = 1
public const val SSL_CLIENT_HELLO_ERROR: Int = 0
public const val SSL_CLIENT_HELLO_RETRY: Int = -1

public const val SSL_READ_EARLY_DATA_ERROR: Int = 0
public const val SSL_READ_EARLY_DATA_SUCCESS: Int = 1
public const val SSL_READ_EARLY_DATA_FINISH: Int = 2
