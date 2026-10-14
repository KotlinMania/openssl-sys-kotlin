#include <napi.h>
#include <cstdint>
#include <cstdlib>
#include <cstring>
#include <string>
#include <vector>
#include "openssl_wrapper.h"

namespace {

int HexVal(char c) {
    if (c >= '0' && c <= '9') return c - '0';
    if (c >= 'a' && c <= 'f') return c - 'a' + 10;
    if (c >= 'A' && c <= 'F') return c - 'A' + 10;
    return 0;
}

std::vector<uint8_t> DecodeHex(const std::string& hex) {
    size_t len = hex.size() / 2;
    std::vector<uint8_t> out(len);
    for (size_t i = 0; i < len; ++i) {
        out[i] = static_cast<uint8_t>((HexVal(hex[2 * i]) << 4) | HexVal(hex[2 * i + 1]));
    }
    return out;
}

std::string EncodeHex(const uint8_t* data, size_t len) {
    static const char kHex[] = "0123456789abcdef";
    std::string out;
    out.resize(len * 2);
    for (size_t i = 0; i < len; ++i) {
        out[2 * i] = kHex[(data[i] >> 4) & 0xF];
        out[2 * i + 1] = kHex[data[i] & 0xF];
    }
    return out;
}

uint64_t ArgU64(const Napi::CallbackInfo& info, size_t idx) {
    if (idx >= info.Length() || info[idx].IsNull() || info[idx].IsUndefined()) return 0ULL;
    return static_cast<uint64_t>(info[idx].As<Napi::Number>().Int64Value());
}

int64_t ArgI64(const Napi::CallbackInfo& info, size_t idx) {
    if (idx >= info.Length() || info[idx].IsNull() || info[idx].IsUndefined()) return 0LL;
    return info[idx].As<Napi::Number>().Int64Value();
}

int32_t ArgI32(const Napi::CallbackInfo& info, size_t idx) {
    if (idx >= info.Length() || info[idx].IsNull() || info[idx].IsUndefined()) return 0;
    return info[idx].As<Napi::Number>().Int32Value();
}

std::string ArgStr(const Napi::CallbackInfo& info, size_t idx) {
    if (idx >= info.Length() || info[idx].IsNull() || info[idx].IsUndefined()) return "";
    return info[idx].As<Napi::String>().Utf8Value();
}

Napi::Value ShaDigestHex(
    const Napi::CallbackInfo& info,
    size_t out_len,
    int32_t (*fn)(const void*, uint64_t, void*)) {
    Napi::Env env = info.Env();
    std::string hex = ArgStr(info, 0);
    std::vector<uint8_t> in = DecodeHex(hex);
    uint8_t md[64];
    std::memset(md, 0, sizeof(md));
    int32_t ok = fn(in.empty() ? nullptr : in.data(), static_cast<uint64_t>(in.size()), md);
    if (!ok) return env.Null();
    return Napi::String::New(env, EncodeHex(md, out_len));
}

}  // namespace

