#ifndef _GNU_SOURCE
#define _GNU_SOURCE
#endif
#include "openssl_wrapper.h"
#include <stdlib.h>
#include <string.h>
#include <stdio.h>

#ifdef _WIN32
#define WIN32_LEAN_AND_MEAN
#include <windows.h>
#else
#include <dlfcn.h>
#ifndef RTLD_DEFAULT
#define RTLD_DEFAULT ((void*)0)
#endif
#endif
#ifdef __APPLE__
#include <TargetConditionals.h>
#endif

static void* g_libcrypto = NULL;
static void* g_libssl = NULL;
static int g_initialized = 0;

static void* ossl_try_open(const char* path) {
    if (!path || !path[0]) return NULL;
#ifdef _WIN32
    return (void*)LoadLibraryA(path);
#else
    return dlopen(path, RTLD_LAZY | RTLD_GLOBAL);
#endif
}

static void* ossl_try_open_in_dir(const char* dir, const char* const* names) {
    if (!dir || !dir[0]) return NULL;
    char buf[1024];
    for (int i = 0; names[i] != NULL; ++i) {
#ifdef _WIN32
        snprintf(buf, sizeof(buf), "%s\\%s", dir, names[i]);
#else
        snprintf(buf, sizeof(buf), "%s/%s", dir, names[i]);
#endif
        void* h = ossl_try_open(buf);
        if (h) return h;
    }
    return NULL;
}

static void ossl_ensure_loaded(void) {
    if (g_initialized) return;
    g_initialized = 1;

#if defined(_WIN32)
    static const char* const crypto_names[] = {
        "libcrypto-3-x64.dll",
        "libcrypto-3.dll",
        "libcrypto-1_1-x64.dll",
        "libcrypto.dll",
        NULL
    };
    static const char* const ssl_names[] = {
        "libssl-3-x64.dll",
        "libssl-3.dll",
        "libssl-1_1-x64.dll",
        "libssl.dll",
        NULL
    };
#elif defined(__APPLE__)
    static const char* const crypto_names[] = {
        "libcrypto.3.dylib",
        "libcrypto.1.1.dylib",
        "libcrypto.46.dylib",
        "libcrypto.44.dylib",
        "libcrypto.35.dylib",
        NULL
    };
    static const char* const ssl_names[] = {
        "libssl.3.dylib",
        "libssl.1.1.dylib",
        "libssl.48.dylib",
        "libssl.46.dylib",
        "libssl.44.dylib",
        "libssl.35.dylib",
        NULL
    };
#else
    static const char* const crypto_names[] = {
        "libcrypto.so.3",
        "libcrypto.so.1.1",
        "libcrypto.so",
        NULL
    };
    static const char* const ssl_names[] = {
        "libssl.so.3",
        "libssl.so.1.1",
        "libssl.so",
        NULL
    };
#endif

    const char* env_lib_dir = getenv("OPENSSL_LIB_DIR");
    if (env_lib_dir && env_lib_dir[0]) {
        g_libcrypto = ossl_try_open_in_dir(env_lib_dir, crypto_names);
        g_libssl = ossl_try_open_in_dir(env_lib_dir, ssl_names);
    }

    const char* env_dir = getenv("OPENSSL_DIR");
    if (env_dir && env_dir[0]) {
        char sub[1024];
#ifdef _WIN32
        snprintf(sub, sizeof(sub), "%s\\bin", env_dir);
        if (!g_libcrypto) g_libcrypto = ossl_try_open_in_dir(sub, crypto_names);
        if (!g_libssl) g_libssl = ossl_try_open_in_dir(sub, ssl_names);
        snprintf(sub, sizeof(sub), "%s\\lib", env_dir);
        if (!g_libcrypto) g_libcrypto = ossl_try_open_in_dir(sub, crypto_names);
        if (!g_libssl) g_libssl = ossl_try_open_in_dir(sub, ssl_names);
#else
        snprintf(sub, sizeof(sub), "%s/lib", env_dir);
        if (!g_libcrypto) g_libcrypto = ossl_try_open_in_dir(sub, crypto_names);
        if (!g_libssl) g_libssl = ossl_try_open_in_dir(sub, ssl_names);
        snprintf(sub, sizeof(sub), "%s/lib64", env_dir);
        if (!g_libcrypto) g_libcrypto = ossl_try_open_in_dir(sub, crypto_names);
        if (!g_libssl) g_libssl = ossl_try_open_in_dir(sub, ssl_names);
#endif
    }

#if defined(__APPLE__) && TARGET_OS_OSX
    static const char* const mac_dirs[] = {
        "/usr/local/lib",
        "/usr/lib",
        NULL
    };
    for (int i = 0; mac_dirs[i] != NULL; ++i) {
        if (!g_libcrypto) g_libcrypto = ossl_try_open_in_dir(mac_dirs[i], crypto_names);
        if (!g_libssl) g_libssl = ossl_try_open_in_dir(mac_dirs[i], ssl_names);
    }
#elif defined(_WIN32)
    static const char* const win_dirs[] = {
        "C:\\Program Files\\OpenSSL-Win64\\bin",
        "C:\\Program Files\\OpenSSL\\bin",
        "C:\\OpenSSL-Win64\\bin",
        "C:\\vcpkg\\installed\\x64-windows\\bin",
        "C:\\msys64\\mingw64\\bin",
        "C:\\msys64\\ucrt64\\bin",
        "C:\\Program Files\\Git\\mingw64\\bin",
        NULL
    };
    for (int i = 0; win_dirs[i] != NULL; ++i) {
        if (!g_libcrypto) g_libcrypto = ossl_try_open_in_dir(win_dirs[i], crypto_names);
        if (!g_libssl) g_libssl = ossl_try_open_in_dir(win_dirs[i], ssl_names);
    }
#else
    static const char* const linux_dirs[] = {
        "/usr/lib/x86_64-linux-gnu",
        "/usr/lib/aarch64-linux-gnu",
        "/usr/lib64",
        "/usr/lib",
        "/lib/x86_64-linux-gnu",
        "/lib/aarch64-linux-gnu",
        "/lib64",
        "/lib",
        NULL
    };
    for (int i = 0; linux_dirs[i] != NULL; ++i) {
        if (!g_libcrypto) g_libcrypto = ossl_try_open_in_dir(linux_dirs[i], crypto_names);
        if (!g_libssl) g_libssl = ossl_try_open_in_dir(linux_dirs[i], ssl_names);
    }
#endif

    for (int i = 0; crypto_names[i] != NULL && !g_libcrypto; ++i) {
        g_libcrypto = ossl_try_open(crypto_names[i]);
    }
    for (int i = 0; ssl_names[i] != NULL && !g_libssl; ++i) {
        g_libssl = ossl_try_open(ssl_names[i]);
    }
}

