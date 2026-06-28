package com.example.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ConsoleLog
import com.example.data.RouterConfig
import com.example.data.RouterRepository
import com.example.data.RouterStatus
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

enum class TabType {
    DASHBOARD, CONSOLE, TEST
}

data class SpeedSnapshot(
    val downloadSpeedMbps: Float,
    val uploadSpeedMbps: Float,
    val cpuUsagePercent: Float = 0f,
    val timestamp: Long = System.currentTimeMillis()
)

data class DnsVariant(
    val id: String,
    val name: String,
    val description: String?,
    val servers: List<String>
)

data class DnsProvider(
    val id: String,
    val name: String,
    val description: String,
    val variants: List<DnsVariant>
)

val PublicDnsProviders = listOf(
    DnsProvider(
        id = "cloudflare",
        name = "Cloudflare",
        description = "Один из самых быстрых публичных DNS-провайдеров в мире с акцентом на конфиденциальность.",
        variants = listOf(
            DnsVariant("cloudflare_default", "Базовый (быстрый доступ и приватность)", null, listOf("1.1.1.1", "1.0.0.1")),
            DnsVariant("cloudflare_malware", "Защита от вредоносного ПО (Malware Block)", null, listOf("1.1.1.2", "1.0.0.2")),
            DnsVariant("cloudflare_family", "Семейный (блокировка вредоносного ПО + контента для взрослых + SafeSearch)", null, listOf("1.1.1.3", "1.0.0.3"))
        )
    ),
    DnsProvider(
        id = "opendns",
        name = "OpenDNS (Cisco)",
        description = "Один из старейших и надежнейших сервисов, принадлежащий компании Cisco.",
        variants = listOf(
            DnsVariant("opendns_home", "Базовый (Home / без фильтрации)", null, listOf("208.67.222.222", "208.67.220.220")),
            DnsVariant("opendns_family", "Семейный (Family Shield / предустановленная фильтрация)", "Подходит для роутеров, фильтрует контент для взрослых без необходимости создания учетной записи.", listOf("208.67.222.123", "208.67.220.123"))
        )
    ),
    DnsProvider(
        id = "cleanbrowsing",
        name = "CleanBrowsing",
        description = "Специализированный сервис, ориентированный исключительно на блокировку нежелательного контента.",
        variants = listOf(
            DnsVariant("cleanbrowsing_security", "Фильтр безопасности (Security Filter)", null, listOf("185.228.168.9", "185.228.169.9")),
            DnsVariant("cleanbrowsing_adult", "Взрослый фильтр (Adult Filter)", null, listOf("185.228.168.10", "185.228.169.11")),
            DnsVariant("cleanbrowsing_family", "Семейный фильтр (Family Filter)", "Блокирует прокси, VPN-туннели, порнографию и жестко включает безопасный поиск в Google/YouTube.", listOf("185.228.168.168", "185.228.169.168"))
        )
    ),
    DnsProvider(
        id = "adguard",
        name = "AdGuard DNS",
        description = "Лучший выбор для тех, кто хочет избавиться от рекламы на уровне сетевых запросов.",
        variants = listOf(
            DnsVariant("adguard_default", "Базовый (блокировка рекламы, трекеров и фишинга)", null, listOf("94.140.14.14", "94.140.15.15")),
            DnsVariant("adguard_family", "Семейный (базовый + контент для взрослых + SafeSearch)", null, listOf("94.140.14.15", "94.140.15.16")),
            DnsVariant("adguard_unfiltered", "Нефильтрующий (просто быстрый и надежный сервис)", null, listOf("94.140.14.140", "94.140.14.141"))
        )
    ),
    DnsProvider(
        id = "controld",
        name = "Control D",
        description = "Современный и очень гибкий DNS от создателей Windscribe.",
        variants = listOf(
            DnsVariant("controld_uncensored", "Нефильтрующий (чистый интернет / Uncensored)", null, listOf("76.76.2.1", "76.76.10.1")),
            DnsVariant("controld_ads", "Блокировка рекламы (Ads & Trackers)", null, listOf("76.76.2.2", "76.76.10.2")),
            DnsVariant("controld_family", "Семейный (все: реклама + соцсети + контент для взрослых)", null, listOf("76.76.2.3", "76.76.10.3"))
        )
    ),
    DnsProvider(
        id = "quad9",
        name = "Quad9",
        description = "Сервис с акцентом на кибербезопасность.",
        variants = listOf(
            DnsVariant("quad9_default", "Стандартный (по умолчанию / блокировка фишинга и малвари)", null, listOf("9.9.9.9", "149.112.112.112")),
            DnsVariant("quad9_ecs", "Стандартный + ECS-поддержка (с поддержкой ECS)", "Передает малую часть вашего IP-адреса (EDNS Client Subnet) для более точной маршрутизации до CDN-серверов.", listOf("9.9.9.11", "149.112.112.11"))
        )
    ),
    DnsProvider(
        id = "google",
        name = "Google Public DNS",
        description = "Один из самых популярных публичных сервисов.",
        variants = listOf(
            DnsVariant("google_default", "Стандартный", null, listOf("8.8.8.8", "8.8.4.4"))
        )
    )
)

data class RouterVpnItem(
    val name: String,
    val type: String, // "OpenVPN", "WireGuard", "AmneziaWG"
    val isRunning: Boolean = false,
    val isChecked: Boolean = false
)

data class UiState(
    val currentTab: TabType = TabType.DASHBOARD,
    val config: RouterConfig? = null,
    val isConfiguring: Boolean = false, // True opens the configuration bottom sheet/dialog
    val isConnecting: Boolean = false,
    val connectionError: String? = null,
    val isStatusRefreshing: Boolean = false,
    val status: RouterStatus = RouterStatus(),
    val commandInput: String = "",
    val commandOutput: String = "",
    val consoleHistory: List<ConsoleLog> = emptyList(),
    val speedHistory: List<SpeedSnapshot> = emptyList(),
    val currentDownloadSpeed: Float = 0f,
    val currentUploadSpeed: Float = 0f,
    val openVpnTransition: String? = null,
    val wireguardTransition: String? = null,
    val ledTransition: String? = null,
    val availableInterfaces: List<String> = listOf("wg0"),
    val availableOpenVpnServices: List<String> = emptyList(),
    val isConnectionVerified: Boolean = false,
    val cpuUsage: String = "—",
    val memoryUsage: String = "—",
    val uptime: String = "—",
    val isInitialLoadComplete: Boolean = false,
    val vpnList: List<RouterVpnItem> = emptyList(),
    val tentativeVpnList: List<RouterVpnItem> = emptyList(),
    val isVpnListDialogOpen: Boolean = false,
    val isVpnTransitioning: Boolean = false,
    val vpnTransitionText: String? = null,
    val isDnsListDialogOpen: Boolean = false
)

