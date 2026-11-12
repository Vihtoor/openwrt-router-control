with open("app/src/main/java/com/example/ui/SshKeyManagerDialog.kt", "r") as f:
    content = f.read()

import_statement = "import androidx.core.content.FileProvider\nimport android.content.Intent\nimport java.io.File\n"
if "import androidx.core.content.FileProvider" not in content:
    content = content.replace("import android.content.Context", import_statement + "import android.content.Context")

target = """                                        IconButton(onClick = {
                                            coroutineScope.launch {
                                                SshKeyManager(context).deleteKeyFiles(key.id)
                                                if (selectedKeyId == key.id) {
                                                    onSelectKey(null)
                                                }
                                            }
                                        }) {
                                            Icon(Icons.Default.Delete, contentDescription = translateText("Удалить", context))
                                        }"""

replacement = """                                        IconButton(onClick = {
                                            val pubFile = File(File(context.filesDir, ".ssh"), "id_${key.id}.pub")
                                            if (pubFile.exists()) {
                                                val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", pubFile)
                                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                                    type = "text/plain"
                                                    putExtra(Intent.EXTRA_STREAM, uri)
                                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                                }
                                                context.startActivity(Intent.createChooser(shareIntent, translateText("Поделиться публичным ключом", context)))
                                            } else {
                                                Toast.makeText(context, translateText("Файл публичного ключа не найден", context), Toast.LENGTH_SHORT).show()
                                            }
                                        }) {
                                            Icon(androidx.compose.material.icons.filled.Share, contentDescription = translateText("Поделиться публичным ключом", context))
                                        }
                                        IconButton(onClick = {
                                            coroutineScope.launch {
                                                SshKeyManager(context).deleteKeyFiles(key.id)
                                                if (selectedKeyId == key.id) {
                                                    onSelectKey(null)
                                                }
                                            }
                                        }) {
                                            Icon(Icons.Default.Delete, contentDescription = translateText("Удалить", context))
                                        }"""

if target in content:
    content = content.replace(target, replacement)
    
if "import androidx.compose.material.icons.filled.Share" not in content:
    content = content.replace("import androidx.compose.material.icons.filled.Delete", "import androidx.compose.material.icons.filled.Delete\nimport androidx.compose.material.icons.filled.Share")

with open("app/src/main/java/com/example/ui/SshKeyManagerDialog.kt", "w") as f:
    f.write(content)