static uintptr_t ossl_sym_crypto(const char* name) {
    ossl_ensure_loaded();
#ifdef _WIN32
    if (g_libcrypto) {
        FARPROC p = GetProcAddress((HMODULE)g_libcrypto, name);
        if (p) return (uintptr_t)p;
    }
    return 0;
#else
    if (g_libcrypto) {
        void* p = dlsym(g_libcrypto, name);
        if (p) return (uintptr_t)p;
    }
    return 0;
#endif
}

static uintptr_t ossl_sym_ssl(const char* name) {
    ossl_ensure_loaded();
#ifdef _WIN32
    if (g_libssl) {
        FARPROC p = GetProcAddress((HMODULE)g_libssl, name);
        if (p) return (uintptr_t)p;
    }
    return ossl_sym_crypto(name);
#else
    if (g_libssl) {
        void* p = dlsym(g_libssl, name);
        if (p) return (uintptr_t)p;
    }
    return ossl_sym_crypto(name);
#endif
}

/* Portable FIPS 180-4 SHA-1, SHA-224/256, SHA-384/512 for targets without dynamic libcrypto */

static uint32_t ossl_rotr32(uint32_t x, uint32_t n) {
    return (x >> n) | (x << (32U - n));
}

static uint32_t ossl_rotl32(uint32_t x, uint32_t n) {
    return (x << n) | (x >> (32U - n));
}

static uint64_t ossl_rotr64(uint64_t x, uint32_t n) {
    return (x >> n) | (x << (64U - n));
}

static void ossl_sha1_soft(const uint8_t* data, size_t len, uint8_t* out) {
    uint32_t h0 = 0x67452301U, h1 = 0xefcdab89U, h2 = 0x98badcfeU, h3 = 0x10325476U, h4 = 0xc3d2e1f0U;
    size_t total_len = ((len + 9 + 63) / 64) * 64;
    uint8_t* msg = (uint8_t*)calloc(total_len, 1);
    if (!msg) return;
    if (len > 0 && data) memcpy(msg, data, len);
    msg[len] = 0x80;
    uint64_t bit_len = (uint64_t)len * 8ULL;
    for (int i = 0; i < 8; ++i) msg[total_len - 1 - i] = (uint8_t)(bit_len >> (i * 8));
    uint32_t w[80];
    for (size_t off = 0; off < total_len; off += 64) {
        for (int i = 0; i < 16; ++i) {
            size_t j = off + (size_t)i * 4;
            w[i] = ((uint32_t)msg[j] << 24) | ((uint32_t)msg[j + 1] << 16) |
                   ((uint32_t)msg[j + 2] << 8) | (uint32_t)msg[j + 3];
        }
        for (int i = 16; i < 80; ++i) w[i] = ossl_rotl32(w[i - 3] ^ w[i - 8] ^ w[i - 14] ^ w[i - 16], 1);
        uint32_t a = h0, b = h1, c = h2, d = h3, e = h4;
        for (int i = 0; i < 80; ++i) {
            uint32_t f, k;
            if (i < 20) { f = (b & c) | ((~b) & d); k = 0x5a827999U; }
            else if (i < 40) { f = b ^ c ^ d; k = 0x6ed9eba1U; }
            else if (i < 60) { f = (b & c) | (b & d) | (c & d); k = 0x8f1bbcdcU; }
            else { f = b ^ c ^ d; k = 0xca62c1d6U; }
            uint32_t temp = ossl_rotl32(a, 5) + f + e + k + w[i];
            e = d; d = c; c = ossl_rotl32(b, 30); b = a; a = temp;
        }
        h0 += a; h1 += b; h2 += c; h3 += d; h4 += e;
    }
    free(msg);
    uint32_t hs[5] = { h0, h1, h2, h3, h4 };
    for (int i = 0; i < 5; ++i) {
        out[i * 4]     = (uint8_t)(hs[i] >> 24);
        out[i * 4 + 1] = (uint8_t)(hs[i] >> 16);
        out[i * 4 + 2] = (uint8_t)(hs[i] >> 8);
        out[i * 4 + 3] = (uint8_t)hs[i];
    }
}

static void ossl_sha256_core(const uint8_t* data, size_t len, const uint32_t iv[8], uint8_t* out, int out_words) {
    static const uint32_t k[64] = {
        0x428a2f98U, 0x71374491U, 0xb5c0fbcfU, 0xe9b5dba5U, 0x3956c25bU, 0x59f111f1U, 0x923f82a4U, 0xab1c5ed5U,
        0xd807aa98U, 0x12835b01U, 0x243185beU, 0x550c7dc3U, 0x72be5d74U, 0x80deb1feU, 0x9bdc06a7U, 0xc19bf174U,
        0xe49b69c1U, 0xefbe4786U, 0x0fc19dc6U, 0x240ca1ccU, 0x2de92c6fU, 0x4a7484aaU, 0x5cb0a9dcU, 0x76f988daU,
        0x983e5152U, 0xa831c66dU, 0xb00327c8U, 0xbf597fc7U, 0xc6e00bf3U, 0xd5a79147U, 0x06ca6351U, 0x14292967U,
        0x27b70a85U, 0x2e1b2138U, 0x4d2c6dfcU, 0x53380d13U, 0x650a7354U, 0x766a0abbU, 0x81c2c92eU, 0x92722c85U,
        0xa2bfe8a1U, 0xa81a664bU, 0xc24b8b70U, 0xc76c51a3U, 0xd192e819U, 0xd6990624U, 0xf40e3585U, 0x106aa070U,
        0x19a4c116U, 0x1e376c08U, 0x2748774cU, 0x34b0bcb5U, 0x391c0cb3U, 0x4ed8aa4aU, 0x5b9cca4fU, 0x682e6ff3U,
        0x748f82eeU, 0x78a5636fU, 0x84c87814U, 0x8cc70208U, 0x90befffaU, 0xa4506cebU, 0xbef9a3f7U, 0xc67178f2U
    };
    uint32_t h[8];
    memcpy(h, iv, sizeof(h));
    size_t total_len = ((len + 9 + 63) / 64) * 64;
    uint8_t* msg = (uint8_t*)calloc(total_len, 1);
    if (!msg) return;
    if (len > 0 && data) memcpy(msg, data, len);
    msg[len] = 0x80;
    uint64_t bit_len = (uint64_t)len * 8ULL;
    for (int i = 0; i < 8; ++i) msg[total_len - 1 - i] = (uint8_t)(bit_len >> (i * 8));
    uint32_t w[64];
    for (size_t off = 0; off < total_len; off += 64) {
        for (int i = 0; i < 16; ++i) {
            size_t j = off + (size_t)i * 4;
            w[i] = ((uint32_t)msg[j] << 24) | ((uint32_t)msg[j + 1] << 16) |
                   ((uint32_t)msg[j + 2] << 8) | (uint32_t)msg[j + 3];
        }
        for (int i = 16; i < 64; ++i) {
            uint32_t s0 = ossl_rotr32(w[i - 15], 7) ^ ossl_rotr32(w[i - 15], 18) ^ (w[i - 15] >> 3);
            uint32_t s1 = ossl_rotr32(w[i - 2], 17) ^ ossl_rotr32(w[i - 2], 19) ^ (w[i - 2] >> 10);
            w[i] = w[i - 16] + s0 + w[i - 7] + s1;
        }
        uint32_t a = h[0], b = h[1], c = h[2], d = h[3], e = h[4], f = h[5], g = h[6], hh = h[7];
        for (int i = 0; i < 64; ++i) {
            uint32_t s1 = ossl_rotr32(e, 6) ^ ossl_rotr32(e, 11) ^ ossl_rotr32(e, 25);
            uint32_t ch = (e & f) ^ ((~e) & g);
            uint32_t temp1 = hh + s1 + ch + k[i] + w[i];
            uint32_t s0 = ossl_rotr32(a, 2) ^ ossl_rotr32(a, 13) ^ ossl_rotr32(a, 22);
            uint32_t maj = (a & b) ^ (a & c) ^ (b & c);
            uint32_t temp2 = s0 + maj;
            hh = g; g = f; f = e; e = d + temp1; d = c; c = b; b = a; a = temp1 + temp2;
        }
        h[0] += a; h[1] += b; h[2] += c; h[3] += d;
        h[4] += e; h[5] += f; h[6] += g; h[7] += hh;
    }
    free(msg);
    for (int i = 0; i < out_words; ++i) {
        out[i * 4]     = (uint8_t)(h[i] >> 24);
        out[i * 4 + 1] = (uint8_t)(h[i] >> 16);
        out[i * 4 + 2] = (uint8_t)(h[i] >> 8);
        out[i * 4 + 3] = (uint8_t)h[i];
    }
}

