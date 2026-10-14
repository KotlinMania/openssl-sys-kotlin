import Testing
import OpensslSys

@Suite("OpensslSys Export Tests")
struct OpensslSysExportTests {
    @Test("OpenSSL version and initialization")
    func testVersionAndInit() throws {
        OpensslSys.opensslInit()
        let verNum = OpensslSys.openSslVersionNum()
        #expect(verNum >= 0x10101000)
        let verStr = OpensslSys.openSslVersion(key: OpensslSys.OPENSSL_VERSION)
        #expect(verStr != nil)
        #expect(verStr?.isEmpty == false)
    }

    @Test("OpenSSL CRYPTO_malloc, ERR_PACK, RAND_status, and OBJ_sn2nid")
    func testCryptoErrRandObj() throws {
        let ptr = OpensslSys.opensslMalloc(num: UInt64(64))
        #expect(ptr != 0)
        OpensslSys.opensslFree(addr: ptr)

        let packed = OpensslSys.errPack(lib: OpensslSys.ERR_LIB_PEM, function: 0, reason: 0x42)
        #expect(OpensslSys.errGetLib(errcode: packed) == OpensslSys.ERR_LIB_PEM)
        #expect(OpensslSys.errGetReason(errcode: packed) == 0x42)

        #expect(OpensslSys.randStatus() == 1)
        #expect(OpensslSys.objSn2nid(sn: "SHA256") == OpensslSys.NID_sha256)
    }
}
