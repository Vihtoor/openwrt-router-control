import sys

def modify_file(filepath):
    with open(filepath, 'r') as f:
        content = f.read()

    # Target 1: Password field
    start_str = "Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {\n                            OutlinedTextField(\n                                value = password,"
    end_str = "Icon(imageVector = image, contentDescription = null)\n                            }\n                        }"
    
    start_idx = content.find(start_str)
    end_idx = content.find(end_str, start_idx) + len(end_str)
    
    if start_idx != -1 and end_idx != -1:
        target1 = content[start_idx:end_idx]
        print("Target 1 found.")
    else:
        print("Target 1 not found.", start_idx, end_idx)
        return

    replacement1 = """Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = password,
                                onValueChange = { 
                                     password = it
                                    onFieldChanged()
                                },
                                label = { Text(translateText("Пароль", context)) },
                                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                                singleLine = true,
                                modifier = Modifier.weight(1f).onFocusChanged {
                                    isPasswordFocused = it.isFocused
                                    if (it.isFocused) {
                                        coroutineScope.launch {
                                            if (isTv) {
                                                listState.animateScrollToItem(5)
                                            } else {
                                                kotlinx.coroutines.delay(300)
                                                listState.animateScrollBy(10000f)
                                            }
                                        }
                                    }
                                },
                                shape = RoundedCornerShape(12.dp)
                            )
                            val image = if (passwordVisible) Icons.Default.Clear else Icons.Default.Lock
                            IconButton(
                                onClick = { passwordVisible = !passwordVisible },
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Icon(imageVector = image, contentDescription = null)
                            }
                            if (isTv) {
                                val testInteractionSource = remember { MutableInteractionSource() }
                                val isTestFocused by testInteractionSource.collectIsFocusedAsState()
                                Button(
                                    onClick = {
                                        if (!isConnecting) {
                                            val p = port.toIntOrNull() ?: 22
                                            onTest(ip, p, user, password, sshKeyId)
                                        }
                                    },
                                    interactionSource = testInteractionSource,
                                    modifier = Modifier.padding(top = 8.dp).height(48.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    enabled = true,
                                    contentPadding = PaddingValues(horizontal = 16.dp),
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
                        }"""
    
    content = content.replace(target1, replacement1)
    
    with open(filepath, 'w') as f:
        f.write(content)
    print("Target 1 replaced successfully.")

modify_file('app/src/main/java/com/example/ui/SettingsPanel.kt')
