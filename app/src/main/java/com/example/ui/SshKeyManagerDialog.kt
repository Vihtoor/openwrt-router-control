package com.example.ui

import android.content.ClipData
import android.content.ClipboardManager
import androidx.core.content.FileProvider
import android.content.Intent
import java.io.File
import android.content.Context
import android.widget.Toast
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import com.jcraft.jsch.JSch
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.RouterDatabase
import com.example.data.SshKeyEntity
import com.example.data.SshKeyManager
import com.example.translateText
import com.jcraft.jsch.KeyPair
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SshKeyManagerDialog(
    onDismiss: () -> Unit,
    onSelectKey: (Long?) -> Unit,
    selectedKeyId: Long?
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var keys by remember { mutableStateOf<List<SshKeyEntity>>(emptyList()) }
    var isGenerating by remember { mutableStateOf(false) }
    var showImportDialog by remember { mutableStateOf(false) }
    var showGenerateDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val db = RouterDatabase.getDatabase(context)
        db.sshKeyDao().getAllKeysFlow().collect {
            keys = it
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.8f),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = translateText("Управление SSH-ключами", context),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                if (keys.isEmpty()) {
                    Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(translateText("Нет сохраненных ключей", context), color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                } else {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(keys) { key ->
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                                ListItem(
                                    headlineContent = { Text(key.alias, fontWeight = FontWeight.Bold) },
                                    supportingContent = { 
                                        Column {
                                            Text("${key.algorithm} • ${key.fingerprintSha256}", maxLines = 1, overflow = TextOverflow.Ellipsis)
                                        }
                                    },
                                    modifier = Modifier.weight(1f).clickable { onSelectKey(key.id) }
                                )
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 16.dp)) {
                                    if (selectedKeyId == key.id) {
                                            Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                        }
                                        var menuExpanded by remember { mutableStateOf(false) }
                                        Box {
                                            val menuInteractionSource = remember { MutableInteractionSource() }
                                            val isMenuFocused by menuInteractionSource.collectIsFocusedAsState()
                                            IconButton(
                                                onClick = { menuExpanded = true },
                                                interactionSource = menuInteractionSource,
                                                modifier = Modifier.background(if (isMenuFocused) MaterialTheme.colorScheme.primaryContainer else Color.Transparent, RoundedCornerShape(20.dp))
                                            ) {
                                                Icon(Icons.Default.MoreVert, contentDescription = translateText("Опции", context), tint = if (isMenuFocused) MaterialTheme.colorScheme.onPrimaryContainer else LocalContentColor.current)
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
                                                        } else {
                                                            Toast.makeText(context, translateText("Файл публичного ключа не найден", context), Toast.LENGTH_SHORT).show()
                                                        }
                                                    }
                                                )
                                                DropdownMenuItem(
                                                    text = { Text(translateText("Удалить", context), color = MaterialTheme.colorScheme.error) },
                                                    leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
                                                    onClick = {
                                                        menuExpanded = false
                                                        coroutineScope.launch {
                                                            SshKeyManager(context).deleteKeyFiles(key.id)
                                                            if (selectedKeyId == key.id) {
                                                                onSelectKey(null)
                                                            }
                                                        }
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            Divider()
                        }
                    }
                }
                      Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth().height(48.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { showImportDialog = true },
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                            Icon(Icons.Default.Upload, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(translateText("Импорт ключа", context), fontSize = 13.sp, textAlign = TextAlign.Center, maxLines = 1)
                        }
                    }
                    Button(
                        onClick = { showGenerateDialog = true },
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(translateText("Новый ключ", context), fontSize = 13.sp, textAlign = TextAlign.Center, maxLines = 1)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                    Text(translateText("Закрыть", context))
                }
            }
        }
    }

    if (showImportDialog) {
        ImportKeyDialog(onDismiss = { showImportDialog = false }, onKeyImported = { id -> 
            onSelectKey(id)
            showImportDialog = false
        })
    }

    if (showGenerateDialog) {
        var newAlias by remember { mutableStateOf("My Key") }
        var algorithm by remember { mutableStateOf(KeyPair.ED25519) }
        var passphrase by remember { mutableStateOf("") }
        var isAlgorithmDropdownExpanded by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { if (!isGenerating) showGenerateDialog = false },
            title = { Text(translateText("Генерация SSH-ключа", context)) },
            text = {
                Column {
                    OutlinedTextField(
                        value = newAlias,
                        onValueChange = { newAlias = it },
                        label = { Text(translateText("Имя ключа", context)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    ExposedDropdownMenuBox(
                        expanded = isAlgorithmDropdownExpanded,
                        onExpandedChange = { isAlgorithmDropdownExpanded = !isAlgorithmDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = if (algorithm == KeyPair.ED25519) "ED25519" else "RSA 2048",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(translateText("Алгоритм", context)) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isAlgorithmDropdownExpanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = isAlgorithmDropdownExpanded,
                            onDismissRequest = { isAlgorithmDropdownExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("ED25519 (Рекомендуется)") },
                                onClick = { algorithm = KeyPair.ED25519; isAlgorithmDropdownExpanded = false }
                            )
                            DropdownMenuItem(
                                text = { Text("RSA 2048") },
                                onClick = { algorithm = KeyPair.RSA; isAlgorithmDropdownExpanded = false }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    if (algorithm != KeyPair.ED25519) {
                        OutlinedTextField(
                            value = passphrase,
                            onValueChange = { passphrase = it },
                            label = { Text(translateText("Пароль для ключа (опционально)", context)) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
                    Text(
                        text = translateText("Генерация ключей производится в ОЗУ. Приватный ключ будет надежно зашифрован.", context) +
                               if (algorithm == KeyPair.ED25519) "\n\n" + translateText("Примечание: для ED25519 пароль ключа не поддерживается", context) else "",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        isGenerating = true
                        coroutineScope.launch {
                            try {
                                val manager = SshKeyManager(context)
                                val entity = manager.generateAndSaveKey(
                                    alias = newAlias,
                                    algorithm = algorithm,
                                    passphrase = passphrase.ifEmpty { null }
                                )
                                onSelectKey(entity.id)
                                showGenerateDialog = false
                            } catch (e: Throwable) {
                                e.printStackTrace()
                                Toast.makeText(context, "Error: ${e.message ?: e.toString()}", Toast.LENGTH_LONG).show()
                            } finally {
                                isGenerating = false
                            }
                        }
                    },
                    enabled = !isGenerating && newAlias.isNotBlank()
                ) {
                    if (isGenerating) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                    } else {
                        Text(translateText("Сгенерировать", context))
                    }
                }
            },
            dismissButton = {
                if (!isGenerating) {
                    TextButton(onClick = { showGenerateDialog = false }) {
                        Text(translateText("Отмена", context))
                    }
                }
            }
        )
    }
}

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
    var passwordVisible by remember { mutableStateOf(false) }
    var publicKeyText by remember { mutableStateOf("") }
    
    var parsedKeyType by remember { mutableStateOf<Int?>(null) }
    var isEncrypted by remember { mutableStateOf(false) }
    var isDecrypted by remember { mutableStateOf(true) }
    
    var errorMsg by remember { mutableStateOf<String?>(null) }
    var showDsaWarning by remember { mutableStateOf(false) }
    var dsaWarningShownFor by remember { mutableStateOf<String?>(null) }
    
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
            
            val currentKeyString = String(bytes)
            if (kp.keyType == KeyPair.DSA && dsaWarningShownFor != currentKeyString) {
                showDsaWarning = true
                dsaWarningShownFor = currentKeyString
            }
            
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
                            placeholder = { Text(translateText("Вставьте сюда текст ключа", context)) },
                            maxLines = 3,
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = androidx.compose.ui.text.TextStyle(fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace, fontSize = 12.sp),
                            shape = RoundedCornerShape(12.dp)
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
                                visualTransformation = if (passwordVisible) androidx.compose.ui.text.input.VisualTransformation.None else androidx.compose.ui.text.input.PasswordVisualTransformation(),
                                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Password),
                                trailingIcon = {
                                    val image = if (passwordVisible) Icons.Default.Clear else Icons.Default.Lock
                                    val description = translateText(if (passwordVisible) "Скрыть пароль" else "Показать пароль", context)
                                    
                                    val isTv = context.packageManager.hasSystemFeature("android.software.leanback")
                                    val interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
                                    val isFocused by interactionSource.collectIsFocusedAsState()
                                    
                                    IconButton(
                                        onClick = { passwordVisible = !passwordVisible },
                                        interactionSource = interactionSource,
                                        modifier = Modifier.background(if (isTv && isFocused) MaterialTheme.colorScheme.surfaceVariant else androidx.compose.ui.graphics.Color.Transparent, androidx.compose.foundation.shape.CircleShape)
                                    ) {
                                        Icon(imageVector = image, contentDescription = description)
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
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
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
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
                        placeholder = { Text(translateText("Вставьте сюда текст ключа", context)) },
                        maxLines = 3,
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = androidx.compose.ui.text.TextStyle(fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace, fontSize = 12.sp),
                        shape = RoundedCornerShape(12.dp)
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
                        enabled = !isSaving && isDecrypted && alias.isNotBlank() && parsedKeyType != null && (parsedKeyType != KeyPair.ED25519 || !isEncrypted) && parsedKeyType != KeyPair.DSA
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
        val resetState = {
            inputMode = 1
            privateKeyText = ""
            privateKeyBytes = null
            fileName = null
            alias = ""
            passphrase = ""
            passwordVisible = false
            publicKeyText = ""
            parsedKeyType = null
            isEncrypted = false
            isDecrypted = true
            errorMsg = null
            dsaWarningShownFor = null
            showDsaWarning = false
        }
        
        if (showDsaWarning) {
            AlertDialog(
                onDismissRequest = { resetState() },
                title = { Text(translateText("Внимание", context)) },
                text = { Text(translateText("DSA-ключи не поддерживаются современными версиями SSH-сервера на OpenWrt - рекомендуется ED25519, RSA или ECDSA", context)) },
                confirmButton = {
                    TextButton(onClick = { resetState() }) {
                        Text(translateText("Понятно", context))
                    }
                }
            )
        }
    }
}
