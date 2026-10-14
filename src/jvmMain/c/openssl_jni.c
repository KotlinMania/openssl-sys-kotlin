#include "../../nativeInterop/cinterop/openssl_wrapper.c"
#include <jni.h>

/* Version & Initialization */

JNIEXPORT jlong JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_openSslVersionNum(
    JNIEnv* env, jclass clazz) {
    (void)env; (void)clazz;
    return (jlong)openssl_version_num();
}

JNIEXPORT jstring JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_openSslVersion(
    JNIEnv* env, jclass clazz, jint t) {
    (void)clazz;
    const char* s = openssl_version((int32_t)t);
    return s ? (*env)->NewStringUTF(env, s) : NULL;
}

JNIEXPORT jint JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_opensslInitCrypto(
    JNIEnv* env, jclass clazz, jlong opts, jlong settings) {
    (void)env; (void)clazz;
    return (jint)openssl_init_crypto((uint64_t)opts, (uint64_t)settings);
}

JNIEXPORT jint JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_opensslInitSsl(
    JNIEnv* env, jclass clazz, jlong opts, jlong settings) {
    (void)env; (void)clazz;
    return (jint)openssl_init_ssl((uint64_t)opts, (uint64_t)settings);
}

JNIEXPORT jlong JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_cryptoMalloc(
    JNIEnv* env, jclass clazz, jlong num, jstring file, jint line) {
    (void)clazz;
    const char* cfile = file ? (*env)->GetStringUTFChars(env, file, NULL) : NULL;
    uint64_t ptr = openssl_crypto_malloc((uint64_t)num, cfile, (int32_t)line);
    if (cfile) (*env)->ReleaseStringUTFChars(env, file, cfile);
    return (jlong)ptr;
}

JNIEXPORT void JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_cryptoFree(
    JNIEnv* env, jclass clazz, jlong addr, jstring file, jint line) {
    (void)clazz;
    const char* cfile = file ? (*env)->GetStringUTFChars(env, file, NULL) : NULL;
    openssl_crypto_free((uint64_t)addr, cfile, (int32_t)line);
    if (cfile) (*env)->ReleaseStringUTFChars(env, file, cfile);
}

/* SHA One-Shot Digests */

static jbyteArray jni_sha_digest(
    JNIEnv* env, jbyteArray data, jsize out_len,
    int32_t (*fn)(const void*, uint64_t, void*)) {
    if (!data) return NULL;
    jsize len = (*env)->GetArrayLength(env, data);
    jbyte* in_bytes = (*env)->GetByteArrayElements(env, data, NULL);
    unsigned char md[64];
    memset(md, 0, sizeof(md));
    int32_t ok = fn(in_bytes, (uint64_t)len, md);
    if (in_bytes) (*env)->ReleaseByteArrayElements(env, data, in_bytes, JNI_ABORT);
    if (!ok) return NULL;
    jbyteArray out = (*env)->NewByteArray(env, out_len);
    if (out) (*env)->SetByteArrayRegion(env, out, 0, out_len, (const jbyte*)md);
    return out;
}

JNIEXPORT jbyteArray JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_sha1(
    JNIEnv* env, jclass clazz, jbyteArray data) {
    (void)clazz;
    return jni_sha_digest(env, data, 20, openssl_sha1);
}

JNIEXPORT jbyteArray JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_sha224(
    JNIEnv* env, jclass clazz, jbyteArray data) {
    (void)clazz;
    return jni_sha_digest(env, data, 28, openssl_sha224);
}

JNIEXPORT jbyteArray JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_sha256(
    JNIEnv* env, jclass clazz, jbyteArray data) {
    (void)clazz;
    return jni_sha_digest(env, data, 32, openssl_sha256);
}

JNIEXPORT jbyteArray JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_sha384(
    JNIEnv* env, jclass clazz, jbyteArray data) {
    (void)clazz;
    return jni_sha_digest(env, data, 48, openssl_sha384);
}

JNIEXPORT jbyteArray JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_sha512(
    JNIEnv* env, jclass clazz, jbyteArray data) {
    (void)clazz;
    return jni_sha_digest(env, data, 64, openssl_sha512);
}

/* BIO */

JNIEXPORT jlong JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_bioSMem(
    JNIEnv* env, jclass clazz) {
    (void)env; (void)clazz;
    return (jlong)openssl_bio_s_mem();
}

JNIEXPORT jlong JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_bioNew(
    JNIEnv* env, jclass clazz, jlong method) {
    (void)env; (void)clazz;
    return (jlong)openssl_bio_new((uint64_t)method);
}

