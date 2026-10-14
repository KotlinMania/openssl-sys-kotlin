// Entry point for the OpenSSL N-API native module
// Shared between Kotlin/JS (jsMain) and Kotlin/WASM (wasmJsMain via Node.js)

try {
    module.exports = require('./build/Release/openssl_native.node');
} catch (err) {
    try {
        module.exports = require('./build/Debug/openssl_native.node');
    } catch (err2) {
        throw new Error(
            'Could not load openssl_native addon. ' +
            'Run `npm install` or `node-gyp rebuild` in native/node-openssl directory.\n' +
            'Original error: ' + err.message + '\n' +
            'Debug error: ' + err2.message
        );
    }
}