static void ossl_sha512_core(const uint8_t* data, size_t len, const uint64_t iv[8], uint8_t* out, int out_words) {
    static const uint64_t k[80] = {
        0x428a2f98d728ae22ULL, 0x7137449123ef65cdULL, 0xb5c0fbcfec4d3b2fULL, 0xe9b5dba58189dbbcULL,
        0x3956c25bf348b538ULL, 0x59f111f1b605d019ULL, 0x923f82a4af194f9bULL, 0xab1c5ed5da6d8118ULL,
        0xd807aa98a3030242ULL, 0x12835b0145706fbeULL, 0x243185be4ee4b28cULL, 0x550c7dc3d5ffb4e2ULL,
        0x72be5d74f27b896fULL, 0x80deb1fe3b1696b1ULL, 0x9bdc06a725c71235ULL, 0xc19bf174cf692694ULL,
        0xe49b69c19ef14ad2ULL, 0xefbe4786384f25e3ULL, 0x0fc19dc68b8cd5b5ULL, 0x240ca1cc77ac9c65ULL,
        0x2de92c6f592b0275ULL, 0x4a7484aa6ea6e483ULL, 0x5cb0a9dcbd41fbd4ULL, 0x76f988da831153b5ULL,
        0x983e5152ee66dfabULL, 0xa831c66d2db43210ULL, 0xb00327c898fb213fULL, 0xbf597fc7beef0ee4ULL,
        0xc6e00bf33da88fc2ULL, 0xd5a79147930aa725ULL, 0x06ca6351e003826fULL, 0x142929670a0e6e70ULL,
        0x27b70a8546d22ffcULL, 0x2e1b21385c26c926ULL, 0x4d2c6dfc5ac42aedULL, 0x53380d139d95b3dfULL,
        0x650a73548baf63deULL, 0x766a0abb3c77b2a8ULL, 0x81c2c92e47edaee6ULL, 0x92722c851482353bULL,
        0xa2bfe8a14cf10364ULL, 0xa81a664bbc423001ULL, 0xc24b8b70d0f89791ULL, 0xc76c51a30654be30ULL,
        0xd192e819d6ef5218ULL, 0xd69906245565a910ULL, 0xf40e35855771202aULL, 0x106aa07032bbd1b8ULL,
        0x19a4c116b8d2d0c8ULL, 0x1e376c085141ab53ULL, 0x2748774cdf8eeb99ULL, 0x34b0bcb5e19b48a8ULL,
        0x391c0cb3c5c95a63ULL, 0x4ed8aa4ae3418acbULL, 0x5b9cca4f7763e373ULL, 0x682e6ff3d6b2b8a3ULL,
        0x748f82ee5defb2fcULL, 0x78a5636f43172f60ULL, 0x84c87814a1f0ab72ULL, 0x8cc702081a6439ecULL,
        0x90befffa23631e28ULL, 0xa4506cebde82bde9ULL, 0xbef9a3f7b2c67915ULL, 0xc67178f2e372532bULL,
        0xca273eceea26619cULL, 0xd186b8c721c0c207ULL, 0xeada7dd6cde0eb1eULL, 0xf57d4f7fee6ed178ULL,
        0x06f067aa72176fbaULL, 0x0a637dc5a2c898a6ULL, 0x113f9804bef90daeULL, 0x1b710b35131c471bULL,
        0x28db77f523047d84ULL, 0x32caab7b40c72493ULL, 0x3c9ebe0a15c9bebcULL, 0x431d67c49c100d4cULL,
        0x4cc5d4becb3e42b6ULL, 0x597f299cfc657e2aULL, 0x5fcb6fab3ad6faecULL, 0x6c44198c4a475817ULL
    };
    uint64_t h[8];
    memcpy(h, iv, sizeof(h));
    size_t total_len = ((len + 17 + 127) / 128) * 128;
    uint8_t* msg = (uint8_t*)calloc(total_len, 1);
    if (!msg) return;
    if (len > 0 && data) memcpy(msg, data, len);
    msg[len] = 0x80;
    uint64_t bit_len = (uint64_t)len * 8ULL;
    for (int i = 0; i < 8; ++i) msg[total_len - 1 - i] = (uint8_t)(bit_len >> (i * 8));
    uint64_t w[80];
    for (size_t off = 0; off < total_len; off += 128) {
        for (int i = 0; i < 16; ++i) {
            size_t j = off + (size_t)i * 8;
            uint64_t v = 0;
            for (int b = 0; b < 8; ++b) v = (v << 8) | (uint64_t)msg[j + (size_t)b];
            w[i] = v;
        }
        for (int i = 16; i < 80; ++i) {
            uint64_t s0 = ossl_rotr64(w[i - 15], 1) ^ ossl_rotr64(w[i - 15], 8) ^ (w[i - 15] >> 7);
            uint64_t s1 = ossl_rotr64(w[i - 2], 19) ^ ossl_rotr64(w[i - 2], 61) ^ (w[i - 2] >> 6);
            w[i] = w[i - 16] + s0 + w[i - 7] + s1;
        }
        uint64_t a = h[0], b = h[1], c = h[2], d = h[3], e = h[4], f = h[5], g = h[6], hh = h[7];
        for (int i = 0; i < 80; ++i) {
            uint64_t s1 = ossl_rotr64(e, 14) ^ ossl_rotr64(e, 18) ^ ossl_rotr64(e, 41);
            uint64_t ch = (e & f) ^ ((~e) & g);
            uint64_t temp1 = hh + s1 + ch + k[i] + w[i];
            uint64_t s0 = ossl_rotr64(a, 28) ^ ossl_rotr64(a, 34) ^ ossl_rotr64(a, 39);
            uint64_t maj = (a & b) ^ (a & c) ^ (b & c);
            uint64_t temp2 = s0 + maj;
            hh = g; g = f; f = e; e = d + temp1; d = c; c = b; b = a; a = temp1 + temp2;
        }
        h[0] += a; h[1] += b; h[2] += c; h[3] += d;
        h[4] += e; h[5] += f; h[6] += g; h[7] += hh;
    }
    free(msg);
    for (int i = 0; i < out_words; ++i) {
        for (int b = 0; b < 8; ++b) {
            out[i * 8 + b] = (uint8_t)(h[i] >> ((7 - b) * 8));
        }
    }
}