JNIEXPORT void JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_bioFreeAll(
    JNIEnv* env, jclass clazz, jlong bio) {
    (void)env; (void)clazz;
    openssl_bio_free_all((uint64_t)bio);
}

JNIEXPORT jint JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_bioRead(
    JNIEnv* env, jclass clazz, jlong bio, jbyteArray buf, jint len) {
    (void)clazz;
    if (!buf || len <= 0) return -1;
    jsize arr_len = (*env)->GetArrayLength(env, buf);
    jint actual_len = len < arr_len ? len : arr_len;
    jbyte* tmp = (jbyte*)malloc((size_t)actual_len);
    if (!tmp) return -1;
    int32_t rc = openssl_bio_read((uint64_t)bio, tmp, (int32_t)actual_len);
    if (rc > 0) {
        (*env)->SetByteArrayRegion(env, buf, 0, (jsize)rc, tmp);
    }
    free(tmp);
    return (jint)rc;
}

JNIEXPORT jint JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_bioWrite(
    JNIEnv* env, jclass clazz, jlong bio, jbyteArray buf, jint len) {
    (void)clazz;
    if (!buf || len <= 0) return -1;
    jsize arr_len = (*env)->GetArrayLength(env, buf);
    jint actual_len = len < arr_len ? len : arr_len;
    jbyte* tmp = (jbyte*)malloc((size_t)actual_len);
    if (!tmp) return -1;
    (*env)->GetByteArrayRegion(env, buf, 0, actual_len, tmp);
    int32_t rc = openssl_bio_write((uint64_t)bio, tmp, (int32_t)actual_len);
    free(tmp);
    return (jint)rc;
}

JNIEXPORT jlong JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_bioCtrl(
    JNIEnv* env, jclass clazz, jlong bio, jint cmd, jlong larg, jlong parg) {
    (void)env; (void)clazz;
    return (jlong)openssl_bio_ctrl((uint64_t)bio, (int32_t)cmd, (int64_t)larg, (uint64_t)parg);
}

JNIEXPORT jbyteArray JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_bioGetMemData(
    JNIEnv* env, jclass clazz, jlong bio) {
    (void)clazz;
    uint64_t ptr = 0ULL;
    int64_t len = openssl_bio_get_mem_data_ptr((uint64_t)bio, &ptr);
    if (len <= 0 || !ptr) {
        return (*env)->NewByteArray(env, 0);
    }
    jbyteArray out = (*env)->NewByteArray(env, (jsize)len);
    if (out) {
        (*env)->SetByteArrayRegion(env, out, 0, (jsize)len, (const jbyte*)(uintptr_t)ptr);
    }
    return out;
}

/* EVP Digest */

JNIEXPORT jlong JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_evpSha1(
    JNIEnv* env, jclass clazz) {
    (void)env; (void)clazz;
    return (jlong)openssl_evp_sha1();
}

JNIEXPORT jlong JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_evpSha224(
    JNIEnv* env, jclass clazz) {
    (void)env; (void)clazz;
    return (jlong)openssl_evp_sha224();
}

JNIEXPORT jlong JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_evpSha256(
    JNIEnv* env, jclass clazz) {
    (void)env; (void)clazz;
    return (jlong)openssl_evp_sha256();
}

JNIEXPORT jlong JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_evpSha384(
    JNIEnv* env, jclass clazz) {
    (void)env; (void)clazz;
    return (jlong)openssl_evp_sha384();
}

JNIEXPORT jlong JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_evpSha512(
    JNIEnv* env, jclass clazz) {
    (void)env; (void)clazz;
    return (jlong)openssl_evp_sha512();
}

JNIEXPORT jlong JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_evpGetDigestbyname(
    JNIEnv* env, jclass clazz, jstring name) {
    (void)clazz;
    if (!name) return 0L;
    const char* cname = (*env)->GetStringUTFChars(env, name, NULL);
    if (!cname) return 0L;
    uint64_t md = openssl_evp_get_digestbyname(cname);
    (*env)->ReleaseStringUTFChars(env, name, cname);
    return (jlong)md;
}

JNIEXPORT jint JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_evpMdGetSize(
    JNIEnv* env, jclass clazz, jlong md) {
    (void)env; (void)clazz;
    return (jint)openssl_evp_md_get_size((uint64_t)md);
}

JNIEXPORT jlong JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_evpMdCtxNew(
    JNIEnv* env, jclass clazz) {
    (void)env; (void)clazz;
    return (jlong)openssl_evp_md_ctx_new();
}

JNIEXPORT void JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_evpMdCtxFree(
    JNIEnv* env, jclass clazz, jlong ctx) {
    (void)env; (void)clazz;
    openssl_evp_md_ctx_free((uint64_t)ctx);
}

