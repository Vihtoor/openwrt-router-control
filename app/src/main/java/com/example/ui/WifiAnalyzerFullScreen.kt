package com.example.ui

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.TestTabLocalizations
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.pow
import kotlin.random.Random

enum class WifiBand {
    BAND_2_4_GHZ,
    BAND_5_GHZ,
    BAND_6_GHZ
}

data class WifiChannelNetwork(
    val ssid: String,
    val centerChannel: Int,
    val widthMhz: Int,
    val baseDbm: Float,
    var currentDbm: State<Float>,
    val color: Color,
    val isConnected: Boolean,
    val band: WifiBand = WifiBand.BAND_2_4_GHZ,
    val frequency: Int = 2412,
    val bssid: String = ""
)

data class TagBound(
    val netSsid: String,
    val netBssid: String,
    val centerChannel: Int,
    val band: WifiBand,
    val rect: Rect
)

data class DragSession(
    val netSsid: String,
    val netBssid: String,
    val startChannel: Int,
    val band: WifiBand,
    val currentDragChannel: Int
)

fun WifiChannelNetwork.belongsToSameRouter(connectedSsid: String?, connectedBssid: String?): Boolean {
    if (this.isConnected) return true
    if (connectedSsid == null) return false
    
    // 1. Check BSSID (MAC Address similarity)
    if (this.bssid.isNotBlank() && !connectedBssid.isNullOrBlank()) {
        val mac1 = this.bssid.lowercase().replace(":", "")
        val mac2 = connectedBssid.lowercase().replace(":", "")
        if (mac1.length >= 8 && mac2.length >= 8) {
            // First 4 octets (8 hex chars) represent the same physical dual/tri-band router chassis
            if (mac1.substring(0, 8) == mac2.substring(0, 8)) {
                return true
            }
        }
    }
    
    // 2. Check SSID (Name prefix/base name similarity)
    val s1 = this.ssid.lowercase().removeSuffix("*").trim()
    val s2 = connectedSsid.lowercase().removeSuffix("*").trim()
    if (s1.isEmpty() || s2.isEmpty()) return false
    if (s1 == s2) return true
    
    fun clean(s: String): String {
        var res = s
        val suffixes = listOf(
            "_5g", "_2.4g", "_2g", "_6g", "_ext", 
            "-5g", "-2.4g", "-2g", "-6g", "_guest",
            " 5g", " 2.4g", " 2g", " 6g", " ext"
        )
        var changed = true
        while (changed) {
            changed = false
            for (suff in suffixes) {
                if (res.endsWith(suff)) {
                    res = res.removeSuffix(suff)
                    changed = true
                }
            }
        }
        return res
    }
    
    val c1 = clean(s1)
    val c2 = clean(s2)
    if (c1.isEmpty() || c2.isEmpty()) return false
    if (c1 == c2) return true
    
    // Sibling prefix/suffix check (e.g. "Xiaomi_AX3000T" and "Xiaomi_AX3000T_5G")
    if (c1.length >= 4 && c2.length >= 4) {
        if (c1.startsWith(c2) || c2.startsWith(c1) || c1.endsWith(c2) || c2.endsWith(c1)) {
            return true
        }
    }
    
    return false
}

fun getStandardCenterChannel(controlChannel: Int, widthMhz: Int, band: WifiBand): Int {
    if (widthMhz <= 20) return controlChannel
    
    return when (band) {
        WifiBand.BAND_2_4_GHZ -> {
            if (widthMhz == 40) {
                if (controlChannel <= 7) controlChannel + 2 else controlChannel - 2
            } else {
                controlChannel
            }
        }
        WifiBand.BAND_5_GHZ -> {
            when (widthMhz) {
                40 -> {
                    val blocks = listOf(
                        listOf(36, 40) to 38,
                        listOf(44, 48) to 46,
                        listOf(52, 56) to 54,
                        listOf(60, 64) to 62,
                        listOf(100, 104) to 102,
                        listOf(108, 112) to 110,
                        listOf(116, 120) to 118,
                        listOf(124, 128) to 126,
                        listOf(132, 136) to 134,
                        listOf(140, 144) to 142,
                        listOf(149, 153) to 151,
                        listOf(157, 161) to 159,
                        listOf(165, 169) to 167
                    )
                    val match = blocks.firstOrNull { it.first.contains(controlChannel) }
                    match?.second ?: controlChannel
                }
                80 -> {
                    val blocks = listOf(
                        listOf(36, 40, 44, 48) to 42,
                        listOf(52, 56, 60, 64) to 58,
                        listOf(100, 104, 108, 112) to 106,
                        listOf(116, 120, 124, 128) to 122,
                        listOf(132, 136, 140, 144) to 138,
                        listOf(149, 153, 157, 161) to 155
                    )
                    val match = blocks.firstOrNull { it.first.contains(controlChannel) }
                    match?.second ?: controlChannel
                }
                160 -> {
                    val blocks = listOf(
                        listOf(36, 40, 44, 48, 52, 56, 60, 64) to 50,
                        listOf(100, 104, 108, 112, 116, 120, 124, 128) to 114
                    )
                    val match = blocks.firstOrNull { it.first.contains(controlChannel) }
                    match?.second ?: controlChannel
                }
                else -> controlChannel
            }
        }
        WifiBand.BAND_6_GHZ -> {
            val k = (controlChannel - 1) / 4
            when (widthMhz) {
                40 -> {
                    val start = (k / 2 * 2) * 4 + 1
                    start + 2
                }
                80 -> {
                    val start = (k / 4 * 4) * 4 + 1
                    start + 6
                }
                160 -> {
                    val start = (k / 8 * 8) * 4 + 1
                    start + 14
                }
                320 -> {
                    val start = (k / 16 * 16) * 4 + 1
                    start + 30
                }
                else -> controlChannel
            }
        }
    }
}

fun get5GhzPositionPercent(chan: Float): Float {
    val channels = floatArrayOf(18f, 36f, 40f, 44f, 48f, 52f, 56f, 60f, 64f, 100f, 116f, 132f, 149f, 165f, 181f)
    val positions = floatArrayOf(0.00f, 0.10f, 0.18f, 0.26f, 0.34f, 0.42f, 0.50f, 0.58f, 0.66f, 0.74f, 0.80f, 0.86f, 0.92f, 0.98f, 1.00f)
    
    if (chan <= channels.first()) return positions.first()
    if (chan >= channels.last()) return positions.last()
    
    var i = 0
    while (i < channels.size - 1 && chan > channels[i + 1]) {
        i++
    }
    
    val cMin = channels[i]
    val cMax = channels[i + 1]
    val pMin = positions[i]
    val pMax = positions[i + 1]
    
    val t = (chan - cMin) / (cMax - cMin)
    return pMin + t * (pMax - pMin)
}

// Precision distance logarithmic model based on screenshot RSSIs
fun estimateDistance(rssi: Float): Int {
    val absR = kotlin.math.abs(rssi)
    val dist = if (absR <= 42f) {
        1.0
    } else {
        10.0.pow((absR - 42.0) * 0.051)
    }
    return dist.toInt().coerceIn(1, 300)
}

