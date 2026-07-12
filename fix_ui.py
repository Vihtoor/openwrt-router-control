with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

replacement = """@Composable
fun TelemetryCard(state: UiState, modifier: Modifier = Modifier) {
    var showTemperature by remember { mutableStateOf(false) }
    LaunchedEffect(state.speedHistory.lastOrNull()?.timestamp) {
        showTemperature = !showTemperature
    }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("telemetry_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Processor
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (showTemperature) getLocalizedText("Температура") else getLocalizedText("Процессор"),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (showTemperature) {
                    if (state.cpuTemperature == null) {
                        Text(
                            text = getLocalizedText("недоступна"),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    } else {
                        val temp = state.cpuTemperature
                        val tempColor = when {
                            temp < 65f -> Color(0xFF4CAF50)
                            temp < 75f -> Color(0xFFFFC107)
                            else -> Color(0xFFF44336)
                        }
                        Text(
                            text = String.format("%.1f °C", temp),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = tempColor,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    Text(
                        text = state.cpuUsage,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }"""

target = """@Composable
fun TelemetryCard(state: UiState, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("telemetry_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Processor
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Процессор",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = state.cpuUsage,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }"""

content = content.replace(target.strip(), replacement.strip())

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
