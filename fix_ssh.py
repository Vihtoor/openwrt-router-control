import re

with open("app/src/main/java/com/example/data/SshClientManager.kt", "r") as f:
    content = f.read()

# Remove aggressive closeSessionInternal() from executeCommand's catch block
content = re.sub(
    r'        } catch \(e: Exception\) \{\n\s+Log\.e\("SshClientManager", "Command execution error: \$\{e\.message\}", e\)\n\s+mutex\.withLock \{\n\s+closeSessionInternal\(\)\n\s+\}\n\s+throw e\n\s+\} finally \{',
    r'        } catch (e: Exception) {\n            Log.e("SshClientManager", "Command execution error: ${e.message}", e)\n            if (e is com.jcraft.jsch.JSchException && (e.message?.contains("session is down") == true || e.message?.contains("socket is not established") == true)) {\n                mutex.withLock {\n                    closeSessionInternal()\n                }\n            }\n            throw e\n        } finally {',
    content
)

with open("app/src/main/java/com/example/data/SshClientManager.kt", "w") as f:
    f.write(content)
