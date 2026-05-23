{
  "targets": [
    {
      "target_name": "openssl_shim",
      "sources": [
        "src/openssl_shim.cc"
      ],
      "include_dirs": [
        "<!@(node -p \"require('node-addon-api').include\")"
      ],
      "dependencies": [
        "<!(node -p \"require('node-addon-api').gyp\")"
      ],
      "defines": [ "NAPI_DISABLE_CPP_EXCEPTIONS" ],
      "cflags_cc": [ "-std=gnu++17", "-fexceptions" ],
      "cflags!": [ "-fno-exceptions" ],
      "cflags_cc!": [ "-fno-exceptions" ],
      "conditions": [
        ["OS=='mac'", {
          "include_dirs+": [
            "/opt/homebrew/opt/openssl@3/include",
            "/usr/local/opt/openssl@3/include"
          ],
          "libraries": [
            "-L/opt/homebrew/opt/openssl@3/lib",
            "-L/usr/local/opt/openssl@3/lib",
            "-lssl",
            "-lcrypto"
          ],
          "xcode_settings": {
            "GCC_ENABLE_CPP_EXCEPTIONS": "YES",
            "CLANG_CXX_LIBRARY": "libc++",
            "CLANG_CXX_LANGUAGE_STANDARD": "gnu++17",
            "MACOSX_DEPLOYMENT_TARGET": "13.0",
            "OTHER_LDFLAGS": [
              "-L/opt/homebrew/opt/openssl@3/lib",
              "-L/usr/local/opt/openssl@3/lib"
            ]
          }
        }],
        ["OS=='linux'", {
          "libraries": [ "-lssl", "-lcrypto" ]
        }],
        ["OS=='win'", {
          "libraries": [ "ssl.lib", "crypto.lib" ]
        }]
      ]
    }
  ]
}