@Composable
fun WifiSignalIcon(tint: Color = Color.White, modifier: Modifier = Modifier.size(24.dp)) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val strokeWidth = 2.5.dp.toPx()
        
        // Dot at bottom center
        drawCircle(
            color = tint,
            radius = 2.5.dp.toPx(),
            center = Offset(w / 2f, h - 3.dp.toPx())
        )
        
        // Arc 1 (small)
        drawArc(
            color = tint,
            startAngle = 225f,
            sweepAngle = 90f,
            useCenter = false,
            topLeft = Offset(w / 2f - 6.dp.toPx(), h - 13.dp.toPx()),
            size = Size(12.dp.toPx(), 12.dp.toPx()),
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
        
        // Arc 2 (medium)
        drawArc(
            color = tint,
            startAngle = 225f,
            sweepAngle = 90f,
            useCenter = false,
            topLeft = Offset(w / 2f - 11.dp.toPx(), h - 19.dp.toPx()),
            size = Size(22.dp.toPx(), 22.dp.toPx()),
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
        
        // Arc 3 (large)
        drawArc(
            color = tint,
            startAngle = 225f,
            sweepAngle = 90f,
            useCenter = false,
            topLeft = Offset(w / 2f - 16.dp.toPx(), h - 25.dp.toPx()),
            size = Size(32.dp.toPx(), 32.dp.toPx()),
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
    }
}

@Composable
fun BandTab(
    number: String,
    unit: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    focusRequester: FocusRequester,
    onFocusChanged: (Boolean) -> Unit,
    isFocused: Boolean
) {
    val backgroundColor = if (isSelected) Color(0xFF007AFF) else Color(0xFF1C1C1E)
    val contentColor = if (isSelected) Color.White else Color(0xFFE5E5EA)
    val alpha = if (isSelected) 1f else 0.6f
    val shape = RoundedCornerShape(50.dp)
    
    Row(
        modifier = Modifier
            .clip(shape)
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .focusRequester(focusRequester)
            .onFocusChanged { onFocusChanged(it.isFocused) }
            .border(
                width = if (isFocused) 3.dp else 1.dp,
                color = if (isFocused) Color(0xFF5AC8FA) else if (isSelected) Color(0xFF007AFF) else Color(0xFF3A3A3C),
                shape = shape
            )
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = number,
            style = TextStyle(
                color = contentColor,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
            text = unit,
            style = TextStyle(
                color = contentColor.copy(alpha = alpha),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTextApi::class)
@Composable
fun WifiAnalyzerFullScreen(
    onClose: () -> Unit,
    viewModel: com.example.ui.RouterViewModel? = null
) {
    val context = LocalContext.current
    val locale = remember { context.resources.configuration.locales[0].language }
    val isTv = remember {
        context.packageManager.hasSystemFeature(android.content.pm.PackageManager.FEATURE_LEANBACK)
    }
    val scope = rememberCoroutineScope()

    val wifiManager = remember { context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager }
    val connectivityManager = remember { context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }

    var selectedBand by remember { mutableStateOf(WifiBand.BAND_2_4_GHZ) }
    var selected6GhzSubband by remember { mutableStateOf(0) }
    var isFetchingBandSettings by remember { mutableStateOf(false) }
    var isWaitingForFirstScanInNewBand by remember { mutableStateOf(true) }
    val prefs = remember { context.getSharedPreferences("wifi_analyzer_prefs", Context.MODE_PRIVATE) }
    var activeHelpDialog by remember {
        val isFirstRun = prefs.getBoolean("first_run_help_shown", true)
        if (isFirstRun) {
            prefs.edit().putBoolean("first_run_help_shown", false).apply()
        }
        mutableStateOf(isFirstRun)
    }

    var activeConnectedSsid by remember { mutableStateOf<String?>(null) }
    var activeConnectedBssid by remember { mutableStateOf<String?>(null) }
    var activeConnectedBand by remember { mutableStateOf<WifiBand?>(null) }
    var lastKnownConnectedSsid by remember { mutableStateOf<String?>(null) }
    var lastKnownConnectedBssid by remember { mutableStateOf<String?>(null) }
    var lastKnownConnectedBand by remember { mutableStateOf<WifiBand?>(null) }
    val ssidOverriddenChannels = remember { mutableStateMapOf<String, Int>() }

    LaunchedEffect(activeConnectedSsid, activeConnectedBssid, activeConnectedBand) {
        if (!activeConnectedSsid.isNullOrBlank() && activeConnectedSsid != "<unknown ssid>" && !activeConnectedSsid!!.contains("unknown")) {
            lastKnownConnectedSsid = activeConnectedSsid
            lastKnownConnectedBand = activeConnectedBand
        }
        if (!activeConnectedBssid.isNullOrBlank()) {
            lastKnownConnectedBssid = activeConnectedBssid
        }
    }

    var onUpdateWifiData by remember { mutableStateOf<(() -> Unit)?>(null) }
    var activeDragSession by remember { mutableStateOf<DragSession?>(null) }
    var pendingChannelChange by remember { mutableStateOf<DragSession?>(null) }
    var isApplyingChannelChange by remember { mutableStateOf(false) }
    var applyChannelMessage by remember { mutableStateOf<String?>(null) }
    var ssidChangingChannel by remember { mutableStateOf<String?>(null) }
    var isWaitingForFirstScanAfterCommit by remember { mutableStateOf(false) }
    var lastChangeTime by remember { mutableStateOf(0L) }
    var ssidWaitingForScan by remember { mutableStateOf<String?>(null) }

    var activeSettingsSsid by remember { mutableStateOf<String?>(null) }
    var activeSettingsBssid by remember { mutableStateOf<String?>(null) }
    var activeSettingsBand by remember { mutableStateOf<WifiBand?>(null) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    var isFetchingSettings by remember { mutableStateOf(false) }
    var detectedIfaceSection by remember { mutableStateOf("") }
    var detectedDeviceSection by remember { mutableStateOf("") }
    var currentPassword by remember { mutableStateOf("") }
    var currentTxPower by remember { mutableStateOf("") }
    var settingsError by remember { mutableStateOf<String?>(null) }
    var targetChannelChangingChannel by remember { mutableStateOf<Int?>(null) }
    var bandChangingChannel by remember { mutableStateOf<WifiBand?>(null) }
    var currentChannelVal by remember { mutableStateOf(0) }
    var currentHtmode by remember { mutableStateOf("auto") }
    var currentCountry by remember { mutableStateOf("US") }
    var currentEncryption by remember { mutableStateOf("psk2") }
    var availableTxPowers by remember { mutableStateOf<List<String>>(listOf("auto") + (0..23 step 2).map { it.toString() }) }

    val tagBounds = remember { ArrayList<TagBound>() }

    val silentFetchRouterSsidSettings: (String) -> Unit = { ssid ->
        if (viewModel != null) {
            val radioDevice = when (selectedBand) {
                WifiBand.BAND_5_GHZ -> "radio1"
                WifiBand.BAND_6_GHZ -> "radio2"
                else -> "radio0"
            }
            val cmd = "for i in " + "$(seq 0 16)" + "; do " +
                      "s=" + "$(uci -q get wireless.@wifi-iface[\$i].ssid); " +
                      "d=" + "$(uci -q get wireless.@wifi-iface[\$i].device); " +
                      "if [ \"\$s\" = \"$ssid\" ] && [ \"\$d\" = \"$radioDevice\" ]; then " +
                      "c=" + "$(uci -q get wireless.\$radioDevice.channel); " +
                      "echo \"SUCCESS|\$c|wireless.@wifi-iface[\$i]|wireless.\$radioDevice\"; " +
                      "exit 0; " +
                      "fi; " +
                      "done; " +
                      "echo \"NOT_FOUND\""
            viewModel.executeConsoleCommandDirect(cmd) { response ->
                val clean = response.trim()
                if (clean.contains("SUCCESS|")) {
                    val parts = clean.split("|")
                    if (parts.size >= 4) {
                        val chan = parts[1].trim().toIntOrNull() ?: 0
                        if (chan > 0) {
                            currentChannelVal = chan
                        }
                        detectedIfaceSection = parts[2]
                        detectedDeviceSection = parts[3]
                    }
                }
            }
        }
    }

    val fetchSsidDataBeforeChange: (String, WifiBand, () -> Unit) -> Unit = { ssid, band, onComplete ->
        if (viewModel != null) {
            val radioDevice = when (band) {
                WifiBand.BAND_5_GHZ -> "radio1"
                WifiBand.BAND_6_GHZ -> "radio2"
                else -> "radio0"
            }
            val cmd = "for i in " + "$(seq 0 16)" + "; do " +
                      "s=" + "$(uci -q get wireless.@wifi-iface[\$i].ssid); " +
                      "d=" + "$(uci -q get wireless.@wifi-iface[\$i].device); " +
                      "if [ \"\$s\" = \"$ssid\" ] && [ \"\$d\" = \"$radioDevice\" ]; then " +
                      "k=" + "$(uci -q get wireless.@wifi-iface[\$i].key); " +
                      "e=" + "$(uci -q get wireless.@wifi-iface[\$i].encryption); " +
                      "p=" + "$(uci -q get wireless.\$d.txpower); " +
                      "c=" + "$(uci -q get wireless.\$d.channel); " +
                      "h=" + "$(uci -q get wireless.\$d.htmode); " +
                      "y=" + "$(uci -q get wireless.\$d.country); " +
                      "echo \"SUCCESS|wireless.@wifi-iface[\$i]|wireless.\$d|\$k|\$p|\$c|\$h|\$y|none|\$e\"; " +
                      "exit 0; " +
                      "fi; " +
                      "done; " +
                      "echo \"NOT_FOUND\""
            viewModel.executeConsoleCommandDirect(cmd) { response ->
                val clean = response.trim()
                if (clean.contains("SUCCESS|")) {
                    val parts = clean.split("|")
                    if (parts.size >= 10) {
                        detectedIfaceSection = parts[1].trim()
                        detectedDeviceSection = parts[2].trim()
                        currentPassword = parts[3]
                        currentTxPower = parts[4].ifEmpty { "auto" }
                        currentChannelVal = parts[5].trim().toIntOrNull() ?: 0
                        currentHtmode = parts[6].trim().ifEmpty { "auto" }
                        currentCountry = parts[7].trim().ifEmpty { "US" }
                        currentEncryption = parts[9].trim().ifEmpty { "psk2" }
                    }
                }
                onComplete()
            }
        } else {
            onComplete()
        }
    }

    val fetchRouterSsidSettingsForBand: (WifiBand, MutableList<WifiChannelNetwork>) -> Unit = { band, networks ->
        isFetchingBandSettings = true
        if (!isWaitingForFirstScanAfterCommit) {
            networks.clear()
        }
        if (viewModel != null) {
            val radioDevice = when (band) {
                WifiBand.BAND_5_GHZ -> "radio1"
                WifiBand.BAND_6_GHZ -> "radio2"
                else -> "radio0"
            }
            val cmd = "for i in " + "$(seq 0 16)" + "; do " +
                      "d=" + "$(uci -q get wireless.@wifi-iface[\$i].device); " +
                      "if [ \"\$d\" = \"$radioDevice\" ]; then " +
                      "s=" + "$(uci -q get wireless.@wifi-iface[\$i].ssid); " +
                      "c=" + "$(uci -q get wireless.$radioDevice.channel); " +
                      "echo \"SUCCESS|\$s|\$c|wireless.@wifi-iface[\$i]|wireless.$radioDevice\"; " +
                      "exit 0; " +
                      "fi; " +
                      "done; " +
                      "echo \"NOT_FOUND\""
            
            viewModel.executeConsoleCommandDirect(cmd) { response ->
                val clean = response.trim()
                if (clean.contains("SUCCESS|")) {
                    val parts = clean.split("|")
                    if (parts.size >= 5) {
                        val ssid = parts[1].trim()
                        val chan = parts[2].trim().toIntOrNull() ?: 0
                        if (chan > 0) {
                            currentChannelVal = chan
                        }
                        detectedIfaceSection = parts[3].trim()
                        detectedDeviceSection = parts[4].trim()
                        activeSettingsSsid = ssid
                        activeSettingsBand = band
                    }
                } else {
                    val routerNetwork = networks.find { it.isConnected || it.belongsToSameRouter(activeConnectedSsid ?: lastKnownConnectedSsid, activeConnectedBssid ?: lastKnownConnectedBssid) }
                    if (routerNetwork != null) {
                        silentFetchRouterSsidSettings(routerNetwork.ssid)
                        activeSettingsSsid = routerNetwork.ssid
                        activeSettingsBand = band
                    }
                }
                isFetchingBandSettings = false
                isWaitingForFirstScanInNewBand = false
                try {
                    wifiManager.startScan()
                } catch (e: Exception) {}
                onUpdateWifiData?.invoke()
            }
        } else {
            isFetchingBandSettings = false
            isWaitingForFirstScanInNewBand = false
            try {
                wifiManager.startScan()
            } catch (e: Exception) {}
            onUpdateWifiData?.invoke()
        }
    }

    val channels = remember(selectedBand, selected6GhzSubband) {
        when (selectedBand) {
            WifiBand.BAND_2_4_GHZ -> (1..14).toList()
            WifiBand.BAND_5_GHZ -> listOf(36, 40, 44, 48, 52, 56, 60, 64, 100, 116, 132, 149, 165)
            WifiBand.BAND_6_GHZ -> {
                when (selected6GhzSubband) {
                    0 -> (1..29 step 4).toList()
                    1 -> (33..61 step 4).toList()
                    2 -> (65..93 step 4).toList()
                    3 -> (97..125 step 4).toList()
                    4 -> (129..157 step 4).toList()
                    5 -> (161..189 step 4).toList()
                    else -> listOf(193, 197, 201, 205, 209, 213, 217, 221, 225, 229, 233)
                }
            }
        }
    }

    // Keep active connection info updated
    LaunchedEffect(Unit) {
        while (isActive) {
            try {
                val info = wifiManager.connectionInfo
                if (info != null && info.networkId != -1) {
                    activeConnectedBssid = info.bssid
                    val raw = info.ssid
                    if (raw != null && raw != "<unknown ssid>") {
                        activeConnectedSsid = raw.removeSurrounding("\"")
                    }
                } else {
                    activeConnectedBssid = null
                    activeConnectedSsid = null
                }
            } catch (e: Exception) {
                activeConnectedBssid = null
                activeConnectedSsid = null
            }
            delay(3000)
        }
    }

    // Focus requesters for TV
    val backButtonFocusRequester = remember { FocusRequester() }
    val infoButtonFocusRequester = remember { FocusRequester() }
    val band2FocusRequester = remember { FocusRequester() }
    val band5FocusRequester = remember { FocusRequester() }
    val band6FocusRequester = remember { FocusRequester() }

    var isBackFocused by remember { mutableStateOf(false) }
    var isInfoFocused by remember { mutableStateOf(false) }
    var isBand2Focused by remember { mutableStateOf(false) }
    var isBand5Focused by remember { mutableStateOf(false) }
    var isBand6Focused by remember { mutableStateOf(false) }

    var isRefreshing by remember { mutableStateOf(false) }
    var showChannelMismatchExplanation by remember { mutableStateOf(false) }
    var focusedSubbandIndex by remember { mutableStateOf(-1) }
    val refreshButtonFocusRequester = remember { FocusRequester() }
    var isRefreshButtonFocused by remember { mutableStateOf(false) }
    val tvDpadCardFocusRequester = remember { FocusRequester() }
    var isTvDpadCardFocused by remember { mutableStateOf(false) }
    var helpButtonRect by remember { mutableStateOf<Rect?>(null) }

    // Fluctuating simulator animators
    val signalDrift1 = remember { Animatable(-42f) }
    val signalDrift2 = remember { Animatable(-75f) }
    val signalDrift3 = remember { Animatable(-60f) }
    val signalDrift4 = remember { Animatable(-52f) }
    val signalDrift5 = remember { Animatable(-80f) }
    val signalDriftB = remember { Animatable(-83f) }
    val signalDriftD = remember { Animatable(-85f) }
    val signalDriftH = remember { Animatable(-86f) }

    val signalDrift5_1 = remember { Animatable(-38f) }
    val signalDrift5_2 = remember { Animatable(-65f) }
    val signalDrift5_3 = remember { Animatable(-70f) }
    val signalDrift5_4 = remember { Animatable(-78f) }
    val signalDrift5_5 = remember { Animatable(-55f) }

    val signalDrift6_1 = remember { Animatable(-35f) }
    val signalDrift6_2 = remember { Animatable(-68f) }
    val signalDrift6_3 = remember { Animatable(-72f) }
    val signalDrift6_4 = remember { Animatable(-61f) }

    // Real-time simulator triggers
    LaunchedEffect(Unit) {
        while (isActive) {
            signalDrift1.animateTo(-42f + Random.nextInt(-2, 3), tween(1100, easing = LinearEasing))
            delay(100)
        }
    }
    LaunchedEffect(Unit) {
        while (isActive) {
            signalDrift2.animateTo(-75f + Random.nextInt(-4, 5), tween(1400, easing = LinearEasing))
            delay(100)
        }
    }
    LaunchedEffect(Unit) {
        while (isActive) {
            signalDrift3.animateTo(-60f + Random.nextInt(-3, 3), tween(1200, easing = LinearEasing))
            delay(100)
        }
    }
    LaunchedEffect(Unit) {
        while (isActive) {
            signalDrift4.animateTo(-52f + Random.nextInt(-3, 4), tween(1300, easing = LinearEasing))
            delay(100)
        }
    }
    LaunchedEffect(Unit) {
        while (isActive) {
            signalDrift5.animateTo(-80f + Random.nextInt(-4, 4), tween(1500, easing = LinearEasing))
            delay(100)
        }
    }
    LaunchedEffect(Unit) {
        while (isActive) {
            signalDriftB.animateTo(-83f + Random.nextInt(-3, 3), tween(1250, easing = LinearEasing))
            delay(100)
        }
    }
    LaunchedEffect(Unit) {
        while (isActive) {
            signalDriftD.animateTo(-85f + Random.nextInt(-4, 4), tween(1450, easing = LinearEasing))
            delay(100)
        }
    }
    LaunchedEffect(Unit) {
        while (isActive) {
            signalDriftH.animateTo(-86f + Random.nextInt(-2, 3), tween(1350, easing = LinearEasing))
            delay(100)
        }
    }

    LaunchedEffect(Unit) {
        while (isActive) {
            signalDrift5_1.animateTo(-38f + Random.nextInt(-3, 4), tween(1200, easing = LinearEasing))
            delay(100)
        }
    }
    LaunchedEffect(Unit) {
        while (isActive) {
            signalDrift5_2.animateTo(-65f + Random.nextInt(-4, 4), tween(1300, easing = LinearEasing))
            delay(100)
        }
    }
    LaunchedEffect(Unit) {
        while (isActive) {
            signalDrift5_3.animateTo(-70f + Random.nextInt(-3, 3), tween(1100, easing = LinearEasing))
            delay(100)
        }
    }
    LaunchedEffect(Unit) {
        while (isActive) {
            signalDrift5_4.animateTo(-78f + Random.nextInt(-4, 4), tween(1400, easing = LinearEasing))
            delay(100)
        }
    }
    LaunchedEffect(Unit) {
        while (isActive) {
            signalDrift5_5.animateTo(-55f + Random.nextInt(-3, 3), tween(1250, easing = LinearEasing))
            delay(100)
        }
    }

    LaunchedEffect(Unit) {
        while (isActive) {
            signalDrift6_1.animateTo(-35f + Random.nextInt(-2, 3), tween(1000, easing = LinearEasing))
            delay(100)
        }
    }
    LaunchedEffect(Unit) {
        while (isActive) {
            signalDrift6_2.animateTo(-68f + Random.nextInt(-4, 4), tween(1300, easing = LinearEasing))
            delay(100)
        }
    }
    LaunchedEffect(Unit) {
        while (isActive) {
            signalDrift6_3.animateTo(-72f + Random.nextInt(-3, 4), tween(1400, easing = LinearEasing))
            delay(100)
        }
    }
    LaunchedEffect(Unit) {
        while (isActive) {
            signalDrift6_4.animateTo(-61f + Random.nextInt(-3, 3), tween(1150, easing = LinearEasing))
            delay(100)
        }
    }

    // Location permission state
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        )
    }

    var isLocationEnabled by remember {
        mutableStateOf(
            try {
                val lm = context.getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager
                lm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER) ||
                lm.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)
            } catch (e: Exception) {
                false
            }
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fine = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarse = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        hasLocationPermission = fine || coarse

        isLocationEnabled = try {
            val lm = context.getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager
            lm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER) ||
            lm.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)
        } catch (e: Exception) {
            false
        }
    }

    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
            if (event == androidx.lifecycle.Lifecycle.Event.ON_RESUME) {
                hasLocationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                                        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                isLocationEnabled = try {
                    val lm = context.getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager
                    lm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER) ||
                    lm.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)
                } catch (e: Exception) {
                    false
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    var showLocationServicesDialog by remember { mutableStateOf(false) }
    LaunchedEffect(hasLocationPermission, isLocationEnabled) {
        if (hasLocationPermission && !isLocationEnabled) {
            showLocationServicesDialog = true
        }
    }

    // Dynamic lists & animators map
    val signalAnimators = remember { mutableMapOf<String, Animatable<Float, AnimationVector1D>>() }
    val realWifiNetworks = remember { mutableStateListOf<WifiChannelNetwork>() }

    LaunchedEffect(selectedBand, selected6GhzSubband) {
        realWifiNetworks.clear()
        isWaitingForFirstScanInNewBand = true
        ssidOverriddenChannels.clear()
        activeSettingsSsid = null
        currentChannelVal = 0
        ssidWaitingForScan = null
        lastKnownConnectedSsid = null
        lastKnownConnectedBssid = null
        lastKnownConnectedBand = null
        fetchRouterSsidSettingsForBand(selectedBand, realWifiNetworks)
        try {
            wifiManager.startScan()
        } catch (e: Exception) {}
    }

    LaunchedEffect(realWifiNetworks.toList(), ssidWaitingForScan) {
        val target = ssidWaitingForScan
        if (target != null) {
            val found = realWifiNetworks.any { it.ssid == target }
            if (found) {
                ssidWaitingForScan = null
            }
        }
    }

    // Core scanner function
    val updateWifiManagerData = remember {
        {
            if (isFetchingBandSettings || isRefreshing || isWaitingForFirstScanInNewBand) {
                // Keep the list completely empty while fetching router settings, during manual refresh, or waiting for first scan
                realWifiNetworks.clear()
            } else if (hasLocationPermission) {
                val results = try {
                    wifiManager.scanResults
                } catch (e: SecurityException) {
                    null
                } ?: emptyList()

                var connectedBssid: String? = null
                var connectedSsid: String? = null
                var connectedBand: WifiBand? = null
                try {
                    val info = wifiManager.connectionInfo
                    if (info != null && info.networkId != -1) {
                        connectedBssid = info.bssid
                        val raw = info.ssid
                        if (raw != null && raw != "<unknown ssid>") {
                            connectedSsid = raw.removeSurrounding("\"")
                        }
                        val freq = info.frequency
                        connectedBand = when {
                            freq <= 2484 -> WifiBand.BAND_2_4_GHZ
                            freq in 4900..5925 -> WifiBand.BAND_5_GHZ
                            freq in 5925..7125 -> WifiBand.BAND_6_GHZ
                            else -> WifiBand.BAND_2_4_GHZ
                        }
                    }
                } catch (e: Exception) {}

                activeConnectedBssid = connectedBssid
                activeConnectedSsid = connectedSsid
                activeConnectedBand = connectedBand

                val mapped = results.map { sr ->
                    val rawSsid = sr.SSID
                    val ssid = if (rawSsid.isNullOrBlank()) sr.BSSID ?: "Hidden" else rawSsid
                    val freq = sr.frequency
                    
                    val band = when {
                        freq <= 2484 -> WifiBand.BAND_2_4_GHZ
                        freq in 4900..5925 -> WifiBand.BAND_5_GHZ
                        freq in 5925..7125 -> WifiBand.BAND_6_GHZ
                        else -> WifiBand.BAND_2_4_GHZ
                    }

                    val channel = when (band) {
                        WifiBand.BAND_2_4_GHZ -> if (freq == 2484) 14 else (freq - 2407) / 5
                        WifiBand.BAND_5_GHZ -> (freq - 5000) / 5
                        WifiBand.BAND_6_GHZ -> (freq - 5940) / 5
                    }

                    val width = when (sr.channelWidth) {
                        ScanResult.CHANNEL_WIDTH_20MHZ -> 20
                        ScanResult.CHANNEL_WIDTH_40MHZ -> 40
                        ScanResult.CHANNEL_WIDTH_80MHZ -> 80
                        ScanResult.CHANNEL_WIDTH_160MHZ -> 160
                        ScanResult.CHANNEL_WIDTH_80MHZ_PLUS_MHZ -> 160
                        else -> 20
                    }

                    val bssid = sr.BSSID ?: ssid
                    val isConnected = bssid == connectedBssid || (connectedSsid != null && ssid == connectedSsid)

                    val anim = signalAnimators.getOrPut(bssid) {
                        Animatable(sr.level.toFloat())
                    }
                    scope.launch {
                        anim.animateTo(sr.level.toFloat(), animationSpec = tween(1200, easing = LinearEasing))
                    }

                    // Network color: Highlight active connection as solid vivid red, else distinct hash-based
                    val color = if (isConnected) {
                        Color(0xFFFF3B30)
                    } else {
                        val hue = (kotlin.math.abs(ssid.hashCode()) % 360).toFloat()
                        Color.hsv(hue, 0.72f, 0.95f)
                    }

                    WifiChannelNetwork(
                        ssid = ssid,
                        centerChannel = getStandardCenterChannel(channel, width, band),
                        widthMhz = width,
                        baseDbm = sr.level.toFloat(),
                        currentDbm = anim.asState(),
                        color = color,
                        isConnected = isConnected,
                        band = band,
                        frequency = freq,
                        bssid = bssid
                    )
                }

                // If active connection exists elsewhere/standalone, append it
                var containsActive = mapped.any { it.isConnected }
                val finalFiltered = if (!containsActive && connectedSsid != null) {
                    val freq = try { wifiManager.connectionInfo.frequency } catch (e: Exception) { 2437 }
                    val lvl = try { wifiManager.connectionInfo.rssi } catch (e: Exception) { -40 }
                    
                    val band = when {
                        freq <= 2484 -> WifiBand.BAND_2_4_GHZ
                        freq in 4900..5925 -> WifiBand.BAND_5_GHZ
                        freq in 5925..7125 -> WifiBand.BAND_6_GHZ
                        else -> WifiBand.BAND_2_4_GHZ
                    }

                    val channel = when (band) {
                        WifiBand.BAND_2_4_GHZ -> if (freq == 2484) 14 else (freq - 2407) / 5
                        WifiBand.BAND_5_GHZ -> (freq - 5000) / 5
                        WifiBand.BAND_6_GHZ -> (freq - 5940) / 5
                    }

                    val anim = signalAnimators.getOrPut(connectedBssid ?: connectedSsid) {
                        Animatable(lvl.toFloat())
                    }
                    scope.launch {
                        anim.animateTo(lvl.toFloat(), animationSpec = tween(1200, easing = LinearEasing))
                    }

                    mapped + WifiChannelNetwork(
                        ssid = connectedSsid,
                        centerChannel = getStandardCenterChannel(channel, 40, band),
                        widthMhz = 40,
                        baseDbm = lvl.toFloat(),
                        currentDbm = anim.asState(),
                        color = Color(0xFFFF3B30),
                        isConnected = true,
                        band = band,
                        frequency = freq,
                        bssid = connectedBssid ?: ""
                    )
                } else {
                    mapped
                }

                realWifiNetworks.clear()
                realWifiNetworks.addAll(finalFiltered)

                if (ssidChangingChannel != null) {
                    val targetFreq = when (bandChangingChannel) {
                        WifiBand.BAND_2_4_GHZ -> 2407 + (targetChannelChangingChannel ?: 0) * 5
                        WifiBand.BAND_5_GHZ -> 5000 + (targetChannelChangingChannel ?: 0) * 5
                        WifiBand.BAND_6_GHZ -> 5940 + (targetChannelChangingChannel ?: 0) * 5
                        else -> 0
                    }
                    val isTimeout = lastChangeTime > 0L && (System.currentTimeMillis() - lastChangeTime > 15000L)
                    val foundSignalInScan = results.any { sr ->
                        val cleanSrSsid = (sr.SSID ?: "").removeSurrounding("\"")
                        val cleanSsidChan = (ssidChangingChannel ?: "").removeSurrounding("\"")
                        val isSameSsid = cleanSrSsid.isNotEmpty() && cleanSrSsid == cleanSsidChan
                        
                        val scannedChannel = when {
                            sr.frequency >= 2400 && sr.frequency <= 2500 -> (sr.frequency - 2407) / 5
                            sr.frequency >= 5150 && sr.frequency <= 5850 -> (sr.frequency - 5000) / 5
                            sr.frequency >= 5925 && sr.frequency <= 7125 -> (sr.frequency - 5940) / 5
                            else -> 0
                        }
                        val maxChanDiff = if (bandChangingChannel == WifiBand.BAND_2_4_GHZ) 4 else 16
                        val isCorrectChannel = targetChannelChangingChannel == null || 
                                               scannedChannel == targetChannelChangingChannel || 
                                               Math.abs(scannedChannel - (targetChannelChangingChannel ?: 0)) <= maxChanDiff
                                               
                        isSameSsid && isCorrectChannel
                    }
                    val isConnectedToNewChannel = try {
                        val currentFreq = wifiManager.connectionInfo.frequency
                        val cleanConnSsid = (connectedSsid ?: "").removeSurrounding("\"")
                        val cleanSsidChan = (ssidChangingChannel ?: "").removeSurrounding("\"")
                        val isSameSsid = cleanConnSsid.isNotEmpty() && cleanConnSsid == cleanSsidChan
                        
                        val currentChannel = when {
                            currentFreq >= 2400 && currentFreq <= 2500 -> (currentFreq - 2407) / 5
                            currentFreq >= 5150 && currentFreq <= 5850 -> (currentFreq - 5000) / 5
                            currentFreq >= 5925 && currentFreq <= 7125 -> (currentFreq - 5940) / 5
                            else -> 0
                        }
                        val maxChanDiff = if (bandChangingChannel == WifiBand.BAND_2_4_GHZ) 4 else 16
                        val isCorrectChannel = targetChannelChangingChannel == null || 
                                               currentChannel == targetChannelChangingChannel || 
                                               Math.abs(currentChannel - (targetChannelChangingChannel ?: 0)) <= maxChanDiff
                                               
                        isSameSsid && isCorrectChannel
                    } catch (e: Exception) {
                        false
                    }
                    
                    val elapsed = System.currentTimeMillis() - lastChangeTime
                    val isCooldownPassed = elapsed >= 4000L
                    
                    if (isTimeout || (isCooldownPassed && (foundSignalInScan || isConnectedToNewChannel))) {
                        val oldTargetChan = targetChannelChangingChannel
                        if (oldTargetChan != null && oldTargetChan > 0) {
                            currentChannelVal = oldTargetChan
                        }
                        ssidChangingChannel = null
                        targetChannelChangingChannel = null
                        bandChangingChannel = null
                        lastChangeTime = 0L
                        isApplyingChannelChange = false
                        applyChannelMessage = null
                        
                        // Clear state completely on settings change completion except realWifiNetworks and overrides to prevent image flickering/jumping
                        // realWifiNetworks.clear()
                        // Keep ssidOverriddenChannels and lastKnownNetworks until first post-commit scan results arrive in receiver
                        currentChannelVal = oldTargetChan ?: 0
                        
                        fetchRouterSsidSettingsForBand(selectedBand, realWifiNetworks)
                    }
                }
            }
        }
    }

    LaunchedEffect(updateWifiManagerData) {
        onUpdateWifiData = updateWifiManagerData
    }

    val fetchSsidSettings: (String, WifiBand) -> Unit = { ssid, band ->
        if (viewModel != null) {
            isFetchingSettings = true
            showSettingsDialog = true
            settingsError = null
            
            val cmd = "for i in " + "$(seq 0 16)" + "; do " +
                      "s=" + "$(uci -q get wireless.@wifi-iface[\$i].ssid); " +
                      "if [ \"\$s\" = \"$ssid\" ]; then " +
                      "k=" + "$(uci -q get wireless.@wifi-iface[\$i].key); " +
                      "e=" + "$(uci -q get wireless.@wifi-iface[\$i].encryption); " +
                      "d=" + "$(uci -q get wireless.@wifi-iface[\$i].device); " +
                      "p=" + "$(uci -q get wireless.\$d.txpower); " +
                      "c=" + "$(uci -q get wireless.\$d.channel); " +
                      "h=" + "$(uci -q get wireless.\$d.htmode); " +
                      "y=" + "$(uci -q get wireless.\$d.country); " +
                      "txlist=" + "$(iwinfo \$d txpowerlist 2>/dev/null | awk '{print \$1}' | tr '\\n' ','); " +
                      "echo \"SUCCESS|wireless.@wifi-iface[\$i]|wireless.\$d|\$k|\$p|\$c|\$h|\$y|\$txlist|\$e\"; " +
                      "exit 0; " +
                      "fi; " +
                      "done; " +
                      "echo \"NOT_FOUND\""
            
            viewModel.executeConsoleCommandDirect(cmd) { response ->
                isFetchingSettings = false
                val clean = response.trim()
                if (clean.contains("SUCCESS|")) {
                    val parts = clean.split("|")
                    if (parts.size >= 10) {
                        detectedIfaceSection = parts[1]
                        detectedDeviceSection = parts[2]
                        currentPassword = parts[3]
                        currentTxPower = parts[4].ifEmpty { "auto" }
                        currentChannelVal = parts[5].trim().toIntOrNull() ?: 0
                        currentHtmode = parts[6].trim().ifEmpty { "auto" }
                        currentCountry = parts[7].trim().ifEmpty { "US" }
                        currentEncryption = parts[9].trim().ifEmpty { "psk2" }
                    } else if (parts.size >= 8) {
                        detectedIfaceSection = parts[1]
                        detectedDeviceSection = parts[2]
                        currentPassword = parts[3]
                        currentTxPower = parts[4].ifEmpty { "auto" }
                        currentChannelVal = parts[5].trim().toIntOrNull() ?: 0
                        currentHtmode = parts[6].trim().ifEmpty { "auto" }
                        currentCountry = parts[7].trim().ifEmpty { "US" }
                        currentEncryption = "psk2"
                    } else if (parts.size >= 6) {
                        detectedIfaceSection = parts[1]
                        detectedDeviceSection = parts[2]
                        currentPassword = parts[3]
                        currentTxPower = parts[4].ifEmpty { "auto" }
                        currentChannelVal = parts[5].trim().toIntOrNull() ?: 0
                        currentHtmode = "auto"
                        currentCountry = "US"
                        currentEncryption = "psk2"
                    } else if (parts.size >= 5) {
                        detectedIfaceSection = parts[1]
                        detectedDeviceSection = parts[2]
                        currentPassword = parts[3]
                        currentTxPower = parts[4].ifEmpty { "auto" }
                        currentChannelVal = 0
                        currentHtmode = "auto"
                        currentCountry = "US"
                        currentEncryption = "psk2"
                    } else {
                        settingsError = getSettingsString("error_parsing", locale)
                    }
                    
                    // Parse available tx powers
                    val rawTxPowerList = if (parts.size >= 9) parts[8].trim() else ""
                    val parsedList = rawTxPowerList.split(",")
                        .map { it.trim().toIntOrNull() }
                        .filterNotNull()
                    val minP = parsedList.minOrNull() ?: 0
                    val maxP = parsedList.maxOrNull() ?: 23
                    val step2List = if (minP <= maxP) (minP..maxP step 2).toList() else listOf(minP)
                    availableTxPowers = listOf("auto") + step2List.map { it.toString() }
                } else {
                    settingsError = getSettingsString("error_not_found", locale)
                }
            }
        }
    }

    val saveSsidSettings: (String, String, Int, String, String, String) -> Unit = { newPassword, newTxPower, newChannel, newHtmode, newCountry, newEncryption ->
        val ssid = activeSettingsSsid ?: ""
        val band = activeSettingsBand ?: selectedBand
        isApplyingChannelChange = true
        applyChannelMessage = getSettingsString("apply_settings", locale)
        showSettingsDialog = false
        
        fetchSsidDataBeforeChange(ssid, band) {
            if (viewModel != null) {
                val commands = java.util.ArrayList<String>()
                commands.add("uci set $detectedIfaceSection.key='$newPassword'")
                commands.add("uci set $detectedIfaceSection.encryption='$newEncryption'")
                if (newTxPower.lowercase() == "auto") {
                    commands.add("uci set $detectedDeviceSection.txpower='auto'")
                } else {
                    commands.add("uci set $detectedDeviceSection.txpower='$newTxPower'")
                }
                if (newChannel > 0) {
                    commands.add("uci set $detectedDeviceSection.channel='$newChannel'")
                }
                val finalHtmode = if (newHtmode.isNotEmpty() && newHtmode != "auto") newHtmode else currentHtmode
                if (finalHtmode.isNotEmpty() && finalHtmode != "auto") {
                    commands.add("uci set $detectedDeviceSection.htmode='$finalHtmode'")
                }
                if (newCountry.isNotEmpty()) {
                    commands.add("uci set $detectedDeviceSection.country='$newCountry'")
                }
                commands.add("uci commit wireless")
                commands.add("wifi")
                
                val cmd = commands.joinToString(" && ")
                
                ssidChangingChannel = activeSettingsSsid
                targetChannelChangingChannel = if (newChannel > 0) newChannel else currentChannelVal
                if (activeSettingsSsid != null && targetChannelChangingChannel != null) {
                    ssidOverriddenChannels[activeSettingsSsid!!] = targetChannelChangingChannel!!
                }
                bandChangingChannel = activeSettingsBand
                lastChangeTime = System.currentTimeMillis()
                isWaitingForFirstScanAfterCommit = true
                ssidWaitingForScan = activeSettingsSsid
                
                viewModel.executeConsoleCommandDirect(cmd) { response ->
                    updateWifiManagerData()
                    activeSettingsSsid?.let { silentFetchRouterSsidSettings(it) }
                }
            } else {
                isApplyingChannelChange = false
            }
        }
    }

    // Trigger scanning permissions and loop
    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    val wifiScanReceiver = remember {
        object : BroadcastReceiver() {
            override fun onReceive(c: Context?, i: Intent?) {
                if (i?.action == WifiManager.SCAN_RESULTS_AVAILABLE_ACTION) {
                    if (isWaitingForFirstScanAfterCommit) {
                        ssidOverriddenChannels.clear()
                        lastKnownConnectedSsid = null
                        lastKnownConnectedBssid = null
                        isWaitingForFirstScanAfterCommit = false
                    }
                    isWaitingForFirstScanInNewBand = false
                }
                updateWifiManagerData()
            }
        }
    }

    LaunchedEffect(hasLocationPermission, isLocationEnabled) {
        if (hasLocationPermission && isLocationEnabled) {
            val filter = IntentFilter().apply {
                addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
                addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
                addAction("android.net.conn.CONNECTIVITY_CHANGE")
            }
            try {
                context.registerReceiver(wifiScanReceiver, filter)
            } catch (e: Exception) {}
            
            updateWifiManagerData()
            
            while (isActive) {
                try {
                    wifiManager.startScan()
                } catch (e: Exception) {}
                updateWifiManagerData()
                delay(7000)
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            try {
                context.unregisterReceiver(wifiScanReceiver)
            } catch (e: Exception) {}
            // Clear all saved / stored data on exiting the window
            realWifiNetworks.clear()
            ssidOverriddenChannels.clear()
            lastKnownConnectedSsid = null
            lastKnownConnectedBssid = null
            lastKnownConnectedBand = null
            activeConnectedSsid = null
            activeConnectedBssid = null
            activeConnectedBand = null
            activeSettingsSsid = null
            activeSettingsBssid = null
            activeSettingsBand = null
            ssidWaitingForScan = null
        }
    }

    // Select correct network data array
    val displayNetworks = remember(
        selectedBand, 
        hasLocationPermission, 
        isLocationEnabled, 
        realWifiNetworks.size, 
        ssidChangingChannel, 
        targetChannelChangingChannel, 
        bandChangingChannel, 
        pendingChannelChange,
        activeConnectedSsid,
        activeConnectedBssid,
        lastKnownConnectedSsid,
        lastKnownConnectedBssid,
        ssidOverriddenChannels.size,
        isFetchingBandSettings,
        isRefreshing,
        isWaitingForFirstScanAfterCommit,
        activeSettingsSsid,
        activeSettingsBand,
        currentChannelVal,
        isWaitingForFirstScanInNewBand
    ) {
        if ((isFetchingBandSettings && !isWaitingForFirstScanAfterCommit) || isRefreshing || isWaitingForFirstScanInNewBand) {
            return@remember emptyList<WifiChannelNetwork>()
        }
        val lists = if (hasLocationPermission && isLocationEnabled && realWifiNetworks.isNotEmpty()) {
            realWifiNetworks.toList()
        } else {
            emptyList()
        }

        val targetSsid = ssidChangingChannel ?: pendingChannelChange?.netSsid
        val targetBand = bandChangingChannel ?: pendingChannelChange?.band
        val targetChan = targetChannelChangingChannel ?: pendingChannelChange?.currentDragChannel

        var listMapped = lists.map { net ->
            val overriddenChan = ssidOverriddenChannels[net.ssid]
            if (targetSsid != null && net.ssid == targetSsid && targetChan != null && targetBand != null) {
                val realCenterChan = getStandardCenterChannel(targetChan, net.widthMhz, targetBand)
                net.copy(
                    centerChannel = realCenterChan,
                    band = targetBand,
                    frequency = when (targetBand) {
                        WifiBand.BAND_2_4_GHZ -> 2407 + realCenterChan * 5
                        WifiBand.BAND_5_GHZ -> 5000 + realCenterChan * 5
                        WifiBand.BAND_6_GHZ -> 5940 + realCenterChan * 5
                    }
                )
            } else if (overriddenChan != null && overriddenChan > 0) {
                val realCenterChan = getStandardCenterChannel(overriddenChan, net.widthMhz, net.band)
                net.copy(
                    centerChannel = realCenterChan,
                    frequency = when (net.band) {
                        WifiBand.BAND_2_4_GHZ -> 2407 + realCenterChan * 5
                        WifiBand.BAND_5_GHZ -> 5000 + realCenterChan * 5
                        WifiBand.BAND_6_GHZ -> 5940 + realCenterChan * 5
                    }
                )
            } else {
                net
            }
        }.filter { net -> net.band == selectedBand }

        val checkSsid = activeSettingsSsid ?: (if (activeConnectedBand == selectedBand) activeConnectedSsid else null) ?: (if (lastKnownConnectedBand == selectedBand) lastKnownConnectedSsid else null)
        if (checkSsid != null) {
            val hasRouterActive = listMapped.any { it.ssid == checkSsid }
            if (!hasRouterActive) {
                val chan = if (currentChannelVal > 0) currentChannelVal else when (selectedBand) {
                    WifiBand.BAND_2_4_GHZ -> 1
                    WifiBand.BAND_5_GHZ -> 36
                    WifiBand.BAND_6_GHZ -> 1
                }
                val isConn = (checkSsid == activeConnectedSsid)
                val hue = (kotlin.math.abs(checkSsid.hashCode()) % 360).toFloat()
                val col = if (isConn) Color(0xFFFF3B30) else Color.hsv(hue, 0.72f, 0.95f)
                
                val synthWidth = run {
                    val mode = currentHtmode.uppercase()
                    if (mode.contains("160")) 160
                    else if (mode.contains("80")) 80
                    else if (mode.contains("40")) 40
                    else if (mode.contains("20")) 20
                    else {
                        if (selectedBand == WifiBand.BAND_2_4_GHZ) 20 else 80
                    }
                }
                val realCenter = getStandardCenterChannel(chan, synthWidth, selectedBand)
                val synthNet = WifiChannelNetwork(
                    ssid = checkSsid,
                    centerChannel = realCenter,
                    widthMhz = synthWidth,
                    baseDbm = -45f,
                    currentDbm = mutableStateOf(-45f),
                    color = col,
                    isConnected = isConn,
                    band = selectedBand,
                    frequency = when (selectedBand) {
                        WifiBand.BAND_2_4_GHZ -> 2407 + realCenter * 5
                        WifiBand.BAND_5_GHZ -> 5000 + realCenter * 5
                        WifiBand.BAND_6_GHZ -> 5940 + realCenter * 5
                    },
                    bssid = activeSettingsBssid ?: (if (isConn) (activeConnectedBssid ?: "") else "")
                )
                listMapped = listMapped + synthNet
            }
        }

        listMapped
    }

    val filteredDisplayNetworks = remember(displayNetworks, selectedBand, selected6GhzSubband) {
        if (selectedBand == WifiBand.BAND_6_GHZ) {
            displayNetworks.filter { net ->
                val ch = net.centerChannel
                when (selected6GhzSubband) {
                    0 -> ch in 1..29
                    1 -> ch in 33..61
                    2 -> ch in 65..93
                    3 -> ch in 97..125
                    4 -> ch in 129..157
                    5 -> ch in 161..189
                    else -> ch in 193..233
                }
            }
        } else {
            displayNetworks
        }
    }

    val matchedRouterNetworkSsid = remember(
        filteredDisplayNetworks, 
        activeConnectedSsid, 
        activeConnectedBssid, 
        lastKnownConnectedSsid, 
        lastKnownConnectedBssid,
        activeSettingsSsid,
        activeSettingsBand,
        selectedBand
    ) {
        filteredDisplayNetworks.find { 
            it.isConnected || 
            it.belongsToSameRouter(activeConnectedSsid ?: lastKnownConnectedSsid, activeConnectedBssid ?: lastKnownConnectedBssid) ||
            (activeSettingsBand == selectedBand && activeSettingsSsid != null && (it.ssid == activeSettingsSsid || it.belongsToSameRouter(activeSettingsSsid, null)))
        }?.ssid
    }

    LaunchedEffect(matchedRouterNetworkSsid, selectedBand) {
        matchedRouterNetworkSsid?.let { ssid ->
            silentFetchRouterSsidSettings(ssid)
        }
    }

    // Auto request TV focus, select 2.4 GHz band, clear old graph state on start
    LaunchedEffect(Unit) {
        selectedBand = WifiBand.BAND_2_4_GHZ
        realWifiNetworks.removeAll { it.band == WifiBand.BAND_2_4_GHZ }
        realWifiNetworks.clear()
        try {
            updateWifiManagerData()
        } catch (e: Exception) {}
        if (isTv) {
            try {
                band2FocusRequester.requestFocus()
            } catch (e: Exception) {}
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("wifi_analyzer_fullscreen"),
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                tonalElevation = 0.dp,
                color = Color.Black // Dark chic theme as per dashboard
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = onClose,
                            modifier = Modifier
                                .testTag("btn_close_wifi_analyzer")
                                .focusRequester(backButtonFocusRequester)
                                .onFocusChanged { isBackFocused = it.isFocused }
                                .then(
                                    if (isTv) {
                                        Modifier.border(
                                            width = 1.5.dp,
                                            color = if (isBackFocused) Color(0xFF5AC8FA) else Color.Transparent,
                                            shape = RoundedCornerShape(24.dp)
                                        )
                                    } else Modifier
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = if (locale == "ru") "Назад" else "Back",
                                tint = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Column {
                            Text(
                                text = TestTabLocalizations.getWifiAnalyzerTitle(locale),
                                fontSize = 18.sp,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = if (locale == "ru") "Спектр радиоэфира" else "Radio spectrum monitoring",
                                fontSize = 11.sp,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.5f)
                            )
                        }
                    }

                    IconButton(
                        onClick = { activeHelpDialog = true },
                        modifier = Modifier
                            .testTag("btn_help_wifi_analyzer")
                            .focusRequester(infoButtonFocusRequester)
                            .onFocusChanged { isInfoFocused = it.isFocused }
                            .then(
                                if (isTv) {
                                    Modifier.border(
                                        width = 1.5.dp,
                                        color = if (isInfoFocused) Color(0xFF5AC8FA) else Color.Transparent,
                                        shape = RoundedCornerShape(24.dp)
                                    )
                                } else Modifier
                                                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Help",
                            tint = Color(0xFF5AC8FA)
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.Black), // High contrast AMOLED background
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 1. Channel Band Header + Custom Horizontal Selector Tabs EXACTLY as in screenshot
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = if (locale == "ru") "Каналы" else "Channels",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    BandTab(
                        number = "2.4",
                        unit = "GHz",
                        isSelected = selectedBand == WifiBand.BAND_2_4_GHZ,
                        onClick = { selectedBand = WifiBand.BAND_2_4_GHZ },
                        focusRequester = band2FocusRequester,
                        onFocusChanged = { isBand2Focused = it },
                        isFocused = isBand2Focused
                    )
                    BandTab(
                        number = "5",
                        unit = "GHz",
                        isSelected = selectedBand == WifiBand.BAND_5_GHZ,
                        onClick = { selectedBand = WifiBand.BAND_5_GHZ },
                        focusRequester = band5FocusRequester,
                        onFocusChanged = { isBand5Focused = it },
                        isFocused = isBand5Focused
                    )
                    BandTab(
                        number = "6",
                        unit = "GHz",
                        isSelected = selectedBand == WifiBand.BAND_6_GHZ,
                        onClick = { selectedBand = WifiBand.BAND_6_GHZ },
                        focusRequester = band6FocusRequester,
                        onFocusChanged = { isBand6Focused = it },
                        isFocused = isBand6Focused
                    )
                }
            }

            // Divider matching screenshot look
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.White.copy(alpha = 0.08f))
            )

            val textMeasurer = rememberTextMeasurer()
            val gridLineColor = Color.White.copy(alpha = 0.22f) // High contrast grid as requested
            val axisTextColor = Color.White.copy(alpha = 0.65f)
            val isLocationActive = hasLocationPermission && isLocationEnabled

            val routerNetworkUnderCurrentBand = remember(filteredDisplayNetworks, activeConnectedSsid, activeConnectedBssid, lastKnownConnectedSsid, lastKnownConnectedBssid) {
                filteredDisplayNetworks.find { net ->
                    net.isConnected || net.belongsToSameRouter(activeConnectedSsid ?: lastKnownConnectedSsid, activeConnectedBssid ?: lastKnownConnectedBssid)
                }
            }



            // Sub-bands layout for 6 GHz (7 subbands corresponding to screenshot)
            if (selectedBand == WifiBand.BAND_6_GHZ && isLocationActive) {
                val subbands = listOf(
                    "1 - 29", "33 - 61", "65 - 93", "97 - 125",
                    "129 - 157", "161 - 189", "193 - 233"
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    subbands.forEachIndexed { index, title ->
                        val isSubSelected = selected6GhzSubband == index
                        val isSubFocused = focusedSubbandIndex == index
                        val containerColor = if (isSubSelected || isSubFocused) Color(0xFF5AC8FA).copy(alpha = 0.2f) else Color(0xFF1C1C1E)
                        val borderColor = if (isSubSelected) Color(0xFF5AC8FA) else if (isSubFocused) Color(0xFF5AC8FA).copy(alpha = 0.6f) else Color.Transparent
                        val textColor = if (isSubSelected || isSubFocused) Color(0xFF5AC8FA) else Color.White.copy(alpha = 0.6f)

                        Box(
                            modifier = Modifier
                                .onFocusChanged { if (it.isFocused) focusedSubbandIndex = index }
                                .focusable()
                                .clip(RoundedCornerShape(8.dp))
                                .background(containerColor)
                                .border(
                                    width = if (isSubFocused) 2.dp else 1.dp,
                                    color = if (isSubFocused || isSubSelected) Color(0xFF5AC8FA) else Color.Transparent,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { selected6GhzSubband = index }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = title,
                                style = TextStyle(
                                    color = textColor,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }

            val routerNetworks = remember(
                filteredDisplayNetworks, 
                activeConnectedSsid, 
                activeConnectedBssid, 
                lastKnownConnectedSsid, 
                lastKnownConnectedBssid,
                activeSettingsSsid,
                activeSettingsBand,
                selectedBand
            ) {
                filteredDisplayNetworks.filter { net ->
                    net.isConnected || 
                    net.belongsToSameRouter(activeConnectedSsid ?: lastKnownConnectedSsid, activeConnectedBssid ?: lastKnownConnectedBssid) ||
                    (activeSettingsBand == selectedBand && activeSettingsSsid != null && (net.ssid == activeSettingsSsid || net.belongsToSameRouter(activeSettingsSsid, null)))
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                if (!isLocationActive) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Warning",
                            tint = Color(0xFFFF3B30),
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = if (locale == "ru") {
                                "Отсутствует доступ к местоположению, который требуется для работы анализатора"
                            } else {
                                "Location access required for the analyzer is missing or disabled"
                            },
                            style = TextStyle(
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                lineHeight = 22.sp
                            ),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Button(
                            onClick = {
                                if (!hasLocationPermission) {
                                    permissionLauncher.launch(
                                        arrayOf(
                                            Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION
                                        )
                                    )
                                } else {
                                    try {
                                        context.startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                                    } catch (e: Exception) {}
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF3B30)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = if (!hasLocationPermission) {
                                    if (locale == "ru") "Предоставить разрешение" else "Grant Permission"
                                } else {
                                    if (locale == "ru") "Включить геолокацию" else "Enable Location"
                                },
                                color = Color.White
                            )
                        }
                    }
                } else {
                    BoxWithConstraints(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 4.dp, vertical = 8.dp)
                    ) {
                        val canvasWidth = constraints.maxWidth.toFloat()
                        val canvasHeight = constraints.maxHeight.toFloat()
                        val leftMargin = 100f
                        val rightMargin = 40f
                        val topMargin = 110f
                        val bottomMargin = 80f
                        val chartWidth = canvasWidth - leftMargin - rightMargin
                        val chartHeight = canvasHeight - topMargin - bottomMargin
                        val density = LocalDensity.current

                        Canvas(
                            modifier = Modifier
                                .fillMaxSize()
                                .pointerInput(selectedBand, filteredDisplayNetworks) {
                                    detectTapGestures(
                                        onDoubleTap = { offset: Offset ->
                                            val hit = tagBounds.find { bound ->
                                                bound.rect.contains(offset)
                                            }
                                            if (hit != null) {
                                                activeSettingsSsid = hit.netSsid
                                                activeSettingsBssid = hit.netBssid
                                                activeSettingsBand = hit.band
                                                fetchSsidSettings(hit.netSsid, hit.band)
                                            }
                                        }
                                    )
                                }
                                .pointerInput(selectedBand, filteredDisplayNetworks) {
                                    detectDragGestures(
                                        onDragStart = { offset ->
                                            // check if offset hit any router SSD tag bounds
                                            val hit = tagBounds.find { bound ->
                                                bound.rect.contains(offset)
                                            }
                                            if (hit != null) {
                                                activeDragSession = DragSession(
                                                    netSsid = hit.netSsid,
                                                    netBssid = hit.netBssid,
                                                    startChannel = hit.centerChannel,
                                                    band = hit.band,
                                                    currentDragChannel = hit.centerChannel
                                                )
                                            } else {
                                                activeDragSession = null
                                            }
                                        },
                                        onDrag = { change, _ ->
                                            val session = activeDragSession
                                            if (session != null) {
                                                change.consume()
                                                val currentTouchX = change.position.x
                                                
                                                // Compute getXPos values exactly as we do inside DrawScope to find closest channel!
                                                val canvasWidthVal = size.width.toFloat()
                                                val leftMarginVal = 100f
                                                val rightMarginVal = 40f
                                                val chartWidthVal = canvasWidthVal - leftMarginVal - rightMarginVal
                                                
                                                val minChan = channels.first().toFloat()
                                                val maxChan = channels.last().toFloat()
                                                
                                                val (minExt, maxExt) = when (selectedBand) {
                                                    WifiBand.BAND_2_4_GHZ -> Pair(1f - 2.5f, 14f + 2.5f)
                                                    WifiBand.BAND_5_GHZ -> Pair(36f - 18f, 165f + 16f)
                                                    WifiBand.BAND_6_GHZ -> Pair(minChan - 4.5f, maxChan + 4.5f)
                                                }
                                                val chanSpanScale = maxExt - minExt
                                                
                                                val currentGetXPos = { chan: Float ->
                                                    val pct = if (selectedBand == WifiBand.BAND_5_GHZ) {
                                                        get5GhzPositionPercent(chan)
                                                    } else {
                                                        (chan - minExt) / chanSpanScale
                                                    }
                                                    leftMarginVal + pct * chartWidthVal
                                                }
                                                
                                                val closestCh = channels.minByOrNull { ch ->
                                                    kotlin.math.abs(currentGetXPos(ch.toFloat()) - currentTouchX)
                                                }
                                                if (closestCh != null) {
                                                    activeDragSession = session.copy(currentDragChannel = closestCh)
                                                }
                                            }
                                        },
                                        onDragEnd = {
                                            val session = activeDragSession
                                            if (session != null) {
                                                if (session.currentDragChannel != session.startChannel) {
                                                    pendingChannelChange = session
                                                }
                                                activeDragSession = null
                                            }
                                        },
                                        onDragCancel = {
                                            activeDragSession = null
                                        }
                                    )
                                }
                        ) {
                        val canvasWidth = size.width
                        val canvasHeight = size.height
                        val leftMargin = 100f
                        val rightMargin = 40f
                        val topMargin = 110f
                        val bottomMargin = 80f

                        val chartWidth = canvasWidth - leftMargin - rightMargin
                        val chartHeight = canvasHeight - topMargin - bottomMargin

                        // Y-Axis limits: -100 to -20 dBm (80 dBm span)
                        val dbmMin = -100f
                        val dbmMax = -20f
                        val dbmRange = dbmMax - dbmMin

                        // Draw Horizontal Grid lines (-20 to -90, Q is written at bottom)
                        for (dbm in -90..-20 step 10) {
                            val pct = (dbm - dbmMin) / dbmRange
                            val y = topMargin + (1f - pct) * chartHeight

                            drawLine(
                                color = gridLineColor,
                                start = Offset(leftMargin, y),
                                end = Offset(canvasWidth - rightMargin, y),
                                strokeWidth = 1.5f
                            )

                            // Y Tick Labels
                            val labelText = "$dbm"
                            val tagResult = textMeasurer.measure(
                                text = labelText,
                                style = TextStyle(
                                    color = axisTextColor,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            )
                            drawText(
                                textMeasurer = textMeasurer,
                                text = labelText,
                                topLeft = Offset(leftMargin - tagResult.size.width - 20f, y - tagResult.size.height / 2f),
                                style = TextStyle(
                                    color = axisTextColor,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            )
                        }

                        // Q Indicator / Label at bottom-most level
                        val qY = topMargin + chartHeight
                        val qResult = textMeasurer.measure(
                            text = "Q",
                            style = TextStyle(
                                color = axisTextColor,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Normal
                            )
                        )
                        drawText(
                            textMeasurer = textMeasurer,
                            text = "Q",
                            topLeft = Offset(leftMargin - qResult.size.width - 20f, qY - qResult.size.height / 2f),
                            style = TextStyle(
                                color = axisTextColor,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Normal
                            )
                        )

                        val minChan = channels.first().toFloat()
                        val maxChan = channels.last().toFloat()

                        // Calculate visual extent of the axis to add margins and shift starting scale
                        val (minExt, maxExt) = when (selectedBand) {
                            WifiBand.BAND_2_4_GHZ -> Pair(1f - 2.5f, 14f + 2.5f)
                            WifiBand.BAND_5_GHZ -> Pair(36f - 18f, 165f + 16f)
                            WifiBand.BAND_6_GHZ -> Pair(minChan - 4.5f, maxChan + 4.5f)
                        }

                        val chanSpanScale = maxExt - minExt

                        val getXPos = { chan: Float ->
                            val pct = if (selectedBand == WifiBand.BAND_5_GHZ) {
                                get5GhzPositionPercent(chan)
                            } else {
                                (chan - minExt) / chanSpanScale
                            }
                            leftMargin + pct * chartWidth
                        }

                        // Draw Vertical X-Axis grids and values
                        channels.forEach { ch ->
                            val x = getXPos(ch.toFloat())

                            // Check if this channel line is the current drag target
                            val isDragLine = activeDragSession?.let { session ->
                                session.band == selectedBand && session.currentDragChannel == ch
                            } ?: false

                            // Draw high contrast vertical grid lines
                            drawLine(
                                color = if (isDragLine) Color(0xFFFF3B30) else gridLineColor,
                                start = Offset(x, topMargin),
                                end = Offset(x, topMargin + chartHeight),
                                strokeWidth = if (isDragLine) 3.5f else 1.5f
                            )

                            // Channel tick label
                            val valTextResult = textMeasurer.measure(
                                text = "$ch",
                                style = TextStyle(
                                    color = axisTextColor,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            )
                            drawText(
                                textMeasurer = textMeasurer,
                                text = "$ch",
                                topLeft = Offset(x - valTextResult.size.width / 2f, topMargin + chartHeight + 12f),
                                style = TextStyle(
                                    color = axisTextColor,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            )
                        }

                        // Ch title label next to first channel number
                        val chResult = textMeasurer.measure(
                            text = "Ch",
                            style = TextStyle(
                                color = axisTextColor,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Normal
                            )
                        )
                        drawText(
                            textMeasurer = textMeasurer,
                            text = "Ch",
                            topLeft = Offset(leftMargin - chResult.size.width - 20f, topMargin + chartHeight + 12f),
                            style = TextStyle(
                                color = axisTextColor,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Normal
                            )
                        )

                        // Draw bottom floor axis line
                        drawLine(
                            color = Color.White.copy(alpha = 0.25f),
                            start = Offset(leftMargin, qY),
                            end = Offset(canvasWidth - rightMargin, qY),
                            strokeWidth = 1.5f
                        )

                        // Clear and collect bounds during drawing phase
                        tagBounds.clear()

                        // Draw network curves dynamically
                        filteredDisplayNetworks.forEach { net ->
                            val isHighlightRed = net.isConnected || 
                                net.belongsToSameRouter(activeConnectedSsid ?: lastKnownConnectedSsid, activeConnectedBssid ?: lastKnownConnectedBssid) ||
                                (activeSettingsBand == selectedBand && activeSettingsSsid != null && (net.ssid == activeSettingsSsid || net.belongsToSameRouter(activeSettingsSsid, null)))
                            val channelSpan = when (net.widthMhz) {
                                320 -> 32f
                                160 -> 16f
                                80 -> 8f
                                40 -> 4f
                                22 -> 2.2f
                                else -> 2f
                            }

                            // Dynamic center channel during drag and drop gesture
                            val rawCc = if (activeDragSession != null && activeDragSession?.band == selectedBand && (activeDragSession?.netSsid == net.ssid || net.belongsToSameRouter(activeDragSession?.netSsid, activeDragSession?.netBssid))) {
                                activeDragSession!!.currentDragChannel
                            } else if (pendingChannelChange != null && pendingChannelChange?.band == selectedBand && (pendingChannelChange?.netSsid == net.ssid || net.belongsToSameRouter(pendingChannelChange?.netSsid, pendingChannelChange?.netBssid))) {
                                pendingChannelChange!!.currentDragChannel
                            } else if (ssidChangingChannel != null && bandChangingChannel == selectedBand && targetChannelChangingChannel != null && (ssidChangingChannel == net.ssid || net.belongsToSameRouter(ssidChangingChannel, null))) {
                                targetChannelChangingChannel!!
                            } else {
                                null
                            }
                            val cC = if (rawCc != null) {
                                if (activeDragSession != null) {
                                    rawCc.toFloat()
                                } else {
                                    getStandardCenterChannel(rawCc, net.widthMhz, selectedBand).toFloat()
                                }
                            } else {
                                net.centerChannel.toFloat()
                            }

                            val peakDbm = net.currentDbm.value.coerceIn(-100f, -20f)
                            val peakX = getXPos(cC)
                            val peakYPct = (peakDbm - dbmMin) / dbmRange
                            val peakY = topMargin + (1f - peakYPct) * chartHeight

                            // Symmetric visual width on the screen
                            val visualHalfWidth = when (selectedBand) {
                                WifiBand.BAND_2_4_GHZ -> {
                                    val (minExt, maxExt) = Pair(1f - 2.5f, 14f + 2.5f)
                                    val chanSpanScale = maxExt - minExt
                                    (channelSpan / chanSpanScale) * chartWidth
                                }
                                WifiBand.BAND_5_GHZ -> {
                                    channelSpan * 0.02f * chartWidth
                                }
                                WifiBand.BAND_6_GHZ -> {
                                    val minChan = if (channels.isNotEmpty()) channels.first().toFloat() else 1f
                                    val maxChan = if (channels.isNotEmpty()) channels.last().toFloat() else 233f
                                    val (minExt, maxExt) = Pair(minChan - 4.5f, maxChan + 4.5f)
                                    val chanSpanScale = maxExt - minExt
                                    (channelSpan / chanSpanScale) * chartWidth
                                }
                            }

                            // Generate smooth parabolic path symmetrically
                            val path = Path()
                            val steps = 40
                            var isFirst = true
                            val startX = peakX - visualHalfWidth
                            val endX = peakX + visualHalfWidth

                            for (i in 0..steps) {
                                val stepPct = i.toFloat() / steps // 0f .. 1f
                                val offsetRatio = -1f + stepPct * 2f // -1f .. 1f
                                val xPos = peakX + offsetRatio * visualHalfWidth
                                
                                val calculatedDbm = -100f + (peakDbm + 100f) * (1f - offsetRatio.pow(2)).coerceAtLeast(0f)
                                val yPct = (calculatedDbm - dbmMin) / dbmRange
                                val yPos = topMargin + (1f - yPct) * chartHeight

                                if (isFirst) {
                                    path.moveTo(xPos, yPos)
                                    isFirst = false
                                } else {
                                    path.lineTo(xPos, yPos)
                                }
                            }

                            // Connect path endpoints with baseline
                            val fillPath = Path().apply {
                                addPath(path)
                                lineTo(endX, qY)
                                lineTo(startX, qY)
                                close()
                            }

                            // Determine alpha multiplier to fade out graph if it's currently changing channel
                            val isChangingThisSsid = (ssidChangingChannel == net.ssid)
                            val curveAlphaMultiplier = 1.0f

                            // Use clipRect inside canvas to restrict drawing strictly inside grid bounds
                            clipRect(
                                left = leftMargin,
                                top = topMargin,
                                right = canvasWidth - rightMargin,
                                bottom = qY
                            ) {
                                // Fill translucent gradient under peak
                                drawPath(
                                    path = fillPath,
                                    color = if (isHighlightRed) Color(0xFFFF3B30).copy(alpha = 0.16f * curveAlphaMultiplier) else net.color.copy(alpha = 0.08f * curveAlphaMultiplier)
                                )

                                // Draw vertical axis of the parabola in red for the router network, always
                                if (isHighlightRed) {
                                    drawLine(
                                        color = Color(0xFFFF3B30).copy(alpha = curveAlphaMultiplier),
                                        start = Offset(peakX, topMargin),
                                        end = Offset(peakX, qY),
                                        strokeWidth = 2.0f
                                    )
                                }

                                // Draw Curve Stroke
                                drawPath(
                                    path = path,
                                    color = if (isHighlightRed) Color(0xFFFF3B30).copy(alpha = curveAlphaMultiplier) else net.color.copy(alpha = curveAlphaMultiplier),
                                    style = Stroke(
                                        width = if (isHighlightRed) 4.5f else 2.5f,
                                        join = StrokeJoin.Round,
                                        cap = StrokeCap.Round
                                    )
                                )
                            }

                            // Network Label showing SSID and distance
                            if (peakX in leftMargin..(canvasWidth - rightMargin)) {
                                val distMeters = estimateDistance(peakDbm)
                                val labelText = "${net.ssid} (~${distMeters}m)"

                                val textColor = if (isHighlightRed) Color.White.copy(alpha = curveAlphaMultiplier) else net.color.copy(alpha = curveAlphaMultiplier)
                                val textLayoutResult = textMeasurer.measure(
                                    text = labelText,
                                    style = TextStyle(
                                        color = textColor,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )

                                val tw = textLayoutResult.size.width
                                val th = textLayoutResult.size.height

                                val tagX = peakX - tw / 2f
                                val tagY = peakY - th - 8f

                                if (isHighlightRed) {
                                    // Highlight name of connected router network in a solid red rounded box and keep bounds
                                    tagBounds.add(
                                        TagBound(
                                            netSsid = net.ssid,
                                            netBssid = net.bssid,
                                            centerChannel = net.centerChannel,
                                            band = selectedBand,
                                            rect = Rect(
                                                left = tagX - 10f,
                                                top = tagY - 4f,
                                                right = tagX + tw + 10f,
                                                bottom = tagY + th + 4f
                                            )
                                        )
                                    )

                                    drawRoundRect(
                                        color = Color(0xFFFF3B30).copy(alpha = curveAlphaMultiplier),
                                        topLeft = Offset(tagX - 10f, tagY - 4f),
                                        size = androidx.compose.ui.geometry.Size(tw + 20f, th + 8f),
                                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f)
                                    )
                                    drawText(
                                        textMeasurer = textMeasurer,
                                        text = labelText,
                                        topLeft = Offset(tagX, tagY),
                                        style = TextStyle(
                                            color = Color.White.copy(alpha = curveAlphaMultiplier),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )

                                    // Display the channel number at the TOP of the red vertical axis line (above grid)
                                    val displayChanNum = if (activeDragSession != null && activeDragSession?.band == selectedBand && (activeDragSession?.netSsid == net.ssid || net.belongsToSameRouter(activeDragSession?.netSsid, activeDragSession?.netBssid))) {
                                        activeDragSession!!.currentDragChannel
                                    } else if (pendingChannelChange != null && pendingChannelChange?.band == selectedBand && (pendingChannelChange?.netSsid == net.ssid || net.belongsToSameRouter(pendingChannelChange?.netSsid, pendingChannelChange?.netBssid))) {
                                        pendingChannelChange!!.currentDragChannel
                                    } else {
                                        net.centerChannel
                                    }
                                    val hasChannelMismatch = (activeDragSession == null && pendingChannelChange == null && ssidChangingChannel == null) && currentChannelVal > 0 && displayChanNum != currentChannelVal
                                    val chLabel = if (hasChannelMismatch) {
                                        if (locale == "ru") "Канал $currentChannelVal ($displayChanNum)" else "Ch $currentChannelVal ($displayChanNum)"
                                    } else {
                                        if (locale == "ru") "Канал $displayChanNum" else "Ch $displayChanNum"
                                    }
                                    val chLayoutResult = textMeasurer.measure(
                                        text = chLabel,
                                        style = TextStyle(
                                            color = Color(0xFFFF3B30).copy(alpha = curveAlphaMultiplier),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    val chw = chLayoutResult.size.width
                                    val chh = chLayoutResult.size.height
                                    drawText(
                                        textMeasurer = textMeasurer,
                                        text = chLabel,
                                        topLeft = Offset(peakX - chw / 2f, topMargin - chh - 4f),
                                        style = TextStyle(
                                            color = Color(0xFFFF3B30).copy(alpha = curveAlphaMultiplier),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                } else {
                                    drawText(
                                        textMeasurer = textMeasurer,
                                        text = labelText,
                                        topLeft = Offset(tagX, tagY),
                                        style = TextStyle(
                                            color = net.color.copy(alpha = curveAlphaMultiplier),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }
                        }
                    } // End of Canvas

                // Display equivalent styled Help Button on top with exact alignment
                val routerNet = filteredDisplayNetworks.find { 
                    it.isConnected || 
                    it.belongsToSameRouter(activeConnectedSsid ?: lastKnownConnectedSsid, activeConnectedBssid ?: lastKnownConnectedBssid) ||
                    (activeSettingsBand == selectedBand && activeSettingsSsid != null && (it.ssid == activeSettingsSsid || it.belongsToSameRouter(activeSettingsSsid, null)))
                }
                if (routerNet != null && currentChannelVal > 0) {
                    val displayChanNum = if (activeDragSession != null && activeDragSession?.band == selectedBand && (activeDragSession?.netSsid == routerNet.ssid || routerNet.belongsToSameRouter(activeDragSession?.netSsid, activeDragSession?.netBssid))) {
                        activeDragSession!!.currentDragChannel
                    } else if (pendingChannelChange != null && pendingChannelChange?.band == selectedBand && (pendingChannelChange?.netSsid == routerNet.ssid || routerNet.belongsToSameRouter(pendingChannelChange?.netSsid, pendingChannelChange?.netBssid))) {
                        pendingChannelChange!!.currentDragChannel
                    } else {
                        routerNet.centerChannel
                    }
                    val hasChannelMismatch = (activeDragSession == null && pendingChannelChange == null && ssidChangingChannel == null) && currentChannelVal > 0 && displayChanNum != currentChannelVal
                    if (hasChannelMismatch) {
                        val cC = displayChanNum.toFloat()
                        val minChan = if (channels.isNotEmpty()) channels.first().toFloat() else 1f
                        val maxChan = if (channels.isNotEmpty()) channels.last().toFloat() else 14f
                        val (minExt, maxExt) = when (selectedBand) {
                            WifiBand.BAND_2_4_GHZ -> Pair(1f - 2.5f, 14f + 2.5f)
                            WifiBand.BAND_5_GHZ -> Pair(36f - 18f, 165f + 16f)
                            WifiBand.BAND_6_GHZ -> Pair(minChan - 4.5f, maxChan + 4.5f)
                        }
                        val chanSpanScale = maxExt - minExt
                        val peakX = if (selectedBand == WifiBand.BAND_5_GHZ) {
                            leftMargin + get5GhzPositionPercent(cC) * chartWidth
                        } else {
                            leftMargin + ((cC - minExt) / chanSpanScale) * chartWidth
                        }
                        
                        val circleX = peakX
                        val estimatedChh = 13f * density.density
                        val visibleIconRadius = 12f * density.density
                        val safetyGap = 10f * density.density
                        val circleY = topMargin - estimatedChh - 4f - visibleIconRadius - safetyGap
                        
                        val circleXDp = with(density) { circleX.toDp() }
                        val circleYDp = with(density) { circleY.toDp() }
                        
                        var isTooltipFocused by remember { mutableStateOf(false) }
                        IconButton(
                            onClick = { showChannelMismatchExplanation = true },
                            modifier = Modifier
                                .absoluteOffset(
                                    x = circleXDp - 24.dp,
                                    y = circleYDp - 24.dp
                                )
                                .size(48.dp)
                                .onFocusChanged { isTooltipFocused = it.isFocused }
                                .then(
                                    if (isTv) {
                                        Modifier.border(
                                            width = 1.5.dp,
                                            color = if (isTooltipFocused) Color(0xFF5AC8FA) else Color.Transparent,
                                            shape = RoundedCornerShape(24.dp)
                                        )
                                    } else Modifier
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Help",
                                tint = Color(0xFF5AC8FA),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }

                if (routerNet != null) {
                    val minChan = if (channels.isNotEmpty()) channels.first().toFloat() else 1f
                    val maxChan = if (channels.isNotEmpty()) channels.last().toFloat() else 14f
                    val (minExt, maxExt) = when (selectedBand) {
                        WifiBand.BAND_2_4_GHZ -> Pair(1f - 2.5f, 14f + 2.5f)
                        WifiBand.BAND_5_GHZ -> Pair(36f - 18f, 165f + 16f)
                        WifiBand.BAND_6_GHZ -> Pair(minChan - 4.5f, maxChan + 4.5f)
                    }
                    val chanSpanScale = maxExt - minExt
                    val localGetXPos = { chan: Float ->
                        val pct = if (selectedBand == WifiBand.BAND_5_GHZ) {
                            get5GhzPositionPercent(chan)
                        } else {
                            (chan - minExt) / chanSpanScale
                        }
                        leftMargin + pct * chartWidth
                    }

                    val dbmMin = -100f
                    val dbmMax = -20f
                    val dbmRange = dbmMax - dbmMin
                    val peakDbm = routerNet.currentDbm.value.coerceIn(-100f, -20f)

                    val rawCc = if (activeDragSession != null && activeDragSession?.band == selectedBand && (activeDragSession?.netSsid == routerNet.ssid || routerNet.belongsToSameRouter(activeDragSession?.netSsid, activeDragSession?.netBssid))) {
                        activeDragSession!!.currentDragChannel
                    } else if (pendingChannelChange != null && pendingChannelChange?.band == selectedBand && (pendingChannelChange?.netSsid == routerNet.ssid || routerNet.belongsToSameRouter(pendingChannelChange?.netSsid, pendingChannelChange?.netBssid))) {
                        pendingChannelChange!!.currentDragChannel
                    } else if (ssidChangingChannel != null && bandChangingChannel == selectedBand && targetChannelChangingChannel != null && (ssidChangingChannel == routerNet.ssid || routerNet.belongsToSameRouter(ssidChangingChannel, null))) {
                        targetChannelChangingChannel!!
                    } else {
                        null
                    }
                    val cC = if (rawCc != null) {
                        if (activeDragSession != null) {
                            rawCc.toFloat()
                        } else {
                            getStandardCenterChannel(rawCc, routerNet.widthMhz, selectedBand).toFloat()
                        }
                    } else {
                        routerNet.centerChannel.toFloat()
                    }

                    val peakX = localGetXPos(cC)
                    val peakYPct = (peakDbm - dbmMin) / dbmRange
                    val peakY = topMargin + (1f - peakYPct) * chartHeight

                    val distMeters = estimateDistance(peakDbm)
                    val labelText = "${routerNet.ssid} (~${distMeters}m)"

                    val textLayoutResult = textMeasurer.measure(
                        text = labelText,
                        style = TextStyle(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    val tw = textLayoutResult.size.width
                    val th = textLayoutResult.size.height

                    val tagX = peakX - tw / 2f
                    val tagY = peakY - th - 8f

                    val tagXDp = with(density) { (tagX - 10f).toDp() }
                    val tagYDp = with(density) { (tagY - 4f).toDp() }
                    val tagWidthDp = with(density) { (tw + 20f).toDp() }
                    val tagHeightDp = with(density) { (th + 8f).toDp() }

                    var isTagFocused by remember { mutableStateOf(false) }
                    var lastOkClickTime by remember { mutableStateOf(0L) }
                    
                    Box(
                        modifier = Modifier
                            .absoluteOffset(x = tagXDp, y = tagYDp)
                            .size(width = tagWidthDp, height = tagHeightDp)
                            .onFocusChanged { isTagFocused = it.isFocused }
                            .focusable()
                            .then(
                                if (isTagFocused) {
                                    Modifier.border(
                                        width = 3.dp,
                                        color = Color(0xFF5AC8FA),
                                        shape = RoundedCornerShape(6.dp)
                                    )
                                } else Modifier
                            )
                            .onKeyEvent { keyEvent ->
                                if (keyEvent.type == KeyEventType.KeyDown) {
                                    when (keyEvent.key) {
                                        Key.DirectionLeft -> {
                                            val currentChVal = activeDragSession?.currentDragChannel ?: routerNet.centerChannel
                                            val currentIndex = channels.indexOf(currentChVal)
                                            if (currentIndex > 0) {
                                                val newChan = channels[currentIndex - 1]
                                                activeDragSession = DragSession(
                                                    netSsid = routerNet.ssid,
                                                    netBssid = routerNet.bssid,
                                                    startChannel = routerNet.centerChannel,
                                                    band = selectedBand,
                                                    currentDragChannel = newChan
                                                )
                                            }
                                            true
                                        }
                                        Key.DirectionRight -> {
                                            val currentChVal = activeDragSession?.currentDragChannel ?: routerNet.centerChannel
                                            val currentIndex = channels.indexOf(currentChVal)
                                            if (currentIndex < channels.size - 1) {
                                                val newChan = channels[currentIndex + 1]
                                                activeDragSession = DragSession(
                                                    netSsid = routerNet.ssid,
                                                    netBssid = routerNet.bssid,
                                                    startChannel = routerNet.centerChannel,
                                                    band = selectedBand,
                                                    currentDragChannel = newChan
                                                )
                                            }
                                            true
                                        }
                                        else -> {
                                            val isOKey = keyEvent.key == Key.O || 
                                                         run {
                                                             val uni = keyEvent.nativeKeyEvent.unicodeChar
                                                             uni == 'o'.code || uni == 'O'.code || uni == 'о'.code || uni == 'О'.code || uni == 'o'.uppercaseChar().code || uni == 'о'.uppercaseChar().code
                                                         }
                                            val isConfirm = isOKey || keyEvent.key == Key.DirectionCenter || keyEvent.key == Key.Enter || keyEvent.key == Key.NumPadEnter
                                            if (isConfirm) {
                                                val currentTime = java.lang.System.currentTimeMillis()
                                                if (currentTime - lastOkClickTime < 500) {
                                                    activeSettingsSsid = routerNet.ssid
                                                    activeSettingsBssid = routerNet.bssid
                                                    activeSettingsBand = selectedBand
                                                    fetchSsidSettings(routerNet.ssid, selectedBand)
                                                    activeDragSession = null
                                                    lastOkClickTime = 0L
                                                } else {
                                                    lastOkClickTime = currentTime
                                                    val session = activeDragSession
                                                    if (session != null) {
                                                        if (session.currentDragChannel != session.startChannel) {
                                                            pendingChannelChange = session
                                                        }
                                                        activeDragSession = null
                                                    }
                                                }
                                                true
                                            } else {
                                                false
                                            }
                                        }
                                    }
                                } else false
                            }
                    )
                }

                val infiniteTransition = rememberInfiniteTransition()
                val rotationAngle by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 360f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1200, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    )
                )

                // Elegant Glowing Floating action button overlaid at the bottom right corner
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 24.dp, end = 24.dp)
                        .size(56.dp)
                        .focusRequester(refreshButtonFocusRequester)
                        .onFocusChanged { isRefreshButtonFocused = it.isFocused }
                        .focusable()
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0xFF007AFF), Color(0xFF5AC8FA))
                            )
                        )
                        .clickable {
                            if (!isRefreshing) {
                                scope.launch {
                                    isRefreshing = true
                                    realWifiNetworks.clear() // Completely clear the chart/range graph
                                    ssidOverriddenChannels.clear()
                                    lastKnownConnectedSsid = null
                                    lastKnownConnectedBssid = null
                                    lastKnownConnectedBand = null
                                    activeConnectedSsid = null
                                    activeConnectedBssid = null
                                    activeConnectedBand = null
                                    activeSettingsSsid = null
                                    currentChannelVal = 0
                                    ssidWaitingForScan = null
                                    try {
                                        wifiManager.startScan()
                                    } catch (e: Exception) {}
                                    fetchRouterSsidSettingsForBand(selectedBand, realWifiNetworks)
                                    delay(1500)
                                    isRefreshing = false
                                    updateWifiManagerData()
                                }
                            }
                        }
                        .border(
                            width = if (isRefreshButtonFocused) 3.dp else 1.dp,
                            color = if (isRefreshButtonFocused) Color(0xFF5AC8FA) else Color.Transparent,
                            shape = CircleShape
                        )
                        .testTag("btn_refresh_wifi_analyzer_floating"),
                    contentAlignment = Alignment.Center
                ) {
                    if (isRefreshing) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawArc(
                                brush = Brush.sweepGradient(
                                    colors = listOf(
                                        Color(0xFFFF2D55),
                                        Color(0xFFFF9500),
                                        Color(0xFF4CD964),
                                        Color(0xFF5AC8FA),
                                        Color(0xFFFF2D55)
                                    )
                                ),
                                startAngle = rotationAngle,
                                sweepAngle = 280f,
                                useCenter = false,
                                style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                            )
                        }
                    } else {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Обновить",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }


            }
        }
    }

            val hintCardFocusRequester = remember { FocusRequester() }
            var isHintCardFocused by remember { mutableStateOf(false) }

            if (isLocationActive && routerNetworkUnderCurrentBand != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .focusRequester(hintCardFocusRequester)
                        .onFocusChanged { isHintCardFocused = it.isFocused }
                        .focusable()
                        .clickable {
                            activeSettingsSsid = routerNetworkUnderCurrentBand.ssid
                            activeSettingsBssid = routerNetworkUnderCurrentBand.bssid
                            activeSettingsBand = routerNetworkUnderCurrentBand.band
                            fetchSsidSettings(routerNetworkUnderCurrentBand.ssid, routerNetworkUnderCurrentBand.band)
                        }
                        .background(
                            if (isHintCardFocused) Color(0xFFFF3B30).copy(alpha = 0.25f) else Color(0xFFFF3B30).copy(alpha = 0.12f),
                            RoundedCornerShape(8.dp)
                        )
                        .border(
                            width = if (isHintCardFocused) 2.5.dp else 1.dp,
                            color = if (isHintCardFocused) Color(0xFF5AC8FA) else Color(0xFFFF3B30).copy(alpha = 0.35f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Hint",
                            tint = Color(0xFFFF3B30),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = getAnalyzerString("hint_text", locale, routerNetworkUnderCurrentBand.ssid),
                            style = TextStyle(
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    }

    if (showChannelMismatchExplanation) {
        val mismatchRouterNet = filteredDisplayNetworks.find { 
            it.isConnected || 
            it.belongsToSameRouter(activeConnectedSsid ?: lastKnownConnectedSsid, activeConnectedBssid ?: lastKnownConnectedBssid) ||
            (activeSettingsBand == selectedBand && activeSettingsSsid != null && (it.ssid == activeSettingsSsid || it.belongsToSameRouter(activeSettingsSsid, null)))
        }
        val mismatchDisplayChan = if (mismatchRouterNet != null) {
            if (activeDragSession != null && activeDragSession?.band == selectedBand && (activeDragSession?.netSsid == mismatchRouterNet.ssid || mismatchRouterNet.belongsToSameRouter(activeDragSession?.netSsid, activeDragSession?.netBssid))) {
                activeDragSession!!.currentDragChannel
            } else if (pendingChannelChange != null && pendingChannelChange?.band == selectedBand && (pendingChannelChange?.netSsid == mismatchRouterNet.ssid || mismatchRouterNet.belongsToSameRouter(pendingChannelChange?.netSsid, pendingChannelChange?.netBssid))) {
                pendingChannelChange!!.currentDragChannel
            } else {
                mismatchRouterNet.centerChannel
            }
        } else {
            currentChannelVal
        }

        val titleText = if (locale == "ru") "Несоответствие каналов" else "Channel Mismatch Explanation"
        val concreteWidthInfo = run {
            var w = mismatchRouterNet?.widthMhz ?: 0
            if (w <= 0) {
                val mode = currentHtmode.uppercase()
                w = if (mode.contains("160")) 160
                    else if (mode.contains("80")) 80
                    else if (mode.contains("40")) 40
                    else if (mode.contains("20")) 20
                    else {
                        when (selectedBand) {
                            WifiBand.BAND_2_4_GHZ -> 20
                            WifiBand.BAND_5_GHZ -> 80
                            WifiBand.BAND_6_GHZ -> 80
                        }
                    }
            }
            w
        }
        val bodyText = if (locale == "ru") {
            "График отображается на канале $mismatchDisplayChan (на роутере применен канал $currentChannelVal), так как роутер работает в режиме широкой полосы пропускания ($concreteWidthInfo МГц) и использует этот канал в качестве основного управляющего контрольного канала (Primary Control Channel).\n\n" +
            "Именно по нему передаются служебные Beacon-кадры, данные о синхронизации и ассоциировании клиентов. Хотя роутер физически занимает также соседние каналы для расширения полосы (до $currentChannelVal в скобках), график в анализаторе строится относительно контрольного канала. Это технически корректное поведение для точной оценки загруженности радиоэфира."
        } else {
            "The graph displays at channel $mismatchDisplayChan (router is configured to channel $currentChannelVal) because the router is operating on a wide channel bandwidth ($concreteWidthInfo MHz) and selects this specific channel as its Primary Control Channel.\n\n" +
            "Management Beacons, synch-frames, and connection negotiations occur strictly on the Control Channel. Although the hardware occupies additional secondary channels to expand bandwidth (indicated in parentheses), the Wi-Fi analyzer maps details relative to the Control Channel to ensure rigorous, technically correct spectrum monitoring."
        }

        androidx.compose.ui.window.Dialog(
            onDismissRequest = { showChannelMismatchExplanation = false },
            properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
        ) {
            val dialogFocusRequester = remember { FocusRequester() }
            var isDialogFocused by remember { mutableStateOf(false) }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .focusRequester(dialogFocusRequester)
                        .onFocusChanged { isDialogFocused = it.isFocused }
                        .focusable(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Mismatch Explanation",
                        tint = Color(0xFF5AC8FA),
                        modifier = Modifier.size(40.dp)
                    )

                    Text(
                        text = titleText,
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    Text(
                        text = bodyText,
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            lineHeight = 20.sp
                        ),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Justify
                    )

                    Button(
                        onClick = { showChannelMismatchExplanation = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007AFF)),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Text(
                            text = if (locale == "ru") "Понятно" else "Got It",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            LaunchedEffect(Unit) {
                try {
                    dialogFocusRequester.requestFocus()
                } catch (e: Exception) {}
            }
        }
    }

    if (activeHelpDialog) {
        val titleText = TestTabLocalizations.getDialogTitle("wifi_analyzer", locale)
        val bodyText = TestTabLocalizations.getDialogBody("wifi_analyzer_fullscreen", locale)

        androidx.compose.ui.window.Dialog(
            onDismissRequest = { activeHelpDialog = false },
            properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
        ) {
            val scrollState = rememberScrollState()
            val dialogFocusRequester = remember { FocusRequester() }
            var isDialogFocused by remember { mutableStateOf(false) }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
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
                                            scope.launch { scrollState.animateScrollBy(150f) }
                                            true
                                        } else false
                                    }
                                    Key.DirectionUp -> {
                                        if (scrollState.value > 0) {
                                            scope.launch { scrollState.animateScrollBy(-150f) }
                                            true
                                        } else false
                                    }
                                    else -> false
                                }
                            } else false
                        },
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = titleText,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    LaunchedEffect(Unit) {
                        if (isTv) {
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
                                        .onFocusChanged { isDialogFocused = it.isFocused }
                                } else Modifier
                            )
                            .verticalScroll(scrollState),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = bodyText,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Button(
                        onClick = { activeHelpDialog = false },
                        modifier = Modifier.align(Alignment.End),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = if (locale == "ru") "Закрыть" else "Close")
                    }
                }
            }
        }
    }

    pendingChannelChange?.let { pending ->
        val textDesc = getAnalyzerString("confirm_body", locale, pending.netSsid, "${pending.startChannel}", "${pending.currentDragChannel}")
        androidx.compose.ui.window.Dialog(
            onDismissRequest = { pendingChannelChange = null },
            properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
        ) {
            val focusRequesterConfirm = remember { FocusRequester() }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = getAnalyzerString("confirm_title", locale),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = textDesc,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    val warningText = getChannelWarning(pending.currentDragChannel, pending.band, locale)
                    if (warningText != null) {
                        Text(
                            text = warningText,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFFFF9500), // Vibrant amber warning color
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = { pendingChannelChange = null },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(text = getAnalyzerString("no", locale))
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Button(
                            onClick = {
                                val changeSession = pending
                                pendingChannelChange = null
                                if (viewModel != null) {
                                    isApplyingChannelChange = true
                                    applyChannelMessage = if (locale == "ru") "Применение настроек... Пожалуйста, подождите" else "Applying settings... Please wait"
                                    
                                    val radioDevice = if (changeSession.band == WifiBand.BAND_5_GHZ) "radio1" else "radio0"
                                    fetchSsidDataBeforeChange(changeSession.netSsid ?: "", changeSession.band) {
                                        val actualRadioDevice = if (detectedDeviceSection.isNotEmpty()) {
                                            detectedDeviceSection.removePrefix("wireless.")
                                        } else {
                                            radioDevice
                                        }
                                        val command = "uci set wireless.$actualRadioDevice.channel=${changeSession.currentDragChannel} && uci commit wireless && wifi"
                                        
                                        ssidChangingChannel = changeSession.netSsid
                                        targetChannelChangingChannel = changeSession.currentDragChannel
                                        if (changeSession.netSsid != null) {
                                            ssidOverriddenChannels[changeSession.netSsid] = changeSession.currentDragChannel
                                        }
                                        bandChangingChannel = changeSession.band
                                        lastChangeTime = System.currentTimeMillis()
                                        ssidWaitingForScan = changeSession.netSsid
                                        
                                        viewModel.executeConsoleCommandDirect(command) { res ->
                                            updateWifiManagerData()
                                            changeSession.netSsid?.let { silentFetchRouterSsidSettings(it) }
                                        }
                                    }
                                } else {
                                    updateWifiManagerData()
                                }
                            },
                            modifier = Modifier.focusRequester(focusRequesterConfirm),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF3B30))
                        ) {
                            Text(text = getAnalyzerString("yes", locale), color = Color.White)
                        }

                        LaunchedEffect(Unit) {
                            if (isTv) {
                                try {
                                    focusRequesterConfirm.requestFocus()
                                } catch (e: Exception) {}
                            }
                        }
                    }
                }
            }
        }
    }

    if (isApplyingChannelChange || ssidChangingChannel != null || applyChannelMessage != null) {
        androidx.compose.ui.window.Dialog(
            onDismissRequest = {},
            properties = androidx.compose.ui.window.DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false, usePlatformDefaultWidth = false)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator(color = Color(0xFFFF3B30))
                    val textToDisplay = applyChannelMessage ?: (if (locale == "ru") "Применение настроек... Пожалуйста, подождите" else "Applying settings... Please wait")
                    Text(
                        text = textToDisplay,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }
    } else if (ssidWaitingForScan != null) {
        androidx.compose.ui.window.Dialog(
            onDismissRequest = {},
            properties = androidx.compose.ui.window.DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false, usePlatformDefaultWidth = false)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator(color = Color(0xFFFF3B30))
                    val textToDisplay = if (locale == "ru") "Идет сканирование сетей..." else "Scanning networks..."
                    Text(
                        text = textToDisplay,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }
    }

    if (showSettingsDialog) {
        androidx.compose.ui.window.Dialog(
            onDismissRequest = { if (!isFetchingSettings) showSettingsDialog = false },
            properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
        ) {
            val focusRequesterSettings = remember { FocusRequester() }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .then(if (isTv) Modifier.fillMaxHeight(0.85f) else Modifier.wrapContentHeight()),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .then(if (isTv) Modifier.verticalScroll(rememberScrollState()) else Modifier),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = getSettingsString("settings_title", locale, activeSettingsSsid ?: ""),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    if (isFetchingSettings) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            CircularProgressIndicator(color = Color(0xFFFF3B30))
                            Text(
                                text = getSettingsString("fetching", locale),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    } else if (settingsError != null) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Error",
                                tint = Color(0xFFFF3B30),
                                modifier = Modifier.size(36.dp)
                            )
                            Text(
                                text = settingsError ?: "",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFFFF3B30),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            Button(
                                onClick = { showSettingsDialog = false },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF3B30))
                            ) {
                                Text(text = getAnalyzerString("no", locale), color = Color.White)
                            }
                        }
                    } else {
                        var isCancelFocused by remember { mutableStateOf(false) }
                        var isSaveFocused by remember { mutableStateOf(false) }
                        val cancelFocusRequester = remember { FocusRequester() }
                        val saveFocusRequester = remember { FocusRequester() }
                        val channelFocusRequester = remember { FocusRequester() }
                        var pwdInput by remember(currentPassword) { mutableStateOf(currentPassword) }
                        var encryptionInput by remember(currentEncryption) { mutableStateOf(currentEncryption) }
                        var encryptionDropdownExpanded by remember { mutableStateOf(false) }
                        val encryptionOptions = remember {
                            listOf("psk2", "sae", "sae-mixed", "psk-mixed", "psk", "none")
                        }
                        fun getEncryptionDisplayName(enc: String): String {
                            return when (enc) {
                                "psk2" -> "WPA2-PSK (AES)"
                                "sae" -> "WPA3-SAE (AES)"
                                "sae-mixed" -> "WPA2-PSK/WPA3-SAE Mixed"
                                "psk-mixed" -> "WPA/WPA2-PSK Mixed"
                                "psk" -> "WPA-PSK"
                                "none" -> getSettingsString("encryption_none", locale)
                                else -> enc
                            }
                        }
                        var txPowerInput by remember(currentTxPower) { mutableStateOf(currentTxPower) }
                        val settingsChannels = remember(activeSettingsBand) {
                            when (activeSettingsBand ?: WifiBand.BAND_2_4_GHZ) {
                                WifiBand.BAND_2_4_GHZ -> (1..14).toList()
                                WifiBand.BAND_5_GHZ -> listOf(36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144, 149, 153, 157, 161, 165)
                                WifiBand.BAND_6_GHZ -> (1..233 step 4).toList()
                            }
                        }
                        var selectedChannelInput by remember(currentChannelVal) { mutableStateOf(currentChannelVal) }
                        var dropdownExpanded by remember { mutableStateOf(false) }

                        var widthInput by remember(currentHtmode) { mutableStateOf(currentHtmode) }
                        var countryInput by remember(currentCountry) { mutableStateOf(currentCountry) }

                        val widthOptions = remember(activeSettingsBand) {
                            if (activeSettingsBand == WifiBand.BAND_2_4_GHZ) {
                                listOf("auto", "HT20", "HT40")
                            } else {
                                listOf("auto", "HT20", "HT40", "VHT20", "VHT40", "VHT80", "VHT160", "HE20", "HE40", "HE80", "HE160")
                            }
                        }
                        var widthDropdownExpanded by remember { mutableStateOf(false) }

                        val countryOptions = remember {
                            listOf("US", "DE", "FR", "RU", "UA", "BY", "CN", "JP", "GB", "CA", "AU", "KR", "BR")
                        }
                        var countryDropdownExpanded by remember { mutableStateOf(false) }

                        val txPowerOptions = availableTxPowers
                        var txPowerDropdownExpanded by remember { mutableStateOf(false) }

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Password Input
                            OutlinedTextField(
                                value = pwdInput,
                                onValueChange = { pwdInput = it },
                                label = { Text(text = getSettingsString("password_label", locale)) },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFFFF3B30),
                                    focusedLabelColor = Color(0xFFFF3B30),
                                    unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                                    unfocusedLabelColor = Color.White.copy(alpha = 0.6f)
                                )
                            )

                            // Encryption Input Dropdown
                            Box(modifier = Modifier.fillMaxWidth()) {
                                OutlinedTextField(
                                    value = getEncryptionDisplayName(encryptionInput),
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text(text = getSettingsString("encryption_label", locale)) },
                                    modifier = Modifier.fillMaxWidth().focusProperties { canFocus = false },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFFFF3B30),
                                        focusedLabelColor = Color(0xFFFF3B30),
                                        unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                                        unfocusedLabelColor = Color.White.copy(alpha = 0.6f)
                                    )
                                )
                                var isEncFocused by remember { mutableStateOf(false) }
                                Box(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .clip(RoundedCornerShape(12.dp))
                                        .onFocusChanged { isEncFocused = it.isFocused }
                                        .clickable { encryptionDropdownExpanded = true }
                                        .border(
                                            width = if (isEncFocused) 2.5.dp else 0.dp,
                                            color = if (isEncFocused) Color(0xFF5AC8FA) else Color.Transparent,
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                )

                                DropdownMenu(
                                    expanded = encryptionDropdownExpanded,
                                    onDismissRequest = { encryptionDropdownExpanded = false },
                                    modifier = Modifier
                                        .fillMaxWidth(0.8f)
                                        .background(MaterialTheme.colorScheme.surface)
                                ) {
                                    encryptionOptions.forEach { enc ->
                                        DropdownMenuItem(
                                            text = { Text(text = getEncryptionDisplayName(enc), color = MaterialTheme.colorScheme.onSurface) },
                                            onClick = {
                                                encryptionInput = enc
                                                encryptionDropdownExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            // TxPower Input Dropdown with step 2 DB
                            Box(modifier = Modifier.fillMaxWidth()) {
                                OutlinedTextField(
                                    value = if (txPowerInput.lowercase() == "auto") "auto" else "$txPowerInput dBm",
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text(text = getSettingsString("txpower_label", locale)) },
                                    modifier = Modifier.fillMaxWidth().focusProperties { canFocus = false },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFFFF3B30),
                                        focusedLabelColor = Color(0xFFFF3B30),
                                        unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                                        unfocusedLabelColor = Color.White.copy(alpha = 0.6f)
                                    )
                                )
                                var isTxPowerFocused by remember { mutableStateOf(false) }
                                Box(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .clip(RoundedCornerShape(12.dp))
                                        .onFocusChanged { isTxPowerFocused = it.isFocused }
                                        .clickable { txPowerDropdownExpanded = true }
                                        .border(
                                            width = if (isTxPowerFocused) 2.5.dp else 0.dp,
                                            color = if (isTxPowerFocused) Color(0xFF5AC8FA) else Color.Transparent,
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                )

                                DropdownMenu(
                                    expanded = txPowerDropdownExpanded,
                                    onDismissRequest = { txPowerDropdownExpanded = false },
                                    modifier = Modifier
                                        .fillMaxWidth(0.8f)
                                        .heightIn(max = 240.dp)
                                        .background(MaterialTheme.colorScheme.surface)
                                ) {
                                    txPowerOptions.forEach { pwr ->
                                        DropdownMenuItem(
                                            text = { Text(text = if (pwr == "auto") "auto" else "$pwr dBm", color = MaterialTheme.colorScheme.onSurface) },
                                            onClick = {
                                                txPowerInput = pwr
                                                txPowerDropdownExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            // Width Input Dropdown
                            Box(modifier = Modifier.fillMaxWidth()) {
                                OutlinedTextField(
                                    value = widthInput,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text(text = getSettingsString("width_label", locale)) },
                                    modifier = Modifier.fillMaxWidth().focusProperties { canFocus = false },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFFFF3B30),
                                        focusedLabelColor = Color(0xFFFF3B30),
                                        unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                                        unfocusedLabelColor = Color.White.copy(alpha = 0.6f)
                                    )
                                )
                                var isWidthFocused by remember { mutableStateOf(false) }
                                Box(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .clip(RoundedCornerShape(12.dp))
                                        .onFocusChanged { isWidthFocused = it.isFocused }
                                        .clickable { widthDropdownExpanded = true }
                                        .border(
                                            width = if (isWidthFocused) 2.5.dp else 0.dp,
                                            color = if (isWidthFocused) Color(0xFF5AC8FA) else Color.Transparent,
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                )

                                DropdownMenu(
                                    expanded = widthDropdownExpanded,
                                    onDismissRequest = { widthDropdownExpanded = false },
                                    modifier = Modifier
                                        .fillMaxWidth(0.8f)
                                        .background(MaterialTheme.colorScheme.surface)
                                ) {
                                    widthOptions.forEach { w ->
                                        DropdownMenuItem(
                                            text = { Text(text = w, color = MaterialTheme.colorScheme.onSurface) },
                                            onClick = {
                                                widthInput = w
                                                widthDropdownExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            // Country Input Dropdown
                            Box(modifier = Modifier.fillMaxWidth()) {
                                OutlinedTextField(
                                    value = countryInput,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text(text = getSettingsString("country_label", locale)) },
                                    modifier = Modifier.fillMaxWidth().focusProperties { canFocus = false },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFFFF3B30),
                                        focusedLabelColor = Color(0xFFFF3B30),
                                        unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                                        unfocusedLabelColor = Color.White.copy(alpha = 0.6f)
                                    )
                                )
                                var isCountryFocused by remember { mutableStateOf(false) }
                                Box(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .clip(RoundedCornerShape(12.dp))
                                        .onFocusChanged { isCountryFocused = it.isFocused }
                                        .clickable { countryDropdownExpanded = true }
                                        .border(
                                            width = if (isCountryFocused) 2.5.dp else 0.dp,
                                            color = if (isCountryFocused) Color(0xFF5AC8FA) else Color.Transparent,
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                )

                                DropdownMenu(
                                    expanded = countryDropdownExpanded,
                                    onDismissRequest = { countryDropdownExpanded = false },
                                    modifier = Modifier
                                        .fillMaxWidth(0.8f)
                                        .heightIn(max = 240.dp)
                                        .background(MaterialTheme.colorScheme.surface)
                                ) {
                                    val combinedOptions = if (countryInput.isNotEmpty() && !countryOptions.contains(countryInput)) {
                                        listOf(countryInput) + countryOptions
                                    } else {
                                        countryOptions
                                    }
                                    combinedOptions.forEach { c ->
                                        DropdownMenuItem(
                                            text = { Text(text = c, color = MaterialTheme.colorScheme.onSurface) },
                                            onClick = {
                                                countryInput = c
                                                countryDropdownExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            // Channel Selection Dropdown Menu
                            Box(modifier = Modifier.fillMaxWidth()) {
                                OutlinedTextField(
                                    value = if (selectedChannelInput > 0) "${getSettingsString("channel_label", locale)}: $selectedChannelInput" else getSettingsString("channel_label", locale),
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text(text = getSettingsString("channel_title", locale)) },
                                    modifier = Modifier.fillMaxWidth().focusProperties { canFocus = false },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFFFF3B30),
                                        focusedLabelColor = Color(0xFFFF3B30),
                                        unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                                        unfocusedLabelColor = Color.White.copy(alpha = 0.6f)
                                    )
                                )
                                // Transparent highlight layer over full field to capture click
                                var isChanFocused by remember { mutableStateOf(false) }
                                Box(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .clip(RoundedCornerShape(12.dp))
                                        .focusRequester(channelFocusRequester)
                                        .onFocusChanged { isChanFocused = it.isFocused }
                                        .clickable { dropdownExpanded = true }
                                        .border(
                                            width = if (isChanFocused) 2.5.dp else 0.dp,
                                            color = if (isChanFocused) Color(0xFF5AC8FA) else Color.Transparent,
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                )

                                DropdownMenu(
                                    expanded = dropdownExpanded,
                                    onDismissRequest = { dropdownExpanded = false },
                                    modifier = Modifier
                                        .fillMaxWidth(0.8f)
                                        .heightIn(max = 240.dp)
                                        .background(MaterialTheme.colorScheme.surface)
                                ) {
                                    settingsChannels.forEach { ch ->
                                        DropdownMenuItem(
                                            text = { Text(text = "${getSettingsString("channel_label", locale)}: $ch", color = MaterialTheme.colorScheme.onSurface) },
                                            onClick = {
                                                selectedChannelInput = ch
                                                dropdownExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(
                                onClick = { showSettingsDialog = false },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .focusRequester(cancelFocusRequester)
                                    .onFocusChanged { isCancelFocused = it.isFocused }
                                    .focusable()
                                    .onKeyEvent { keyEvent ->
                                        if (keyEvent.type == KeyEventType.KeyDown) {
                                            when (keyEvent.key) {
                                                Key.DirectionRight -> {
                                                    try {
                                                        saveFocusRequester.requestFocus()
                                                    } catch (_: Exception) {}
                                                    true
                                                }
                                                else -> false
                                            }
                                        } else false
                                    }
                                    .border(
                                        width = 2.dp,
                                        color = if (isCancelFocused) Color.White else Color.Transparent,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                            ) {
                                Text(
                                    text = getAnalyzerString("no", locale),
                                    color = if (isCancelFocused) Color.White else Color.White.copy(alpha = 0.6f)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Button(
                                onClick = {
                                    saveSsidSettings(pwdInput, txPowerInput, selectedChannelInput, widthInput, countryInput, encryptionInput)
                                },
                                modifier = Modifier
                                    .focusRequester(saveFocusRequester)
                                    .onFocusChanged { isSaveFocused = it.isFocused }
                                    .focusable()
                                    .onKeyEvent { keyEvent ->
                                        if (keyEvent.type == KeyEventType.KeyDown) {
                                            when (keyEvent.key) {
                                                Key.DirectionLeft -> {
                                                    try {
                                                        cancelFocusRequester.requestFocus()
                                                    } catch (_: Exception) {}
                                                    true
                                                }
                                                else -> false
                                            }
                                        } else false
                                    }
                                    .border(
                                        width = 2.dp,
                                        color = if (isSaveFocused) Color.White else Color.Transparent,
                                        shape = RoundedCornerShape(12.dp)
                                    ),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSaveFocused) Color(0xFFFF453A) else Color(0xFFFF3B30)
                                )
                            ) {
                                Text(text = getSettingsString("save", locale), color = Color.White)
                            }
                        }

                        LaunchedEffect(Unit) {
                            try {
                                channelFocusRequester.requestFocus()
                            } catch (e: Exception) {}
                        }
                    }
                }
            }
        }
    }
}

fun getChannelWarning(channel: Int, band: WifiBand, lang: String): String? {
    return when (band) {
        WifiBand.BAND_2_4_GHZ -> {
            if (channel in 12..13) {
                when (lang) {
                    "ru" -> "⚠️ Каналы 12 и 13 запрещены или ограничены по мощности в США (FCC), но разрешены в Европе и СНГ."
                    "uk" -> "⚠️ Канали 12 та 13 заборонені або обмежені за потужністю в США (FCC), але дозволені в Європі та СНД."
                    "be" -> "⚠️ Каналы 12 і 13 забаронены ці абмежаваны па магутнасці ў ЗША (FCC), але дазволены ў Еўропе і СНД."
                    "de" -> "⚠️ Die Kanäle 12 und 13 sind in den USA (FCC) eingeschränkt oder verboten, in Europa jedoch erlaubt."
                    "es" -> "⚠️ Los canales 12 y 13 están prohibidos o restringidos en EE. UU. (FCC), pero permitidos en Europa."
                    "fr" -> "⚠️ Les canaux 12 et 13 sont interdits ou restreints aux USA (FCC), mais autorisés en Europe."
                    "it" -> "⚠️ I canali 12 e 13 sono proibiti o limitati negli USA (FCC), ma ammessi in Europa."
                    "pt" -> "⚠️ Os canais 12 e 13 são proibidos os limitados nos EUA (FCC), mas regulamentados na Europa."
                    "da" -> "⚠️ Kanal 12 og 13 er begrænset eller forbudt i USA (FCC), men tilladt i Europa."
                    "fi" -> "⚠️ Kanavat 12 ja 13 ovat kiellettyjä tai rajoitettuja Yhdysvalloissa (FCC), mutta sallittuja Euroopassa."
                    "kk" -> "⚠️ 12 және 13 арналар АҚШ-та (FCC) шектелген немесе тыйым салынған, бірақ Еуропада рұқсат етілген."
                    "lt" -> "⚠️ 12 ir 13 kanalai yra draudžiami arba ribojami JAV (FCC), bet leidžiami Europoje."
                    "lv" -> "⚠️ 12. un 13. kanāls ir aizliegts vai ierobežots ASV (FCC), bet atļauts Eiropā."
                    "sv" -> "⚠️ Kanal 12 och 13 är begränsade eller förbjudna i USA (FCC), men tillåtna i Europa."
                    else -> "⚠️ Channels 12 and 13 are restricted or forbidden in the USA (FCC), but allowed in Europe and CIS."
                }
            } else if (channel == 14) {
                when (lang) {
                    "ru" -> "⚠️ Канал 14 разрешен только в Японии для устаревшего стандарта 802.11b. Не поддерживается в большинстве стран мира."
                    "uk" -> "⚠️ Канал 14 дозволений тільки в Японії для застарілого стандарту 802.11b. Не підтримується в більшості країн світу."
                    "be" -> "⚠️ Канал 14 дазволены толькі ў Японіі для састарэлага стандарту 802.11b. Не падтрымліваецца ў большасць краін свету."
                    "de" -> "⚠️ Kanal 14 ist nur in Japan für den alten Standard 802.11b zugelassen. In den meisten Ländern nicht unterstützt."
                    "es" -> "⚠️ El canal 14 solo se permite en Japón para el estándar 802.11b heredado. No es compatible en la mayoría de los países."
                    "fr" -> "⚠️ Le canal 14 est uniquement autorisé au Japon pour l'ancien standard 802.11b. Non supporté dans la plupart des pays."
                    "it" -> "⚠️ Il canale 14 è consentito solo in Giappone per il vecchio standard 802.11b. Non è supportato nella maggior parte dei paesi."
                    "pt" -> "⚠️ O canal 14 só é permitido no Japão para o antigo padrão 802.11b. Não suportado na maioria dos países."
                    "da" -> "⚠️ Kanal 14 er kun tilladt i Japan til den ældre 802.11b-standard. understøttes ikke i de fleste lande."
                    "fi" -> "⚠️ Kanava 14 on sallittu vain Japanissa vanhaa 802.11b-standardia varten. Ei tueta useimmissa maissa."
                    "kk" -> "⚠️ 14-ші арна тек Жапонияда ескірген 802.11b стандарты үшін рұқсат етілген. Көптеген елдерде қолданбайды."
                    "lt" -> "⚠️ 14 kanalas leidžiamas tik Japonijoje senajam 802.11b standartui. Nepalaikomas daugelyje šalių."
                    "lv" -> "⚠️ 14. kanāls ir atļauts tikai Japānā vecajam 802.11b standartam. Netiek atbalstīts lielākajā daļā valstu."
                    "sv" -> "⚠️ Kanal 14 är endast tillåten i Japan för den äldre standarden 802.11b. Stöds inte i de flesta länder."
                    else -> "⚠️ Channel 14 is allowed only in Japan for the legacy 802.11b standard. Unsupported in most other countries."
                }
            } else null
        }
        WifiBand.BAND_5_GHZ -> {
            if (channel in listOf(52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)) {
                when (lang) {
                    "ru" -> "⚠️ Этот канал входит в диапазон DFS. Он перекрывается с военными и погодными радарами. Роутер может автоматически сменить канал или временно отключить сеть при обнаружении радаров."
                    "uk" -> "⚠️ Цей канал входить в діапазон DFS. Він перекривається з військовими та погодними радарами. Роутер може автоматично змінити канал або тимчасово вимкнути мережу при виявленні радарів."
                    "be" -> "⚠️ Гэты канал уваходзіць у дыяпазон DFS. Ён перакрываецца з ваеннымі і надвор'евымі радарамі. Роўтэр можа аўтаматычна змяніць канал ці часова адключыць сетку пры выяўленні радараў."
                    "de" -> "⚠️ Dieser Kanal liegt im DFS-Bereich. Er überschneidet sich mit Militär- und Wetterradaren. Der Router kann den Kanal automatisch wechseln oder das Netzwerk vorübergehend trennen."
                    "es" -> "⚠️ Este canal pertenece al rango DFS. Se superpone con radares militares y meteorológicos. El enrutador puede cambiar de canal automáticamente o desconectar temporalmente la red si detecta radares."
                    "fr" -> "⚠️ Ce canal fait partie de la bande DFS. Il se chevauche avec les radars militaires et météo. Le routeur peut changer de canal automatiquement ou désactiver temporairement le réseau s'il détecte un radar."
                    "it" -> "⚠️ Questo canale è nell'intervallo DFS. Si sovrappone a radar militari e meteorologici. Il router potrebbe cambiare canale automaticamente o disattivare temporaneamente la rete se rileva radar."
                    "pt" -> "⚠️ Este canal pertence à gama DFS. Sobrepõe-se a radares militares e meteorológicos. O router pode mudar de canal automaticamente ou desligar temporariamente a rede se detetar radares."
                    "da" -> "⚠️ Denne kanal er i DFS-området. Den overlapper med militær- og vejrradarer. Routeren kan automatisk skifte kanal eller deaktivere netværket midlertidigt."
                    "fi" -> "⚠️ Tämä kanava kuuluu DFS-alueeseen. Se menee päällekkäin sotilas- ja sääsäiden kanssa. Reititin voi vaihtaa kanavaa automaattisesti tai katkaista verkon tilapäisesti."
                    "kk" -> "⚠️ Бұл арна DFS ауқымына жатады. Ол әскери және ауа райы радарларымен қабаттасады. Радар анықталған жағдайда роутер автоматты түрде арнаны өзгертеді."
                    "lt" -> "⚠️ Šis kanalas patenka į DFS diapazoną. Jis sutampa su kariniais ir meteorologiniais radarai. Reititin gali automatiškai pakeisti kanalą arba laikinai atjungti tinklą."
                    "lv" -> "⚠️ Šis kanāls ir DFS diapazonā. Tas pārklājas ar militārajiem un laikapstākļu radariem. Reitings var automātiski mainīt kanālu vai uz laiku atslēgt tīklu."
                    "sv" -> "⚠️ Denna kanal ligger i DFS-intervallet. Den överlappar med militär- och väderradarer. Routern kan automatiskt byta kanal eller tillfälligt stänga av nätverket."
                    else -> "⚠️ This is a DFS (Dynamic Frequency Selection) channel. It overlaps with military and weather radars. Your router may dynamically change channel or temporarily drop connection if radar is detected."
                }
            } else if (channel in 149..165) {
                when (lang) {
                    "ru" -> "⚠️ Каналы 149-165 требуют лицензирования или не поддерживаются в некоторых странах Европы и Азии."
                    "uk" -> "⚠️ Канали 149-165 вимагають ліцензування або не підтримуються в деяких країнах Європи та Азії."
                    "be" -> "⚠️ Каналы 149-165 патрабуюць ліцэнзавання ці не падтрымліваюцца ў некаторых краінах Еўропы і Азіі."
                    "de" -> "⚠️ Die Kanäle 149-165 erfordern in einigen europäischen oder asiatischen Ländern eine Lizenzierung oder werden nicht unterstützt."
                    "es" -> "⚠️ Los canales 149-165 requieren licencia o no son compatibles en algunos países de Europa y Asia."
                    "fr" -> "⚠️ Les canaux 149-165 nécessitent une licence ou ne sont pas supportés dans certains pays d'Europe et d'Asie."
                    "it" -> "⚠️ I canali 149-165 richiedono licenze o non sono supportati in alcuni paesi europei e asiatici."
                    "pt" -> "⚠️ Os canais 149-165 exigem licenciamento ou não são suportados em alguns países da Europa e Ásia."
                    "da" -> "⚠️ Kanal 149-165 kræver licens eller understøttes ikke i nogle lande i Europa og Asien."
                    "fi" -> "⚠️ Kanavat 149-165 vaativat lisensointia tai niitä ei tueta joissakin Euroopan ja Aasian maissa."
                    "kk" -> "⚠️ 149-165 арналары Еуропа мен Азияның кейбір елдерінде лицензиялауды талап етеді немесе мүлдем қолданбайды."
                    "lt" -> "⚠️ 149-165 kanalams reikia licencijos arba jie nepalaikomi kai kuriose Europos ir Azijos šalyse."
                    "lv" -> "⚠️ 149.-165. kanālam ir nepieciešama licencēšana vai tas netiek atbalstīts dažās Eiropas un Āzijas valstīs."
                    "sv" -> "⚠️ Kanal 149-165 kräver licensiering eller stöds inte i vissa länder i Europa och Asien."
                    else -> "⚠️ Channels 149-165 require licensing or are unsupported in some European and Asian countries."
                }
            } else null
        }
        else -> null
    }
}

fun getAnalyzerString(key: String, lang: String, p1: String = "", p2: String = "", p3: String = ""): String {
    val l = when (lang) {
        "ru", "uk", "be", "de", "es", "fr", "it", "pt", "da", "fi", "kk", "lt", "lv", "sv" -> lang
        else -> "en"
    }
    return when (key) {
        "hint_text" -> when (l) {
            "ru" -> "Чтобы изменить номер канал \"$p1\" потяните за его название влево или вправо.Чтобы задать новый пароль, мощность передачи и прочие настройки, дважды тапните по названию."
            "uk" -> "Щоб змінити номер каналу \"$p1\" потягніть за його назву вліво або вправо.Щоб задати новий пароль, потужність передачі та інші налаштування, двічі тапніть по назві."
            "be" -> "Каб змяніць нумар канала \"$p1\" пацягніце за яго назву ўлева або ўправа.Каб задаць новы пароль, магутнасць перадачы і іншыя налады, двойчы тапніце па назве."
            "de" -> "Um die Kanalnummer von „$p1“ zu ändern, ziehen Sie den Namen nach links oder rechts.Um ein neues Passwort, die Sendeleistung und andere Einstellungen festzulegen, tippen Sie doppelt auf den Namen."
            "es" -> "Para cambiar el número de canal de \"$p1\", arrastre su nombre hacia la izquierda o la derecha.Para establecer una nueva contraseña, potencia de transmisión y otros ajustes, toque dos veces el nombre."
            "fr" -> "Pour modifier le numéro de canal de « $p1 », faites glisser son nom vers la gauche ou la droite.Pour définir un nouveau mot de passe, une puissance de transmission et d'autres paramètres, appuyez deux fois sur le nom."
            "it" -> "Per modificare il numero del canale di \"$p1\", trascina il nome a sinistra o a destra.Per impostare una nuova password, potenza di trasmissione e altre impostazioni, tocca due volte il nome."
            "pt" -> "Para alterar o número do canal de \"$p1\", arraste o nome para a esquerda ou direita.Para definir uma nova senha, potência de transmissão e outras configurações, toque duas vezes no nome."
            "da" -> "For at ændre kanalnummeret for \"$p1\" skal du trække navnet til venstre eller højre.For at indstille en ny adgangskode, sendestyrke og andre indstillinger skal du dobbelttrykke på navnet."
            "fi" -> "Muuttaaksesi kanavan \"$p1\" numeroa, vedä sen nimeä vasemmalle tai oikealle.Asettaaksesi uuden salasanan, lähetystehon ja muut asetukset, kaksoisnapauta nimeä."
            "kk" -> "«$p1» арна нөмірін өзгерту үшін оның атауын солға немесе оңға қарай тартыңыз.Жаңа парольді, тарату қуатын және басқа параметрлерді орнату үшін атауын екі рет түртіңіз."
            "lt" -> "Norėdami pakeisti „$p1“ kanalo numerį, vilkite jo pavadinimą į kairę arba į dešinę.Norėdami nustatyti naują slaptažodį, siuntimo galią ir kitus nustatymus, dukart bakstelėkite pavadinimą."
            "lv" -> "Lai mainītu \"$p1\" kanāla numuru, velciet tā nosaukumu pa kreisi vai pa labi.Lai iestatītu jaunu paroli, raidīšanas jaudu un citus iestatījumus, divreiz pieskarieties nosaukumam."
            "sv" -> "För att ändra kanalnumret för \"$p1\" drar du dess namn åt vänster eller höger.För att ställa in ett nytt lösenord, sändningseffekt och andra inställningar dubbelklickar du på namnet."
            else -> "To change the channel of \"$p1\", drag its name left or right.To set a new password, transmit power, and other settings, double tap its name."
        }
        "confirm_title" -> when (l) {
            "ru" -> "Изменение канала Wi-Fi"
            "uk" -> "Зміна каналу Wi-Fi"
            "be" -> "Змена канала Wi-Fi"
            "de" -> "Wi-Fi-Kanal ändern"
            "es" -> "Cambiar canal de Wi-Fi"
            "fr" -> "Changer le canal Wi-Fi"
            "it" -> "Modifica canale Wi-Fi"
            "pt" -> "Alterar canal Wi-Fi"
            "da" -> "Skift Wi-Fi-kanal"
            "fi" -> "Vaihda Wi-Fi-kanavaa"
            "kk" -> "Wi-Fi арнасын өзгерту"
            "lt" -> "Pakeisti Wi-Fi kanalą"
            "lv" -> "Mainīt Wi-Fi kanālu"
            "sv" -> "Ändra Wi-Fi-kanal"
            else -> "Change Wi-Fi Channel"
        }
        "confirm_body" -> when (l) {
            "ru" -> "Вы действительно хотите изменить частотный канал сети \"$p1\" с $p2-го на $p3-й? Настройки применятся автоматически."
            "uk" -> "Ви дійсно хочете змінити частотний канал мережі \"$p1\" з $p2-го на $p3-й?"
            "be" -> "Вы сапраўды хочаце змяніць частотны канал сеткі \"$p1\" з $p2-га на $p3-ы?"
            "de" -> "Möchten Sie den Kanal von „$p1“ wirklich von $p2 auf $p3 ändern?"
            "es" -> "¿Really desee cambiar el canal de red de \"$p1\" de $p2 a $p3?"
            "fr" -> "Voulez-vous vraiment changer le canal de « $p1 » de $p2 à $p3 ?"
            "it" -> "Vuoi davvero cambiare il canale di \"$p1\" da $p2 a $p3?"
            "pt" -> "Deseja realmente alterar o canal de \"$p1\" de $p2 para $p3?"
            "da" -> "Er du sikker på, at du vil ændre nätverkskanalen for \"$p1\" fra $p2 til $p3?"
            "fi" -> "Haluatko varmasti vaihtaa verkon \"$p1\" kanavan $p2 -> $p3?"
            "kk" -> "«$p1» желісінің арнасын $p2 арнасынан $p3 арнасына өзгерткіңіз келе ме?"
            "lt" -> "Ar tikrai norite pakeisti tinklo „$p1“ kanalą iš $p2 į $p3?"
            "lv" -> "Vai tiešām vēlaties mainīt tīkla \"$p1\" kanālu no $p2 uz $p3?"
            "sv" -> "Är du säker på att du vill ändра nätverkskanalen för \"$p1\" från $p2 till $p3?"
            else -> "Are you sure you want to change the Wi-Fi channel of \"$p1\" from Channel $p2 to $p3? This will apply the configuration over SSH."
        }
        "yes" -> when (l) {
            "ru" -> "Применить"
            "uk" -> "Застосувати"
            "be" -> "Ужыць"
            "de" -> "Anwenden"
            "es" -> "Aplicar"
            "fr" -> "Appliquer"
            "it" -> "Applica"
            "pt" -> "Aplicar"
            "da" -> "Anvend"
            "fi" -> "Käytä"
            "kk" -> "Қолдану"
            "lt" -> "Taikyti"
            "lv" -> "Lietot"
            "sv" -> "Verkställ"
            else -> "Apply"
        }
        "no" -> when (l) {
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
        else -> ""
    }
}

fun getSettingsString(key: String, lang: String, p1: String = ""): String {
    val l = when (lang) {
        "ru", "uk", "be", "de", "es", "fr", "it", "pt", "da", "fi", "kk", "lt", "lv", "sv" -> lang
        else -> "en"
    }
    return when (key) {
        "settings_title" -> when (l) {
            "ru" -> "Настройки AP: $p1"
            "uk" -> "Налаштування AP: $p1"
            "be" -> "Налады AP: $p1"
            "de" -> "AP-Einstellungen: $p1"
            "es" -> "Ajustes de AP: $p1"
            "fr" -> "Paramètres de l'AP: $p1"
            "it" -> "Impostazioni AP: $p1"
            "pt" -> "Definições de AP: $p1"
            "da" -> "AP-indstillinger: $p1"
            "fi" -> "Tukiaseman asetukset: $p1"
            "kk" -> "AP параметрлері: $p1"
            "lt" -> "AP nustatymai: $p1"
            "lv" -> "AP iestatījumi: $p1"
            "sv" -> "AP-inställningar: $p1"
            else -> "AP Settings: $p1"
        }
        "error_parsing" -> when (l) {
            "ru" -> "Ошибка разбора ответа роутера"
            "uk" -> "Помилка розбору відповіді роутера"
            "be" -> "Памылка разбору адказу роўтэра"
            "de" -> "Fehler beim Parsen der Router-Antwort"
            "es" -> "Error al analizar la respuesta del router"
            "fr" -> "Erreur lors de l'analyse de la réponse du routeur"
            "it" -> "Errore durante l'analisi della risposta del router"
            "pt" -> "Erro ao analisar a resposta do roteador"
            "da" -> "Fejl under parsing af routersvar"
            "fi" -> "Virhe tulkittaessa reitittimen vastausta"
            "kk" -> "Роутер жауабын талдау қатесі"
            "lt" -> "Klaida analizuojant maršruto parinktuvo atsakymą"
            "lv" -> "Kļūda, analizējot maršrutētāja atbildi"
            "sv" -> "Fel vid parsning av routersvar"
            else -> "Error parsing router response"
        }
        "error_not_found" -> when (l) {
            "ru" -> "Сеть не найдена в конфигурации роутера"
            "uk" -> "Мережу не знайдено в конфігурації роутера"
            "be" -> "Сетка не знойдзена ў канфігурацыі роўтэра"
            "de" -> "Netzwerk in der Routerkonfiguration nicht gefunden"
            "es" -> "Red no encontrada en la configuración del router"
            "fr" -> "Réseau non trouvé dans la configuration du routeur"
            "it" -> "Rete non trovata nella configurazione del router"
            "pt" -> "Rede não encontrada na configuração do roteador"
            "da" -> "Netværk blev ikke fundet i routerkonfigurationen"
            "fi" -> "Verkkoa ei löytynyt reitittimen kokoonpanosta"
            "kk" -> "Роутер конфигурациясында желі табылмады"
            "lt" -> "Tinklas nerastas maršruto parinktuvo konfigūracijoje"
            "lv" -> "Tīkls nav atrasts maršrutētāja konfigurācijā"
            "sv" -> "Nätverket hittades inte i routerns konfiguration"
            else -> "Network not found in router system configuration"
        }
        "apply_settings" -> when (l) {
            "ru" -> "Применение настроек... Пожалуйста, подождите"
            "uk" -> "Застосування налаштувань... Будь ласка, зачекайте"
            "be" -> "Ужыванне налад... Калі ласка, пачакайце"
            "de" -> "Einstellungen werden angewendet... Bitte warten"
            "es" -> "Aplicando configuración... Por favor espere"
            "fr" -> "Application des paramètres... Veuillez patienter"
            "it" -> "Applicazione delle impostazioni in corso... Attendere prego"
            "pt" -> "Aplicando configurações... Por favor, aguarde"
            "da" -> "Anvender indstillinger... Vent venligst"
            "fi" -> "Asetuksia otetaan käyttöön... Odota hetki"
            "kk" -> "Параметрлерді қолдану... Күте тұрыңыз"
            "lt" -> "Taikomi nustatymai... Palaukite"
            "lv" -> "Iestatījumu lietošana... Lūdzu, uzgaidiet"
            "sv" -> "Verkställer inställningar... Vänligen vänta"
            else -> "Applying settings... Please wait"
        }
        "encryption_label" -> when (l) {
            "ru" -> "Шифрование"
            "uk" -> "Шифрування"
            "be" -> "Шыфраванне"
            "de" -> "Verschlüsselung"
            "es" -> "Cifrado"
            "fr" -> "Chiffrement"
            "it" -> "Crittografia"
            "pt" -> "Criptografia"
            "da" -> "Kryptering"
            "fi" -> "Salaus"
            "kk" -> "Шифрлеу"
            "lt" -> "Šifravimas"
            "lv" -> "Šifrēšana"
            "sv" -> "Kryptering"
            else -> "Encryption"
        }
        "encryption_none" -> when (l) {
            "ru" -> "Без шифрования (Открытая)"
            "uk" -> "Без шифрування (Відкрита)"
            "be" -> "Без шыфравання (Адкрытая)"
            "de" -> "Keine Verschlüsselung (Offen)"
            "es" -> "Sin cifrado (Abierta)"
            "fr" -> "Aucun chiffrement (Ouvert)"
            "it" -> "Nessuna crittografia (Aperta)"
            "pt" -> "Sem criptografia (Aberta)"
            "da" -> "Ingen kryptering (Åben)"
            "fi" -> "Ei salausta (Avoin)"
            "kk" -> "Шифрлеусіз (Ашық)"
            "lt" -> "Be šifravimo (Atviras)"
            "lv" -> "Bez šifrēšanas (Atvērts)"
            "sv" -> "Ingen kryptering (Öppen)"
            else -> "No encryption (Open)"
        }
        "password_label" -> when (l) {
            "ru" -> "Пароль Wi-Fi (Ключ)"
            "uk" -> "Пароль Wi-Fi (Ключ)"
            "be" -> "Пароль Wi-Fi (Ключ)"
            "de" -> "WLAN-Passwort (Schlüssel)"
            "es" -> "Contraseña de Wi-Fi"
            "fr" -> "Mot de passe Wi-Fi"
            "it" -> "Password Wi-Fi"
            "pt" -> "Senha do Wi-Fi"
            "da" -> "Wi-Fi-adgangskode"
            "fi" -> "Wi-Fi-salasana"
            "kk" -> "Wi-Fi паролі (Кілт)"
            "lt" -> "Wi-Fi slaptažodis"
            "lv" -> "Wi-Fi parole"
            "sv" -> "Wi-Fi-lösenord"
            else -> "Wi-Fi Password (Key)"
        }
        "txpower_label" -> when (l) {
            "ru" -> "Мощность передатчика (dBm)"
            "uk" -> "Потужність передавача (dBm)"
            "be" -> "Магутнасць перадатчыка (dBm)"
            "de" -> "Sendeleistung (dBm)"
            "es" -> "Potencia de transmisión (dBm)"
            "fr" -> "Puissance de transmission (dBm)"
            "it" -> "Potenza di trasmissione (dBm)"
            "pt" -> "Potência do transmissor (dBm)"
            "da" -> "Sendestyrke (dBm)"
            "fi" -> "Lähetysteho (dBm)"
            "kk" -> "Тарату қуаты (dBm)"
            "lt" -> "Siuntimo galia (dBm)"
            "lv" -> "Raidīšanas jauda (dBm)"
            "sv" -> "Sändningseffekt (dBm)"
            else -> "Transmit Power (dBm)"
        }
        "save" -> when (l) {
            "ru" -> "Сохранить"
            "uk" -> "Зберегти"
            "be" -> "Захаваць"
            "de" -> "Speichern"
            "es" -> "Guardar"
            "fr" -> "Enregistrer"
            "it" -> "Salva"
            "pt" -> "Salvar"
            "da" -> "Gem"
            "fi" -> "Tallenna"
            "kk" -> "Сақтау"
            "lt" -> "Išsaugoti"
            "lv" -> "Saglabāt"
            "sv" -> "Spara"
            else -> "Save"
        }
        "fetching" -> when (l) {
            "ru" -> "Запрос мощности передатчика и пароля у роутера..."
            "uk" -> "Запит потужності передавача та пароля у роутера..."
            "be" -> "Запыт магутнасці перадатчыка і пароля ў роутера..."
            "de" -> "Sendeleistung und Passwort vom Router abfragen..."
            "es" -> "Solicitando potencia del transmisor y contraseña al router..."
            "fr" -> "Demande de la puissance de transmission et du mot de passe au routeur..."
            "it" -> "Richiesta potenza di trasmissione e password dal router..."
            "pt" -> "Solicitando potência do transmissor e senha ao roteador..."
            "da" -> "Anmoder om sendestyrke og adgangskode fra router..."
            "fi" -> "Pyydetään lähetystehoa ja salasanaa reitittimeltä..."
            "kk" -> "Роутерден тарату қуаты мен парольді сұрату..."
            "lt" -> "Užklausiamas siųstuvo galingumas ir slaptažodis iš maršruto parinktuvo..."
            "lv" -> "Pieprasa raidītāja jaudu un paroli no maršrutētāja..."
            "sv" -> "Begär sändandeffekt och lösenord från routern..."
            else -> "Requesting transmitter power and password from router..."
        }
        "channel_title" -> when (l) {
            "ru" -> "Выбор канала"
            "uk" -> "Вибір каналу"
            "be" -> "Выбар канала"
            "de" -> "Kanal auswählen"
            "es" -> "Seleccionar canal"
            "fr" -> "Sélectionner le canal"
            "it" -> "Seleziona canale"
            "pt" -> "Selecionar canal"
            "da" -> "Vælg kanal"
            "fi" -> "Valitse kanava"
            "kk" -> "Арнаны таңдау"
            "lt" -> "Pasirinkite kanalą"
            "lv" -> "Izvēlēties kanālu"
            "sv" -> "Välj kanal"
            else -> "Select Channel"
        }
        "channel_label" -> when (l) {
            "ru" -> "Канал"
            "uk" -> "Канал"
            "be" -> "Канал"
            "de" -> "Kanal"
            "es" -> "Canal"
            "fr" -> "Canal"
            "it" -> "Canale"
            "pt" -> "Canal"
            "da" -> "Kanal"
            "fi" -> "Kanava"
            "kk" -> "Арна"
            "lt" -> "Kanalas"
            "lv" -> "Kanāls"
            "sv" -> "Kanal"
            else -> "Channel"
        }
        "saving_waiting" -> when (l) {
            "ru" -> "Идет сохранение настроек, ожидайте появления сигнала \"$p1\""
            "uk" -> "Йде збереження налаштувань, очікуйте появи сигналу \"$p1\""
            "be" -> "Ідзе захаванне налад, чакайце з'яўлення сігналу \"$p1\""
            "de" -> "Einstellungen werden gespeichert, bitte warten Sie auf das Signal von „$p1“"
            "es" -> "Se están guardando los ajustes, por favor espere a que aparezca la señal de \"$p1\""
            "fr" -> "Enregistrement des paramètres, veuillez patienter jusqu'à l'apparition du signal de « $p1 »"
            "it" -> "Salvataggio delle impostazioni in corso, attendere la comparsa del segnale di \"$p1\""
            "pt" -> "Definições a ser salvas, por favor aguarde até que o sinal de \"$p1\" apareça"
            "da" -> "Gemmer indstillinger, vent venligst på, at signalet fra \"$p1\" vises"
            "fi" -> "Asetuksia tallennetaan, odota, kunnes signaali \"$p1\" ilmestyy"
            "kk" -> "Параметрлер сақталуда, «$p1» сигналының пайда болуын күтіңіз"
            "lt" -> "Nustatymai išsaugomi, palaukite, kol pasirodys „$p1“ signalas"
            "lv" -> "Iestatījumi tiek saglabāti, lūdzu, uzgaidiet, līdz parādās \"$p1\" signāls"
            "sv" -> "Inställningarna sparas, vänligen vänta tills signalen för „$p1“ visas"
            else -> "Saving settings, please wait for the signal of \"$p1\" to appear"
        }
        "width_label" -> when (l) {
            "ru" -> "Ширина канала"
            "uk" -> "Ширина каналу"
            "be" -> "Шырыня канала"
            "de" -> "Kanalbreite"
            "es" -> "Ancho de canal"
            "fr" -> "Largeur du canal"
            "it" -> "Larghezza canale"
            "pt" -> "Largura de banda"
            "da" -> "Kanalbredde"
            "fi" -> "Kanavan leveys"
            "kk" -> "Арна ені"
            "lt" -> "Kanalo plotis"
            "lv" -> "Kanāla platums"
            "sv" -> "Kanalbredd"
            else -> "Channel Width"
        }
        "country_label" -> when (l) {
            "ru" -> "Код страны"
            "uk" -> "Код країни"
            "be" -> "Код краіны"
            "de" -> "Ländercode"
            "es" -> "Código de país"
            "fr" -> "Code pays"
            "it" -> "Codice paese"
            "pt" -> "Código do país"
            "da" -> "Landekode"
            "fi" -> "Maakoodi"
            "kk" -> "Ел коды"
            "lt" -> "Šalies kodas"
            "lv" -> "Valsts kods"
            "sv" -> "Landskod"
            else -> "Country Code"
        }
        else -> ""
    }
}
