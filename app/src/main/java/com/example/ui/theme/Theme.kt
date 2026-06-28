package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = VibrantPrimaryDark,
    onPrimary = VibrantOnPrimaryDark,
    primaryContainer = VibrantPrimaryContainerDark,
    onPrimaryContainer = VibrantOnPrimaryContainerDark,
    secondary = VibrantSecondaryDark,
    onSecondary = VibrantOnSecondaryDark,
    background = VibrantBackgroundDark,
    onBackground = VibrantOnBackgroundDark,
    surface = VibrantSurfaceDark,
    onSurface = VibrantOnSurfaceDark,
    surfaceVariant = VibrantSurfaceVariantDark,
    onSurfaceVariant = VibrantOnSurfaceVariantDark,
    outline = VibrantBorderDark
  )

private val LightColorScheme =
  lightColorScheme(
    primary = VibrantPrimaryLight,
    onPrimary = VibrantOnPrimaryLight,
    primaryContainer = VibrantPrimaryContainerLight,
    onPrimaryContainer = VibrantOnPrimaryContainerLight,
    secondary = VibrantSecondaryLight,
    onSecondary = VibrantOnSecondaryLight,
    background = VibrantBackgroundLight,
    onBackground = VibrantOnBackgroundLight,
    surface = VibrantSurfaceLight,
    onSurface = VibrantOnSurfaceLight,
    surfaceVariant = VibrantSurfaceVariantLight,
    onSurfaceVariant = VibrantOnSurfaceVariantLight,
    outline = VibrantBorderLight
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