Napi::Object Init(Napi::Env env, Napi::Object exports) {
    exports.Set("openSslVersionNum", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        return Napi::Number::New(info.Env(), static_cast<double>(openssl_version_num()));
    }));

    exports.Set("openSslVersion", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        const char* s = openssl_version(ArgI32(info, 0));
        return s ? Napi::String::New(info.Env(), s) : info.Env().Null();
    }));

    exports.Set("opensslInitCrypto", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        return Napi::Number::New(info.Env(), openssl_init_crypto(ArgU64(info, 0), ArgU64(info, 1)));
    }));

    exports.Set("opensslInitSsl", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        return Napi::Number::New(info.Env(), openssl_init_ssl(ArgU64(info, 0), ArgU64(info, 1)));
    }));

    exports.Set("cryptoMalloc", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        std::string file = ArgStr(info, 1);
        uint64_t p = openssl_crypto_malloc(ArgU64(info, 0), file.c_str(), ArgI32(info, 2));
        return Napi::Number::New(info.Env(), static_cast<double>(p));
    }));

    exports.Set("cryptoFree", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        std::string file = ArgStr(info, 1);
        openssl_crypto_free(ArgU64(info, 0), file.c_str(), ArgI32(info, 2));
        return info.Env().Undefined();
    }));

    exports.Set("sha1Hex", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        return ShaDigestHex(info, 20, openssl_sha1);
    }));

    exports.Set("sha224Hex", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        return ShaDigestHex(info, 28, openssl_sha224);
    }));

    exports.Set("sha256Hex", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        return ShaDigestHex(info, 32, openssl_sha256);
    }));

    exports.Set("sha384Hex", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        return ShaDigestHex(info, 48, openssl_sha384);
    }));

    exports.Set("sha512Hex", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        return ShaDigestHex(info, 64, openssl_sha512);
    }));

    exports.Set("bioSMem", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        return Napi::Number::New(info.Env(), static_cast<double>(openssl_bio_s_mem()));
    }));

    exports.Set("bioNew", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        return Napi::Number::New(info.Env(), static_cast<double>(openssl_bio_new(ArgU64(info, 0))));
    }));

    exports.Set("bioFreeAll", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        openssl_bio_free_all(ArgU64(info, 0));
        return info.Env().Undefined();
    }));

    exports.Set("bioWriteHex", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        uint64_t bio = ArgU64(info, 0);
        std::vector<uint8_t> in = DecodeHex(ArgStr(info, 1));
        int32_t len = ArgI32(info, 2);
        int32_t actual = (len >= 0 && static_cast<size_t>(len) <= in.size()) ? len : static_cast<int32_t>(in.size());
        int32_t rc = openssl_bio_write(bio, in.empty() ? nullptr : in.data(), actual);
        return Napi::Number::New(info.Env(), rc);
    }));

    exports.Set("bioReadHex", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        uint64_t bio = ArgU64(info, 0);
        int32_t len = ArgI32(info, 1);
        if (len <= 0) return Napi::String::New(info.Env(), "0:");
        std::vector<uint8_t> buf(static_cast<size_t>(len));
        int32_t rc = openssl_bio_read(bio, buf.data(), len);
        std::string hex = rc > 0 ? EncodeHex(buf.data(), static_cast<size_t>(rc)) : "";
        return Napi::String::New(info.Env(), std::to_string(rc) + ":" + hex);
    }));

    exports.Set("bioCtrl", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        int64_t rc = openssl_bio_ctrl(ArgU64(info, 0), ArgI32(info, 1), ArgI64(info, 2), ArgU64(info, 3));
        return Napi::Number::New(info.Env(), static_cast<double>(rc));
    }));

    exports.Set("bioGetMemDataHex", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        uint64_t ptr = 0ULL;
        int64_t len = openssl_bio_get_mem_data_ptr(ArgU64(info, 0), &ptr);
        if (len <= 0 || !ptr) return Napi::String::New(info.Env(), "");
        return Napi::String::New(info.Env(), EncodeHex(reinterpret_cast<const uint8_t*>(static_cast<uintptr_t>(ptr)), static_cast<size_t>(len)));
    }));

    exports.Set("evpSha1", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        return Napi::Number::New(info.Env(), static_cast<double>(openssl_evp_sha1()));
    }));

    exports.Set("evpSha224", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        return Napi::Number::New(info.Env(), static_cast<double>(openssl_evp_sha224()));
    }));

    exports.Set("evpSha256", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        return Napi::Number::New(info.Env(), static_cast<double>(openssl_evp_sha256()));
    }));

    exports.Set("evpSha384", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        return Napi::Number::New(info.Env(), static_cast<double>(openssl_evp_sha384()));
    }));

    exports.Set("evpSha512", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        return Napi::Number::New(info.Env(), static_cast<double>(openssl_evp_sha512()));
    }));

    exports.Set("evpGetDigestbyname", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        std::string name = ArgStr(info, 0);
        return Napi::Number::New(info.Env(), static_cast<double>(openssl_evp_get_digestbyname(name.c_str())));
    }));

    exports.Set("evpMdGetSize", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        return Napi::Number::New(info.Env(), openssl_evp_md_get_size(ArgU64(info, 0)));
    }));

    exports.Set("evpMdCtxNew", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        return Napi::Number::New(info.Env(), static_cast<double>(openssl_evp_md_ctx_new()));
    }));

    exports.Set("evpMdCtxFree", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        openssl_evp_md_ctx_free(ArgU64(info, 0));
        return info.Env().Undefined();
    }));

    exports.Set("evpDigestInitEx", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        return Napi::Number::New(info.Env(), openssl_evp_digest_init_ex(ArgU64(info, 0), ArgU64(info, 1), ArgU64(info, 2)));
    }));

    exports.Set("evpDigestUpdateHex", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        std::vector<uint8_t> in = DecodeHex(ArgStr(info, 1));
        int32_t rc = openssl_evp_digest_update(ArgU64(info, 0), in.empty() ? nullptr : in.data(), static_cast<uint64_t>(in.size()));
        return Napi::Number::New(info.Env(), rc);
    }));

    exports.Set("evpDigestFinalExHex", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        uint8_t md[64];
        std::memset(md, 0, sizeof(md));
        uint32_t s = 0;
        int32_t rc = openssl_evp_digest_final_ex(ArgU64(info, 0), md, &s);
        std::string hex = (rc == 1 && s > 0) ? EncodeHex(md, s) : "";
        return Napi::String::New(info.Env(), std::to_string(rc) + ":" + hex);
    }));

    exports.Set("tlsMethod", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        return Napi::Number::New(info.Env(), static_cast<double>(openssl_tls_method()));
    }));

    exports.Set("tlsClientMethod", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        return Napi::Number::New(info.Env(), static_cast<double>(openssl_tls_client_method()));
    }));

    exports.Set("tlsServerMethod", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        return Napi::Number::New(info.Env(), static_cast<double>(openssl_tls_server_method()));
    }));

    exports.Set("sslCtxNew", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        return Napi::Number::New(info.Env(), static_cast<double>(openssl_ssl_ctx_new(ArgU64(info, 0))));
    }));

    exports.Set("sslCtxFree", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        openssl_ssl_ctx_free(ArgU64(info, 0));
        return info.Env().Undefined();
    }));

    exports.Set("sslCtxCtrl", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        int64_t rc = openssl_ssl_ctx_ctrl(ArgU64(info, 0), ArgI32(info, 1), ArgI64(info, 2), ArgU64(info, 3));
        return Napi::Number::New(info.Env(), static_cast<double>(rc));
    }));

    exports.Set("sslCtxGetOptions", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        return Napi::Number::New(info.Env(), static_cast<double>(openssl_ssl_ctx_get_options(ArgU64(info, 0))));
    }));

    exports.Set("sslCtxSetOptions", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        return Napi::Number::New(info.Env(), static_cast<double>(openssl_ssl_ctx_set_options(ArgU64(info, 0), ArgU64(info, 1))));
    }));

    exports.Set("sslCtxClearOptions", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        return Napi::Number::New(info.Env(), static_cast<double>(openssl_ssl_ctx_clear_options(ArgU64(info, 0), ArgU64(info, 1))));
    }));

    exports.Set("sslNew", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        return Napi::Number::New(info.Env(), static_cast<double>(openssl_ssl_new(ArgU64(info, 0))));
    }));

    exports.Set("sslFree", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        openssl_ssl_free(ArgU64(info, 0));
        return info.Env().Undefined();
    }));

    exports.Set("sslCtrl", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        int64_t rc = openssl_ssl_ctrl(ArgU64(info, 0), ArgI32(info, 1), ArgI64(info, 2), ArgU64(info, 3));
        return Napi::Number::New(info.Env(), static_cast<double>(rc));
    }));

    exports.Set("errGetError", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        return Napi::Number::New(info.Env(), static_cast<double>(openssl_err_get_error()));
    }));

    exports.Set("errPeekError", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        return Napi::Number::New(info.Env(), static_cast<double>(openssl_err_peek_error()));
    }));

    exports.Set("errPeekLastError", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        return Napi::Number::New(info.Env(), static_cast<double>(openssl_err_peek_last_error()));
    }));

    exports.Set("errClearError", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        openssl_err_clear_error();
        return info.Env().Undefined();
    }));

    exports.Set("errErrorStringN", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        int32_t max_len = ArgI32(info, 1);
        size_t len = (max_len > 0 && max_len < 4096) ? static_cast<size_t>(max_len) : 256;
        std::vector<char> buf(len + 1, 0);
        openssl_err_error_string_n(ArgU64(info, 0), buf.data(), static_cast<uint64_t>(len));
        buf[len] = '\0';
        return Napi::String::New(info.Env(), buf.data());
    }));

    exports.Set("randBytesHex", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        int32_t num = ArgI32(info, 0);
        if (num < 0) return Napi::String::New(info.Env(), "0:");
        if (num == 0) return Napi::String::New(info.Env(), "1:");
        std::vector<uint8_t> buf(static_cast<size_t>(num), 0);
        int32_t rc = openssl_rand_bytes(buf.data(), num);
        std::string hex = (rc == 1) ? EncodeHex(buf.data(), static_cast<size_t>(num)) : "";
        return Napi::String::New(info.Env(), std::to_string(rc) + ":" + hex);
    }));

    exports.Set("randStatus", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        return Napi::Number::New(info.Env(), openssl_rand_status());
    }));

    exports.Set("objNid2sn", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        const char* s = openssl_obj_nid2sn(ArgI32(info, 0));
        return s ? Napi::String::New(info.Env(), s) : info.Env().Null();
    }));

    exports.Set("objNid2ln", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        const char* s = openssl_obj_nid2ln(ArgI32(info, 0));
        return s ? Napi::String::New(info.Env(), s) : info.Env().Null();
    }));

    exports.Set("objSn2nid", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        std::string s = ArgStr(info, 0);
        return Napi::Number::New(info.Env(), openssl_obj_sn2nid(s.c_str()));
    }));

    exports.Set("objLn2nid", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        std::string s = ArgStr(info, 0);
        return Napi::Number::New(info.Env(), openssl_obj_ln2nid(s.c_str()));
    }));

    exports.Set("objTxt2nid", Napi::Function::New(env, [](const Napi::CallbackInfo& info) -> Napi::Value {
        std::string s = ArgStr(info, 0);
        return Napi::Number::New(info.Env(), openssl_obj_txt2nid(s.c_str()));
    }));

    return exports;
}

NODE_API_MODULE(openssl_native, Init)