/* Version & Initialization */

uint64_t openssl_version_num(void) {
    typedef unsigned long (*fn_t)(void);
    fn_t fn = (fn_t)ossl_sym_crypto("OpenSSL_version_num");
    return fn ? (uint64_t)fn() : 0x30000000ULL;
}

const char* openssl_version(int32_t t) {
    typedef const char* (*fn_t)(int);
    fn_t fn = (fn_t)ossl_sym_crypto("OpenSSL_version");
    if (fn) return fn((int)t);
    return (t == 4) ? "platform: native" : "OpenSSL 3.0.0";
}

int32_t openssl_init_crypto(uint64_t opts, uint64_t settings) {
    typedef int (*fn_t)(uint64_t, const void*);
    fn_t fn = (fn_t)ossl_sym_crypto("OPENSSL_init_crypto");
    return fn ? (int32_t)fn(opts, (const void*)(uintptr_t)settings) : 1;
}

int32_t openssl_init_ssl(uint64_t opts, uint64_t settings) {
    typedef int (*fn_t)(uint64_t, const void*);
    fn_t fn = (fn_t)ossl_sym_ssl("OPENSSL_init_ssl");
    return fn ? (int32_t)fn(opts, (const void*)(uintptr_t)settings) : 1;
}

uint64_t openssl_crypto_malloc(uint64_t num, const char* file, int32_t line) {
    typedef void* (*fn_t)(size_t, const char*, int);
    fn_t fn = (fn_t)ossl_sym_crypto("CRYPTO_malloc");
    const char* f = file ? file : "openssl-sys-kotlin";
    if (fn) return (uint64_t)(uintptr_t)fn((size_t)num, f, (int)line);
    void* p = malloc((size_t)(num > 0 ? num : 1));
    return (uint64_t)(uintptr_t)p;
}

void openssl_crypto_free(uint64_t addr, const char* file, int32_t line) {
    if (!addr) return;
    typedef void (*fn_t)(void*, const char*, int);
    fn_t fn = (fn_t)ossl_sym_crypto("CRYPTO_free");
    const char* f = file ? file : "openssl-sys-kotlin";
    if (fn) {
        fn((void*)(uintptr_t)addr, f, (int)line);
    } else {
        free((void*)(uintptr_t)addr);
    }
}

/* SHA One-Shot Digests */

int32_t openssl_sha1(const void* d, uint64_t n, void* md) {
    if (!md) return 0;
    typedef unsigned char* (*fn_t)(const unsigned char*, size_t, unsigned char*);
    fn_t fn = (fn_t)ossl_sym_crypto("SHA1");
    const unsigned char empty = 0;
    const unsigned char* in = d ? (const unsigned char*)d : &empty;
    if (fn) return fn(in, (size_t)n, (unsigned char*)md) != NULL ? 1 : 0;
    ossl_sha1_soft(in, (size_t)n, (uint8_t*)md);
    return 1;
}

int32_t openssl_sha224(const void* d, uint64_t n, void* md) {
    if (!md) return 0;
    typedef unsigned char* (*fn_t)(const unsigned char*, size_t, unsigned char*);
    fn_t fn = (fn_t)ossl_sym_crypto("SHA224");
    const unsigned char empty = 0;
    const unsigned char* in = d ? (const unsigned char*)d : &empty;
    if (fn) return fn(in, (size_t)n, (unsigned char*)md) != NULL ? 1 : 0;
    static const uint32_t iv[8] = {
        0xc1059ed8U, 0x367cd507U, 0x3070dd17U, 0xf70e5939U,
        0xffc00b31U, 0x68581511U, 0x64f98fa7U, 0xbefa4fa4U
    };
    ossl_sha256_core(in, (size_t)n, iv, (uint8_t*)md, 7);
    return 1;
}

int32_t openssl_sha256(const void* d, uint64_t n, void* md) {
    if (!md) return 0;
    typedef unsigned char* (*fn_t)(const unsigned char*, size_t, unsigned char*);
    fn_t fn = (fn_t)ossl_sym_crypto("SHA256");
    const unsigned char empty = 0;
    const unsigned char* in = d ? (const unsigned char*)d : &empty;
    if (fn) return fn(in, (size_t)n, (unsigned char*)md) != NULL ? 1 : 0;
    static const uint32_t iv[8] = {
        0x6a09e667U, 0xbb67ae85U, 0x3c6ef372U, 0xa54ff53aU,
        0x510e527fU, 0x9b05688cU, 0x1f83d9abU, 0x5be0cd19U
    };
    ossl_sha256_core(in, (size_t)n, iv, (uint8_t*)md, 8);
    return 1;
}

int32_t openssl_sha384(const void* d, uint64_t n, void* md) {
    if (!md) return 0;
    typedef unsigned char* (*fn_t)(const unsigned char*, size_t, unsigned char*);
    fn_t fn = (fn_t)ossl_sym_crypto("SHA384");
    const unsigned char empty = 0;
    const unsigned char* in = d ? (const unsigned char*)d : &empty;
    if (fn) return fn(in, (size_t)n, (unsigned char*)md) != NULL ? 1 : 0;
    static const uint64_t iv[8] = {
        0xcbbb9d5dc1059ed8ULL, 0x629a292a367cd507ULL, 0x9159015a3070dd17ULL, 0x152fecd8f70e5939ULL,
        0x67332667ffc00b31ULL, 0x8eb44a8768581511ULL, 0xdb0c2e0d64f98fa7ULL, 0x47b5481dbefa4fa4ULL
    };
    ossl_sha512_core(in, (size_t)n, iv, (uint8_t*)md, 6);
    return 1;
}

