package com.example
import androidx.compose.ui.res.stringResource

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.BackHandler
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import com.example.ui.SettingsPanel
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.focusable
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusProperties
import kotlinx.coroutines.delay
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.data.ConsoleLog
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import com.example.ui.*
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.graphics.StrokeCap

private val StarFilledIcon = androidx.compose.ui.graphics.vector.ImageVector.Builder(
    name = "StarFilled",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    path(
        fill = SolidColor(androidx.compose.ui.graphics.Color(0xFFFFFFFF)),
        pathFillType = PathFillType.NonZero
    ) {
        moveTo(12.0f, 17.27f)
        lineTo(18.18f, 21.0f)
        lineTo(16.54f, 13.97f)
        lineTo(22.0f, 9.24f)
        lineTo(14.81f, 8.63f)
        lineTo(12.0f, 2.0f)
        lineTo(9.19f, 8.63f)
        lineTo(2.0f, 9.24f)
        lineTo(7.46f, 13.97f)
        lineTo(5.82f, 21.0f)
        close()
    }
}.build()

private val StarOutlineIcon = androidx.compose.ui.graphics.vector.ImageVector.Builder(
    name = "StarOutline",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    path(
        stroke = SolidColor(androidx.compose.ui.graphics.Color(0xFFFFFFFF)),
        strokeLineWidth = 2f,
        strokeAlpha = 1.0f,
        strokeLineCap = StrokeCap.Round,
        pathFillType = PathFillType.NonZero
    ) {
        moveTo(12.0f, 17.27f)
        lineTo(18.18f, 21.0f)
        lineTo(16.54f, 13.97f)
        lineTo(22.0f, 9.24f)
        lineTo(14.81f, 8.63f)
        lineTo(12.0f, 2.0f)
        lineTo(9.19f, 8.63f)
        lineTo(2.0f, 9.24f)
        lineTo(7.46f, 13.97f)
        lineTo(5.82f, 21.0f)
        close()
    }
}.build()

private val CopyIcon = androidx.compose.ui.graphics.vector.ImageVector.Builder(
    name = "Copy",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    path(
        stroke = SolidColor(androidx.compose.ui.graphics.Color(0xFF64D2FF)),
        strokeLineWidth = 2f,
        strokeLineCap = StrokeCap.Round
    ) {
        moveTo(4f, 20f)
        lineTo(4f, 6f)
        lineTo(14f, 6f)
    }
    path(
        stroke = SolidColor(androidx.compose.ui.graphics.Color(0xFFFFB300)),
        strokeLineWidth = 2f,
        strokeLineCap = StrokeCap.Round
    ) {
        moveTo(8f, 10f)
        lineTo(8f, 22f)
        lineTo(18f, 22f)
        lineTo(18f, 10f)
        close()
    }
}.build()

private val PasteIcon = androidx.compose.ui.graphics.vector.ImageVector.Builder(
    name = "Paste",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    path(
        stroke = SolidColor(androidx.compose.ui.graphics.Color(0xFF64D2FF)),
        strokeLineWidth = 2f,
        strokeLineCap = StrokeCap.Round
    ) {
        moveTo(9f, 2f)
        lineTo(15f, 2f)
        lineTo(15f, 5f)
        lineTo(9f, 5f)
        close()
    }
    path(
        stroke = SolidColor(androidx.compose.ui.graphics.Color(0xFF64D2FF)),
        strokeLineWidth = 2f,
        strokeLineCap = StrokeCap.Round
    ) {
        moveTo(9f, 4f)
        lineTo(5f, 4f)
        lineTo(5f, 22f)
        lineTo(19f, 22f)
        lineTo(19f, 4f)
        lineTo(15f, 4f)
    }
    path(
        stroke = SolidColor(androidx.compose.ui.graphics.Color(0xFF64D2FF)),
        strokeLineWidth = 2f,
        strokeLineCap = StrokeCap.Round
    ) {
        moveTo(9f, 10f)
        lineTo(15f, 10f)
    }
    path(
        stroke = SolidColor(androidx.compose.ui.graphics.Color(0xFF64D2FF)),
        strokeLineWidth = 2f,
        strokeLineCap = StrokeCap.Round
    ) {
        moveTo(9f, 14f)
        lineTo(15f, 14f)
    }
}.build()

private val TerminalIcon = androidx.compose.ui.graphics.vector.ImageVector.Builder(
    name = "Terminal",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    path(
        fill = SolidColor(androidx.compose.ui.graphics.Color.Black),
        pathFillType = PathFillType.NonZero
    ) {
        moveTo(6f, 6f)
        lineTo(12f, 12f)
        lineTo(6f, 18f)
        lineTo(8.5f, 18f)
        lineTo(14.5f, 12f)
        lineTo(8.5f, 6f)
        close()
    }
    path(
        fill = SolidColor(androidx.compose.ui.graphics.Color.Black),
        pathFillType = PathFillType.NonZero
    ) {
        moveTo(14f, 16f)
        lineTo(20f, 16f)
        lineTo(20f, 18f)
        lineTo(14f, 18f)
        close()
    }
}.build()