class RouterViewModel(private val repository: RouterRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _sessionCommandHistory = MutableStateFlow<List<String>>(emptyList())
    val sessionCommandHistory: StateFlow<List<String>> = _sessionCommandHistory.asStateFlow()

    private val _iperfCommandHistory = MutableStateFlow<List<String>>(emptyList())
    val iperfCommandHistory: StateFlow<List<String>> = _iperfCommandHistory.asStateFlow()

    private var speedJob: Job? = null

    // For calculating traffic speeds
    private var lastRxBytes = 0L
    private var lastTxBytes = 0L
    private var lastBytesTime = 0L

    private var isWaitingForTrafficAfterReboot = false
    private var pendingCommand: String? = null
    private var hasRetriedOnStartup = false
    private var isFirstLaunchVpnSyncDone = false

    private var interactiveShellLogId: Long? = null
    private val interactiveShellOutputBuffer = StringBuilder()
    private var interactiveShellJob: Job? = null

    fun startInteractiveShellSession() {
        val config = _uiState.value.config ?: return
        stopInteractiveShellSession()
        interactiveShellJob = viewModelScope.launch {
            try {
                repository.clearConsoleLogs()
                _uiState.update { it.copy(commandOutput = "") }

                interactiveShellOutputBuffer.setLength(0)
                val initialText = "Connecting to SSH interactive shell...\r\n"
                interactiveShellOutputBuffer.append(initialText)
                val logId = repository.insertConsoleLog("sh", interactiveShellOutputBuffer.toString())
                interactiveShellLogId = logId

                repository.startInteractiveShell(config) { chunk ->
                    val cleanedChunk = chunk.replace("]0;root@OpenWrt:", "")
                        .replace("\u001b]0;root@OpenWrt:\u0007", "")
                        .replace("\u001B]0;root@OpenWrt:\u0007", "")
                    
                    var i = 0
                    val len = cleanedChunk.length
                    while (i < len) {
                        val char = cleanedChunk[i]
                        if (char == '\b' || char == '\u0008' || char == '\u007F' || char == '\u007f') {
                            val isErasePattern = i + 2 < len && 
                                                 cleanedChunk[i + 1] == ' ' && 
                                                 (cleanedChunk[i + 2] == '\b' || cleanedChunk[i + 2] == '\u0008' || cleanedChunk[i + 2] == '\u007F' || cleanedChunk[i + 2] == '\u007f')
                            if (isErasePattern) {
                                if (interactiveShellOutputBuffer.isNotEmpty()) {
                                    interactiveShellOutputBuffer.setLength(interactiveShellOutputBuffer.length - 1)
                                }
                                i += 3
                            } else {
                                if (interactiveShellOutputBuffer.isNotEmpty()) {
                                    interactiveShellOutputBuffer.setLength(interactiveShellOutputBuffer.length - 1)
                                }
                                i++
                            }
                        } else if (char == '\u001b' || char == '\u001B') {
                            if (i + 1 < len && cleanedChunk[i + 1] == '[') {
                                var j = i + 2
                                while (j < len && cleanedChunk[j] !in 'a'..'z' && cleanedChunk[j] !in 'A'..'Z') {
                                    j++
                                }
                                if (j < len) {
                                    val cmd = cleanedChunk[j]
                                    val seq = cleanedChunk.substring(i, j + 1)
                                    if (cmd == 'm') {
                                        interactiveShellOutputBuffer.append(seq)
                                    } else if (cmd == 'D') {
                                        val paramStr = cleanedChunk.substring(i + 2, j)
                                        val count = paramStr.toIntOrNull() ?: 1
                                        for (k in 0 until count) {
                                            if (interactiveShellOutputBuffer.isNotEmpty()) {
                                                interactiveShellOutputBuffer.setLength(interactiveShellOutputBuffer.length - 1)
                                            }
                                        }
                                    }
                                    // For K, J, P, X, A, B, C, G, H, etc. we just ignore the sequence
                                    i = j + 1
                                } else {
                                    interactiveShellOutputBuffer.append(char)
                                    i++
                                }
                            } else {
                                interactiveShellOutputBuffer.append(char)
                                i++
                            }
                        } else {
                            interactiveShellOutputBuffer.append(char)
                            i++
                        }
                    }
                    if (interactiveShellOutputBuffer.length > 50000) {
                        interactiveShellOutputBuffer.delete(0, interactiveShellOutputBuffer.length - 50000)
                    }
                    viewModelScope.launch {
                        interactiveShellLogId?.let { lid ->
                            repository.updateConsoleLog(lid, "sh", interactiveShellOutputBuffer.toString())
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("RouterViewModel", "Error starting interactive shell: ${e.message}")
                interactiveShellOutputBuffer.append("\nError starting shell: ${e.message}\n")
                interactiveShellLogId?.let { lid ->
                    repository.updateConsoleLog(lid, "sh", interactiveShellOutputBuffer.toString())
                }
            }
        }
    }

    fun stopInteractiveShellSession() {
        interactiveShellJob?.cancel()
        interactiveShellJob = null
        viewModelScope.launch {
            try {
                repository.stopInteractiveShell()
            } catch (e: Exception) {
                Log.e("RouterViewModel", "Error stopping interactive shell: ${e.message}")
            }
        }
        interactiveShellLogId = null
        interactiveShellOutputBuffer.setLength(0)
    }

    init {
        var isFirstEmission = true
        // Observe router configuration in DB
        repository.routerConfigFlow
            .onEach { config ->
                _uiState.update { 
                    it.copy(
                        config = config, 
                        isConfiguring = if (config == null) true else (if (isFirstEmission) false else it.isConfiguring), 
                        isConnectionVerified = config != null,
                        currentTab = if (isFirstEmission && config != null) TabType.DASHBOARD else it.currentTab,
                        isInitialLoadComplete = true
                    ) 
                }
                isFirstEmission = false
                if (config != null) {
                    // Start updates for this config
                    startPollers(config)
                    refreshStatus()
                    fetchInterfaces(config)
                    fetchEnabledOpenVpnServices(config)

                    // Execute pending command if any
                    pendingCommand?.let { cmd ->
                        pendingCommand = null
                        when (cmd) {
                            "led_on" -> toggleLed(true)
                            "led_off" -> toggleLed(false)
                            "reboot" -> rebootRouter()
                            "vpn_on" -> toggleMasterVpn(true)
                            "vpn_off" -> toggleMasterVpn(false)
                        }
                    }
                } else {
                    stopPollers()
                    _uiState.update { it.copy(status = RouterStatus(), speedHistory = emptyList()) }
                }
            }
            .launchIn(viewModelScope)

        // Observe console logs Flow
        repository.consoleLogsFlow
            .onEach { logs ->
                _uiState.update { it.copy(consoleHistory = logs) }
                if (logs.isNotEmpty()) {
                    val uniqueCommands = logs.map { it.command }.distinct()
                    val iperfCmds = uniqueCommands.filter { it.contains("iperf3") }
                    val standardCmds = uniqueCommands.filter { !it.contains("iperf3") }
                    if (_sessionCommandHistory.value.isEmpty()) {
                        _sessionCommandHistory.update { standardCmds }
                    }
                    if (_iperfCommandHistory.value.isEmpty()) {
                        _iperfCommandHistory.update { iperfCmds }
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun switchTab(tab: TabType) {
        val oldTab = _uiState.value.currentTab
        _uiState.update { it.copy(currentTab = tab) }
        if (tab == TabType.CONSOLE && oldTab != TabType.CONSOLE) {
            _uiState.update { it.copy(commandInput = "") }
            startInteractiveShellSession()
        } else if (oldTab == TabType.CONSOLE && tab != TabType.CONSOLE) {
            stopInteractiveShellSession()
        }
        if (tab == TabType.DASHBOARD && oldTab != TabType.DASHBOARD) {
            refreshStatus()
        }
    }

    fun setConfiguring(isConfiguring: Boolean) {
        val wasConfiguring = _uiState.value.isConfiguring
        _uiState.update { it.copy(isConfiguring = isConfiguring) }
        if (!isConfiguring && wasConfiguring) {
            refreshStatus()
        }
    }

    fun setCommandInput(input: String) {
        _uiState.update { it.copy(commandInput = input) }
    }

    private fun cleanHost(input: String): String {
        var host = input.trim()
        if (host.startsWith("http://", ignoreCase = true)) {
            host = host.substring("http://".length)
        } else if (host.startsWith("https://", ignoreCase = true)) {
            host = host.substring("https://".length)
        }
        val slashIndex = host.indexOf('/')
        if (slashIndex != -1) {
            host = host.substring(0, slashIndex)
        }
        val colonIndex = host.indexOf(':')
        if (colonIndex != -1) {
            host = host.substring(0, colonIndex)
        }
        return host.trim()
    }

    // Test SSH connectivity and load interfaces without saving
    fun setDnsListDialogOpen(isOpen: Boolean) {
        _uiState.update { it.copy(isDnsListDialogOpen = isOpen) }
    }

    fun setDnsServers(servers: List<String>) {
        val cfg = _uiState.value.config ?: return
        viewModelScope.launch {
            try {
                // Show a brief loading indication and optimistically update status
                _uiState.update { 
                    it.copy(
                        isStatusRefreshing = true,
                        status = it.status.copy(
                            dnsServers = servers.joinToString(", "),
                            isCustomDns = servers.isNotEmpty()
                        )
                    ) 
                }
                repository.setDns(cfg, servers)
                // Wait for router network service to restart before querying status
                kotlinx.coroutines.delay(2000)
                refreshStatus()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        connectionError = "Failed to set DNS: ${e.message}",
                        isStatusRefreshing = false
                    )
                }
            }
        }
    }
    
    fun testConnection(ip: String, port: Int, user: String, pass: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isConnecting = true, connectionError = null) }
            val cleanedIp = cleanHost(ip)
            val tempConfig = RouterConfig(
                ipAddress = cleanedIp,
                port = port,
                username = user.trim(),
                sshKeyOrPassword = pass
            )
            val success = repository.testConnection(tempConfig)
            if (success) {
                val list = repository.fetchRouterInterfaces(tempConfig)
                _uiState.update {
                    it.copy(
                        isConnecting = false,
                        connectionError = null,
                        isConnectionVerified = true,
                        availableInterfaces = list
                    )
                }
                val existingConfig = _uiState.value.config
                if (existingConfig != null && list.size == 1 && list.first() != existingConfig.wgInterface) {
                    val updatedConfig = existingConfig.copy(wgInterface = list.first())
                    repository.saveRouterConfig(updatedConfig)
                }
            } else {
                _uiState.update {
                    it.copy(
                        isConnecting = false,
                        isConnectionVerified = false,
                        connectionError = "Connection failed. Please check IP, port, login, and password."
                    )
                }
            }
        }
    }

    // Save configuration and close dialog
    fun saveConfig(ip: String, port: Int, user: String, pass: String, ledBehavior: String, wgInterface: String) {
        viewModelScope.launch {
            val cleanedIp = cleanHost(ip)
            val finalConfig = RouterConfig(
                ipAddress = cleanedIp,
                port = port,
                username = user.trim(),
                sshKeyOrPassword = pass,
                ledBehavior = ledBehavior,
                wgInterface = wgInterface
            )
            repository.saveRouterConfig(finalConfig)
            _uiState.update { 
                it.copy(
                    isConfiguring = false, 
                    connectionError = null
                ) 
            }
        }
    }

    fun clearConnectionError() {
        _uiState.update { it.copy(connectionError = null) }
    }

    private fun fetchInterfaces(config: RouterConfig) {
        viewModelScope.launch {
            val list = repository.fetchRouterInterfaces(config)
            _uiState.update { it.copy(availableInterfaces = list) }
            if (list.size == 1 && list.first() != config.wgInterface) {
                val updatedConfig = config.copy(wgInterface = list.first())
                repository.saveRouterConfig(updatedConfig)
            }
        }
    }

    fun fetchEnabledOpenVpnServices(config: RouterConfig) {
        viewModelScope.launch {
            val list = repository.fetchEnabledOpenVpnServices(config)
            _uiState.update { it.copy(availableOpenVpnServices = list) }
        }
    }

    fun selectOpenVpnService(newService: String) {
        val config = _uiState.value.config ?: return
        val isOpenVpnRunning = _uiState.value.status.isOpenVpnActive
        val isWgRunning = _uiState.value.status.isWireGuardActive
        val oldService = config.openVpnService

        viewModelScope.launch {
            val oldIp = _uiState.value.status.publicIp
            val updatedConfig = config.copy(openVpnService = newService)
            repository.saveRouterConfig(updatedConfig)
            _uiState.update { it.copy(config = updatedConfig) }

            if (isOpenVpnRunning) {
                val transitionText = "Переключение..."
                _uiState.update {
                    it.copy(
                        openVpnTransition = transitionText,
                        status = it.status.copy(
                            isOpenVpnActive = true,
                            isWireGuardActive = false
                        ),
                        wireguardTransition = null
                    )
                }
                try {
                    repository.setOpenVpnStatus(updatedConfig, false, oldService)
                    delay(800)
                    repository.setOpenVpnStatus(updatedConfig, true, newService)
                    
                    var attempts = 0
                    while (attempts < 15) {
                        val currentIp = repository.queryPublicIpOnly(updatedConfig)
                        if (currentIp != oldIp && currentIp != "Connecting..." && currentIp != "Detecting..." && currentIp != "Offline / Error") {
                            break
                        }
                        delay(1000)
                        attempts++
                    }
                    var fresh = repository.queryRouterStatus(updatedConfig)
                    var checkAttempts = 0
                    while (!fresh.isOpenVpnActive && checkAttempts < 5) {
                        delay(1000)
                        fresh = repository.queryRouterStatus(updatedConfig)
                        checkAttempts++
                    }
                    _uiState.update {
                        it.copy(
                            status = fresh.copy(
                                isOpenVpnActive = fresh.isOpenVpnActive,
                                isWireGuardActive = false
                            ),
                            openVpnTransition = null,
                            wireguardTransition = null
                        )
                    }
                } catch (e: Exception) {
                    _uiState.update {
                        it.copy(
                            openVpnTransition = null,
                            wireguardTransition = null
                        )
                    }
                }
            } else {
                // OpenVPN is off, just refresh status to reflect new defaults if needed
                refreshStatus()
            }
        }
    }

    fun selectWireGuardInterface(newIface: String) {
        val config = _uiState.value.config ?: return
        val isOpenVpnRunning = _uiState.value.status.isOpenVpnActive
        val isWgRunning = _uiState.value.status.isWireGuardActive
        val oldIface = config.wgInterface

        viewModelScope.launch {
            val oldIp = _uiState.value.status.publicIp
            val updatedConfig = config.copy(wgInterface = newIface)
            repository.saveRouterConfig(updatedConfig)
            _uiState.update { it.copy(config = updatedConfig) }

            if (isWgRunning) {
                val transitionText = "Переключение..."
                _uiState.update {
                    it.copy(
                        wireguardTransition = transitionText,
                        status = it.status.copy(
                            isWireGuardActive = true,
                            isOpenVpnActive = false
                        ),
                        openVpnTransition = null
                    )
                }
                try {
                    val tempOldWgConfig = updatedConfig.copy(wgInterface = oldIface)
                    repository.setWireguardStatus(tempOldWgConfig, false)
                    delay(800)
                    repository.setWireguardStatus(updatedConfig, true)
                    
                    var attempts = 0
                    while (attempts < 15) {
                        val currentIp = repository.queryPublicIpOnly(updatedConfig)
                        if (currentIp != oldIp && currentIp != "Connecting..." && currentIp != "Detecting..." && currentIp != "Offline / Error") {
                            break
                        }
                        delay(1000)
                        attempts++
                    }
                    var fresh = repository.queryRouterStatus(updatedConfig)
                    var checkAttempts = 0
                    while (!fresh.isWireGuardActive && checkAttempts < 5) {
                        delay(1000)
                        fresh = repository.queryRouterStatus(updatedConfig)
                        checkAttempts++
                    }
                    _uiState.update {
                        it.copy(
                            status = fresh.copy(
                                isWireGuardActive = fresh.isWireGuardActive,
                                isOpenVpnActive = false
                            ),
                            wireguardTransition = null,
                            openVpnTransition = null
                        )
                    }
                } catch (e: Exception) {
                    _uiState.update {
                        it.copy(
                            wireguardTransition = null,
                            openVpnTransition = null
                        )
                    }
                }
            } else {
                // WireGuard is off, just refresh status to reflect new defaults if needed
                refreshStatus()
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            stopPollers()
            repository.deleteRouterConfig()
            _uiState.update { it.copy(isConnectionVerified = false, availableInterfaces = listOf("wg0")) }
        }
    }

    // Manual status refresh triggered from the top-right button
    fun refreshStatus() {
        val config = _uiState.value.config ?: return
        viewModelScope.launch {
            _uiState.update { 
                it.copy(
                    isStatusRefreshing = true,
                    vpnList = emptyList(),
                    tentativeVpnList = emptyList(),
                    status = it.status.copy()
                ) 
            }
            try {
                // Fetch interfaces with protos, openvpn services, and actual router status in parallel
                val interfacesWithProtosDeferred = async { repository.fetchInterfacesWithProtos(config) }
                val openVpnDeferred = async { repository.fetchEnabledOpenVpnServices(config) }
                val statusDeferred = async { repository.queryRouterStatus(config) }

                val ifacesWithProtos = try { interfacesWithProtosDeferred.await() } catch (e: Exception) { emptyMap<String, String>() }
                val availableOvpn = try { openVpnDeferred.await() } catch (e: Exception) { emptyList<String>() }
                var freshStatus = statusDeferred.await()

                if (freshStatus.version == "Offline / Error" && _uiState.value.status.version != "Detecting...") {
                    freshStatus = freshStatus.copy(
                        dnsServers = _uiState.value.status.dnsServers,
                        isCustomDns = _uiState.value.status.isCustomDns
                    )
                }

                val activeWgList = freshStatus.activeWgInterface?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() } ?: emptyList()
                val isWireguardActiveOnRouter = freshStatus.isWireGuardActive || activeWgList.isNotEmpty()

                // Parse saved selections
                val savedSelectedNames = config.selectedVpns.split(",").map { it.trim() }.filter { it.isNotEmpty() }.toSet()

                val newVpnList = mutableListOf<RouterVpnItem>()
                val activeOvpnList = freshStatus.activeOpenVpnInstances?.split(",")?.map { it.trim().lowercase() }?.filter { it.isNotEmpty() } ?: emptyList()

                // 1. OpenVPN services
                for (serviceName in availableOvpn) {
                    val sNameLower = serviceName.lowercase()
                    val isRunning = freshStatus.isOpenVpnActive && (
                        activeOvpnList.contains(sNameLower) ||
                        freshStatus.openVpnInstanceName?.lowercase() == sNameLower ||
                        (freshStatus.openVpnInstanceName.isNullOrEmpty() && config.openVpnService == serviceName)
                    )
                    val isChecked = isRunning
                    newVpnList.add(RouterVpnItem(name = serviceName, type = "OpenVPN", isRunning = isRunning, isChecked = isChecked))
                }

                // 2. Interfaces (WireGuard and AmneziaWG)
                for ((ifaceName, proto) in ifacesWithProtos) {
                    val type = if (proto == "amneziawg") "AmneziaWG" else "WireGuard"
                    val isRunning = activeWgList.contains(ifaceName)
                    val isChecked = isRunning
                    newVpnList.add(RouterVpnItem(name = ifaceName, type = type, isRunning = isRunning, isChecked = isChecked))
                }

                var finalConfig = config
                var finalVpnList: List<RouterVpnItem> = newVpnList

                if (!isFirstLaunchVpnSyncDone) {
                    val runningList = newVpnList.filter { it.isRunning }
                    if (runningList.isNotEmpty()) {
                        val runningNames = runningList.map { it.name }
                        finalConfig = config.copy(
                            selectedVpns = runningNames.joinToString(","),
                            isVpnMasterOn = true
                        )
                        repository.saveRouterConfig(finalConfig)
                        finalVpnList = newVpnList.map { item ->
                            if (item.isRunning) item.copy(isChecked = true) else item
                        }
                    }
                    isFirstLaunchVpnSyncDone = true
                }

                _uiState.update { 
                    it.copy(
                        availableInterfaces = ifacesWithProtos.keys.toList(),
                        availableOpenVpnServices = availableOvpn,
                        vpnList = finalVpnList,
                        status = freshStatus.copy(isWireGuardActive = isWireguardActiveOnRouter), 
                        isStatusRefreshing = false,
                        wireguardTransition = null,
                        openVpnTransition = null,
                        config = finalConfig
                    ) 
                }

                val isUnknown = freshStatus.publicIp == "Unknown" && 
                                freshStatus.location == "Unknown" && 
                                freshStatus.provider == "Unknown"
                if (isUnknown && !hasRetriedOnStartup) {
                    hasRetriedOnStartup = true
                    viewModelScope.launch {
                        delay(1000)
                        refreshStatus()
                    }
                }
            } catch (e: Exception) {
                Log.e("RouterViewModel", "Error in refreshStatus: ${e.message}", e)
                _uiState.update { it.copy(isStatusRefreshing = false) }
            }
        }
    }

    fun openVpnListDialog() {
        val config = _uiState.value.config
        _uiState.update { 
            it.copy(
                vpnList = emptyList(), 
                tentativeVpnList = emptyList(),
                isVpnListDialogOpen = true
            ) 
        }
        if (config == null) return
        viewModelScope.launch {
            try {
                // Fetch interfaces, services, and query status
                val interfacesWithProtosDeferred = async { repository.fetchInterfacesWithProtos(config) }
                val openVpnDeferred = async { repository.fetchEnabledOpenVpnServices(config) }
                val statusDeferred = async { repository.queryRouterStatus(config) }

                val ifacesWithProtos = try { interfacesWithProtosDeferred.await() } catch (e: Exception) { emptyMap<String, String>() }
                val availableOvpn = try { openVpnDeferred.await() } catch (e: Exception) { emptyList<String>() }
                val freshStatus = try { statusDeferred.await() } catch (e: Exception) { RouterStatus() }

                val activeWgList = freshStatus.activeWgInterface?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() } ?: emptyList()
                val isWireguardActiveOnRouter = freshStatus.isWireGuardActive || activeWgList.isNotEmpty()
                val savedSelectedNames = config.selectedVpns.split(",").map { it.trim() }.filter { it.isNotEmpty() }.toSet()

                val newVpnList = mutableListOf<RouterVpnItem>()
                val activeOvpnList = freshStatus.activeOpenVpnInstances?.split(",")?.map { it.trim().lowercase() }?.filter { it.isNotEmpty() } ?: emptyList()

                // 1. OpenVPN services
                for (serviceName in availableOvpn) {
                    val sNameLower = serviceName.lowercase()
                    val isRunning = freshStatus.isOpenVpnActive && (
                        activeOvpnList.contains(sNameLower) ||
                        freshStatus.openVpnInstanceName?.lowercase() == sNameLower ||
                        (freshStatus.openVpnInstanceName.isNullOrEmpty() && config.openVpnService == serviceName)
                    )
                    val isChecked = isRunning
                    newVpnList.add(RouterVpnItem(name = serviceName, type = "OpenVPN", isRunning = isRunning, isChecked = isChecked))
                }

                // 2. Interfaces (WireGuard and AmneziaWG)
                for ((ifaceName, proto) in ifacesWithProtos) {
                    val type = if (proto == "amneziawg") "AmneziaWG" else "WireGuard"
                    val isRunning = activeWgList.contains(ifaceName)
                    val isChecked = isRunning
                    newVpnList.add(RouterVpnItem(name = ifaceName, type = type, isRunning = isRunning, isChecked = isChecked))
                }

                _uiState.update { 
                    it.copy(
                        availableInterfaces = ifacesWithProtos.keys.toList(),
                        availableOpenVpnServices = availableOvpn,
                        vpnList = newVpnList,
                        tentativeVpnList = newVpnList,
                        status = freshStatus.copy(isWireGuardActive = isWireguardActiveOnRouter)
                    ) 
                }
            } catch (e: Exception) {
                Log.e("RouterViewModel", "Error in openVpnListDialog fetch: ${e.message}", e)
            }
        }
    }

    fun cancelVpnListChanges() {
        _uiState.update { 
            it.copy(
                tentativeVpnList = emptyList(),
                isVpnListDialogOpen = false
            ) 
        }
    }

    fun toggleTentativeVpnItem(itemName: String, checked: Boolean) {
        _uiState.update { state ->
            state.copy(
                tentativeVpnList = state.tentativeVpnList.map {
                    if (it.name == itemName) it.copy(isChecked = checked) else it
                }
            )
        }
    }

    fun applyVpnListChanges(isTv: Boolean = false) {
        val config = _uiState.value.config ?: return
        val tentative = _uiState.value.tentativeVpnList
        val selectedNames = tentative.filter { it.isChecked }.map { it.name }.joinToString(",")
        val isMasterOn = true

        viewModelScope.launch {
            _uiState.update { it.copy(isVpnTransitioning = true, vpnTransitionText = "Применение изменений...") }
            
            // Save the selected state to DB first. Since there is no master switch in the new unified layout,
            // we automatically set isVpnMasterOn to true if at least one VPN is selected, or false if none are.
            val updatedConfig = config.copy(
                selectedVpns = selectedNames,
                isVpnMasterOn = selectedNames.isNotEmpty()
            )
            repository.saveRouterConfig(updatedConfig)
            _uiState.update { it.copy(config = updatedConfig) }
            
            if (isMasterOn) {
                // Determine start/stop actions
                val toStart = tentative.filter { it.isChecked && !it.isRunning }
                val toStop = tentative.filter { !it.isChecked && it.isRunning }
                
                val commands = mutableListOf<String>()
                for (item in toStart) {
                    if (item.type == "OpenVPN") {
                        commands.add("/etc/init.d/openvpn start ${item.name}")
                    } else {
                        commands.add("ifup ${item.name}")
                    }
                }
                for (item in toStop) {
                    if (item.type == "OpenVPN") {
                        commands.add("/etc/init.d/openvpn stop ${item.name}")
                    } else {
                        commands.add("ifdown ${item.name}")
                    }
                }
                
                if (commands.isNotEmpty()) {
                    try {
                        val combinedCommand = commands.joinToString("; ")
                        repository.executeConsoleCommand(updatedConfig, combinedCommand, saveToLog = false)
                        delay(1200)
                    } catch (e: Exception) {
                        Log.e("RouterViewModel", "Failed to apply VPN changes: ${e.message}")
                    }
                }
            }
            // Refresh
            refreshStatus()
            _uiState.update { 
                it.copy(
                    isVpnTransitioning = false, 
                    vpnTransitionText = null, 
                    tentativeVpnList = emptyList(),
                    isVpnListDialogOpen = false
                ) 
            }
        }
    }

    fun toggleMasterVpn(enable: Boolean) {
        val config = _uiState.value.config
        if (config == null) {
            pendingCommand = if (enable) "vpn_on" else "vpn_off"
            return
        }
        viewModelScope.launch {
            _uiState.update { 
                it.copy(
                    isVpnTransitioning = true, 
                    vpnTransitionText = if (enable) "Включение..." else "Выключение..."
                ) 
            }
            
            // 1. Update config isVpnMasterOn in DB
            val updatedConfig = config.copy(isVpnMasterOn = enable)
            repository.saveRouterConfig(updatedConfig)
            _uiState.update { it.copy(config = updatedConfig) }
            
            val vpnItems = _uiState.value.vpnList
            val commands = mutableListOf<String>()
            
            if (enable) {
                // Start checked, stop unchecked
                for (item in vpnItems) {
                    if (item.isChecked) {
                        if (item.type == "OpenVPN") {
                            commands.add("/etc/init.d/openvpn start ${item.name}")
                        } else {
                            commands.add("ifup ${item.name}")
                        }
                    } else {
                        if (item.type == "OpenVPN") {
                            commands.add("/etc/init.d/openvpn stop ${item.name}")
                        } else {
                            commands.add("ifdown ${item.name}")
                        }
                    }
                }
            } else {
                // Stop all
                for (item in vpnItems) {
                    if (item.type == "OpenVPN") {
                        commands.add("/etc/init.d/openvpn stop ${item.name}")
                    } else {
                        commands.add("ifdown ${item.name}")
                    }
                }
            }
            
            if (commands.isNotEmpty()) {
                try {
                    val combinedCmd = commands.joinToString("; ")
                    repository.executeConsoleCommand(updatedConfig, combinedCmd, saveToLog = false)
                    delay(1200)
                } catch (e: Exception) {
                    Log.e("RouterViewModel", "Failed to toggle master VPN: ${e.message}")
                }
            }
            
            refreshStatus()
            _uiState.update { it.copy(isVpnTransitioning = false, vpnTransitionText = null) }
        }
    }

    // Toggle VPN statuses with mutual exclusivity
    fun toggleOpenVpn(enable: Boolean) {
        val config = _uiState.value.config
        if (config == null) {
            pendingCommand = if (enable) "ovpn_on" else "ovpn_off"
            return
        }
        viewModelScope.launch {
            val oldIp = _uiState.value.status.publicIp
            val transitionText = if (enable) "Включение..." else "Выключение..."
            val isWgActiveCurrently = _uiState.value.status.isWireGuardActive
            _uiState.update {
                it.copy(
                    openVpnTransition = transitionText,
                    status = it.status.copy(
                        isOpenVpnActive = enable,
                        isWireGuardActive = if (enable) false else it.status.isWireGuardActive
                    ),
                    wireguardTransition = if (enable && isWgActiveCurrently) "Выключение..." else if (enable) null else it.wireguardTransition
                )
            }
            try {
                if (enable) {
                    // Turn off WireGuard first (mutual exclusion)
                    repository.setWireguardStatus(config, false)
                    delay(800)
                    // Start OpenVPN
                    repository.setOpenVpnStatus(config, true)
                } else {
                    repository.setOpenVpnStatus(config, false)
                }
                
                // Wait for new IP to establish connection status
                var attempts = 0
                while (attempts < 15) {
                    val currentIp = repository.queryPublicIpOnly(config)
                    if (currentIp != oldIp && currentIp != "Connecting..." && currentIp != "Detecting..." && currentIp != "Offline / Error") {
                        break
                    }
                    delay(1000)
                    attempts++
                }
                refreshStatus()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        openVpnTransition = null,
                        wireguardTransition = null
                    )
                }
            }
        }
    }
 
    fun toggleWireguard(enable: Boolean) {
        val config = _uiState.value.config
        if (config == null) {
            pendingCommand = if (enable) "wg_on" else "wg_off"
            return
        }
        viewModelScope.launch {
            val oldIp = _uiState.value.status.publicIp
            val transitionText = if (enable) "Включение..." else "Выключение..."
            val isOpenVpnActiveCurrently = _uiState.value.status.isOpenVpnActive
            _uiState.update {
                it.copy(
                    wireguardTransition = transitionText,
                    status = it.status.copy(
                        isWireGuardActive = enable,
                        isOpenVpnActive = if (enable) false else it.status.isOpenVpnActive
                    ),
                    openVpnTransition = if (enable && isOpenVpnActiveCurrently) "Выключение..." else if (enable) null else it.openVpnTransition
                )
            }
            try {
                if (enable) {
                    // Turn off OpenVPN first (mutual exclusion)
                    repository.setOpenVpnStatus(config, false)
                    delay(800)
                    // Start WireGuard
                    repository.setWireguardStatus(config, true)
                } else {
                    repository.setWireguardStatus(config, false)
                }
                
                // Wait for new IP to establish connection status
                var attempts = 0
                while (attempts < 15) {
                    val currentIp = repository.queryPublicIpOnly(config)
                    if (currentIp != oldIp && currentIp != "Connecting..." && currentIp != "Detecting..." && currentIp != "Offline / Error") {
                        break
                    }
                    delay(1000)
                    attempts++
                }
                refreshStatus()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        openVpnTransition = null,
                        wireguardTransition = null
                    )
                }
            }
        }
    }

    fun toggleAllVpns(enable: Boolean) {
        val config = _uiState.value.config ?: return
        viewModelScope.launch {
            val oldIp = _uiState.value.status.publicIp
            val transitionText = if (enable) "Включение..." else "Выключение..."
            _uiState.update {
                it.copy(
                    openVpnTransition = transitionText,
                    wireguardTransition = transitionText,
                    status = it.status.copy(
                        isOpenVpnActive = enable,
                        isWireGuardActive = enable
                    )
                )
            }
            try {
                coroutineScope {
                    launch { repository.setOpenVpnStatus(config, enable) }
                    launch { repository.setWireguardStatus(config, enable) }
                }
                
                // Wait for new IP to establish connection status
                var attempts = 0
                while (attempts < 15) {
                    val currentIp = repository.queryPublicIpOnly(config)
                    if (currentIp != oldIp && currentIp != "Connecting..." && currentIp != "Detecting..." && currentIp != "Offline / Error") {
                        break
                    }
                    delay(1000)
                    attempts++
                }
                refreshStatus()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        openVpnTransition = null,
                        wireguardTransition = null
                    )
                }
            }
        }
    }

    // Toggle LED on and off
    fun toggleLed(enable: Boolean) {
        val config = _uiState.value.config
        if (config == null) {
            pendingCommand = if (enable) "led_on" else "led_off"
            return
        }
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    ledTransition = null,
                    status = it.status.copy(isLedActive = enable)
                )
            }
            try {
                repository.setLedStatus(config, enable)
            } catch (e: Exception) {
                // Keep the optimistic state as instructed
            }
        }
    }

    // Send SSH console command
    fun sendConsoleCommand() {
        val config = _uiState.value.config ?: return
        val command = _uiState.value.commandInput.trim()
        if (command.isEmpty()) return

        // Prepend to session command history so index 0 is the most recent
        _sessionCommandHistory.update { currentList ->
            if (currentList.contains(command)) {
                listOf(command) + (currentList - command)
            } else {
                listOf(command) + currentList
            }
        }

        if (repository.writeToConsoleStdin(command)) {
            _uiState.update { it.copy(commandInput = "") }
            return
        }

        _uiState.update { it.copy(commandInput = "", isStatusRefreshing = true) }
        viewModelScope.launch {
            val response = repository.executeConsoleCommand(config, command)
            _uiState.update { it.copy(commandOutput = response, isStatusRefreshing = false) }
        }
    }

    // Send SSH iperf console command
    fun sendIperfConsoleCommand() {
        val config = _uiState.value.config ?: return
        val command = _uiState.value.commandInput.trim()
        if (command.isEmpty()) return

        // Prepend to iPerf command history so index 0 is the most recent
        _iperfCommandHistory.update { currentList ->
            if (currentList.contains(command)) {
                listOf(command) + (currentList - command)
            } else {
                listOf(command) + currentList
            }
        }

        _uiState.update { it.copy(commandInput = "", isStatusRefreshing = true) }
        viewModelScope.launch {
            val response = repository.executeConsoleCommand(config, command)
            _uiState.update { it.copy(commandOutput = response, isStatusRefreshing = false) }
        }
    }


    fun addToSessionHistory(command: String) {
        val trimmed = command.trim()
        if (trimmed.isEmpty()) return
        _sessionCommandHistory.update { currentList ->
            if (currentList.contains(trimmed)) {
                listOf(trimmed) + (currentList - trimmed)
            } else {
                listOf(trimmed) + currentList
            }
        }
    }

    fun deleteFromSessionHistory(command: String) {
        _sessionCommandHistory.update { it - command }
        viewModelScope.launch {
            repository.deleteConsoleLogsByCommand(command)
        }
    }

    fun deleteFromIperfHistory(command: String) {
        _iperfCommandHistory.update { it - command }
        viewModelScope.launch {
            repository.deleteConsoleLogsByCommand(command)
        }
    }

    fun clearConsoleLogs() {
        viewModelScope.launch {
            repository.clearConsoleLogs()
            _uiState.update { it.copy(commandOutput = "") }
        }
    }

    fun rebootRouter() {
        val config = _uiState.value.config
        if (config == null) {
            pendingCommand = "reboot"
            return
        }
        viewModelScope.launch {
            _uiState.update { 
                it.copy(
                    isStatusRefreshing = true,
                    status = RouterStatus(
                        isOpenVpnActive = false,
                        isWireGuardActive = false,
                        isLedActive = false,
                        publicIp = "Перезапуск...",
                        location = "Ожидание соединения...",
                        provider = "Пожалуйста, подождите..."
                    )
                ) 
            }
            stopPollers() // Stop speed monitoring during reboot
            isWaitingForTrafficAfterReboot = true
            
            try {
                repository.rebootRouter(config)
            } catch (e: Exception) {
                // If reboot command failed immediately (e.g., connection lost), we still proceed to poll
            }
            
            // Explicitly disconnect SSH to clear stale network sockets/sessions
            try {
                repository.disconnect()
            } catch (e: Exception) {
                // ignore
            }
            
            // Wait 15 seconds initially for the router to shut down and start boot cycle
            delay(15000)
            
            // Restart tracking. Once the traffic speed graphing starts updating, we will autorefresh status.
            startPollers(config)
        }
    }

    fun onAppResume() {
        refreshStatus()
    }

    // Start background pollers
    private fun startPollers(config: RouterConfig) {
        stopPollers()

        // Reset speed tracking
        lastRxBytes = 0L
        lastTxBytes = 0L
        lastBytesTime = 0L

        // Speed metrics poller: updates WAN download/upload rates every 2 seconds
        speedJob = viewModelScope.launch {
            while (isActive) {
                try {
                    val telemetry = repository.queryWanTrafficBytes(config)
                    val rxBytes = telemetry.rxBytes
                    val txBytes = telemetry.txBytes
                    val nowBytesTime = System.currentTimeMillis()

                    if (lastRxBytes != 0L && lastTxBytes != 0L && lastBytesTime != 0L) {
                        val timeDiffSecs = (nowBytesTime - lastBytesTime) / 1000.0
                        if (timeDiffSecs > 0) {
                            val rxDiff = rxBytes - lastRxBytes
                            val txDiff = txBytes - lastTxBytes

                            if (rxDiff >= 0 && txDiff >= 0) {
                                val rxSpeedMbps = (((rxDiff * 8.0) / (1024.0 * 1024.0)) / timeDiffSecs).toFloat()
                                val txSpeedMbps = (((txDiff * 8.0) / (1024.0 * 1024.0)) / timeDiffSecs).toFloat()

                                addSpeedToHistory(rxSpeedMbps, txSpeedMbps, telemetry.cpuUsage, telemetry.memoryUsage, telemetry.uptime)

                                if (isWaitingForTrafficAfterReboot) {
                                    isWaitingForTrafficAfterReboot = false
                                    refreshStatus()
                                }
                            } else {
                                updateTelemetryOnly(telemetry.cpuUsage, telemetry.memoryUsage, telemetry.uptime)
                            }
                        }
                    } else {
                        updateTelemetryOnly(telemetry.cpuUsage, telemetry.memoryUsage, telemetry.uptime)
                    }
                    lastRxBytes = rxBytes
                    lastTxBytes = txBytes
                    lastBytesTime = nowBytesTime
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    // Ignore transient errors
                }
                delay(2000)
            }
        }
    }

    private fun addSpeedToHistory(download: Float, upload: Float, cpu: String, mem: String, uptime: String) {
        _uiState.update { currentState ->
            val cleanCpu = cpu.trim()
            val cpuPercent = if (cleanCpu.endsWith("%")) {
                cleanCpu.removeSuffix("%").trim().replace(",", ".").toFloatOrNull() ?: 0f
            } else {
                val parsed = cleanCpu.replace(",", ".").toFloatOrNull() ?: 0f
                if (parsed > 0f) {
                    (parsed * 100f).coerceIn(0f, 100f)
                } else {
                    0f
                }
            }
            val snapshot = SpeedSnapshot(
                downloadSpeedMbps = download,
                uploadSpeedMbps = upload,
                cpuUsagePercent = cpuPercent.coerceIn(0f, 100f)
            )
            val updatedHistory = (currentState.speedHistory + snapshot).takeLast(30)
            currentState.copy(
                speedHistory = updatedHistory,
                currentDownloadSpeed = download,
                currentUploadSpeed = upload,
                cpuUsage = cpu,
                memoryUsage = mem,
                uptime = uptime
            )
        }
    }

    private fun updateTelemetryOnly(cpu: String, mem: String, uptime: String) {
        _uiState.update { currentState ->
            currentState.copy(
                cpuUsage = cpu,
                memoryUsage = mem,
                uptime = uptime
            )
        }
    }

    private fun stopPollers() {
        speedJob?.cancel()
        speedJob = null
    }

    fun executeConsoleCommandDirect(command: String, callback: (String) -> Unit) {
        val config = _uiState.value.config
        if (config == null) {
            callback("Error: No router configuration found.")
            return
        }
        viewModelScope.launch {
            val response = repository.executeConsoleCommand(config, command, saveToLog = false)
            callback(response)
        }
    }

    fun writeRawToConsoleStdin(text: String) {
        repository.writeRawToConsoleStdin(text)
    }

    override fun onCleared() {
        super.onCleared()
        stopPollers()
        stopInteractiveShellSession()
        runBlocking {
            launch(Dispatchers.IO) {
                repository.disconnect()
            }
        }
    }
}