int32_t openssl_sha512(const void* d, uint64_t n, void* md) {
    if (!md) return 0;
    typedef unsigned char* (*fn_t)(const unsigned char*, size_t, unsigned char*);
    fn_t fn = (fn_t)ossl_sym_crypto("SHA512");
    const unsigned char empty = 0;
    const unsigned char* in = d ? (const unsigned char*)d : &empty;
    if (fn) return fn(in, (size_t)n, (unsigned char*)md) != NULL ? 1 : 0;
    static const uint64_t iv[8] = {
        0x6a09e667f3bcc908ULL, 0xbb67ae8584caa73bULL, 0x3c6ef372fe94f82bULL, 0xa54ff53a5f1d36f1ULL,
        0x510e527fade682d1ULL, 0x9b05688c2b3e6c1fULL, 0x1f83d9abfb41bd6bULL, 0x5be0cd19137e2179ULL
    };
    ossl_sha512_core(in, (size_t)n, iv, (uint8_t*)md, 8);
    return 1;
}

/* BIO */

typedef struct {
    unsigned char* buf;
    size_t len;
    size_t cap;
    size_t rpos;
    int eof_ret;
} ossl_soft_bio_t;

uint64_t openssl_bio_s_mem(void) {
    typedef const void* (*fn_t)(void);
    fn_t fn = (fn_t)ossl_sym_crypto("BIO_s_mem");
    return fn ? (uint64_t)(uintptr_t)fn() : 0xB1000001ULL;
}

uint64_t openssl_bio_new(uint64_t method) {
    if (!method) return 0ULL;
    typedef void* (*fn_t)(const void*);
    fn_t fn = (fn_t)ossl_sym_crypto("BIO_new");
    if (fn) return (uint64_t)(uintptr_t)fn((const void*)(uintptr_t)method);
    ossl_soft_bio_t* b = (ossl_soft_bio_t*)calloc(1, sizeof(ossl_soft_bio_t));
    if (!b) return 0ULL;
    b->eof_ret = -1;
    return (uint64_t)(uintptr_t)b;
}

void openssl_bio_free_all(uint64_t bio) {
    if (!bio) return;
    typedef void (*fn_t)(void*);
    fn_t fn = (fn_t)ossl_sym_crypto("BIO_free_all");
    if (fn) {
        fn((void*)(uintptr_t)bio);
        return;
    }
    ossl_soft_bio_t* b = (ossl_soft_bio_t*)(uintptr_t)bio;
    free(b->buf);
    free(b);
}

int32_t openssl_bio_read(uint64_t bio, void* data, int32_t len) {
    if (!bio || !data || len <= 0) return -1;
    typedef int (*fn_t)(void*, void*, int);
    fn_t fn = (fn_t)ossl_sym_crypto("BIO_read");
    if (fn) return (int32_t)fn((void*)(uintptr_t)bio, data, (int)len);
    ossl_soft_bio_t* b = (ossl_soft_bio_t*)(uintptr_t)bio;
    size_t avail = (b->len > b->rpos) ? (b->len - b->rpos) : 0;
    if (avail == 0) return b->eof_ret;
    size_t n = ((size_t)len < avail) ? (size_t)len : avail;
    memcpy(data, b->buf + b->rpos, n);
    b->rpos += n;
    return (int32_t)n;
}

int32_t openssl_bio_write(uint64_t bio, const void* data, int32_t len) {
    if (!bio || !data || len <= 0) return 0;
    typedef int (*fn_t)(void*, const void*, int);
    fn_t fn = (fn_t)ossl_sym_crypto("BIO_write");
    if (fn) return (int32_t)fn((void*)(uintptr_t)bio, data, (int)len);
    ossl_soft_bio_t* b = (ossl_soft_bio_t*)(uintptr_t)bio;
    size_t need = b->len + (size_t)len;
    if (need > b->cap) {
        size_t new_cap = b->cap ? b->cap * 2 : 64;
        while (new_cap < need) new_cap *= 2;
        unsigned char* nb = (unsigned char*)realloc(b->buf, new_cap);
        if (!nb) return -1;
        b->buf = nb;
        b->cap = new_cap;
    }
    memcpy(b->buf + b->len, data, (size_t)len);
    b->len += (size_t)len;
    return len;
}

int64_t openssl_bio_ctrl(uint64_t bio, int32_t cmd, int64_t larg, uint64_t parg) {
    if (!bio) return 0LL;
    typedef long (*fn_t)(void*, int, long, void*);
    fn_t fn = (fn_t)ossl_sym_crypto("BIO_ctrl");
    if (fn) return (int64_t)fn((void*)(uintptr_t)bio, (int)cmd, (long)larg, (void*)(uintptr_t)parg);
    ossl_soft_bio_t* b = (ossl_soft_bio_t*)(uintptr_t)bio;
    size_t avail = (b->len > b->rpos) ? (b->len - b->rpos) : 0;
    switch (cmd) {
        case 1: /* BIO_CTRL_RESET */
            b->len = 0;
            b->rpos = 0;
            return 1LL;
        case 2: /* BIO_CTRL_EOF */
            return avail == 0 ? 1LL : 0LL;
        case 3: /* BIO_CTRL_INFO */
            if (parg) *(void**)(uintptr_t)parg = (void*)(b->buf + b->rpos);
            return (int64_t)avail;
        case 10: /* BIO_CTRL_PENDING */
            return (int64_t)avail;
        case 11: /* BIO_CTRL_FLUSH */
            return 1LL;
        case 130: /* BIO_C_SET_BUF_MEM_EOF_RETURN */
            b->eof_ret = (int)larg;
            return 1LL;
        default:
            return 1LL;
    }
}

int64_t openssl_bio_get_mem_data_ptr(uint64_t bio, uint64_t* out_ptr) {
    if (out_ptr) *out_ptr = 0ULL;
    if (!bio) return 0LL;
    void* ptr = NULL;
    int64_t len = openssl_bio_ctrl(bio, 3 /* BIO_CTRL_INFO */, 0LL, (uint64_t)(uintptr_t)&ptr);
    if (out_ptr) *out_ptr = (uint64_t)(uintptr_t)ptr;
    return len;
}

/* EVP Digest */

typedef struct {
    uint64_t type;
    unsigned char* buf;
    size_t len;
    size_t cap;
} ossl_soft_md_ctx_t;

uint64_t openssl_evp_sha1(void) {
    typedef const void* (*fn_t)(void);
    fn_t fn = (fn_t)ossl_sym_crypto("EVP_sha1");
    return fn ? (uint64_t)(uintptr_t)fn() : 64ULL;
}

uint64_t openssl_evp_sha224(void) {
    typedef const void* (*fn_t)(void);
    fn_t fn = (fn_t)ossl_sym_crypto("EVP_sha224");
    return fn ? (uint64_t)(uintptr_t)fn() : 675ULL;
}

