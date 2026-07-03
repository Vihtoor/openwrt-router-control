package com.example.data

import android.util.Log
import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.ChannelShell
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.Properties

data class SshResult(
    val exitCode: Int,
    val stdout: String,
    val stderr: String
) {
    fun fullOutput(): String {
        return if (stderr.isNotEmpty()) {
            if (stdout.isNotEmpty()) "$stdout\nError: $stderr" else "Error: $stderr"
        } else {
            stdout
        }
    }
}

class SshClientManager {
    private val mutex = Mutex()
    private var activeSession: Session? = null
    private var lastConfig: RouterConfig? = null

    private var activeChannel: ChannelExec? = null
    private var activeStdin: java.io.OutputStream? = null
    @Volatile
    private var resetTimeoutRequested = false

    private var activeShellChannel: ChannelShell? = null
    private var activeShellStdin: java.io.OutputStream? = null
    private var shellJob: Job? = null

    private val writeChannel = kotlinx.coroutines.channels.Channel<ByteArray>(
        kotlinx.coroutines.channels.Channel.UNLIMITED
    )
    private val writerScope = kotlinx.coroutines.CoroutineScope(
        kotlinx.coroutines.Dispatchers.IO + kotlinx.coroutines.SupervisorJob()
    )

    init {
        writerScope.launch {
            for (bytes in writeChannel) {
                val stdin = activeShellStdin ?: activeStdin ?: continue
                try {
                    stdin.write(bytes)
                    stdin.flush()
                } catch (e: Exception) {
                    Log.e("SshClientManager", "Failed to write to stdin: ${e.message}")
                }
            }
        }
    }

    fun writeToStdin(text: String): Boolean {
        val bytes = (text + "\n").toByteArray(Charsets.UTF_8)
        val success = writeChannel.trySend(bytes).isSuccess
        if (success && activeShellChannel == null && activeChannel != null) {
            resetTimeoutRequested = true
        }
        return success
    }

    fun writeRawToStdin(text: String): Boolean {
        val bytes = text.toByteArray(Charsets.UTF_8)
        val success = writeChannel.trySend(bytes).isSuccess
        if (success && activeShellChannel == null && activeChannel != null) {
            resetTimeoutRequested = true
        }
        return success
    }

    suspend fun testConnection(config: RouterConfig): Boolean = withContext(Dispatchers.IO) {
        try {
            val session = getConnectedSession(config)
            session.isConnected
        } catch (e: Exception) {
            Log.e("SshClientManager", "Connection test failed: ${e.message}")
            false
        }
    }

    suspend fun getConnectedSession(config: RouterConfig): Session = withContext(Dispatchers.IO) {
        mutex.withLock {
            val currentSession = activeSession
            if (currentSession != null && currentSession.isConnected && lastConfig == config) {
                return@withContext currentSession
            }

            closeSessionInternal()

            Log.d("SshClientManager", "Establishing new SSH session to ${config.ipAddress}:${config.port}")
            val jsch = JSch()
            val session = jsch.getSession(config.username, config.ipAddress, config.port)
            session.setPassword(config.sshKeyOrPassword)
            
            val properties = Properties()
            properties["StrictHostKeyChecking"] = "no"
            properties["TCPKeepAlive"] = "yes"
            session.setConfig(properties)
            session.timeout = 5000 // 5 seconds connect timeout
            session.serverAliveInterval = 30000 // send keep-alive every 30s
            session.serverAliveCountMax = 3 // disconnect after 3 missed keepalives
            session.connect()

            activeSession = session
            lastConfig = config
            session
        }
    }

