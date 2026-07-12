with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    main = f.read()

old_list = """    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(
            top = 8.dp,
            bottom = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (isTv || (isTablet && !isPortrait)) {"""

new_list = """    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(
            top = 8.dp,
            bottom = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            CapabilitiesBanner(state.config)
        }
        if (isTv || (isTablet && !isPortrait)) {"""

main = main.replace(old_list, new_list)
with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(main)

