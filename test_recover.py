import sys

def recover(filepath):
    with open(filepath, 'r') as f:
        content = f.read()

    replacement2 = """var menuExpanded by remember { mutableStateOf(false) }
                                        Box {
                                            val menuInteractionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
                                            val isMenuFocused by menuInteractionSource.collectIsFocusedAsState()
                                            IconButton(
                                                onClick = { menuExpanded = true },
                                                interactionSource = menuInteractionSource,
                                                modifier = Modifier.background(if (isMenuFocused) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent, RoundedCornerShape(20.dp))
                                            ) {
                                                Icon(Icons.Default.MoreVert, contentDescription = translateText("Опции", context))
                                            }
                                            DropdownMenu(
                                                expanded = menuExpanded,
                                                onDismissRequest = { menuExpanded = false }
                                            ) {
                                                DropdownMenuItem(
                                                    text = { Text(translateText("Скопировать", context)) },
                                                    leadingIcon = { Icon(Icons.Default.ContentCopy, contentDescription = null) },
                                                    onClick = {
                                                        menuExpanded = false
                                                        coroutineScope.launch {
                                                            val pubKey = SshKeyManager(context).loadPublicKeyString(key.id)
                                                            if (pubKey != null) {
                                                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                                                clipboard.setPrimaryClip(ClipData.newPlainText("SSH Public Key", pubKey))
                                                                Toast.makeText(context, translateText("Публичный ключ скопирован", context), Toast.LENGTH_SHORT).show()
                                                            }
                                                        }
                                                    }
                                                )
                                                DropdownMenuItem(
                                                    text = { Text(translateText("Скачать", context)) },
                                                    leadingIcon = { Icon(Icons.Default.Download, contentDescription = null) },
                                                    onClick = {
                                                        menuExpanded = false
                                                        val pubFile = File(File(context.filesDir, ".ssh"), "id_${key.id}.pub")
                                                        val privFile = File(File(context.filesDir, ".ssh"), "id_${key.id}")
                                                        
                                                        val downloadsDir = android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_DOWNLOADS)
                                                        val appDownloadsDir = File(downloadsDir, "AppSSHKeys")
                                                        if (!appDownloadsDir.exists()) appDownloadsDir.mkdirs()
                                                        
                                                        try {
                                                            if (privFile.exists()) {
                                                                privFile.copyTo(File(appDownloadsDir, "${key.name}_id_${key.id}"), overwrite = true)
                                                            }
                                                            if (pubFile.exists()) {
                                                                pubFile.copyTo(File(appDownloadsDir, "${key.name}_id_${key.id}.pub"), overwrite = true)
                                                            }
                                                            Toast.makeText(context, translateText("Ключи сохранены в Загрузки/AppSSHKeys", context), Toast.LENGTH_SHORT).show()
                                                        } catch (e: Exception) {
                                                            Toast.makeText(context, translateText("Ошибка при сохранении", context), Toast.LENGTH_SHORT).show()
                                                        }
                                                    }
                                                )
                                                DropdownMenuItem(
                                                    text = { Text(translateText("Поделиться", context)) },
                                                    leadingIcon = { Icon(Icons.Default.Share, contentDescription = null) },
                                                    onClick = {
                                                        menuExpanded = false
                                                        val pubFile = File(File(context.filesDir, ".ssh"), "id_${key.id}.pub")
                                                        if (pubFile.exists()) {
                                                            val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", pubFile)
                                                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                                                type = "text/plain"
                                                                putExtra(Intent.EXTRA_STREAM, uri)
                                                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                                            }
                                                            context.startActivity(Intent.createChooser(shareIntent, translateText("Поделиться публичным ключом", context)))
                                                        }
                                                    }
                                                )"""
    
    rep_len = len(replacement2)
    # The format is replacement2 + char + replacement2 + char + ... + replacement2
    
    recovered = []
    i = rep_len
    while i < len(content):
        recovered.append(content[i])
        i += 1 + rep_len
        
    print("Recovered length:", len(recovered))
    print("First 100 chars:", "".join(recovered[:100]))
    
    with open("SshKeyManagerDialog_recovered.kt", "w") as f:
        f.write("".join(recovered))

recover('app/src/main/java/com/example/ui/SshKeyManagerDialog.kt')
