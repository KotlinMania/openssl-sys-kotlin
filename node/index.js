'use strict';

// CommonJS loader for the openssl_shim N-API addon. The Kotlin/JS jsNodejs
// target consumes this via an `external` declaration block that mirrors the
// addon's exports: `evpQDigest(name: string, data: Buffer) -> Buffer | null`
// and `opensslVersionNumber() -> number`.

module.exports = require('./build/Release/openssl_shim.node');