JNIEXPORT jint JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_evpDigestInitEx(
    JNIEnv* env, jclass clazz, jlong ctx, jlong type, jlong impl) {
    (void)env; (void)clazz;
    return (jint)openssl_evp_digest_init_ex((uint64_t)ctx, (uint64_t)type, (uint64_t)impl);
}

JNIEXPORT jint JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_evpDigestUpdate(
    JNIEnv* env, jclass clazz, jlong ctx, jbyteArray data) {
    (void)clazz;
    if (!data) return 0;
    jsize len = (*env)->GetArrayLength(env, data);
    jbyte* bytes = (*env)->GetByteArrayElements(env, data, NULL);
    int32_t rc = openssl_evp_digest_update((uint64_t)ctx, bytes, (uint64_t)len);
    if (bytes) (*env)->ReleaseByteArrayElements(env, data, bytes, JNI_ABORT);
    return (jint)rc;
}

JNIEXPORT jint JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_evpDigestFinalEx(
    JNIEnv* env, jclass clazz, jlong ctx, jbyteArray md, jintArray outLen) {
    (void)clazz;
    if (!md) return 0;
    unsigned char buf[64];
    memset(buf, 0, sizeof(buf));
    uint32_t s = 0;
    int32_t rc = openssl_evp_digest_final_ex((uint64_t)ctx, buf, &s);
    if (rc == 1 && s > 0) {
        jsize arr_len = (*env)->GetArrayLength(env, md);
        jsize copy_len = (jsize)s < arr_len ? (jsize)s : arr_len;
        (*env)->SetByteArrayRegion(env, md, 0, copy_len, (const jbyte*)buf);
        if (outLen && (*env)->GetArrayLength(env, outLen) > 0) {
            jint len_val = (jint)s;
            (*env)->SetIntArrayRegion(env, outLen, 0, 1, &len_val);
        }
    }
    return (jint)rc;
}

/* SSL / TLS */

JNIEXPORT jlong JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_tlsMethod(
    JNIEnv* env, jclass clazz) {
    (void)env; (void)clazz;
    return (jlong)openssl_tls_method();
}

JNIEXPORT jlong JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_tlsClientMethod(
    JNIEnv* env, jclass clazz) {
    (void)env; (void)clazz;
    return (jlong)openssl_tls_client_method();
}

JNIEXPORT jlong JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_tlsServerMethod(
    JNIEnv* env, jclass clazz) {
    (void)env; (void)clazz;
    return (jlong)openssl_tls_server_method();
}

JNIEXPORT jlong JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_sslCtxNew(
    JNIEnv* env, jclass clazz, jlong method) {
    (void)env; (void)clazz;
    return (jlong)openssl_ssl_ctx_new((uint64_t)method);
}

JNIEXPORT void JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_sslCtxFree(
    JNIEnv* env, jclass clazz, jlong ctx) {
    (void)env; (void)clazz;
    openssl_ssl_ctx_free((uint64_t)ctx);
}

JNIEXPORT jlong JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_sslCtxCtrl(
    JNIEnv* env, jclass clazz, jlong ctx, jint cmd, jlong larg, jlong parg) {
    (void)env; (void)clazz;
    return (jlong)openssl_ssl_ctx_ctrl((uint64_t)ctx, (int32_t)cmd, (int64_t)larg, (uint64_t)parg);
}

JNIEXPORT jlong JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_sslCtxGetOptions(
    JNIEnv* env, jclass clazz, jlong ctx) {
    (void)env; (void)clazz;
    return (jlong)openssl_ssl_ctx_get_options((uint64_t)ctx);
}

JNIEXPORT jlong JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_sslCtxSetOptions(
    JNIEnv* env, jclass clazz, jlong ctx, jlong op) {
    (void)env; (void)clazz;
    return (jlong)openssl_ssl_ctx_set_options((uint64_t)ctx, (uint64_t)op);
}

JNIEXPORT jlong JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_sslCtxClearOptions(
    JNIEnv* env, jclass clazz, jlong ctx, jlong op) {
    (void)env; (void)clazz;
    return (jlong)openssl_ssl_ctx_clear_options((uint64_t)ctx, (uint64_t)op);
}

JNIEXPORT jlong JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_sslNew(
    JNIEnv* env, jclass clazz, jlong ctx) {
    (void)env; (void)clazz;
    return (jlong)openssl_ssl_new((uint64_t)ctx);
}

JNIEXPORT void JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_sslFree(
    JNIEnv* env, jclass clazz, jlong ssl) {
    (void)env; (void)clazz;
    openssl_ssl_free((uint64_t)ssl);
}

