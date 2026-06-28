package com.example

import android.content.Context
import android.os.Build
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

object IperfServerManager {
    private const val TAG = "IperfServerManager"
    
    private val _logs = MutableStateFlow<List<String>>(emptyList())
    val logs: StateFlow<List<String>> = _logs.asStateFlow()
    
    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()
    
    private var serverProcess: Process? = null
    
    fun clearLogs() {
        _logs.value = emptyList()
    }
    
    private fun addLogLine(line: String) {
        val current = _logs.value.toMutableList()
        current.add(line)
        _logs.value = current
    }
    
    @Synchronized
    fun startServer(context: Context) {
        if (_isRunning.value) {
            Log.d(TAG, "Server already running")
            return
        }
        
        _isRunning.value = true
        _logs.value = emptyList()
        
        addLogLine("System: Starting Native iPerf3 Server...")
        
        val filesDirFile = copyAssetToFiles(context)
        addLogLine("System: Executing program: ${filesDirFile.absolutePath}")
        addLogLine("System: Working directory: ${context.filesDir.absolutePath}")
        
        val openFds = try {
            File("/proc/self/fd").listFiles()?.size ?: 0
        } catch (e: Exception) {
            0
        }
        addLogLine("System: Open FDs before start: $openFds")
        
        val fdSoftLimit = getFdSoftLimit()
        addLogLine("System: FD soft limit: $fdSoftLimit")
        
        var process: Process? = null
        
        // 1. Try starting from filesDir
        try {
            val pb = ProcessBuilder(filesDirFile.absolutePath, "-s", "--forceflush")
                .directory(context.filesDir)
                .redirectErrorStream(true)
            pb.environment()["TMPDIR"] = context.cacheDir.absolutePath
            process = pb.start()
        } catch (e: Exception) {
            addLogLine("System Warning: Permission denied on local folder. Retrying using native system library path...")
        }
        
        // 2. If it failed or couldn't start, retry with native system library path
        if (process == null) {
            val nativeLibFile = File(context.applicationInfo.nativeLibraryDir, "libiperf3.so")
            addLogLine("System: Executing native system path: ${nativeLibFile.absolutePath}")
            try {
                val pb = ProcessBuilder(nativeLibFile.absolutePath, "-s", "--forceflush")
                    .directory(context.filesDir)
                    .redirectErrorStream(true)
                pb.environment()["TMPDIR"] = context.cacheDir.absolutePath
                process = pb.start()
                addLogLine("System: Process started successfully.")
            } catch (e: Exception) {
                addLogLine("System Error: Failed to launch native iperf3 server: ${e.message}")
                _isRunning.value = false
                addLogLine("System: Native iPerf3 Server stopped.")
                return
            }
        } else {
            addLogLine("System: Process started successfully.")
        }
        
        serverProcess = process
        
        // Read output stream
        Thread {
            try {
                val reader = BufferedReader(InputStreamReader(process.inputStream))
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    line?.let { addLogLine(it) }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error reading iperf3 process output", e)
            } finally {
                stopServer()
            }
        }.start()
        
        // Wait for process to exit
        Thread {
            try {
                process.waitFor()
            } catch (e: Exception) {
                Log.e(TAG, "Process wait interrupted", e)
            } finally {
                _isRunning.value = false
                addLogLine("System: Native iPerf3 Server stopped.")
                synchronized(this) {
                    if (serverProcess == process) {
                        serverProcess = null
                    }
                }
            }
        }.start()
    }
    
    @Synchronized
    fun stopServer() {
        serverProcess?.let {
            try {
                it.destroy()
            } catch (e: Exception) {
                Log.e(TAG, "Error destroying process", e)
            }
            serverProcess = null
        }
    }
    
    private fun copyAssetToFiles(context: Context): File {
        val destFile = File(context.filesDir, "iperf3")
        try {
            val abis = Build.SUPPORTED_ABIS
            var copied = false
            for (abi in abis) {
                val assetPath = "$abi/iperf3"
                try {
                    context.assets.open(assetPath).use { input ->
                        destFile.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                    destFile.setExecutable(true, false)
                    copied = true
                    break
                } catch (e: Exception) {
                    // Try next ABI
                }
            }
            if (!copied) {
                context.assets.open("arm64-v8a/iperf3").use { input ->
                    destFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                destFile.setExecutable(true, false)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed copying asset to files", e)
        }
        return destFile
    }
    
    private fun getFdSoftLimit(): Int {
        try {
            val limitsFile = File("/proc/self/limits")
            if (limitsFile.exists()) {
                limitsFile.bufferedReader().use { reader ->
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        val currentLine = line ?: continue
                        if (currentLine.contains("Max open files", ignoreCase = true)) {
                            val parts = currentLine.split("\\s+".toRegex()).filter { it.isNotEmpty() }
                            if (parts.size >= 4) {
                                parts[3].toIntOrNull()?.let { return it }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            // ignore
        }
        return 32768
    }
}
