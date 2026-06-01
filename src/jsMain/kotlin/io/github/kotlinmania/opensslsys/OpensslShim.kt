// Kotlin/JS external bindings to the Node N-API addon at
// node/build/Release/openssl_shim.node. The addon is loaded lazily on
// first use via require('@openssl-sys-kotlin/openssl-shim'); the jsNodejs
// test target wires the addon directory in as a yarn-resolvable name.
// Browser callers see opensslShimOrThrow() raise UnsupportedOperationException.
package io.github.kotlinmania.opensslsys

import org.khronos.webgl.Uint8Array

internal external interface OpensslShim {
    fun evpQDigest(
        name: String,
        data: Uint8Array,
    ): Uint8Array?
    fun opensslVersionNumber(): Double
}

// Lazy require so loading Kotlin/JS code in a browser bundle doesn't fault
// at module-load time. Both the runtime detection and the require call
// happen on first access — browser bundles can ship this code as long as
// no caller actually invokes an SHA function.
private val shim: OpensslShim? by lazy {
    val resolved: dynamic =
        js(
            """
        (function () {
          if (typeof process === 'undefined' || !process.versions || !process.versions.node) {
            return null;
          }
          try {
            return require('@openssl-sys-kotlin/openssl-shim');
          } catch (e) {
            return null;
          }
        })()
        """,
        )
    if (resolved == null) null else resolved.unsafeCast<OpensslShim>()
}

internal fun opensslShimOrThrow(): OpensslShim =
    shim ?: throw UnsupportedOperationException(
        "openssl-sys-kotlin: the @openssl-sys-kotlin/openssl-shim N-API addon " +
            "is not available in this runtime. Browsers cannot load .node files; " +
            "Node consumers need node/ built (npm install + npm run build:native) " +
            "and the package resolvable as '@openssl-sys-kotlin/openssl-shim'.",
    )

/**
 * Returns the OPENSSL_VERSION_NUMBER reported by the loaded libssl, as a
 * `Long` — e.g. `0x30600020` for OpenSSL 3.6.0. Throws if the N-API addon
 * is not loadable from the current runtime.
 */
public fun opensslVersionNumber(): Long = opensslShimOrThrow().opensslVersionNumber().toLong()
