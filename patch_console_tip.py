import re

file_path = "app/src/main/java/com/example/MainActivity.kt"

with open(file_path, "r") as f:
    content = f.read()

# Add states to ConsoleTab
state_target = """    val context = androidx.compose.ui.platform.LocalContext.current
    val isTv = remember {
        context.packageManager.hasSystemFeature(android.content.pm.PackageManager.FEATURE_LEANBACK)
    }"""

state_replacement = """    val context = androidx.compose.ui.platform.LocalContext.current
    val isTv = remember {
        context.packageManager.hasSystemFeature(android.content.pm.PackageManager.FEATURE_LEANBACK)
    }
    val sharedPrefs = remember { context.getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE) }
    var showConsoleInteractiveTip by remember { mutableStateOf(sharedPrefs.getBoolean("show_console_tip_v1", true)) }
    val focusRequesterConsoleTip = remember { androidx.compose.ui.focus.FocusRequester() }
    var isDismissButtonFocused by remember { mutableStateOf(false) }
    
    androidx.compose.runtime.LaunchedEffect(showConsoleInteractiveTip, isTv) {
        if (showConsoleInteractiveTip && isTv) {
            kotlinx.coroutines.delay(100)
            try { focusRequesterConsoleTip.requestFocus() } catch (e: Exception) {}
        }
    }"""

content = content.replace(state_target, state_replacement)

# Add the UI to the Box
ui_target = """            Box(modifier = Modifier.align(Alignment.CenterStart).fillMaxHeight().width(edgeSize).then(gestureModifier))
            Box(modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight().width(edgeSize).then(gestureModifier))
        }"""

ui_replacement = """            Box(modifier = Modifier.align(Alignment.CenterStart).fillMaxHeight().width(edgeSize).then(gestureModifier))
            Box(modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight().width(edgeSize).then(gestureModifier))
            
            if (showConsoleInteractiveTip) {
                val infiniteTransition = androidx.compose.animation.core.rememberInfiniteTransition(label = "arrow_animation")
                val arrowOffset by infiniteTransition.animateFloat(
                    initialValue = -6f,
                    targetValue = 6f,
                    animationSpec = androidx.compose.animation.core.infiniteRepeatable(
                        animation = androidx.compose.animation.core.tween(durationMillis = 900, easing = androidx.compose.animation.core.FastOutSlowInEasing),
                        repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
                    ),
                    label = "arrow_bounce"
                )
                val arrowAlpha by infiniteTransition.animateFloat(
                    initialValue = 0.5f,
                    targetValue = 0.85f,
                    animationSpec = androidx.compose.animation.core.infiniteRepeatable(
                        animation = androidx.compose.animation.core.tween(durationMillis = 900, easing = androidx.compose.animation.core.FastOutSlowInEasing),
                        repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
                    ),
                    label = "arrow_pulse"
                )
                val buttonAlpha by infiniteTransition.animateFloat(
                    initialValue = 0.2f,
                    targetValue = 1.0f,
                    animationSpec = androidx.compose.animation.core.infiniteRepeatable(
                        animation = androidx.compose.animation.core.tween(durationMillis = 500),
                        repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
                    ),
                    label = "button_pulse"
                )

                // The tip card
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    androidx.compose.material3.Card(
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                        colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color(0xFF1E1E24)),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFFF9500)),
                        modifier = Modifier.widthIn(max = 300.dp).padding(16.dp)
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
                                    imageVector = androidx.compose.material.icons.Icons.Default.Info,
                                    contentDescription = "Tip",
                                    tint = Color(0xFFFF9500),
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = com.example.IperfLocalizations.getConsoleTipTitle(locale),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Text(
                                text = com.example.IperfLocalizations.getConsoleTipBody(locale),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.85f),
                                lineHeight = 16.sp
                            )
                            Button(
                                onClick = {
                                    showConsoleInteractiveTip = false
                                    sharedPrefs.edit().putBoolean("show_console_tip_v1", false).apply()
                                },
                                colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9500)),
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(6.dp),
                                border = if (isDismissButtonFocused) androidx.compose.foundation.BorderStroke(2.dp, Color.White) else null,
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                modifier = Modifier
                                    .align(Alignment.End)
                                    .height(32.dp)
                                    .alpha(buttonAlpha)
                                    .androidx.compose.ui.focus.onFocusChanged { isDismissButtonFocused = it.isFocused }
                                    .focusRequester(focusRequesterConsoleTip)
                            ) {
                                Text(
                                    text = com.example.IperfLocalizations.getSplitterTipDismiss(locale),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Black,
                                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                                )
                            }
                        }
                    }
                }
                
                // Left arrow
                androidx.compose.foundation.Canvas(
                    modifier = Modifier.align(Alignment.CenterStart).size(width = 24.dp, height = 12.dp).offset(x = (8 + arrowOffset).dp, y = 0.dp)
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
                    drawPath(path = path, color = Color(0xFFFF9500).copy(alpha = arrowAlpha))
                }
                // Right arrow
                androidx.compose.foundation.Canvas(
                    modifier = Modifier.align(Alignment.CenterEnd).size(width = 24.dp, height = 12.dp).offset(x = (-8 - arrowOffset).dp, y = 0.dp)
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
                    drawPath(path = path, color = Color(0xFFFF9500).copy(alpha = arrowAlpha))
                }
                // Top arrow
                androidx.compose.foundation.Canvas(
                    modifier = Modifier.align(Alignment.TopCenter).size(width = 12.dp, height = 24.dp).offset(x = 0.dp, y = (8 + arrowOffset).dp)
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
                    drawPath(path = path, color = Color(0xFFFF9500).copy(alpha = arrowAlpha))
                }
                // Bottom arrow
                androidx.compose.foundation.Canvas(
                    modifier = Modifier.align(Alignment.BottomCenter).size(width = 12.dp, height = 24.dp).offset(x = 0.dp, y = (-8 - arrowOffset).dp)
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
                    drawPath(path = path, color = Color(0xFFFF9500).copy(alpha = arrowAlpha))
                }
            }
        }"""

content = content.replace(ui_target, ui_replacement)

with open(file_path, "w") as f:
    f.write(content)
print("Done")
