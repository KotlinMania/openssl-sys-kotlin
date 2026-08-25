# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 27/68 (39.7%)
- **Function parity:** 0/55 matched (target 16) — 0.0%
- **Class/type parity:** 3/110 matched (target 5) — 2.7%
- **Combined symbol parity:** 3/165 matched (target 21) — 1.8%
- **Average inline-code cosine:** 0.56 (function body across 27 matched files)
- **Average documentation cosine:** 0.00 (doc text across 27 matched files)
- **Cheat-zeroed Files:** 12
- **Critical Issues:** 12 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. ssl

- **Target:** `opensslsys.Ssl [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 222210.0
- **Functions:** 0/22 matched (target 0)
- **Missing functions:** `SSL_CTX_set_mode`, `SSL_set_mtu`, `SSL_get_extms_support`, `SSL_CTX_set_tmp_dh`, `SSL_CTX_set_tmp_ecdh`, `SSL_set_tmp_dh`, `SSL_set_tmp_ecdh`, `SSL_CTX_set_dh_auto`, `SSL_set_dh_auto`, `SSL_CTX_add_extra_chain_cert`, `SSL_CTX_get_extra_chain_certs`, `SSL_CTX_set0_verify_cert_store`, `SSL_set0_verify_cert_store`, `SSL_add0_chain_cert`, `SSL_CTX_set1_sigalgs_list`, `SSL_CTX_set_ecdh_auto`, `SSL_set_ecdh_auto`, `SSL_CTX_sess_set_cache_size`, `SSL_CTX_sess_get_cache_size`, `SSL_CTX_set_session_cache_mode`, `SSL_CTX_set_read_ahead`, `SSL_session_reused`
- **Types:** 0/0 matched
- **Missing types:** _none_

### 2. evp

- **Target:** `opensslsys.Evp [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 141410.0
- **Functions:** 0/14 matched (target 0)
- **Missing functions:** `EVP_get_digestbynid`, `EVP_DigestSignUpdate`, `EVP_DigestVerifyUpdate`, `EVP_PKEY_size`, `EVP_PKEY_CTX_set_hkdf_mode`, `EVP_PKEY_CTX_set_hkdf_md`, `EVP_PKEY_CTX_set1_hkdf_salt`, `EVP_PKEY_CTX_set1_hkdf_key`, `EVP_PKEY_CTX_add1_hkdf_info`, `EVP_PKEY_CTX_set_signature_md`, `EVP_PKEY_assign_RSA`, `EVP_PKEY_assign_DSA`, `EVP_PKEY_assign_DH`, `EVP_PKEY_assign_EC_KEY`
- **Types:** 0/0 matched
- **Missing types:** _none_

### 3. tls1

- **Target:** `opensslsys.Tls1 [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 90910.0
- **Functions:** 0/9 matched (target 0)
- **Missing functions:** `SSL_set_tlsext_host_name`, `SSL_set_tlsext_status_type`, `SSL_get_tlsext_status_ocsp_resp`, `SSL_set_tlsext_status_ocsp_resp`, `SSL_CTX_set_tlsext_servername_callback`, `SSL_CTX_set_tlsext_servername_callback__fixed_rust`, `SSL_CTX_set_tlsext_servername_arg`, `SSL_CTX_set_tlsext_status_cb`, `SSL_CTX_set_tlsext_status_arg`
- **Types:** 0/0 matched
- **Missing types:** _none_

### 4. crypto

- **Target:** `opensslsys.Crypto [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 50510.0
- **Functions:** 0/2 matched (target 0)
- **Missing functions:** `OPENSSL_malloc`, `OPENSSL_free`
- **Types:** 0/3 matched (target 0)
- **Missing types:** `CRYPTO_EX_new`, `CRYPTO_EX_dup`, `CRYPTO_EX_free`

### 5. bio

- **Target:** `opensslsys.Bio [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 40410.0
- **Functions:** 0/4 matched (target 0)
- **Missing functions:** `BIO_set_retry_read`, `BIO_set_retry_write`, `BIO_clear_retry_flags`, `BIO_get_mem_data`
- **Types:** 0/0 matched
- **Missing types:** _none_

### 6. rsa

- **Target:** `opensslsys.Rsa [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 20210.0
- **Functions:** 0/2 matched (target 0)
- **Missing functions:** `EVP_PKEY_CTX_set_rsa_oaep_md`, `EVP_PKEY_CTX_set0_rsa_oaep_label`
- **Types:** 0/0 matched
- **Missing types:** _none_

### 7. x509_vfy

- **Target:** `opensslsys.X509Vfy [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10110.0
- **Functions:** 0/1 matched (target 0)
- **Missing functions:** `X509_LOOKUP_add_dir`
- **Types:** 0/0 matched
- **Missing types:** _none_

### 8. ec

- **Target:** `opensslsys.Ec [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10110.0
- **Functions:** 0/1 matched (target 0)
- **Missing functions:** `EVP_EC_gen`
- **Types:** 0/0 matched
- **Missing types:** _none_

### 9. x509v3

- **Target:** `opensslsys.X509v3 [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10110.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/1 matched (target 0)
- **Missing types:** `GENERAL_NAME`

### 10. sha

- **Target:** `opensslsys.Sha [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 210.0
- **Functions:** 0/0 matched (target 11)
- **Missing functions:** _none_
- **Types:** 2/2 matched
- **Missing types:** _none_

### 11. bn

- **Target:** `opensslsys.Bn`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 100.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 1/1 matched
- **Missing types:** _none_

### 12. obj_mac

- **Target:** `opensslsys.ObjMac [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

### 13. err

- **Target:** `opensslsys.Err [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 5)
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

### 14. x509

- **Target:** `opensslsys.X509`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 0.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

### 15. asn1

- **Target:** `opensslsys.Asn1`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 0.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

### 16. aes

- **Target:** `opensslsys.Aes`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 0.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

### 17. ssl3

- **Target:** `opensslsys.Ssl3`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 0.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

### 18. srtp

- **Target:** `opensslsys.Srtp`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 0.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

### 19. ocsp

- **Target:** `opensslsys.Ocsp`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 0.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

### 20. types

- **Target:** `opensslsys.Types`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 0.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 2)
- **Missing types:** _none_

### 21. pkcs7

- **Target:** `opensslsys.Pkcs7`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 0.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

### 22. dtls1

- **Target:** `opensslsys.Dtls1`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 0.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

### 23. pem

- **Target:** `opensslsys.Pem`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 0.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

### 24. dsa

- **Target:** `opensslsys.Dsa`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 0.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

### 25. cms

- **Target:** `opensslsys.Cms`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 0.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

### 26. dh

- **Target:** `opensslsys.Dh`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 0.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

### 27. core_dispatch

- **Target:** `opensslsys.CoreDispatch`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 0.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

## Reexport / Wiring Modules

These files match `reexport_modules` patterns in `.ast_distance_config.json`. They are filtered out of
normal priority and missing-file ladders because they are wiring
modules, not direct logic ports. Consult them for call-site routing;
do not treat them as the next implementation target by default.

### Missing

| Source | Expected target | Deps | Source path | Expected path |
|--------|-----------------|------|-------------|---------------|
| `handwritten.mod` | `handwritten.Mod` | 0 | `handwritten/mod.rs` | `handwritten/Mod.kt` |
| `lib` | `Lib` | 0 | `lib.rs` | `Lib.kt` |

