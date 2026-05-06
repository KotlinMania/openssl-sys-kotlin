# openssl-sys-kotlin in Kotlin

[![GitHub link](https://img.shields.io/badge/GitHub-KotlinMania%2Fopenssl--sys--kotlin-blue.svg)](https://github.com/KotlinMania/openssl-sys-kotlin)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.kotlinmania/openssl-sys-kotlin)](https://central.sonatype.com/artifact/io.github.kotlinmania/openssl-sys-kotlin)
[![Build status](https://img.shields.io/github/actions/workflow/status/KotlinMania/openssl-sys-kotlin/ci.yml?branch=main)](https://github.com/KotlinMania/openssl-sys-kotlin/actions)

This is a Kotlin Multiplatform line-by-line transliteration port of [`rust-openssl/rust-openssl`](https://github.com/rust-openssl/rust-openssl).

**Original Project:** This port is based on [`rust-openssl/rust-openssl`](https://github.com/rust-openssl/rust-openssl). All design credit and project intent belong to the upstream authors; this repository is a faithful port to Kotlin Multiplatform with no behavioural changes intended.

### Porting status

This is an **in-progress port**. The goal is feature parity with the upstream Rust crate while providing a native Kotlin Multiplatform API. Every Kotlin file carries a `// port-lint: source <path>` header naming its upstream Rust counterpart so the AST-distance tool can track provenance.

---

## Upstream README — `rust-openssl/rust-openssl`

> The text below is reproduced and lightly edited from [`https://github.com/rust-openssl/rust-openssl`](https://github.com/rust-openssl/rust-openssl). It is the upstream project's own description and remains under the upstream authors' authorship; links have been rewritten to absolute upstream URLs so they continue to resolve from this repository.

## rust-openssl

[![crates.io](https://img.shields.io/crates/v/openssl.svg)](https://crates.io/crates/openssl)

OpenSSL bindings for the Rust programming language.

[Documentation](https://docs.rs/openssl).

## Release Support

The current supported release of `openssl` is 0.10 and `openssl-sys` is 0.9.

New major versions will be published at most once per year. After a new
release, the previous major version will be partially supported with bug
fixes for 3 months, after which support will be dropped entirely.

### Contribution

Unless you explicitly state otherwise, any contribution intentionally
submitted for inclusion in the work by you, as defined in the Apache-2.0
license, shall be dual licensed under the terms of both the Apache License,
Version 2.0 and the MIT license without any additional terms or conditions.

---

## About this Kotlin port

### Installation

```kotlin
dependencies {
    implementation("io.github.kotlinmania:openssl-sys-kotlin:0.1.0-SNAPSHOT")
}
```

### Building

```bash
./gradlew build
./gradlew test
```

### Targets

- macOS arm64
- Linux x64
- Windows mingw-x64
- iOS arm64 / simulator-arm64 (Swift export + XCFramework)
- JS (browser + Node.js)
- Wasm-JS (browser + Node.js)
- Android (API 24+)

### Porting guidelines

See [AGENTS.md](AGENTS.md) and [CLAUDE.md](CLAUDE.md) for translator discipline, port-lint header convention, and Rust → Kotlin idiom mapping.

### License

This Kotlin port is distributed under the same MIT license as the upstream [`rust-openssl/rust-openssl`](https://github.com/rust-openssl/rust-openssl). See [LICENSE](LICENSE) (and any sibling `LICENSE-*` / `NOTICE` files mirrored from upstream) for the full text.

Original work copyrighted by the rust-openssl authors.  
Kotlin port: Copyright (c) 2026 Sydney Renee and The Solace Project.

### Acknowledgments

Thanks to the [`rust-openssl/rust-openssl`](https://github.com/rust-openssl/rust-openssl) maintainers and contributors for the original Rust implementation. This port reproduces their work in Kotlin Multiplatform; bug reports about upstream design or behavior should go to the upstream repository.
