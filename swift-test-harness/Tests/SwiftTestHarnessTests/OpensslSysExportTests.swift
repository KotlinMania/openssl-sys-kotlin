import Testing
import OpensslSys

@Suite("OpensslSys Export Tests")
struct OpensslSysExportTests {
    @Test("OpenSSL version and initialization")
    func testVersionAndInit() throws {
        OpensslSys.opensslInit()
        OpensslSys.assumeInit()
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

    @Test("OpenSSL types, stacks, and EVP/SSL helpers")
    func testTypesAndHelpers() throws {
        let md = OpensslSys.evpGetDigestbynid(type: OpensslSys.NID_sha256)
        #expect(md != nil)
        if let md {
            #expect(OpensslSys.evpMdSize(md: md) == 32)
        }
        let x509Stack = OpensslSys.StackStX509(address: 123)
        #expect(x509Stack.address == 123)
        let ecKey = OpensslSys.evpEcGen(curve: "prime256v1")
        #expect(ecKey != nil)
    }
}
