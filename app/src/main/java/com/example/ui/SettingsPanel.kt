@file:OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.RouterConfig
import com.example.translateText
import com.example.checkUpdate
import com.example.downloadAndInstallApk
import com.example.UpdateInfo

import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.clip

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.text.KeyboardOptions
import kotlinx.coroutines.launch

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.lazy.rememberLazyListState


@Composable
fun SettingsPanel(
    state: UiState,
    viewModel: RouterViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val isTv = remember { context.packageManager.hasSystemFeature("android.software.leanback") }
    val isTablet = configuration.screenWidthDp >= 600
    val scaleModifier = if (isTv || isTablet) Modifier.fillMaxSize().padding(vertical = 20.dp, horizontal = 16.dp) else Modifier.fillMaxSize().padding(16.dp)
    
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Box(modifier = scaleModifier) {

    var editingConfig by remember { mutableStateOf<RouterConfig?>(null) }
    var isAddingNew by remember { mutableStateOf(false) }

    if (state.allConfigs.isEmpty() || isAddingNew || editingConfig != null) {
        val configToEdit = editingConfig ?: RouterConfig(
            ipAddress = "192.168.1.1",
            username = "root",
            sshKeyOrPassword = "",
            isActive = true
        )
        EditRouterForm(
            modifier = if (isTablet) Modifier.fillMaxSize() else Modifier,
            config = configToEdit,
            isFirst = state.allConfigs.isEmpty(),
            isTv = isTv,
            connectionError = state.connectionError,
            isConnecting = state.isConnecting,
            isConnectionVerified = state.isConnectionVerified,
            onSave = { name, ip, port, user, pass, led, wg ->
                viewModel.saveConfig(configToEdit.id, name, ip, port, user, pass, led, wg)
                isAddingNew = false
                editingConfig = null
            },
            onTest = { ip, port, user, pass ->
                viewModel.testConnection(ip, port, user, pass)
            },
            onCancel = {
                if (state.allConfigs.isNotEmpty()) {
                    isAddingNew = false
                    editingConfig = null
                }
            },
            onFieldChanged = {
                viewModel.clearConnectionError()
            }
        )
    } else {
        RouterProfilesList(
            modifier = if (isTablet) Modifier.fillMaxSize() else Modifier,
            configs = state.allConfigs,
            activeConfigId = state.config?.id,
            onSelect = { id ->
                if (state.config?.id != id) {
                    viewModel.switchConfig(id) {
                        val packageManager = context.packageManager
                        val intent = packageManager.getLaunchIntentForPackage(context.packageName)
                        val componentName = intent!!.component
                        val mainIntent = android.content.Intent.makeRestartActivityTask(componentName)
                        context.startActivity(mainIntent)
                        Runtime.getRuntime().exit(0)
                    }
                } else {
                    onDismiss()
                }
            },
            onEdit = { config ->
                editingConfig = config
            },
            onDelete = { id ->
                viewModel.deleteConfig(id)
            },
            onAddNew = {
                viewModel.clearConnectionError()
                isAddingNew = true
            },
            onDismiss = onDismiss
        )
    }
        }
    }}

