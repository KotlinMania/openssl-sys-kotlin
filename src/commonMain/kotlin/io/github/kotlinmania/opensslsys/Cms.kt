// port-lint: source openssl-sys/src/cms.rs
package io.github.kotlinmania.opensslsys

public const val CMS_TEXT: UInt = 0x1u
public const val CMS_NOCERTS: UInt = 0x2u
public const val CMS_NO_CONTENT_VERIFY: UInt = 0x4u
public const val CMS_NO_ATTR_VERIFY: UInt = 0x8u
public val CMS_NOSIGS: UInt = CMS_NO_CONTENT_VERIFY or CMS_NO_ATTR_VERIFY
public const val CMS_NOINTERN: UInt = 0x10u
public const val CMS_NO_SIGNER_CERT_VERIFY: UInt = 0x20u
public const val CMS_NOVERIFY: UInt = 0x20u
public const val CMS_DETACHED: UInt = 0x40u
public const val CMS_BINARY: UInt = 0x80u
public const val CMS_NOATTR: UInt = 0x100u
public const val CMS_NOSMIMECAP: UInt = 0x200u
public const val CMS_NOOLDMIMETYPE: UInt = 0x400u
public const val CMS_CRLFEOL: UInt = 0x800u
public const val CMS_STREAM: UInt = 0x1000u
public const val CMS_NOCRL: UInt = 0x2000u
public const val CMS_PARTIAL: UInt = 0x4000u
public const val CMS_REUSE_DIGEST: UInt = 0x8000u
public const val CMS_USE_KEYID: UInt = 0x10000u
public const val CMS_DEBUG_DECRYPT: UInt = 0x20000u
public const val CMS_KEY_PARAM: UInt = 0x40000u
public const val CMS_ASCIICRLF: UInt = 0x80000u