class MainActivity : ComponentActivity() {
    private val viewModel: RouterViewModel by viewModels {
        RouterViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (androidx.core.content.ContextCompat.checkSelfPermission(
                    this, android.Manifest.permission.POST_NOTIFICATIONS
                ) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                androidx.core.app.ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    1002
                )
            }
        }

        handleIntent(intent)

        // Initialize and push all dynamic shortcuts to the system
        try {
            ShortcutHelper.pushShortcutById(this, "led_on")
            ShortcutHelper.pushShortcutById(this, "led_off")
            ShortcutHelper.pushShortcutById(this, "reboot")
        } catch (e: Throwable) {
            android.util.Log.e("MainActivity", "Failed to initialize dynamic shortcuts", e)
        }

        setContent {
            MyApplicationTheme {
                MainScreen(viewModel)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        stopService(android.content.Intent(this, SshForegroundService::class.java))
    }

    override fun onStop() {
        super.onStop()
        val intent = android.content.Intent(this, SshForegroundService::class.java)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: android.content.Intent?) {
        if (intent == null) return
        val command = intent.getStringExtra("command") ?: return
        android.util.Log.d("MainActivity", "Handling shortcut command: $command")
        when (command) {
            "led_on" -> viewModel.toggleLed(true)
            "led_off" -> viewModel.toggleLed(false)
            "reboot" -> viewModel.rebootRouter()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MainScreen(viewModel: RouterViewModel) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val isTv = remember {
        context.packageManager.hasSystemFeature(android.content.pm.PackageManager.FEATURE_LEANBACK)
    }
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val sessionCommandHistory by viewModel.sessionCommandHistory.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    var showRebootConfirm by remember { mutableStateOf(false) }
    var isTestFullScreen by remember { mutableStateOf(false) }
    var speedTestUrl by remember { mutableStateOf("https://speed.measurementlab.net/") }
    var isIPerfFullScreen by remember { mutableStateOf(false) }
    val bottomNavTestTabFocusRequester = remember { androidx.compose.ui.focus.FocusRequester() }
    val testTabLastCardFocusRequester = remember { androidx.compose.ui.focus.FocusRequester() }
    val setIPerfFullScreen = { value: Boolean ->
        isIPerfFullScreen = value
    }
    var isWifiAnalyzerFullScreen by remember { mutableStateOf(false) }

    var updateInfo by remember { mutableStateOf<UpdateInfo?>(null) }
    
    LaunchedEffect(Unit) {
        val lang = context.resources.configuration.locales[0].language
        val info = checkUpdate(lang)
        if (info != null) {
            val currentVersion = context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: "v1.0.0"
            val latestVer = info.version.replace("v", "").replace(".", "").toIntOrNull() ?: 0
            val currentVer = currentVersion.replace("v", "").replace(".", "").toIntOrNull() ?: 0
            if (latestVer > currentVer) {
                val prefs = context.getSharedPreferences("app_settings", android.content.Context.MODE_PRIVATE)
                val skippedVersion = prefs.getString("skipped_update_version", "")
                if (info.version != skippedVersion) {
                    updateInfo = info
                }
            }
        }
    }
    
    updateInfo?.let { info ->
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { updateInfo = null },
            title = { Text(translateText("Доступно обновление", context), fontWeight = FontWeight.Bold) },
            text = {
                val uriHandler = androidx.compose.ui.platform.LocalUriHandler.current
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(translateText("Найдена новая версия:", context) + " ${info.version}", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyColumn(modifier = Modifier.weight(1f, fill = false)) {
                        item {
                            Text(info.releaseNotes, style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                            val downloadText = "- " + translateText("Скачать обновление вы можете вручную с https://github.com/Vihtoor/openwrt-router-control/releases", context)
                            val url = "https://github.com/Vihtoor/openwrt-router-control/releases"
                            val startIndex = downloadText.indexOf(url)
                            if (startIndex != -1) {
                                val annotatedString = androidx.compose.ui.text.buildAnnotatedString {
                                    append(downloadText)
                                    addStyle(
                                        style = androidx.compose.ui.text.SpanStyle(textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline),
                                        start = startIndex,
                                        end = startIndex + url.length
                                    )
                                    addStringAnnotation(
                                        tag = "URL",
                                        annotation = url,
                                        start = startIndex,
                                        end = startIndex + url.length
                                    )
                                }
                                androidx.compose.foundation.text.ClickableText(
                                    text = annotatedString,
                                    onClick = { offset ->
                                        annotatedString.getStringAnnotations(tag = "URL", start = offset, end = offset)
                                            .firstOrNull()?.let { annotation ->
                                                uriHandler.openUri(annotation.item)
                                            }
                                    },
                                    style = MaterialTheme.typography.bodySmall.copy(color = androidx.compose.ui.graphics.Color(0xFF4CAF50))
                                )
                            } else {
                                Text(downloadText, style = MaterialTheme.typography.bodySmall, color = androidx.compose.ui.graphics.Color(0xFF4CAF50), modifier = Modifier.clickable { uriHandler.openUri(url) })
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("- " + translateText("Проверить обновление можно вручную в разделе настроек О приложении", context), style = MaterialTheme.typography.bodySmall, color = androidx.compose.ui.graphics.Color(0xFF4CAF50))
                        }
                    }
                }
            },
            confirmButton = {
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    androidx.compose.material3.Button(onClick = {
                        val prefs = context.getSharedPreferences("app_settings", android.content.Context.MODE_PRIVATE)
                        prefs.edit().putString("skipped_update_version", info.version).apply()
                        updateInfo = null
                    }) {
                        Text(translateText("Выйти и больше не спрашивать", context), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                    }
                    androidx.compose.material3.Button(onClick = {
                        downloadAndInstallApk(context, info.apkUrl)
                        updateInfo = null
                    }) {
                        Text(translateText("Обновить", context))
                    }
                    androidx.compose.material3.Button(onClick = { updateInfo = null }) {
                        Text(translateText("Выход", context))
                    }
                }
            }
        )
    }

    LaunchedEffect(state.currentTab) {
        if (state.currentTab != TabType.TEST) {
            isTestFullScreen = false
            isIPerfFullScreen = false
            isWifiAnalyzerFullScreen = false
        } else {
            viewModel.clearConsoleLogs()
        }
    }

    LaunchedEffect(isIPerfFullScreen) {
        if (isIPerfFullScreen) {
            viewModel.clearConsoleLogs()
            IperfService.start(context.applicationContext)
        } else {
            IperfService.stop(context.applicationContext)
        }
    }

    var showExitConfirm by remember { mutableStateOf(false) }

    if (state.config != null) {
        if (state.isConfiguring) {
            BackHandler {
                viewModel.setConfiguring(false)
            }
        } else if (state.currentTab == TabType.TEST && isWifiAnalyzerFullScreen) {
            BackHandler {
                isWifiAnalyzerFullScreen = false
            }
        } else if (state.currentTab == TabType.TEST && isIPerfFullScreen) {
            BackHandler {
                setIPerfFullScreen(false)
            }
        } else if (state.currentTab == TabType.TEST && isTestFullScreen) {
            BackHandler {
                isTestFullScreen = false
            }
        } else if (state.currentTab == TabType.TEST) {
            BackHandler {
                viewModel.switchTab(TabType.DASHBOARD)
            }
        } else if (state.currentTab == TabType.CONSOLE) {
            BackHandler {
                viewModel.switchTab(TabType.DASHBOARD)
            }
        } else if (state.currentTab == TabType.DASHBOARD) {
            BackHandler {
                showExitConfirm = true
            }
        }
    } else if (!state.isConfiguring) {
        BackHandler {
            showExitConfirm = true
        }
    }

    if (showExitConfirm) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showExitConfirm = false },
            title = { Text(translateText("Выйти из приложения?", context)) },
            text = { Text(translateText("Вы действительно хотите закрыть приложение?", context)) },
            confirmButton = {
                androidx.compose.material3.TextButton(onClick = { 
                    showExitConfirm = false
                    (context as? android.app.Activity)?.finishAffinity()
                    kotlin.system.exitProcess(0)
                }) {
                    Text(translateText("Да", context))
                }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(onClick = { showExitConfirm = false }) {
                    Text(translateText("Отмена", context))
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            textContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.onAppResume()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("app_scaffold"),
        topBar = {
            if (!(state.config == null || state.isConfiguring) && state.currentTab != TabType.CONSOLE && (state.currentTab != TabType.TEST || (!isTestFullScreen && !isIPerfFullScreen && !isWifiAnalyzerFullScreen))) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .height(if (isTv) 44.8.dp else 56.dp),
                    color = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
                ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, end = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        val hasRouterData = state.config != null &&
                                !state.isConnecting &&
                                state.status.publicIp != "Connecting..." &&
                                state.status.publicIp != "Offline / Error" &&
                                state.status.model != "Detecting..." &&
                                state.status.model != "Offline / Error" &&
                                state.status.version != "Detecting..." &&
                                state.status.version != "Offline / Error"

                        val titleText = if (hasRouterData) {
                            val openWrtText = if (state.status.version.lowercase().contains("openwrt")) {
                                state.status.version
                            } else {
                                "OpenWrt ${state.status.version}"
                            }
                            "$openWrtText @ ${state.status.model}"
                        } else {
                            "Router Control"
                        }

                        Text(
                            text = titleText,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (hasRouterData) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.basicMarquee(
                                iterations = Int.MAX_VALUE,
                                animationMode = MarqueeAnimationMode.Immediately,
                                repeatDelayMillis = 0,
                                initialDelayMillis = 0,
                                spacing = androidx.compose.foundation.MarqueeSpacing(32.dp),
                                velocity = 40.dp
                            ),
                            maxLines = 1
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        if (state.config != null) {
                            IconButton(
                                onClick = { viewModel.setConfiguring(true) },
                                modifier = Modifier
                                    .size(48.dp)
                                    .testTag("settings_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "Настройки",
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        }
                    }
                }
            }
            }
        },
        bottomBar = {
            if (state.config != null && !WindowInsets.isImeVisible && !(state.currentTab == TabType.TEST && (isTestFullScreen || isIPerfFullScreen || isWifiAnalyzerFullScreen)) && !(isTv && state.currentTab == TabType.CONSOLE)) {
                var isDashboardFocused by remember { mutableStateOf(false) }
                val isDashboardActive = (state.currentTab == TabType.DASHBOARD) || isDashboardFocused

                var isConsoleFocused by remember { mutableStateOf(false) }
                val isConsoleActive = (state.currentTab == TabType.CONSOLE) || isConsoleFocused

                var isTestFocused by remember { mutableStateOf(false) }
                val isTestActive = (state.currentTab == TabType.TEST) || isTestFocused

                var isRebootFocused by remember { mutableStateOf(false) }
                val isRebootActive = isRebootFocused

                Box(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
                ) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .height(65.6.dp),
                        windowInsets = WindowInsets(0.dp)
                    ) {
                    NavigationBarItem(
                        selected = state.currentTab == TabType.DASHBOARD,
                        onClick = { viewModel.switchTab(TabType.DASHBOARD) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = if (isDashboardFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            unselectedTextColor = if (isDashboardFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            indicatorColor = Color.Transparent
                        ),
                        icon = { 
                            Icon(
                                imageVector = Icons.Default.Home, 
                                contentDescription = "Главная",
                                modifier = Modifier.size(if (isDashboardActive) 22.dp else 20.dp)
                            ) 
                        },
                        label = { 
                            Text(
                                text = "Главная",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 11.sp,
                                    fontWeight = if (isDashboardActive) FontWeight.Bold else FontWeight.Normal
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            ) 
                        },
                        modifier = Modifier
                            .onFocusChanged { isDashboardFocused = it.isFocused }
                            .testTag("tab_dashboard"),
                        alwaysShowLabel = true
                    )
                    NavigationBarItem(
                        selected = state.currentTab == TabType.CONSOLE,
                        onClick = { viewModel.switchTab(TabType.CONSOLE) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = if (isConsoleFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            unselectedTextColor = if (isConsoleFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            indicatorColor = Color.Transparent
                        ),
                        icon = { 
                            Icon(
                                imageVector = TerminalIcon, 
                                contentDescription = "Консоль",
                                modifier = Modifier.size(if (isConsoleActive) 22.dp else 20.dp)
                            ) 
                        },
                        label = { 
                            Text(
                                text = "Консоль",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 11.sp,
                                    fontWeight = if (isConsoleActive) FontWeight.Bold else FontWeight.Normal
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            ) 
                        },
                        modifier = Modifier
                            .onFocusChanged { isConsoleFocused = it.isFocused }
                            .testTag("tab_console"),
                        alwaysShowLabel = true
                    )
                    NavigationBarItem(
                        selected = state.currentTab == TabType.TEST,
                        onClick = { viewModel.switchTab(TabType.TEST) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = if (isTestFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            unselectedTextColor = if (isTestFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            indicatorColor = Color.Transparent
                        ),
                        icon = { 
                            Icon(
                                imageVector = Icons.Default.Build, 
                                contentDescription = "Инструменты",
                                modifier = Modifier.size(if (isTestActive) 22.dp else 20.dp)
                            ) 
                        },
                        label = { 
                            Text(
                                text = "Инструменты",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 11.sp,
                                    fontWeight = if (isTestActive) FontWeight.Bold else FontWeight.Normal
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            ) 
                        },
                        modifier = Modifier
                            .focusRequester(bottomNavTestTabFocusRequester)
                            .onKeyEvent { keyEvent ->
                                if (isTv && keyEvent.type == androidx.compose.ui.input.key.KeyEventType.KeyDown && keyEvent.key == androidx.compose.ui.input.key.Key.DirectionUp) {
                                    if (state.currentTab == TabType.TEST) {
                                        try {
                                            testTabLastCardFocusRequester.requestFocus()
                                            return@onKeyEvent true
                                        } catch (e: Exception) {}
                                    }
                                }
                                false
                            }
                            .onFocusChanged { isTestFocused = it.isFocused }
                            .testTag("tab_test"),
                        alwaysShowLabel = true
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = { showRebootConfirm = true },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = if (isRebootFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            unselectedTextColor = if (isRebootFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            indicatorColor = Color.Transparent
                        ),
                        icon = { 
                            Icon(
                                imageVector = Icons.Default.Refresh, 
                                contentDescription = "Перезагрузка",
                                modifier = Modifier.size(if (isRebootActive) 22.dp else 20.dp)
                            ) 
                        },
                        label = { 
                            Text(
                                text = "Перезапуск",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 11.sp,
                                    fontWeight = if (isRebootActive) FontWeight.Bold else FontWeight.Normal
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            ) 
                        },
                        modifier = Modifier
                            .onFocusChanged { isRebootFocused = it.isFocused }
                            .testTag("tab_reboot"),
                        alwaysShowLabel = true
                    )
                }
                }
            }
        },
        contentWindowInsets = WindowInsets.safeDrawing.exclude(WindowInsets.ime)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (!state.isInitialLoadComplete) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.material3.CircularProgressIndicator(
                        modifier = Modifier.size(64.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            } else if (state.config == null || state.isConfiguring) {
                // Settings & Credential Panel
                SettingsPanel(
                    state = state,
                    viewModel = viewModel,
                    onDismiss = {
                        if (state.config != null) {
                            viewModel.setConfiguring(false)
                        }
                    }
                )
            } else {
                // Regular tabs content
                AnimatedContent(
                    targetState = state.currentTab,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(220))
                    },
                    label = "tab_animation"
                ) { tab ->
                    when (tab) {
                        TabType.DASHBOARD -> DashboardTab(
                            state = state,
                            onNavigateToConsole = { cmd ->
                                if (cmd != null) {
                                    viewModel.setCommandInput(cmd)
                                }
                                viewModel.switchTab(TabType.CONSOLE)
                            },
                            onRefreshCapabilities = { viewModel.recheckCapabilities() },
                            onLedToggle = {
                                viewModel.toggleLed(it)
                                ShortcutHelper.pushShortcutById(context, if (it) "led_on" else "led_off")
                            },
                            onMasterVpnToggle = {
                                viewModel.toggleMasterVpn(it)
                            },
                            onOpenVpnListDialog = {
                                viewModel.openVpnListDialog()
                            },
                            onToggleTentativeVpnItem = { name, checked ->
                                viewModel.toggleTentativeVpnItem(name, checked)
                            },
                            onApplyVpnChanges = {
                                android.widget.Toast.makeText(context, translateText("Применение изменений...", context), android.widget.Toast.LENGTH_SHORT).show()
                                viewModel.applyVpnListChanges(isTv)
                            },
                            onCancelVpnChanges = {
                                viewModel.cancelVpnListChanges()
                            },
                            onOpenDnsListDialog = {
                                viewModel.setDnsListDialogOpen(true)
                            },
                            onDnsVariantSelected = { servers ->
                                viewModel.setDnsServers(servers)
                            },
                            onCancelDnsChanges = {
                                viewModel.setDnsListDialogOpen(false)
                            },
                            onRefreshStatus = {
                                viewModel.refreshStatus()
                            }
                        )
                        TabType.CONSOLE -> ConsoleTab(
                            consoleHistory = state.consoleHistory.filter { !it.command.contains("iperf3") },
                            commandOutput = state.commandOutput,
                            sessionCommandHistory = sessionCommandHistory,
                            onCommandChange = { viewModel.setCommandInput(it) },
                            onSend = { 
                                viewModel.sendConsoleCommand()
                                focusManager.clearFocus()
                            },
                            onClearHistory = { viewModel.startInteractiveShellSession() },
                            onDeleteHistoryItem = { viewModel.deleteFromSessionHistory(it) },
                            onWriteRawToConsoleStdin = { viewModel.writeRawToConsoleStdin(it) },
                            onAddHistoryItem = { viewModel.addToSessionHistory(it) }
                        )
                        TabType.TEST -> TestTab(
                            state = state,
                            isFullScreen = isTestFullScreen,
                            onFullScreenChange = { isTestFullScreen = it },
                            isIPerfFullScreen = isIPerfFullScreen,
                            onIPerfFullScreenChange = { setIPerfFullScreen(it) },
                            isWifiAnalyzerFullScreen = isWifiAnalyzerFullScreen,
                            onWifiAnalyzerFullScreenChange = { isWifiAnalyzerFullScreen = it },
                            viewModel = viewModel,
                            speedTestUrl = speedTestUrl,
                            onSpeedTestUrlChange = { speedTestUrl = it },
                            bottomNavFocusRequester = bottomNavTestTabFocusRequester,
                            lastCardFocusRequester = testTabLastCardFocusRequester
                        )
                    }
                }
            }

            if (showRebootConfirm) {
                AlertDialog(
                    onDismissRequest = { showRebootConfirm = false },
                    title = { Text("Перезагрузка роутера") },
                    text = { Text("Вы уверены, что хотите перезагрузить роутер? Это временно прервет сетевое соединение.") },
                    confirmButton = {
                        Button(
                            onClick = {
                                showRebootConfirm = false
                                viewModel.rebootRouter()
                                ShortcutHelper.pushShortcutById(context, "reboot")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Перезагрузить")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showRebootConfirm = false }) {
                            Text("Отмена")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun InternetStatusCard(state: UiState, modifier: Modifier = Modifier, onRefreshStatus: (() -> Unit)? = null, onNavigateToConsole: (String?) -> Unit = {}, onRefreshCapabilities: () -> Unit = {}) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val isTv = remember {
        context.packageManager.hasSystemFeature(android.content.pm.PackageManager.FEATURE_LEANBACK)
    }
    var isFocused by remember { mutableStateOf(false) }
    var showDevices by remember { mutableStateOf(false) }
    
    val isDark = androidx.compose.foundation.isSystemInDarkTheme()

    // On phone in light theme, make it identical to TogglesCard
    val cardBg = if (!isTv && !isDark) {
        MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
    } else {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
    }

    val cardElevation = if (!isTv && !isDark) 1.dp else 2.dp

    // Spacing optimization to match TogglesCard perfectly on phones, and increased height on TV
    val cardPadding = if (isTv) 12.dp else 12.dp
    val itemSpacing = if (isTv) 8.dp else 10.dp
    val itemPadding = if (isTv) 4.dp else 0.dp

    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(if (isTv) Modifier.height(172.dp) else Modifier)
            .testTag("internet_status_card")
            .then(
                if (isTv) {
                    Modifier
                        .onFocusChanged { isFocused = it.isFocused }
                        .focusable()
                        .border(
                            width = 2.dp,
                            color = if (isFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                            shape = RoundedCornerShape(16.dp)
                        )
                } else Modifier
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardBg
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = cardElevation)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .padding(cardPadding)
                    .padding(end = if (onRefreshStatus != null) 56.dp else 0.dp),
                verticalArrangement = Arrangement.spacedBy(itemSpacing)
            ) {
                // IP Field
                StatusRow(
                    label = "Публичный IP",
                    value = state.status.publicIp,
                    iconColor = MaterialTheme.colorScheme.primary,
                    imageVector = Icons.Default.Info,
                    modifier = if (isTv) Modifier
                        .background(cardBg, RoundedCornerShape(12.dp))
                        .padding(itemPadding) else Modifier
                )

                // Location Field
                StatusRow(
                    label = "Локация",
                    value = state.status.location,
                    iconColor = MaterialTheme.colorScheme.tertiary,
                    imageVector = Icons.Default.Home,
                    modifier = if (isTv) Modifier
                        .background(cardBg, RoundedCornerShape(12.dp))
                        .padding(itemPadding) else Modifier
                )

                // ISP Provider Field
                StatusRow(
                    label = "Провайдер",
                    value = state.status.provider,
                    iconColor = MaterialTheme.colorScheme.secondary,
                    imageVector = Icons.Default.Info,
                    modifier = if (isTv) Modifier
                        .background(cardBg, RoundedCornerShape(12.dp))
                        .padding(itemPadding) else Modifier
                )
            }

            if (onRefreshStatus != null) {
                val rotationAnim = remember { Animatable(0f) }
                LaunchedEffect(state.isStatusRefreshing) {
                    if (state.isStatusRefreshing) {
                        rotationAnim.animateTo(
                            targetValue = rotationAnim.value + 360f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(1200, easing = LinearEasing),
                                repeatMode = RepeatMode.Restart
                            )
                        )
                    } else {
                        rotationAnim.stop()
                    }
                }

                IconButton(
                    onClick = { onRefreshStatus() },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 8.dp, end = 8.dp)
                        .size(48.dp)
                        .background(
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.12f),
                            shape = androidx.compose.foundation.shape.CircleShape
                        )
                        .testTag("refresh_button")
                        .rotate(rotationAnim.value)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Обновить статус",
                        modifier = Modifier.size(26.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun SpeedMeterCard(state: UiState, modifier: Modifier = Modifier, onNavigateToConsole: (String?) -> Unit = {}, onRefreshCapabilities: () -> Unit = {}) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val isTv = remember {
        context.packageManager.hasSystemFeature(android.content.pm.PackageManager.FEATURE_LEANBACK)
    }
    val configuration = androidx.compose.ui.platform.LocalConfiguration.current
    val isTablet = remember(configuration) {
        configuration.screenWidthDp >= 600 && !isTv
    }
    var isFocused by remember { mutableStateOf(false) }
    var showDevices by remember { mutableStateOf(false) }
    
    val greenColor = if (androidx.compose.foundation.isSystemInDarkTheme()) Color(0xFF00E676) else Color(0xFF2E7D32)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("speed_meter_card")
            .then(
                if (isTv) {
                    Modifier
                        .onFocusChanged { isFocused = it.isFocused }
                        .focusable()
                        .border(
                            width = 2.dp,
                            color = if (isFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                            shape = RoundedCornerShape(16.dp)
                        )
                } else Modifier
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Current Rates values
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                var isDlFocused by remember { mutableStateOf(false) }
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { showDevices = true }
                        .onFocusChanged { isDlFocused = it.isFocused }
                        .focusable()
                        .background(if (isDlFocused) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent)
                        .padding(4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(RoundedCornerShape(50))
                                .background(greenColor)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Загрузка ↓",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = String.format("%.2f Мбит/с", state.currentDownloadSpeed),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = greenColor
                    )
                }

                // Inline CPU, Memory, and Uptime telemetry for TV or Tablet Landscape
                val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
                if (isTv || (isTablet && isLandscape)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        // CPU
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            var showTemperature by remember { mutableStateOf(false) }
                            LaunchedEffect(state.speedHistory.lastOrNull()?.timestamp) {
                                showTemperature = !showTemperature
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(RoundedCornerShape(50))
                                        .background(
                                            if (showTemperature && state.cpuTemperature != null) {
                                                when {
                                                    state.cpuTemperature!! < 65f -> Color(0xFF009688)
                                                    state.cpuTemperature!! < 75f -> Color(0xFF7FFF00)
                                                    else -> Color(0xFFDC143C)
                                                }
                                            } else {
                                                Color(0xFFDC143C)
                                            }
                                        )
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (showTemperature) translateText("Температура", androidx.compose.ui.platform.LocalContext.current) else translateText("Процессор", androidx.compose.ui.platform.LocalContext.current),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            if (showTemperature) {
                                if (state.cpuTemperature == null) {
                                    Text(
                                        text = translateText("недоступна", androidx.compose.ui.platform.LocalContext.current),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                } else {
                                    val temp = state.cpuTemperature
                                    val tempColor = when {
                                        temp < 65f -> Color(0xFF009688) // Teal
                                        temp < 75f -> Color(0xFF7FFF00) // Chartreuse
                                        else -> Color(0xFFDC143C) // Crimson
                                    }
                                    Text(
                                        text = String.format("%.1f °C", temp),
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = tempColor
                                    )
                                }
                            } else {
                                Text(
                                    text = state.cpuUsage,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFDC143C)
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .height(24.dp)
                                .width(1.dp)
                                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
                        )

                        // Memory
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "ОЗУ",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = state.memoryUsage,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Box(
                            modifier = Modifier
                                .height(24.dp)
                                .width(1.dp)
                                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
                        )

                        // Uptime
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Аптайм",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = state.uptime,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                if (!isTv && !(isTablet && isLandscape)) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(Color(0xFFFF1744))
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "ЦП",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = state.cpuUsage,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF1744)
                        )
                    }
                }

                var isUlFocused by remember { mutableStateOf(false) }
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { showDevices = true }
                        .onFocusChanged { isUlFocused = it.isFocused }
                        .focusable()
                        .background(if (isUlFocused) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent)
                        .padding(4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(RoundedCornerShape(50))
                                .background(Color(0xFFFFB300))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Выгрузка ↑",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = String.format("%.2f Мбит/с", state.currentUploadSpeed),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFB300)
                    )
                }
            }

            // Line Graph View
            val chartHeight = if (isTv) 132.dp else if (isTablet) 280.8.dp else 129.6.dp
            Box(modifier = Modifier.fillMaxWidth().height(chartHeight)) {
                SpeedChart(
                    history = state.speedHistory,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(chartHeight)
                        .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                        .background(Color.Black.copy(alpha = 0.05f))
                )
                val isOnline = state.status.publicIp != "Offline / Error" && state.status.publicIp != "Connecting..."
                Surface(
                    color = if (isOnline) greenColor else Color(0xFFFF3D00),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(y = (-4).dp)
                        .padding(top = 8.dp, end = 16.dp)
                        .size(12.dp)
                ) {}
            }
        }


        if (showDevices) {
            DeviceListDialog(
                title = androidx.compose.ui.res.stringResource(R.string.devices_title),
                config = state.config,
                devices = state.deviceSpeeds,
                deviceHistory = state.deviceSpeedHistory,
                onDismiss = { showDevices = false },
                onNavigateToConsole = { cmd ->
                    showDevices = false
                    onNavigateToConsole(cmd)
                },
                onRefreshCapabilities = onRefreshCapabilities
            )
        }
    }
}

fun Modifier.drawSimpleScrollbar(
    state: androidx.compose.foundation.lazy.LazyListState,
    paddingEnd: androidx.compose.ui.unit.Dp = 6.dp,
    heightOffset: androidx.compose.ui.unit.Dp = 0.dp
): Modifier = this.drawWithContent {
    drawContent()
    val layoutInfo = state.layoutInfo
    val totalItemsCount = layoutInfo.totalItemsCount
    val visibleItemsInfo = layoutInfo.visibleItemsInfo
    val isOverflow = if (visibleItemsInfo.isEmpty()) false else {
        if (totalItemsCount > visibleItemsInfo.size) true
        else {
            val first = visibleItemsInfo.first()
            val last = visibleItemsInfo.last()
            val viewportStart = layoutInfo.viewportStartOffset
            val viewportEnd = layoutInfo.viewportEndOffset
            first.offset < viewportStart || (last.offset + last.size) > viewportEnd
        }
    }
    
    if (isOverflow) {
        val totalHeight = this.size.height
        val firstVisibleItem = visibleItemsInfo.first()
        val firstVisibleIndex = firstVisibleItem.index
        val firstVisibleItemOffset = firstVisibleItem.offset.toFloat()
        val itemHeight = firstVisibleItem.size.toFloat()
        
        val progress = if (itemHeight > 0f) {
            firstVisibleIndex.toFloat() - (firstVisibleItemOffset / itemHeight)
        } else {
            firstVisibleIndex.toFloat()
        }
        
        val visibleItemsSize = visibleItemsInfo.size
        val scrollbarHeight = ((totalHeight * (visibleItemsSize.toFloat() / totalItemsCount)) - heightOffset.toPx()).coerceAtLeast(30f)
        val scrollbarOffset = (totalHeight * (progress / totalItemsCount)).coerceIn(0f, totalHeight - scrollbarHeight)
        
        drawRect(
            color = androidx.compose.ui.graphics.Color.Gray.copy(alpha = 0.5f),
            topLeft = androidx.compose.ui.geometry.Offset(this.size.width - paddingEnd.toPx(), scrollbarOffset + heightOffset.toPx() / 2),
            size = androidx.compose.ui.geometry.Size(4.dp.toPx(), scrollbarHeight)
        )
    }
}

@Composable
fun TogglesCard(state: UiState,
    
    onLedToggle: (Boolean) -> Unit,
    onMasterVpnToggle: (Boolean) -> Unit,
    onOpenVpnListDialog: () -> Unit,
    onToggleTentativeVpnItem: (String, Boolean) -> Unit,
    onApplyVpnChanges: () -> Unit,
    onCancelVpnChanges: () -> Unit,
    onOpenDnsListDialog: () -> Unit,
    onDnsVariantSelected: (List<String>) -> Unit,
    onCancelDnsChanges: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val isTv = remember {
        context.packageManager.hasSystemFeature(android.content.pm.PackageManager.FEATURE_LEANBACK)
    }
    val configuration = androidx.compose.ui.platform.LocalConfiguration.current
    val isTablet = remember(configuration) {
        configuration.screenWidthDp >= 600 && !isTv
    }
    val isSmartphone = !isTv && !isTablet

    val runningItems = state.vpnList.filter { it.isRunning }
    val gettingDataFromRouter = state.isStatusRefreshing || 
            state.status.publicIp.isEmpty() || 
            state.status.publicIp.contains("Connecting", ignoreCase = true) || 
            state.status.publicIp.contains("Unknown", ignoreCase = true) || 
            state.status.location.isEmpty() || 
            state.status.location.contains("Detecting", ignoreCase = true) ||
            state.status.location.contains("Unknown", ignoreCase = true)
    val isNoActiveVPN = runningItems.isEmpty()

    val isSingleVPNActive = (runningItems.size == 1) || 
            gettingDataFromRouter || 
            isNoActiveVPN
    val cardHeight = if (isSingleVPNActive) 78.dp else 130.dp
    var isVpnListFocused by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // --- 1. NEW UNIFIED VPN CARD ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(cardHeight)
                .testTag("vpn_master_card"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = if (isSingleVPNActive) 9.dp else 15.dp,
                        bottom = if (isSingleVPNActive) 9.dp else 15.dp
                    )
            ) {
                val isDark = androidx.compose.foundation.isSystemInDarkTheme()
                val connStatusCardBg = if (!isTv && !isDark) {
                    MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
                } else {
                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                }

                // Top-left label "VPN"
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .width(48.dp)
                        .padding(top = if (isSingleVPNActive) 1.8.dp else 3.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "VPN",
                        style = if (isTv) {
                            MaterialTheme.typography.bodyMedium
                        } else if (isSmartphone) {
                            MaterialTheme.typography.bodyMedium.copy(fontSize = if (isSingleVPNActive) 11.sp else 12.sp)
                        } else {
                            MaterialTheme.typography.bodyMedium
                        },
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .align(Alignment.Center),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left Icon/Button to open List aligned perfectly at the same height as the switch
                    IconButton(
                        onClick = { onOpenVpnListDialog() },
                        modifier = Modifier
                            .size(if (isSingleVPNActive) 36.dp else 48.dp)
                            .onFocusChanged { isVpnListFocused = it.isFocused }
                            .background(
                                if (isVpnListFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(if (isSingleVPNActive) 9.dp else 12.dp)
                            )
                            .testTag("btn_vpn_list_trigger")
                    ) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.List,
                            contentDescription = "Список соединений",
                            tint = if (isVpnListFocused) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Middle area: Informational window of active VPN connections / transitions (no outer border)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(connStatusCardBg, shape = RoundedCornerShape(8.dp))
                            .clip(RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = if (isSingleVPNActive) 3.6.dp else 6.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (gettingDataFromRouter) {
                            Text(
                                text = translateText("Получение данных от роутера...", context),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium
                            )
                        } else if (state.isVpnTransitioning) {
                            Text(
                                text = translateText(state.vpnTransitionText ?: "Применение изменений...", context),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium
                            )
                        } else if (runningItems.isNotEmpty()) {
                            if (runningItems.isNotEmpty()) {
                                BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                                    val fits = runningItems.size <= 2

                                    if (fits) {
                                        Column(
                                            modifier = Modifier.fillMaxSize(),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.Start
                                        ) {
                                            runningItems.forEachIndexed { itemIndex, item ->
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.padding(vertical = if (isSingleVPNActive) 1.2.dp else 2.dp)
                                                ) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(15.dp)
                                                            .clip(androidx.compose.foundation.shape.CircleShape)
                                                            .background(Color(0xFF4CAF50)),
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        if (runningItems.size > 1) {
                                                            Text(
                                                                text = (itemIndex + 1).toString(),
                                                                style = MaterialTheme.typography.labelSmall.copy(
                                                                    fontSize = 9.sp,
                                                                    fontWeight = FontWeight.Bold,
                                                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                                                    lineHeight = 9.sp
                                                                ),
                                                                color = Color.White
                                                            )
                                                        }
                                                    }
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Text(
                                                        text = "${item.name} (${item.type}) - ${translateText("Активен", context)}",
                                                        style = if (isTv) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.bodySmall,
                                                        color = MaterialTheme.colorScheme.primary,
                                                        fontWeight = FontWeight.Medium
                                                    )
                                                }
                                            }
                                        }
                                    } else {
                                        val listState = rememberLazyListState()
                                        val startIndex = (Int.MAX_VALUE / 2) - ((Int.MAX_VALUE / 2) % runningItems.size)

                                        LaunchedEffect(runningItems) {
                                            listState.scrollToItem(startIndex)
                                            if (isTv) {
                                                try {
                                                    listState.scroll(androidx.compose.foundation.MutatePriority.PreventUserInput) {
                                                        var lastTimeNanos = System.nanoTime()
                                                        val pixelsPerSecond = 25f
                                                        while (true) {
                                                            androidx.compose.runtime.withFrameNanos { frameTimeNanos ->
                                                                val elapsedNanos = frameTimeNanos - lastTimeNanos
                                                                lastTimeNanos = frameTimeNanos
                                                                val deltaSeconds = elapsedNanos / 1_000_000_000f
                                                                scrollBy(pixelsPerSecond * deltaSeconds)
                                                            }
                                                        }
                                                    }
                                                } catch (e: Exception) {
                                                    // ignore
                                                }
                                            } else {
                                                try {
                                                    listState.scroll(androidx.compose.foundation.MutatePriority.PreventUserInput) {
                                                        var lastTimeNanos = System.nanoTime()
                                                        val pixelsPerSecond = 25f
                                                        while (true) {
                                                            androidx.compose.runtime.withFrameNanos { frameTimeNanos ->
                                                                val elapsedNanos = frameTimeNanos - lastTimeNanos
                                                                lastTimeNanos = frameTimeNanos
                                                                val deltaSeconds = elapsedNanos / 1_000_000_000f
                                                                scrollBy(pixelsPerSecond * deltaSeconds)
                                                            }
                                                        }
                                                    }
                                                } catch (e: Exception) {
                                                    // ignore
                                                }
                                            }
                                        }

                                        LazyColumn(
                                            state = listState,
                                            modifier = Modifier.fillMaxSize(),
                                            verticalArrangement = Arrangement.spacedBy(4.dp),
                                            userScrollEnabled = false
                                        ) {
                                            items(Int.MAX_VALUE) { index ->
                                                val itemIndex = index % runningItems.size
                                                val item = runningItems[itemIndex]
                                                Column {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        modifier = Modifier.padding(vertical = 1.dp)
                                                    ) {
                                                        Box(
                                                            modifier = Modifier
                                                                .size(15.dp)
                                                                .clip(androidx.compose.foundation.shape.CircleShape)
                                                                .background(Color(0xFF4CAF50)),
                                                            contentAlignment = Alignment.Center
                                                        ) {
                                                            if (runningItems.size > 1) {
                                                                Text(
                                                                    text = (itemIndex + 1).toString(),
                                                                    style = MaterialTheme.typography.labelSmall.copy(
                                                                        fontSize = 9.sp,
                                                                        fontWeight = FontWeight.Bold,
                                                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                                                        lineHeight = 9.sp
                                                                    ),
                                                                    color = Color.White
                                                                )
                                                            }
                                                        }
                                                        Spacer(modifier = Modifier.width(6.dp))
                                                        Text(
                                                            text = "${item.name} (${item.type}) - ${translateText("Активен", context)}",
                                                            style = if (isTv) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.bodySmall,
                                                            color = MaterialTheme.colorScheme.primary,
                                                            fontWeight = FontWeight.Medium
                                                        )
                                                    }
                                                    if (itemIndex == runningItems.size - 1) {
                                                        Spacer(modifier = Modifier.height(2.dp))
                                                        Box(
                                                            modifier = Modifier
                                                                .fillMaxWidth()
                                                                .height(0.6.dp)
                                                                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f))
                                                        )
                                                        Spacer(modifier = Modifier.height(2.dp))
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                if (!state.isVpnListDialogOpen && !gettingDataFromRouter) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(vertical = 2.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(15.dp)
                                                .clip(androidx.compose.foundation.shape.CircleShape)
                                                .background(Color(0xFFF44336)),
                                            contentAlignment = Alignment.Center
                                        ) {}
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Нет активных VPN соединений",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        } else {
                            Text(
                                text = "Все VPN отключены",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    if (state.isVpnTransitioning) {
                        Spacer(modifier = Modifier.width(11.dp))
                        androidx.compose.material3.CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        // --- 2. DNS CARD ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(78.dp)
                .testTag("dns_master_card"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 9.dp,
                        bottom = 9.dp
                    )
            ) {
                val isDark = androidx.compose.foundation.isSystemInDarkTheme()
                val connStatusCardBg = if (!isTv && !isDark) {
                    MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
                } else {
                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .width(48.dp)
                        .padding(top = 1.8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "DNS",
                        style = if (isTv) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.bodyMedium.copy(fontSize = 11.sp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .align(Alignment.Center),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    var isDnsListFocused by remember { mutableStateOf(false) }
                    IconButton(
                        onClick = { onOpenDnsListDialog() },
                        modifier = Modifier
                            .size(36.dp)
                            .onFocusChanged { isDnsListFocused = it.isFocused }
                            .background(
                                if (isDnsListFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(9.dp)
                            )
                            .testTag("btn_dns_list_trigger")
                    ) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.Settings,
                            contentDescription = "Выбор DNS",
                            tint = if (isDnsListFocused) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(connStatusCardBg, shape = RoundedCornerShape(8.dp))
                            .clip(RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 3.6.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        val dnsText = if (state.isStatusRefreshing) {
                            translateText("Получение данных...", context)
                        } else if (!state.status.isCustomDns) {
                            translateText("По умолчанию (Провайдер)", context)
                        } else {
                            val currentIps = state.status.dnsServers.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                            var matchedProvider: com.example.ui.DnsProvider? = null
                            var matchedVariant: com.example.ui.DnsVariant? = null
                            
                            for (p in com.example.ui.getPublicDnsProviders(context)) {
                                for (v in p.variants) {
                                    if (currentIps.any { it in v.servers }) {
                                        matchedProvider = p
                                        matchedVariant = v
                                        break
                                    }
                                }
                                if (matchedVariant != null) break
                            }
                            
                            if (matchedProvider != null && matchedVariant != null) {
                                val unmatchedIps = currentIps.filter { it !in matchedVariant.servers }
                                val baseName = "${matchedProvider.name}: ${matchedVariant.name}"
                                if (unmatchedIps.isNotEmpty()) {
                                    "$baseName, ${translateText("дополнительно", context)} ${unmatchedIps.joinToString(", ")}"
                                } else {
                                    baseName
                                }
                            } else {
                                state.status.dnsServers
                            }
                        }

                        var isOverflowing by remember(dnsText) { mutableStateOf(false) }

                        if (!isOverflowing) {
                            Text(
                                text = "DNS $dnsText",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (state.status.isCustomDns) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Medium,
                                onTextLayout = { textLayoutResult ->
                                    if (textLayoutResult.lineCount > 3) {
                                        isOverflowing = true
                                    }
                                }
                            )
                        } else {
                            val listState = rememberLazyListState()
                            val startIndex = Int.MAX_VALUE / 2

                            LaunchedEffect(dnsText) {
                                listState.scrollToItem(startIndex)
                                try {
                                    listState.scroll(androidx.compose.foundation.MutatePriority.PreventUserInput) {
                                        var lastTimeNanos = System.nanoTime()
                                        val pixelsPerSecond = 25f
                                        while (true) {
                                            androidx.compose.runtime.withFrameNanos { frameTimeNanos ->
                                                val elapsedNanos = frameTimeNanos - lastTimeNanos
                                                lastTimeNanos = frameTimeNanos
                                                val deltaSeconds = elapsedNanos / 1_000_000_000f
                                                scrollBy(pixelsPerSecond * deltaSeconds)
                                            }
                                        }
                                    }
                                } catch (e: Exception) {}
                            }

                            LazyColumn(
                                state = listState,
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                userScrollEnabled = false
                            ) {
                                items(Int.MAX_VALUE) {
                                    Text(
                                        text = "DNS $dnsText",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (state.status.isCustomDns) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- 3. LED CONTROLLER CARD ---
        if (state.status.model.contains("Xiaomi", ignoreCase = true) && state.status.isLedFileValid) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("led_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Box(modifier = Modifier.padding(8.dp)) {
                    ToggleRow(
                        title = "Светодиод роутера",
                        subtitle = state.ledTransition ?: if (state.status.isLedActive) "Включен" else "Выключен",
                        isActive = state.status.isLedActive,
                        icon = androidx.compose.material.icons.Icons.Default.Star,
                        onToggle = onLedToggle,
                        testTag = "led_switch",
                        isCompact = true,
                        isSmartphone = isSmartphone
                    )
                }
            }
        }
    }

    // --- Dialog list for available VPNs ---
    if (state.isVpnListDialogOpen) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { onCancelVpnChanges() },
            title = {
                Text(
                    text = "Доступные VPN. Выберите те, которые должны быть включены",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                if (state.tentativeVpnList.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = translateText("Получение данных от роутера...", context),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else {
                    val listState = rememberLazyListState()
                    Box(modifier = Modifier.heightIn(max = 300.dp)) {
                        LazyColumn(
                            state = listState,
                            verticalArrangement = Arrangement.spacedBy(5.6.dp),
                            modifier = Modifier.drawSimpleScrollbar(listState)
                        ) {
                            items(state.tentativeVpnList) { item ->
                                val isItemRunning = item.isRunning
                                val textColor = if (isItemRunning) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                }
                                val fontWeight = if (isItemRunning) {
                                    FontWeight.Bold
                                } else {
                                    FontWeight.Normal
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onToggleTentativeVpnItem(item.name, !item.isChecked) }
                                        .padding(vertical = 2.8.dp)
                                        .padding(end = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    androidx.compose.material3.Checkbox(
                                        checked = item.isChecked,
                                        onCheckedChange = { checked -> onToggleTentativeVpnItem(item.name, checked) },
                                        modifier = Modifier.testTag("checkbox_${item.name}")
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = item.name,
                                            color = textColor,
                                            fontWeight = fontWeight,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Text(
                                            text = when (item.type) {
                                                "OpenVPN" -> "OpenVPN Служба"
                                                "WireGuard" -> "WireGuard Интерфейс"
                                                "AmneziaWG" -> "AmneziaWG Интерфейс"
                                                else -> item.type
                                            },
                                            style = MaterialTheme.typography.bodySmall,
                                            color = if (isItemRunning) MaterialTheme.colorScheme.primary.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                androidx.compose.material3.Button(
                    onClick = { onApplyVpnChanges() },
                    modifier = Modifier.testTag("vpn_dialog_apply"),
                    enabled = state.tentativeVpnList.isNotEmpty()
                ) {
                    Text("Применить")
                }
            },
        )
    }

    // --- Dialog list for DNS ---
    if (state.isDnsListDialogOpen) {
        var showConfirmationDialog by remember { mutableStateOf(false) }
        var selectedVariant by remember { mutableStateOf<com.example.ui.DnsVariant?>(null) }
        var expandedProviderId by remember { mutableStateOf<String?>(null) }
        var pendingCustomDns by remember { mutableStateOf<List<String>?>(null) }
        
        // For Flow A: Public first, then ask for additional custom IPs
        var showAddExtraDnsAskDialog by remember { mutableStateOf(false) }
        var showAdditionalDnsInputDialog by remember { mutableStateOf(false) }
        var customAdditionalIps by remember { mutableStateOf("") }

        // For Flow B: Custom first, then ask to pick a public DNS
        var showAddPublicDnsAskDialog by remember { mutableStateOf(false) }

        if (showConfirmationDialog && selectedVariant != null) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showConfirmationDialog = false },
                title = { Text(translateText("Подтверждение DNS", context)) },
                text = { Text(translateText("Установить %s в качестве DNS сервера?", context).format(selectedVariant!!.name)) },
                confirmButton = {
                    androidx.compose.material3.Button(onClick = {
                        if (selectedVariant!!.id == "isp") {
                            onDnsVariantSelected(emptyList())
                            showConfirmationDialog = false
                            onCancelDnsChanges()
                        } else if (selectedVariant!!.id == "custom") {
                            showConfirmationDialog = false
                            showAddPublicDnsAskDialog = true
                        } else {
                            // Public DNS selected
                            if (pendingCustomDns.isNullOrEmpty()) {
                                showConfirmationDialog = false
                                showAddExtraDnsAskDialog = true
                            } else {
                                val finalServers = pendingCustomDns!! + selectedVariant!!.servers
                                onDnsVariantSelected(finalServers.distinct())
                                showConfirmationDialog = false
                                onCancelDnsChanges()
                            }
                        }
                    }) {
                        Text(translateText("Подтвердить", context))
                    }
                },
                dismissButton = {
                    androidx.compose.material3.TextButton(onClick = { showConfirmationDialog = false }) {
                        Text(translateText("Отмена", context))
                    }
                }
            )
        }

        // Flow A Ask
        if (showAddExtraDnsAskDialog && selectedVariant != null) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showAddExtraDnsAskDialog = false },
                title = { Text(translateText("Дополнительный DNS", context)) },
                text = { Text(translateText("Желаете ли вы добавить дополнительный DNS?", context)) },
                confirmButton = {
                    androidx.compose.material3.Button(onClick = {
                        showAddExtraDnsAskDialog = false
                        showAdditionalDnsInputDialog = true
                    }) {
                        Text(translateText("Да", context))
                    }
                },
                dismissButton = {
                    androidx.compose.material3.TextButton(onClick = {
                        onDnsVariantSelected(selectedVariant!!.servers)
                        showAddExtraDnsAskDialog = false
                        onCancelDnsChanges()
                    }) {
                        Text(translateText("Нет", context))
                    }
                }
            )
        }

        // Flow B Ask
        if (showAddPublicDnsAskDialog && selectedVariant != null) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showAddPublicDnsAskDialog = false },
                title = { Text(translateText("Дополнительный DNS", context)) },
                text = { Text(translateText("Желаете ли вы добавить один из публичных DNS?", context)) },
                confirmButton = {
                    androidx.compose.material3.Button(onClick = {
                        pendingCustomDns = selectedVariant!!.servers
                        showAddPublicDnsAskDialog = false
                        // User returns to the list to pick a public DNS
                    }) {
                        Text(translateText("Да", context))
                    }
                },
                dismissButton = {
                    androidx.compose.material3.TextButton(onClick = {
                        onDnsVariantSelected(selectedVariant!!.servers)
                        showAddPublicDnsAskDialog = false
                        onCancelDnsChanges()
                    }) {
                        Text(translateText("Нет", context))
                    }
                }
            )
        }

        if (showAdditionalDnsInputDialog && selectedVariant != null) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showAdditionalDnsInputDialog = false },
                title = { Text(translateText("Дополнительный DNS", context)) },
                text = {
                    Column {
                        Text(
                            text = translateText("Добавьте один или несколько IP, разделяя их запятыми", context),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        androidx.compose.material3.OutlinedTextField(
                            value = customAdditionalIps,
                            onValueChange = { customAdditionalIps = it },
                            placeholder = { 
                                Text(
                                    "0.0.0.0, 0.0.0.1, 0.0.0.2",
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                ) 
                            },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = MaterialTheme.typography.bodyMedium
                        )
                    }
                },
                confirmButton = {
                    androidx.compose.material3.Button(onClick = {
                        val additionalIpsList = customAdditionalIps.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                        val finalServers = selectedVariant!!.servers + additionalIpsList
                        onDnsVariantSelected(finalServers.distinct())
                        showAdditionalDnsInputDialog = false
                        onCancelDnsChanges()
                    }) {
                        Text(translateText("Добавить", context))
                    }
                },
                dismissButton = {
                    androidx.compose.material3.TextButton(onClick = { showAdditionalDnsInputDialog = false }) {
                        Text(translateText("Отмена", context))
                    }
                }
            )
        }

        androidx.compose.ui.window.Dialog(onDismissRequest = { onCancelDnsChanges() }) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(horizontal = 24.dp, vertical = 14.dp)
                    ) {
                        Text(
                            text = translateText("Выберите DNS для использования на роутере.", context),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontSize = 12.sp, 
                                lineHeight = 18.sp
                            ),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    val listState = rememberLazyListState()
                    Box(
                        modifier = Modifier
                            .weight(1f, fill = false)
                            .heightIn(max = 400.dp)
                            .drawSimpleScrollbar(listState, paddingEnd = 10.dp, heightOffset = 8.dp)
                    ) {
                        LazyColumn(
                            state = listState,
                            verticalArrangement = Arrangement.spacedBy(0.dp),
                            modifier = Modifier
                                .focusGroup()
                                .padding(end = 22.dp, start = 24.dp, top = 8.dp, bottom = 8.dp)
                        ) {
                            item {
                                val isIspActive = !state.status.isCustomDns
                                val infiniteTransition = androidx.compose.animation.core.rememberInfiniteTransition()
                                val alpha by infiniteTransition.animateFloat(
                                    initialValue = 0.2f,
                                    targetValue = 0.8f,
                                    animationSpec = androidx.compose.animation.core.infiniteRepeatable(
                                        animation = androidx.compose.animation.core.tween(1000),
                                        repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
                                    )
                                )

                                val firstItemFocusRequester = remember { FocusRequester() }
                                LaunchedEffect(Unit) {
                                    if (isTv) {
                                        try {
                                            firstItemFocusRequester.requestFocus()
                                        } catch (e: Exception) {}
                                    }
                                }

                                var isIspFocused by remember { mutableStateOf(false) }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .focusRequester(firstItemFocusRequester)
                                        .onFocusChanged { isIspFocused = it.isFocused }
                                        .background(
                                            color = if (isIspFocused) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) else Color.Transparent,
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .clickable {
                                            selectedVariant = com.example.ui.DnsVariant("isp", "Провайдер (По умолчанию)", null, emptyList())
                                            showConfirmationDialog = true
                                        }
                                        .padding(vertical = 12.dp, horizontal = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = translateText("По умолчанию (Провайдер)", context),
                                        modifier = Modifier.weight(1f),
                                        color = if (isIspActive) Color(0xFF4CAF50).copy(alpha = alpha) else MaterialTheme.colorScheme.onSurface,
                                        fontWeight = if (isIspActive) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                                androidx.compose.material3.HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                            }
                            
                            item {
                                var isExpanded by remember { mutableStateOf(false) }
                                val isUnknownCustom = state.status.isCustomDns && com.example.ui.getPublicDnsProviders(context).none { p ->
                                    p.variants.any { v -> state.status.dnsServers.split(",").map{it.trim()}.all { v.servers.contains(it) } }
                                }
                                
                                val infiniteTransition = androidx.compose.animation.core.rememberInfiniteTransition()
                                val alpha by infiniteTransition.animateFloat(
                                    initialValue = 0.2f,
                                    targetValue = 0.8f,
                                    animationSpec = androidx.compose.animation.core.infiniteRepeatable(
                                        animation = androidx.compose.animation.core.tween(1000),
                                        repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
                                    )
                                )

                                Column(modifier = Modifier.fillMaxWidth()) {
                                    var isCustomDnsFocused by remember { mutableStateOf(false) }
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .onFocusChanged { isCustomDnsFocused = it.isFocused }
                                            .background(
                                                color = if (isCustomDnsFocused) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) else Color.Transparent,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .clickable { isExpanded = !isExpanded }
                                            .padding(vertical = 12.dp, horizontal = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = translateText("Пользовательский DNS", context),
                                            modifier = Modifier.weight(1f),
                                            color = if (isUnknownCustom) Color(0xFF4CAF50).copy(alpha = alpha) else MaterialTheme.colorScheme.onSurface,
                                            fontWeight = if (isUnknownCustom) FontWeight.Bold else FontWeight.Normal
                                        )
                                        Icon(
                                            imageVector = if (isExpanded) androidx.compose.material.icons.Icons.Default.KeyboardArrowUp else androidx.compose.material.icons.Icons.Default.KeyboardArrowDown,
                                            contentDescription = "Expand",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    
                                    androidx.compose.animation.AnimatedVisibility(visible = isExpanded) {
                                        Column(modifier = Modifier.padding(start = 16.dp, top = 4.dp, bottom = 12.dp)) {
                                            var customDnsInput by remember { mutableStateOf(if (isUnknownCustom) state.status.dnsServers else "") }
                                            
                                            androidx.compose.material3.OutlinedTextField(
                                                value = customDnsInput,
                                                onValueChange = { customDnsInput = it },
                                                label = { Text(translateText("IP адрес DNS сервера", context)) },
                                                singleLine = true,
                                                modifier = Modifier.fillMaxWidth().padding(end = 18.dp),
                                                textStyle = MaterialTheme.typography.bodyMedium
                                            )
                                            
                                            Spacer(modifier = Modifier.height(8.dp))
                                            
                                            androidx.compose.material3.Button(
                                                onClick = {
                                                    if (customDnsInput.isNotBlank()) {
                                                        selectedVariant = com.example.ui.DnsVariant("custom", customDnsInput, null, customDnsInput.split(",").map { it.trim() })
                                                        showConfirmationDialog = true
                                                    }
                                                },
                                                modifier = Modifier.align(Alignment.End).padding(end = 18.dp)
                                            ) {
                                                Text(translateText("Задать", context))
                                            }
                                        }
                                    }
                                }
                                androidx.compose.material3.HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                            }

                            itemsIndexed(com.example.ui.getPublicDnsProviders(context)) { index, provider ->
                                val isProviderActive = state.status.isCustomDns && provider.variants.any { variant ->
                                    state.status.dnsServers.split(",").map{it.trim()}.all { variant.servers.contains(it) }
                                }
                                
                                val infiniteTransition = androidx.compose.animation.core.rememberInfiniteTransition()
                                val alpha by infiniteTransition.animateFloat(
                                    initialValue = 0.2f,
                                    targetValue = 0.8f,
                                    animationSpec = androidx.compose.animation.core.infiniteRepeatable(
                                        animation = androidx.compose.animation.core.tween(1000),
                                        repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
                                    )
                                )

                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 6.dp, vertical = 12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        var isProviderTextFocused by remember { mutableStateOf(false) }
                                        Text(
                                            text = if (provider.variants.size == 1) {
                                                "${provider.name} (${provider.variants.first().servers.joinToString(", ")})"
                                            } else {
                                                provider.name
                                            },
                                            modifier = Modifier
                                                .weight(1f)
                                                .onFocusChanged { isProviderTextFocused = it.isFocused }
                                                .background(
                                                    color = if (isProviderTextFocused) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) else Color.Transparent,
                                                    shape = RoundedCornerShape(8.dp)
                                                )
                                                .clickable {
                                                    if (provider.variants.size == 1) {
                                                        selectedVariant = provider.variants.first()
                                                        showConfirmationDialog = true
                                                    } else {
                                                        expandedProviderId = if (expandedProviderId == provider.id) null else provider.id
                                                    }
                                                }
                                                .padding(8.dp),
                                            color = if (isProviderActive) Color(0xFF4CAF50).copy(alpha = alpha) else MaterialTheme.colorScheme.onSurface,
                                            fontWeight = if (isProviderActive) FontWeight.Bold else FontWeight.Normal
                                        )
                                        var showTooltip by remember { mutableStateOf(false) }
                                        var isInfoFocused by remember { mutableStateOf(false) }
                                        Box {
                                            IconButton(
                                                onClick = { showTooltip = !showTooltip },
                                                modifier = Modifier
                                                    .size(24.dp)
                                                    .onFocusChanged { isInfoFocused = it.isFocused }
                                                    .background(
                                                        color = if (isInfoFocused) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f) else Color.Transparent,
                                                        shape = androidx.compose.foundation.shape.CircleShape
                                                    )
                                            ) {
                                                Icon(
                                                    imageVector = androidx.compose.material.icons.Icons.Default.Info,
                                                    contentDescription = "Info",
                                                    tint = if (isInfoFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                            if (showTooltip) {
                                                androidx.compose.material3.DropdownMenu(
                                                    expanded = showTooltip,
                                                    onDismissRequest = { showTooltip = false }
                                                ) {
                                                    Text(
                                                        text = provider.description,
                                                        modifier = Modifier.padding(8.dp),
                                                        style = MaterialTheme.typography.bodySmall
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    androidx.compose.animation.AnimatedVisibility(visible = expandedProviderId == provider.id) {
                                        Column(modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)) {
                                            provider.variants.forEachIndexed { vIndex, variant ->
                                                val isVariantActive = state.status.isCustomDns && state.status.dnsServers.split(",").map{it.trim()}.all { variant.servers.contains(it) }
                                                androidx.compose.runtime.key(variant.name) {
                                                    var isVariantFocused by remember { mutableStateOf(false) }
                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .onFocusChanged { isVariantFocused = it.isFocused }
                                                            .background(
                                                                color = if (isVariantFocused) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) else Color.Transparent,
                                                                shape = RoundedCornerShape(8.dp)
                                                            )
                                                            .clickable {
                                                                selectedVariant = variant
                                                                showConfirmationDialog = true
                                                            }
                                                            .padding(vertical = 8.dp, horizontal = 12.dp),
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Text(
                                                        text = "${vIndex + 1}. ${variant.name} (${variant.servers.joinToString(", ")})",
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = if (isVariantActive) Color(0xFF4CAF50) else MaterialTheme.colorScheme.onSurfaceVariant,
                                                        fontWeight = if (isVariantActive) FontWeight.Bold else FontWeight.Normal
                                                    )
                                                }
                                                }
                                            }
                                        }
                                    }
                                }
                                if (index < com.example.ui.getPublicDnsProviders(context).lastIndex) {
                                    androidx.compose.material3.HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        androidx.compose.material3.TextButton(onClick = { onCancelDnsChanges() }) {
                            Text(translateText(stringResource(R.string.action_close), context))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TelemetryCard(state: UiState, modifier: Modifier = Modifier) {
    var showTemperature by remember { mutableStateOf(false) }
    LaunchedEffect(state.speedHistory.lastOrNull()?.timestamp) {
        showTemperature = !showTemperature
    }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("telemetry_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Processor
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (showTemperature) translateText("Температура", androidx.compose.ui.platform.LocalContext.current) else translateText("Процессор", androidx.compose.ui.platform.LocalContext.current),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (showTemperature) {
                    if (state.cpuTemperature == null) {
                        Text(
                            text = translateText("недоступна", androidx.compose.ui.platform.LocalContext.current),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    } else {
                        val temp = state.cpuTemperature
                        val tempColor = when {
                            temp < 65f -> Color(0xFF009688) // Teal
                            temp < 75f -> Color(0xFF7FFF00) // Chartreuse
                            else -> Color(0xFFDC143C) // Crimson
                        }
                        Text(
                            text = String.format("%.1f °C", temp),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = tempColor,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    Text(
                        text = state.cpuUsage,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Divider
            Box(
                modifier = Modifier
                    .height(36.dp)
                    .width(1.dp)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            )

            // Memory
            Column(
                modifier = Modifier.weight(1.3f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "ОЗУ",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = state.memoryUsage,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            // Divider
            Box(
                modifier = Modifier
                    .height(36.dp)
                    .width(1.dp)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            )

            // Uptime
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Аптайм",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = state.uptime,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun DashboardTab(state: UiState,
    
    onLedToggle: (Boolean) -> Unit,
    onMasterVpnToggle: (Boolean) -> Unit,
    onOpenVpnListDialog: () -> Unit,
    onToggleTentativeVpnItem: (String, Boolean) -> Unit,
    onApplyVpnChanges: () -> Unit,
    onCancelVpnChanges: () -> Unit,
    onOpenDnsListDialog: () -> Unit,
    onDnsVariantSelected: (List<String>) -> Unit,
    onCancelDnsChanges: () -> Unit,
    onRefreshStatus: () -> Unit,
    onNavigateToConsole: (String?) -> Unit = {},
    onRefreshCapabilities: () -> Unit = {}
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val isTv = remember {
        context.packageManager.hasSystemFeature(android.content.pm.PackageManager.FEATURE_LEANBACK)
    }
    val configuration = androidx.compose.ui.platform.LocalConfiguration.current
    val isTablet = remember(configuration) {
        configuration.screenWidthDp >= 600
    }
    val isPortrait = configuration.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(
            top = 8.dp,
            bottom = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        if (isTv || (isTablet && !isPortrait)) {
            // TV or Tablet Landscape layout code: place toggles card next to status card in a row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    InternetStatusCard(
                        state = state,
                        onRefreshStatus = onRefreshStatus,
                        modifier = Modifier.weight(1f),
                        onNavigateToConsole = onNavigateToConsole, onRefreshCapabilities = onRefreshCapabilities,
                    )
                    TogglesCard(
                        state = state,
                        onLedToggle = onLedToggle,
                        onMasterVpnToggle = onMasterVpnToggle,
                        onOpenVpnListDialog = onOpenVpnListDialog,
                        onToggleTentativeVpnItem = onToggleTentativeVpnItem,
                        onApplyVpnChanges = onApplyVpnChanges,
                        onCancelVpnChanges = onCancelVpnChanges,
                        onOpenDnsListDialog = onOpenDnsListDialog,
                        onDnsVariantSelected = onDnsVariantSelected,
                        onCancelDnsChanges = onCancelDnsChanges,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            item {
                SpeedMeterCard(state = state, onNavigateToConsole = onNavigateToConsole, onRefreshCapabilities = onRefreshCapabilities)
            }
        } else {
            // Regular layout (Smartphones & Tablets in Portrait orientation)
            item {
                InternetStatusCard(
                    state = state,
                    onRefreshStatus = onRefreshStatus,
                    onNavigateToConsole = onNavigateToConsole, onRefreshCapabilities = onRefreshCapabilities,
                )
            }
            item {
                SpeedMeterCard(state = state, onNavigateToConsole = onNavigateToConsole, onRefreshCapabilities = onRefreshCapabilities)
            }
            item {
                TelemetryCard(state = state)
            }
            item {
                TogglesCard(
                    state = state,
                    onLedToggle = onLedToggle,
                    onMasterVpnToggle = onMasterVpnToggle,
                    onOpenVpnListDialog = onOpenVpnListDialog,
                    onToggleTentativeVpnItem = onToggleTentativeVpnItem,
                    onApplyVpnChanges = onApplyVpnChanges,
                    onCancelVpnChanges = onCancelVpnChanges,
                    onOpenDnsListDialog = onOpenDnsListDialog,
                    onDnsVariantSelected = onDnsVariantSelected,
                    onCancelDnsChanges = onCancelDnsChanges,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun StatusRow(label: String, value: String, iconColor: Color, imageVector: ImageVector, modifier: Modifier = Modifier) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val isTv = remember {
        context.packageManager.hasSystemFeature(android.content.pm.PackageManager.FEATURE_LEANBACK)
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(if (isTv) 20.dp else 24.dp)
        )
        Spacer(modifier = Modifier.width(if (isTv) 10.dp else 12.dp))
        Column {
            Text(
                text = translateText(label, context),
                style = if (isTv) MaterialTheme.typography.labelSmall else MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = translateText(value, context),
                style = if (isTv) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun ToggleRow(
    title: String,
    subtitle: String,
    isActive: Boolean,
    icon: ImageVector,
    onToggle: (Boolean) -> Unit,
    testTag: String,
    isCompact: Boolean = false,
    isSmartphone: Boolean = false,
    onIconClick: (() -> Unit)? = null,
    dropdownContent: (@Composable () -> Unit)? = null,
    enabled: Boolean = true
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val isTv = remember {
        context.packageManager.hasSystemFeature(android.content.pm.PackageManager.FEATURE_LEANBACK)
    }
    var isIconFocused by remember { mutableStateOf(false) }
    var isAreaFocused by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth().then(if (!enabled) Modifier.alpha(0.58f) else Modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left Column / Box with Icon (Focusable/Clickable separately if onIconClick != null)
        Box(
            modifier = if (onIconClick != null) {
                Modifier
                    .onFocusChanged { isIconFocused = it.isFocused }
                    .clip(RoundedCornerShape(8.dp))
                    .then(
                        if (isTv && isIconFocused) {
                            Modifier.background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f))
                        } else Modifier
                    )
                    .clickable { onIconClick() }
                    .padding(8.dp)
            } else {
                Modifier.padding(8.dp)
            },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(if (isTv) 22.dp else if (isSmartphone) 19.2.dp else if (isCompact) 24.dp else 28.dp)
            )

            if (onIconClick != null && dropdownContent != null) {
                dropdownContent()
            }
        }

        Spacer(modifier = Modifier.width(if (isTv) 4.dp else if (isSmartphone) 4.dp else if (isCompact) 6.dp else 8.dp))

        // Right area (Title, Subtitle, and Switch) clickable to toggle the switch!
        Row(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .onFocusChanged { isAreaFocused = it.isFocused }
                .then(
                    if (isTv && isAreaFocused && enabled) {
                        Modifier.background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
                    } else Modifier
                )
                .then(
                    if (enabled) Modifier.clickable { onToggle(!isActive) } else Modifier
                )
                .padding(
                    vertical = if (isTv) 5.dp else if (isSmartphone) 1.6.dp else if (isCompact) 2.dp else 8.dp,
                    horizontal = 8.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = if (isTv) {
                        MaterialTheme.typography.bodyMedium
                    } else if (isSmartphone) {
                        MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp)
                    } else if (isCompact) {
                        MaterialTheme.typography.bodyMedium
                    } else {
                        MaterialTheme.typography.bodyLarge
                    },
                    fontWeight = FontWeight.Bold
                )
                val greenColor = if (androidx.compose.foundation.isSystemInDarkTheme()) Color(0xFF00E676) else Color(0xFF2E7D32)
                Text(
                    text = subtitle,
                    style = if (isTv) {
                        MaterialTheme.typography.labelSmall
                    } else if (isSmartphone) {
                        MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp)
                    } else if (isCompact) {
                        MaterialTheme.typography.labelSmall
                    } else {
                        MaterialTheme.typography.bodySmall
                    },
                    color = when {
                        subtitle.startsWith("Включение") || subtitle.startsWith("Выключение") || subtitle.startsWith("Переключение") -> Color(0xFFFF9500)
                        isActive && title != "Светодиод роутера" -> greenColor
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
            Switch(
                checked = isActive,
                onCheckedChange = null, // Handled by row click
                enabled = enabled,
                colors = androidx.compose.material3.SwitchDefaults.colors(
                    uncheckedThumbColor = if (androidx.compose.foundation.isSystemInDarkTheme()) MaterialTheme.colorScheme.outline else Color(0xFF9E9E9E),
                    uncheckedTrackColor = if (androidx.compose.foundation.isSystemInDarkTheme()) MaterialTheme.colorScheme.surfaceVariant else Color(0xFFE0E0E0)
                ),
                modifier = Modifier
                    .testTag(testTag)
                    .offset(x = 1.dp)
                    .then(
                        if (isTv) Modifier.scale(0.9f) else if (isSmartphone) Modifier.scale(0.8f) else Modifier
                    )
            )
        }
    }
}

@Composable
fun SpeedChart(history: List<SpeedSnapshot>, modifier: Modifier = Modifier) {
    if (history.isEmpty()) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Ожидание данных трафика...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
        }
        return
    }

    val maxVal = history.maxOfOrNull { maxOf(it.downloadSpeedMbps, it.uploadSpeedMbps) } ?: 5f
    val scaleMax = maxOf(maxVal, 2.0f) // minimum scale of 2Mbps

    val downloadColor = if (androidx.compose.foundation.isSystemInDarkTheme()) Color(0xFF00E676) else Color(0xFF2E7D32)
    val uploadColor = Color(0xFFFFB300)
    val cpuColor = Color(0xFFFF1744) // Red color for CPU

    Canvas(modifier = modifier.padding(vertical = 4.dp)) {
        val width = size.width
        val height = size.height
        val margin = 8.dp.toPx()
        val graphWidth = width - 2 * margin
        val graphHeight = height - 2 * margin

        // Draw horizontal grid lines
        val gridLines = 3
        for (i in 0..gridLines) {
            val y = margin + graphHeight * (i.toFloat() / gridLines)
            drawLine(
                color = Color.Gray.copy(alpha = 0.12f),
                start = Offset(margin, y),
                end = Offset(width - margin, y),
                strokeWidth = 1.dp.toPx()
            )
        }

        if (history.size > 1) {
            val stepX = graphWidth / (history.size - 1)

            val dlPath = Path()
            val ulPath = Path()
            val cpuPath = Path()

            history.forEachIndexed { index, snapshot ->
                val x = margin + index * stepX
                val dlY = margin + graphHeight * (1f - (snapshot.downloadSpeedMbps / scaleMax).coerceIn(0f, 1f))
                val ulY = margin + graphHeight * (1f - (snapshot.uploadSpeedMbps / scaleMax).coerceIn(0f, 1f))
                val cpuY = margin + graphHeight * (1f - (snapshot.cpuUsagePercent / 100f).coerceIn(0f, 1f))

                if (index == 0) {
                    dlPath.moveTo(x, dlY)
                    ulPath.moveTo(x, ulY)
                    cpuPath.moveTo(x, cpuY)
                } else {
                    dlPath.lineTo(x, dlY)
                    ulPath.lineTo(x, ulY)
                    cpuPath.lineTo(x, cpuY)
                }
            }

            // Draw Paths with round stroke joins
            drawPath(
                path = dlPath,
                color = downloadColor,
                style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
            )

            drawPath(
                path = ulPath,
                color = uploadColor,
                style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
            )

            drawPath(
                path = cpuPath,
                color = cpuColor,
                style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
            )

            // Draw light translucent gradient fields under pathways
            val dlFill = Path().apply {
                addPath(dlPath)
                lineTo(margin + (history.size - 1) * stepX, margin + graphHeight)
                lineTo(margin, margin + graphHeight)
                close()
            }
            drawPath(
                path = dlFill,
                brush = Brush.verticalGradient(
                    colors = listOf(downloadColor.copy(alpha = 0.15f), Color.Transparent),
                    startY = margin,
                    endY = margin + graphHeight
                )
            )

            val ulFill = Path().apply {
                addPath(ulPath)
                lineTo(margin + (history.size - 1) * stepX, margin + graphHeight)
                lineTo(margin, margin + graphHeight)
                close()
            }
            drawPath(
                path = ulFill,
                brush = Brush.verticalGradient(
                    colors = listOf(uploadColor.copy(alpha = 0.12f), Color.Transparent),
                    startY = margin,
                    endY = margin + graphHeight
                )
            )

            val cpuFill = Path().apply {
                addPath(cpuPath)
                lineTo(margin + (history.size - 1) * stepX, margin + graphHeight)
                lineTo(margin, margin + graphHeight)
                close()
            }
            drawPath(
                path = cpuFill,
                brush = Brush.verticalGradient(
                    colors = listOf(cpuColor.copy(alpha = 0.08f), Color.Transparent),
                    startY = margin,
                    endY = margin + graphHeight
                )
            )
        }
    }
}

@Composable
fun MagnifyingGlassIcon(isPlus: Boolean, tint: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(24.dp)) {
        val width = size.width
        val height = size.height
        
        // Let's define the center of the lens
        val cx = width * 0.45f
        val cy = height * 0.45f
        val radius = width * 0.28f
        
        // Handle coordinates (bottom-right direction)
        val hStart = cx + radius * 0.707f
        val hEnd = width * 0.85f
        val hYStart = cy + radius * 0.707f
        val hYEnd = height * 0.85f
        
        // Draw the Lens circle outline
        drawCircle(
            color = tint,
            radius = radius,
            center = Offset(cx, cy),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
        )
        
        // Draw the Handle line
        drawLine(
            color = tint,
            start = Offset(hStart, hYStart),
            end = Offset(hEnd, hYEnd),
            strokeWidth = 3.5f.dp.toPx(),
            cap = StrokeCap.Round
        )
        
        // Draw inner sign (Minus or Plus)
        val signHalfLen = radius * 0.5f
        // Horizontal bar
        drawLine(
            color = tint,
            start = Offset(cx - signHalfLen, cy),
            end = Offset(cx + signHalfLen, cy),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )
        // Vertical bar if isPlus
        if (isPlus) {
            drawLine(
                color = tint,
                start = Offset(cx, cy - signHalfLen),
                end = Offset(cx, cy + signHalfLen),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )
        }
    }
}

fun parseAnsiToAnnotatedString(text: String): AnnotatedString {
    return buildAnnotatedString {
        var i = 0
        val n = text.length
        
        var currentStyle = SpanStyle()
        var isBold = false
        var isUnderline = false
        var fgColor: Color? = null
        var bgColor: Color? = null
        
        while (i < n) {
            val char = text[i]
            if (char == '\u001B' && i + 1 < n && text[i + 1] == '[') {
                // Find the ending 'm' (or other command character)
                var j = i + 2
                while (j < n && text[j] !in 'a'..'z' && text[j] !in 'A'..'Z') {
                    j++
                }
                if (j < n) {
                    val cmd = text[j]
                    val seq = text.substring(i + 2, j)
                    if (cmd == 'm') {
                        // Parse styling params
                        val parts = seq.split(';')
                        for (part in parts) {
                            val code = part.toIntOrNull() ?: 0
                            when (code) {
                                0 -> { // Reset
                                    isBold = false
                                    isUnderline = false
                                    fgColor = null
                                    bgColor = null
                                }
                                1 -> isBold = true
                                4 -> isUnderline = true
                                22 -> isBold = false // Normal weight
                                24 -> isUnderline = false
                                
                                // Foreground standard
                                30 -> fgColor = Color(0xFF000000) // Black
                                31 -> fgColor = Color(0xFFFF453A) // Red (Apple System Red / Bright red)
                                32 -> fgColor = Color(0xFF30D158) // Green
                                33 -> fgColor = Color(0xFFFFD60A) // Yellow
                                34 -> fgColor = Color(0xFF0A84FF) // Blue
                                35 -> fgColor = Color(0xFFBF5AF2) // Magenta
                                36 -> fgColor = Color(0xFF64D2FF) // Cyan
                                37 -> fgColor = Color(0xFFE5E5EA) // White
                                39 -> fgColor = null // Default fg
                                
                                // Foreground bright
                                90 -> fgColor = Color(0xFF8E8E93) // Bright Black (Gray)
                                91 -> fgColor = Color(0xFFFF453A) // Bright Red
                                92 -> fgColor = Color(0xFF30D158) // Bright Green
                                93 -> fgColor = Color(0xFFFFD60A) // Bright Yellow
                                94 -> fgColor = Color(0xFF0A84FF) // Bright Blue
                                95 -> fgColor = Color(0xFFBF5AF2) // Bright Magenta
                                96 -> fgColor = Color(0xFF64D2FF) // Bright Cyan
                                97 -> fgColor = Color(0xFFFFFFFF) // Bright White
                                
                                // Background standard
                                40 -> bgColor = Color(0xFF000000)
                                41 -> bgColor = Color(0xFFFF453A)
                                42 -> bgColor = Color(0xFF30D158)
                                43 -> bgColor = Color(0xFFFFD60A)
                                44 -> bgColor = Color(0xFF0A84FF)
                                45 -> bgColor = Color(0xFFBF5AF2)
                                46 -> bgColor = Color(0xFF64D2FF)
                                47 -> bgColor = Color(0xFFE5E5EA)
                                49 -> bgColor = null // Default bg
                            }
                        }
                        
                        // Collect updated style
                        currentStyle = SpanStyle(
                            color = fgColor ?: Color(0xFF30D158), // Default terminal color in app is 0xFF30D158 (green)
                            background = bgColor ?: Color.Unspecified,
                            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
                            textDecoration = if (isUnderline) TextDecoration.Underline else TextDecoration.None
                        )
                    }
                    // Move index beyond the ANSI gate
                    i = j + 1
                    continue
                }
            }
            
            // Append plain text
            val startTextIdx = i
            while (i < n && !(text[i] == '\u001B' && i + 1 < n && text[i + 1] == '[')) {
                i++
            }
            val plainSegment = text.substring(startTextIdx, i)
            if (plainSegment.isNotEmpty()) {
                pushStyle(currentStyle)
                append(plainSegment)
                pop()
            }
        }
    }
}

@Composable
fun ConsoleTab(
    consoleHistory: List<com.example.data.ConsoleLog>,
    commandOutput: String,
    
    sessionCommandHistory: List<String> = emptyList(),
    onCommandChange: (String) -> Unit,
    onSend: () -> Unit,
    onClearHistory: () -> Unit,
    onDeleteHistoryItem: (String) -> Unit = {},
    onWriteRawToConsoleStdin: (String) -> Unit = {},
    onAddHistoryItem: (String) -> Unit = {}
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val focusManager = androidx.compose.ui.platform.LocalFocusManager.current
    var historyIndex by remember { mutableStateOf(-1) }
    var terminalFontSize by remember { mutableStateOf(12f) }
    var isTerminalFocused by remember { mutableStateOf(false) }
    var cursorVisible by remember { mutableStateOf(false) }
    var currentTypedLine by remember { mutableStateOf("") }
    var lastEnteredCommand by remember { mutableStateOf("") }
    LaunchedEffect(isTerminalFocused) {
        if (isTerminalFocused) {
            while (true) {
                cursorVisible = !cursorVisible
                kotlinx.coroutines.delay(500)
            }
        } else {
            cursorVisible = false
        }
    }
    val clearButtonFocusRequester = remember { FocusRequester() }
    val termKeysFocusRequester = remember { FocusRequester() }
    val decreaseBtnFocusRequester = remember { FocusRequester() }
    val increaseBtnFocusRequester = remember { FocusRequester() }
    val copyBtnFocusRequester = remember { FocusRequester() }
    val pasteBtnFocusRequester = remember { FocusRequester() }

    val context = androidx.compose.ui.platform.LocalContext.current
    val isTv = remember {
        context.packageManager.hasSystemFeature(android.content.pm.PackageManager.FEATURE_LEANBACK)
    }
    val primaryColor = MaterialTheme.colorScheme.primary
    val locale = remember { context.resources.configuration.locales[0].language }

    var showBatteryOptDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val pm = context.getSystemService(android.content.Context.POWER_SERVICE) as android.os.PowerManager
        val isIgnoring = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            pm.isIgnoringBatteryOptimizations(context.packageName)
        } else true
        
        if (!isIgnoring) {
            val prefs = context.getSharedPreferences("router_prefs", android.content.Context.MODE_PRIVATE)
            if (!prefs.getBoolean("battery_optimization_requested", false)) {
                showBatteryOptDialog = true
            }
        }
    }

    if (showBatteryOptDialog) {
        AlertDialog(
            onDismissRequest = {
                showBatteryOptDialog = false
                context.getSharedPreferences("router_prefs", android.content.Context.MODE_PRIVATE)
                    .edit().putBoolean("battery_optimization_requested", true).apply()
            },
            title = { Text(if (locale == "ru") "Фоновое подключение" else "Background Connection") },
            text = { Text(if (locale == "ru") "Для удержания SSH-соединения при сворачивании приложения необходимо отключить оптимизацию батареи для этого приложения. Разрешить?" else "To keep the SSH connection alive when the app is minimized, you need to disable battery optimization for this app. Allow?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showBatteryOptDialog = false
                        context.getSharedPreferences("router_prefs", android.content.Context.MODE_PRIVATE)
                            .edit().putBoolean("battery_optimization_requested", true).apply()
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            val intent = android.content.Intent(
                                android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
                                android.net.Uri.parse("package:${context.packageName}")
                            )
                            try {
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                android.util.Log.e("BatteryOpt", "Failed to start intent: ${e.message}")
                            }
                        }
                    }
                ) {
                    Text(if (locale == "ru") "Разрешить" else "Allow")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showBatteryOptDialog = false
                        context.getSharedPreferences("router_prefs", android.content.Context.MODE_PRIVATE)
                            .edit().putBoolean("battery_optimization_requested", true).apply()
                    }
                ) {
                    Text(if (locale == "ru") "Позже" else "Later")
                }
            }
        )
    }

    val favoritePrefs = remember(context) { context.getSharedPreferences("console_favorite_commands_prefs", android.content.Context.MODE_PRIVATE) }
    var favoritesList by remember {
        mutableStateOf(
            run {
                val raw = favoritePrefs.getString("favorites_list", null)
                if (raw.isNullOrEmpty()) {
                    emptyList<String>()
                } else {
                    try {
                        val arr = org.json.JSONArray(raw)
                        val list = mutableListOf<String>()
                        for (i in 0 until arr.length()) {
                            list.add(arr.getString(i))
                        }
                        list
                    } catch (e: Exception) {
                        emptyList()
                    }
                }
            }
        )
    }

    fun saveFavorites(newList: List<String>) {
        favoritesList = newList
        try {
            val arr = org.json.JSONArray()
            newList.forEach { arr.put(it) }
            favoritePrefs.edit().putString("favorites_list", arr.toString()).commit()
        } catch (e: Exception) {}
    }

    var showFavoritesDialog by remember { mutableStateOf(false) }
    var showHistoryDialog by remember { mutableStateOf(false) }
    var showAddFavoriteDialog by remember { mutableStateOf(false) }
    var newFavoriteText by remember { mutableStateOf("") }

    val msgAdded = remember(locale) { IperfLocalizations.getMsgAdded(locale) }
    val msgExist = remember(locale) { IperfLocalizations.getMsgExist(locale) }
    val msgEmpty = remember(locale) { IperfLocalizations.getMsgEmpty(locale) }

    // Automatically scroll to bottom when history or command output gets updated
    LaunchedEffect(consoleHistory.size, consoleHistory.firstOrNull()?.output, commandOutput) {
        if (consoleHistory.isNotEmpty()) {
            listState.scrollToItem(0)
        }
    }

    if (showAddFavoriteDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showAddFavoriteDialog = false },
            title = { Text(text = translateText("Добавить в избранное", context), color = Color.White) },
            text = {
                androidx.compose.material3.OutlinedTextField(
                    value = newFavoriteText,
                    onValueChange = { newFavoriteText = it },
                    label = { Text(translateText("Команда", context)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                androidx.compose.material3.TextButton(
                    onClick = {
                        val cmd = newFavoriteText.trim()
                        if (cmd.isNotEmpty()) {
                            if (!favoritesList.contains(cmd)) {
                                val updated = favoritesList + cmd
                                saveFavorites(updated)
                                android.widget.Toast.makeText(context, msgAdded, android.widget.Toast.LENGTH_SHORT).show()
                            } else {
                                android.widget.Toast.makeText(context, msgExist, android.widget.Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            android.widget.Toast.makeText(context, msgEmpty, android.widget.Toast.LENGTH_SHORT).show()
                        }
                        newFavoriteText = ""
                        showAddFavoriteDialog = false
                    }
                ) {
                    Text(translateText("Добавить", context))
                }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(onClick = { showAddFavoriteDialog = false }) {
                    Text(translateText("Отмена", context))
                }
            },
            containerColor = Color(0xFF1E1E24)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .padding(16.dp)
            .testTag("console_tab_view"),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = translateText("SSH терминал", context),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                var isDecreaseBtnFocused by remember { mutableStateOf(false) }
                IconButton(
                    onClick = { if (terminalFontSize > 8f) terminalFontSize -= 1f },
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("decrease_console_font_btn")
                        .focusRequester(decreaseBtnFocusRequester)
                        .focusProperties {
                            right = increaseBtnFocusRequester
                        }
                        .onFocusChanged { isDecreaseBtnFocused = it.isFocused }
                        .drawWithContent {
                            drawContent()
                            if (isDecreaseBtnFocused) {
                                val strokeWidth = 2.dp.toPx()
                                val borderCol = if (isTv) primaryColor else Color.White
                                drawRoundRect(
                                    color = borderCol,
                                    topLeft = androidx.compose.ui.geometry.Offset(strokeWidth / 2, strokeWidth / 2),
                                    size = androidx.compose.ui.geometry.Size(size.width - strokeWidth, size.height - strokeWidth),
                                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(8.dp.toPx(), 8.dp.toPx()),
                                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidth)
                                )
                            }
                        }
                ) {
                    MagnifyingGlassIcon(isPlus = false, tint = MaterialTheme.colorScheme.primary)
                }
                var isIncreaseBtnFocused by remember { mutableStateOf(false) }
                IconButton(
                    onClick = { if (terminalFontSize < 32f) terminalFontSize += 1f },
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("increase_console_font_btn")
                        .focusRequester(increaseBtnFocusRequester)
                        .focusProperties {
                            left = decreaseBtnFocusRequester
                            right = copyBtnFocusRequester
                        }
                        .onFocusChanged { isIncreaseBtnFocused = it.isFocused }
                        .drawWithContent {
                            drawContent()
                            if (isIncreaseBtnFocused) {
                                val strokeWidth = 2.dp.toPx()
                                val borderCol = if (isTv) primaryColor else Color.White
                                drawRoundRect(
                                    color = borderCol,
                                    topLeft = androidx.compose.ui.geometry.Offset(strokeWidth / 2, strokeWidth / 2),
                                    size = androidx.compose.ui.geometry.Size(size.width - strokeWidth, size.height - strokeWidth),
                                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(8.dp.toPx(), 8.dp.toPx()),
                                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidth)
                                )
                            }
                        }
                ) {
                    MagnifyingGlassIcon(isPlus = true, tint = MaterialTheme.colorScheme.primary)
                }
                var isCopyBtnFocused by remember { mutableStateOf(false) }
                val clipboardManager = androidx.compose.ui.platform.LocalClipboardManager.current
                val copySuccessMsg = translateText("Текст скопирован в буфер обмена", context)
                IconButton(
                    onClick = {
                        val allText = consoleHistory.asReversed().joinToString("\n\n") { log ->
                            "root@openwrt:~# ${log.command}\n${log.output}"
                        }
                        clipboardManager.setText(androidx.compose.ui.text.AnnotatedString(allText))
                        android.widget.Toast.makeText(context, copySuccessMsg, android.widget.Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("copy_console_btn")
                        .focusRequester(copyBtnFocusRequester)
                        .focusProperties {
                            left = increaseBtnFocusRequester
                            right = pasteBtnFocusRequester
                        }
                        .onFocusChanged { isCopyBtnFocused = it.isFocused }
                        .border(
                            width = 2.dp,
                            color = if (isCopyBtnFocused) Color.White else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    Icon(
                        imageVector = CopyIcon,
                        contentDescription = translateText("Копировать", context),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                var isPasteBtnFocused by remember { mutableStateOf(false) }
                IconButton(
                    onClick = {
                        clipboardManager.getText()?.text?.let { pasteText ->
                            pasteText.forEach { char ->
                                val toSend = if (char == '\n') "\r" else char.toString()
                                onWriteRawToConsoleStdin(toSend)
                                if (char == '\n' || char == '\r') {
                                    val cmd = currentTypedLine.trim()
                                    if (cmd.isNotEmpty()) {
                                        lastEnteredCommand = cmd
                                        onAddHistoryItem(cmd)
                                    }
                                    currentTypedLine = ""
                                } else {
                                    currentTypedLine += char
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("paste_console_btn")
                        .focusRequester(pasteBtnFocusRequester)
                        .focusProperties {
                            left = copyBtnFocusRequester
                            right = clearButtonFocusRequester
                        }
                        .onFocusChanged { isPasteBtnFocused = it.isFocused }
                        .border(
                            width = 2.dp,
                            color = if (isPasteBtnFocused) Color.White else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    Icon(
                        imageVector = PasteIcon,
                        contentDescription = translateText("Вставить", context),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                var isClearBtnFocused by remember { mutableStateOf(false) }
                IconButton(
                    onClick = onClearHistory,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("clear_console_btn")
                        .focusRequester(clearButtonFocusRequester)
                        .focusProperties {
                            left = pasteBtnFocusRequester
                        }
                        .onFocusChanged { isClearBtnFocused = it.isFocused }
                        .border(
                            width = 2.dp,
                            color = if (isClearBtnFocused) Color.White else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = translateText("Очистить логи", context),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        // Terminal Output container (Hacker styled Green on black)
        var terminalView by remember { mutableStateOf<com.example.ui.TerminalInputView?>(null) }
        val imm = context.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager

        LaunchedEffect(terminalView) {
            if (terminalView != null) {
                kotlinx.coroutines.delay(300)
                terminalView?.requestFocus()
                                    terminalView?.let { 
                        imm.restartInput(it)
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                            it.windowInsetsController?.show(android.view.WindowInsets.Type.ime())
                        } else {
                            imm.showSoftInput(it, android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT) 
                        }
                    }
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF0F0F14))
                .border(
                    width = 2.dp,
                    color = if (isTerminalFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable {
                    terminalView?.requestFocus()
                                        terminalView?.let { 
                        imm.restartInput(it)
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                            it.windowInsetsController?.show(android.view.WindowInsets.Type.ime())
                        } else {
                            imm.showSoftInput(it, android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT) 
                        }
                    }
                }
                .padding(12.dp)
        ) {
            val clipboardManagerLocal = androidx.compose.ui.platform.LocalClipboardManager.current
            
            androidx.compose.ui.viewinterop.AndroidView(
                factory = { ctx ->
                    com.example.ui.TerminalInputView(ctx).apply {
                        terminalView = this
                    }
                },
                update = { view ->
                    view.onProcessIncomingText = { insertedText ->
                        for (char in insertedText) {
                            val toSend = if (char == '\n') "\r" else char.toString()
                            onWriteRawToConsoleStdin(toSend)
                            if (char == '\n' || char == '\r') {
                                val cmd = currentTypedLine.trim()
                                if (cmd.isNotEmpty()) {
                                    lastEnteredCommand = cmd
                                    onAddHistoryItem(cmd)
                                }
                                currentTypedLine = ""
                            } else {
                                currentTypedLine += char
                            }
                        }
                    }
                    view.onHandlePaste = {
                        val pastedText = clipboardManagerLocal.getText()?.text
                        if (pastedText != null) {
                            view.onProcessIncomingText(pastedText.toString())
                        }
                    }
                    view.onDeleteSurroundingText = { before, _ ->
                        val textToDelete = currentTypedLine.takeLast(before)
                        val bytesToDelete = textToDelete.toByteArray(Charsets.UTF_8).size
                        repeat(bytesToDelete) {
                            onWriteRawToConsoleStdin("\u007F")
                        }
                        if (currentTypedLine.isNotEmpty()) {
                            val actualBefore = minOf(before, currentTypedLine.length)
                            currentTypedLine = currentTypedLine.dropLast(actualBefore)
                        }
                    }
                    view.onSendKeyEvent = { keyEvent ->
                        if (keyEvent.action == android.view.KeyEvent.ACTION_DOWN) {
                            val isUpDown = keyEvent.keyCode == android.view.KeyEvent.KEYCODE_DPAD_UP || keyEvent.keyCode == android.view.KeyEvent.KEYCODE_DPAD_DOWN
                            if (isUpDown) {
                                if (consoleHistory.isNotEmpty()) {
                                    scope.launch {
                                        val amount = if (keyEvent.keyCode == android.view.KeyEvent.KEYCODE_DPAD_UP) -150f else 150f
                                        listState.animateScrollBy(amount)
                                    }
                                }
                                true
                            } else if (keyEvent.keyCode == android.view.KeyEvent.KEYCODE_ENTER) {
                                view.onProcessIncomingText("\r")
                                true
                            } else if (keyEvent.keyCode == android.view.KeyEvent.KEYCODE_DEL) {
                                view.onDeleteSurroundingText(1, 0)
                                true
                            } else {
                                false
                            }
                        } else {
                            false
                        }
                    }
                    view.setOnFocusChangeListener { _, hasFocus ->
                        isTerminalFocused = hasFocus
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            Box(modifier = Modifier.fillMaxSize()) {
                            // Render logs
                            if (consoleHistory.isEmpty()) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.BottomStart
                                ) {
                                    Text(
                                        text = translateText("Подключение к интерактивной оболочке SSH...", context),
                                        color = Color(0xFF30D158),
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = (terminalFontSize + 1f).sp,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            } else {
                                LazyColumn(
                                    state = listState,
                                    modifier = Modifier.fillMaxSize(),
                                    reverseLayout = true,
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    itemsIndexed(consoleHistory) { index, log ->
                                        SelectionContainer {
                                            Column(modifier = Modifier.fillMaxWidth()) {
                                                Text(
                                                    text = "root@openwrt:~# ${log.command}",
                                                    color = Color(0xFFFF9500),
                                                    fontFamily = FontFamily.Monospace,
                                                    fontSize = (terminalFontSize + 1f).sp,
                                                    fontWeight = FontWeight.Bold,
                                                    lineHeight = (terminalFontSize + 4f).sp
                                                )
                                                Spacer(modifier = Modifier.height(2.dp))
                                                
                                                val parsedOutput = androidx.compose.runtime.remember(log.output) {
                                                    parseAnsiToAnnotatedString(log.output)
                                                }
                                                val isMostRecentSh = index == 0 && log.command == "sh"
                                                val finalOutput = if (isMostRecentSh && cursorVisible) {
                                                    androidx.compose.ui.text.buildAnnotatedString {
                                                        append(parsedOutput)
                                                        append("█")
                                                    }
                                                } else {
                                                    parsedOutput
                                                }
                                                
                                                Text(
                                                    text = finalOutput,
                                                    color = Color(0xFF30D158),
                                                    fontFamily = FontFamily.Monospace,
                                                    fontSize = terminalFontSize.sp,
                                                    lineHeight = (terminalFontSize + 3f).sp,
                                                    modifier = Modifier.padding(start = 8.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                        }
        }

        // Termux-style auxiliary keyboard control bar
        val termuxKeys = listOf("ADD_FAV", "SHOW_FAV", "▲", "ESC", "CTRL", "ALT", "/", "-", "_", "|", "&&", ";")
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1E1E24), RoundedCornerShape(8.dp))
        ) {
            val keyboardRowState = rememberLazyListState()
            val showLeftArrow by remember { derivedStateOf { keyboardRowState.canScrollBackward } }
            val showRightArrow by remember { derivedStateOf { keyboardRowState.canScrollForward } }

            LazyRow(
                state = keyboardRowState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .padding(
                        start = if (showLeftArrow) 24.dp else 4.dp,
                        end = if (showRightArrow) 24.dp else 4.dp
                    ),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(termuxKeys) { key ->
                    var isKeyFocused by remember { mutableStateOf(false) }
                    val itemModifier = if (key == "ADD_FAV") {
                        Modifier.focusRequester(termKeysFocusRequester)
                    } else {
                        Modifier
                    }
                    Box(
                        modifier = Modifier
                            .height(38.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (isKeyFocused) MaterialTheme.colorScheme.primary else Color(0xFF2D2D36))
                            .then(itemModifier)
                            .onFocusChanged { isKeyFocused = it.isFocused }
                            .focusable()
                            .clickable {
                                when (key) {
                                    "ADD_FAV" -> {
                                        val lastCmd = if (lastEnteredCommand.isNotEmpty()) {
                                            lastEnteredCommand
                                        } else {
                                            sessionCommandHistory.firstOrNull() ?: ""
                                        }
                                        newFavoriteText = lastCmd
                                        showAddFavoriteDialog = true
                                    }
                                    "SHOW_FAV" -> {
                                        showFavoritesDialog = true
                                    }
                                    "ESC" -> {
                                        onWriteRawToConsoleStdin("\u001B")
                                    }
                                    "CTRL" -> {
                                        // Send Ctrl+C sequence
                                        onWriteRawToConsoleStdin("\u0003")
                                        currentTypedLine = ""
                                    }
                                    "ALT" -> {
                                        // Standard Escape prefix for ALT
                                        onWriteRawToConsoleStdin("\u001B")
                                    }
                                    "▲" -> {
                                        showHistoryDialog = true
                                    }
                                    else -> {
                                        onWriteRawToConsoleStdin(key)
                                    }
                                }
                            }
                            .padding(horizontal = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        when (key) {
                            "ADD_FAV" -> {
                                Icon(
                                    imageVector = StarOutlineIcon,
                                    contentDescription = "Add to Favorites",
                                    tint = Color(0xFFFFB300),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            "SHOW_FAV" -> {
                                Icon(
                                    imageVector = StarFilledIcon,
                                    contentDescription = "Show Favorites",
                                    tint = Color(0xFFFFB300),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            else -> {
                                Text(
                                    text = key,
                                    color = if (key == "CTRL" || key == "ESC" || key == "▲") Color(0xFF64D2FF) else Color.White,
                                    style = MaterialTheme.typography.labelMedium.copy(fontFamily = FontFamily.Monospace),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            if (showLeftArrow) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .matchParentSize()
                        .width(28.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0xFF1E1E24), Color.Transparent)
                            )
                        ),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "Scroll Left",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            if (showRightArrow) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .matchParentSize()
                        .width(28.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color.Transparent, Color(0xFF1E1E24))
                            )
                        ),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Scroll Right",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }



        if (showFavoritesDialog) {
            Dialog(
                onDismissRequest = { showFavoritesDialog = false },
                properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .border(width = 1.dp, color = Color.White.copy(alpha = 0.12f), shape = RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF1E1E24)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = IperfLocalizations.getFavoritesTitle(locale),
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            IconButton(
                                onClick = { showFavoritesDialog = false },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close",
                                    tint = Color.White.copy(alpha = 0.6f)
                                )
                            }
                        }

                        if (favoritesList.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = IperfLocalizations.getFavoritesEmpty(locale),
                                    color = Color.White.copy(alpha = 0.4f),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        } else {
                            val consoleFavsFocusRequester = remember { FocusRequester() }
                            LaunchedEffect(showFavoritesDialog) {
                                if (showFavoritesDialog && favoritesList.isNotEmpty()) {
                                    try {
                                        delay(100)
                                        consoleFavsFocusRequester.requestFocus()
                                    } catch (_: Exception) {}
                                }
                            }
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 280.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                itemsIndexed(favoritesList) { index, favCmd ->
                                    var isTextFocused by remember { mutableStateOf(false) }
                                    var isDeleteFocused by remember { mutableStateOf(false) }
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 2.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(8.dp))
                                                .focusRequester(if (index == 0) consoleFavsFocusRequester else FocusRequester())
                                                .onFocusChanged { isTextFocused = it.isFocused }
                                                .background(
                                                    if (isTextFocused) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                                                    else Color(0xFF2D2D36)
                                                )
                                                .clickable {
                                                    onWriteRawToConsoleStdin(favCmd + "\r")
                                                    showFavoritesDialog = false
                                                }
                                                .onKeyEvent { keyEvent ->
                                                    if (keyEvent.type == androidx.compose.ui.input.key.KeyEventType.KeyDown) {
                                                        val keyCode = keyEvent.nativeKeyEvent.keyCode
                                                        if (keyCode == android.view.KeyEvent.KEYCODE_DPAD_CENTER || keyCode == android.view.KeyEvent.KEYCODE_ENTER) {
                                                            onWriteRawToConsoleStdin(favCmd + "\r")
                                                            showFavoritesDialog = false
                                                            true
                                                        } else {
                                                            false
                                                        }
                                                    } else {
                                                        false
                                                    }
                                                }
                                                .padding(horizontal = 12.dp, vertical = 10.dp)
                                        ) {
                                            Text(
                                                text = favCmd,
                                                fontFamily = FontFamily.Monospace,
                                                color = Color.White,
                                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp)
                                            )
                                        }
                                        IconButton(
                                            onClick = {
                                                val updated = favoritesList.filter { it != favCmd }
                                                saveFavorites(updated)
                                            },
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .onFocusChanged { isDeleteFocused = it.isFocused }
                                                .background(
                                                    if (isDeleteFocused) Color.Red.copy(alpha = 0.2f)
                                                    else Color.Transparent
                                                )
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Delete",
                                                tint = if (isDeleteFocused) Color.Red else Color.Red.copy(alpha = 0.8f),
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        val historyFocusRequester = remember { FocusRequester() }

        LaunchedEffect(showHistoryDialog) {
            if (showHistoryDialog && sessionCommandHistory.isNotEmpty()) {
                try {
                    kotlinx.coroutines.delay(100)
                    historyFocusRequester.requestFocus()
                } catch (e: Exception) {}
            }
        }

if (showHistoryDialog) {
            androidx.compose.ui.window.Dialog(
                onDismissRequest = { showHistoryDialog = false },
                properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .border(width = 1.dp, color = Color.White.copy(alpha = 0.12f), shape = RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF1E1E24)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = IperfLocalizations.getHistoryTitle(locale),
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = { showHistoryDialog = false },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close",
                                    tint = Color.White.copy(alpha = 0.6f)
                                )
                            }
                        }

                        if (sessionCommandHistory.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = IperfLocalizations.getHistoryEmpty(locale),
                                    color = Color.White.copy(alpha = 0.4f),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        } else {
                            val historyListState = rememberLazyListState()
                            LazyColumn(
                                state = historyListState,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 280.dp)
                                    .drawScrollbar(historyListState),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                itemsIndexed(sessionCommandHistory) { index, itemCmd ->
                                    var isTextFocused by remember { mutableStateOf(false) }
                                    var isDeleteFocused by remember { mutableStateOf(false) }
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 2.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(8.dp))
                                                .focusRequester(if (index == 0) historyFocusRequester else FocusRequester())
                                                .onFocusChanged { isTextFocused = it.isFocused }
                                                .background(
                                                    if (isTextFocused) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                                                    else Color(0xFF2D2D36)
                                                )
                                                .clickable {
                                                    onWriteRawToConsoleStdin(itemCmd + "\r")
                                                    showHistoryDialog = false
                                                }
                                                .onKeyEvent { keyEvent ->
                                                    if (keyEvent.type == androidx.compose.ui.input.key.KeyEventType.KeyDown) {
                                                        val keyCode = keyEvent.nativeKeyEvent.keyCode
                                                        if (keyCode == android.view.KeyEvent.KEYCODE_DPAD_CENTER || keyCode == android.view.KeyEvent.KEYCODE_ENTER) {
                                                            onWriteRawToConsoleStdin(itemCmd + "\r")
                                                            showHistoryDialog = false
                                                            true
                                                        } else {
                                                            false
                                                        }
                                                    } else {
                                                        false
                                                    }
                                                }
                                                .padding(horizontal = 12.dp, vertical = 10.dp)
                                        ) {
                                            Text(
                                                text = itemCmd,
                                                fontFamily = FontFamily.Monospace,
                                                color = Color.White,
                                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp)
                                            )
                                        }
                                        IconButton(
                                            onClick = {
                                                onDeleteHistoryItem(itemCmd)
                                            },
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .onFocusChanged { isDeleteFocused = it.isFocused }
                                                .background(
                                                    if (isDeleteFocused) Color.Red.copy(alpha = 0.2f)
                                                    else Color.Transparent
                                                )
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Delete",
                                                tint = if (isDeleteFocused) Color.Red else Color.Red.copy(alpha = 0.8f),
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

data class UpdateInfo(val version: String, val releaseNotes: String, val apkUrl: String)

suspend fun checkUpdate(lang: String): UpdateInfo? {
    return kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
        try {
            val url = java.net.URL("https://api.github.com/repos/Vihtoor/openwrt-router-control/releases/latest")
            val connection = url.openConnection() as java.net.HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Accept", "application/vnd.github.v3+json")
            if (connection.responseCode == 200) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                val json = org.json.JSONObject(response)
                val tagName = json.getString("tag_name")
                var body = json.optString("body", "")
                val assets = json.getJSONArray("assets")
                var apkUrl = ""
                for (i in 0 until assets.length()) {
                    val asset = assets.getJSONObject(i)
                    if (asset.getString("name").endsWith(".apk")) {
                        apkUrl = asset.getString("browser_download_url")
                        break
                    }
                }
                
                if (apkUrl.isNotEmpty()) {
                    return@withContext UpdateInfo(tagName, body, apkUrl)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        null
    }
}

fun downloadAndInstallApk(context: android.content.Context, url: String) {
    val fileName = "openwrt_router_control_update.apk"
    val downloadDir = context.getExternalFilesDir(android.os.Environment.DIRECTORY_DOWNLOADS)
    if (downloadDir != null && !downloadDir.exists()) {
        downloadDir.mkdirs()
    }
    val file = java.io.File(downloadDir, fileName)
    if (file.exists()) {
        file.delete()
    }

    android.widget.Toast.makeText(context, "Загрузка обновления...", android.widget.Toast.LENGTH_SHORT).show()
    
    kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
        try {
            val connection = java.net.URL(url).openConnection() as java.net.HttpURLConnection
            connection.requestMethod = "GET"
            connection.connect()
            if (connection.responseCode == 200) {
                connection.inputStream.use { input ->
                    java.io.FileOutputStream(file).use { output ->
                        input.copyTo(output)
                    }
                }
                kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                    val intent = android.content.Intent(android.content.Intent.ACTION_VIEW)
                    intent.setDataAndType(androidx.core.content.FileProvider.getUriForFile(context, "${context.packageName}.provider", file), "application/vnd.android.package-archive")
                    intent.addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                android.widget.Toast.makeText(context, "Ошибка загрузки: ${e.message}", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }
}
fun getLocalIpAddress(targetIp: String? = null): String {
    val cleanTargetIp = targetIp?.removePrefix("[")?.removeSuffix("]")
    if (!cleanTargetIp.isNullOrEmpty() && cleanTargetIp != "127.0.0.1") {
        try {
            java.net.DatagramSocket().use { socket ->
                socket.connect(java.net.InetAddress.getByName(cleanTargetIp), 1024)
                val localAddress = socket.localAddress.hostAddress
                if (!localAddress.isNullOrEmpty() && localAddress != "0.0.0.0") {
                    return localAddress
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
    try {
        val interfaces = java.net.NetworkInterface.getNetworkInterfaces()
        val interfaceList = ArrayList<java.net.NetworkInterface>()
        while (interfaces.hasMoreElements()) {
            interfaceList.add(interfaces.nextElement())
        }

        // Prioritize: wlan (Wi-Fi), then ap (Access Point), then eth/en (Ethernet)
        interfaceList.sortWith { o1, o2 ->
            val name1 = o1.name.lowercase()
            val name2 = o2.name.lowercase()
            val score1 = when {
                name1.startsWith("wlan") -> 0
                name1.startsWith("ap") -> 1
                name1.startsWith("eth") || name1.startsWith("en") -> 2
                else -> 3
            }
            val score2 = when {
                name2.startsWith("wlan") -> 0
                name2.startsWith("ap") -> 1
                name2.startsWith("eth") || name2.startsWith("en") -> 2
                else -> 3
            }
            score1.compareTo(score2)
        }

        for (networkInterface in interfaceList) {
            val addresses = networkInterface.inetAddresses
            while (addresses.hasMoreElements()) {
                val address = addresses.nextElement()
                if (!address.isLoopbackAddress && address is java.net.Inet4Address) {
                    val ip = address.hostAddress
                    if (!ip.isNullOrEmpty()) {
                        return ip
                    }
                }
            }
        }
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
    return "127.0.0.1"
}

@Composable
fun IPerfFullScreen(
    state: com.example.ui.UiState,
    onClose: () -> Unit,
    viewModel: com.example.ui.RouterViewModel
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val keyboardController = androidx.compose.ui.platform.LocalSoftwareKeyboardController.current
    val focusManager = androidx.compose.ui.platform.LocalFocusManager.current
    val iPerfLogs by IperfServerManager.logs.collectAsStateWithLifecycle()
    val isRunning by IperfServerManager.isRunning.collectAsStateWithLifecycle()
    val sessionCommandHistory by viewModel.iperfCommandHistory.collectAsStateWithLifecycle()
    val localIpAddress = remember(state.config?.ipAddress) { getLocalIpAddress(state.config?.ipAddress) }
    var routerTextFieldValue by remember { mutableStateOf(TextFieldValue("iperf3 -c $localIpAddress -t 30")) }
    var iPerfHistoryIndex by remember { mutableStateOf(-1) }
    
    var splitterRatio by remember { mutableStateOf(0.5f) }
    var parentWidth by remember { mutableStateOf(0f) }
    var parentHeight by remember { mutableStateOf(0f) }
    val locale = remember { context.resources.configuration.locales[0].language }
    val sharedPrefs = remember { context.getSharedPreferences("router_app_prefs", android.content.Context.MODE_PRIVATE) }
    var showInteractiveSplitterTip by remember {
        mutableStateOf(sharedPrefs.getBoolean("show_splitter_tip_v1", true))
    }
    
    val iperfFavoritePrefs = remember(context) { context.getSharedPreferences("iperf_favorite_commands_prefs", android.content.Context.MODE_PRIVATE) }
    var iperfFavoritesList by remember {
        mutableStateOf(
            run {
                val raw = iperfFavoritePrefs.getString("favorites_list", null)
                if (raw.isNullOrEmpty()) {
                    emptyList<String>()
                } else {
                    try {
                        val arr = org.json.JSONArray(raw)
                        val list = mutableListOf<String>()
                        for (i in 0 until arr.length()) {
                            list.add(arr.getString(i))
                        }
                        list
                    } catch (e: Exception) {
                        emptyList()
                    }
                }
            }
        )
    }

    fun saveIperfFavorites(newList: List<String>) {
        iperfFavoritesList = newList
        try {
            val arr = org.json.JSONArray()
            newList.forEach { arr.put(it) }
            iperfFavoritePrefs.edit().putString("favorites_list", arr.toString()).commit()
        } catch (e: Exception) {}
    }

    var showIperfFavoritesDialog by remember { mutableStateOf(false) }
    var showIperfHistoryDialog by remember { mutableStateOf(false) }

    val msgAdded = remember(locale) { IperfLocalizations.getMsgAdded(locale) }
    val msgExist = remember(locale) { IperfLocalizations.getMsgExist(locale) }
    val msgEmpty = remember(locale) { IperfLocalizations.getMsgEmpty(locale) }
    
    var routerConsoleFontSize by remember { mutableStateOf(12f) }
    var iperfConsoleFontSize by remember { mutableStateOf(12f) }
    var isInputFocused by remember { mutableStateOf(false) }
    var isSendFocused by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val iperfRouterSendFocusRequester = remember { FocusRequester() }
    val iperfBackFocusRequester = remember { FocusRequester() }

    val formatTitle = stringResource(R.string.iperf_server_title, "$localIpAddress:5201")
    
    LaunchedEffect(localIpAddress) {
        viewModel.setCommandInput("iperf3 -c $localIpAddress -t 30")
    }
    
    LaunchedEffect(routerTextFieldValue.text) {
        if (routerTextFieldValue.text.isEmpty()) {
            iPerfHistoryIndex = -1
        }
    }
    
    // Start and stop of the iPerf server is now managed in MainActivity's MainScreen scope
    // to prevent server restarts during UI recompositions of the iPerf full screen console.
    

    
    val configuration = androidx.compose.ui.platform.LocalConfiguration.current
    val isTv = remember {
        context.packageManager.hasSystemFeature(android.content.pm.PackageManager.FEATURE_LEANBACK)
    }
    val isTablet = configuration.screenWidthDp >= 600
    val isTvOrTablet = isTv || isTablet
    
    // Automatically focus on router console send btn on launch for TVs
    LaunchedEffect(Unit) {
        if (isTv) {
            delay(200)
            try {
                iperfRouterSendFocusRequester.requestFocus()
            } catch (e: Exception) {}
        }
    }
    var isSplitterFocused by remember { mutableStateOf(false) }
    
    val iPerfLogsState = rememberLazyListState()
    val routerLogsState = rememberLazyListState()
    
    LaunchedEffect(iPerfLogs.size) {
        if (iPerfLogs.isNotEmpty()) {
            iPerfLogsState.animateScrollToItem(iPerfLogs.size - 1)
        }
    }
    
    LaunchedEffect(state.consoleHistory.size, state.commandOutput) {
        if (state.consoleHistory.isNotEmpty()) {
            routerLogsState.animateScrollToItem(0)
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F0F14))
            .imePadding()
            .padding(16.dp)
            .testTag("iperf_fullscreen_view")
            .onGloballyPositioned { coordinates ->
                parentWidth = coordinates.size.width.toFloat()
                parentHeight = coordinates.size.height.toFloat()
            }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                var isIPerfBackFocused by remember { mutableStateOf(false) }
                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .testTag("iperf_back_btn")
                        .size(36.dp)
                        .focusRequester(iperfBackFocusRequester)
                        .onFocusChanged { isIPerfBackFocused = it.isFocused }
                        .focusProperties {
                            up = iperfRouterSendFocusRequester
                        }
                        .then(
                            if (isTv) {
                                Modifier.border(
                                    width = 1.5.dp,
                                    color = if (isIPerfBackFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                                    shape = RoundedCornerShape(18.dp)
                                )
                            } else Modifier
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Назад",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    text = formatTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            
            if (isTvOrTablet) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            parentWidth = coordinates.size.width.toFloat()
                        },
                horizontalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(splitterRatio)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF16161C))
                        .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.iperf_console_title),
                                style = MaterialTheme.typography.titleMedium,
                                color = Color(0xFF64D2FF),
                                fontWeight = FontWeight.Bold
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                var isDecreaseIperfFontFocused by remember { mutableStateOf(false) }
                                IconButton(
                                    onClick = {
                                        if (iperfConsoleFontSize > 8f) iperfConsoleFontSize -= 1f
                                    },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .testTag("btn_decrease_iperf_font_tablet")
                                        .onFocusChanged { isDecreaseIperfFontFocused = it.isFocused }
                                        .then(
                                            if (isTv) {
                                                Modifier.border(
                                                    width = 1.5.dp,
                                                    color = if (isDecreaseIperfFontFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                                                    shape = RoundedCornerShape(18.dp)
                                                )
                                            } else Modifier
                                        )
                                ) {
                                    MagnifyingGlassIcon(isPlus = false, tint = Color.White.copy(alpha = 0.6f))
                                }
                                var isIncreaseIperfFontFocused by remember { mutableStateOf(false) }
                                IconButton(
                                    onClick = {
                                        if (iperfConsoleFontSize < 32f) iperfConsoleFontSize += 1f
                                    },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .testTag("btn_increase_iperf_font_tablet")
                                        .onFocusChanged { isIncreaseIperfFontFocused = it.isFocused }
                                        .then(
                                            if (isTv) {
                                                Modifier.border(
                                                    width = 1.5.dp,
                                                    color = if (isIncreaseIperfFontFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                                                    shape = RoundedCornerShape(18.dp)
                                                )
                                            } else Modifier
                                        )
                                ) {
                                    MagnifyingGlassIcon(isPlus = true, tint = Color.White.copy(alpha = 0.6f))
                                }
                                val clipboardManager = androidx.compose.ui.platform.LocalClipboardManager.current
                                val copySuccessMsg = if (locale == "ru") "Текст скопирован в буфер обмена" else "Text copied to clipboard"
                                var isCopyIperfFocused by remember { mutableStateOf(false) }
                                IconButton(
                                    onClick = {
                                        val allText = iPerfLogs.joinToString("\n")
                                        clipboardManager.setText(androidx.compose.ui.text.AnnotatedString(allText))
                                        android.widget.Toast.makeText(context, copySuccessMsg, android.widget.Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .testTag("btn_copy_iperf_console_tablet")
                                        .onFocusChanged { isCopyIperfFocused = it.isFocused }
                                        .then(
                                            if (isTv) {
                                                Modifier.border(
                                                    width = 1.5.dp,
                                                    color = if (isCopyIperfFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                                                    shape = RoundedCornerShape(18.dp)
                                                )
                                            } else Modifier
                                        )
                                ) {
                                    Icon(
                                        imageVector = CopyIcon,
                                        contentDescription = "Копировать",
                                        tint = Color.White.copy(alpha = 0.6f),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                var isClearIperfFocused by remember { mutableStateOf(false) }
                                IconButton(
                                    onClick = { IperfServerManager.clearLogs() },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .testTag("btn_clear_iperf_console_tablet")
                                        .onFocusChanged { isClearIperfFocused = it.isFocused }
                                        .then(
                                            if (isTv) {
                                                Modifier.border(
                                                    width = 1.5.dp,
                                                    color = if (isClearIperfFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                                                    shape = RoundedCornerShape(18.dp)
                                                )
                                            } else Modifier
                                        )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Очистить консоль",
                                        tint = Color.White.copy(alpha = 0.6f),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                        androidx.compose.foundation.text.selection.SelectionContainer(
                            modifier = Modifier.weight(1f).fillMaxWidth()
                        ) {
                            LazyColumn(
                                state = iPerfLogsState,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(iPerfLogs) { log ->
                                    Text(
                                        text = log,
                                        color = Color(0xFF30D158),
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = iperfConsoleFontSize.sp,
                                        lineHeight = (iperfConsoleFontSize + 2f).sp,
                                        modifier = Modifier.padding(vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
                
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(16.dp)
                        .onFocusChanged { isSplitterFocused = it.isFocused }
                        .focusable()
                        .onKeyEvent { keyEvent ->
                            if (keyEvent.type == KeyEventType.KeyDown) {
                                when (keyEvent.key) {
                                    Key.DirectionLeft -> {
                                        splitterRatio = (splitterRatio - 0.05f).coerceIn(0.15f, 0.85f)
                                        true
                                    }
                                    Key.DirectionRight -> {
                                        splitterRatio = (splitterRatio + 0.05f).coerceIn(0.15f, 0.85f)
                                        true
                                    }
                                    Key.DirectionUp -> {
                                        focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Previous)
                                        true
                                    }
                                    Key.DirectionDown -> {
                                        focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Next)
                                        true
                                    }
                                    else -> false
                                }
                            } else {
                                keyEvent.key == Key.DirectionLeft || keyEvent.key == Key.DirectionRight ||
                                keyEvent.key == Key.DirectionUp || keyEvent.key == Key.DirectionDown
                            }
                        }
                        .pointerInput(parentWidth) {
                            detectDragGestures { change, dragAmount ->
                                change.consume()
                                if (parentWidth > 0) {
                                    val deltaRatio = dragAmount.x / parentWidth
                                    splitterRatio = (splitterRatio + deltaRatio).coerceIn(0.15f, 0.85f)
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(if (isSplitterFocused) 8.dp else 6.dp)
                            .fillMaxHeight(0.3f)
                            .background(
                                if (isSplitterFocused) Color(0xFFFF9500) else Color.White.copy(alpha = 0.4f),
                                RoundedCornerShape(4.dp)
                            )
                            .then(
                                if (isSplitterFocused) Modifier.border(1.5.dp, Color.White, RoundedCornerShape(4.dp))
                                else Modifier
                            )
                    )
                }
                
                Box(
                    modifier = Modifier
                        .weight(1f - splitterRatio)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF16161C))
                        .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.router_console_title),
                                style = MaterialTheme.typography.titleMedium,
                                color = Color(0xFFFF9500),
                                fontWeight = FontWeight.Bold
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                var isAddFavFocused by remember { mutableStateOf(false) }
                                IconButton(
                                    onClick = {
                                        val currentCmd = routerTextFieldValue.text.trim()
                                        if (currentCmd.isNotEmpty()) {
                                            if (!iperfFavoritesList.contains(currentCmd)) {
                                                val updated = iperfFavoritesList + currentCmd
                                                saveIperfFavorites(updated)
                                                android.widget.Toast.makeText(context, msgAdded, android.widget.Toast.LENGTH_SHORT).show()
                                            } else {
                                                android.widget.Toast.makeText(context, msgExist, android.widget.Toast.LENGTH_SHORT).show()
                                            }
                                        } else {
                                            android.widget.Toast.makeText(context, msgEmpty, android.widget.Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .testTag("iperf_add_fav_header_tablet")
                                        .onFocusChanged { isAddFavFocused = it.isFocused }
                                        .then(
                                            if (isTv) {
                                                Modifier.border(
                                                    width = 1.5.dp,
                                                    color = if (isAddFavFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                                                    shape = RoundedCornerShape(18.dp)
                                                )
                                            } else Modifier
                                        )
                                ) {
                                    Icon(
                                        imageVector = StarOutlineIcon,
                                        contentDescription = "Add to Favorites",
                                        tint = Color(0xFFFFB300),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                var isShowFavFocused by remember { mutableStateOf(false) }
                                IconButton(
                                    onClick = { showIperfFavoritesDialog = true },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .testTag("iperf_show_fav_header_tablet")
                                        .onFocusChanged { isShowFavFocused = it.isFocused }
                                        .then(
                                            if (isTv) {
                                                Modifier.border(
                                                    width = 1.5.dp,
                                                    color = if (isShowFavFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                                                    shape = RoundedCornerShape(18.dp)
                                                )
                                            } else Modifier
                                        )
                                ) {
                                    Icon(
                                        imageVector = StarFilledIcon,
                                        contentDescription = "Show Favorites",
                                        tint = Color(0xFFFFB300),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                var isDecreaseRouterFontFocused by remember { mutableStateOf(false) }
                                IconButton(
                                    onClick = {
                                        if (routerConsoleFontSize > 8f) routerConsoleFontSize -= 1f
                                    },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .testTag("btn_decrease_router_font_tablet")
                                        .onFocusChanged { isDecreaseRouterFontFocused = it.isFocused }
                                        .then(
                                            if (isTv) {
                                                Modifier.border(
                                                    width = 1.5.dp,
                                                    color = if (isDecreaseRouterFontFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                                                    shape = RoundedCornerShape(18.dp)
                                                )
                                            } else Modifier
                                        )
                                ) {
                                    MagnifyingGlassIcon(isPlus = false, tint = Color.White.copy(alpha = 0.6f))
                                }
                                var isIncreaseRouterFontFocused by remember { mutableStateOf(false) }
                                IconButton(
                                    onClick = {
                                        if (routerConsoleFontSize < 32f) routerConsoleFontSize += 1f
                                    },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .testTag("btn_increase_router_font_tablet")
                                        .onFocusChanged { isIncreaseRouterFontFocused = it.isFocused }
                                        .then(
                                            if (isTv) {
                                                Modifier.border(
                                                    width = 1.5.dp,
                                                    color = if (isIncreaseRouterFontFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                                                    shape = RoundedCornerShape(18.dp)
                                                )
                                            } else Modifier
                                        )
                                ) {
                                    MagnifyingGlassIcon(isPlus = true, tint = Color.White.copy(alpha = 0.6f))
                                }
                                var isHistoryFocused by remember { mutableStateOf(false) }
                                IconButton(
                                    onClick = {
                                        showIperfHistoryDialog = true
                                    },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .testTag("btn_arrow_up_router_console_tablet")
                                        .onFocusChanged { isHistoryFocused = it.isFocused }
                                        .then(
                                            if (isTv) {
                                                Modifier.border(
                                                    width = 1.5.dp,
                                                    color = if (isHistoryFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                                                    shape = RoundedCornerShape(18.dp)
                                                )
                                            } else Modifier
                                        )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowUp,
                                        contentDescription = "Предыдущая команда",
                                        tint = Color.White.copy(alpha = 0.6f),
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                var isClearFocused by remember { mutableStateOf(false) }
                                IconButton(
                                    onClick = { viewModel.startInteractiveShellSession() },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .testTag("btn_clear_router_console_tablet")
                                        .onFocusChanged { isClearFocused = it.isFocused }
                                        .then(
                                            if (isTv) {
                                                Modifier.border(
                                                    width = 1.5.dp,
                                                    color = if (isClearFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                                                    shape = RoundedCornerShape(18.dp)
                                                )
                                            } else Modifier
                                        )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = translateText("Очистить и перезапустить консоль", context),
                                        tint = Color.White.copy(alpha = 0.6f),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                        
                        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                            if (state.consoleHistory.isEmpty()) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = stringResource(R.string.iperf_router_console_empty),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.White.copy(alpha = 0.4f),
                                        textAlign = TextAlign.Center,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            } else {
                                androidx.compose.foundation.text.selection.SelectionContainer(
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    LazyColumn(
                                        state = routerLogsState,
                                        modifier = Modifier.fillMaxSize(),
                                        reverseLayout = true,
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        items(state.consoleHistory) { log ->
                                            Column(modifier = Modifier.fillMaxWidth()) {
                                                Text(
                                                    text = "root@openwrt:~# ${log.command}",
                                                    color = Color(0xFFFF9500),
                                                    fontFamily = FontFamily.Monospace,
                                                    fontSize = (routerConsoleFontSize + 1f).sp,
                                                    fontWeight = FontWeight.Bold,
                                                    lineHeight = (routerConsoleFontSize + 4f).sp
                                                )
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(
                                                    text = parseAnsiToAnnotatedString(log.output),
                                                    color = Color(0xFF30D158),
                                                    fontFamily = FontFamily.Monospace,
                                                    fontSize = routerConsoleFontSize.sp,
                                                    lineHeight = (routerConsoleFontSize + 3f).sp,
                                                    modifier = Modifier.padding(start = 8.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(46.dp)
                                    .background(
                                        color = Color(0xFF2A2A35),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .border(
                                        width = if (isTv && isInputFocused) 1.5.dp else 1.dp,
                                        color = if (isTv && isInputFocused) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.24f),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "#",
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Box(
                                    modifier = Modifier.weight(1f),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    if (routerTextFieldValue.text.isEmpty()) {
                                        Text(
                                            stringResource(R.string.console_hint_command),
                                            fontFamily = FontFamily.Monospace,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                    BasicTextField(
                                        value = routerTextFieldValue,
                                        onValueChange = { newValue ->
                                            routerTextFieldValue = newValue
                                            viewModel.setCommandInput(newValue.text)
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("iperf_router_input")
                                            .onFocusChanged { isInputFocused = it.isFocused },
                                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                                            fontFamily = FontFamily.Monospace,
                                            color = Color.White
                                        ),
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(
                                            imeAction = ImeAction.Send
                                        ),
                                        keyboardActions = KeyboardActions(
                                            onSend = {
                                                viewModel.sendIperfConsoleCommand()
                                                routerTextFieldValue = TextFieldValue("")
                                                focusManager.clearFocus()
                                            }
                                        ),
                                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
                                    )
                                }
                            }
                            Button(
                                onClick = {
                                    viewModel.sendIperfConsoleCommand()
                                    routerTextFieldValue = TextFieldValue("")
                                    focusManager.clearFocus()
                                },
                                modifier = Modifier
                                    .size(46.dp)
                                    .testTag("iperf_router_send")
                                    .focusRequester(iperfRouterSendFocusRequester)
                                    .onFocusChanged { isSendFocused = it.isFocused }
                                    .focusProperties {
                                        down = iperfBackFocusRequester
                                    }
                                    .then(
                                        if (isTv) {
                                            Modifier.border(
                                                width = 2.dp,
                                                color = if (isSendFocused) Color.White else Color.Transparent,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                        } else Modifier
                                    ),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                ),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Send,
                                    contentDescription = "Отправить"
                                )
                            }
                        }
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        parentHeight = coordinates.size.height.toFloat()
                    },
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(splitterRatio)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF16161C))
                        .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.iperf_console_title),
                                style = MaterialTheme.typography.titleMedium,
                                color = Color(0xFF64D2FF),
                                fontWeight = FontWeight.Bold
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                var isDecreaseIperfFontFocused by remember { mutableStateOf(false) }
                                IconButton(
                                    onClick = {
                                        if (iperfConsoleFontSize > 8f) iperfConsoleFontSize -= 1f
                                    },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .testTag("btn_decrease_iperf_font_mobile")
                                        .onFocusChanged { isDecreaseIperfFontFocused = it.isFocused }
                                        .then(
                                            if (isTv) {
                                                Modifier.border(
                                                    width = 1.5.dp,
                                                    color = if (isDecreaseIperfFontFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                                                    shape = RoundedCornerShape(18.dp)
                                                )
                                            } else Modifier
                                        )
                                ) {
                                    MagnifyingGlassIcon(isPlus = false, tint = Color.White.copy(alpha = 0.6f))
                                }
                                var isIncreaseIperfFontFocused by remember { mutableStateOf(false) }
                                IconButton(
                                    onClick = {
                                        if (iperfConsoleFontSize < 32f) iperfConsoleFontSize += 1f
                                    },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .testTag("btn_increase_iperf_font_mobile")
                                        .onFocusChanged { isIncreaseIperfFontFocused = it.isFocused }
                                        .then(
                                            if (isTv) {
                                                Modifier.border(
                                                    width = 1.5.dp,
                                                    color = if (isIncreaseIperfFontFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                                                    shape = RoundedCornerShape(18.dp)
                                                )
                                            } else Modifier
                                        )
                                ) {
                                    MagnifyingGlassIcon(isPlus = true, tint = Color.White.copy(alpha = 0.6f))
                                }
                                val clipboardManager = androidx.compose.ui.platform.LocalClipboardManager.current
                                val copySuccessMsg = if (locale == "ru") "Текст скопирован в буфер обмена" else "Text copied to clipboard"
                                var isCopyIperfFocused by remember { mutableStateOf(false) }
                                IconButton(
                                    onClick = {
                                        val allText = iPerfLogs.joinToString("\n")
                                        clipboardManager.setText(androidx.compose.ui.text.AnnotatedString(allText))
                                        android.widget.Toast.makeText(context, copySuccessMsg, android.widget.Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .testTag("btn_copy_iperf_console_mobile")
                                        .onFocusChanged { isCopyIperfFocused = it.isFocused }
                                        .then(
                                            if (isTv) {
                                                Modifier.border(
                                                    width = 1.5.dp,
                                                    color = if (isCopyIperfFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                                                    shape = RoundedCornerShape(18.dp)
                                                )
                                            } else Modifier
                                        )
                                ) {
                                    Icon(
                                        imageVector = CopyIcon,
                                        contentDescription = "Копировать",
                                        tint = Color.White.copy(alpha = 0.6f),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                var isClearIperfFocused by remember { mutableStateOf(false) }
                                IconButton(
                                    onClick = { IperfServerManager.clearLogs() },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .testTag("btn_clear_iperf_console_mobile")
                                        .onFocusChanged { isClearIperfFocused = it.isFocused }
                                        .then(
                                            if (isTv) {
                                                Modifier.border(
                                                    width = 1.5.dp,
                                                    color = if (isClearIperfFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                                                    shape = RoundedCornerShape(18.dp)
                                                )
                                            } else Modifier
                                        )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Очистить консоль",
                                        tint = Color.White.copy(alpha = 0.6f),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                        androidx.compose.foundation.text.selection.SelectionContainer(
                            modifier = Modifier.weight(1f).fillMaxWidth()
                        ) {
                            LazyColumn(
                                state = iPerfLogsState,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(iPerfLogs) { log ->
                                    Text(
                                        text = log,
                                        color = Color(0xFF30D158),
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = iperfConsoleFontSize.sp,
                                        lineHeight = (iperfConsoleFontSize + 2f).sp,
                                        modifier = Modifier.padding(vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .pointerInput(parentHeight) {
                            detectDragGestures { change, dragAmount ->
                                change.consume()
                                if (parentHeight > 0) {
                                    val deltaRatio = dragAmount.y / parentHeight
                                    splitterRatio = (splitterRatio + deltaRatio).coerceIn(0.15f, 0.85f)
                                }
                            }
                        }
                        .padding(vertical = 2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(60.dp)
                            .height(6.dp)
                            .background(Color.White.copy(alpha = 0.4f), RoundedCornerShape(3.dp))
                    )
                }
                
                Box(
                    modifier = Modifier
                        .weight(1f - splitterRatio)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF16161C))
                        .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.router_console_title),
                                style = MaterialTheme.typography.titleMedium,
                                color = Color(0xFFFF9500),
                                fontWeight = FontWeight.Bold
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                IconButton(
                                    onClick = {
                                        val currentCmd = routerTextFieldValue.text.trim()
                                        if (currentCmd.isNotEmpty()) {
                                            if (!iperfFavoritesList.contains(currentCmd)) {
                                                val updated = iperfFavoritesList + currentCmd
                                                saveIperfFavorites(updated)
                                                android.widget.Toast.makeText(context, msgAdded, android.widget.Toast.LENGTH_SHORT).show()
                                            } else {
                                                android.widget.Toast.makeText(context, msgExist, android.widget.Toast.LENGTH_SHORT).show()
                                            }
                                        } else {
                                            android.widget.Toast.makeText(context, msgEmpty, android.widget.Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    modifier = Modifier.size(36.dp).testTag("iperf_add_fav_header_mobile")
                                ) {
                                    Icon(
                                        imageVector = StarOutlineIcon,
                                        contentDescription = "Add to Favorites",
                                        tint = Color(0xFFFFB300),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                IconButton(
                                    onClick = { showIperfFavoritesDialog = true },
                                    modifier = Modifier.size(36.dp).testTag("iperf_show_fav_header_mobile")
                                ) {
                                    Icon(
                                        imageVector = StarFilledIcon,
                                        contentDescription = "Show Favorites",
                                        tint = Color(0xFFFFB300),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                var isDecreaseRouterFontFocused by remember { mutableStateOf(false) }
                                IconButton(
                                    onClick = {
                                        if (routerConsoleFontSize > 8f) routerConsoleFontSize -= 1f
                                    },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .testTag("btn_decrease_router_font_mobile")
                                        .onFocusChanged { isDecreaseRouterFontFocused = it.isFocused }
                                        .then(
                                            if (isTv) {
                                                Modifier.border(
                                                    width = 1.5.dp,
                                                    color = if (isDecreaseRouterFontFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                                                    shape = RoundedCornerShape(18.dp)
                                                )
                                            } else Modifier
                                        )
                                ) {
                                    MagnifyingGlassIcon(isPlus = false, tint = Color.White.copy(alpha = 0.6f))
                                }
                                var isIncreaseRouterFontFocused by remember { mutableStateOf(false) }
                                IconButton(
                                    onClick = {
                                        if (routerConsoleFontSize < 32f) routerConsoleFontSize += 1f
                                    },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .testTag("btn_increase_router_font_mobile")
                                        .onFocusChanged { isIncreaseRouterFontFocused = it.isFocused }
                                        .then(
                                            if (isTv) {
                                                Modifier.border(
                                                    width = 1.5.dp,
                                                    color = if (isIncreaseRouterFontFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                                                    shape = RoundedCornerShape(18.dp)
                                                )
                                            } else Modifier
                                        )
                                ) {
                                    MagnifyingGlassIcon(isPlus = true, tint = Color.White.copy(alpha = 0.6f))
                                }
                                IconButton(
                                    onClick = {
                                        showIperfHistoryDialog = true
                                    },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .testTag("btn_arrow_up_router_console_mobile")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowUp,
                                        contentDescription = "Предыдущая команда",
                                        tint = Color.White.copy(alpha = 0.6f),
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                IconButton(
                                    onClick = { viewModel.startInteractiveShellSession() },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .testTag("btn_clear_router_console_mobile")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = translateText("Очистить и перезапустить консоль", context),
                                        tint = Color.White.copy(alpha = 0.6f),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                        
                        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                            if (state.consoleHistory.isEmpty()) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = stringResource(R.string.iperf_router_console_empty),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.White.copy(alpha = 0.4f),
                                        textAlign = TextAlign.Center,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            } else {
                                androidx.compose.foundation.text.selection.SelectionContainer(
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    LazyColumn(
                                        state = routerLogsState,
                                        modifier = Modifier.fillMaxSize(),
                                        reverseLayout = true,
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        items(state.consoleHistory) { log ->
                                            Column(modifier = Modifier.fillMaxWidth()) {
                                                Text(
                                                    text = "root@openwrt:~# ${log.command}",
                                                    color = Color(0xFFFF9500),
                                                    fontFamily = FontFamily.Monospace,
                                                    fontSize = (routerConsoleFontSize + 1f).sp,
                                                    fontWeight = FontWeight.Bold,
                                                    lineHeight = (routerConsoleFontSize + 4f).sp
                                                )
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(
                                                    text = parseAnsiToAnnotatedString(log.output),
                                                    color = Color(0xFF30D158),
                                                    fontFamily = FontFamily.Monospace,
                                                    fontSize = routerConsoleFontSize.sp,
                                                    lineHeight = (routerConsoleFontSize + 3f).sp,
                                                    modifier = Modifier.padding(start = 8.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(46.dp)
                                    .background(
                                        color = Color(0xFF2A2A35),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = Color.White.copy(alpha = 0.24f),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "#",
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Box(
                                    modifier = Modifier.weight(1f),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    if (routerTextFieldValue.text.isEmpty()) {
                                        Text(
                                            stringResource(R.string.console_hint_command),
                                            fontFamily = FontFamily.Monospace,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                    BasicTextField(
                                        value = routerTextFieldValue,
                                        onValueChange = { newValue ->
                                            routerTextFieldValue = newValue
                                            viewModel.setCommandInput(newValue.text)
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("iperf_router_input"),
                                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                                            fontFamily = FontFamily.Monospace,
                                            color = Color.White
                                        ),
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(
                                            imeAction = ImeAction.Send
                                        ),
                                        keyboardActions = KeyboardActions(
                                            onSend = {
                                                viewModel.sendIperfConsoleCommand()
                                                routerTextFieldValue = TextFieldValue("")
                                                focusManager.clearFocus()
                                            }
                                        ),
                                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
                                    )
                                }
                            }
                            Button(
                                onClick = {
                                    viewModel.sendIperfConsoleCommand()
                                    routerTextFieldValue = TextFieldValue("")
                                    focusManager.clearFocus()
                                },
                                modifier = Modifier
                                    .size(46.dp)
                                    .testTag("iperf_router_send"),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                ),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Send,
                                    contentDescription = "Отправить"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
        
        if (showInteractiveSplitterTip && parentWidth > 0 && parentHeight > 0) {
            val focusRequesterSplitterTip = remember { FocusRequester() }
            var isDismissButtonFocused by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) {
                kotlinx.coroutines.delay(100)
                try {
                    if (isTv) {
                        focusRequesterSplitterTip.requestFocus()
                    }
                } catch (_: Exception) {}
            }
            val density = androidx.compose.ui.platform.LocalDensity.current
            val parentWidthDp = with(density) { parentWidth.toDp() }
            val parentHeightDp = with(density) { parentHeight.toDp() }

            var tooltipWidthPx by remember { mutableStateOf(0f) }
            var tooltipHeightPx by remember { mutableStateOf(0f) }
            val tooltipWidthDp = with(density) { tooltipWidthPx.toDp() }
            val tooltipHeightDp = with(density) { tooltipHeightPx.toDp() }

            val infiniteTransition = rememberInfiniteTransition(label = "arrow_animation")
            val arrowOffset by infiniteTransition.animateFloat(
                initialValue = -6f,
                targetValue = 6f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 900, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "arrow_bounce"
            )
            val arrowAlpha by infiniteTransition.animateFloat(
                initialValue = 0.5f,
                targetValue = 0.85f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 900, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "arrow_pulse"
            )
            val buttonAlpha by infiniteTransition.animateFloat(
                initialValue = 0.2f,
                targetValue = 1.0f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 1000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "button_pulse"
            )

            // Splitter center divider line as target
            val targetX = if (isTvOrTablet) {
                parentWidthDp * splitterRatio
            } else {
                parentWidthDp * 0.5f
            }
            val targetY = if (isTvOrTablet) {
                parentHeightDp * 0.5f
            } else {
                // Adjust for upper header row (44.dp) and centerline of the 20.dp divider
                44.dp + (parentHeightDp - 20.dp) * splitterRatio + 10.dp
            }

            val tooltipX = (targetX - tooltipWidthDp / 2).coerceIn(10.dp, parentWidthDp - tooltipWidthDp - 10.dp)
            val maxTooltipY = if (isTvOrTablet) {
                parentHeightDp - tooltipHeightDp - 10.dp
            } else {
                parentHeightDp + 44.dp - tooltipHeightDp - 10.dp
            }
            val tooltipY = if (isTvOrTablet) {
                (targetY - tooltipHeightDp + 28.dp).coerceIn(10.dp, maxTooltipY)
            } else {
                (targetY - tooltipHeightDp - 20.dp).coerceIn(10.dp, maxTooltipY)
            }

            Column(
                modifier = Modifier
                    .offset(x = tooltipX, y = tooltipY)
                    .width(260.dp)
                    .onGloballyPositioned { coords ->
                        tooltipWidthPx = coords.size.width.toFloat()
                        tooltipHeightPx = coords.size.height.toFloat()
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                androidx.compose.material3.Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color(0xFF1E1E24)),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFFF9500)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Tip",
                                tint = Color(0xFFFF9500),
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = IperfLocalizations.getSplitterTipTitle(locale),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                                // No hardcoded fallback
                            )
                        }
                        Text(
                            text = if (isTvOrTablet) {
                                IperfLocalizations.getSplitterTipBodyTv(locale)
                            } else {
                                IperfLocalizations.getSplitterTipBody(locale)
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.85f),
                            lineHeight = 16.sp
                        )
                        Button(
                            onClick = {
                                showInteractiveSplitterTip = false
                                sharedPrefs.edit().putBoolean("show_splitter_tip_v1", false).apply()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9500)),
                            shape = RoundedCornerShape(6.dp),
                            border = if (isDismissButtonFocused) androidx.compose.foundation.BorderStroke(2.dp, Color.White) else null,
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier
                                .align(Alignment.End)
                                .height(32.dp)
                                .alpha(buttonAlpha)
                                .onFocusChanged { isDismissButtonFocused = it.isFocused }
                                .focusRequester(focusRequesterSplitterTip)
                        ) {
                            Text(
                                text = IperfLocalizations.getSplitterTipDismiss(locale),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                if (isTvOrTablet) {
                    androidx.compose.foundation.Canvas(
                        modifier = Modifier
                            .size(width = 24.dp, height = 12.dp)
                            .offset(
                                x = arrowOffset.dp,
                                y = 0.dp
                            )
                    ) {
                        val path = androidx.compose.ui.graphics.Path().apply {
                            val hw = size.height / 2f
                            moveTo(0f, size.height / 2f)
                            lineTo(hw, 0f)
                            lineTo(hw, size.height * 0.35f)
                            lineTo(size.width - hw, size.height * 0.35f)
                            lineTo(size.width - hw, 0f)
                            lineTo(size.width, size.height / 2f)
                            lineTo(size.width - hw, size.height)
                            lineTo(size.width - hw, size.height * 0.65f)
                            lineTo(hw, size.height * 0.65f)
                            lineTo(hw, size.height)
                            close()
                        }
                        drawPath(
                            path = path,
                            color = Color(0xFFFF9500).copy(alpha = arrowAlpha)
                        )
                    }
                }
            }

            if (!isTvOrTablet) {
                androidx.compose.foundation.Canvas(
                    modifier = Modifier
                        .size(width = 12.dp, height = 24.dp)
                        .offset(
                            x = targetX - 6.dp,
                            y = targetY - 12.dp + arrowOffset.dp
                        )
                ) {
                    val path = androidx.compose.ui.graphics.Path().apply {
                        val hw = size.width / 2f
                        moveTo(size.width / 2f, 0f)
                        lineTo(0f, hw)
                        lineTo(size.width * 0.35f, hw)
                        lineTo(size.width * 0.35f, size.height - hw)
                        lineTo(0f, size.height - hw)
                        lineTo(size.width / 2f, size.height)
                        lineTo(size.width, size.height - hw)
                        lineTo(size.width * 0.65f, size.height - hw)
                        lineTo(size.width * 0.65f, hw)
                        lineTo(size.width, hw)
                        close()
                    }
                    drawPath(
                        path = path,
                        color = Color(0xFFFF9500).copy(alpha = arrowAlpha)
                    )
                }
            }
        }
    }

    val iperfFavoritesFocusRequester = remember { FocusRequester() }

    LaunchedEffect(showIperfFavoritesDialog) {
        if (showIperfFavoritesDialog && iperfFavoritesList.isNotEmpty()) {
            try {
                kotlinx.coroutines.delay(100)
                iperfFavoritesFocusRequester.requestFocus()
            } catch (e: Exception) {}
        }
    }

    if (showIperfFavoritesDialog) {
        androidx.compose.ui.window.Dialog(
            onDismissRequest = { showIperfFavoritesDialog = false },
            properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .border(width = 1.dp, color = Color.White.copy(alpha = 0.12f), shape = RoundedCornerShape(12.dp)),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF1E1E24)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = IperfLocalizations.getFavoritesTitle(locale),
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = { showIperfFavoritesDialog = false },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.White.copy(alpha = 0.6f)
                            )
                        }
                    }

                    if (iperfFavoritesList.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = IperfLocalizations.getFavoritesEmpty(locale),
                                color = Color.White.copy(alpha = 0.4f),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    } else {
                        val favoritesListState = rememberLazyListState()
                        LazyColumn(
                            state = favoritesListState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 280.dp)
                                .drawScrollbar(favoritesListState),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            itemsIndexed(iperfFavoritesList) { index, favCmd ->
                                var isTextFocused by remember { mutableStateOf(false) }
                                var isDeleteFocused by remember { mutableStateOf(false) }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .focusRequester(if (index == 0) iperfFavoritesFocusRequester else FocusRequester())
                                            .onFocusChanged { isTextFocused = it.isFocused }
                                            .background(
                                                if (isTextFocused) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                                                else Color(0xFF2D2D36)
                                            )
                                            .clickable {
                                                routerTextFieldValue = TextFieldValue(favCmd, selection = TextRange(favCmd.length))
                                                viewModel.setCommandInput(favCmd)
                                                showIperfFavoritesDialog = false
                                                scope.launch {
                                                    delay(150)
                                                    try {
                                                        iperfRouterSendFocusRequester.requestFocus()
                                                    } catch (e: Exception) {}
                                                }
                                            }
                                            .onKeyEvent { keyEvent ->
                                                if (keyEvent.type == androidx.compose.ui.input.key.KeyEventType.KeyDown) {
                                                    val keyCode = keyEvent.nativeKeyEvent.keyCode
                                                    if (keyCode == android.view.KeyEvent.KEYCODE_DPAD_CENTER || keyCode == android.view.KeyEvent.KEYCODE_ENTER) {
                                                        routerTextFieldValue = TextFieldValue(favCmd, selection = TextRange(favCmd.length))
                                                        viewModel.setCommandInput(favCmd)
                                                        showIperfFavoritesDialog = false
                                                        scope.launch {
                                                            delay(150)
                                                            try {
                                                                iperfRouterSendFocusRequester.requestFocus()
                                                            } catch (e: Exception) {}
                                                        }
                                                        true
                                                    } else {
                                                        false
                                                    }
                                                } else {
                                                    false
                                                }
                                            }
                                            .padding(horizontal = 12.dp, vertical = 10.dp)
                                    ) {
                                        Text(
                                            text = favCmd,
                                            fontFamily = FontFamily.Monospace,
                                            color = Color.White,
                                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                    IconButton(
                                        onClick = {
                                            val updated = iperfFavoritesList.filter { it != favCmd }
                                            saveIperfFavorites(updated)
                                        },
                                        modifier = Modifier
                                            .size(36.dp)
                                            .onFocusChanged { isDeleteFocused = it.isFocused }
                                            .then(
                                                if (isTv) {
                                                    Modifier.border(
                                                        width = 1.5.dp,
                                                        color = if (isDeleteFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                                                        shape = RoundedCornerShape(18.dp)
                                                    )
                                                } else Modifier
                                            )
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    val iperfHistoryFocusRequester = remember { FocusRequester() }

    LaunchedEffect(showIperfHistoryDialog) {
        if (showIperfHistoryDialog && sessionCommandHistory.isNotEmpty()) {
            try {
                kotlinx.coroutines.delay(100)
                iperfHistoryFocusRequester.requestFocus()
            } catch (e: Exception) {}
        }
    }

    if (showIperfHistoryDialog) {
        androidx.compose.ui.window.Dialog(
            onDismissRequest = { showIperfHistoryDialog = false },
            properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .border(width = 1.dp, color = Color.White.copy(alpha = 0.12f), shape = RoundedCornerShape(12.dp)),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF1E1E24)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = IperfLocalizations.getHistoryTitle(locale),
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = { showIperfHistoryDialog = false },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.White.copy(alpha = 0.6f)
                            )
                        }
                    }

                    if (sessionCommandHistory.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = IperfLocalizations.getHistoryEmpty(locale),
                                color = Color.White.copy(alpha = 0.4f),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    } else {
                        val historyListState = rememberLazyListState()
                        LazyColumn(
                            state = historyListState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 280.dp)
                                .drawScrollbar(historyListState),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            itemsIndexed(sessionCommandHistory) { index, itemCmd ->
                                var isTextFocused by remember { mutableStateOf(false) }
                                var isDeleteFocused by remember { mutableStateOf(false) }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .focusRequester(if (index == 0) iperfHistoryFocusRequester else FocusRequester())
                                            .onFocusChanged { isTextFocused = it.isFocused }
                                            .background(
                                                if (isTextFocused) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                                                else Color(0xFF2D2D36)
                                            )
                                            .clickable {
                                                routerTextFieldValue = TextFieldValue(itemCmd, selection = TextRange(itemCmd.length))
                                                viewModel.setCommandInput(itemCmd)
                                                showIperfHistoryDialog = false
                                                scope.launch {
                                                    delay(150)
                                                    try {
                                                        iperfRouterSendFocusRequester.requestFocus()
                                                    } catch (e: Exception) {}
                                                }
                                            }
                                            .onKeyEvent { keyEvent ->
                                                if (keyEvent.type == androidx.compose.ui.input.key.KeyEventType.KeyDown) {
                                                    val keyCode = keyEvent.nativeKeyEvent.keyCode
                                                    if (keyCode == android.view.KeyEvent.KEYCODE_DPAD_CENTER || keyCode == android.view.KeyEvent.KEYCODE_ENTER) {
                                                        routerTextFieldValue = TextFieldValue(itemCmd, selection = TextRange(itemCmd.length))
                                                        viewModel.setCommandInput(itemCmd)
                                                        showIperfHistoryDialog = false
                                                        scope.launch {
                                                            delay(150)
                                                            try {
                                                                iperfRouterSendFocusRequester.requestFocus()
                                                            } catch (e: Exception) {}
                                                        }
                                                        true
                                                    } else {
                                                        false
                                                    }
                                                } else {
                                                    false
                                                }
                                            }
                                            .padding(horizontal = 12.dp, vertical = 10.dp)
                                    ) {
                                        Text(
                                            text = itemCmd,
                                            fontFamily = FontFamily.Monospace,
                                            color = Color.White,
                                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp)
                                        )
                                    }
                                    IconButton(
                                        onClick = {
                                            viewModel.deleteFromIperfHistory(itemCmd)
                                        },
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .onFocusChanged { isDeleteFocused = it.isFocused }
                                            .background(
                                                if (isDeleteFocused) Color.Red.copy(alpha = 0.2f)
                                                else Color.Transparent
                                            )
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = if (isDeleteFocused) Color.Red else Color.Red.copy(alpha = 0.8f),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TestTab(
    state: com.example.ui.UiState,
    isFullScreen: Boolean,
    onFullScreenChange: (Boolean) -> Unit,
    isIPerfFullScreen: Boolean,
    onIPerfFullScreenChange: (Boolean) -> Unit,
    isWifiAnalyzerFullScreen: Boolean = false,
    onWifiAnalyzerFullScreenChange: (Boolean) -> Unit = {},
    viewModel: com.example.ui.RouterViewModel,
    speedTestUrl: String = "https://speed.measurementlab.net/",
    onSpeedTestUrlChange: (String) -> Unit = {},
    bottomNavFocusRequester: androidx.compose.ui.focus.FocusRequester? = null,
    lastCardFocusRequester: androidx.compose.ui.focus.FocusRequester? = null
) {
    var isLoading by remember { androidx.compose.runtime.mutableStateOf(true) }
    var webViewRef by remember { androidx.compose.runtime.mutableStateOf<android.webkit.WebView?>(null) }
    var showDevices by remember { mutableStateOf(false) }
    
    var activeHelpDialog by remember { androidx.compose.runtime.mutableStateOf<String?>(null) }
    val context = androidx.compose.ui.platform.LocalContext.current
    val isTv = remember {
        context.packageManager.hasSystemFeature(android.content.pm.PackageManager.FEATURE_LEANBACK)
    }

    if (isWifiAnalyzerFullScreen) {
        WifiAnalyzerFullScreen(
            onClose = { onWifiAnalyzerFullScreenChange(false) },
            viewModel = viewModel
        )
    } else if (isIPerfFullScreen) {
        IPerfFullScreen(
            state = state,
            onClose = { onIPerfFullScreenChange(false) },
            viewModel = viewModel
        )
    } else if (isFullScreen) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .testTag("test_tab_fullscreen_webview"),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.ui.viewinterop.AndroidView(
                factory = { context ->
                    android.webkit.WebView(context).apply {
                        layoutParams = android.view.ViewGroup.LayoutParams(
                            android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                            android.view.ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        isFocusable = true
                        isFocusableInTouchMode = true
                        requestFocus()
                        
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.useWideViewPort = true
                        settings.loadWithOverviewMode = true
                        settings.mediaPlaybackRequiresUserGesture = false
                        
                        webViewClient = object : android.webkit.WebViewClient() {
                            override fun onPageStarted(
                                view: android.webkit.WebView?,
                                url: String?,
                                favicon: android.graphics.Bitmap?
                            ) {
                                isLoading = true
                            }
                            override fun onPageFinished(view: android.webkit.WebView?, url: String?) {
                                isLoading = false
                                if (url?.contains("measurementlab") == true) {
                                val wrongAgreeText = context.getString(R.string.speedtest_policy_wrong).replace("\"", "\\\"").replace("'", "\\'")
                                val correctAgreeText = context.getString(R.string.speedtest_policy_correct).replace("\"", "\\\"").replace("'", "\\'")
                                val downloadLabel = context.getString(R.string.speedtest_download_corrected).replace("\"", "\\\"").replace("'", "\\'")
                                val uploadLabel = context.getString(R.string.speedtest_upload_corrected).replace("\"", "\\\"").replace("'", "\\'")

                                val jsString = """
                                    (function() {
                                        var wrongAgreeText = "$wrongAgreeText";
                                        var correctAgreeText = "$correctAgreeText";
                                        var downloadLabel = "$downloadLabel";
                                        var uploadLabel = "$uploadLabel";

                                        function translateNode(node) {
                                            if (!node) return;
                                            
                                            if (node.shadowRoot) {
                                                translateNode(node.shadowRoot);
                                            }
                                            
                                            if (node.nodeType === Node.TEXT_NODE) {
                                                var val = node.nodeValue;
                                                if (val) {
                                                    var needsUpdate = false;
                                                    var newVal = val;
                                                    
                                                    // Handle Russian policy typo
                                                    if (newVal.indexOf("сполитикой") !== -1) {
                                                        newVal = newVal.replace("сполитикой", "с политикой");
                                                        needsUpdate = true;
                                                    }
                                                    // Handle Ukrainian policy typo
                                                    if (newVal.indexOf("зполітикою") !== -1) {
                                                        newVal = newVal.replace("зполітикою", "з політикою");
                                                        needsUpdate = true;
                                                    }
                                                    // Handle Belarusian policy typo
                                                    if (newVal.indexOf("спалітыкай") !== -1) {
                                                        newVal = newVal.replace("спалітыкай", "з палітыкай");
                                                        needsUpdate = true;
                                                    }
                                                    // General string replace based on app translation strings
                                                    if (wrongAgreeText && newVal.indexOf(wrongAgreeText) !== -1) {
                                                        newVal = newVal.replace(wrongAgreeText, correctAgreeText);
                                                        needsUpdate = true;
                                                    }

                                                    if (needsUpdate) {
                                                        node.nodeValue = newVal;
                                                        val = newVal;
                                                    }

                                                    var trimVal = val.trim();
                                                    var trimValLower = trimVal.toLowerCase();
                                                    if (trimValLower === "загрузить" || 
                                                        trimValLower === "завантажити" || 
                                                        trimValLower === "загрузіць" || 
                                                        trimValLower === "жүктеу") {
                                                        var type = getLabelMetricType(node);
                                                        if (type === "download") {
                                                            node.nodeValue = val.replace(trimVal, downloadLabel);
                                                        } else if (type === "upload") {
                                                            node.nodeValue = val.replace(trimVal, uploadLabel);
                                                        }
                                                    }
                                                }
                                            }

                                            // Android TV Focus & D-pad navigation alignment for local policy elements and inputs
                                            if (node.nodeType === Node.ELEMENT_NODE) {
                                                var tag = node.tagName.toLowerCase();
                                                var text = (node.textContent || "").toLowerCase();
                                                var isPolicyElement = false;
                                                
                                                if (tag === "label" || tag === "input" || tag === "span" || tag === "div") {
                                                    if (text.indexOf("политикой") !== -1 || 
                                                        text.indexOf("соглашаюсь") !== -1 || 
                                                        text.indexOf("згоду") !== -1 || 
                                                        text.indexOf("політикою") !== -1 ||
                                                        text.indexOf("палітыкай") !== -1 || 
                                                        text.indexOf("келісемін") !== -1 || 
                                                        text.indexOf("agree") !== -1 || 
                                                        text.indexOf("policy") !== -1 || 
                                                        text.indexOf("consent") !== -1) {
                                                        
                                                        isPolicyElement = true;
                                                    }
                                                }
                                                
                                                if (tag === "input" && node.type === "checkbox") {
                                                    isPolicyElement = true;
                                                }
                                                
                                                if (isPolicyElement) {
                                                    if (!node.hasAttribute("tabindex") || node.getAttribute("tabindex") !== "0") {
                                                        node.setAttribute("tabindex", "0");
                                                    }
                                                    
                                                    if (!node._focusSetup) {
                                                        node._focusSetup = true;
                                                        node.style.cursor = "pointer";
                                                        
                                                        node.addEventListener("focus", function() {
                                                            this.style.outline = "3px solid #2196F3";
                                                            this.style.outlineOffset = "2px";
                                                            this.style.borderRadius = "4px";
                                                            this.style.backgroundColor = "rgba(33, 150, 243, 0.15)";
                                                        });
                                                        
                                                        node.addEventListener("blur", function() {
                                                            this.style.outline = "";
                                                            this.style.outlineOffset = "";
                                                            this.style.borderRadius = "";
                                                            this.style.backgroundColor = "";
                                                        });
                                                        
                                                        node.addEventListener("keydown", function(e) {
                                                            if (e.key === "Enter" || e.keyCode === 13 || e.key === " " || e.keyCode === 32) {
                                                                e.preventDefault();
                                                                
                                                                var cb = this.querySelector('input[type="checkbox"]');
                                                                if (!cb && tag === "input" && this.type === "checkbox") {
                                                                    cb = this;
                                                                }
                                                                if (!cb) {
                                                                    var parent = this.parentElement || this.parentNode;
                                                                    if (parent) {
                                                                        cb = parent.querySelector('input[type="checkbox"]');
                                                                    }
                                                                }
                                                                if (cb) {
                                                                    cb.click();
                                                                } else {
                                                                    this.click();
                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            }

                                            if (node.childNodes && node.childNodes.length > 0) {
                                                for (var i = 0; i < node.childNodes.length; i++) {
                                                    translateNode(node.childNodes[i]);
                                                }
                                            }
                                        }

                                        function getLabelMetricType(node) {
                                            var parent = node.parentElement;
                                            var depth = 0;
                                            while (parent && depth < 10) {
                                                var attrs = "";
                                                if (parent.attributes) {
                                                    for (var i = 0; i < parent.attributes.length; i++) {
                                                        var attr = parent.attributes[i];
                                                        attrs += " " + attr.name + "=" + attr.value;
                                                    }
                                                }
                                                var textSource = ((parent.id || "") + " " + (parent.className || "") + " " + attrs).toLowerCase();
                                                if (textSource.indexOf("download") !== -1 || textSource.indexOf("down") !== -1) {
                                                    return "download";
                                                }
                                                if (textSource.indexOf("upload") !== -1 || textSource.indexOf("up") !== -1) {
                                                    return "upload";
                                                }
                                                parent = parent.parentElement;
                                                depth++;
                                            }

                                            var allZagr = [];
                                            function collectZagrElements(root) {
                                                if (!root) return;
                                                if (root.shadowRoot) collectZagrElements(root.shadowRoot);
                                                if (root.nodeType === Node.TEXT_NODE && root.nodeValue) {
                                                    var itemTrim = root.nodeValue.trim().toLowerCase();
                                                    if (itemTrim === "загрузить" || 
                                                        itemTrim === "завантажити" || 
                                                        itemTrim === "загрузіць" || 
                                                        itemTrim === "жүктеу") {
                                                        allZagr.push(root);
                                                    }
                                                }
                                                if (root.childNodes) {
                                                    for (var i = 0; i < root.childNodes.length; i++) {
                                                        collectZagrElements(root.childNodes[i]);
                                                    }
                                                 }
                                            }
                                            collectZagrElements(document.body);
                                            
                                            var idx = allZagr.indexOf(node);
                                            if (idx !== -1) {
                                                if (idx % 2 === 0) {
                                                    return "download";
                                                } else {
                                                    return "upload";
                                                }
                                            }
                                            return null;
                                        }

                                        if (!window.speedtestTranslatorInterval) {
                                            window.speedtestTranslatorInterval = setInterval(function() {
                                                translateNode(document.body);
                                            }, 200);
                                        }
                                    })();
                                """.trimIndent()
                                view?.evaluateJavascript(jsString, null)
                                }
                            }
                        }
                        loadUrl(speedTestUrl)
                        webViewRef = this
                    }
                },
                update = { webView ->
                    if (webView.url != speedTestUrl) {
                        webView.loadUrl(speedTestUrl)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            if (isLoading) {
                androidx.compose.material3.CircularProgressIndicator(
                    modifier = Modifier.size(64.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    } else {
        val firstCardFocusRequester = remember { FocusRequester() }
        val mlabHelpFocusRequester = remember { FocusRequester() }
        
        val cloudflareCardFocusRequester = remember { FocusRequester() }
        val cloudflareHelpFocusRequester = remember { FocusRequester() }
        
        val fastCardFocusRequester = remember { FocusRequester() }
        val fastHelpFocusRequester = remember { FocusRequester() }
        
        val waveformCardFocusRequester = remember { FocusRequester() }
        val waveformHelpFocusRequester = remember { FocusRequester() }
        
        val speedofmeCardFocusRequester = remember { FocusRequester() }
        val speedofmeHelpFocusRequester = remember { FocusRequester() }
        
        val wifiCardFocusRequester = remember { FocusRequester() }
        val wifiHelpFocusRequester = remember { FocusRequester() }
        
        val iperfCardFocusRequester = remember { FocusRequester() }
        val iperfHelpFocusRequester = remember { FocusRequester() }
        val devicesCardFocusRequester = remember { FocusRequester() }
        val devicesHelpFocusRequester = remember { FocusRequester() }
        
        var isMlabFocused by remember { mutableStateOf(false) }
        var isCloudflareFocused by remember { mutableStateOf(false) }
        var isFastFocused by remember { mutableStateOf(false) }
        var isWaveformFocused by remember { mutableStateOf(false) }
        var isSpeedofmeFocused by remember { mutableStateOf(false) }
        var isWifiFocused by remember { mutableStateOf(false) }
        var isIperfFocused by remember { mutableStateOf(false) }
        var isDevicesFocused by remember { mutableStateOf(false) }

        var isMlabHelpFocused by remember { mutableStateOf(false) }
        var isCloudflareHelpFocused by remember { mutableStateOf(false) }
        var isFastHelpFocused by remember { mutableStateOf(false) }
        var isWaveformHelpFocused by remember { mutableStateOf(false) }
        var isSpeedofmeHelpFocused by remember { mutableStateOf(false) }
        var isWifiHelpFocused by remember { mutableStateOf(false) }
        var isIperfHelpFocused by remember { mutableStateOf(false) }
        var isDevicesHelpFocused by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            if (isTv) {
                try {
                    devicesCardFocusRequester.requestFocus()
                } catch (e: Exception) {
                    // Ignore transient requestFocus errors on initial compose
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 10.8.dp, bottom = 16.dp)
                .verticalScroll(androidx.compose.foundation.rememberScrollState())
                .testTag("test_tab_view")
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(devicesCardFocusRequester)
                    .onFocusChanged { isDevicesFocused = it.isFocused }
                    .then(
                        if (isTv) {
                            Modifier
                                .onKeyEvent { keyEvent ->
                                    if (keyEvent.type == KeyEventType.KeyDown) {
                                        when (keyEvent.key) {
                                            Key.DirectionRight -> {
                                                devicesHelpFocusRequester.requestFocus()
                                                true
                                            }
                                            Key.DirectionDown -> {
                                                wifiCardFocusRequester.requestFocus()
                                                true
                                            }
                                            else -> false
                                        }
                                    } else false
                                }
                                .border(
                                    width = 2.dp,
                                    color = if (isDevicesFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                        } else Modifier
                    )
                    .clickable { showDevices = true }
                    .padding(bottom = 10.8.dp)
                    .testTag("devices_start_card"),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 10.8.dp, bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.List,
                            contentDescription = stringResource(R.string.devices_title),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Column {
                            Text(
                                text = stringResource(R.string.devices_title),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = TestTabLocalizations.getDevicesDesc(context.resources.configuration.locales[0].language),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    
                    IconButton(
                        onClick = { activeHelpDialog = "devices" },
                        modifier = Modifier
                            .testTag("btn_help_devices")
                            .focusRequester(devicesHelpFocusRequester)
                            .onFocusChanged { isDevicesHelpFocused = it.isFocused }
                            .then(
                                if (isTv) {
                                    Modifier
                                        .onKeyEvent { keyEvent ->
                                            if (keyEvent.type == KeyEventType.KeyDown) {
                                                when (keyEvent.key) {
                                                    Key.DirectionLeft -> {
                                                        devicesCardFocusRequester.requestFocus()
                                                        true
                                                    }
                                                    Key.DirectionDown -> {
                                                        wifiHelpFocusRequester.requestFocus()
                                                        true
                                                    }
                                                    else -> false
                                                }
                                            } else false
                                        }
                                        .border(
                                            width = 1.5.dp,
                                            color = if (isDevicesHelpFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                                            shape = RoundedCornerShape(24.dp)
                                        )
                                } else Modifier
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = TestTabLocalizations.getHelpButtonDesc(context.resources.configuration.locales[0].language),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            val locale = context.resources.configuration.locales[0].language

            // Wi-Fi Analyzer Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(wifiCardFocusRequester)
                    .onFocusChanged { isWifiFocused = it.isFocused }
                    .then(
                        if (isTv) {
                            Modifier
                                .onKeyEvent { keyEvent ->
                                    if (keyEvent.type == KeyEventType.KeyDown) {
                                        when (keyEvent.key) {
                                            Key.DirectionRight -> {
                                                wifiHelpFocusRequester.requestFocus()
                                                true
                                            }
                                            Key.DirectionUp -> {
                                                devicesCardFocusRequester.requestFocus()
                                                true
                                            }
                                            Key.DirectionDown -> {
                                                iperfCardFocusRequester.requestFocus()
                                                true
                                            }
                                            else -> false
                                        }
                                    } else false
                                }
                                .border(
                                    width = 2.dp,
                                    color = if (isWifiFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                        } else Modifier
                    )
                    .clickable { onWifiAnalyzerFullScreenChange(true) }
                    .padding(bottom = 10.8.dp)
                    .testTag("wifi_analyzer_card"),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 10.8.dp, bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = TestTabLocalizations.getWifiAnalyzerTitle(locale),
                            tint = Color(0xFFFF3B30),
                            modifier = Modifier.size(24.dp)
                        )
                        Column {
                            Text(
                                text = TestTabLocalizations.getWifiAnalyzerTitle(locale),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = TestTabLocalizations.getWifiAnalyzerDesc(locale),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    
                    IconButton(
                        onClick = { activeHelpDialog = "wifi_analyzer" },
                        modifier = Modifier
                            .testTag("btn_help_wifi_analyzer")
                            .focusRequester(wifiHelpFocusRequester)
                            .onFocusChanged { isWifiHelpFocused = it.isFocused }
                            .then(
                                if (isTv) {
                                    Modifier
                                        .onKeyEvent { keyEvent ->
                                            if (keyEvent.type == KeyEventType.KeyDown) {
                                                when (keyEvent.key) {
                                                    Key.DirectionLeft -> {
                                                        wifiCardFocusRequester.requestFocus()
                                                        true
                                                    }
                                                    Key.DirectionUp -> {
                                                        devicesHelpFocusRequester.requestFocus()
                                                        true
                                                    }
                                                    Key.DirectionDown -> {
                                                        iperfHelpFocusRequester.requestFocus()
                                                        true
                                                    }
                                                    else -> false
                                                }
                                            } else false
                                        }
                                        .border(
                                            width = 1.5.dp,
                                            color = if (isWifiHelpFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                                            shape = RoundedCornerShape(24.dp)
                                        )
                                } else Modifier
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = TestTabLocalizations.getHelpButtonDesc(context.resources.configuration.locales[0].language),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Beautiful M3 iPerf3 Card below M-Lab
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(iperfCardFocusRequester)
                    .onFocusChanged { isIperfFocused = it.isFocused }
                    .then(
                        if (isTv) {
                            Modifier
                                .onKeyEvent { keyEvent ->
                                    if (keyEvent.type == KeyEventType.KeyDown) {
                                        when (keyEvent.key) {
                                            Key.DirectionRight -> {
                                                iperfHelpFocusRequester.requestFocus()
                                                true
                                            }
                                            Key.DirectionUp -> {
                                                wifiCardFocusRequester.requestFocus()
                                                true
                                            }
                                            Key.DirectionDown -> {
                                                firstCardFocusRequester.requestFocus()
                                                true
                                            }
                                            else -> false
                                        }
                                    } else false
                                }
                                .border(
                                    width = 2.dp,
                                    color = if (isIperfFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                        } else Modifier
                    )
                    .clickable { onIPerfFullScreenChange(true) }
                    .padding(bottom = 10.8.dp)
                    .testTag("iperf_card"),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 10.8.dp, bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = stringResource(R.string.iperf_card_title),
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(24.dp)
                        )
                        Column {
                            Text(
                                text = stringResource(R.string.iperf_card_title),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = stringResource(R.string.iperf_card_desc),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    
                    IconButton(
                        onClick = { activeHelpDialog = "iperf" },
                        modifier = Modifier
                            .testTag("btn_help_iperf")
                            .focusRequester(iperfHelpFocusRequester)
                            .onFocusChanged { isIperfHelpFocused = it.isFocused }
                            .then(
                                if (isTv) {
                                    Modifier
                                        .onKeyEvent { keyEvent ->
                                            if (keyEvent.type == KeyEventType.KeyDown) {
                                                when (keyEvent.key) {
                                                    Key.DirectionLeft -> {
                                                        iperfCardFocusRequester.requestFocus()
                                                        true
                                                    }
                                                    Key.DirectionUp -> {
                                                        wifiHelpFocusRequester.requestFocus()
                                                        true
                                                    }
                                                    Key.DirectionDown -> {
                                                        mlabHelpFocusRequester.requestFocus()
                                                        true
                                                    }
                                                    else -> false
                                                }
                                            } else false
                                        }
                                        .border(
                                            width = 1.5.dp,
                                            color = if (isIperfHelpFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                                            shape = RoundedCornerShape(24.dp)
                                        )
                                } else Modifier
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = TestTabLocalizations.getHelpButtonDesc(context.resources.configuration.locales[0].language),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }


            // Beautiful M3 Header Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(firstCardFocusRequester)
                    .onFocusChanged { isMlabFocused = it.isFocused }
                    .then(
                        if (isTv) {
                            Modifier
                                .onKeyEvent { keyEvent ->
                                    if (keyEvent.type == KeyEventType.KeyDown) {
                                        when (keyEvent.key) {
                                            Key.DirectionRight -> {
                                                mlabHelpFocusRequester.requestFocus()
                                                true
                                            }
                                                                                        Key.DirectionUp -> {
                                                iperfCardFocusRequester.requestFocus()
                                                true
                                            }
                                            Key.DirectionDown -> {
                                                cloudflareCardFocusRequester.requestFocus()
                                                true
                                            }
                                            else -> false
                                        }
                                    } else false
                                }
                                .border(
                                    width = 2.dp,
                                    color = if (isMlabFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                        } else Modifier
                    )
                    .clickable { 
                        onSpeedTestUrlChange("https://speed.measurementlab.net/")
                        onFullScreenChange(true) 
                    }
                    .padding(bottom = 10.8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 10.8.dp, bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = stringResource(R.string.speed_test_title),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Column {
                            Text(
                                text = stringResource(R.string.speed_test_title),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    
                    IconButton(
                        onClick = { activeHelpDialog = "mlab" },
                        modifier = Modifier
                            .testTag("btn_help_mlab")
                            .focusRequester(mlabHelpFocusRequester)
                            .onFocusChanged { isMlabHelpFocused = it.isFocused }
                            .then(
                                if (isTv) {
                                    Modifier
                                        .onKeyEvent { keyEvent ->
                                            if (keyEvent.type == KeyEventType.KeyDown) {
                                                when (keyEvent.key) {
                                                    Key.DirectionLeft -> {
                                                        firstCardFocusRequester.requestFocus()
                                                        true
                                                    }
                                                                                                        Key.DirectionUp -> {
                                                        iperfHelpFocusRequester.requestFocus()
                                                        true
                                                    }
                                                    Key.DirectionDown -> {
                                                        cloudflareHelpFocusRequester.requestFocus()
                                                        true
                                                    }
                                                    else -> false
                                                }
                                            } else false
                                        }
                                        .border(
                                            width = 1.5.dp,
                                            color = if (isMlabHelpFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                                            shape = RoundedCornerShape(24.dp)
                                        )
                                } else Modifier
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = TestTabLocalizations.getHelpButtonDesc(context.resources.configuration.locales[0].language),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // 1. Cloudflare Speed test
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(cloudflareCardFocusRequester)
                    .onFocusChanged { isCloudflareFocused = it.isFocused }
                    .then(
                        if (isTv) {
                            Modifier
                                .onKeyEvent { keyEvent ->
                                    if (keyEvent.type == KeyEventType.KeyDown) {
                                        when (keyEvent.key) {
                                            Key.DirectionRight -> {
                                                cloudflareHelpFocusRequester.requestFocus()
                                                true
                                            }
                                            Key.DirectionUp -> {
                                                firstCardFocusRequester.requestFocus()
                                                true
                                            }
                                            Key.DirectionDown -> {
                                                fastCardFocusRequester.requestFocus()
                                                true
                                            }
                                            else -> false
                                        }
                                    } else false
                                }
                                .border(
                                    width = 2.dp,
                                    color = if (isCloudflareFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                        } else Modifier
                    )
                    .clickable {
                        onSpeedTestUrlChange("https://speed.cloudflare.com/")
                        onFullScreenChange(true)
                    }
                    .padding(bottom = 10.8.dp)
                    .testTag("cloudflare_speedtest_card"),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 10.8.dp, bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = TestTabLocalizations.getCloudflareTitle(locale),
                            tint = Color(0xFFF38020),
                            modifier = Modifier.size(24.dp)
                        )
                        Column {
                            Text(
                                text = TestTabLocalizations.getCloudflareTitle(locale),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = TestTabLocalizations.getCloudflareDesc(locale),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    
                    IconButton(
                        onClick = { activeHelpDialog = "cloudflare" },
                        modifier = Modifier
                            .testTag("btn_help_cloudflare")
                            .focusRequester(cloudflareHelpFocusRequester)
                            .onFocusChanged { isCloudflareHelpFocused = it.isFocused }
                            .then(
                                if (isTv) {
                                    Modifier
                                        .onKeyEvent { keyEvent ->
                                            if (keyEvent.type == KeyEventType.KeyDown) {
                                                when (keyEvent.key) {
                                                    Key.DirectionLeft -> {
                                                        cloudflareCardFocusRequester.requestFocus()
                                                        true
                                                    }
                                                    Key.DirectionUp -> {
                                                        mlabHelpFocusRequester.requestFocus()
                                                        true
                                                    }
                                                    Key.DirectionDown -> {
                                                        fastHelpFocusRequester.requestFocus()
                                                        true
                                                    }
                                                    else -> false
                                                }
                                            } else false
                                        }
                                        .border(
                                            width = 1.5.dp,
                                            color = if (isCloudflareHelpFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                                            shape = RoundedCornerShape(24.dp)
                                        )
                                } else Modifier
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = TestTabLocalizations.getHelpButtonDesc(context.resources.configuration.locales[0].language),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // 2. Fast.com
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(fastCardFocusRequester)
                    .onFocusChanged { isFastFocused = it.isFocused }
                    .then(
                        if (isTv) {
                            Modifier
                                .onKeyEvent { keyEvent ->
                                    if (keyEvent.type == KeyEventType.KeyDown) {
                                        when (keyEvent.key) {
                                            Key.DirectionRight -> {
                                                fastHelpFocusRequester.requestFocus()
                                                true
                                            }
                                            Key.DirectionUp -> {
                                                cloudflareCardFocusRequester.requestFocus()
                                                true
                                            }
                                            Key.DirectionDown -> {
                                                waveformCardFocusRequester.requestFocus()
                                                true
                                            }
                                            else -> false
                                        }
                                    } else false
                                }
                                .border(
                                    width = 2.dp,
                                    color = if (isFastFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                        } else Modifier
                    )
                    .clickable {
                        onSpeedTestUrlChange("https://fast.com/")
                        onFullScreenChange(true)
                    }
                    .padding(bottom = 10.8.dp)
                    .testTag("fast_speedtest_card"),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 10.8.dp, bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Fast.com Speed Test",
                            tint = Color(0xFFE50914),
                            modifier = Modifier.size(24.dp)
                        )
                        Column {
                            Text(
                                text = "Fast.com",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = TestTabLocalizations.getFastDesc(locale),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    
                    IconButton(
                        onClick = { activeHelpDialog = "fast" },
                        modifier = Modifier
                            .testTag("btn_help_fast")
                            .focusRequester(fastHelpFocusRequester)
                            .onFocusChanged { isFastHelpFocused = it.isFocused }
                            .then(
                                if (isTv) {
                                    Modifier
                                        .onKeyEvent { keyEvent ->
                                            if (keyEvent.type == KeyEventType.KeyDown) {
                                                when (keyEvent.key) {
                                                    Key.DirectionLeft -> {
                                                        fastCardFocusRequester.requestFocus()
                                                        true
                                                    }
                                                    Key.DirectionUp -> {
                                                        cloudflareHelpFocusRequester.requestFocus()
                                                        true
                                                    }
                                                    Key.DirectionDown -> {
                                                        waveformHelpFocusRequester.requestFocus()
                                                        true
                                                    }
                                                    else -> false
                                                }
                                            } else false
                                        }
                                        .border(
                                            width = 1.5.dp,
                                            color = if (isFastHelpFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                                            shape = RoundedCornerShape(24.dp)
                                        )
                                } else Modifier
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = TestTabLocalizations.getHelpButtonDesc(context.resources.configuration.locales[0].language),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // 3. Waveform Bufferbloat
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(waveformCardFocusRequester)
                    .onFocusChanged { isWaveformFocused = it.isFocused }
                    .then(
                        if (isTv) {
                            Modifier
                                .onKeyEvent { keyEvent ->
                                    if (keyEvent.type == KeyEventType.KeyDown) {
                                        when (keyEvent.key) {
                                            Key.DirectionRight -> {
                                                waveformHelpFocusRequester.requestFocus()
                                                true
                                            }
                                            Key.DirectionUp -> {
                                                fastCardFocusRequester.requestFocus()
                                                true
                                            }
                                            Key.DirectionDown -> {
                                                speedofmeCardFocusRequester.requestFocus()
                                                true
                                            }
                                            else -> false
                                        }
                                    } else false
                                }
                                .border(
                                    width = 2.dp,
                                    color = if (isWaveformFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                        } else Modifier
                    )
                    .clickable {
                        onSpeedTestUrlChange("https://www.waveform.com/tools/bufferbloat")
                        onFullScreenChange(true)
                    }
                    .padding(bottom = 10.8.dp)
                    .testTag("waveform_speedtest_card"),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 10.8.dp, bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Waveform Bufferbloat Test",
                            tint = Color(0xFFBF5AF2),
                            modifier = Modifier.size(24.dp)
                        )
                        Column {
                            Text(
                                text = "Waveform Bufferbloat",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = TestTabLocalizations.getWaveformDesc(locale),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    
                    IconButton(
                        onClick = { activeHelpDialog = "waveform" },
                        modifier = Modifier
                            .testTag("btn_help_waveform")
                            .focusRequester(waveformHelpFocusRequester)
                            .onFocusChanged { isWaveformHelpFocused = it.isFocused }
                            .then(
                                if (isTv) {
                                    Modifier
                                        .onKeyEvent { keyEvent ->
                                            if (keyEvent.type == KeyEventType.KeyDown) {
                                                when (keyEvent.key) {
                                                    Key.DirectionLeft -> {
                                                        waveformCardFocusRequester.requestFocus()
                                                        true
                                                    }
                                                    Key.DirectionUp -> {
                                                        fastHelpFocusRequester.requestFocus()
                                                        true
                                                    }
                                                    Key.DirectionDown -> {
                                                        speedofmeHelpFocusRequester.requestFocus()
                                                        true
                                                    }
                                                    else -> false
                                                }
                                            } else false
                                        }
                                        .border(
                                            width = 1.5.dp,
                                            color = if (isWaveformHelpFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                                            shape = RoundedCornerShape(24.dp)
                                        )
                                } else Modifier
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = TestTabLocalizations.getHelpButtonDesc(context.resources.configuration.locales[0].language),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // 4. Speedof.me
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(speedofmeCardFocusRequester)
                    .then(if (lastCardFocusRequester != null) Modifier.focusRequester(lastCardFocusRequester) else Modifier)
                    .onFocusChanged { isSpeedofmeFocused = it.isFocused }
                    .then(
                        if (isTv) {
                            Modifier
                                .onKeyEvent { keyEvent ->
                                    if (keyEvent.type == KeyEventType.KeyDown) {
                                        when (keyEvent.key) {
                                            Key.DirectionRight -> {
                                                speedofmeHelpFocusRequester.requestFocus()
                                                true
                                            }
                                            Key.DirectionUp -> {
                                                waveformCardFocusRequester.requestFocus()
                                                true
                                            }
                                            Key.DirectionDown -> {
                                                if (bottomNavFocusRequester != null) {
                                                    try {
                                                        bottomNavFocusRequester.requestFocus()
                                                        true
                                                    } catch (e: Exception) {
                                                        false
                                                    }
                                                } else {
                                                    wifiCardFocusRequester.requestFocus()
                                                    true
                                                }
                                            }
                                            else -> false
                                        }
                                    } else false
                                }
                                .border(
                                    width = 2.dp,
                                    color = if (isSpeedofmeFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                        } else Modifier
                    )
                    .clickable {
                        onSpeedTestUrlChange("https://speedof.me/")
                        onFullScreenChange(true)
                    }
                    .padding(bottom = 10.8.dp)
                    .testTag("speedofme_speedtest_card"),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 10.8.dp, bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Speedof.me Speed Test",
                            tint = Color(0xFF00BFFF),
                            modifier = Modifier.size(24.dp)
                        )
                        Column {
                            Text(
                                text = "Speedof.me",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = TestTabLocalizations.getSpeedofmeDesc(locale),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    
                    IconButton(
                        onClick = { activeHelpDialog = "speedofme" },
                        modifier = Modifier
                            .testTag("btn_help_speedofme")
                            .focusRequester(speedofmeHelpFocusRequester)
                            .onFocusChanged { isSpeedofmeHelpFocused = it.isFocused }
                            .then(
                                if (isTv) {
                                    Modifier
                                        .onKeyEvent { keyEvent ->
                                            if (keyEvent.type == KeyEventType.KeyDown) {
                                                when (keyEvent.key) {
                                                    Key.DirectionLeft -> {
                                                        speedofmeCardFocusRequester.requestFocus()
                                                        true
                                                    }
                                                    Key.DirectionUp -> {
                                                        waveformHelpFocusRequester.requestFocus()
                                                        true
                                                    }
                                                    Key.DirectionDown -> {
                                                        wifiHelpFocusRequester.requestFocus()
                                                        true
                                                    }
                                                    else -> false
                                                }
                                            } else false
                                        }
                                        .border(
                                            width = 1.5.dp,
                                            color = if (isSpeedofmeHelpFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                                            shape = RoundedCornerShape(24.dp)
                                        )
                                } else Modifier
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = TestTabLocalizations.getHelpButtonDesc(context.resources.configuration.locales[0].language),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }

    if (activeHelpDialog != null) {
        val localeText = context.resources.configuration.locales[0].language
        val title = TestTabLocalizations.getDialogTitle(activeHelpDialog!!, localeText)
        val body = TestTabLocalizations.getDialogBody(activeHelpDialog!!, localeText, state.config)

        androidx.compose.ui.window.Dialog(
            onDismissRequest = { activeHelpDialog = null },
            properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
        ) {
            val configuration = androidx.compose.ui.platform.LocalConfiguration.current
            val isPortrait = configuration.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT
            val scrollState = rememberScrollState()
            val primaryColor = MaterialTheme.colorScheme.primary
            val dialogFocusRequester = remember { FocusRequester() }
            val copyButtonFocusRequester = remember { FocusRequester() }
            var isDialogColumnFocused by remember { mutableStateOf(false) }
            val scope = rememberCoroutineScope()

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = if (isPortrait) 16.dp else 40.dp)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .onKeyEvent { keyEvent ->
                            if (keyEvent.type == KeyEventType.KeyDown) {
                                when (keyEvent.key) {
                                    Key.DirectionDown -> {
                                        if (scrollState.value < scrollState.maxValue) {
                                            scope.launch {
                                                scrollState.animateScrollBy(150f)
                                            }
                                            true
                                        } else {
                                            false
                                        }
                                    }
                                    Key.DirectionUp -> {
                                        if (scrollState.value > 0) {
                                            scope.launch {
                                                scrollState.animateScrollBy(-150f)
                                            }
                                            true
                                        } else {
                                            false
                                        }
                                    }
                                    else -> false
                                }
                            } else false
                        },
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    LaunchedEffect(Unit) {
                        if (isTv) {
                            // Try multiple times with small delays to ensure the dialog layout is attached and focus is requested successfully
                            for (i in 1..5) {
                                try {
                                    dialogFocusRequester.requestFocus()
                                    break
                                } catch (e: Exception) {
                                    delay(80)
                                }
                            }
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = if (isTv) 320.dp else 450.dp)
                            .then(
                                if (isTv) {
                                    Modifier
                                        .focusRequester(dialogFocusRequester)
                                        .focusable()
                                        .onFocusChanged { isDialogColumnFocused = it.isFocused
                                         }
                                         .onKeyEvent { keyEvent ->
                                             if (keyEvent.type == KeyEventType.KeyDown) {
                                                 when (keyEvent.key) {
                                                     Key.DirectionDown -> {
                                                         if (scrollState.value < scrollState.maxValue) {
                                                             scope.launch {
                                                                 scrollState.animateScrollBy(150f)
                                                             }
                                                             true
                                                         } else {
                                                             try {
                                                                 copyButtonFocusRequester.requestFocus()
                                                                 true
                                                             } catch (e: Exception) {
                                                                 false
                                                             }
                                                         }
                                                     }
                                                     Key.DirectionUp -> {
                                                         if (scrollState.value > 0) {
                                                             scope.launch {
                                                                 scrollState.animateScrollBy(-150f)
                                                             }
                                                             true
                                                         } else {
                                                             false
                                                         }
                                                     }
                                                     else -> false
                                                 }
                                             } else false }
                                        .border(
                                            width = 1.5.dp,
                                            color = if (isDialogColumnFocused) MaterialTheme.colorScheme.primary.copy(alpha = 0.5f) else Color.Transparent,
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .padding(4.dp)
                                } else Modifier
                            )
                            .drawWithContent {
                                drawContent()
                                if (scrollState.maxValue > 0) {
                                    val viewPortHeight = this.size.height
                                    val contentHeight = viewPortHeight + scrollState.maxValue
                                    val thumbHeight = (viewPortHeight * (viewPortHeight / contentHeight)).coerceAtLeast(60f)
                                    val scrollPercent = scrollState.value.toFloat() / scrollState.maxValue
                                    val maxOffset = viewPortHeight - thumbHeight
                                    val thumbOffset = maxOffset * scrollPercent
                                    
                                    val paddingEndPx = 12.dp.toPx()
                                    val badgeRadius = 10.dp.toPx()
                                    
                                    // Position scrollbar at the center of the right padding area (14dp from the right edge of the Column)
                                    val trackX = this.size.width - 14.dp.toPx()
                                    val trackWidth = 4.dp.toPx()
                                    
                                    val badgeX = trackX
                                    val badgeY = badgeRadius + (viewPortHeight - 2 * badgeRadius) * scrollPercent
                                    
                                    // Draw track
                                    drawRoundRect(
                                        color = Color.Gray.copy(alpha = 0.2f),
                                        topLeft = Offset(trackX - trackWidth / 2, 0f),
                                        size = Size(trackWidth, viewPortHeight),
                                        cornerRadius = CornerRadius(trackWidth / 2, trackWidth / 2)
                                    )
                                    
                                    // Draw thumb
                                    drawRoundRect(
                                        color = primaryColor,
                                        topLeft = Offset(trackX - trackWidth / 2, thumbOffset),
                                        size = Size(trackWidth, thumbHeight),
                                        cornerRadius = CornerRadius(trackWidth / 2, trackWidth / 2)
                                    )

                                    // Draw badge circle
                                    drawCircle(
                                        color = primaryColor,
                                        radius = badgeRadius,
                                        center = Offset(badgeX, badgeY)
                                    )

                                    // Draw text inside badge using nativeCanvas directly
                                    val nativeCanvas = drawContext.canvas.nativeCanvas
                                    val pct = (scrollPercent * 100).toInt()
                                    val textPaint = android.text.TextPaint().apply {
                                        color = android.graphics.Color.WHITE
                                        textSize = 9.dp.toPx()
                                        textAlign = android.graphics.Paint.Align.CENTER
                                        isAntiAlias = true
                                        typeface = android.graphics.Typeface.DEFAULT_BOLD
                                    }
                                    nativeCanvas.drawText(
                                        "$pct%",
                                        badgeX,
                                        badgeY + 3.dp.toPx(),
                                        textPaint
                                    )
                                }
                            }
                            .padding(end = if (scrollState.maxValue > 0) 28.dp else 0.dp)
                            .pointerInput(Unit) {
                                detectDragGestures { change, dragAmount ->
                                    change.consume()
                                    scrollState.dispatchRawDelta(-dragAmount.y)
                                }
                            }
                            .verticalScroll(scrollState),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        body.split("\n\n").forEach { paragraph ->
                            if (paragraph.startsWith("##")) {
                                Text(
                                    text = paragraph.replace("##", "").trim(),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            } else if (paragraph.startsWith("###")) {
                                Text(
                                    text = paragraph.replace("###", "").trim(),
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            } else if (paragraph.startsWith("opkg update")) {
                                val clipboardManager = androidx.compose.ui.platform.LocalClipboardManager.current
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = paragraph,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.weight(1f)
                                    )
                                    var btnFocused by remember { mutableStateOf(false) }
                                    androidx.compose.material3.IconButton(
                                        onClick = {
                                            clipboardManager.setText(androidx.compose.ui.text.AnnotatedString(paragraph))
                                            viewModel.setCommandInput(paragraph)
                                            viewModel.switchTab(com.example.ui.TabType.CONSOLE)
                                        },
                                        modifier = Modifier.focusRequester(copyButtonFocusRequester).onFocusChanged { btnFocused = it.isFocused }.focusable().background(if (btnFocused) MaterialTheme.colorScheme.primary.copy(alpha=0.2f) else Color.Transparent, shape = androidx.compose.foundation.shape.CircleShape)
                                    ) {
                                        androidx.compose.material3.Icon(
                                            imageVector = androidx.compose.material.icons.Icons.Default.ContentCopy,
                                            contentDescription = "Copy",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            } else {
                                val boldRegex = Regex("\\*\\*(.*?)\\*\\*")
                                val annotatedString = androidx.compose.ui.text.buildAnnotatedString {
                                    val matches = boldRegex.findAll(paragraph)
                                    var lastIndex = 0
                                    for (match in matches) {
                                        val start = match.range.first
                                        val end = match.range.last + 1
                                        append(paragraph.substring(lastIndex, start))
                                        pushStyle(androidx.compose.ui.text.SpanStyle(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface))
                                        append(match.groupValues[1])
                                        pop()
                                        lastIndex = end
                                    }
                                    if (lastIndex < paragraph.length) {
                                        append(paragraph.substring(lastIndex))
                                    }
                                }
                                Text(
                                    text = annotatedString,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        var isOkFocused by remember { mutableStateOf(false) }
                        TextButton(
                            onClick = { activeHelpDialog = null },
                            modifier = Modifier
                                .onFocusChanged { isOkFocused = it.isFocused }
                                .then(
                                    if (isTv) {
                                        Modifier.border(
                                            width = 1.5.dp,
                                            color = if (isOkFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                    } else Modifier
                                )
                        ) {
                            Text("OK")
                        }
                    }
                }
            }
        }
    }

    if (showDevices) {
        DeviceListDialog(
            title = androidx.compose.ui.res.stringResource(R.string.devices_title),
            config = state.config,
            devices = state.deviceSpeeds,
            deviceHistory = state.deviceSpeedHistory,
            onDismiss = { showDevices = false },
            onNavigateToConsole = { cmd ->
                showDevices = false
                if (cmd != null) {
                    viewModel.setCommandInput(cmd)
                }
                viewModel.switchTab(TabType.CONSOLE)
            },
            onRefreshCapabilities = { viewModel.recheckCapabilities() }
        )
    }
}

@Composable
fun Text(text: String, modifier: Modifier = Modifier, color: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Unspecified, fontSize: androidx.compose.ui.unit.TextUnit = androidx.compose.ui.unit.TextUnit.Unspecified, fontStyle: androidx.compose.ui.text.font.FontStyle? = null, fontWeight: androidx.compose.ui.text.font.FontWeight? = null, fontFamily: androidx.compose.ui.text.font.FontFamily? = null, letterSpacing: androidx.compose.ui.unit.TextUnit = androidx.compose.ui.unit.TextUnit.Unspecified, textDecoration: androidx.compose.ui.text.style.TextDecoration? = null, textAlign: androidx.compose.ui.text.style.TextAlign? = null,
    lineHeight: androidx.compose.ui.unit.TextUnit = androidx.compose.ui.unit.TextUnit.Unspecified,
    overflow: androidx.compose.ui.text.style.TextOverflow = androidx.compose.ui.text.style.TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: ((androidx.compose.ui.text.TextLayoutResult) -> Unit)? = null,
    style: androidx.compose.ui.text.TextStyle = androidx.compose.material3.LocalTextStyle.current
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val translated = translateText(text, context)
    androidx.compose.material3.Text(
        text = translated,
        modifier = modifier,
        color = color,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        onTextLayout = onTextLayout,
        style = style
    )
}

@Composable
fun Icon(
    imageVector: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String?,
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier,
    tint: androidx.compose.ui.graphics.Color = androidx.compose.material3.LocalContentColor.current
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val translated = contentDescription?.let { translateText(it, context) }
    androidx.compose.material3.Icon(
        imageVector = imageVector,
        contentDescription = translated,
        modifier = modifier,
        tint = tint
    )
}

fun translateText(raw: String, context: android.content.Context): String {
    val lang = context.resources.configuration.locales[0].language
    return when (raw) {
        "Подключение к интерактивной оболочке SSH..." -> when (lang) {
            "ru" -> "Подключение к интерактивной оболочке SSH..."
            "uk" -> "Підключення до інтерактивної оболонки SSH..."
            "be" -> "Падключэнне да інтэрактыўнай абалонкі SSH..."
            "de" -> "Verbindung zur interaktiven SSH-Shell..."
            "es" -> "Conectando al shell interactivo SSH..."
            "fr" -> "Connexion au shell interactif SSH..."
            "it" -> "Connessione alla shell interattiva SSH..."
            "pt" -> "Conectando ao shell interativo SSH..."
            "da" -> "Opretter forbindelse til interaktiv SSH-shell..."
            "fi" -> "Yhdistetään interaktiiviseen SSH-kuoreen..."
            "kk" -> "SSH интерактивті қабықшасына қосылу..."
            "lt" -> "Jungiamasi prie interaktyvaus SSH apvalkalo..."
            "lv" -> "Notiek savienojuma izveide ar interaktīvo SSH čaulu..."
            "sv" -> "Ansluter till interaktiv SSH-skal..."
            else -> "Connecting to SSH interactive shell..."
        }
        "Очистить и перезапустить консоль" -> when (lang) {
            "ru" -> "Очистить и перезапустить консоль"
            "uk" -> "Очистити і перезапустити консоль"
            "be" -> "Ачысціць і перазапусціць кансоль"
            "de" -> "Konsole leeren und neu starten"
            "es" -> "Limpiar y reiniciar consola"
            "fr" -> "Effacer et redémarrer la console"
            "it" -> "Pulisci e riavvia console"
            "pt" -> "Limpar e reiniciar console"
            "da" -> "Ryd og genstart konsol"
            "fi" -> "Tyhjennä ja käynnistä konsoli uudelleen"
            "kk" -> "Консольді тазалау және қайта іске қосу"
            "lt" -> "Išvalyti ir paleisti konsolę iš naujo"
            "lv" -> "Notīrīt un restartēt konsoli"
            "sv" -> "Rensa och starta om konsolen"
            else -> "Clear and restart console"
        }
        "Выйти из приложения?" -> when (lang) {
            "ru" -> "Выйти из приложения?"
            "uk" -> "Вийти з програми?"
            "be" -> "Выйсці з праграмы?"
            "de" -> "App beenden?"
            "es" -> "¿Salir de la aplicación?"
            "fr" -> "Quitter l'application ?"
            "it" -> "Uscire dall'applicazione?"
            "pt" -> "Sair do aplicativo?"
            "da" -> "Afslut appen?"
            "fi" -> "Lopeta sovellus?"
            "kk" -> "Қолданбадан шығу керек пе?"
            "lt" -> "Išeiti iš programos?"
            "lv" -> "Iziet no lietotnes?"
            "sv" -> "Avsluta appen?"
            else -> "Exit application?"
        }
        "Вы действительно хотите закрыть приложение?" -> when (lang) {
            "ru" -> "Вы действительно хотите закрыть приложение?"
            "uk" -> "Ви дійсно хочете закрити програму?"
            "be" -> "Вы сапраўды хочаце закрыць праграму?"
            "de" -> "Möchten Sie die App wirklich schließen?"
            "es" -> "¿Realmente quieres cerrar la aplicación?"
            "fr" -> "Voulez-vous vraiment fermer l'application ?"
            "it" -> "Vuoi davvero chiudere l'applicazione?"
            "pt" -> "Deseja realmente fechar o aplicativo?"
            "da" -> "Vil du virkelig lukke appen?"
            "fi" -> "Haluatko varmasti sulkea sovelluksen?"
            "kk" -> "Қолданбаны шынымен жапқыңыз келе ме?"
            "lt" -> "Ar tikrai norite uždaryti programą?"
            "lv" -> "Vai tiešām vēlaties aizvērt lietotni?"
            "sv" -> "Vill du verkligen stänga appen?"
            else -> "Do you really want to close the application?"
        }
        "Да" -> when (lang) {
            "ru" -> "Да"
            "uk" -> "Так"
            "be" -> "Так"
            "de" -> "Ja"
            "es" -> "Sí"
            "fr" -> "Oui"
            "it" -> "Sì"
            "pt" -> "Sim"
            "da" -> "Ja"
            "fi" -> "Kyllä"
            "kk" -> "Иә"
            "lt" -> "Taip"
            "lv" -> "Jā"
            "sv" -> "Ja"
            else -> "Yes"
        }
        "Отмена" -> when (lang) {
            "ru" -> "Отмена"
            "uk" -> "Скасувати"
            "be" -> "Адмена"
            "de" -> "Abbrechen"
            "es" -> "Cancelar"
            "fr" -> "Annuler"
            "it" -> "Annulla"
            "pt" -> "Cancelar"
            "da" -> "Annuller"
            "fi" -> "Peruuta"
            "kk" -> "Бас тарту"
            "lt" -> "Atšaukti"
            "lv" -> "Atcelt"
            "sv" -> "Avbryt"
            else -> "Cancel"
        }
        "Вставить" -> when (lang) {
            "ru" -> "Вставить"
            "uk" -> "Вставити"
            "be" -> "Уставіць"
            "de" -> "Einfügen"
            "es" -> "Pegar"
            "fr" -> "Coller"
            "it" -> "Incolla"
            "pt" -> "Colar"
            "da" -> "Sæt ind"
            "fi" -> "Liitä"
            "kk" -> "Қою"
            "lt" -> "Įklijuoti"
            "lv" -> "Ielīmēt"
            "sv" -> "Klistra in"
            else -> "Paste"
        }
        "Доступно обновление" -> when (lang) {
            "ru" -> "Доступно обновление"
            "uk" -> "Доступне оновлення"
            "be" -> "Даступна абнаўленне"
            "de" -> "Update verfügbar"
            "es" -> "Actualización disponible"
            "fr" -> "Mise à jour disponible"
            "it" -> "Aggiornamento disponibile"
            "pt" -> "Atualização disponível"
            "da" -> "Opdatering tilgængelig"
            "fi" -> "Päivitys saatavilla"
            "kk" -> "Жаңарту қолжетімді"
            "lt" -> "Galimas atnaujinimas"
            "lv" -> "Pieejams atjauninājums"
            "sv" -> "Uppdatering tillgänglig"
            else -> "Update available"
        }
        "Найдена новая версия:" -> when (lang) {
            "ru" -> "Найдена новая версия:"
            "uk" -> "Знайдено нову версію:"
            "be" -> "Знойдзена новая версія:"
            "de" -> "Neue Version gefunden:"
            "es" -> "Nueva versión encontrada:"
            "fr" -> "Nouvelle version trouvée :"
            "it" -> "Nuova versione trovata:"
            "pt" -> "Nova versão encontrada:"
            "da" -> "Ny version fundet:"
            "fi" -> "Uusi versio löytyi:"
            "kk" -> "Жаңа нұсқа табылды:"
            "lt" -> "Rasta nauja versija:"
            "lv" -> "Atrasta jauna versija:"
            "sv" -> "Ny version hittades:"
            else -> "New version found:"
        }
        "Обновить" -> when (lang) {
            "ru" -> "Обновить"
            "uk" -> "Оновити"
            "be" -> "Абнавіць"
            "de" -> "Aktualisieren"
            "es" -> "Actualizar"
            "fr" -> "Mettre à jour"
            "it" -> "Aggiorna"
            "pt" -> "Atualizar"
            "da" -> "Opdater"
            "fi" -> "Päivitä"
            "kk" -> "Жаңарту"
            "lt" -> "Atnaujinti"
            "lv" -> "Atjaunināt"
            "sv" -> "Uppdatera"
            else -> "Update"
        }
        "Выход" -> when (lang) {
            "ru" -> "Выход"
            "uk" -> "Вихід"
            "be" -> "Выхад"
            "de" -> "Beenden"
            "es" -> "Salir"
            "fr" -> "Quitter"
            "it" -> "Esci"
            "pt" -> "Sair"
            "da" -> "Afslut"
            "fi" -> "Poistu"
            "kk" -> "Шығу"
            "lt" -> "Išeiti"
            "lv" -> "Iziet"
            "sv" -> "Avsluta"
            else -> "Exit"
        }
        "Проверить обновления" -> when (lang) {
            "ru" -> "Проверить обновления"
            "uk" -> "Перевірити оновлення"
            "be" -> "Праверыць абнаўленні"
            "de" -> "Auf Updates prüfen"
            "es" -> "Buscar actualizaciones"
            "fr" -> "Vérifier les mises à jour"
            "it" -> "Controlla aggiornamenti"
            "pt" -> "Verificar atualizações"
            "da" -> "Søg efter opdateringer"
            "fi" -> "Tarkista päivitykset"
            "kk" -> "Жаңартуларды тексеру"
            "lt" -> "Tikrinti atnaujinimus"
            "lv" -> "Pārbaudīt atjauninājumus"
            "sv" -> "Sök efter uppdateringar"
            else -> "Check for updates"
        }
        "У вас установлена последняя версия" -> when (lang) {
            "ru" -> "У вас установлена последняя версия"
            "uk" -> "У вас встановлена остання версія"
            "be" -> "У вас усталявана апошняя версія"
            "de" -> "Sie haben die neueste Version installiert"
            "es" -> "Tienes la última versión instalada"
            "fr" -> "Vous avez la dernière version installée"
            "it" -> "Hai l'ultima versione installata"
            "pt" -> "Você tem a última versão instalada"
            "da" -> "Du har den seneste version installeret"
            "fi" -> "Sinulla on uusin versio asennettuna"
            "kk" -> "Сізде соңғы нұсқасы орнатылған"
            "lt" -> "Įdiegta naujausia versija"
            "lv" -> "Jums ir instalēta jaunākā versija"
            "sv" -> "Du har den senaste versionen installerad"
            else -> "You have the latest version installed"
        }
        "Ошибка проверки обновлений" -> when (lang) {
            "ru" -> "Ошибка проверки обновлений"
            "uk" -> "Помилка перевірки оновлень"
            "be" -> "Памылка праверкі абнаўленняў"
            "de" -> "Fehler bei der Update-Prüfung"
            "es" -> "Error al buscar actualizaciones"
            "fr" -> "Erreur lors de la vérification des mises à jour"
            "it" -> "Errore durante il controllo degli aggiornamenti"
            "pt" -> "Erro ao verificar atualizações"
            "da" -> "Fejl ved søgning efter opdateringer"
            "fi" -> "Virhe tarkistettaessa päivityksiä"
            "kk" -> "Жаңартуларды тексеру қатесі"
            "lt" -> "Klaida tikrinant atnaujinimus"
            "lv" -> "Kļūda, pārbaudot atjauninājumus"
            "sv" -> "Fel vid sökning efter uppdateringar"
            else -> "Error checking for updates"
        }
        "Выйти и больше не спрашивать" -> when (lang) {
            "ru" -> "Выйти и больше не спрашивать"
            "uk" -> "Вийти і більше не питати"
            "be" -> "Выйсці і больш не пытацца"
            "de" -> "Beenden und nicht mehr fragen"
            "es" -> "Salir y no volver a preguntar"
            "fr" -> "Quitter et ne plus demander"
            "it" -> "Esci e non chiedere più"
            "pt" -> "Sair e não perguntar mais"
            "da" -> "Afslut og spørg ikke igen"
            "fi" -> "Poistu äläkä kysy uudelleen"
            "kk" -> "Шығу және қайта сұрамау"
            "lt" -> "Išeiti ir daugiau neklausti"
            "lv" -> "Iziet un vairs nejautāt"
            "sv" -> "Avsluta och fråga inte igen"
            else -> "Exit and do not ask again"
        }
        "Скачать обновление вы можете вручную с https://github.com/Vihtoor/openwrt-router-control/releases" -> when (lang) {
            "ru" -> "Скачать обновление вы можете вручную с https://github.com/Vihtoor/openwrt-router-control/releases"
            "uk" -> "Завантажити оновлення ви можете вручну з https://github.com/Vihtoor/openwrt-router-control/releases"
            "be" -> "Сцягнуць абнаўленне вы можаце ўручную з https://github.com/Vihtoor/openwrt-router-control/releases"
            "de" -> "Sie können das Update manuell herunterladen von https://github.com/Vihtoor/openwrt-router-control/releases"
            "es" -> "Puede descargar la actualización manualmente desde https://github.com/Vihtoor/openwrt-router-control/releases"
            "fr" -> "Vous pouvez télécharger la mise à jour manuellement depuis https://github.com/Vihtoor/openwrt-router-control/releases"
            "it" -> "Puoi scaricare l'aggiornamento manualmente da https://github.com/Vihtoor/openwrt-router-control/releases"
            "pt" -> "Você pode baixar a atualização manualmente de https://github.com/Vihtoor/openwrt-router-control/releases"
            "da" -> "Du kan downloade opdateringen manuelt fra https://github.com/Vihtoor/openwrt-router-control/releases"
            "fi" -> "Voit ladata päivityksen manuaalisesti osoitteesta https://github.com/Vihtoor/openwrt-router-control/releases"
            "kk" -> "Жаңартуды қолмен https://github.com/Vihtoor/openwrt-router-control/releases сілтемесінен жүктей аласыз"
            "lt" -> "Atnaujinimą galite atsisiųsti rankiniu būdu iš https://github.com/Vihtoor/openwrt-router-control/releases"
            "lv" -> "Atjauninājumu varat lejupielādēt manuāli no https://github.com/Vihtoor/openwrt-router-control/releases"
            "sv" -> "Du kan ladda ner uppdateringen manuellt från https://github.com/Vihtoor/openwrt-router-control/releases"
            else -> "You can download the update manually from https://github.com/Vihtoor/openwrt-router-control/releases"
        }
        "Проверить обновление можно вручную в разделе настроек О приложении" -> when (lang) {
            "ru" -> "Проверить обновление можно вручную в разделе настроек О приложении"
            "uk" -> "Перевірити оновлення можна вручну в розділі налаштувань Про програму"
            "be" -> "Праверыць абнаўленне можна ўручную ў раздзеле налад Пра праграму"
            "de" -> "Sie können manuell im Einstellungsbereich 'Über die App' nach Updates suchen"
            "es" -> "Puede buscar actualizaciones manualmente en la sección de configuración Acerca de la aplicación"
            "fr" -> "Vous pouvez vérifier manuellement les mises à jour dans la section Paramètres À propos de l'application"
            "it" -> "Puoi controllare manualmente gli aggiornamenti nella sezione delle impostazioni Informazioni sull'app"
            "pt" -> "Você pode verificar as atualizações manualmente na seção de configurações Sobre o aplicativo"
            "da" -> "Du kan søge efter opdateringer manuelt i indstillingssektionen Om appen"
            "fi" -> "Voit tarkistaa päivitykset manuaalisesti Tietoja sovelluksesta -asetusosiossa"
            "kk" -> "Жаңартуды Қолданба туралы параметрлер бөлімінде қолмен тексеруге болады"
            "lt" -> "Atnaujinimus galite patikrinti rankiniu būdu nustatymų skiltyje Apie programą"
            "lv" -> "Atjauninājumus varat pārbaudīt manuāli iestatījumu sadaļā Par lietotni"
            "sv" -> "Du kan söka efter uppdateringar manuellt i inställningsavsnittet Om appen"
            else -> "You can check for updates manually in the About app settings section"
        }
        "Выберите DNS для использования на роутере." -> {
            when (lang) {
                "ru" -> "Выберите DNS для использования на роутере."
                "uk" -> "Виберіть DNS для використання на роутері."
                "be" -> "Выберыце DNS для выкарыстання на роўтэры."
                "de" -> "Wählen Sie das DNS für den Router aus."
                "es" -> "Elija el DNS para usar en el enrutador."
                "fr" -> "Choisissez le DNS à utiliser sur le routeur."
                "it" -> "Scegli il DNS da utilizzare sul router."
                "pt" -> "Escolha o DNS para usar no roteador."
                "da" -> "Vælg DNS til brug på routeren."
                "fi" -> "Valitse reitittimessä käytettävä DNS."
                "kk" -> "Роутерде пайдалану үшін DNS таңдаңыз."
                "lt" -> "Pasirinkite DNS naudoti maršrutizatoriuje."
                "lv" -> "Izvēlieties DNS lietošanai maršrutētājā."
                "sv" -> "Välj DNS för användning på routern."
                else -> "Choose DNS to use on the router."
            }
        }
        "Пользовательский DNS" -> {
            when (lang) {
                "ru" -> "Пользовательский DNS"
                "uk" -> "Користувацький DNS"
                "be" -> "Карыстацкі DNS"
                "de" -> "Benutzerdefiniertes DNS"
                "es" -> "DNS personalizado"
                "fr" -> "DNS personnalisé"
                "it" -> "DNS personalizzato"
                "pt" -> "DNS personalizado"
                "da" -> "Brugerdefineret DNS"
                "fi" -> "Mukautettu DNS"
                "kk" -> "Пайдаланушылық DNS"
                "lt" -> "Pasirinktinis DNS"
                "lv" -> "Pielāgots DNS"
                "sv" -> "Anpassad DNS"
                else -> "Custom DNS"
            }
        }
        "IP адрес DNS сервера" -> {
            when (lang) {
                "ru" -> "IP адрес DNS сервера"
                "uk" -> "IP адреса DNS сервера"
                "be" -> "IP адрас DNS сервера"
                "de" -> "IP-Adresse des DNS-Servers"
                "es" -> "Dirección IP del servidor DNS"
                "fr" -> "Adresse IP du serveur DNS"
                "it" -> "Indirizzo IP del server DNS"
                "pt" -> "Endereço IP do servidor DNS"
                "da" -> "IP-adresse for DNS-server"
                "fi" -> "DNS-palvelimen IP-osoite"
                "kk" -> "DNS серверінің IP мекенжайы"
                "lt" -> "DNS serverio IP adresas"
                "lv" -> "DNS servera IP adrese"
                "sv" -> "IP-adress för DNS-server"
                else -> "DNS server IP address"
            }
        }
        "Задать" -> {
            when (lang) {
                "ru" -> "Задать"
                "uk" -> "Встановити"
                "be" -> "Задаць"
                "de" -> "Festlegen"
                "es" -> "Establecer"
                "fr" -> "Définir"
                "it" -> "Imposta"
                "pt" -> "Definir"
                "da" -> "Indstil"
                "fi" -> "Aseta"
                "kk" -> "Орнату"
                "lt" -> "Nustatyti"
                "lv" -> "Iestatīt"
                "sv" -> "Ställ in"
                else -> "Set"
            }
        }
        "Желаете ли вы добавить дополнительный DNS?" -> {
            when (lang) {
                "ru" -> "Желаете ли вы добавить дополнительный DNS?"
                "uk" -> "Чи бажаєте ви додати додатковий DNS?"
                "be" -> "Ці жадаеце вы дадаць дадатковы DNS?"
                "de" -> "Möchten Sie einen zusätzlichen DNS hinzufügen?"
                "es" -> "¿Desea agregar un DNS adicional?"
                "fr" -> "Souhaitez-vous ajouter un DNS supplémentaire ?"
                "it" -> "Vuoi aggiungere un DNS aggiuntivo?"
                "pt" -> "Deseja adicionar um DNS adicional?"
                "da" -> "Vil du tilføje en yderligere DNS?"
                "fi" -> "Haluatko lisätä ylimääräisen DNS:n?"
                "kk" -> "Қосымша DNS қосқыңыз келе ме?"
                "lt" -> "Ar norite pridėti papildomą DNS?"
                "lv" -> "Vai vēlaties pievienot papildu DNS?"
                "sv" -> "Vill du lägga till en extra DNS?"
                else -> "Do you want to add an additional DNS?"
            }
        }
        "Желаете ли вы добавить один из публичных DNS?" -> {
            when (lang) {
                "ru" -> "Желаете ли вы добавить один из публичных DNS?"
                "uk" -> "Чи бажаєте ви додати один з публічних DNS?"
                "be" -> "Ці жадаеце вы дадаць адзін з публічных DNS?"
                "de" -> "Möchten Sie einen öffentlichen DNS hinzufügen?"
                "es" -> "¿Desea agregar uno de los DNS públicos?"
                "fr" -> "Souhaitez-vous ajouter un des DNS publics ?"
                "it" -> "Vuoi aggiungere uno dei DNS pubblici?"
                "pt" -> "Deseja adicionar um dos DNS públicos?"
                "da" -> "Vil du tilføje en af de offentlige DNS'er?"
                "fi" -> "Haluatko lisätä yhden julkisista DNS:istä?"
                "kk" -> "Ашық DNS-тің бірін қосқыңыз келе ме?"
                "lt" -> "Ar norite pridėti vieną iš viešųjų DNS?"
                "lv" -> "Vai vēlaties pievienot vienu no publiskajiem DNS?"
                "sv" -> "Vill du lägga till en av de offentliga DNS:erna?"
                else -> "Do you want to add one of the public DNS?"
            }
        }
        "Добавьте один или несколько IP, разделяя их запятыми" -> {
            when (lang) {
                "ru" -> "Добавьте один или несколько IP, разделяя их запятыми"
                "uk" -> "Додайте один або кілька IP, розділяючи їх комами"
                "be" -> "Дадайце адзін або некалькі IP, раздзяляючы іх коскамі"
                "de" -> "Fügen Sie eine oder mehrere IP-Adressen durch Kommas getrennt hinzu"
                "es" -> "Agregue una o más IP, separadas por comas"
                "fr" -> "Ajoutez une ou plusieurs adresses IP, séparées par des virgules"
                "it" -> "Aggiungi uno o più IP, separati da virgole"
                "pt" -> "Adicione um ou mais IPs, separados por vírgulas"
                "da" -> "Tilføj en eller flere IP'er, adskilt af kommaer"
                "fi" -> "Lisää yksi tai useampi IP-osoite pilkuilla erotettuna"
                "kk" -> "Бір немесе бірнеше IP қосыңыз, оларды үтірмен бөліңіз"
                "lt" -> "Pridėkite vieną ar kelis IP, atskirtus kableliais"
                "lv" -> "Pievienojiet vienu vai vairākus IP, atdalot tos ar komatiem"
                "sv" -> "Lägg till en eller flera IP-adresser separerade med kommatecken"
                else -> "Add one or more IPs, separated by commas"
            }
        }
        "Дополнительный DNS" -> {
            when (lang) {
                "ru" -> "Дополнительный DNS"
                "uk" -> "Додатковий DNS"
                "be" -> "Дадатковы DNS"
                "de" -> "Zusätzliches DNS"
                "es" -> "DNS adicional"
                "fr" -> "DNS supplémentaire"
                "it" -> "DNS aggiuntivo"
                "pt" -> "DNS adicional"
                "da" -> "Yderligere DNS"
                "fi" -> "Lisä-DNS"
                "kk" -> "Қосымша DNS"
                "lt" -> "Papildomas DNS"
                "lv" -> "Papildu DNS"
                "sv" -> "Ytterligare DNS"
                else -> "Additional DNS"
            }
        }
        "Да" -> {
            when (lang) {
                "ru" -> "Да"
                "uk" -> "Так"
                "be" -> "Так"
                "de" -> "Ja"
                "es" -> "Sí"
                "fr" -> "Oui"
                "it" -> "Sì"
                "pt" -> "Sim"
                "da" -> "Ja"
                "fi" -> "Kyllä"
                "kk" -> "Иә"
                "lt" -> "Taip"
                "lv" -> "Jā"
                "sv" -> "Ja"
                else -> "Yes"
            }
        }
        "Нет" -> {
            when (lang) {
                "ru" -> "Нет"
                "uk" -> "Ні"
                "be" -> "Не"
                "de" -> "Nein"
                "es" -> "No"
                "fr" -> "Non"
                "it" -> "No"
                "pt" -> "Não"
                "da" -> "Nej"
                "fi" -> "Ei"
                "kk" -> "Жоқ"
                "lt" -> "Ne"
                "lv" -> "Nē"
                "sv" -> "Nej"
                else -> "No"
            }
        }
        "Connecting..." -> {
            when (lang) {
                "ru" -> "Подключение..."
                "uk" -> "Підключення..."
                "be" -> "Падключэнне..."
                "de" -> "Verbinden..."
                "es" -> "Conectando..."
                "fr" -> "Connexion..."
                "it" -> "Connessione..."
                "pt" -> "Conectando..."
                "da" -> "Forbinder..."
                "fi" -> "Yhdistetään..."
                "kk" -> "Қосылуда..."
                "lt" -> "Jungiamasi..."
                "lv" -> "Savienojas..."
                "sv" -> "Ansluter..."
                else -> "Connecting..."
            }
        }
        "Detecting..." -> {
            when (lang) {
                "ru" -> "Определение..."
                "uk" -> "Визначення..."
                "be" -> "Вызначэнне..."
                "de" -> "Ermitteln..."
                "es" -> "Detectando..."
                "fr" -> "Détection..."
                "it" -> "Rilevamento..."
                "pt" -> "Detectando..."
                "da" -> "Registrerer..."
                "fi" -> "Tunnistetaan..."
                "kk" -> "Анықталуда..."
                "lt" -> "Aptinkama..."
                "lv" -> "Nosaka..."
                "sv" -> "Söker..."
                else -> "Detecting..."
            }
        }
        "Offline / Error" -> {
            when (lang) {
                "ru" -> "Офлайн / Ошибка"
                "uk" -> "Офлайн / Помилка"
                "be" -> "Афлайн / Памылка"
                "de" -> "Offline / Fehler"
                "es" -> "Sin conexão / Error"
                "fr" -> "Hors ligne / Erreur"
                "it" -> "Offline / Errore"
                "pt" -> "Offline / Erro"
                "da" -> "Offline / Fejl"
                "fi" -> "Offline / Virhe"
                "kk" -> "Офлайн / Қате"
                "lt" -> "Neprisijungęs / Klaida"
                "lv" -> "Bezsaiste / Kļūda"
                "sv" -> "Offline / Fel"
                else -> "Offline / Error"
            }
        }
        "Unavailable" -> {
            when (lang) {
                "ru" -> "Недоступно"
                "uk" -> "Недоступно"
                "be" -> "Недаступна"
                "de" -> "Nicht verfügbar"
                "es" -> "No disponible"
                "fr" -> "Indisponible"
                "it" -> "Non disponibile"
                "pt" -> "Indisponível"
                "da" -> "Utilgængelig"
                "fi" -> "Ei saatavilla"
                "kk" -> "Қолжетімсіз"
                "lt" -> "Neprieinama"
                "lv" -> "Nav pieejams"
                "sv" -> "Inte tillgänglig"
                else -> "Unavailable"
            }
        }
        "Unknown" -> {
            when (lang) {
                "ru" -> "Неизвестно"
                "uk" -> "Невідомо"
                "be" -> "Невядома"
                "de" -> "Unbekannt"
                "es" -> "Desconocido"
                "fr" -> "Inconnu"
                "it" -> "Sconosciuto"
                "pt" -> "Desconhecido"
                "da" -> "Ukendt"
                "fi" -> "Tuntematon"
                "kk" -> "Белгісіз"
                "lt" -> "Nežinoma"
                "lv" -> "Nezināms"
                "sv" -> "Okänd"
                else -> "Unknown"
            }
        }
        "Подтверждение DNS" -> {
            when (lang) {
                "ru" -> "Подтверждение DNS"
                "uk" -> "Підтвердження DNS"
                "be" -> "Пацвярджэнне DNS"
                "de" -> "DNS-Bestätigung"
                "es" -> "Confirmación de DNS"
                "fr" -> "Confirmation DNS"
                "it" -> "Conferma DNS"
                "pt" -> "Confirmação de DNS"
                "da" -> "DNS-bekræftelse"
                "fi" -> "DNS-vahvistus"
                "kk" -> "DNS растау"
                "lt" -> "DNS patvirtinimas"
                "lv" -> "DNS apstiprinājums"
                "sv" -> "DNS-bekräftelse"
                else -> "DNS Confirmation"
            }
        }
        "Установить %s в качестве DNS сервера?" -> {
            when (lang) {
                "ru" -> "Установить %s в качестве DNS сервера?"
                "uk" -> "Встановити %s як DNS сервер?"
                "be" -> "Усталяваць %s у якасці DNS сервера?"
                "de" -> "%s als DNS-Server festlegen?"
                "es" -> "¿Establecer %s como servidor DNS?"
                "fr" -> "Définir %s comme serveur DNS ?"
                "it" -> "Impostare %s come server DNS?"
                "pt" -> "Definir %s como servidor DNS?"
                "da" -> "Indstil %s som DNS-server?"
                "fi" -> "Aseta %s DNS-palvelimeksi?"
                "kk" -> "%s DNS сервері ретінде орнату керек пе?"
                "lt" -> "Nustatyti %s kaip DNS serverį?"
                "lv" -> "Iestatīt %s kā DNS serveri?"
                "sv" -> "Ställ in %s som DNS-server?"
                else -> "Set %s as DNS server?"
            }
        }
        "Подтвердить" -> {
            when (lang) {
                "ru" -> "Подтвердить"
                "uk" -> "Підтвердити"
                "be" -> "Пацвердзіць"
                "de" -> "Bestätigen"
                "es" -> "Confirmar"
                "fr" -> "Confirmer"
                "it" -> "Conferma"
                "pt" -> "Confirmar"
                "da" -> "Bekræft"
                "fi" -> "Vahvista"
                "kk" -> "Растау"
                "lt" -> "Patvirtinti"
                "lv" -> "Apstiprināt"
                "sv" -> "Bekräfta"
                else -> "Confirm"
            }
        }
        "Публичные DNS сервисы" -> {
            when (lang) {
                "ru" -> "Публичные DNS сервисы"
                "uk" -> "Публічні DNS сервіси"
                "be" -> "Публічныя DNS сэрвісы"
                "de" -> "Öffentliche DNS-Dienste"
                "es" -> "Servicios DNS públicos"
                "fr" -> "Services DNS publics"
                "it" -> "Servizi DNS pubblici"
                "pt" -> "Serviços DNS públicos"
                "da" -> "Offentlige DNS-tjenester"
                "fi" -> "Julkiset DNS-palvelut"
                "kk" -> "Жалпыға ортақ DNS қызметтері"
                "lt" -> "Viešosios DNS paslaugos"
                "lv" -> "Publiskie DNS pakalpojumi"
                "sv" -> "Offentliga DNS-tjänster"
                else -> "Public DNS Services"
            }
        }
        "дополнительно" -> {
            when (lang) {
                "ru" -> "дополнительно"
                "uk" -> "додатково"
                "be" -> "дадаткова"
                "de" -> "zusätzlich"
                "es" -> "adicionalmente"
                "fr" -> "en plus"
                "it" -> "inoltre"
                "pt" -> "adicionalmente"
                "da" -> "ekstra"
                "fi" -> "lisäksi"
                "kk" -> "қосымша"
                "lt" -> "papildomai"
                "lv" -> "papildus"
                "sv" -> "extra"
                else -> "additionally"
            }
        }
        "По умолчанию (Провайдер)" -> {
            when (lang) {
                "ru" -> "По умолчанию (Провайдер)"
                "uk" -> "За замовчуванням (Провайдер)"
                "be" -> "Па змаўчанні (Правайдэр)"
                "de" -> "Standard (ISP)"
                "es" -> "Predeterminado (ISP)"
                "fr" -> "Par défaut (FAI)"
                "it" -> "Predefinito (ISP)"
                "pt" -> "Padrão (ISP)"
                "da" -> "Standard (ISP)"
                "fi" -> "Oletus (ISP)"
                "kk" -> "Әдепкі (Провайдер)"
                "lt" -> "Numatytasis (ISP)"
                "lv" -> "Noklusējums (ISP)"
                "sv" -> "Standard (ISP)"
                else -> "Default (ISP)"
            }
        }
        "Пользовательский: %s" -> {
            when (lang) {
                "ru" -> "Пользовательский: %s"
                "uk" -> "Користувацький: %s"
                "be" -> "Карыстацкі: %s"
                "de" -> "Benutzerdefiniert: %s"
                "es" -> "Personalizado: %s"
                "fr" -> "Personnalisé : %s"
                "it" -> "Personalizzato: %s"
                "pt" -> "Personalizado: %s"
                "da" -> "Brugerdefineret: %s"
                "fi" -> "Mukautettu: %s"
                "kk" -> "Пайдаланушылық: %s"
                "lt" -> "Pasirinktinis: %s"
                "lv" -> "Pielāgots: %s"
                "sv" -> "Anpassad: %s"
                else -> "Custom: %s"
            }
        }
        "Получение данных..." -> {
            when (lang) {
                "ru" -> "Получение данных..."
                "uk" -> "Отримання даних..."
                "be" -> "Атрыманне дадзеных..."
                "de" -> "Daten abrufen..."
                "es" -> "Obteniendo datos..."
                "fr" -> "Obtention des données..."
                "it" -> "Recupero dati..."
                "pt" -> "Obtendo dados..."
                "da" -> "Henter data..."
                "fi" -> "Haetaan tietoja..."
                "kk" -> "Деректер алынуда..."
                "lt" -> "Gaunami duomenys..."
                "lv" -> "Datu iegūšana..."
                "sv" -> "Hämtar data..."
                else -> "Getting data..."
            }
        }
        "Температура" -> {
            when (lang) {
                "ru" -> "Температура"
                "uk" -> "Температура"
                "be" -> "Тэмпература"
                "de" -> "Temperatur"
                "es" -> "Temperatura"
                "fr" -> "Température"
                "it" -> "Temperatura"
                "pt" -> "Temperatura"
                "da" -> "Temperatur"
                "fi" -> "Lämpötila"
                "kk" -> "Температура"
                "lt" -> "Temperatūra"
                "lv" -> "Temperatūra"
                "sv" -> "Temperatur"
                else -> "Temperature"
            }
        }
        "недоступна" -> {
            when (lang) {
                "ru" -> "недоступна"
                "uk" -> "недоступна"
                "be" -> "недаступна"
                "de" -> "nicht verfügbar"
                "es" -> "no disponible"
                "fr" -> "indisponible"
                "it" -> "non disponibile"
                "pt" -> "indisponível"
                "da" -> "ikke tilgængelig"
                "fi" -> "ei saatavilla"
                "kk" -> "қолжетімсіз"
                "lt" -> "neprieinama"
                "lv" -> "nav pieejams"
                "sv" -> "ej tillgänglig"
                else -> "unavailable"
            }
        }
        "Router Control" -> context.getString(R.string.app_name)
        "Настройка подключения" -> context.getString(R.string.tab_dashboard)
        "Главная" -> context.getString(R.string.tab_dashboard)
        "Консоль" -> context.getString(R.string.tab_console)
        "Тест" -> context.getString(R.string.tab_test)
        "Инструменты" -> context.getString(R.string.tab_test)
        "Тест скорости M-Lab" -> context.getString(R.string.speed_test_title)
        "Перезапуск" -> context.getString(R.string.tab_reboot)
        "Перезагрузка" -> context.getString(R.string.tab_reboot)
        "Перезагрузка роутера" -> context.getString(R.string.dialog_reboot_title)
        "Вы уверены, что хотите перезагрузить роутер? Это временно прервет сетевое соединение." -> context.getString(R.string.dialog_reboot_msg)
        "Перезагрузить" -> context.getString(R.string.btn_reboot)
        "Отмена" -> context.getString(R.string.btn_cancel)
        "Публичный IP" -> context.getString(R.string.label_public_ip)
        "Локация" -> context.getString(R.string.label_location)
        "Провайдер" -> context.getString(R.string.label_provider)
        "Загрузка ↓" -> context.getString(R.string.speed_download)
        "Выгрузка ↑" -> context.getString(R.string.speed_upload)
        "Процессор" -> context.getString(R.string.label_processor)
        "ЦП" -> context.getString(R.string.label_cpu)
        "ОЗУ" -> context.getString(R.string.label_ram)
        "Аптайм" -> context.getString(R.string.label_uptime)
        "Ожидание данных трафика..." -> context.getString(R.string.msg_waiting_traffic)
        "SSH терминал" -> context.getString(R.string.console_title)
        "Очистить логи" -> context.getString(R.string.console_desc_clear)
        "Терминал пуст. Введите команду в поле ниже." -> context.getString(R.string.console_empty)
        "Терминал пуст. Введите команду в поле ниже или используйте уже введенную команду для запуска теста на скачивание с роутера на смартфон в течение 30 секунд." -> context.getString(R.string.iperf_router_console_empty)
        "Терминал пуст. Введите команду в поле ниже или используйте уже введенную команду для запуска теста на скачивание с роутера на смартфон в течение 30 секунд" -> context.getString(R.string.iperf_router_console_empty)
        "Терминал пуст. Ведите команду в поле ниже или используйте уже введенную команду для запуска теста на скачивание с роутера на смартфон в течение 30 секунд." -> context.getString(R.string.iperf_router_console_empty)
        "Терминал пуст. Ведите команду в поле ниже или используйте уже введенную команду для запуска теста на скачивание с роутера на смартфон в течение 30 секунд" -> context.getString(R.string.iperf_router_console_empty)
        "Введите команду" -> context.getString(R.string.console_hint_command)
        "Отправить" -> context.getString(R.string.console_desc_send)
        "SSH Подключение" -> context.getString(R.string.ssh_title)
        context.getString(R.string.action_close) -> context.getString(R.string.ssh_desc_close)
        "Введите реквизиты доступа SSH вашего роутера OpenWrt для сбора статистики и управления службами VPN." -> context.getString(R.string.ssh_instructions)
        "IP Адрес" -> context.getString(R.string.ssh_host)
        "Порт SSH" -> context.getString(R.string.ssh_port)
        "Логин" -> context.getString(R.string.ssh_username)
        "Пароль SSH" -> context.getString(R.string.ssh_password)
        "Пароль" -> context.getString(R.string.ssh_password_short)
        "Скрыть" -> context.getString(R.string.ssh_btn_hide)
        "Показать" -> context.getString(R.string.ssh_btn_show)
        "Подключено" -> context.getString(R.string.btn_connected)
        "Подключить" -> context.getString(R.string.btn_connect)
        "Поведение светодиода" -> context.getString(R.string.led_behavior_title)
        "Горит постоянно" -> context.getString(R.string.led_always_on)
        "Индикатор трафика" -> context.getString(R.string.led_traffic_indicator)
        "Интерфейс WireGuard" -> context.getString(R.string.wg_interface_title)
        "Выберите интерфейс" -> context.getString(R.string.wg_select_interface)
        "Выбор интерфейса" -> context.getString(R.string.wg_desc_select)
        "Сохранить изменения" -> context.getString(R.string.btn_save_changes)
        "Удалить подключение" -> context.getString(R.string.btn_delete_conn)
        "Запущена" -> context.getString(R.string.status_running)
        "Остановлена" -> context.getString(R.string.status_stopped)
        "OpenVPN Служба" -> context.getString(R.string.title_openvpn_service)
        "WireGuard Интерфейс" -> context.getString(R.string.title_wireguard_interface)
        "Включен" -> context.getString(R.string.status_on)
        "Выключен" -> context.getString(R.string.status_off)
        "Выберите OpenVPN, нажав на иконку слева" -> context.getString(R.string.status_select_openvpn)
        "Выберите Wireguard, нажав на иконку слева" -> context.getString(R.string.status_select_wireguard)
        "Выберите OpenVPN / Wireguard, нажав на иконку слева" -> context.getString(R.string.status_select_vpn)
        "Список соединений" -> context.getString(R.string.vpn_list_desc)
        "Получение данных от роутера..." -> context.getString(R.string.vpn_getting_data)
        "Применение изменений..." -> context.getString(R.string.vpn_applying_changes)
        "Активен" -> context.getString(R.string.vpn_active)
        "Нет активных VPN соединений" -> context.getString(R.string.vpn_no_active)
        "Все VPN отключены" -> context.getString(R.string.vpn_all_disabled)
        "Доступные VPN. Выберите те, которые должны быть включены" -> context.getString(R.string.vpn_available_title)
        "Применить" -> context.getString(R.string.vpn_apply)
        "AmneziaWG Интерфейс" -> context.getString(R.string.vpn_amneziawg_interface)
        "Светодиод роутера" -> context.getString(R.string.title_router_led)
        "Включение..." -> context.getString(R.string.status_enabling)
        "Включение VPN..." -> context.getString(R.string.status_enabling)
        "Выключение..." -> context.getString(R.string.status_disabling)
        "Выключение VPN..." -> context.getString(R.string.status_disabling)
        "Переключение..." -> context.getString(R.string.status_switching)
        "Кликните здесь, чтобы открыть клавиатуру и начать ввод..." -> {
            when (lang) {
                "ru" -> "Кликните здесь, чтобы открыть клавиатуру и начать ввод..."
                "uk" -> "Клікніть тут, щоб відкрити клавіатуру та почати введення..."
                "be" -> "Клікніце тут, каб адкрыць клавіятуру і пачаць увод..."
                "de" -> "Klicken Sie hier, um die Tastatur zu öffnen und mit der Eingabe zu beginnen..."
                "es" -> "Haga clic aquí para abrir el teclado y comenzar a escribir..."
                "fr" -> "Cliquez ici pour ouvrir le clavier et commencer à saisir..."
                "it" -> "Clicca qui per aprire la tastiera e iniziare a digitare..."
                "pt" -> "Clique aqui para abrir o teclado e começar a digitar..."
                "da" -> "Klik her for at åbne tastaturet og begynde at skrive..."
                "fi" -> "Avaa näppäimistö ja aloita kirjoittaminen napsauttamalla tästä..."
                "kk" -> "Пернетақтаны ашу және теруді бастау үшін осы жерді басыңыз..."
                "lt" -> "Spustelėkite čias, kad atidarytumėte klaviatūrą ir pradėtumėte rašyti..."
                "lv" -> "Noklikšķiniet šeit, lai atvērtu tastatūru un sāktu rakstīt..."
                "sv" -> "Klicka här för att öppna tangentbordet och börja skriva..."
                else -> "Click here to open the keyboard and start typing..."
            }
        }
        "Добавить в избранное" -> {
            when (lang) {
                "ru" -> "Добавить в избранное"
                "uk" -> "Додати до обраного"
                "be" -> "Дадаць у абранае"
                "de" -> "Zu Favoriten hinzufügen"
                "es" -> "Agregar a favoritos"
                "fr" -> "Ajouter aux favoris"
                "it" -> "Aggiungi ai preferiti"
                "pt" -> "Adicionar aos favoritos"
                "da" -> "Tilføj til favoritter"
                "fi" -> "Lisää suosikkeihin"
                "kk" -> "Таңдаулыларға қосу"
                "lt" -> "Pridėti prie mėgstamiausių"
                "lv" -> "Pievienot izlasei"
                "sv" -> "Lägg till i favoriter"
                else -> "Add to Favorites"
            }
        }
        "Команда" -> {
            when (lang) {
                "ru" -> "Команда"
                "uk" -> "Команда"
                "be" -> "Каманда"
                "de" -> "Befehl"
                "es" -> "Comando"
                "fr" -> "Commande"
                "it" -> "Comando"
                "pt" -> "Comando"
                "da" -> "Kommando"
                "fi" -> "Komento"
                "kk" -> "Команда"
                "lt" -> "Komanda"
                "lv" -> "Komanda"
                "sv" -> "Kommando"
                else -> "Command"
            }
        }
        "Добавить" -> {
            when (lang) {
                "ru" -> "Добавить"
                "uk" -> "Додати"
                "be" -> "Дадаць"
                "de" -> "Hinzufügen"
                "es" -> "Agregar"
                "fr" -> "Ajouter"
                "it" -> "Aggiungi"
                "pt" -> "Adicionar"
                "da" -> "Tilføj"
                "fi" -> "Lisää"
                "kk" -> "Қосу"
                "lt" -> "Pridėti"
                "lv" -> "Pievienot"
                "sv" -> "Lägg till"
                else -> "Add"
            }
        }
        "Текст скопирован в буфер обмена" -> {
            when (lang) {
                "ru" -> "Текст скопирован в буфер обмена"
                "uk" -> "Текст скопійовано в буфер обміну"
                "be" -> "Тэкст скапіяваны ў буфер абмену"
                "de" -> "Text in die Zwischenablage kopiert"
                "es" -> "Texto copiado al portapapeles"
                "fr" -> "Texte copié dans le presse-papiers"
                "it" -> "Testo copiato negli appunti"
                "pt" -> "Texto copiado para a área de transferência"
                "da" -> "Tekst kopieret til udklipsholder"
                "fi" -> "Teksti kopioitu leikepöydälle"
                "kk" -> "Мәтін алмасу буферіне көшірілді"
                "lt" -> "Tekstas nukopijuotas į iškarpinę"
                "lv" -> "Teksts nokopēts starpliktuvē"
                "sv" -> "Texten har kopierats till urklipp"
                else -> "Text copied to clipboard"
            }
        }
        "Копировать" -> {
            when (lang) {
                "ru" -> "Копировать"
                "uk" -> "Копіювати"
                "be" -> "Капіяваць"
                "de" -> "Kopieren"
                "es" -> "Copiar"
                "fr" -> "Copier"
                "it" -> "Copia"
                "pt" -> "Copiar"
                "da" -> "Kopier"
                "fi" -> "Kopioi"
                "kk" -> "Көшіру"
                "lt" -> "Kopijuoti"
                "lv" -> "Kopēt"
                "sv" -> "Kopiera"
                else -> "Copy"
            }
        }
        "Android application for managing OpenWrt routers." -> when (lang) {
            "ru" -> "Android-приложение для управления роутерами OpenWrt."
            "uk" -> "Android-додаток для керування роутерами OpenWrt."
            "be" -> "Android-дадатак для кіравання роўтэрамі OpenWrt."
            "de" -> "Android-Anwendung zur Verwaltung von OpenWrt-Routern."
            "es" -> "Aplicación de Android para gestionar enrutadores OpenWrt."
            "fr" -> "Application Android pour gérer les routeurs OpenWrt."
            "it" -> "Applicazione Android per gestire i router OpenWrt."
            "pt" -> "Aplicativo Android para gerenciar roteadores OpenWrt."
            "da" -> "Android-applikation til styring af OpenWrt-routere."
            "fi" -> "Android-sovellus OpenWrt-reitittimien hallintaan."
            "kk" -> "OpenWrt роутерлерін басқаруға арналған Android қолданбасы."
            "lt" -> "„Android“ programa, skirta „OpenWrt“ maršrutizatoriams valdyti."
            "lv" -> "Android lietojumprogramma OpenWrt maršrutētāju pārvaldībai."
            "ro" -> "Aplicație Android pentru gestionarea routerelor OpenWrt."
            "sv" -> "Android-applikation för att hantera OpenWrt-routrar."
            "bg" -> "Приложение за Android за управление на рутери OpenWrt."
            "cs" -> "Aplikace pro Android pro správu routerů OpenWrt."
            "el" -> "Εφαρμογή Android για διαχείριση δρομολογητών OpenWrt."
            "et" -> "Androidi rakendus OpenWrt ruuterite haldamiseks."
            "hu" -> "Android alkalmazás az OpenWrt útválasztók kezeléséhez."
            "pl" -> "Aplikacja na Androida do zarządzania routerami OpenWrt."
            "sk" -> "Aplikácia pre Android na správu smerovačov OpenWrt."
            "sl" -> "Aplikacija za Android za upravljanje usmerjevalnikov OpenWrt."
            "tr" -> "OpenWrt yönlendiricileri yönetmek için Android uygulaması."
            else -> "Android application for managing OpenWrt routers."
        }
        "Connection established" -> when (lang) {
            "ru" -> "Соединение установлено"
            "uk" -> "З'єднання встановлено"
            "be" -> "Злучэнне ўстаноўлена"
            "de" -> "Verbindung hergestellt"
            "es" -> "Conexión establecida"
            "fr" -> "Connexion établie"
            "it" -> "Connessione stabilita"
            "pt" -> "Conexão estabelecida"
            "da" -> "Forbindelse etableret"
            "fi" -> "Yhteys muodostettu"
            "kk" -> "Қосылым орнатылды"
            "lt" -> "Ryšys užmegztas"
            "lv" -> "Savienojums izveidots"
            "ro" -> "Conexiune stabilită"
            "sv" -> "Anslutning upprättad"
            "bg" -> "Връзката е установена"
            "cs" -> "Připojení navázáno"
            "el" -> "Η σύνδεση δημιουργήθηκε"
            "et" -> "Ühendus loodud"
            "hu" -> "Kapcsolat létrehozva"
            "pl" -> "Połączenie nawiązane"
            "sk" -> "Pripojenie vytvorené"
            "sl" -> "Povezava vzpostavljena"
            "tr" -> "Bağlantı kuruldu"
            else -> "Connection established"
        }
        "IP адрес / Хост" -> when (lang) {
            "ru" -> "IP адрес / Хост"
            "uk" -> "IP адреса / Хост"
            "be" -> "IP адрас / Хост"
            "de" -> "IP-Adresse / Host"
            "es" -> "Dirección IP / Host"
            "fr" -> "Adresse IP / Hôte"
            "it" -> "Indirizzo IP / Host"
            "pt" -> "Endereço IP / Host"
            "da" -> "IP-adresse / Vært"
            "fi" -> "IP-osoite / Isäntä"
            "kk" -> "IP мекенжайы / Хост"
            "lt" -> "IP adresas / Pagarbiai"
            "lv" -> "IP adrese / Saimnieks"
            "ro" -> "Adresă IP / Gazdă"
            "sv" -> "IP-adress / Värd"
            "bg" -> "IP адрес / Хост"
            "cs" -> "IP adresa / Hostitel"
            "el" -> "Διεύθυνση IP / Κεντρικός υπολογιστής"
            "et" -> "IP-aadress / Host"
            "hu" -> "IP cím / Gazdagép"
            "pl" -> "Adres IP / Host"
            "sk" -> "IP adresa / Hostiteľ"
            "sl" -> "IP naslov / Gostitelj"
            "tr" -> "IP adresi / Sunucu"
            else -> "IP address / Host"
        }
        "Openwrt Router Control" -> when (lang) {
            "ru" -> "Openwrt Router Control"
            "uk" -> "Openwrt Router Control"
            "be" -> "Openwrt Router Control"
            "de" -> "Openwrt Router Control"
            "es" -> "Openwrt Router Control"
            "fr" -> "Openwrt Router Control"
            "it" -> "Openwrt Router Control"
            "pt" -> "Openwrt Router Control"
            "da" -> "Openwrt Router Control"
            "fi" -> "Openwrt Router Control"
            "kk" -> "Openwrt Router Control"
            "lt" -> "Openwrt Router Control"
            "lv" -> "Openwrt Router Control"
            "ro" -> "Openwrt Router Control"
            "sv" -> "Openwrt Router Control"
            "bg" -> "Openwrt Router Control"
            "cs" -> "Openwrt Router Control"
            "el" -> "Openwrt Router Control"
            "et" -> "Openwrt Router Control"
            "hu" -> "Openwrt Router Control"
            "pl" -> "Openwrt Router Control"
            "sk" -> "Openwrt Router Control"
            "sl" -> "Openwrt Router Control"
            "tr" -> "Openwrt Router Control"
            else -> "Openwrt Router Control"
        }
        "Test connection" -> when (lang) {
            "ru" -> "Проверить соединение"
            "uk" -> "Перевірити з'єднання"
            "be" -> "Праверыць злучэнне"
            "de" -> "Verbindung testen"
            "es" -> "Probar conexión"
            "fr" -> "Tester la connexion"
            "it" -> "Prova connessione"
            "pt" -> "Testar conexão"
            "da" -> "Test forbindelse"
            "fi" -> "Testaa yhteys"
            "kk" -> "Қосылымды тексеру"
            "lt" -> "Bandyti ryšį"
            "lv" -> "Pārbaudīt savienojumu"
            "ro" -> "Testare conexiune"
            "sv" -> "Testa anslutning"
            "bg" -> "Тестване на връзката"
            "cs" -> "Otestovat připojení"
            "el" -> "Δοκιμή σύνδεσης"
            "et" -> "Testi ühendust"
            "hu" -> "Kapcsolat tesztelése"
            "pl" -> "Testuj połączenie"
            "sk" -> "Otestovať pripojenie"
            "sl" -> "Preizkusi povezavo"
            "tr" -> "Bağlantıyı test et"
            else -> "Test connection"
        }
        "Версия" -> when (lang) {
            "ru" -> "Версия"
            "uk" -> "Версія"
            "be" -> "Версія"
            "de" -> "Version"
            "es" -> "Versión"
            "fr" -> "Version"
            "it" -> "Versione"
            "pt" -> "Versão"
            "da" -> "Version"
            "fi" -> "Versio"
            "kk" -> "Нұсқа"
            "lt" -> "Versija"
            "lv" -> "Versija"
            "ro" -> "Versiune"
            "sv" -> "Version"
            "bg" -> "Версия"
            "cs" -> "Verze"
            "el" -> "Έκδοση"
            "et" -> "Versioon"
            "hu" -> "Verzió"
            "pl" -> "Wersja"
            "sk" -> "Verzia"
            "sl" -> "Različica"
            "tr" -> "Sürüm"
            else -> "Version"
        }
        "Добавить новый роутер" -> when (lang) {
            "ru" -> "Добавить новый роутер"
            "uk" -> "Додати новий роутер"
            "be" -> "Дадаць новы роўтэр"
            "de" -> "Neuen Router hinzufügen"
            "es" -> "Agregar nuevo enrutador"
            "fr" -> "Ajouter un nouveau routeur"
            "it" -> "Aggiungi nuovo router"
            "pt" -> "Adicionar novo roteador"
            "da" -> "Tilføj ny router"
            "fi" -> "Lisää uusi reititin"
            "kk" -> "Жаңа роутер қосу"
            "lt" -> "Pridėti naują maršrutizatorių"
            "lv" -> "Pievienot jaunu maršrutētāju"
            "ro" -> "Adăugați un router nou"
            "sv" -> "Lägg till ny router"
            "bg" -> "Добавяне на нов рутер"
            "cs" -> "Přidat nový router"
            "el" -> "Προσθήκη νέου δρομολογητή"
            "et" -> "Lisa uus ruuter"
            "hu" -> "Új útválasztó hozzáadása"
            "pl" -> "Dodaj nowy router"
            "sk" -> "Pridať nový smerovač"
            "sl" -> "Dodaj nov usmerjevalnik"
            "tr" -> "Yeni yönlendirici ekle"
            else -> "Add new router"
        }
        context.getString(R.string.action_close) -> when (lang) {
            "ru" -> context.getString(R.string.action_close)
            "uk" -> "Закрити"
            "be" -> "Зачыніць"
            "de" -> "Schließen"
            "es" -> "Cerrar"
            "fr" -> "Fermer"
            "it" -> "Chiudi"
            "pt" -> "Fechar"
            "da" -> "Luk"
            "fi" -> "Sulje"
            "kk" -> "Жабу"
            "lt" -> "Uždaryti"
            "lv" -> "Aizvērt"
            "ro" -> "Închide"
            "sv" -> "Stäng"
            "bg" -> "Затваряне"
            "cs" -> "Zavřít"
            "el" -> "Κλείσιμο"
            "et" -> "Sulge"
            "hu" -> "Bezárás"
            "pl" -> "Zamknij"
            "sk" -> "Zavrieť"
            "sl" -> "Zapri"
            "tr" -> "Kapat"
            else -> "Close"
        }
        "Имя пользователя" -> when (lang) {
            "ru" -> "Имя пользователя"
            "uk" -> "Ім'я користувача"
            "be" -> "Імя карыстальніка"
            "de" -> "Benutzername"
            "es" -> "Nombre de usuario"
            "fr" -> "Nom d'utilisateur"
            "it" -> "Nome utente"
            "pt" -> "Nome de usuário"
            "da" -> "Brugernavn"
            "fi" -> "Käyttäjätunnus"
            "kk" -> "Пайдаланушы аты"
            "lt" -> "Vartotojo vardas"
            "lv" -> "Lietotājvārds"
            "ro" -> "Nume de utilizator"
            "sv" -> "Användarnamn"
            "bg" -> "Потребителско име"
            "cs" -> "Uživatelské jméno"
            "el" -> "Όνομα χρήστη"
            "et" -> "Kasutajanimi"
            "hu" -> "Felhasználónév"
            "pl" -> "Nazwa użytkownika"
            "sk" -> "Používateľské meno"
            "sl" -> "Uporabniško ime"
            "tr" -> "Kullanıcı adı"
            else -> "Username"
        }
        "Имя профиля" -> when (lang) {
            "ru" -> "Имя профиля"
            "uk" -> "Ім'я профілю"
            "be" -> "Імя профілю"
            "de" -> "Profilname"
            "es" -> "Nombre de perfil"
            "fr" -> "Nom du profil"
            "it" -> "Nome del profilo"
            "pt" -> "Nome do perfil"
            "da" -> "Profilnavn"
            "fi" -> "Profiilin nimi"
            "kk" -> "Профиль аты"
            "lt" -> "Profilio pavadinimas"
            "lv" -> "Profila nosaukums"
            "ro" -> "Nume profil"
            "sv" -> "Profilnamn"
            "bg" -> "Име на профила"
            "cs" -> "Název profilu"
            "el" -> "Όνομα προφίλ"
            "et" -> "Profiili nimi"
            "hu" -> "Profil neve"
            "pl" -> "Nazwa profilu"
            "sk" -> "Názov profilu"
            "sl" -> "Ime profila"
            "tr" -> "Profil adı"
            else -> "Profile name"
        }
        "Нет настроенных роутеров" -> when (lang) {
            "ru" -> "Нет настроенных роутеров"
            "uk" -> "Немає налаштованих роутерів"
            "be" -> "Няма наладжаных роўтэраў"
            "de" -> "Keine konfigurierten Router"
            "es" -> "No hay enrutadores configurados"
            "fr" -> "Aucun routeur configuré"
            "it" -> "Nessun router configurato"
            "pt" -> "Nenhum roteador configurado"
            "da" -> "Ingen konfigurerede routere"
            "fi" -> "Ei määritettyjä reitittimiä"
            "kk" -> "Бапталған роутерлер жоқ"
            "lt" -> "Nėra sukonfigūruotų maršrutizatorių"
            "lv" -> "Nav konfigurētu maršrutētāju"
            "ro" -> "Niciun router configurat"
            "sv" -> "Inga konfigurerade routrar"
            "bg" -> "Няма конфигурирани рутери"
            "cs" -> "Žádné nakonfigurované routery"
            "el" -> "Δεν υπάρχουν διαμορφωμένοι δρομολογητές"
            "et" -> "Konfigureeritud ruutereid pole"
            "hu" -> "Nincsenek konfigurált útválasztók"
            "pl" -> "Brak skonfigurowanych routerów"
            "sk" -> "Žiadne nakonfigurované smerovače"
            "sl" -> "Ni konfiguriranih usmerjevalnikov"
            "tr" -> "Yapılandırılmış yönlendiriciler yok"
            else -> "No routers configured"
        }
        "О приложении" -> when (lang) {
            "ru" -> "О приложении"
            "uk" -> "Про додаток"
            "be" -> "Пра дадатак"
            "de" -> "Über die App"
            "es" -> "Acerca de la aplicación"
            "fr" -> "À propos de l'application"
            "it" -> "Informazioni sull'app"
            "pt" -> "Sobre o aplicativo"
            "da" -> "Om appen"
            "fi" -> "Tietoja sovelluksesta"
            "kk" -> "Қолданба туралы"
            "lt" -> "Apie programą"
            "lv" -> "Par lietotni"
            "ro" -> "Despre aplicație"
            "sv" -> "Om appen"
            "bg" -> "За приложението"
            "cs" -> "O aplikaci"
            "el" -> "Σχετικά με την εφαρμογή"
            "et" -> "Rakenduse kohta"
            "hu" -> "Az alkalmazásról"
            "pl" -> "O aplikacji"
            "sk" -> "O aplikácii"
            "sl" -> "O aplikaciji"
            "tr" -> "Uygulama hakkında"
            else -> "About the app"
        }
        "Ошибка при проверке обновлений" -> when (lang) {
            "ru" -> "Ошибка при проверке обновлений"
            "uk" -> "Помилка під час перевірки оновлень"
            "be" -> "Памылка пры праверцы абнаўленняў"
            "de" -> "Fehler beim Prüfen auf Updates"
            "es" -> "Error al buscar actualizaciones"
            "fr" -> "Erreur lors de la vérification des mises à jour"
            "it" -> "Errore durante il controllo degli aggiornamenti"
            "pt" -> "Erro ao verificar atualizações"
            "da" -> "Fejl under søgning efter opdateringer"
            "fi" -> "Virhe tarkistettaessa päivityksiä"
            "kk" -> "Жаңартуларды тексеру кезіндегі қате"
            "lt" -> "Klaida tikrinant atnaujinimus"
            "lv" -> "Kļūda, pārbaudot atjauninājumus"
            "ro" -> "Eroare la verificarea actualizărilor"
            "sv" -> "Fel vid sökning efter uppdateringar"
            "bg" -> "Грешка при проверка за актуализации"
            "cs" -> "Chyba při kontrole aktualizací"
            "el" -> "Σφάλμα κατά τον έλεγχο για ενημερώσεις"
            "et" -> "Viga värskenduste otsimisel"
            "hu" -> "Hiba történt a frissítések keresésekor"
            "pl" -> "Błąd podczas sprawdzania aktualizacji"
            "sk" -> "Chyba pri kontrole aktualizácií"
            "sl" -> "Napaka pri iskanju posodobitev"
            "tr" -> "Güncellemeler kontrol edilirken hata oluştu"
            else -> "Error checking for updates"
        }
        "Пароль или приватный ключ" -> when (lang) {
            "ru" -> "Пароль или приватный ключ"
            "uk" -> "Пароль або приватний ключ"
            "be" -> "Пароль або прыватны ключ"
            "de" -> "Passwort oder privater Schlüssel"
            "es" -> "Contraseña o clave privada"
            "fr" -> "Mot de passe ou clé privée"
            "it" -> "Password o chiave privata"
            "pt" -> "Senha ou chave privada"
            "da" -> "Adgangskode eller privat nøgle"
            "fi" -> "Salasana tai yksityinen avain"
            "kk" -> "Құпия сөз немесе жеке кілт"
            "lt" -> "Slaptažodis arba privatus raktas"
            "lv" -> "Parole vai privātā atslēga"
            "ro" -> "Parolă sau cheie privată"
            "sv" -> "Lösenord eller privat nyckel"
            "bg" -> "Парола или частен ключ"
            "cs" -> "Heslo nebo soukromý klíč"
            "el" -> "Κωδικός πρόσβασης ή ιδιωτικό κλειδί"
            "et" -> "Parool või privaatvõti"
            "hu" -> "Jelszó vagy privát kulcs"
            "pl" -> "Hasło lub klucz prywatny"
            "sk" -> "Heslo alebo súkromný kľúč"
            "sl" -> "Geslo ali zasebni ključ"
            "tr" -> "Şifre veya özel anahtar"
            else -> "Password or private key"
        }
        "Пожалуйста, добавьте первый профиль роутера для продолжения." -> when (lang) {
            "ru" -> "Пожалуйста, добавьте первый профиль роутера для продолжения."
            "uk" -> "Будь ласка, додайте перший профіль роутера для продовження."
            "be" -> "Калі ласка, дадайце першы профіль роўтэра для працягу."
            "de" -> "Bitte fügen Sie Ihr erstes Router-Profil hinzu, um fortzufahren."
            "es" -> "Por favor, agregue el primer perfil de enrutador para continuar."
            "fr" -> "Veuillez ajouter le premier profil de routeur pour continuer."
            "it" -> "Aggiungi il primo profilo router per continuare."
            "pt" -> "Por favor, adicione o primeiro perfil de roteador para continuar."
            "da" -> "Tilføj venligst den første routerprofil for at fortsætte."
            "fi" -> "Lisää ensimmäinen reititinprofiili jatkaaksesi."
            "kk" -> "Жалғастыру үшін бірінші роутер профилін қосыңыз."
            "lt" -> "Norėdami tęsti, pridėkite pirmąjį maršrutizatoriaus profilį."
            "lv" -> "Lai turpinātu, lūdzu, pievienojiet pirmo maršrutētāja profilu."
            "ro" -> "Vă rugăm să adăugați primul profil de router pentru a continua."
            "sv" -> "Lägg till den första routerprofilen för att fortsätta."
            "bg" -> "Моля, добавете първия профил на рутера, за да продължите."
            "cs" -> "Chcete-li pokračovat, přidejte první profil routeru."
            "el" -> "Παρακαλώ προσθέστε το πρώτο προφίλ δρομολογητή για να συνεχίσετε."
            "et" -> "Jätkamiseks lisage esimene ruuteri profiil."
            "hu" -> "Kérjük, adja hozzá az első útválasztó profilját a folytatáshoz."
            "pl" -> "Dodaj pierwszy profil routera, aby kontynuować."
            "sk" -> "Ak chcete pokračovať, pridajte prvý profil smerovača."
            "sl" -> "Za nadaljevanje dodajte prvi profil usmerjevalnika."
            "tr" -> "Devam etmek için lütfen ilk yönlendirici profilini ekleyin."
            else -> "Please add your first router profile to continue."
        }
        "Порт" -> when (lang) {
            "ru" -> "Порт"
            "uk" -> "Порт"
            "be" -> "Порт"
            "de" -> "Port"
            "es" -> "Puerto"
            "fr" -> "Port"
            "it" -> "Porta"
            "pt" -> "Porta"
            "da" -> "Port"
            "fi" -> "Portti"
            "kk" -> "Порт"
            "lt" -> "Prievadas"
            "lv" -> "Ports"
            "ro" -> "Port"
            "sv" -> "Port"
            "bg" -> "Порт"
            "cs" -> "Port"
            "el" -> "Λιμάνι"
            "et" -> "Port"
            "hu" -> "Port"
            "pl" -> "Port"
            "sk" -> "Port"
            "sl" -> "Vrata"
            "tr" -> "Port"
            else -> "Port"
        }
        "Проверить обновление" -> when (lang) {
            "ru" -> "Проверить обновление"
            "uk" -> "Перевірити оновлення"
            "be" -> "Праверыць абнаўленне"
            "de" -> "Auf Updates prüfen"
            "es" -> "Buscar actualizaciones"
            "fr" -> "Vérifier les mises à jour"
            "it" -> "Controlla aggiornamenti"
            "pt" -> "Verificar atualizações"
            "da" -> "Søg efter opdateringer"
            "fi" -> "Tarkista päivitykset"
            "kk" -> "Жаңартуды тексеру"
            "lt" -> "Tikrinti atnaujinimus"
            "lv" -> "Pārbaudīt atjauninājumus"
            "ro" -> "Verifică actualizări"
            "sv" -> "Sök efter uppdateringar"
            "bg" -> "Проверка за актуализации"
            "cs" -> "Zkontrolovat aktualizace"
            "el" -> "Έλεγχος για ενημερώσεις"
            "et" -> "Otsi värskendusi"
            "hu" -> "Frissítések keresése"
            "pl" -> "Sprawdź aktualizacje"
            "sk" -> "Skontrolovať aktualizácie"
            "sl" -> "Preveri za posodobitve"
            "tr" -> "Güncellemeleri kontrol et"
            else -> "Check for update"
        }
        "Редактировать" -> when (lang) {
            "ru" -> "Редактировать"
            "uk" -> "Редагувати"
            "be" -> "Рэдагаваць"
            "de" -> "Bearbeiten"
            "es" -> "Editar"
            "fr" -> "Éditer"
            "it" -> "Modifica"
            "pt" -> "Editar"
            "da" -> "Rediger"
            "fi" -> "Muokkaa"
            "kk" -> "Өңдеу"
            "lt" -> "Redaguoti"
            "lv" -> "Rediģēt"
            "ro" -> "Editare"
            "sv" -> "Redigera"
            "bg" -> "Редактиране"
            "cs" -> "Upravit"
            "el" -> "Επεξεργασία"
            "et" -> "Muuda"
            "hu" -> "Szerkesztés"
            "pl" -> "Edytuj"
            "sk" -> "Upraviť"
            "sl" -> "Uredi"
            "tr" -> "Düzenle"
            else -> "Edit"
        }
        "Сохраненные роутеры" -> when (lang) {
            "ru" -> "Сохраненные роутеры"
            "uk" -> "Збережені роутери"
            "be" -> "Захаваныя роўтэры"
            "de" -> "Gespeicherte Router"
            "es" -> "Enrutadores guardados"
            "fr" -> "Routeurs enregistrés"
            "it" -> "Router salvati"
            "pt" -> "Roteadores salvos"
            "da" -> "Gemte routere"
            "fi" -> "Tallennetut reitittimet"
            "kk" -> "Сақталған роутерлер"
            "lt" -> "Išsaugoti maršrutizatoriai"
            "lv" -> "Saglabātie maršrutētāji"
            "ro" -> "Routere salvate"
            "sv" -> "Sparade routrar"
            "bg" -> "Запазени рутери"
            "cs" -> "Uložené routery"
            "el" -> "Αποθηκευμένοι δρομολογητές"
            "et" -> "Salvestatud ruuterid"
            "hu" -> "Mentett útválasztók"
            "pl" -> "Zapisane routery"
            "sk" -> "Uložené smerovače"
            "sl" -> "Shranjeni usmerjevalniki"
            "tr" -> "Kaydedilmiş yönlendiriciler"
            else -> "Saved routers"
        }
        "Сохранить профиль" -> when (lang) {
            "ru" -> "Сохранить профиль"
            "uk" -> "Зберегти профіль"
            "be" -> "Захаваць профіль"
            "de" -> "Profil speichern"
            "es" -> "Guardar perfil"
            "fr" -> "Enregistrer le profil"
            "it" -> "Salva profilo"
            "pt" -> "Salvar perfil"
            "da" -> "Gem profil"
            "fi" -> "Tallenna profiili"
            "kk" -> "Профильді сақтау"
            "lt" -> "Išsaugoti profilį"
            "lv" -> "Saglabāt profilu"
            "ro" -> "Salvează profil"
            "sv" -> "Spara profil"
            "bg" -> "Запазване на профила"
            "cs" -> "Uložit profil"
            "el" -> "Αποθήκευση προφίλ"
            "et" -> "Salvesta profiil"
            "hu" -> "Profil mentése"
            "pl" -> "Zapisz profil"
            "sk" -> "Uložiť profil"
            "sl" -> "Shrani profil"
            "tr" -> "Profili kaydet"
            else -> "Save profile"
        }
        "Удалить" -> when (lang) {
            "ru" -> "Удалить"
            "uk" -> "Видалити"
            "be" -> "Выдаліць"
            "de" -> "Löschen"
            "es" -> "Eliminar"
            "fr" -> "Supprimer"
            "it" -> "Elimina"
            "pt" -> "Excluir"
            "da" -> "Slet"
            "fi" -> "Poista"
            "kk" -> "Жою"
            "lt" -> "Ištrinti"
            "lv" -> "Dzēst"
            "ro" -> "Șterge"
            "sv" -> "Ta bort"
            "bg" -> "Изтриване"
            "cs" -> "Smazat"
            "el" -> "Διαγραφή"
            "et" -> "Kustuta"
            "hu" -> "Törlés"
            "pl" -> "Usuń"
            "sk" -> "Odstrániť"
            "sl" -> "Izbriši"
            "tr" -> "Sil"
            else -> "Delete"
        }
        "Connected" -> when (lang) {
            "ru" -> "Подключено"
            "uk" -> "Підключено"
            "be" -> "Падключана"
            "de" -> "Verbunden"
            "es" -> "Conectado"
            "fr" -> "Connecté"
            "it" -> "Connesso"
            "pt" -> "Conectado"
            "da" -> "Tilsluttet"
            "fi" -> "Yhdistetty"
            "kk" -> "Қосылды"
            "lt" -> "Prijungta"
            "lv" -> "Pievienots"
            "ro" -> "Conectat"
            "sv" -> "Ansluten"
            "bg" -> "Свързано"
            "cs" -> "Připojeno"
            "el" -> "Συνδέθηκε"
            "et" -> "Ühendatud"
            "hu" -> "Csatlakoztatva"
            "pl" -> "Połączono"
            "sk" -> "Pripojené"
            "sl" -> "Povezano"
            "tr" -> "Bağlandı"
            else -> "Connected"
        }
        "Connection failed. Please check IP, port, login, and password." -> when (lang) {
            "ru" -> "Ошибка подключения. Пожалуйста, проверьте IP, порт, логин и пароль."
            "uk" -> "Помилка підключення. Будь ласка, перевірте IP, порт, логін та пароль."
            "be" -> "Памылка падлучэння. Калі ласка, праверце IP, порт, лагін і пароль."
            "de" -> "Verbindung fehlgeschlagen. Bitte überprüfen Sie IP, Port, Login und Passwort."
            "es" -> "Conexión fallida. Por favor, compruebe la IP, el puerto, el nombre de usuario y la contraseña."
            "fr" -> "Échec de la connexion. Veuillez vérifier l'IP, le port, l'identifiant et le mot de passe."
            "it" -> "Connessione non riuscita. Controlla IP, porta, login e password."
            "pt" -> "Falha na conexão. Verifique o IP, porta, login e senha."
            "da" -> "Forbindelse mislykkedes. Tjek venligst IP, port, login og adgangskode."
            "fi" -> "Yhteys epäonnistui. Tarkista IP, portti, käyttäjätunnus ja salasana."
            "kk" -> "Қосылу қатесі. IP, порт, логин және құпия сөзді тексеріңіз."
            "lt" -> "Ryšys nepavyko. Patikrinkite IP, prievadą, prisijungimo vardą ir slaptažodį."
            "lv" -> "Savienojums neizdevās. Lūdzu, pārbaudiet IP, portu, pieteikšanās vārdu un paroli."
            "ro" -> "Conexiune eșuată. Vă rugăm să verificați IP-ul, portul, numele de utilizator și parola."
            "sv" -> "Anslutning misslyckades. Kontrollera IP, port, inloggning och lösenord."
            "bg" -> "Грешка при свързване. Моля, проверете IP, порт, потребителско име и парола."
            "cs" -> "Připojení selhalo. Zkontrolujte prosím IP, port, přihlašovací jméno a heslo."
            "el" -> "Αποτυχία σύνδεσης. Ελέγξτε την IP, τη θύρα, το όνομα χρήστη και τον κωδικό πρόσβασης."
            "et" -> "Ühendus ebaõnnestus. Palun kontrollige IP-d, porti, sisselogimist ja parooli."
            "hu" -> "A kapcsolat megszakadt. Kérjük, ellenőrizze az IP-t, portot, bejelentkezést és jelszót."
            "pl" -> "Błąd połączenia. Sprawdź IP, port, login i hasło."
            "sk" -> "Pripojenie zlyhalo. Skontrolujte prosím IP, port, prihlasovacie meno a heslo."
            "sl" -> "Povezava ni uspela. Preverite IP, vrata, prijavo in geslo."
            "tr" -> "Bağlantı başarısız. Lütfen IP, bağlant noktası, giriş ve şifreyi kontrol edin."
            else -> "Connection failed. Please check IP, port, login, and password."
        }
        else -> {
            if (raw.startsWith("Запущена (") && raw.endsWith(")")) {
                val inst = raw.substringAfter("(").substringBefore(")")
                context.getString(R.string.status_running_inst, inst)
            } else if (raw.startsWith("Интерфейс ") && raw.endsWith(" поднят")) {
                val iface = raw.substringAfter("Интерфейс ").substringBefore(" поднят")
                context.getString(R.string.status_wg_up, iface)
            } else if (raw.endsWith(" Остановлена")) {
                val service = raw.substringBefore(" Остановлена")
                context.getString(R.string.status_ovpn_stopped, service)
            } else if (raw.endsWith(" Запущена")) {
                val service = raw.substringBefore(" Запущена")
                context.getString(R.string.status_ovpn_running, service)
            } else if (raw.endsWith(" Выключен")) {
                val iface = raw.substringBefore(" Выключен")
                context.getString(R.string.status_wg_off, iface)
            } else {
                raw
            }
        }
    }
}

fun Modifier.drawScrollbar(state: androidx.compose.foundation.lazy.LazyListState): Modifier = this.drawWithContent {
    drawContent()
    val layoutInfo = state.layoutInfo
    val totalItemsCount = layoutInfo.totalItemsCount
    if (totalItemsCount > 0) {
        val visibleItems = layoutInfo.visibleItemsInfo
        if (visibleItems.isNotEmpty()) {
            val firstVisibleItem = visibleItems.first()
            val lastVisibleItem = visibleItems.last()
            val totalHeight = size.height
            val isScrollable = visibleItems.size < totalItemsCount || 
                               firstVisibleItem.offset < 0 || 
                               (lastVisibleItem.size + lastVisibleItem.offset) > totalHeight
            
            if (isScrollable) {
                val scrollProgress = state.firstVisibleItemIndex.toFloat() / (totalItemsCount - visibleItems.size).coerceAtLeast(1)
                val scrollbarHeight = (totalHeight * (visibleItems.size.toFloat() / totalItemsCount)).coerceIn(36.dp.toPx(), totalHeight)
                val scrollbarTopRange = totalHeight - scrollbarHeight
                val scrollbarTop = scrollProgress * scrollbarTopRange
                
                drawRoundRect(
                    color = Color.White.copy(alpha = 0.3f),
                    topLeft = androidx.compose.ui.geometry.Offset(size.width - 6.dp.toPx(), scrollbarTop),
                    size = Size(4.dp.toPx(), scrollbarHeight),
                    cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
                )
            }
        }
    }
}



@Composable
fun DeviceListDialog(
    title: String,
    config: com.example.data.RouterConfig?,
    devices: List<com.example.ui.DeviceSpeedInfo>,
    deviceHistory: Map<String, List<com.example.ui.DeviceSpeedInfo>>,
    onDismiss: () -> Unit,
    onNavigateToConsole: (String?) -> Unit = {},
    onRefreshCapabilities: () -> Unit = {}
) {
    androidx.compose.runtime.LaunchedEffect(Unit) {
        onRefreshCapabilities()
    }
    val configuration = androidx.compose.ui.platform.LocalConfiguration.current
    val context = androidx.compose.ui.platform.LocalContext.current
    val isTablet = configuration.screenWidthDp >= 600

    var selectedDevice by remember { mutableStateOf<com.example.ui.DeviceSpeedInfo?>(null) }
    var focusedDeviceMac by remember { mutableStateOf<String?>(null) }
    
    val listBg = MaterialTheme.colorScheme.surface
    val headerBg = androidx.compose.ui.graphics.lerp(listBg, Color.White, 0.08f)
    val sortedDevices = devices.sortedByDescending { it.downloadBytes + it.uploadBytes }
    val strWifi6 = stringResource(R.string.wifi_6_ghz)
    val strWifi5 = stringResource(R.string.wifi_5_ghz)
    val strWifi24 = stringResource(R.string.wifi_24_ghz)
    val strWifiOther = stringResource(R.string.wifi_other)
    val strEthernet = stringResource(R.string.ethernet)
    
    val groups = listOf(
        strWifi6 to sortedDevices.filter { it.connectionType == strWifi6 },
        strWifi5 to sortedDevices.filter { it.connectionType == strWifi5 },
        strWifi24 to sortedDevices.filter { it.connectionType == strWifi24 },
        strWifiOther to sortedDevices.filter { it.connectionType.contains("Wi-Fi") && it.connectionType != strWifi6 && it.connectionType != strWifi5 && it.connectionType != strWifi24 },
        strEthernet to sortedDevices.filter { it.connectionType == strEthernet }
    )
    val isTv = remember { context.packageManager.hasSystemFeature(android.content.pm.PackageManager.FEATURE_LEANBACK) }
    val firstDeviceFocusRequester = remember { androidx.compose.ui.focus.FocusRequester() }
    val firstRenderedDevice = remember(groups) { groups.flatMap { it.second }.firstOrNull() }

    var initialFocusRequested by remember { mutableStateOf(false) }
    androidx.compose.runtime.LaunchedEffect(isTv, firstRenderedDevice) {
        if (isTv && firstRenderedDevice != null && !initialFocusRequested) {
            initialFocusRequested = true
            kotlinx.coroutines.delay(100) // Give UI time to compose
            try {
                firstDeviceFocusRequester.requestFocus()
            } catch (e: Exception) {}
        }
    }

    if (isTablet) {
        androidx.compose.ui.window.Dialog(
            onDismissRequest = onDismiss,
            properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Row(modifier = Modifier.fillMaxSize().background(listBg)) {
                Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(headerBg)
                            .padding(horizontal = 24.dp, vertical = 24.dp)
                    ) {
                        Text(
                            text = title,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(R.string.msg_press_device_graph),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    CapabilitiesBanner(config, onNavigateToConsole)
                    if (sortedDevices.isEmpty()) {
                        Text(
                            text = stringResource(R.string.msg_no_devices),
                            modifier = Modifier.padding(24.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    } else {

                        androidx.compose.foundation.lazy.LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp)) {
                            for ((groupName, devicesInGroup) in groups) {
                                if (groupName.startsWith("Wi-Fi") && devicesInGroup.isEmpty()) continue
                                
                                item {
                                    val icon = if (groupName == stringResource(R.string.ethernet)) androidx.compose.material.icons.Icons.Default.Share else androidx.compose.material.icons.Icons.Default.Wifi
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).clip(RoundedCornerShape(8.dp)).background(MaterialTheme.colorScheme.surfaceVariant).padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        androidx.compose.material3.Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(groupName, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                            Text(String.format(stringResource(R.string.msg_devices_connected), devicesInGroup.size), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                    }
                                }
                                
                                if (groupName == strEthernet && devicesInGroup.isEmpty()) {
                                    item {
                                        Text(stringResource(R.string.msg_no_ethernet), modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                } else {
                                    itemsIndexed(devicesInGroup) { index, device ->
                                        var isFocused by remember { mutableStateOf(false) }
                                        Row(
                                            modifier = Modifier
                                                .then(if (device.mac == firstRenderedDevice?.mac) Modifier.focusRequester(firstDeviceFocusRequester) else Modifier)
                                                .fillMaxWidth()
                                                .onFocusChanged { 
                                                    isFocused = it.isFocused
                                                    if (it.isFocused) {
                                                        focusedDeviceMac = device.mac
                                                    }
                                                }
                                                .clickable { focusedDeviceMac = device.mac }
                                                .padding(vertical = 7.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(if (isFocused) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent)
                                                .padding(vertical = 5.dp, horizontal = 12.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = device.hostname,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isFocused) Color.Red else MaterialTheme.colorScheme.onSurface
                                                )
                                                Text("IP: ${device.ip}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                                Text("MAC: ${device.mac}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                            }
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Column(horizontalAlignment = Alignment.End, modifier = Modifier.padding(start = 8.dp)) {
                                                val dlColor = if (androidx.compose.foundation.isSystemInDarkTheme()) Color(0xFF00E676) else Color(0xFF2E7D32)
                                                val ulColor = Color(0xFFFFB300)
                                                if (device.downloadBytes == -1L && device.uploadBytes == -1L) {
                                                    Text("—", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                                } else {
                                                    Text(String.format(stringResource(R.string.format_speed_dl), device.downloadSpeedMbps), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = dlColor)
                                                    Text(String.format(stringResource(R.string.format_speed_ul), device.uploadSpeedMbps), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = ulColor)
                                                    
                                                    val totalBytes = device.downloadBytes + device.uploadBytes
                                                    val trafficStr = if (totalBytes < 1024 * 1024) {
                                                        String.format(stringResource(R.string.format_bytes_kb), totalBytes / 1024f)
                                                    } else if (totalBytes < 1024 * 1024 * 1024) {
                                                        String.format(stringResource(R.string.format_bytes_mb), totalBytes / (1024f * 1024f))
                                                    } else {
                                                        String.format(stringResource(R.string.format_bytes_gb), totalBytes / (1024f * 1024f * 1024f))
                                                    }
                                                    Text(androidx.compose.ui.res.stringResource(R.string.traffic_label) + " " + trafficStr, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                                }
                                            }
                                        }
                                        if (index < devicesInGroup.size - 1) {
                                            androidx.compose.material3.HorizontalDivider(
                                                modifier = Modifier.padding(horizontal = 4.dp),
                                                color = androidx.compose.ui.graphics.lerp(listBg, Color.White, 0.12f),
                                                thickness = 1.dp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                
                val currentMac = focusedDeviceMac ?: sortedDevices.firstOrNull()?.mac
                val rightPaneDevice = sortedDevices.find { it.mac == currentMac }
                
                androidx.compose.material3.VerticalDivider(
                    modifier = Modifier.fillMaxHeight().width(2.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                )
                
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(listBg)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (rightPaneDevice != null) {
                        val history = deviceHistory[rightPaneDevice.mac] ?: listOf(rightPaneDevice)
                        val scrollState = rememberScrollState()
                        Column(modifier = Modifier.fillMaxWidth().verticalScroll(scrollState).focusable()) {
                            Text(rightPaneDevice.hostname, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("${rightPaneDevice.formattedConnectionType(context)} • ${rightPaneDevice.ip}", style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            val dlColor = if (androidx.compose.foundation.isSystemInDarkTheme()) Color(0xFF00E676) else Color(0xFF2E7D32)
                            val ulColor = Color(0xFFFFB300)
                            if (rightPaneDevice.wifiRxBitrate != null && rightPaneDevice.wifiTxBitrate != null) {
                                Text(String.format(stringResource(R.string.msg_link_speed), rightPaneDevice.wifiRxBitrate ?: "—", rightPaneDevice.wifiTxBitrate ?: "—"), color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                            }
                            if (rightPaneDevice.portMappingConfidence == com.example.data.PortMappingConfidence.APPROXIMATE) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(stringResource(R.string.msg_shared_traffic_warning), color = MaterialTheme.colorScheme.tertiary, style = MaterialTheme.typography.bodySmall)
                            }
                            if (rightPaneDevice.downloadBytes == -1L && rightPaneDevice.uploadBytes == -1L) {
                                Text(stringResource(R.string.msg_traffic_unavailable), color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(stringResource(R.string.msg_install_nlbwmon), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(modifier = Modifier.height(8.dp))
                                val clipboard = androidx.compose.ui.platform.LocalClipboardManager.current
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "opkg update && opkg install nlbwmon",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.weight(1f)
                                    )
                                    var btnFocused by remember { mutableStateOf(false) }
                                    androidx.compose.material3.IconButton(
                                        onClick = {
                                            clipboard.setText(androidx.compose.ui.text.AnnotatedString("opkg update && opkg install nlbwmon"))
                                            onNavigateToConsole("opkg update && opkg install nlbwmon")
                                        },
                                        modifier = Modifier.onFocusChanged { btnFocused = it.isFocused }.focusable().background(if (btnFocused) MaterialTheme.colorScheme.primary.copy(alpha=0.2f) else Color.Transparent, shape = androidx.compose.foundation.shape.CircleShape)
                                    ) {
                                        androidx.compose.material3.Icon(
                                            imageVector = androidx.compose.material.icons.Icons.Default.ContentCopy,
                                            contentDescription = "Copy",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            } else {
                                Text(
                                    text = String.format(stringResource(R.string.format_download_speed), rightPaneDevice.downloadSpeedMbps),
                                    color = dlColor,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = String.format(stringResource(R.string.format_upload_speed), rightPaneDevice.uploadSpeedMbps),
                                    color = ulColor,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(24.dp))

                            val strKb = stringResource(R.string.format_bytes_kb)
val strMb = stringResource(R.string.format_bytes_mb)
val strGb = stringResource(R.string.format_bytes_gb)
val formatBytes = { bytes: Long ->
    if (bytes < 1024 * 1024) {
        String.format(strKb, bytes / 1024f)
    } else if (bytes < 1024 * 1024 * 1024) {
        String.format(strMb, bytes / (1024f * 1024f))
    } else {
        String.format(strGb, bytes / (1024f * 1024f * 1024f))
    }
}
                            
                            androidx.compose.foundation.layout.Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(stringResource(R.string.label_today_colon), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("↓ ", style = MaterialTheme.typography.bodySmall, color = dlColor, fontWeight = FontWeight.Bold)
                                Text("${formatBytes(rightPaneDevice.downloadMonthBytes)} ", style = MaterialTheme.typography.bodySmall, color = dlColor, fontWeight = FontWeight.Bold)
                                Text("↑ ", style = MaterialTheme.typography.bodySmall, color = ulColor, fontWeight = FontWeight.Bold)
                                Text("${formatBytes(rightPaneDevice.uploadMonthBytes)}", style = MaterialTheme.typography.bodySmall, color = ulColor, fontWeight = FontWeight.Bold)
                            }
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            val convertedHistory = history.map { 
                                com.example.ui.SpeedSnapshot(
                                    downloadSpeedMbps = it.downloadSpeedMbps,
                                    uploadSpeedMbps = it.uploadSpeedMbps,
                                    cpuUsagePercent = 0f
                                )
                            }
                            
                            Box(modifier = Modifier.fillMaxWidth().height(250.dp)) {
                                SpeedChart(
                                    history = convertedHistory,
                                    modifier = Modifier.fillMaxWidth().height(250.dp).clip(RoundedCornerShape(8.dp)).background(Color.Black.copy(alpha = 0.15f))
                                )
                            }
                        }
                    } else {
                        Text(stringResource(R.string.msg_no_selected_device), color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    } else {
        if (selectedDevice != null) {
            val history = deviceHistory[selectedDevice!!.mac] ?: listOf(selectedDevice!!)
            DeviceSpeedDialog(
                device = selectedDevice!!,
                history = history,
                onDismiss = { selectedDevice = null },
                onNavigateToConsole = onNavigateToConsole
            )
        } else {
            androidx.compose.ui.window.Dialog(
                onDismissRequest = onDismiss,
                properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
            ) {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(androidx.compose.foundation.layout.WindowInsets.systemBars)
                        .padding(top = 0.dp, bottom = 120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = listBg),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                    Column {
                        // Header
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(headerBg)
                                .padding(horizontal = 12.dp, vertical = 16.dp)
                        ) {
                            Text(
                                text = title,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = stringResource(R.string.msg_press_device_graph),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        CapabilitiesBanner(config, onNavigateToConsole)
                        // Content
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f, fill = false)
                                .padding(horizontal = 12.dp)
                        ) {
                            if (sortedDevices.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.msg_no_devices),
                                    modifier = Modifier.padding(vertical = 16.dp),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            } else {
                                val strWifi6 = stringResource(R.string.wifi_6_ghz)
    val strWifi5 = stringResource(R.string.wifi_5_ghz)
    val strWifi24 = stringResource(R.string.wifi_24_ghz)
    val strWifiOther = stringResource(R.string.wifi_other)
    val strEthernet = stringResource(R.string.ethernet)
    
    val groups = listOf(
        strWifi6 to sortedDevices.filter { it.connectionType == strWifi6 },
        strWifi5 to sortedDevices.filter { it.connectionType == strWifi5 },
        strWifi24 to sortedDevices.filter { it.connectionType == strWifi24 },
        strWifiOther to sortedDevices.filter { it.connectionType.contains("Wi-Fi") && it.connectionType != strWifi6 && it.connectionType != strWifi5 && it.connectionType != strWifi24 },
        strEthernet to sortedDevices.filter { it.connectionType == strEthernet }
    )
                                androidx.compose.foundation.lazy.LazyColumn {
                                    for ((groupName, devicesInGroup) in groups) {
                                        if (groupName.startsWith("Wi-Fi") && devicesInGroup.isEmpty()) continue
                                        
                                        item {
                                            val icon = if (groupName == stringResource(R.string.ethernet)) androidx.compose.material.icons.Icons.Default.Share else androidx.compose.material.icons.Icons.Default.Wifi
                                            Row(
                                                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).clip(RoundedCornerShape(8.dp)).background(MaterialTheme.colorScheme.surfaceVariant).padding(12.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                androidx.compose.material3.Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Column {
                                                    Text(groupName, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                                    Text(String.format(stringResource(R.string.msg_devices_connected), devicesInGroup.size), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                                }
                                            }
                                        }
                                        
                                        if (groupName == strEthernet && devicesInGroup.isEmpty()) {
                                            item {
                                                Text(stringResource(R.string.msg_no_ethernet), modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                            }
                                        } else {
                                            itemsIndexed(devicesInGroup) { index, device ->
                                                var isFocused by remember { mutableStateOf(false) }
                                                                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .clip(RoundedCornerShape(8.dp))
                                                        .clickable { selectedDevice = device }
                                                        .onFocusChanged { isFocused = it.isFocused }
                                                        .focusable()
                                                        .background(if (isFocused) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent)
                                                        .padding(vertical = 12.dp, horizontal = 4.dp),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Column(modifier = Modifier.weight(1f)) {
                                                        Text(device.hostname, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                                                        Text("IP: ${device.ip}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                                        Text("MAC: ${device.mac}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                                    }
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Column(horizontalAlignment = Alignment.End, modifier = Modifier.padding(start = 8.dp)) {
                                                        val dlColor = if (androidx.compose.foundation.isSystemInDarkTheme()) Color(0xFF00E676) else Color(0xFF2E7D32)
                                                        val ulColor = Color(0xFFFFB300)
                                                        if (device.downloadBytes == -1L && device.uploadBytes == -1L) {
                                                            Text("—", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                                        } else {
                                                            Text(String.format(stringResource(R.string.format_speed_dl), device.downloadSpeedMbps), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = dlColor)
                                                            Text(String.format(stringResource(R.string.format_speed_ul), device.uploadSpeedMbps), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = ulColor)
                                                            
                                                            val totalBytes = device.downloadBytes + device.uploadBytes
                                                            val trafficStr = if (totalBytes < 1024 * 1024) {
                                                                String.format(stringResource(R.string.format_bytes_kb), totalBytes / 1024f)
                                                            } else if (totalBytes < 1024 * 1024 * 1024) {
                                                                String.format(stringResource(R.string.format_bytes_mb), totalBytes / (1024f * 1024f))
                                                            } else {
                                                                String.format(stringResource(R.string.format_bytes_gb), totalBytes / (1024f * 1024f * 1024f))
                                                            }
                                                            Text(androidx.compose.ui.res.stringResource(R.string.traffic_label) + " " + trafficStr, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                                        }
                                                    }
                                                }
                                                if (index < devicesInGroup.size - 1) {
                                                    androidx.compose.material3.HorizontalDivider(
                                                        modifier = Modifier.padding(horizontal = 4.dp),
                                                        color = androidx.compose.ui.graphics.lerp(listBg, Color.White, 0.12f),
                                                        thickness = 1.dp
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        // Bottom Panel
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(headerBg)
                                .height(52.dp)
                                .padding(horizontal = 12.dp),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            var isFocused by remember { mutableStateOf(false) }
                            androidx.compose.material3.TextButton(
                                onClick = onDismiss,
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                                modifier = Modifier
                                    .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp)
                                    .onFocusChanged { isFocused = it.isFocused }
                                    .focusable()
                                    .background(if (isFocused) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent, RoundedCornerShape(50))
                            ) {
                                Text(stringResource(R.string.action_close))
                            }
                        }
                    }
                }
                }
            }
        }
    }
}


@Composable
fun CapabilitiesBanner(config: com.example.data.RouterConfig?, onNavigateToConsole: (String?) -> Unit = {}, onRefreshCapabilities: () -> Unit = {}) {
    if (config == null) return
    val caps = config.capabilities
    if (caps.switchArchitecture == com.example.data.SwitchArchitecture.UNKNOWN) return
    
    val context = androidx.compose.ui.platform.LocalContext.current
    val prefs = remember { context.getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE) }
    var isDismissed by remember { mutableStateOf(prefs.getBoolean("banner_dismissed_v2", false)) }
    
    if (isDismissed) return

    val lang = context.resources.configuration.locales[0].language
    val messages = mutableListOf<String>()
    val actions = mutableListOf<String>()
    
    if (!caps.hasBridgeUtil) {
        messages.add(com.example.TestTabLocalizations.getBannerNoBridge(lang))
        actions.add("ip-bridge")
    }
    if (!caps.hasSwconfigUtil && caps.switchArchitecture != com.example.data.SwitchArchitecture.DSA) {
        messages.add(com.example.TestTabLocalizations.getBannerNoSwconfig(lang))
        actions.add("swconfig")
    }
    
    if (caps.switchArchitecture == com.example.data.SwitchArchitecture.SWCONFIG) {
        if (!caps.hasBoardJsonWithSwitchSection) {
            messages.add(com.example.TestTabLocalizations.getBannerNoBoardJson(lang))
        }
    } else if (caps.switchArchitecture == com.example.data.SwitchArchitecture.UNSUPPORTED) {
        messages.add(com.example.TestTabLocalizations.getBannerUnsupported(lang, caps.switchArchitecture.toString()))
    }
    
    val message = messages.joinToString("\n\n")
    
    if (message.isNotEmpty()) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(message, color = MaterialTheme.colorScheme.onErrorContainer, style = MaterialTheme.typography.bodyMedium)
                if (actions.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(com.example.TestTabLocalizations.getBannerInstallInstruction(lang), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onErrorContainer)
                    val clipboard = androidx.compose.ui.platform.LocalClipboardManager.current
                    for (act in actions) {
                        val cmd = if (act == "ip-bridge") "opkg update && opkg install ip-bridge" else if (act == "swconfig") "opkg update && opkg install swconfig" else ""
                        if (cmd.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = cmd,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace),
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    modifier = Modifier.weight(1f)
                                )
                                var btnFocused by remember { mutableStateOf(false) }
                                androidx.compose.material3.IconButton(
                                    onClick = {
                                        clipboard.setText(androidx.compose.ui.text.AnnotatedString(cmd))
                                        onNavigateToConsole(cmd)
                                    },
                                    modifier = Modifier.onFocusChanged { btnFocused = it.isFocused }.focusable().background(if (btnFocused) MaterialTheme.colorScheme.primary.copy(alpha=0.2f) else Color.Transparent, shape = androidx.compose.foundation.shape.CircleShape)
                                ) {
                                    androidx.compose.material3.Icon(
                                        imageVector = androidx.compose.material.icons.Icons.Default.ContentCopy,
                                        contentDescription = "Copy",
                                        tint = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                androidx.compose.material3.Button(
                    onClick = {
                        prefs.edit().putBoolean("banner_dismissed_v2", true).apply()
                        isDismissed = true
                    },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onErrorContainer, 
                        contentColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(com.example.TestTabLocalizations.getBannerGotIt(lang))
                }
            }
        }
    }
}

@Composable
fun DeviceSpeedDialog(
    device: com.example.ui.DeviceSpeedInfo,
    history: List<com.example.ui.DeviceSpeedInfo>,
    onDismiss: () -> Unit,
    onNavigateToConsole: (String?) -> Unit = {}, onRefreshCapabilities: () -> Unit = {}
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val currentDevice = history.lastOrNull() ?: device
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(currentDevice.hostname) },
        text = {
            val scrollState = rememberScrollState()
                        Column(modifier = Modifier.fillMaxWidth().verticalScroll(scrollState).focusable()) {
                Text("${currentDevice.formattedConnectionType(context)} • ${currentDevice.ip}", style = MaterialTheme.typography.bodyMedium)
                Text("MAC: ${currentDevice.mac}", style = MaterialTheme.typography.bodyMedium)
    
                
                val dlColor = if (androidx.compose.foundation.isSystemInDarkTheme()) Color(0xFF00E676) else Color(0xFF2E7D32)
                val ulColor = Color(0xFFFFB300)
                if (currentDevice.wifiRxBitrate != null && currentDevice.wifiTxBitrate != null) {
                    Text(String.format(stringResource(R.string.msg_link_speed), currentDevice.wifiRxBitrate ?: "—", currentDevice.wifiTxBitrate ?: "—"), color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                }
                if (currentDevice.portMappingConfidence == com.example.data.PortMappingConfidence.APPROXIMATE) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(stringResource(R.string.msg_shared_traffic_warning), color = MaterialTheme.colorScheme.tertiary, style = MaterialTheme.typography.bodySmall)
                }
                
                if (currentDevice.downloadBytes == -1L && currentDevice.uploadBytes == -1L) {
                    Text(stringResource(R.string.msg_traffic_unavailable), color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(stringResource(R.string.msg_install_nlbwmon), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(8.dp))
                    val clipboard = androidx.compose.ui.platform.LocalClipboardManager.current
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "opkg update && opkg install nlbwmon",
                            style = MaterialTheme.typography.bodyMedium.copy(fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f)
                        )
                        var btnFocused by remember { mutableStateOf(false) }
                        androidx.compose.material3.IconButton(
                            onClick = {
                                clipboard.setText(androidx.compose.ui.text.AnnotatedString("opkg update && opkg install nlbwmon"))
                                onNavigateToConsole("opkg update && opkg install nlbwmon")
                            },
                            modifier = Modifier.onFocusChanged { btnFocused = it.isFocused }.focusable().background(if (btnFocused) MaterialTheme.colorScheme.primary.copy(alpha=0.2f) else Color.Transparent, shape = androidx.compose.foundation.shape.CircleShape)
                        ) {
                            androidx.compose.material3.Icon(
                                imageVector = androidx.compose.material.icons.Icons.Default.ContentCopy,
                                contentDescription = "Copy",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                } else {
                    Text(
                        text = String.format(stringResource(R.string.format_download_speed), currentDevice.downloadSpeedMbps),
                        color = dlColor,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = String.format(stringResource(R.string.format_upload_speed), currentDevice.uploadSpeedMbps),
                        color = ulColor,
                        fontWeight = FontWeight.Bold
                    )
                }
                
    

                val strKb = stringResource(R.string.format_bytes_kb)
val strMb = stringResource(R.string.format_bytes_mb)
val strGb = stringResource(R.string.format_bytes_gb)
val formatBytes = { bytes: Long ->
    if (bytes < 1024 * 1024) {
        String.format(strKb, bytes / 1024f)
    } else if (bytes < 1024 * 1024 * 1024) {
        String.format(strMb, bytes / (1024f * 1024f))
    } else {
        String.format(strGb, bytes / (1024f * 1024f * 1024f))
    }
}
                
                androidx.compose.foundation.layout.Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(stringResource(R.string.label_today_colon), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("↓ ", style = MaterialTheme.typography.bodySmall, color = dlColor, fontWeight = FontWeight.Bold)
                    Text("${formatBytes(currentDevice.downloadMonthBytes)} ", style = MaterialTheme.typography.bodySmall, color = dlColor, fontWeight = FontWeight.Bold)
                    Text("↑ ", style = MaterialTheme.typography.bodySmall, color = ulColor, fontWeight = FontWeight.Bold)
                    Text("${formatBytes(currentDevice.uploadMonthBytes)}", style = MaterialTheme.typography.bodySmall, color = ulColor, fontWeight = FontWeight.Bold)
                }
                
    
                
                val convertedHistory = history.map { 
                    com.example.ui.SpeedSnapshot(
                        downloadSpeedMbps = it.downloadSpeedMbps,
                        uploadSpeedMbps = it.uploadSpeedMbps,
                        cpuUsagePercent = 0f
                    )
                }
                
                Box(modifier = Modifier.fillMaxWidth().height(150.dp)) {
                    SpeedChart(
                        history = convertedHistory,
                        modifier = Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(8.dp)).background(Color.Black.copy(alpha = 0.15f))
                    )
                }
            }
        },
        confirmButton = {
            var isFocused by remember { mutableStateOf(false) }
            androidx.compose.material3.TextButton(
                onClick = onDismiss,
                modifier = Modifier
                    .onFocusChanged { isFocused = it.isFocused }
                    .focusable()
                    .background(if (isFocused) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent, RoundedCornerShape(50))
            ) {
                Text(stringResource(R.string.action_close))
            }
        }
    )
}

