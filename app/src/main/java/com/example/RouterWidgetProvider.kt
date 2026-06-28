package com.example

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import com.example.data.RouterDatabase
import com.example.data.RouterRepository
import com.example.data.SshClientManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RouterWidgetProvider : AppWidgetProvider() {

    companion object {
        private const val TAG = "RouterWidget"
        private const val ACTION_REFRESH = "com.example.widget.ACTION_REFRESH"
        private const val ACTION_TOGGLE_VPN = "com.example.widget.ACTION_TOGGLE_VPN"
        private const val ACTION_TOGGLE_OPENVPN = "com.example.widget.ACTION_TOGGLE_OPENVPN"
        private const val ACTION_TOGGLE_WIREGUARD = "com.example.widget.ACTION_TOGGLE_WIREGUARD"
        private const val ACTION_TOGGLE_LED = "com.example.widget.ACTION_TOGGLE_LED"

        private var lastOpenVpnState: Boolean? = null
        private var lastWireGuardState: Boolean? = null
        private var lastLedState: Boolean? = null

        private var lastOpenVpnText: String = "Инициализация..."
        private var lastWireGuardText: String = "Инициализация..."
        private var lastLedText: String = "Инициализация..."

        // Single shared SSH client manager to keep connections alive and prevent socket leaks
        private val sharedSsh = SshClientManager()

        // Operation guard flag to prevent concurrent SSH requests from spam-clicks
        @Volatile
        private var isOperationInProgress = false
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        for (appWidgetId in appWidgetIds) {
            updateWidgetState(
                context = context,
                appWidgetManager = appWidgetManager,
                appWidgetId = appWidgetId,
                header = "Готов",
                openVpnText = lastOpenVpnText,
                wireGuardText = lastWireGuardText,
                ledText = lastLedText,
                isOpenVpnActive = null,
                isWireGuardActive = null,
                isLedActive = null
            )
        }
        triggerRefresh(context)
    }

    private fun triggerRefresh(context: Context) {
        val intent = Intent(context, RouterWidgetProvider::class.java).apply {
            action = ACTION_REFRESH
        }
        context.sendBroadcast(intent)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        val action = intent.action ?: return
        Log.d(TAG, "OnReceive triggered with action: $action")

        if (action == ACTION_REFRESH || action == ACTION_TOGGLE_LED || action == ACTION_TOGGLE_VPN || action == ACTION_TOGGLE_OPENVPN || action == ACTION_TOGGLE_WIREGUARD) {
            synchronized(RouterWidgetProvider::class.java) {
                if (isOperationInProgress) {
                    Log.w(TAG, "Widget operation already in progress. Ignoring action: $action")
                    return
                }
                isOperationInProgress = true
            }

            when (action) {
                ACTION_REFRESH -> performRefresh(context)
                ACTION_TOGGLE_LED -> performToggleLed(context)
                ACTION_TOGGLE_VPN, ACTION_TOGGLE_OPENVPN, ACTION_TOGGLE_WIREGUARD -> {
                    performToggleVpn(context)
                }
            }
        }
    }

    private fun performRefresh(context: Context) {
        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val db = RouterDatabase.getDatabase(context)
                val dao = db.routerDao()
                val config = dao.getRouterConfig()
                if (config == null) {
                    lastOpenVpnText = "Заполните в приложении"
                    lastWireGuardText = "Заполните в приложении"
                    lastLedText = "Заполните в приложении"
                    updateAllWidgets(context, "Нужна настройка", lastOpenVpnText, lastWireGuardText, lastLedText, false, false, false)
                    return@launch
                }

                updateAllWidgets(context, "Проверка...", "Состояние...", "Состояние...", "Состояние...", lastOpenVpnState, lastWireGuardState, lastLedState)

                kotlinx.coroutines.withTimeout(15000) {
                    val repository = RouterRepository(dao, sharedSsh)
                    val status = repository.queryRouterStatus(config)

                    lastOpenVpnState = status.isOpenVpnActive
                    lastWireGuardState = status.isWireGuardActive
                    lastLedState = status.isLedActive

                    lastOpenVpnText = if (status.isOpenVpnActive) {
                        if (!status.openVpnInstanceName.isNullOrEmpty()) "Запущена (${status.openVpnInstanceName})" else "Запущена"
                    } else "Остановлена"

                    lastWireGuardText = if (status.isWireGuardActive) {
                        "Интерфейс ${config.wgInterface ?: "wg0"} поднят"
                    } else "Выключен"

                    lastLedText = if (status.isLedActive) "Включен 🟢" else "Выключен 🔴"

                    updateAllWidgets(context, "Обновлено", lastOpenVpnText, lastWireGuardText, lastLedText, lastOpenVpnState, lastWireGuardState, lastLedState)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Widget check failed", e)
                updateAllWidgets(context, "Ошибка", lastOpenVpnText, lastWireGuardText, lastLedText, lastOpenVpnState, lastWireGuardState, lastLedState)
            } finally {
                synchronized(RouterWidgetProvider::class.java) {
                    isOperationInProgress = false
                }
                withContext(kotlinx.coroutines.NonCancellable) {
                    withContext(Dispatchers.Main) {
                        pendingResult.finish()
                    }
                }
            }
        }
    }

    private fun performToggleVpn(context: Context) {
        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            var previousOpenVpnText = lastOpenVpnText
            var previousOpenVpnState = lastOpenVpnState
            var previousWireGuardText = lastWireGuardText
            var previousWireGuardState = lastWireGuardState
            try {
                val db = RouterDatabase.getDatabase(context)
                val dao = db.routerDao()
                val config = dao.getRouterConfig()
                if (config == null) {
                    lastOpenVpnText = "Заполните в приложении"
                    lastWireGuardText = "Заполните в приложении"
                    lastLedText = "Заполните в приложении"
                    updateAllWidgets(context, "Нужна настройка", lastOpenVpnText, lastWireGuardText, lastLedText, false, false, false)
                    return@launch
                }

                val currentOpenVpn = lastOpenVpnState ?: false
                val currentWireGuard = lastWireGuardState ?: false
                val isVpnActive = currentOpenVpn || currentWireGuard

                val nextVpnState = !isVpnActive

                // Capture states before transition
                previousOpenVpnText = lastOpenVpnText
                previousOpenVpnState = lastOpenVpnState
                previousWireGuardText = lastWireGuardText
                previousWireGuardState = lastWireGuardState

                if (nextVpnState) {
                    lastOpenVpnState = true
                    lastOpenVpnText = "Включение..."
                    lastWireGuardState = true
                    lastWireGuardText = "Включение..."
                } else {
                    lastOpenVpnState = false
                    lastOpenVpnText = "Выключение..."
                    lastWireGuardState = false
                    lastWireGuardText = "Выключение..."
                }

                updateAllWidgets(
                    context, 
                    "VPN: Переход...", 
                    lastOpenVpnText, 
                    lastWireGuardText, 
                    lastLedText, 
                    lastOpenVpnState, 
                    lastWireGuardState, 
                    lastLedState
                )

                kotlinx.coroutines.withTimeout(35000) {
                    val repository = RouterRepository(dao, sharedSsh)
                    val oldIp = try { repository.queryPublicIpOnly(config) } catch(e: Exception) { "" }

                    if (nextVpnState) {
                        val selectedNames = config.selectedVpns.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                        if (selectedNames.isNotEmpty()) {
                            val availableOvpn = repository.fetchEnabledOpenVpnServices(config)
                            val interfaces = repository.fetchRouterInterfaces(config)
                            for (name in selectedNames) {
                                if (availableOvpn.contains(name)) {
                                    repository.setOpenVpnStatus(config, true, name)
                                } else if (interfaces.contains(name)) {
                                    repository.executeConsoleCommand(config, "ifup $name", saveToLog = false)
                                } else {
                                    repository.executeConsoleCommand(config, "ifup $name", saveToLog = false)
                                }
                            }
                        } else {
                            val hasOvpn = config.openVpnService.isNotEmpty()
                            val hasWg = config.wgInterface.isNotEmpty()
                            if (hasWg) {
                                repository.setWireguardStatus(config, true)
                            } else if (hasOvpn) {
                                repository.setOpenVpnStatus(config, true)
                            } else {
                                repository.setWireguardStatus(config, true)
                            }
                        }
                    } else {
                        repository.setOpenVpnStatus(config, false)
                        repository.setWireguardStatus(config, false)
                        val selectedNames = config.selectedVpns.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                        for (name in selectedNames) {
                            repository.executeConsoleCommand(config, "ifdown $name", saveToLog = false)
                        }
                    }

                    var attempts = 0
                    while (attempts < 15) {
                        try {
                            val currentIp = repository.queryPublicIpOnly(config)
                            if (currentIp != oldIp && currentIp != "Connecting..." && currentIp != "Detecting..." && currentIp != "Offline / Error") {
                                break
                            }
                        } catch (e: Exception) {
                            // ignore, keep checking
                        }
                        kotlinx.coroutines.delay(1000)
                        attempts++
                    }

                    val resultStatus = repository.queryRouterStatus(config)

                    lastOpenVpnState = resultStatus.isOpenVpnActive
                    lastWireGuardState = resultStatus.isWireGuardActive
                    lastLedState = resultStatus.isLedActive

                    lastOpenVpnText = if (resultStatus.isOpenVpnActive) {
                        if (!resultStatus.openVpnInstanceName.isNullOrEmpty()) "Запущена (${resultStatus.openVpnInstanceName})" else "Запущена"
                    } else "Остановлена"

                    lastWireGuardText = if (resultStatus.isWireGuardActive) {
                        "Интерфейс ${config.wgInterface ?: "wg0"} поднят"
                    } else "Выключен"

                    lastLedText = if (resultStatus.isLedActive) "Включен 🟢" else "Выключен 🔴"

                    updateAllWidgets(context, "VPN Изменен", lastOpenVpnText, lastWireGuardText, lastLedText, lastOpenVpnState, lastWireGuardState, lastLedState)
                }
            } catch (e: Exception) {
                Log.e(TAG, "VPN toggle failure", e)
                lastOpenVpnText = previousOpenVpnText
                lastOpenVpnState = previousOpenVpnState
                lastWireGuardText = previousWireGuardText
                lastWireGuardState = previousWireGuardState
                updateAllWidgets(context, "Ошибка 🔴", lastOpenVpnText, lastWireGuardText, lastLedText, lastOpenVpnState, lastWireGuardState, lastLedState)
            } finally {
                synchronized(RouterWidgetProvider::class.java) {
                    isOperationInProgress = false
                }
                withContext(kotlinx.coroutines.NonCancellable) {
                    withContext(Dispatchers.Main) {
                        pendingResult.finish()
                    }
                }
            }
        }
    }

    private fun performToggleLed(context: Context) {
        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val db = RouterDatabase.getDatabase(context)
                val dao = db.routerDao()
                val config = dao.getRouterConfig()
                if (config == null) {
                    lastOpenVpnText = "Заполните в приложении"
                    lastWireGuardText = "Заполните в приложении"
                    lastLedText = "Заполните в приложении"
                    updateAllWidgets(context, "Нужна настройка", lastOpenVpnText, lastWireGuardText, lastLedText, false, false, false)
                    return@launch
                }

                // Optimistically toggle LED state on the widget immediately
                val currentLed = lastLedState ?: false
                val nextLed = !currentLed

                lastLedState = nextLed
                lastLedText = if (nextLed) "Включен 🟢" else "Выключен 🔴"

                updateAllWidgets(context, "Светодиод: Переключен", lastOpenVpnText, lastWireGuardText, lastLedText, lastOpenVpnState, lastWireGuardState, lastLedState)
                
                ShortcutHelper.pushShortcutById(context, if (nextLed) "led_on" else "led_off")

                kotlinx.coroutines.withTimeout(10000) {
                    val repository = RouterRepository(dao, sharedSsh)
                    repository.setLedStatus(config, nextLed)
                }
            } catch (e: Exception) {
                Log.e(TAG, "LED Toggle failure", e)
            } finally {
                synchronized(RouterWidgetProvider::class.java) {
                    isOperationInProgress = false
                }
                withContext(kotlinx.coroutines.NonCancellable) {
                    withContext(Dispatchers.Main) {
                        pendingResult.finish()
                    }
                }
            }
        }
    }

    private fun updateAllWidgets(
        context: Context,
        statusHeader: String,
        openVpn: String,
        wireGuard: String,
        led: String,
        isOpenVpnActive: Boolean? = null,
        isWireGuardActive: Boolean? = null,
        isLedActive: Boolean? = null
    ) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val componentName = ComponentName(context, RouterWidgetProvider::class.java)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)
        for (appWidgetId in appWidgetIds) {
            updateWidgetState(context, appWidgetManager, appWidgetId, statusHeader, openVpn, wireGuard, led, isOpenVpnActive, isWireGuardActive, isLedActive)
        }
    }

    private fun updateWidgetState(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        header: String,
        openVpnText: String,
        wireGuardText: String,
        ledText: String,
        isOpenVpnActive: Boolean? = null,
        isWireGuardActive: Boolean? = null,
        isLedActive: Boolean? = null
    ) {
        val views = RemoteViews(context.packageName, R.layout.router_widget)

        // Keep local static fields synchronized with what is passed to update widget
        val oText = if (openVpnText == "-") lastOpenVpnText else openVpnText
        val wText = if (wireGuardText == "-") lastWireGuardText else wireGuardText
        val lText = if (ledText == "-") lastLedText else ledText

        if (openVpnText != "-") lastOpenVpnText = openVpnText
        if (wireGuardText != "-") lastWireGuardText = wireGuardText
        if (ledText != "-") lastLedText = ledText

        val headerLoc = localize(context, header)
        val oTextLoc = localize(context, oText)
        val wTextLoc = localize(context, wText)
        val lTextLoc = localize(context, lText)

        views.setTextViewText(R.id.widget_status, "${getStatusLabel(context)}: $headerLoc")
        views.setTextViewText(R.id.led_status_text, lTextLoc)

        // Determine actual status for LED
        val ledState = isLedActive ?: lastLedState ?: false
        val ledSwitchRes = if (ledState) R.drawable.ic_switch_on else R.drawable.ic_switch_off
        views.setImageViewResource(R.id.btn_toggle_led, ledSwitchRes)

        // Action pending intents for interactive widgets
        views.setOnClickPendingIntent(R.id.btn_refresh, getPendingActionIntent(context, ACTION_REFRESH))
        views.setOnClickPendingIntent(R.id.btn_toggle_led, getPendingActionIntent(context, ACTION_TOGGLE_LED))

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun getPendingActionIntent(context: Context, action: String): PendingIntent {
        val intent = Intent(context, RouterWidgetProvider::class.java).apply {
            this.action = action
        }
        val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        return PendingIntent.getBroadcast(context, action.hashCode(), intent, flags)
    }

    private fun getStatusLabel(context: Context): String {
        val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0].language
        } else {
            @Suppress("DEPRECATION")
            context.resources.configuration.locale.language
        }
        return when (locale) {
            "ru" -> "Статус"
            "uk" -> "Статус"
            "be" -> "Статус"
            "es" -> "Estado"
            "fr" -> "Statut"
            "de" -> "Status"
            "it" -> "Stato"
            "lt" -> "Būsena"
            "lv" -> "Statuss"
            "fi" -> "Tila"
            "da" -> "Status"
            "sv" -> "Status"
            "pt" -> "Estado"
            "kk" -> "Күйі"
            else -> "Status"
        }
    }

    private fun localize(context: Context, raw: String): String {
        return when (raw) {
            "Инициализация..." -> context.getString(R.string.widget_init)
            "Заполните в приложении" -> context.getString(R.string.widget_fill_app)
            "Нужна настройка" -> context.getString(R.string.widget_setup_req)
            "Проверка..." -> context.getString(R.string.widget_checking)
            "Состояние..." -> context.getString(R.string.widget_status)
            "Обновлено" -> context.getString(R.string.widget_updated)
            "Ошибка" -> context.getString(R.string.widget_error)
            "Ошибка 🔴" -> context.getString(R.string.widget_error_red)
            "Готов" -> context.getString(R.string.widget_ready)
            "Выключение..." -> context.getString(R.string.status_disabling)
            "Включение..." -> context.getString(R.string.status_enabling)
            "Включен 🟢" -> context.getString(R.string.tile_on)
            "Выключен 🔴" -> context.getString(R.string.tile_off)
            "Остановлена" -> context.getString(R.string.status_stopped)
            "Выключен" -> context.getString(R.string.status_off)
            "Запущена" -> context.getString(R.string.status_running)
            "VPN Изменен" -> {
                val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    context.resources.configuration.locales[0].language
                } else {
                    @Suppress("DEPRECATION")
                    context.resources.configuration.locale.language
                }
                when (locale) {
                    "ru" -> "VPN Изменен"
                    "uk" -> "VPN Змінено"
                    "be" -> "VPN Зменены"
                    "es" -> "VPN Cambiado"
                    "fr" -> "VPN Modifié"
                    "de" -> "VPN Geändert"
                    "it" -> "VPN Modificato"
                    "lt" -> "VPN Pakeistas"
                    "lv" -> "VPN Mainīts"
                    "fi" -> "VPN muutettu"
                    "da" -> "VPN ændret"
                    "sv" -> "VPN ändrad"
                    "pt" -> "VPN Alterado"
                    "kk" -> "VPN өзгертілді"
                    else -> "VPN Changed"
                }
            }
            "Светодиод: Переключен" -> {
                val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    context.resources.configuration.locales[0].language
                } else {
                    @Suppress("DEPRECATION")
                    context.resources.configuration.locale.language
                }
                when (locale) {
                    "ru" -> "Светодиод переключен"
                    "uk" -> "Світлодіод перемикнуто"
                    "be" -> "Святлодыёд пераключаны"
                    "es" -> "LED cambiado"
                    "fr" -> "LED Commutée"
                    "de" -> "LED Umgeschaltet"
                    "it" -> "LED Commutato"
                    "lt" -> "LED perjungtas"
                    "lv" -> "LED pārslēgts"
                    "fi" -> "LED vaihdettu"
                    "da" -> "LED ændret"
                    "sv" -> "LED ändrad"
                    "pt" -> "LED Alternado"
                    "kk" -> "Жарық диоды ауыстырылды"
                    else -> "LED Toggled"
                }
            }
            else -> {
                if (raw.startsWith("Запущена (") && raw.endsWith(")")) {
                    val inst = raw.substringAfter("(").substringBefore(")")
                    context.getString(R.string.status_running_inst, inst)
                } else if (raw.startsWith("Интерфейс ") && raw.endsWith(" поднят")) {
                    val iface = raw.substringAfter("Интерфейс ").substringBefore(" поднят")
                    context.getString(R.string.status_wg_up, iface)
                } else {
                    raw
                }
            }
        }
    }
}