    suspend fun executeCommand(
        config: RouterConfig,
        command: String,
        timeoutMs: Long = 10000L,
        onPartialOutput: (suspend (String) -> Unit)? = null
    ): SshResult = withContext(Dispatchers.IO) {
        val session = getConnectedSession(config)
        var channel: ChannelExec? = null
        try {
            channel = session.openChannel("exec") as ChannelExec
            channel.setPtyType("xterm-256color")
            channel.setPty(true)
            channel.setPtySize(1000, 100, 0, 0)
            val wrappedCommand = "export PATH=\"/usr/sbin:/usr/bin:/sbin:/bin:\$PATH\"; if ! command -v tty >/dev/null 2>&1; then tty() { if [ -n \"\$SSH_TTY\" ]; then echo \"\$SSH_TTY\"; else echo \"not a tty\"; return 1; fi; }; fi; $command"
            channel.setCommand(wrappedCommand)

            val outputStream = ByteArrayOutputStream()
            val errorStream = ByteArrayOutputStream()
            channel.setOutputStream(outputStream)
            channel.setErrStream(errorStream)

            val stdin = channel.getOutputStream()
            activeChannel = channel
            activeStdin = stdin
            resetTimeoutRequested = false

            channel.connect(5000)

            var count = 0L
            val limit = timeoutMs / 10L
            var lastEmittedLength = 0
            var lastEmitTime = 0L
            while (!channel.isClosed && count < limit) {
                delay(10)
                if (resetTimeoutRequested) {
                    count = 0L
                    resetTimeoutRequested = false
                } else {
                    count++
                }
                val now = System.currentTimeMillis()
                if (now - lastEmitTime >= 150) {
                    val stdoutText = outputStream.toString("UTF-8").trim()
                    val stderrText = errorStream.toString("UTF-8").trim()
                    val currentOut = if (stderrText.isNotEmpty()) {
                        if (stdoutText.isNotEmpty()) "$stdoutText\nError: $stderrText" else "Error: $stderrText"
                    } else {
                        stdoutText
                    }
                    if (onPartialOutput != null && currentOut.length != lastEmittedLength) {
                        lastEmittedLength = currentOut.length
                        lastEmitTime = now
                        onPartialOutput(currentOut)
                    }
                }
            }

            val exitStatus = channel.exitStatus
            val stdout = outputStream.toString("UTF-8").trim()
            val stderr = errorStream.toString("UTF-8").trim()

            SshResult(exitStatus, stdout, stderr)
        } catch (e: Exception) {
            Log.e("SshClientManager", "Command execution error: ${e.message}", e)
            mutex.withLock {
                closeSessionInternal()
            }
            throw e
        } finally {
            activeChannel = null
            activeStdin = null
            try {
                channel?.disconnect()
            } catch (e: Exception) {}
        }
    }

    suspend fun startInteractiveShell(
        config: RouterConfig,
        onOutput: (String) -> Unit
    ) = withContext(Dispatchers.IO) {
        val session = getConnectedSession(config)
        mutex.withLock {
            if (activeShellChannel != null && activeShellChannel!!.isConnected) {
                return@withContext
            }
            stopInteractiveShellInternal()
            try {
                val channel = session.openChannel("shell") as ChannelShell
                channel.setPtyType("xterm-256color")
                channel.setPty(true)
                channel.setPtySize(1000, 100, 0, 0)

                val shellStdin = channel.outputStream
                val shellStdout = channel.inputStream

                activeShellChannel = channel
                activeShellStdin = shellStdin

                channel.connect(5000)

                shellJob = CoroutineScope(Dispatchers.IO).launch {
                    val reader = shellStdout.reader(Charsets.UTF_8)
                    val buffer = CharArray(4096)
                    try {
                        while (isActive) {
                            val read = reader.read(buffer)
                            if (read > 0) {
                                val text = String(buffer, 0, read)
                                onOutput(text)
                            } else if (read < 0) {
                                break
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("SshClientManager", "Interactive shell read error: ${e.message}")
                    }
                }
            } catch (e: Exception) {
                Log.e("SshClientManager", "Failed to start interactive shell: ${e.message}")
                stopInteractiveShellInternal()
                throw e
            }
        }
    }

    suspend fun stopInteractiveShell() = withContext(Dispatchers.IO) {
        mutex.withLock {
            stopInteractiveShellInternal()
        }
    }

    private fun stopInteractiveShellInternal() {
        try {
            shellJob?.cancel()
        } catch (e: Exception) {}
        shellJob = null
        try {
            activeShellChannel?.disconnect()
        } catch (e: Exception) {}
        activeShellChannel = null
        activeShellStdin = null
    }

    private fun closeSessionInternal() {
        stopInteractiveShellInternal()
        try {
            activeSession?.disconnect()
        } catch (e: Exception) {
            Log.e("SshClientManager", "Failed to close session properly: ${e.message}")
        }
        activeSession = null
        lastConfig = null
    }

    suspend fun disconnect() = withContext(Dispatchers.IO) {
        mutex.withLock {
            closeSessionInternal()
        }
    }
}
