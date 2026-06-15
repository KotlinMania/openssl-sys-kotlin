# OpenSSL Sys Kotlin ABI Parity Report

Generated: 2026-06-15

## Verdict

This repository is not currently a drop-in replacement for Rust `openssl-sys` on the native path. It has a useful Kotlin implementation/scaffold for a small constant/hash subset, but it does not yet expose the Rust crate's FFI contract, OpenSSL discovery/link contract, or Node.js/Wasm bridge contract.

A Rust consumer could not replace `openssl-sys` with this Kotlin artifact today with only minor wrappers. The missing layer is not cosmetic: the pointer-level ABI declarations, opaque OpenSSL types, generated constants, cfg/version gates, OpenSSL library discovery, and N-API bridge are absent or mostly absent.

## Measurements

### ast_distance deep analysis

- Rust source scanned: 68 files from `tmp/openssl-sys/src`.
- Kotlin target scanned: 6 files from `src/commonMain/kotlin/io/github/kotlinmania/opensslsys`.
- Matched files: 5.
- Missing Rust source files in Kotlin target: 63.
- Exact provenance headers after cleanup: 5/5 matched; fallback provenance: 0/5.
- `Sha.kt` is forced to zero similarity because upstream `sha.rs` exposes FFI-style functions while Kotlin defines helper digest functions.

### Symbol parity from ast_distance

- Production definitions: 0/157 matched, 0.0%.
- Supplementary symbols: 51/1485 matched, 3.4%.
- Test definitions: 0/0.
- Kotlin-only extras: 24 real declarations, 0 stubs.

### Convention-normalized ABI scan

This scan allows Kotlin naming conventions: Rust `SHA_LONG` may map to Kotlin `ShaLong`, and Rust `SHA1` may map to Kotlin `sha1` by name. Unlike ast_distance, it is a broad regex pass over `tmp/openssl-sys/src` and includes public extern declarations because those matter for ABI replacement.

| Kind | Rust count | Kotlin commonMain count | Exact matches | Convention-name matches | Missing |
| --- | ---: | ---: | ---: | ---: | ---: |
| Functions | 1538 | 5 | 0 | 10 | 1528 |
| Constants | 1610 | 51 | 51 | 0 | 1559 |
| Type aliases | 21 | 2 | 0 | 2 | 19 |
| Structs | 63 | 0 | 0 | 0 | 63 |
| Enums / opaque types | 104 | 0 | 0 | 0 | 104 |

Function names that convention-normalize are still not ABI-compatible: 0/1538 matched functions have pointer/native-looking Kotlin signatures. The SHA helpers match by conventional name only; they allocate/return `ByteArray?` and do not expose the Rust/OpenSSL pointer contract.

## Current Kotlin Surface

The current meaningful source surface is:

- `src/commonMain/kotlin/io/github/kotlinmania/opensslsys/Aes.kt`: four AES constants.
- `src/commonMain/kotlin/io/github/kotlinmania/opensslsys/Asn1.kt`: ASN.1/MBSTRING constants.
- `src/commonMain/kotlin/io/github/kotlinmania/opensslsys/CoreDispatch.kt`: five OpenSSL 3 key-management constants.
- `src/commonMain/kotlin/io/github/kotlinmania/opensslsys/Srtp.kt`: eight SRTP constants.
- `src/commonMain/kotlin/io/github/kotlinmania/opensslsys/Sha.kt`: `SHA_LBLOCK`, `ShaLong`, `ShaLong64`, and Kotlin digest helpers.

That surface is useful, but it is not the `openssl-sys` ABI surface.

## Native ABI Gap

Rust `openssl-sys` provides an FFI boundary. Examples from upstream include pointer-based APIs such as `SHA1(d: *const c_uchar, n: size_t, md: *mut c_uchar) -> *mut c_uchar`, `BIO_get_mem_data`, `SSL_CTX_set_mode`, `EVP_PKEY_CTX_set_hkdf_md`, and `OPENSSL_malloc`.

The Kotlin source does not currently define equivalent `expect`/`actual` functions, CInterop bindings, `CPointer`-style signatures, opaque pointer wrappers, or native linker configuration. As a result:

- Rust callers expecting the `openssl-sys` symbol contract would need substantial wrappers, not minor wrappers.
- Kotlin Native does not currently prove it can call the same OpenSSL ABI surface.
- JS/Wasm cannot receive ABI parity through Kotlin alone; the bridge layer must be explicit.

## Build / Link Gap

Upstream Cargo metadata points to `tmp/openssl-sys/build/main.rs`, and the package contains 5 Rust build files under `tmp/openssl-sys/build`. That build logic performs OpenSSL discovery and cfg generation through environment variables, pkg-config, vcpkg, vendored OpenSSL, bindgen, header expansion, version gates, static/dynamic link selection, Windows extra libraries, and OpenSSL/LibreSSL/BoringSSL/AWS-LC detection.

Current Gradle wiring does not contain equivalent discovery/link behavior:

