package com.example

import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.util.Log
import com.example.data.RouterDatabase
import com.example.data.RouterRepository
import com.example.data.SshClientManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RouterLedTileService : TileService() {

    companion object {
        private val sharedScope = CoroutineScope(Dispatchers.IO)
        private val sharedSsh = SshClientManager()
        private var updateJob: kotlinx.coroutines.Job? = null
        private var toggleJob: kotlinx.coroutines.Job? = null
    }

    private fun getCachedLedState(): Boolean {
        val prefs = applicationContext.getSharedPreferences("router_prefs", MODE_PRIVATE)
        return prefs.getBoolean("last_known_led_state", true)
    }

    private fun setCachedLedState(state: Boolean) {
        val prefs = applicationContext.getSharedPreferences("router_prefs", MODE_PRIVATE)
        prefs.edit().putBoolean("last_known_led_state", state).apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Do not cancel sharedScope or disconnect sharedSsh so active background commands can complete
    }

    override fun onStartListening() {
        super.onStartListening()
        // Instantly display cached state to avoid any delay or wrong state on UI creation
        val tile = qsTile
        if (tile != null) {
            val cachedActive = getCachedLedState()
            tile.state = if (cachedActive) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
            tile.label = getString(R.string.tile_led)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                tile.subtitle = if (cachedActive) getString(R.string.tile_on) else getString(R.string.tile_off)
            }
            tile.updateTile()
        }
        updateTileState()
    }

    private fun updateTileState(force: Boolean = false) {
        if (!force && toggleJob?.isActive == true) return
        updateJob?.cancel()
        updateJob = sharedScope.launch {
            val tile = withContext(Dispatchers.Main) { qsTile } ?: return@launch
            try {
                val db = RouterDatabase.getDatabase(applicationContext)
                val dao = db.routerDao()
                val config = dao.getRouterConfig()

                if (config == null) {
                    withContext(Dispatchers.Main) {
                        tile.state = Tile.STATE_INACTIVE
                        tile.label = getString(R.string.tile_led)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            tile.subtitle = getString(R.string.tile_configure_app)
                        }
                        tile.updateTile()
                    }
                    return@launch
                }

                // Show background query status preserving the current state
                withContext(Dispatchers.Main) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        tile.subtitle = getString(R.string.tile_requesting)
                    }
                    tile.updateTile()
                }

                val repository = RouterRepository(dao, sharedSsh)
                val status = repository.queryRouterStatus(config)

                if (!force && toggleJob?.isActive == true) return@launch

                // Save to cache
                setCachedLedState(status.isLedActive)

                withContext(Dispatchers.Main) {
                    tile.state = if (status.isLedActive) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
                    tile.label = getString(R.string.tile_led)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        tile.subtitle = if (status.isLedActive) getString(R.string.tile_on) else getString(R.string.tile_off)
                    }
                    tile.updateTile()
                }
            } catch (e: Exception) {
                if (!force && toggleJob?.isActive == true) return@launch
                Log.e("RouterLedTile", "Failed to update tile status", e)
                withContext(Dispatchers.Main) {
                    val tile = qsTile ?: return@withContext
                    tile.state = Tile.STATE_INACTIVE
                    tile.label = getString(R.string.tile_led)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        tile.subtitle = getString(R.string.widget_error_red)
                    }
                    tile.updateTile()
                }
            }
        }
    }

    override fun onClick() {
        super.onClick()
        updateJob?.cancel()
        toggleJob?.cancel()

        toggleJob = sharedScope.launch {
            var toggleSuccess = false
            try {
                val tile = withContext(Dispatchers.Main) { qsTile } ?: return@launch
                val db = RouterDatabase.getDatabase(applicationContext)
                val dao = db.routerDao()
                val config = dao.getRouterConfig() ?: return@launch

                // Get next state by toggling current UI state or cached state
                val currentState = withContext(Dispatchers.Main) { tile.state == Tile.STATE_ACTIVE }
                val nextState = !currentState

                // Update cache & UI immediately (optimistic)
                setCachedLedState(nextState)
                withContext(Dispatchers.Main) {
                    tile.state = if (nextState) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        tile.subtitle = if (nextState) getString(R.string.tile_on) else getString(R.string.tile_off)
                    }
                    tile.updateTile()
                }

                ShortcutHelper.pushShortcutById(applicationContext, if (nextState) "led_on" else "led_off")

                val repository = RouterRepository(dao, sharedSsh)
                repository.setLedStatus(config, nextState)
                
                toggleSuccess = true
            } catch (e: Exception) {
                Log.e("RouterLedTile", "Failed to toggle LED", e)
                withContext(Dispatchers.Main) {
                    val tile = qsTile ?: return@withContext
                    tile.state = Tile.STATE_INACTIVE
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        tile.subtitle = getString(R.string.widget_error_red)
                    }
                    tile.updateTile()
                }
            } finally {
                if (toggleSuccess) {
                    updateTileState(force = true)
                }
            }
        }
    }
}