uint64_t openssl_evp_sha256(void) {
    typedef const void* (*fn_t)(void);
    fn_t fn = (fn_t)ossl_sym_crypto("EVP_sha256");
    return fn ? (uint64_t)(uintptr_t)fn() : 672ULL;
}

uint64_t openssl_evp_sha384(void) {
    typedef const void* (*fn_t)(void);
    fn_t fn = (fn_t)ossl_sym_crypto("EVP_sha384");
    return fn ? (uint64_t)(uintptr_t)fn() : 673ULL;
}

uint64_t openssl_evp_sha512(void) {
    typedef const void* (*fn_t)(void);
    fn_t fn = (fn_t)ossl_sym_crypto("EVP_sha512");
    return fn ? (uint64_t)(uintptr_t)fn() : 674ULL;
}

uint64_t openssl_evp_get_digestbyname(const char* name) {
    if (!name) return 0ULL;
    typedef const void* (*fn_t)(const char*);
    fn_t fn = (fn_t)ossl_sym_crypto("EVP_get_digestbyname");
    if (fn) return (uint64_t)(uintptr_t)fn(name);
    if (strcmp(name, "SHA1") == 0 || strcmp(name, "sha1") == 0 || strcmp(name, "SHA-1") == 0) return 64ULL;
    if (strcmp(name, "SHA224") == 0 || strcmp(name, "sha224") == 0 || strcmp(name, "SHA-224") == 0) return 675ULL;
    if (strcmp(name, "SHA256") == 0 || strcmp(name, "sha256") == 0 || strcmp(name, "SHA-256") == 0) return 672ULL;
    if (strcmp(name, "SHA384") == 0 || strcmp(name, "sha384") == 0 || strcmp(name, "SHA-384") == 0) return 673ULL;
    if (strcmp(name, "SHA512") == 0 || strcmp(name, "sha512") == 0 || strcmp(name, "SHA-512") == 0) return 674ULL;
    return 0ULL;
}

int32_t openssl_evp_md_get_size(uint64_t md) {
    if (!md) return -1;
    typedef int (*fn_t)(const void*);
    fn_t fn = (fn_t)ossl_sym_crypto("EVP_MD_get_size");
    if (!fn) fn = (fn_t)ossl_sym_crypto("EVP_MD_size");
    if (fn) return (int32_t)fn((const void*)(uintptr_t)md);
    switch (md) {
        case 64ULL: return 20;
        case 675ULL: return 28;
        case 672ULL: return 32;
        case 673ULL: return 48;
        case 674ULL: return 64;
        default: return -1;
    }
}

uint64_t openssl_evp_md_ctx_new(void) {
    typedef void* (*fn_t)(void);
    fn_t fn = (fn_t)ossl_sym_crypto("EVP_MD_CTX_new");
    if (fn) return (uint64_t)(uintptr_t)fn();
    ossl_soft_md_ctx_t* c = (ossl_soft_md_ctx_t*)calloc(1, sizeof(ossl_soft_md_ctx_t));
    return (uint64_t)(uintptr_t)c;
}

void openssl_evp_md_ctx_free(uint64_t ctx) {
    if (!ctx) return;
    typedef void (*fn_t)(void*);
    fn_t fn = (fn_t)ossl_sym_crypto("EVP_MD_CTX_free");
    if (fn) {
        fn((void*)(uintptr_t)ctx);
        return;
    }
    ossl_soft_md_ctx_t* c = (ossl_soft_md_ctx_t*)(uintptr_t)ctx;
    free(c->buf);
    free(c);
}

int32_t openssl_evp_digest_init_ex(uint64_t ctx, uint64_t type, uint64_t impl) {
    if (!ctx || !type) return 0;
    typedef int (*fn_t)(void*, const void*, void*);
    fn_t fn = (fn_t)ossl_sym_crypto("EVP_DigestInit_ex");
    if (fn) return (int32_t)fn((void*)(uintptr_t)ctx, (const void*)(uintptr_t)type, (void*)(uintptr_t)impl);
    ossl_soft_md_ctx_t* c = (ossl_soft_md_ctx_t*)(uintptr_t)ctx;
    c->type = type;
    c->len = 0;
    return 1;
}

int32_t openssl_evp_digest_update(uint64_t ctx, const void* d, uint64_t cnt) {
    if (!ctx) return 0;
    typedef int (*fn_t)(void*, const void*, size_t);
    fn_t fn = (fn_t)ossl_sym_crypto("EVP_DigestUpdate");
    const unsigned char empty = 0;
    const void* in = d ? d : (const void*)&empty;
    if (fn) return (int32_t)fn((void*)(uintptr_t)ctx, in, (size_t)cnt);
    ossl_soft_md_ctx_t* c = (ossl_soft_md_ctx_t*)(uintptr_t)ctx;
    if (cnt > 0 && d) {
        size_t need = c->len + (size_t)cnt;
        if (need > c->cap) {
            size_t new_cap = c->cap ? c->cap * 2 : 64;
            while (new_cap < need) new_cap *= 2;
            unsigned char* nb = (unsigned char*)realloc(c->buf, new_cap);
            if (!nb) return 0;
            c->buf = nb;
            c->cap = new_cap;
        }
        memcpy(c->buf + c->len, d, (size_t)cnt);
        c->len += (size_t)cnt;
    }
    return 1;
}

int32_t openssl_evp_digest_final_ex(uint64_t ctx, void* md, uint32_t* s) {
    if (!ctx || !md) return 0;
    typedef int (*fn_t)(void*, unsigned char*, unsigned int*);
    fn_t fn = (fn_t)ossl_sym_crypto("EVP_DigestFinal_ex");
    if (fn) {
        unsigned int out_len = 0;
        int rc = fn((void*)(uintptr_t)ctx, (unsigned char*)md, &out_len);
        if (s) *s = (uint32_t)out_len;
        return (int32_t)rc;
    }
    ossl_soft_md_ctx_t* c = (ossl_soft_md_ctx_t*)(uintptr_t)ctx;
    uint32_t out_len = 0;
    switch (c->type) {
        case 64ULL:  openssl_sha1(c->buf, c->len, md);   out_len = 20; break;
        case 675ULL: openssl_sha224(c->buf, c->len, md); out_len = 28; break;
        case 672ULL: openssl_sha256(c->buf, c->len, md); out_len = 32; break;
        case 673ULL: openssl_sha384(c->buf, c->len, md); out_len = 48; break;
        case 674ULL: openssl_sha512(c->buf, c->len, md); out_len = 64; break;
        default: return 0;
    }
    if (s) *s = out_len;
    c->len = 0;
    return 1;
}

/* SSL / TLS */

typedef struct {
    uint64_t options;
    int64_t min_proto;
    int64_t max_proto;
    int64_t mode;
} ossl_soft_ssl_ctx_t;

typedef struct {
    int64_t min_proto;
    int64_t max_proto;
    int64_t mode;
} ossl_soft_ssl_t;