JNIEXPORT jlong JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_sslCtrl(
    JNIEnv* env, jclass clazz, jlong ssl, jint cmd, jlong larg, jlong parg) {
    (void)env; (void)clazz;
    return (jlong)openssl_ssl_ctrl((uint64_t)ssl, (int32_t)cmd, (int64_t)larg, (uint64_t)parg);
}

/* ERR */

JNIEXPORT jlong JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_errGetError(
    JNIEnv* env, jclass clazz) {
    (void)env; (void)clazz;
    return (jlong)openssl_err_get_error();
}

JNIEXPORT jlong JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_errPeekError(
    JNIEnv* env, jclass clazz) {
    (void)env; (void)clazz;
    return (jlong)openssl_err_peek_error();
}

JNIEXPORT jlong JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_errPeekLastError(
    JNIEnv* env, jclass clazz) {
    (void)env; (void)clazz;
    return (jlong)openssl_err_peek_last_error();
}

JNIEXPORT void JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_errClearError(
    JNIEnv* env, jclass clazz) {
    (void)env; (void)clazz;
    openssl_err_clear_error();
}

JNIEXPORT jstring JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_errErrorStringN(
    JNIEnv* env, jclass clazz, jlong e, jint maxLen) {
    (void)clazz;
    size_t len = (maxLen > 0 && maxLen < 4096) ? (size_t)maxLen : 256;
    char* buf = (char*)calloc(len + 1, 1);
    if (!buf) return (*env)->NewStringUTF(env, "");
    openssl_err_error_string_n((uint64_t)e, buf, (uint64_t)len);
    buf[len] = '\0';
    jstring res = (*env)->NewStringUTF(env, buf);
    free(buf);
    return res;
}

/* RAND */

JNIEXPORT jint JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_randBytes(
    JNIEnv* env, jclass clazz, jbyteArray buf, jint num) {
    (void)clazz;
    if (!buf || num < 0) return 0;
    jsize arr_len = (*env)->GetArrayLength(env, buf);
    jint actual = num < arr_len ? num : arr_len;
    if (actual == 0) return 1;
    unsigned char* tmp = (unsigned char*)malloc((size_t)actual);
    if (!tmp) return 0;
    int32_t rc = openssl_rand_bytes(tmp, (int32_t)actual);
    if (rc == 1) {
        (*env)->SetByteArrayRegion(env, buf, 0, (jsize)actual, (const jbyte*)tmp);
    }
    free(tmp);
    return (jint)rc;
}

JNIEXPORT jint JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_randStatus(
    JNIEnv* env, jclass clazz) {
    (void)env; (void)clazz;
    return (jint)openssl_rand_status();
}

/* OBJ */

JNIEXPORT jstring JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_objNid2sn(
    JNIEnv* env, jclass clazz, jint n) {
    (void)clazz;
    const char* s = openssl_obj_nid2sn((int32_t)n);
    return s ? (*env)->NewStringUTF(env, s) : NULL;
}

JNIEXPORT jstring JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_objNid2ln(
    JNIEnv* env, jclass clazz, jint n) {
    (void)clazz;
    const char* s = openssl_obj_nid2ln((int32_t)n);
    return s ? (*env)->NewStringUTF(env, s) : NULL;
}

JNIEXPORT jint JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_objSn2nid(
    JNIEnv* env, jclass clazz, jstring s) {
    (void)clazz;
    if (!s) return 0;
    const char* cs = (*env)->GetStringUTFChars(env, s, NULL);
    if (!cs) return 0;
    int32_t nid = openssl_obj_sn2nid(cs);
    (*env)->ReleaseStringUTFChars(env, s, cs);
    return (jint)nid;
}

JNIEXPORT jint JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_objLn2nid(
    JNIEnv* env, jclass clazz, jstring s) {
    (void)clazz;
    if (!s) return 0;
    const char* cs = (*env)->GetStringUTFChars(env, s, NULL);
    if (!cs) return 0;
    int32_t nid = openssl_obj_ln2nid(cs);
    (*env)->ReleaseStringUTFChars(env, s, cs);
    return (jint)nid;
}

JNIEXPORT jint JNICALL
Java_io_github_kotlinmania_opensslsys_internal_OpensslJni_objTxt2nid(
    JNIEnv* env, jclass clazz, jstring s) {
    (void)clazz;
    if (!s) return 0;
    const char* cs = (*env)->GetStringUTFChars(env, s, NULL);
    if (!cs) return 0;
    int32_t nid = openssl_obj_txt2nid(cs);
    (*env)->ReleaseStringUTFChars(env, s, cs);
    return (jint)nid;
}
