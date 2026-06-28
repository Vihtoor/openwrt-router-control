import java.net.URL
import java.net.HttpURLConnection

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.google.devtools.ksp)
  alias(libs.plugins.roborazzi)
  alias(libs.plugins.secrets)
}

android {
  namespace = "com.example"
  compileSdk { version = release(36) { minorApiLevel = 1 } }

  defaultConfig {
    applicationId = "openwrt.router.control"
    minSdk = 24
    targetSdk = 36
    versionCode = 5
    versionName = "1.6.3"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  signingConfigs {
    create("release") {
      val keystorePath = System.getenv("KEYSTORE_PATH") ?: "${rootDir}/my-upload-key.jks"
      storeFile = file(keystorePath)
      storePassword = System.getenv("STORE_PASSWORD")
      keyAlias = "upload"
      keyPassword = System.getenv("KEY_PASSWORD")
    }
    create("debugConfig") {
      storeFile = file("${rootDir}/debug.keystore")
      storePassword = "android"
      keyAlias = "androiddebugkey"
      keyPassword = "android"
    }
  }

  buildTypes {
    release {
      isCrunchPngs = false
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      signingConfig = signingConfigs.getByName("release")
    }
    debug {
      signingConfig = signingConfigs.getByName("debugConfig")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  buildFeatures {
    compose = true
    buildConfig = true
  }
  packaging {
    jniLibs {
      useLegacyPackaging = true
      keepDebugSymbols.add("**/libiperf3.so")
    }
  }
  testOptions { unitTests { isIncludeAndroidResources = true } }
}

// Custom task to copy and rename the generated APK to RouterControl-v1.6.3.apk while preserving original app-debug.apk
tasks.register<Copy>("copyRenameApk") {
    val buildDir = layout.buildDirectory.get().asFile
    from(File(buildDir, "outputs/apk/debug/app-debug.apk"))
    into(rootProject.layout.projectDirectory.dir(".build-outputs"))
    rename { "RouterControl-v1.6.3.apk" }
}

val envScriptFile = File(project.rootDir, "align_arm64_elf.py").absolutePath
val envVerifyScriptFile = File(project.rootDir, "inspect_elf.py").absolutePath
tasks.register("checkEnvironment") {
    doLast {
        println("=== ENVIRONMENT CHECK ===")
        val jniFile = File(project.projectDir, "src/main/jniLibs/arm64-v8a/libiperf3.so").absolutePath
        val cleanFile = File(project.projectDir, "src/main/assets/arm64-v8a/iperf3").absolutePath
        val commands = listOf(
            listOf("python3", envVerifyScriptFile, cleanFile),
            listOf("python3", envScriptFile, jniFile, jniFile),
            listOf("python3", envVerifyScriptFile, jniFile)
        )
        for (cmd in commands) {
            try {
                val p = ProcessBuilder(cmd).redirectErrorStream(true).start()
                val out = p.inputStream.bufferedReader().readText().trim()
                p.waitFor()
                println("${cmd.joinToString(" ")} -> Exit: ${p.exitValue()}, Output: $out")
            } catch (e: Exception) {
                println("${cmd.joinToString(" ")} -> Exception: ${e.message}")
            }
        }
    }
}

afterEvaluate {
    tasks.findByName("preBuild")?.dependsOn("testGitHubApi", "downloadIperfBinaries")
    tasks.findByName("assembleDebug")?.finalizedBy("copyRenameApk")
}

tasks.register("testGitHubApi") {
    notCompatibleWithConfigurationCache("Requires project runtime references")
    doLast {
        try {
            val url = URL("https://api.github.com/repos/davidBar-On/android-iperf3/contents/libs/arm64-v8a?ref=gh-pages")
            val conn = url.openConnection() as HttpURLConnection
            conn.setRequestProperty("User-Agent", "Mozilla/5.0")
            conn.connect()
            val textFile = File(project.projectDir, "github_api_root.json")
            if (conn.responseCode == 200) {
                val text = conn.inputStream.bufferedReader().readText()
                textFile.writeText(text)
                println("Wrote API response to: ${textFile.absolutePath}")
            } else {
                val errText = conn.errorStream?.bufferedReader()?.readText() ?: ""
                textFile.writeText("Error ${conn.responseCode}: $errText")
                println("Wrote error response to: ${textFile.absolutePath}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

tasks.register("downloadIperfBinaries") {
    notCompatibleWithConfigurationCache("Requires project runtime references")
    doLast {
        val abis = listOf("arm64-v8a", "armeabi-v7a", "x86", "x86_64")
        val destDir = file("src/main/assets")
        val jniDir = file("src/main/jniLibs")
        for (abi in abis) {
            val abiDir = File(destDir, abi)
            abiDir.mkdirs()
            val destFile = File(abiDir, "iperf3")

            val jniAbiDir = File(jniDir, abi)
            jniAbiDir.mkdirs()
            val jniFile = File(jniAbiDir, "libiperf3.so")

            val isElfValid = destFile.exists() && destFile.length() > 10000 && try {
                destFile.inputStream().use { input ->
                    val magic = ByteArray(4)
                    val bytesRead = input.read(magic)
                    bytesRead == 4 && magic[0] == 0x7F.toByte() && magic[1] == 'E'.toByte() && magic[2] == 'L'.toByte() && magic[3] == 'F'.toByte()
                }
            } catch (e: Exception) {
                false
            }

            if (!isElfValid) {
                println("Downloading iperf3 binary for $abi...")
                val candidateUrls = listOf(
                    "https://raw.githubusercontent.com/davidBar-On/android-iperf3/gh-pages/libs/$abi/iperf3.17.1",
                    "https://raw.githubusercontent.com/davidBar-On/android-iperf3/gh-pages/libs/$abi/iperf3.21",
                    "https://raw.githubusercontent.com/davidBar-On/android-iperf3/gh-pages/libs/$abi/iperf3.19.1"
                )
                var success = false
                for (urlString in candidateUrls) {
                    try {
                        val url = URL(urlString)
                        val conn = url.openConnection() as HttpURLConnection
                        conn.requestMethod = "GET"
                        conn.connect()
                        val responseCode = conn.responseCode
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            val input = conn.inputStream
                            val output = destFile.outputStream()
                            try {
                                val buffer = ByteArray(4096)
                                var bytesRead: Int
                                while (input.read(buffer).also { bytesRead = it } != -1) {
                                    output.write(buffer, 0, bytesRead)
                                }
                            } finally {
                                input.close()
                                output.close()
                            }
                            println("Successfully downloaded iperf3 for $abi from: $urlString")
                            success = true
                            break
                        } else {
                            println("Skipping candidate for $abi ($responseCode): $urlString")
                        }
                    } catch (e: Exception) {
                        println("Candidate failed for $abi (${e.message}): $urlString")
                    }
                }
                if (!success) {
                    println("ERROR: Failed to download iperf3 binary for $abi from all candidate URLs!")
                }
            } else {
                println("iperf3 binary for $abi already exists at ${destFile.absolutePath}")
            }

            // Always copy the clean binary from assets to jniLibs as libiperf3.so
            if (destFile.exists()) {
                println("Copying clean $abi binary to jniLibs as libiperf3.so...")
                destFile.copyTo(jniFile, overwrite = true)
            }

            // If ABI is arm64-v8a or x86_64, auto-align ONLY the libiperf3.so file (in jniLibs) to 16 KB page boundaries
            if ((abi == "arm64-v8a" || abi == "x86_64") && jniFile.exists()) {
                val alignScript = File(project.rootDir, "align_arm64_elf.py").absolutePath
                try {
                    val pb = ProcessBuilder("python3", alignScript, jniFile.absolutePath, jniFile.absolutePath)
                    pb.redirectErrorStream(true)
                    val proc = pb.start()
                    val outputText = proc.inputStream.bufferedReader().readText()
                    proc.waitFor()
                    println("Aligning libiperf3.so output: $outputText")
                } catch (e: Exception) {
                    println("Failed to align libiperf3.so: ${e.message}")
                }

                // Verify the aligned binary using inspect_elf.py
                val inspectScript = File(project.rootDir, "inspect_elf.py").absolutePath
                try {
                    val pb = ProcessBuilder("python3", inspectScript, jniFile.absolutePath)
                    pb.redirectErrorStream(true)
                    val proc = pb.start()
                    val outputText = proc.inputStream.bufferedReader().readText()
                    proc.waitFor()
                    println("Inspecting libiperf3.so ($abi) output:\n$outputText")
                } catch (e: Exception) {
                    println("Failed to inspect libiperf3.so: ${e.message}")
                }
            }
        }
    }
}

// Configure the Secrets Gradle Plugin to use .env and .env.example files
// to match the convention used in Web projects.
secrets {
  propertiesFileName = ".env"
  defaultPropertiesFileName = ".env.example"
}

// Some unused dependencies are commented out below instead of being removed.
// This makes it easy to add them back in the future if needed.
apply(from = "generate_banner.gradle.kts")
dependencies {
  implementation(platform(libs.androidx.compose.bom))
  implementation(platform(libs.firebase.bom))
  // implementation(libs.accompanist.permissions)
  implementation(libs.androidx.activity.compose)
  // implementation(libs.androidx.camera.camera2)
  // implementation(libs.androidx.camera.core)
  // implementation(libs.androidx.camera.lifecycle)
  // implementation(libs.androidx.camera.view)
  implementation(libs.androidx.compose.material.icons.core)
  // implementation(libs.androidx.compose.material.icons.extended)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.ui.graphics)
  implementation(libs.androidx.compose.ui.tooling.preview)
  implementation(libs.androidx.core.ktx)
  // implementation(libs.androidx.datastore.preferences)
  implementation(libs.androidx.lifecycle.runtime.compose)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.lifecycle.viewmodel.compose)
  // implementation(libs.androidx.navigation.compose)
  implementation(libs.androidx.room.ktx)
  implementation(libs.androidx.room.runtime)
  // implementation(libs.coil.compose)
  implementation(libs.converter.moshi)
  // implementation(libs.firebase.ai)
  implementation(libs.kotlinx.coroutines.android)
  implementation(libs.kotlinx.coroutines.core)
  implementation(libs.logging.interceptor)
  implementation(libs.moshi.kotlin)
  implementation(libs.okhttp)
  // implementation(libs.play.services.location)
  implementation(libs.retrofit)
  testImplementation(libs.androidx.compose.ui.test.junit4)
  testImplementation(libs.androidx.core)
  testImplementation(libs.androidx.junit)
  testImplementation(libs.junit)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.robolectric)
  testImplementation(libs.roborazzi)
  testImplementation(libs.roborazzi.compose)
  testImplementation(libs.roborazzi.junit.rule)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.compose.ui.test.junit4)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.runner)
  debugImplementation(libs.androidx.compose.ui.test.manifest)
  debugImplementation(libs.androidx.compose.ui.tooling)
  "ksp"(libs.androidx.room.compiler)
  "ksp"(libs.moshi.kotlin.codegen)
  implementation(libs.jsch)
  implementation(libs.androidx.core.google.shortcuts)
  
  // Force 16 KB memory page aligned version of graphics-path JNI library
  implementation("androidx.graphics:graphics-path:1.0.1")
}