@Composable
fun RouterProfilesList(
    modifier: Modifier = Modifier,
    configs: List<RouterConfig>,
    activeConfigId: Int?,
    onSelect: (Int) -> Unit,
    onEdit: (RouterConfig) -> Unit,
    onDelete: (Int) -> Unit,
    onAddNew: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var showAboutDialog by remember { mutableStateOf(false) }
    var updateInfo by remember { mutableStateOf<UpdateInfo?>(null) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var isCheckingForUpdate by remember { mutableStateOf(false) }

    BackHandler {
        onDismiss()
    }

    if (showAboutDialog) {
        val currentVersion = remember {
            try {
                context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: "v1.0.0"
            } catch (e: Exception) {
                "v1.0.0"
            }
        }
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = { Text(translateText("О приложении", context), fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(translateText("Openwrt Router Control", context), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("${translateText("Версия", context)}: $currentVersion")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(translateText("Android application for managing OpenWrt routers.", context))
                }
            },
            confirmButton = {
                Button(onClick = { showAboutDialog = false }) {
                    Text(translateText("Закрыть", context))
                }
            }
        )
    }

    updateInfo?.let { info ->
        AlertDialog(
            onDismissRequest = { updateInfo = null },
            title = { Text(translateText("Доступно обновление", context), fontWeight = FontWeight.Bold) },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(translateText("Найдена новая версия:", context) + " ${info.version}", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyColumn(modifier = Modifier.weight(1f, fill = false)) {
                        item {
                            Text(info.releaseNotes, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            },
            confirmButton = {
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(onClick = {
                        downloadAndInstallApk(context, info.apkUrl)
                        updateInfo = null
                    }) {
                        Text(translateText("Обновить", context))
                    }
                    Button(onClick = { updateInfo = null }) {
                        Text(translateText("Отмена", context))
                    }
                }
            }
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = translateText("Сохраненные роутеры", context),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                val closeInteractionSource = remember { MutableInteractionSource() }
                val isCloseFocused by closeInteractionSource.collectIsFocusedAsState()
                IconButton(
                    onClick = onDismiss,
                    interactionSource = closeInteractionSource,
                    modifier = Modifier
                        .background(if (isCloseFocused) MaterialTheme.colorScheme.primaryContainer else Color.Transparent, CircleShape)
                ) {
                    Icon(
                        Icons.Default.Close, 
                        contentDescription = translateText("Закрыть", context),
                        tint = if (isCloseFocused) MaterialTheme.colorScheme.onPrimaryContainer else LocalContentColor.current
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = configs, key = { it.id }) { config ->
                    val isActive = config.id == activeConfigId
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isActive) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val rowInteractionSource = remember { MutableInteractionSource() }
                            val isRowFocused by rowInteractionSource.collectIsFocusedAsState()
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(if (isRowFocused) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f) else Color.Transparent, RoundedCornerShape(8.dp))
                                    .clickable(
                                        interactionSource = rowInteractionSource,
                                        indication = LocalIndication.current
                                    ) { onSelect(config.id) }
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isActive,
                                    onClick = null
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = config.profileName,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isActive) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "${config.ipAddress}:${config.port}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = if (isActive) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                    )
                                }
                            }
                            val editInteractionSource = remember { MutableInteractionSource() }
                            val isEditFocused by editInteractionSource.collectIsFocusedAsState()
                            val deleteInteractionSource = remember { MutableInteractionSource() }
                            val isDeleteFocused by deleteInteractionSource.collectIsFocusedAsState()
                            IconButton(
                                onClick = { onEdit(config) },
                                interactionSource = editInteractionSource,
                                modifier = Modifier
                                    .background(if (isEditFocused) MaterialTheme.colorScheme.primaryContainer else Color.Transparent, CircleShape)
                            ) {
                                Icon(
                                    Icons.Default.Edit, 
                                    contentDescription = translateText("Редактировать", context),
                                    tint = if (isEditFocused) MaterialTheme.colorScheme.onPrimaryContainer else LocalContentColor.current
                                )
                            }
                            IconButton(
                                onClick = { onDelete(config.id) },
                                interactionSource = deleteInteractionSource,
                                modifier = Modifier
                                    .background(if (isDeleteFocused) MaterialTheme.colorScheme.errorContainer else Color.Transparent, CircleShape)
                            ) {
                                Icon(
                                    Icons.Default.Delete, 
                                    contentDescription = translateText("Удалить", context),
                                    tint = if (isDeleteFocused) MaterialTheme.colorScheme.onErrorContainer else LocalContentColor.current
                                )
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            val addInteractionSource = remember { MutableInteractionSource() }
            val isAddFocused by addInteractionSource.collectIsFocusedAsState()
            Button(
                onClick = onAddNew,
                interactionSource = addInteractionSource,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isAddFocused) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.secondary,
                    contentColor = if (isAddFocused) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onSecondary
                )
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = translateText("Добавить новый роутер", context))
            }
            
            Divider(modifier = Modifier.padding(vertical = 16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth().clickable { showAboutDialog = true }.padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.width(16.dp))
                Text(translateText("О приложении", context), style = MaterialTheme.typography.titleMedium)
            }
            
            Row(
                modifier = Modifier.fillMaxWidth().clickable {
                    if (!isCheckingForUpdate) {
                        isCheckingForUpdate = true
                        coroutineScope.launch {
                            val lang = context.resources.configuration.locales[0].language
                            val result = checkUpdate(lang)
                            if (result != null) {
                                val currentVersion = context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: "v1.0.0"
                                val latestVer = result.version.replace("v", "").replace(".", "").toIntOrNull() ?: 0
                                val currentVer = currentVersion.replace("v", "").replace(".", "").toIntOrNull() ?: 0
                                if (latestVer > currentVer) {
                                    updateInfo = result
                                } else {
                                    android.widget.Toast.makeText(context, translateText("У вас установлена последняя версия", context), android.widget.Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                android.widget.Toast.makeText(context, translateText("Ошибка при проверке обновлений", context), android.widget.Toast.LENGTH_SHORT).show()
                            }
                            isCheckingForUpdate = false
                        }
                    }
                }.padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isCheckingForUpdate) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Default.Refresh, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(translateText("Проверить обновление", context), style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
fun EditRouterForm(
    modifier: Modifier = Modifier,
    config: RouterConfig,
    isFirst: Boolean,
    isTv: Boolean,
    connectionError: String?,
    isConnecting: Boolean,
    isConnectionVerified: Boolean,
    onSave: (String, String, Int, String, String, String, String) -> Unit,
    onTest: (String, Int, String, String) -> Unit,
    onCancel: () -> Unit,
    onFieldChanged: () -> Unit
) {
    val context = LocalContext.current
    var profileName by remember { mutableStateOf(config.profileName) }
    var ip by remember { mutableStateOf(config.ipAddress) }
    var port by remember { mutableStateOf(config.port.toString()) }
    var user by remember { mutableStateOf(config.username) }
    var password by remember { mutableStateOf(config.sshKeyOrPassword) }
    var passwordVisible by remember { mutableStateOf(false) }
    var isPasswordFocused by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()
    val isImeVisible = WindowInsets.isImeVisible
    LaunchedEffect(isImeVisible) {
        if (isImeVisible && isPasswordFocused) {
            kotlinx.coroutines.delay(200)
            listState.animateScrollBy(10000f)
        }
    }
    val coroutineScope = rememberCoroutineScope()
    if (!isFirst) {
        BackHandler {
            onCancel()
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .imePadding(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = if (isTv) 0.dp else 24.dp)
                .fillMaxWidth()
,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (isFirst) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = translateText("Нет настроенных роутеров", context),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Text(
                            text = translateText("Пожалуйста, добавьте первый профиль роутера для продолжения.", context),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = profileName,
                    onValueChange = { profileName = it; onFieldChanged() },
                    label = { Text(translateText("Имя профиля", context)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(
                        value = ip,
                        onValueChange = { ip = it; onFieldChanged() },
                        label = { Text(translateText("IP адрес / Хост", context)) },
                        singleLine = true,
                        modifier = Modifier.weight(0.7f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = port,
                        onValueChange = { port = it; onFieldChanged() },
                        label = { Text(translateText("Порт", context)) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(0.3f),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
            item {
                OutlinedTextField(
                    value = user,
                    onValueChange = { user = it; onFieldChanged() },
                    label = { Text(translateText("Имя пользователя", context)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }
            item {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = password,
                        onValueChange = { 
                            password = it
                            onFieldChanged()
                        },
                        label = { Text(translateText("Пароль или приватный ключ", context)) },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                                                singleLine = true,
                        modifier = Modifier.weight(1f).onFocusChanged {
                            isPasswordFocused = it.isFocused
                            if (it.isFocused) {
                                coroutineScope.launch {
                                    kotlinx.coroutines.delay(300)
                                    listState.animateScrollBy(10000f)
                                }
                            }
                        },
                        shape = RoundedCornerShape(12.dp)
                    )
                    val image = if (passwordVisible) Icons.Default.Clear else Icons.Default.Lock
                    val iconInteractionSource = remember { MutableInteractionSource() }
                    val isIconFocused by iconInteractionSource.collectIsFocusedAsState()
                    IconButton(
                        onClick = { passwordVisible = !passwordVisible },
                        interactionSource = iconInteractionSource,
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .background(if (isIconFocused) MaterialTheme.colorScheme.primaryContainer else Color.Transparent, CircleShape)
                    ) {
                        Icon(
                            imageVector = image, 
                            contentDescription = null,
                            tint = if (isIconFocused) MaterialTheme.colorScheme.onPrimaryContainer else LocalContentColor.current
                        )
                    }
                }
            }

            if (connectionError != null) {
                item {
                    Text(
                        text = translateText(connectionError, context),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    if (!isFirst) {
                        val cancelInteractionSource = remember { MutableInteractionSource() }
                        val isCancelFocused by cancelInteractionSource.collectIsFocusedAsState()
                        OutlinedButton(
                            onClick = onCancel,
                            interactionSource = cancelInteractionSource,
                            modifier = Modifier.weight(1f).height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (isCancelFocused) MaterialTheme.colorScheme.tertiary else Color.Transparent,
                                contentColor = if (isCancelFocused) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(translateText("Отмена", context))
                        }
                    }
                    val testInteractionSource = remember { MutableInteractionSource() }
                    val isTestFocused by testInteractionSource.collectIsFocusedAsState()
                    Button(
                        onClick = {
                            if (!isConnecting) {
                                val p = port.toIntOrNull() ?: 22
                                onTest(ip, p, user, password)
                            }
                        },
                        interactionSource = testInteractionSource,
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        enabled = true,
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isTestFocused) MaterialTheme.colorScheme.tertiary else if (isConnectionVerified) androidx.compose.ui.graphics.Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary,
                            contentColor = if (isTestFocused) MaterialTheme.colorScheme.onTertiary else if (isConnectionVerified) androidx.compose.ui.graphics.Color.White else MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        if (isConnecting) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                        } else {
                            Text(
                                text = if (isConnectionVerified) translateText("Connected", context) else translateText("Test connection", context),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }

            item {
                val saveInteractionSource = remember { MutableInteractionSource() }
                val isSaveFocused by saveInteractionSource.collectIsFocusedAsState()
                Button(
                    onClick = {
                        val p = port.toIntOrNull() ?: 22
                        onSave(profileName, ip, p, user, password, config.ledBehavior, config.wgInterface)
                    },
                    interactionSource = saveInteractionSource,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSaveFocused) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(translateText("Сохранить профиль", context), color = if (isSaveFocused) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onPrimary)
                }
            }
            item {
                Spacer(modifier = if (isTv) Modifier.height(1.dp) else Modifier.height(20.dp))
            }
        }
    }
}
