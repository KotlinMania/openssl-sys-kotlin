#ifndef OPENSSL_WRAPPER_H
#define OPENSSL_WRAPPER_H

#include <stdint.h>
#include <stddef.h>

#ifdef __cplusplus
extern "C" {
#endif

/* Version & Initialization (init.rs, crypto.rs) */
uint64_t openssl_version_num(void);
const char* openssl_version(int32_t t);
int32_t openssl_init_crypto(uint64_t opts, uint64_t settings);
int32_t openssl_init_ssl(uint64_t opts, uint64_t settings);
uint64_t openssl_crypto_malloc(uint64_t num, const char* file, int32_t line);
void openssl_crypto_free(uint64_t addr, const char* file, int32_t line);

/* SHA One-Shot Digests (sha.rs) */
int32_t openssl_sha1(const void* d, uint64_t n, void* md);
int32_t openssl_sha224(const void* d, uint64_t n, void* md);
int32_t openssl_sha256(const void* d, uint64_t n, void* md);
int32_t openssl_sha384(const void* d, uint64_t n, void* md);
int32_t openssl_sha512(const void* d, uint64_t n, void* md);

/* BIO (bio.rs) */
uint64_t openssl_bio_s_mem(void);
uint64_t openssl_bio_new(uint64_t method);
void openssl_bio_free_all(uint64_t bio);
int32_t openssl_bio_read(uint64_t bio, void* data, int32_t len);
int32_t openssl_bio_write(uint64_t bio, const void* data, int32_t len);
int64_t openssl_bio_ctrl(uint64_t bio, int32_t cmd, int64_t larg, uint64_t parg);
int64_t openssl_bio_get_mem_data_ptr(uint64_t bio, uint64_t* out_ptr);

/* EVP Digest (evp.rs) */
uint64_t openssl_evp_sha1(void);
uint64_t openssl_evp_sha224(void);
uint64_t openssl_evp_sha256(void);
uint64_t openssl_evp_sha384(void);
uint64_t openssl_evp_sha512(void);
uint64_t openssl_evp_get_digestbyname(const char* name);
int32_t openssl_evp_md_get_size(uint64_t md);
uint64_t openssl_evp_md_ctx_new(void);
void openssl_evp_md_ctx_free(uint64_t ctx);
int32_t openssl_evp_digest_init_ex(uint64_t ctx, uint64_t type, uint64_t impl);
int32_t openssl_evp_digest_update(uint64_t ctx, const void* d, uint64_t cnt);
int32_t openssl_evp_digest_final_ex(uint64_t ctx, void* md, uint32_t* s);

/* SSL / TLS (ssl.rs, tls1.rs) */
uint64_t openssl_tls_method(void);
uint64_t openssl_tls_client_method(void);
uint64_t openssl_tls_server_method(void);
uint64_t openssl_ssl_ctx_new(uint64_t method);
void openssl_ssl_ctx_free(uint64_t ctx);
int64_t openssl_ssl_ctx_ctrl(uint64_t ctx, int32_t cmd, int64_t larg, uint64_t parg);
uint64_t openssl_ssl_ctx_get_options(uint64_t ctx);
uint64_t openssl_ssl_ctx_set_options(uint64_t ctx, uint64_t op);
uint64_t openssl_ssl_ctx_clear_options(uint64_t ctx, uint64_t op);
uint64_t openssl_ssl_new(uint64_t ctx);
void openssl_ssl_free(uint64_t ssl);
int64_t openssl_ssl_ctrl(uint64_t ssl, int32_t cmd, int64_t larg, uint64_t parg);

/* ERR (err.rs) */
uint64_t openssl_err_get_error(void);
uint64_t openssl_err_peek_error(void);
uint64_t openssl_err_peek_last_error(void);
void openssl_err_clear_error(void);
void openssl_err_error_string_n(uint64_t e, char* buf, uint64_t len);

/* RAND (rand.rs) */
int32_t openssl_rand_bytes(void* buf, int32_t num);
int32_t openssl_rand_status(void);

/* OBJ (object.rs) */
const char* openssl_obj_nid2sn(int32_t n);
const char* openssl_obj_nid2ln(int32_t n);
int32_t openssl_obj_sn2nid(const char* s);
int32_t openssl_obj_ln2nid(const char* s);
int32_t openssl_obj_txt2nid(const char* s);

#ifdef __cplusplus
}
#endif

#endif /* OPENSSL_WRAPPER_H */
