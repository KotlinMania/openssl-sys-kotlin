// port-lint: ignore — N-API shim that re-exports a narrow slice of the
// openssl-sys FFI surface to Kotlin/JS via Node 22 N-API. Mirrors the
// signatures Kotlin/Native cinterop generates from openssl/evp.h so the
// Node-side Sha.kt has identical semantics to the macosArm64-side Sha.kt.

#include <napi.h>
#include <openssl/evp.h>
#include <openssl/sha.h>

#include <cstddef>
#include <cstring>

namespace {

// JS: openssl_shim.evpQDigest(name: string, data: Buffer) -> Buffer | null
// Wraps EVP_Q_digest(libctx=NULL, name, propq=NULL, data, datalen,
// md, mdlen=NULL). Returns the digest as a new Node Buffer on success,
// or null if EVP_Q_digest returns 0 (unknown algorithm name, allocation
// failure, etc.).
Napi::Value EvpQDigest(const Napi::CallbackInfo& info) {
  Napi::Env env = info.Env();

  if (info.Length() < 2) {
    Napi::TypeError::New(env, "evpQDigest(name, data) requires 2 arguments")
        .ThrowAsJavaScriptException();
    return env.Null();
  }
  if (!info[0].IsString()) {
    Napi::TypeError::New(env, "evpQDigest: name must be a string")
        .ThrowAsJavaScriptException();
    return env.Null();
  }
  if (!info[1].IsBuffer()) {
    Napi::TypeError::New(env, "evpQDigest: data must be a Buffer")
        .ThrowAsJavaScriptException();
    return env.Null();
  }

  std::string name = info[0].As<Napi::String>().Utf8Value();
  Napi::Buffer<uint8_t> data = info[1].As<Napi::Buffer<uint8_t>>();

  // EVP_MAX_MD_SIZE is the upper bound for any algorithm openssl supports.
  unsigned char md[EVP_MAX_MD_SIZE];
  std::size_t mdlen = 0;

  const int ok = EVP_Q_digest(
      /* libctx  */ nullptr,
      /* name    */ name.c_str(),
      /* propq   */ nullptr,
      /* data    */ data.Data(),
      /* datalen */ data.Length(),
      /* md      */ md,
      /* mdlen   */ &mdlen);

  if (ok != 1) {
    return env.Null();
  }

  return Napi::Buffer<uint8_t>::Copy(env, md, mdlen);
}

// JS: openssl_shim.opensslVersionNumber() -> number
// Wraps OpenSSL_version_num() so the JS side can sanity-check that it is
// loading a real OpenSSL 3.x at runtime. Equivalent to the upstream
// OPENSSL_VERSION_NUMBER constant.
Napi::Value OpensslVersionNumber(const Napi::CallbackInfo& info) {
  Napi::Env env = info.Env();
  unsigned long n = OpenSSL_version_num();
  return Napi::Number::New(env, static_cast<double>(n));
}

Napi::Object Init(Napi::Env env, Napi::Object exports) {
  exports.Set("evpQDigest", Napi::Function::New(env, EvpQDigest, "evpQDigest"));
  exports.Set("opensslVersionNumber",
              Napi::Function::New(env, OpensslVersionNumber, "opensslVersionNumber"));
  return exports;
}

}  // namespace

NODE_API_MODULE(NODE_GYP_MODULE_NAME, Init)