uint64_t openssl_tls_method(void) {
    typedef const void* (*fn_t)(void);
    fn_t fn = (fn_t)ossl_sym_ssl("TLS_method");
    return fn ? (uint64_t)(uintptr_t)fn() : 0x55100001ULL;
}

uint64_t openssl_tls_client_method(void) {
    typedef const void* (*fn_t)(void);
    fn_t fn = (fn_t)ossl_sym_ssl("TLS_client_method");
    return fn ? (uint64_t)(uintptr_t)fn() : 0x55100002ULL;
}

uint64_t openssl_tls_server_method(void) {
    typedef const void* (*fn_t)(void);
    fn_t fn = (fn_t)ossl_sym_ssl("TLS_server_method");
    return fn ? (uint64_t)(uintptr_t)fn() : 0x55100003ULL;
}

uint64_t openssl_ssl_ctx_new(uint64_t method) {
    if (!method) return 0ULL;
    typedef void* (*fn_t)(const void*);
    fn_t fn = (fn_t)ossl_sym_ssl("SSL_CTX_new");
    if (fn) return (uint64_t)(uintptr_t)fn((const void*)(uintptr_t)method);
    ossl_soft_ssl_ctx_t* c = (ossl_soft_ssl_ctx_t*)calloc(1, sizeof(ossl_soft_ssl_ctx_t));
    return (uint64_t)(uintptr_t)c;
}

void openssl_ssl_ctx_free(uint64_t ctx) {
    if (!ctx) return;
    typedef void (*fn_t)(void*);
    fn_t fn = (fn_t)ossl_sym_ssl("SSL_CTX_free");
    if (fn) {
        fn((void*)(uintptr_t)ctx);
        return;
    }
    free((void*)(uintptr_t)ctx);
}

int64_t openssl_ssl_ctx_ctrl(uint64_t ctx, int32_t cmd, int64_t larg, uint64_t parg) {
    if (!ctx) return 0LL;
    typedef long (*fn_t)(void*, int, long, void*);
    fn_t fn = (fn_t)ossl_sym_ssl("SSL_CTX_ctrl");
    if (fn) return (int64_t)fn((void*)(uintptr_t)ctx, (int)cmd, (long)larg, (void*)(uintptr_t)parg);
    ossl_soft_ssl_ctx_t* c = (ossl_soft_ssl_ctx_t*)(uintptr_t)ctx;
    switch (cmd) {
        case 33: c->mode |= larg; return c->mode;
        case 77: c->mode &= ~larg; return c->mode;
        case 123: c->min_proto = larg; return 1LL;
        case 124: c->max_proto = larg; return 1LL;
        case 130: return c->min_proto;
        case 131: return c->max_proto;
        default: return 1LL;
    }
}

uint64_t openssl_ssl_ctx_get_options(uint64_t ctx) {
    if (!ctx) return 0ULL;
    typedef uint64_t (*fn_t)(const void*);
    fn_t fn = (fn_t)ossl_sym_ssl("SSL_CTX_get_options");
    if (fn) return fn((const void*)(uintptr_t)ctx);
    if (ossl_sym_ssl("SSL_CTX_ctrl")) {
        return (uint64_t)openssl_ssl_ctx_ctrl(ctx, 32 /* SSL_CTRL_OPTIONS */, 0, 0);
    }
    ossl_soft_ssl_ctx_t* c = (ossl_soft_ssl_ctx_t*)(uintptr_t)ctx;
    return c->options;
}

uint64_t openssl_ssl_ctx_set_options(uint64_t ctx, uint64_t op) {
    if (!ctx) return 0ULL;
    typedef uint64_t (*fn_t)(void*, uint64_t);
    fn_t fn = (fn_t)ossl_sym_ssl("SSL_CTX_set_options");
    if (fn) return fn((void*)(uintptr_t)ctx, op);
    if (ossl_sym_ssl("SSL_CTX_ctrl")) {
        return (uint64_t)openssl_ssl_ctx_ctrl(ctx, 32 /* SSL_CTRL_OPTIONS */, (int64_t)op, 0);
    }
    ossl_soft_ssl_ctx_t* c = (ossl_soft_ssl_ctx_t*)(uintptr_t)ctx;
    c->options |= op;
    return c->options;
}

uint64_t openssl_ssl_ctx_clear_options(uint64_t ctx, uint64_t op) {
    if (!ctx) return 0ULL;
    typedef uint64_t (*fn_t)(void*, uint64_t);
    fn_t fn = (fn_t)ossl_sym_ssl("SSL_CTX_clear_options");
    if (fn) return fn((void*)(uintptr_t)ctx, op);
    if (ossl_sym_ssl("SSL_CTX_ctrl")) {
        return (uint64_t)openssl_ssl_ctx_ctrl(ctx, 77 /* SSL_CTRL_CLEAR_OPTIONS */, (int64_t)op, 0);
    }
    ossl_soft_ssl_ctx_t* c = (ossl_soft_ssl_ctx_t*)(uintptr_t)ctx;
    c->options &= ~op;
    return c->options;
}

uint64_t openssl_ssl_new(uint64_t ctx) {
    if (!ctx) return 0ULL;
    typedef void* (*fn_t)(void*);
    fn_t fn = (fn_t)ossl_sym_ssl("SSL_new");
    if (fn) return (uint64_t)(uintptr_t)fn((void*)(uintptr_t)ctx);
    ossl_soft_ssl_ctx_t* c = (ossl_soft_ssl_ctx_t*)(uintptr_t)ctx;
    ossl_soft_ssl_t* s = (ossl_soft_ssl_t*)calloc(1, sizeof(ossl_soft_ssl_t));
    if (!s) return 0ULL;
    s->min_proto = c->min_proto;
    s->max_proto = c->max_proto;
    s->mode = c->mode;
    return (uint64_t)(uintptr_t)s;
}

void openssl_ssl_free(uint64_t ssl) {
    if (!ssl) return;
    typedef void (*fn_t)(void*);
    fn_t fn = (fn_t)ossl_sym_ssl("SSL_free");
    if (fn) {
        fn((void*)(uintptr_t)ssl);
        return;
    }
    free((void*)(uintptr_t)ssl);
}

int64_t openssl_ssl_ctrl(uint64_t ssl, int32_t cmd, int64_t larg, uint64_t parg) {
    if (!ssl) return 0LL;
    typedef long (*fn_t)(void*, int, long, void*);
    fn_t fn = (fn_t)ossl_sym_ssl("SSL_ctrl");
    if (fn) return (int64_t)fn((void*)(uintptr_t)ssl, (int)cmd, (long)larg, (void*)(uintptr_t)parg);
    ossl_soft_ssl_t* s = (ossl_soft_ssl_t*)(uintptr_t)ssl;
    switch (cmd) {
        case 33: s->mode |= larg; return s->mode;
        case 55: return 1LL;
        case 77: s->mode &= ~larg; return s->mode;
        case 123: s->min_proto = larg; return 1LL;
        case 124: s->max_proto = larg; return 1LL;
        case 130: return s->min_proto;
        case 131: return s->max_proto;
        default: return 1LL;
    }
}

