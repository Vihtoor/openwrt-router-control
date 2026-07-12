import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

devices_card = """            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(devicesCardFocusRequester)
                    .onFocusChanged { isDevicesFocused = it.isFocused }
                    .then(
                        if (isTv) {
                            Modifier
                                .onKeyEvent { keyEvent ->
                                    if (keyEvent.type == KeyEventType.KeyDown) {
                                        when (keyEvent.key) {
                                            Key.DirectionRight -> {
                                                devicesHelpFocusRequester.requestFocus()
                                                true
                                            }
                                            Key.DirectionUp -> {
                                                iperfCardFocusRequester.requestFocus()
                                                true
                                            }
                                            else -> false
                                        }
                                    } else false
                                }
                                .border(
                                    width = 2.dp,
                                    color = if (isDevicesFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                        } else Modifier
                    )
                    .clickable { showDevices = true }
                    .padding(bottom = 10.8.dp)
                    .testTag("devices_start_card"),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 10.8.dp, bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.List,
                            contentDescription = context.getString(R.string.devices_title),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Column {
                            Text(
                                text = context.getString(R.string.devices_title),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    
                    IconButton(
                        onClick = { activeHelpDialog = "devices" },
                        modifier = Modifier
                            .testTag("btn_help_devices")
                            .focusRequester(devicesHelpFocusRequester)
                            .onFocusChanged { isDevicesHelpFocused = it.isFocused }
                            .then(
                                if (isTv) {
                                    Modifier
                                        .onKeyEvent { keyEvent ->
                                            if (keyEvent.type == KeyEventType.KeyDown) {
                                                when (keyEvent.key) {
                                                    Key.DirectionLeft -> {
                                                        devicesCardFocusRequester.requestFocus()
                                                        true
                                                    }
                                                    Key.DirectionUp -> {
                                                        iperfHelpFocusRequester.requestFocus()
                                                        true
                                                    }
                                                    else -> false
                                                }
                                            } else false
                                        }
                                        .border(
                                            width = 1.5.dp,
                                            color = if (isDevicesHelpFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                                            shape = RoundedCornerShape(24.dp)
                                        )
                                } else Modifier
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = TestTabLocalizations.getHelpButtonDesc(locale),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))"""

if "testTag(\"devices_start_card\")" not in content:
    content = content.replace("            Spacer(modifier = Modifier.height(16.dp))", devices_card, 1)
    with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
        f.write(content)
    print("Inserted devices card")
else:
    print("Already inserted")
