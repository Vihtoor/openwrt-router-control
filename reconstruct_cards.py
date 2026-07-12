import sys

cards = """            // Wi-Fi Analyzer Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(wifiCardFocusRequester)
                    .onFocusChanged { isWifiFocused = it.isFocused }
                    .then(
                        if (isTv) {
                            Modifier
                                .onKeyEvent { keyEvent ->
                                    if (keyEvent.type == KeyEventType.KeyDown) {
                                        when (keyEvent.key) {
                                            Key.DirectionRight -> {
                                                wifiHelpFocusRequester.requestFocus()
                                                true
                                            }
                                            Key.DirectionUp -> {
                                                devicesCardFocusRequester.requestFocus()
                                                true
                                            }
                                            Key.DirectionDown -> {
                                                iperfCardFocusRequester.requestFocus()
                                                true
                                            }
                                            else -> false
                                        }
                                    } else false
                                }
                                .border(
                                    width = 2.dp,
                                    color = if (isWifiFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                        } else Modifier
                    )
                    .clickable { onWifiAnalyzerFullScreenChange(true) }
                    .padding(bottom = 10.8.dp)
                    .testTag("wifi_analyzer_card"),
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
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = TestTabLocalizations.getWifiAnalyzerTitle(locale),
                            tint = Color(0xFFFF3B30),
                            modifier = Modifier.size(24.dp)
                        )
                        Column {
                            Text(
                                text = TestTabLocalizations.getWifiAnalyzerTitle(locale),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = TestTabLocalizations.getWifiAnalyzerDesc(locale),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    
                    IconButton(
                        onClick = { activeHelpDialog = "wifi_analyzer" },
                        modifier = Modifier
                            .testTag("btn_help_wifi_analyzer")
                            .focusRequester(wifiHelpFocusRequester)
                            .onFocusChanged { isWifiHelpFocused = it.isFocused }
                            .then(
                                if (isTv) {
                                    Modifier
                                        .onKeyEvent { keyEvent ->
                                            if (keyEvent.type == KeyEventType.KeyDown) {
                                                when (keyEvent.key) {
                                                    Key.DirectionLeft -> {
                                                        wifiCardFocusRequester.requestFocus()
                                                        true
                                                    }
                                                    Key.DirectionUp -> {
                                                        devicesHelpFocusRequester.requestFocus()
                                                        true
                                                    }
                                                    Key.DirectionDown -> {
                                                        iperfHelpFocusRequester.requestFocus()
                                                        true
                                                    }
                                                    else -> false
                                                }
                                            } else false
                                        }
                                        .border(
                                            width = 1.5.dp,
                                            color = if (isWifiHelpFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                                            shape = RoundedCornerShape(24.dp)
                                        )
                                } else Modifier
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = TestTabLocalizations.getHelpButtonDesc(context.resources.configuration.locales[0].language),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Beautiful M3 iPerf3 Card below M-Lab
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(iperfCardFocusRequester)
                    .onFocusChanged { isIperfFocused = it.isFocused }
                    .then(
                        if (isTv) {
                            Modifier
                                .onKeyEvent { keyEvent ->
                                    if (keyEvent.type == KeyEventType.KeyDown) {
                                        when (keyEvent.key) {
                                            Key.DirectionRight -> {
                                                iperfHelpFocusRequester.requestFocus()
                                                true
                                            }
                                            Key.DirectionUp -> {
                                                wifiCardFocusRequester.requestFocus()
                                                true
                                            }
                                            Key.DirectionDown -> {
                                                firstCardFocusRequester.requestFocus()
                                                true
                                            }
                                            else -> false
                                        }
                                    } else false
                                }
                                .border(
                                    width = 2.dp,
                                    color = if (isIperfFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                        } else Modifier
                    )
                    .clickable { onIPerfFullScreenChange(true) }
                    .padding(bottom = 10.8.dp)
                    .testTag("iperf_card"),
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
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = context.getString(R.string.iperf_card_title),
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(24.dp)
                        )
                        Column {
                            Text(
                                text = context.getString(R.string.iperf_card_title),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = context.getString(R.string.iperf_card_desc),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    
                    IconButton(
                        onClick = { activeHelpDialog = "iperf" },
                        modifier = Modifier
                            .testTag("btn_help_iperf")
                            .focusRequester(iperfHelpFocusRequester)
                            .onFocusChanged { isIperfHelpFocused = it.isFocused }
                            .then(
                                if (isTv) {
                                    Modifier
                                        .onKeyEvent { keyEvent ->
                                            if (keyEvent.type == KeyEventType.KeyDown) {
                                                when (keyEvent.key) {
                                                    Key.DirectionLeft -> {
                                                        iperfCardFocusRequester.requestFocus()
                                                        true
                                                    }
                                                    Key.DirectionUp -> {
                                                        wifiHelpFocusRequester.requestFocus()
                                                        true
                                                    }
                                                    Key.DirectionDown -> {
                                                        mlabHelpFocusRequester.requestFocus()
                                                        true
                                                    }
                                                    else -> false
                                                }
                                            } else false
                                        }
                                        .border(
                                            width = 1.5.dp,
                                            color = if (isIperfHelpFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                                            shape = RoundedCornerShape(24.dp)
                                        )
                                } else Modifier
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = TestTabLocalizations.getHelpButtonDesc(context.resources.configuration.locales[0].language),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
"""

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

# I want to insert it right after the devices card ends and before `val locale = ...`
insert_target = """                    }
                }
            }


            val locale = context.resources.configuration.locales[0].language"""

replacement = """                    }
                }
            }

""" + cards + """

            val locale = context.resources.configuration.locales[0].language"""

if insert_target in content:
    content = content.replace(insert_target, replacement)
    with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
        f.write(content)
    print("Success")
else:
    print("Target not found")
