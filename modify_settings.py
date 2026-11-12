import sys

def modify_file(filepath):
    with open(filepath, 'r') as f:
        content = f.read()

    # Target 1: Password field
    target1 = """                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
                                            kotlinx.coroutines.delay(300)
                                            listState.animateScrollBy(10000f)
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
                        }"""
    replacement1 = """                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
    
    if target1 in content:
        content = content.replace(target1, replacement1)
        print("Target 1 replaced.")
    else:
        print("Target 1 not found.")

    # Target 2: SSH Key mode
    target2 = """                            Spacer(modifier = Modifier.height(12.dp))
                            var pubKey by remember { mutableStateOf<String?>(null) }
                            LaunchedEffect(currentKey) {
                                pubKey = SshKeyManager(context).loadPublicKeyString(currentKey!!.id)
                            }
                            
                            OutlinedTextField(
                                value = pubKey?.take(40)?.let { "$it..." } ?: "",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text(translateText("Публичный ключ (скопируйте на роутер)", context)) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                trailingIcon = {
                                    IconButton(onClick = {
                                        if (pubKey != null) {
                                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                            clipboard.setPrimaryClip(ClipData.newPlainText("SSH Public Key", pubKey))
                                            Toast.makeText(context, translateText("Публичный ключ скопирован", context), Toast.LENGTH_SHORT).show()
                                        }
                                    }) {
                                        Icon(Icons.Default.ContentCopy, contentDescription = null)
                                    }
                                }
                            )"""
    replacement2 = """                            Spacer(modifier = Modifier.height(12.dp))
                            var pubKey by remember { mutableStateOf<String?>(null) }
                            LaunchedEffect(currentKey) {
                                pubKey = SshKeyManager(context).loadPublicKeyString(currentKey!!.id)
                            }
                            
                            val copyButtonFocusRequester = remember { FocusRequester() }
                            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = pubKey?.take(40)?.let { "$it..." } ?: "",
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text(translateText("Публичный ключ (скопируйте на роутер)", context)) },
                                    modifier = Modifier.weight(1f).onFocusChanged {
                                        if (isTv && it.isFocused) {
                                            copyButtonFocusRequester.requestFocus()
                                        }
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    trailingIcon = {
                                        IconButton(
                                            onClick = {
                                                if (pubKey != null) {
                                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                                    clipboard.setPrimaryClip(ClipData.newPlainText("SSH Public Key", pubKey))
                                                    Toast.makeText(context, translateText("Публичный ключ скопирован", context), Toast.LENGTH_SHORT).show()
                                                }
                                            },
                                            modifier = Modifier.focusRequester(copyButtonFocusRequester)
                                        ) {
                                            Icon(Icons.Default.ContentCopy, contentDescription = null)
                                        }
                                    }
                                )
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

    if target2 in content:
        content = content.replace(target2, replacement2)
        print("Target 2 replaced.")
    else:
        print("Target 2 not found.")

    # Target 3: Bottom row
    target3 = """                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    if (!isFirst) {
                        val cancelInteractionSource = remember { MutableInteractionSource() }
                        val isCancelFocused by cancelInteractionSource.collectIsFocusedAsState()
                        OutlinedButton(
                            onClick = onCancel,
                            interactionSource = cancelInteractionSource,
                            modifier = Modifier.weight(1f).height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (isCancelFocused) MaterialTheme.colorScheme.tertiary else Color.Transparent,
                                contentColor = if (isCancelFocused) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(translateText("Отмена", context))
                        }
                    }
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
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        enabled = true,
                        contentPadding = PaddingValues(0.dp),
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
                }"""
    replacement3 = """                if (!isFirst || !isTv) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        if (!isFirst) {
                            val cancelInteractionSource = remember { MutableInteractionSource() }
                            val isCancelFocused by cancelInteractionSource.collectIsFocusedAsState()
                            OutlinedButton(
                                onClick = onCancel,
                                interactionSource = cancelInteractionSource,
                                modifier = Modifier.weight(1f).height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (isCancelFocused) MaterialTheme.colorScheme.tertiary else Color.Transparent,
                                    contentColor = if (isCancelFocused) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Text(translateText("Отмена", context))
                            }
                        }
                        if (!isTv) {
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
                                modifier = Modifier.weight(1f).height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                enabled = true,
                                contentPadding = PaddingValues(0.dp),
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
                    }
                }"""

    if target3 in content:
        content = content.replace(target3, replacement3)
        print("Target 3 replaced.")
    else:
        print("Target 3 not found.")

    with open(filepath, 'w') as f:
        f.write(content)

modify_file('app/src/main/java/com/example/ui/SettingsPanel.kt')
