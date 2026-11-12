with open('app/src/main/java/com/example/ui/SshKeyManagerDialog.kt', 'r', encoding='utf-8') as f:
    content = f.read()

import re

target = '''                                                    onClick = {
                                                        menuExpanded = false
                                                        val pubFile = File(File(context.filesDir, ".ssh"), "id_${key.id}.pub")
                                                        val privFile = File(File(context.filesDir, ".ssh"), "id_${key.id}")
                                                        
                                                        val downloadsDir = android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_DOWNLOADS)
                                                        val appDownloadsDir = File(downloadsDir, "AppSSHKeys")
                                                        if (!appDownloadsDir.exists()) appDownloadsDir.mkdirs()
                                                        
                                                        try {
                                                            if (privFile.exists()) {
                                                                privFile.copyTo(File(appDownloadsDir, "${key.alias}_id_${key.id}"), overwrite = true)
                                                            }
                                                            if (pubFile.exists()) {
                                                                pubFile.copyTo(File(appDownloadsDir, "${key.alias}_id_${key.id}.pub"), overwrite = true)
                                                            }
                                                            Toast.makeText(context, translateText("Ключи сохранены в Загрузки/AppSSHKeys", context), Toast.LENGTH_SHORT).show()
                                                        } catch (e: Exception) {
                                                            Toast.makeText(context, translateText("Ошибка при сохранении", context), Toast.LENGTH_SHORT).show()
                                                        }
                                                    }'''

replacement = '''                                                    onClick = {
                                                        menuExpanded = false
                                                        coroutineScope.launch {
                                                            try {
                                                                val pubFile = File(File(context.filesDir, ".ssh"), "id_${key.id}.pub")
                                                                val manager = SshKeyManager(context)
                                                                val downloadsDir = android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_DOWNLOADS)
                                                                val appDownloadsDir = File(downloadsDir, "AppSSHKeys")
                                                                if (!appDownloadsDir.exists()) appDownloadsDir.mkdirs()
                                                                
                                                                val decryptedPrivBytes = manager.loadPrivateKeyBytes(key.id)
                                                                File(appDownloadsDir, "${key.alias}_id_${key.id}").writeBytes(decryptedPrivBytes)
                                                                
                                                                if (pubFile.exists()) {
                                                                    pubFile.copyTo(File(appDownloadsDir, "${key.alias}_id_${key.id}.pub"), overwrite = true)
                                                                }
                                                                Toast.makeText(context, translateText("Ключи сохранены в Загрузки/AppSSHKeys", context), Toast.LENGTH_SHORT).show()
                                                            } catch (e: Exception) {
                                                                Toast.makeText(context, translateText("Ошибка при сохранении", context), Toast.LENGTH_SHORT).show()
                                                            }
                                                        }
                                                    }'''

content = content.replace(target, replacement)
with open('app/src/main/java/com/example/ui/SshKeyManagerDialog.kt', 'w', encoding='utf-8') as f:
    f.write(content)

print("Download patched")
