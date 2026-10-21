pluginManagement {
    repositories { google(); mavenCentral(); gradlePluginPortal() }
}
plugins { id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0" }
dependencyResolutionManagement {
    repositories {
        // An explicit producer staging repository is allowed; never mavenLocal or source substitution.
        val stagingLocation = providers.gradleProperty("producerRepository").orNull
            ?: file("../../build/staging-deploy").takeIf { it.isDirectory }?.absolutePath
        stagingLocation?.let { location ->
            exclusiveContent {
                forRepository { maven { url = uri(location) } }
                filter {
                    includeModule("io.github.kotlinmania", "openssl-sys-kotlin")
                    includeModule("io.github.kotlinmania", "openssl-sys-kotlin-android")
                    includeModule("io.github.kotlinmania", "openssl-sys-kotlin-jvm")
                    includeModule("io.github.kotlinmania", "openssl-sys-kotlin-macosarm64")
                    includeModule("io.github.kotlinmania", "openssl-sys-kotlin-js")
                    includeModule("io.github.kotlinmania", "openssl-sys-kotlin-wasm-js")
                    includeModule("io.github.kotlinmania", "openssl-sys-kotlin-wasm-wasi")
                }
            }
        }
        google()
        mavenCentral()
    }
    versionCatalogs { create("libs") { from(files("../../gradle/libs.versions.toml")) } }
}
rootProject.name = "openssl-sys-publication-consumer"
