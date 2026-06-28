package com.example

import android.content.Context
import android.content.Intent
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat

object ShortcutHelper {
    fun pushShortcutById(context: Context, id: String) {
        val (shortRes, longRes, iconRes, commandVal) = when (id) {
            "led_on" -> Quadruple(
                R.string.shortcut_led_on_short,
                R.string.shortcut_led_on_long,
                R.drawable.ic_shortcut_led_on,
                "led_on"
            )
            "led_off" -> Quadruple(
                R.string.shortcut_led_off_short,
                R.string.shortcut_led_off_long,
                R.drawable.ic_shortcut_led_off,
                "led_off"
            )
            "reboot" -> Quadruple(
                R.string.shortcut_reboot_short,
                R.string.shortcut_reboot_long,
                R.mipmap.ic_launcher,
                "reboot"
            )
            else -> return
        }

        try {
            val intent = Intent(context, MainActivity::class.java).apply {
                action = Intent.ACTION_VIEW
                putExtra("command", commandVal)
                setPackage(context.packageName)
            }

            val shortcut = ShortcutInfoCompat.Builder(context, id)
                .setShortLabel(context.getString(shortRes))
                .setLongLabel(context.getString(longRes))
                .setIcon(IconCompat.createWithResource(context, iconRes))
                .setIntent(intent)
                .build()

            ShortcutManagerCompat.pushDynamicShortcut(context, shortcut)
            android.util.Log.d("ShortcutHelper", "Successfully pushed dynamic shortcut: $id")
        } catch (e: Throwable) {
            android.util.Log.e("ShortcutHelper", "Failed to push dynamic shortcut: $id", e)
        }
    }

    private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
}
