
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportKeyDialog(
    onDismiss: () -> Unit,
    onKeyImported: (Long) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    
    var inputMode by remember { mutableStateOf(1) } // 0 = file, 1 = text
    var privateKeyText by remember { mutableStateOf("") }
    var privateKeyBytes by remember { mutableStateOf<ByteArray?>(null) }
    var fileName by remember { mutableStateOf<String?>(null) }
    
    var alias by remember { mutableStateOf("") }
    var passphrase by remember { mutableStateOf("") }
    var publicKeyText by remember { mutableStateOf("") }
    
    var parsedKeyType by remember { mutableStateOf<Int?>(null) }
    var isEncrypted by remember { mutableStateOf(false) }
    var isDecrypted by remember { mutableStateOf(true) }
    
    var errorMsg by remember { mutableStateOf<String?>(null) }
    
    val launcher = androidx.activity.compose.rememberLauncherForActivityResult(androidx.activity.result.contract.ActivityResultContracts.GetContent()) { uri: android.net.Uri? ->
        if (uri != null) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val bytes = inputStream?.readBytes()
                inputStream?.close()
                if (bytes != null) {
                    privateKeyBytes = bytes
                    fileName = uri.path?.substringAfterLast('/') ?: "file"
                    inputMode = 0
                }
            } catch (e: Exception) {
                errorMsg = "Ошибка чтения файла: ${e.message}"
            }
        }
    }
    
    LaunchedEffect(privateKeyText, privateKeyBytes, inputMode) {
        val bytes = if (inputMode == 1) {
            if (privateKeyText.isNotBlank()) privateKeyText.toByteArray(Charsets.UTF_8) else null
        } else {
            privateKeyBytes
        }
        
        if (bytes == null || bytes.isEmpty()) {
            parsedKeyType = null
            isEncrypted = false
            isDecrypted = true
            errorMsg = null
            return@LaunchedEffect
        }
        
        try {
            val jsch = JSch()
            val kp = KeyPair.load(jsch, bytes, null)
            parsedKeyType = kp.keyType
            isEncrypted = kp.isEncrypted
            isDecrypted = !kp.isEncrypted
            errorMsg = null
            
            if (!kp.isEncrypted) {
                val pubOut = java.io.ByteArrayOutputStream()
                kp.writePublicKey(pubOut, "imported-key")
                val pubStr = String(pubOut.toByteArray())
                if (pubStr.isNotBlank()) {
                    publicKeyText = pubStr
                }
            }
            kp.dispose()
        } catch (e: Exception) {
            parsedKeyType = null
            isEncrypted = false
            isDecrypted = true
            errorMsg = "Не удалось распознать формат ключа"
        }
    }
    
    LaunchedEffect(passphrase, parsedKeyType, isEncrypted) {
        if (isEncrypted && passphrase.isNotEmpty()) {
            val bytes = if (inputMode == 1) privateKeyText.toByteArray(Charsets.UTF_8) else privateKeyBytes
            if (bytes != null) {
                try {
                    val jsch = JSch()
                    val kp = KeyPair.load(jsch, bytes, null)
                    if (kp.decrypt(passphrase)) {
                        isDecrypted = true
                        errorMsg = null
                        val pubOut = java.io.ByteArrayOutputStream()
                        kp.writePublicKey(pubOut, "imported-key")
                        val pubStr = String(pubOut.toByteArray())
                        if (pubStr.isNotBlank() && publicKeyText.isBlank()) {
                            publicKeyText = pubStr
                        }
                    } else {
                        isDecrypted = false
                    }
                    kp.dispose()
                } catch (e: Exception) {
                    isDecrypted = false
                }
            }
        }
    }
    
    var isSaving by remember { mutableStateOf(false) }
    
    Dialog(
        onDismissRequest = { if (!isSaving) onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.95f),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = translateText("Импорт ключа", context),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { launcher.launch("*/*") },
                        modifier = Modifier.weight(1f).height(40.dp),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (inputMode == 0) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (inputMode == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        ),
                        shape = RoundedCornerShape(20.dp),
                        border = if (inputMode == 0) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null
                    ) {
                        Icon(Icons.Default.Upload, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(translateText("Из файла", context), fontSize = 13.sp)
                    }
                    Button(
                        onClick = { inputMode = 1 },
                        modifier = Modifier.weight(1f).height(40.dp),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (inputMode == 1) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (inputMode == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        ),
                        shape = RoundedCornerShape(20.dp),
                        border = if (inputMode == 1) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null
                    ) {
                        Icon(Icons.Default.ContentPaste, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(translateText("Текстом", context), fontSize = 13.sp)
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                val scrollState = androidx.compose.foundation.rememberScrollState()
                Column(modifier = Modifier.weight(1f).verticalScroll(scrollState)) {
                    
                    if (inputMode == 1) {
                        OutlinedTextField(
                            value = privateKeyText,
                            onValueChange = { privateKeyText = it },
                            label = { Text(translateText("Приватный ключ", context)) },
                            modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp, max = 200.dp),
                            textStyle = androidx.compose.ui.text.TextStyle(fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace, fontSize = 12.sp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    } else if (fileName != null) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.InsertDriveFile, contentDescription = null)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(fileName!!, style = MaterialTheme.typography.bodyMedium)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    
                    if (parsedKeyType != null) {
                        val typeName = when(parsedKeyType) {
                            KeyPair.RSA -> "RSA"
                            KeyPair.DSA -> "DSA"
                            KeyPair.ECDSA -> "ECDSA"
                            KeyPair.ED25519 -> "ED25519"
                            else -> "UNKNOWN"
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(androidx.compose.ui.graphics.Color(0xFF1C2E23), RoundedCornerShape(8.dp))
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = androidx.compose.ui.graphics.Color(0xFF6FD394))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(translateText("Формат распознан", context), fontWeight = FontWeight.Bold, color = androidx.compose.ui.graphics.Color(0xFF6FD394), fontSize = 14.sp)
                                Text(typeName, color = androidx.compose.ui.graphics.Color(0xFF6FD394), fontSize = 12.sp)
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        if (isEncrypted) {
                            if (parsedKeyType == KeyPair.ED25519) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(androidx.compose.ui.graphics.Color(0xFF331818), RoundedCornerShape(8.dp))
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Warning, contentDescription = null, tint = androidx.compose.ui.graphics.Color(0xFFF05E5E))
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = translateText("ED25519 ключи с паролем не поддерживаются библиотекой JSch. Вы не сможете использовать этот ключ.", context),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = androidx.compose.ui.graphics.Color(0xFFF05E5E)
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                            
                            OutlinedTextField(
                                value = passphrase,
                                onValueChange = { passphrase = it },
                                label = { Text(translateText("Пароль ключа", context)) },
                                singleLine = true,
                                visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            if (passphrase.isNotEmpty()) {
                                if (isDecrypted) {
                                    Text(translateText("Пароль подходит", context), color = androidx.compose.ui.graphics.Color(0xFF6FD394), fontSize = 12.sp)
                                } else {
                                    Text(translateText("Неверный пароль", context), color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    } else if (errorMsg != null && ((inputMode == 1 && privateKeyText.isNotBlank()) || (inputMode == 0 && privateKeyBytes != null))) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.errorContainer, RoundedCornerShape(8.dp))
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Error, contentDescription = null, tint = MaterialTheme.colorScheme.onErrorContainer)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(errorMsg!!, color = MaterialTheme.colorScheme.onErrorContainer, style = MaterialTheme.typography.bodySmall)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    
                    OutlinedTextField(
                        value = alias,
                        onValueChange = { alias = it },
                        label = { Text(translateText("Название ключа", context)) },
                        placeholder = { Text(translateText("Например: домашний ноутбук", context)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    if (publicKeyText.isBlank() && parsedKeyType != null) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(androidx.compose.ui.graphics.Color(0xFF332A18), RoundedCornerShape(8.dp))
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = androidx.compose.ui.graphics.Color(0xFFF0B95E))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = translateText("Публичная часть не найдена — пожалуйста, вставьте её вручную", context),
                                style = MaterialTheme.typography.bodySmall,
                                color = androidx.compose.ui.graphics.Color(0xFFF0B95E)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    
                    OutlinedTextField(
                        value = publicKeyText,
                        onValueChange = { publicKeyText = it },
                        label = { Text(translateText("Публичный ключ", context)) },
                        placeholder = { Text("ssh-rsa AAAA...") },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 80.dp),
                        textStyle = androidx.compose.ui.text.TextStyle(fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace, fontSize = 12.sp)
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss, enabled = !isSaving) {
                        Text(translateText("Отмена", context))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            isSaving = true
                            coroutineScope.launch {
                                try {
                                    val manager = SshKeyManager(context)
                                    val bytes = if (inputMode == 1) privateKeyText.toByteArray(Charsets.UTF_8) else privateKeyBytes
                                    if (bytes == null) throw Exception("Нет данных ключа")
                                    
                                    val finalPub = if (publicKeyText.isNotBlank()) publicKeyText else null
                                    
                                    val entity = manager.importAndSaveKey(
                                        alias = alias.ifBlank { "Imported Key" },
                                        privateKeyBytes = bytes,
                                        publicKeyString = finalPub,
                                        passphrase = passphrase.ifEmpty { null }
                                    )
                                    onKeyImported(entity.id)
                                    onDismiss()
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Ошибка сохранения: ${e.message}", Toast.LENGTH_LONG).show()
                                } finally {
                                    isSaving = false
                                }
                            }
                        },
                        enabled = !isSaving && isDecrypted && alias.isNotBlank() && parsedKeyType != null && (parsedKeyType != KeyPair.ED25519 || !isEncrypted)
                    ) {
                        if (isSaving) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                        } else {
                            Text(translateText("Сохранить", context))
                        }
                    }
                }
            }
        }
    }
}