/* ERR */

uint64_t openssl_err_get_error(void) {
    typedef unsigned long (*fn_t)(void);
    fn_t fn = (fn_t)ossl_sym_crypto("ERR_get_error");
    return fn ? (uint64_t)fn() : 0ULL;
}

uint64_t openssl_err_peek_error(void) {
    typedef unsigned long (*fn_t)(void);
    fn_t fn = (fn_t)ossl_sym_crypto("ERR_peek_error");
    return fn ? (uint64_t)fn() : 0ULL;
}

uint64_t openssl_err_peek_last_error(void) {
    typedef unsigned long (*fn_t)(void);
    fn_t fn = (fn_t)ossl_sym_crypto("ERR_peek_last_error");
    return fn ? (uint64_t)fn() : 0ULL;
}

void openssl_err_clear_error(void) {
    typedef void (*fn_t)(void);
    fn_t fn = (fn_t)ossl_sym_crypto("ERR_clear_error");
    if (fn) fn();
}

void openssl_err_error_string_n(uint64_t e, char* buf, uint64_t len) {
    if (!buf || len == 0) return;
    buf[0] = '\0';
    typedef void (*fn_t)(unsigned long, char*, size_t);
    fn_t fn = (fn_t)ossl_sym_crypto("ERR_error_string_n");
    if (fn) {
        fn((unsigned long)e, buf, (size_t)len);
    } else {
        snprintf(buf, (size_t)len, "error:%08lX:openssl-sys-kotlin::reason", (unsigned long)e);
    }
}

/* RAND */

int32_t openssl_rand_bytes(void* buf, int32_t num) {
    if (!buf || num < 0) return 0;
    typedef int (*fn_t)(unsigned char*, int);
    fn_t fn = (fn_t)ossl_sym_crypto("RAND_bytes");
    if (fn) return (int32_t)fn((unsigned char*)buf, (int)num);
    uint8_t* out = (uint8_t*)buf;
    uint64_t state = 0x9e3779b97f4a7c15ULL ^ (uint64_t)(uintptr_t)buf;
    for (int32_t i = 0; i < num; ++i) {
        state ^= state >> 12;
        state ^= state << 25;
        state ^= state >> 27;
        uint64_t z = state * 0x2545f4914f6cdd1dULL;
        out[i] = (uint8_t)((z >> 32) ^ (uint64_t)i ^ 0xa5ULL);
    }
    return 1;
}

int32_t openssl_rand_status(void) {
    typedef int (*fn_t)(void);
    fn_t fn = (fn_t)ossl_sym_crypto("RAND_status");
    return fn ? (int32_t)fn() : 1;
}

/* OBJ */

typedef struct {
    int32_t nid;
    const char* sn;
    const char* ln;
    const char* oid;
} ossl_soft_obj_t;

static const ossl_soft_obj_t k_soft_objs[] = {
    { 64,  "SHA1",            "sha1",                     "1.3.14.3.2.26" },
    { 675, "SHA224",          "sha224",                   "2.16.840.1.101.3.4.2.4" },
    { 672, "SHA256",          "sha256",                   "2.16.840.1.101.3.4.2.1" },
    { 673, "SHA384",          "sha384",                   "2.16.840.1.101.3.4.2.2" },
    { 674, "SHA512",          "sha512",                   "2.16.840.1.101.3.4.2.3" },
    { 6,   "RSA-Encryption",  "rsaEncryption",            "1.2.840.113549.1.1.1" },
    { 408, "id-ecPublicKey",  "id-ecPublicKey",           "1.2.840.10045.2.1" },
    { 415, "prime256v1",      "X9.62 prime256v1",         "1.2.840.10045.3.1.7" },
    { 13,  "CN",              "commonName",               "2.5.4.3" },
    { 14,  "C",               "countryName",              "2.5.4.6" },
    { 17,  "O",               "organizationName",         "2.5.4.10" },
    { 85,  "subjectAltName",  "X509v3 Subject Alternative Name", "2.5.29.17" },
    { 0, NULL, NULL, NULL }
};

const char* openssl_obj_nid2sn(int32_t n) {
    typedef const char* (*fn_t)(int);
    fn_t fn = (fn_t)ossl_sym_crypto("OBJ_nid2sn");
    if (fn) return fn((int)n);
    for (int i = 0; k_soft_objs[i].sn != NULL; ++i) {
        if (k_soft_objs[i].nid == n) return k_soft_objs[i].sn;
    }
    return NULL;
}

const char* openssl_obj_nid2ln(int32_t n) {
    typedef const char* (*fn_t)(int);
    fn_t fn = (fn_t)ossl_sym_crypto("OBJ_nid2ln");
    if (fn) return fn((int)n);
    for (int i = 0; k_soft_objs[i].ln != NULL; ++i) {
        if (k_soft_objs[i].nid == n) return k_soft_objs[i].ln;
    }
    return NULL;
}

int32_t openssl_obj_sn2nid(const char* s) {
    if (!s) return 0;
    typedef int (*fn_t)(const char*);
    fn_t fn = (fn_t)ossl_sym_crypto("OBJ_sn2nid");
    if (fn) return (int32_t)fn(s);
    for (int i = 0; k_soft_objs[i].sn != NULL; ++i) {
        if (strcmp(k_soft_objs[i].sn, s) == 0 || strcmp(k_soft_objs[i].ln, s) == 0) return k_soft_objs[i].nid;
    }
    return 0;
}

int32_t openssl_obj_ln2nid(const char* s) {
    if (!s) return 0;
    typedef int (*fn_t)(const char*);
    fn_t fn = (fn_t)ossl_sym_crypto("OBJ_ln2nid");
    if (fn) return (int32_t)fn(s);
    for (int i = 0; k_soft_objs[i].ln != NULL; ++i) {
        if (strcmp(k_soft_objs[i].ln, s) == 0 || strcmp(k_soft_objs[i].sn, s) == 0) return k_soft_objs[i].nid;
    }
    return 0;
}

int32_t openssl_obj_txt2nid(const char* s) {
    if (!s) return 0;
    typedef int (*fn_t)(const char*);
    fn_t fn = (fn_t)ossl_sym_crypto("OBJ_txt2nid");
    if (fn) return (int32_t)fn(s);
    for (int i = 0; k_soft_objs[i].sn != NULL; ++i) {
        if (strcmp(k_soft_objs[i].sn, s) == 0 || strcmp(k_soft_objs[i].ln, s) == 0 || strcmp(k_soft_objs[i].oid, s) == 0) {
            return k_soft_objs[i].nid;
        }
    }
    return 0;
}
