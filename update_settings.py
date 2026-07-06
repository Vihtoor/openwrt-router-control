import re

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "r", encoding="utf-8") as f:
    content = f.read()

imports = """import androidx.activity.compose.BackHandler
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester"""

if "import androidx.activity.compose.BackHandler" not in content:
    content = content.replace("import androidx.compose.foundation.clickable", imports + "\nimport androidx.compose.foundation.clickable")

# Add FocusRequester to EditRouterForm
if "val checkConnectionFocusRequester" not in content:
    content = content.replace("val coroutineScope = rememberCoroutineScope()", "val coroutineScope = rememberCoroutineScope()\n    val checkConnectionFocusRequester = remember { FocusRequester() }\n    if (!isFirst) {\n        BackHandler {\n            onCancel()\n        }\n    }")

# Update Password Field
old_pwd = """                    OutlinedTextField(
                        value = password,
                        onValueChange = { 
                            password = it
                            onFieldChanged()
                        },
                        label = { Text(translateText("Пароль или приватный ключ", context)) },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.weight(1f).onFocusChanged {
                            if (it.isFocused) {
                                coroutineScope.launch {
                                    kotlinx.coroutines.delay(300)
                                    val lastIndex = listState.layoutInfo.totalItemsCount - 1
                                    if (lastIndex >= 0) {
                                        listState.animateScrollToItem(lastIndex)
                                    }
                                }
                            }
                        },
                        shape = RoundedCornerShape(12.dp)
                    )"""

new_pwd = """                    OutlinedTextField(
                        value = password,
                        onValueChange = { 
                            password = it
                            onFieldChanged()
                        },
                        label = { Text(translateText("Пароль или приватный ключ", context)) },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { checkConnectionFocusRequester.requestFocus() }),
                        singleLine = true,
                        modifier = Modifier.weight(1f).onFocusChanged {
                            if (it.isFocused) {
                                coroutineScope.launch {
                                    kotlinx.coroutines.delay(300)
                                    val lastIndex = listState.layoutInfo.totalItemsCount - 1
                                    if (lastIndex >= 0) {
                                        listState.animateScrollToItem(lastIndex)
                                    }
                                }
                            }
                        },
                        shape = RoundedCornerShape(12.dp)
                    )"""

content = content.replace(old_pwd, new_pwd)

# Add focusRequester to "Check connection" button
old_btn = """                    Button(
                        onClick = {
                            val p = port.toIntOrNull() ?: 22
                            onTest(ip, p, user, password)
                        },
                        modifier = Modifier.weight(1f).height(48.dp),"""

new_btn = """                    Button(
                        onClick = {
                            val p = port.toIntOrNull() ?: 22
                            onTest(ip, p, user, password)
                        },
                        modifier = Modifier.weight(1f).height(48.dp).focusRequester(checkConnectionFocusRequester),"""

content = content.replace(old_btn, new_btn)

with open("app/src/main/java/com/example/ui/SettingsPanel.kt", "w", encoding="utf-8") as f:
    f.write(content)
