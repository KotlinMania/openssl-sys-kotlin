import groovy.json.JsonOutput
import java.security.MessageDigest
import java.util.Properties
import javax.xml.parsers.DocumentBuilderFactory
import org.gradle.api.artifacts.component.ModuleComponentIdentifier
import org.gradle.api.artifacts.component.ModuleComponentSelector
import org.gradle.api.artifacts.result.ResolvedDependencyResult
import org.gradle.api.tasks.testing.AbstractTestTask
import org.gradle.api.tasks.testing.Test
import org.w3c.dom.Element

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kmp)
}

providers.gradleProperty("consumerBuildRoot").orNull?.let { layout.buildDirectory.set(file(it)) }
val producerProperties = Properties().apply {
    file("../../gradle.properties").inputStream().use { load(it) }
}
val producerVersion = providers.gradleProperty("producerVersion")
    .getOrElse(producerProperties.getProperty("project.version", "0.1.2"))
val expectedVersions = mapOf(
    "openssl-sys-kotlin" to producerVersion,
)

kotlin {
    jvmToolchain(21)
    compilerOptions { allWarningsAsErrors.set(true) }
    android {
        namespace = "io.github.kotlinmania.opensslsys.publicationconsumer"
        compileSdk = providers.gradleProperty("consumerCompileSdk")
            .getOrElse(producerProperties.getProperty("android.compileSdk", "34"))
            .toInt()
        minSdk = producerProperties.getProperty("android.minSdk", "24").toInt()
        withHostTestBuilder { sourceSetTreeName = "test" }
    }
    sourceSets {
        commonMain.dependencies {
            expectedVersions.forEach { (module, version) ->
                implementation("io.github.kotlinmania:$module:$version")
            }
        }
        commonTest.dependencies { implementation(kotlin("test")) }
    }
}

tasks.withType<AbstractTestTask>().configureEach { outputs.upToDateWhen { false } }
val hostTests = tasks.withType<Test>().matching { it.name == "testAndroidHostTest" }

kotlin.targets.named("android") {
    compilations.named("main") {
        val compileDependencies = configurations.getByName(compileDependencyConfigurationName)
        val runtimeName = compileDependencyConfigurationName.replace("CompileClasspath", "RuntimeClasspath")
        val runtimeDependencies = configurations.named(runtimeName)
        val classpathReceipt = layout.buildDirectory.file("reports/publication/raw-classpaths.json")
        val writeClasspath = tasks.register("writeAndroidPublicationClasspath") {
            outputs.file(classpathReceipt)
            outputs.upToDateWhen { false }
            doLast {
                val artifactsByConfiguration = mapOf(
                    "compile" to compileDependencies,
                    "runtime" to runtimeDependencies.get(),
                ).mapValues { (scope, configuration) ->
                    val artifacts = configuration.incoming.artifacts.artifacts.map { it.file }.distinct()
                    check(artifacts.isNotEmpty() && artifacts.all { it.extension in setOf("aar", "jar") }) {
                        "Unsupported raw Android $scope classpath artifact layout: $artifacts"
                    }
                    artifacts.sortedBy { it.absolutePath }.map { it.absolutePath }
                }
                classpathReceipt.get().asFile.apply {
                    parentFile.mkdirs()
                    writeText(JsonOutput.prettyPrint(JsonOutput.toJson(artifactsByConfiguration)) + "\n")
                }
            }
        }
        val checkClasspath = tasks.register<Exec>("checkAndroidPublicationClasspath") {
            dependsOn(writeClasspath)
            mustRunAfter(hostTests)
            commandLine(
                "python3",
                file("../check_android_classpath.py"),
                classpathReceipt.get().asFile,
                "--configurations",
                "--receipt",
                layout.buildDirectory.file("reports/publication/duplicates.json").get().asFile,
            )
        }
        hostTests.configureEach { finalizedBy(checkClasspath) }
        tasks.register("verifyAndroidConsumer") {
            dependsOn(compileTaskProvider, hostTests, checkClasspath)
            doLast {
                val selections = mutableListOf<String>()
                compileDependencies.incoming.resolutionResult.rootComponent.get().dependencies
                    .filterIsInstance<ResolvedDependencyResult>().forEach { dependency ->
                        val requested = dependency.requested as? ModuleComponentSelector ?: return@forEach
                        val version = expectedVersions[requested.module] ?: return@forEach
                        val variant = dependency.resolvedVariant.externalVariant.orElse(dependency.resolvedVariant)
                        val owner = variant.owner as ModuleComponentIdentifier
                        val attributes = variant.attributes.keySet().associate {
                            it.name to variant.attributes.getAttribute(it).toString()
                        }
                        check(owner.group == "io.github.kotlinmania" && owner.module == "${requested.module}-android" && owner.version == version)
                        check(
                            attributes["org.jetbrains.kotlin.platform.type"] == "androidJvm" &&
                                attributes["org.gradle.libraryelements"] == "aar" &&
                                attributes["org.gradle.jvm.environment"] == "android",
                        )
                        selections += "${owner.group}:${owner.module}:${owner.version} $attributes"
                    }
                check(selections.size == expectedVersions.size) { "Not every root dependency selected an Android AAR" }
                val expected = setOf(
                    "publishedVersionAndMemoryLifecycle",
                    "publishedShaDigestsAndConstantTimeHelpers",
                    "publishedBioErrRandNidAndPublicTypes",
                )
                val executed = mutableSetOf<String>()
                check(hostTests.size == 1)
                val parser = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                hostTests.single().reports.junitXml.outputLocation.get().asFile.listFiles()
                    .orEmpty().filter { it.extension == "xml" }.forEach { xml ->
                        val cases = parser.parse(xml).getElementsByTagName("testcase")
                        for (index in 0 until cases.length) {
                            val case = cases.item(index) as Element
                            if (case.getAttribute("classname") == "consumer.PublicationConsumerTest") {
                                check(listOf("failure", "error", "skipped").none { case.getElementsByTagName(it).length > 0 })
                                check(executed.add(case.getAttribute("name")))
                            }
                        }
                    }
                check(executed == expected) { "Missing/extra/skipped consumer tests: $executed" }
                layout.buildDirectory.file("reports/publication/consumer.txt").get().asFile.apply {
                    parentFile.mkdirs()
                    writeText((selections + executed.sorted()).joinToString("\n", postfix = "\n"))
                }
            }
        }
    }
}