| Capability | Rust build script | Current Gradle |
| --- | --- | --- |
| `OPENSSL_DIR` / prefixed target env | yes | no |
| `OPENSSL_LIBS` | yes | no |
| `OPENSSL_STATIC` | yes | no |
| pkg-config | yes | no |
| vcpkg | yes | no |
| vendored OpenSSL | yes | no |
| bindgen/header expansion | yes | no |
| Kotlin/Native CInterop declarations | n/a | no |
| explicit linker options | cargo-emitted | no |

This is the biggest deliverability gap after missing declarations.

## Node.js / JS / Wasm Gap

I found no tracked N-API/native-addon files outside upstream `tmp/`: no `binding.gyp`, no C/C++ shim, no N-API source, no generated native package entrypoint.

For the goal you described, N-API is the right bridge for Node.js. But it is not currently wired. JS/Wasm targets compile Kotlin code; they do not expose an OpenSSL ABI replacement by themselves. To provide the same ABI experience from JS/Wasm, this repo needs a deliberate bridge layer:

1. Kotlin common API matching the Rust contract by convention.
2. Native actuals backed by Kotlin/Native CInterop or a generated native shim.
3. Node.js actuals through N-API for pointer/handle-like OpenSSL operations.
4. Wasm policy: either unsupported for raw OpenSSL ABI, or backed by a bundled/wasm OpenSSL strategy with explicit memory handles.

## Caller-Driven Priority

`RUST_CALLERS.md` currently reports no direct cross-repo `use openssl_sys::...` imports. That is accurate at import-level granularity, but it does not mean the workspace has no dependency pressure.

A targeted Cargo/Rust scan found `codex-kotlin` depends on `openssl-sys` from `codex-rs/core/Cargo.toml` for `x86_64-unknown-linux-musl` and `aarch64-unknown-linux-musl`, both with `features = ["vendored"]`. The root Codex workspace also marks `openssl-sys` as an intentional cargo-shear ignore because cargo-shear cannot see that platform-specific usage.

That changes the first high-value milestone: before chasing every OpenSSL symbol, the port needs to reproduce the `openssl-sys` build/link contract well enough for Codex musl builds. The minimum caller-driven slice is:

1. Recognize target-specific OpenSSL dependency configuration.
2. Support vendored OpenSSL or an equivalent deterministic musl OpenSSL supply.
3. Emit/encode the link inputs that Rust `openssl-sys` would provide for musl.
4. Preserve enough public ABI shape that downstream wrappers can compile once native linking is wired.

## Highest-Value Port Order

From `port_status_report.md`, the next ABI-relevant files are:

1. `handwritten/types.rs` -> opaque OpenSSL type universe.
2. `ssl.rs` and `handwritten/ssl.rs` -> SSL functions, constants, callbacks, profiles.
3. `evp.rs` and `handwritten/evp.rs` -> digest/key APIs and EVP pointer surfaces.
4. `crypto.rs` and `handwritten/crypto.rs` -> allocation/init/locking/ex-data surfaces.
5. `bio.rs` and `handwritten/bio.rs` -> BIO pointer operations.
6. `obj_mac.rs` -> large NID constant surface.
7. `x509*`, `rsa`, `ec`, `bn`, `pkcs7`, `cms` -> remaining OpenSSL subsystem parity.

## Required Architecture To Deliver

To make this credible as a Rust replacement with minor wrappers:

- Add generated or systematically ported Kotlin declarations for every Rust public symbol, allowing only Kotlin naming conventions.
- Represent opaque OpenSSL structs as stable Kotlin/Native pointer wrappers or typealiases that can round-trip through native code.
- Add Gradle OpenSSL discovery equivalent to Cargo: `OPENSSL_DIR`, target-prefixed env vars, `OPENSSL_LIBS`, `OPENSSL_STATIC`, pkg-config, vcpkg, version/cfg extraction, and platform-specific link libraries.
- Add a cinterop/native shim layer for Kotlin/Native targets.
- Add a Node.js N-API package/shim for JS Node targets; do not pretend plain Kotlin/JS can call OpenSSL ABI directly.
- Define Wasm support explicitly: unsupported raw system OpenSSL ABI, or a separate bundled OpenSSL/WASI strategy.
- Add ABI smoke tests that call real linked OpenSSL functions: version probe, SHA pointer API, OPENSSL_malloc/free, BIO memory buffer, EVP digest lookup, and one SSL context operation.
- Keep the Kotlin convenience helpers if desired, but mark them as helpers layered above the ABI, not as replacements for `openssl-sys` symbols.

## Answer To The Replacement Question

If a Rust project uses a Kotlin-to-Rust bridge and tries to replace Rust `openssl-sys` with this Kotlin artifact today, Rust would not continue with only minor wrappers. The current repo does not yet provide the native ABI declarations or link-time behavior that Rust `openssl-sys` provides.

The path is viable, but the repo is at the early scaffold/subset stage: roughly 7.4% file coverage by ast-distance pairing, 0.0% production definition parity, and 3.4% supplementary symbol parity. The promising part is that the build template now gives us the target matrix to force the issue; the real work is generating/porting the ABI surface and bridge layers.
