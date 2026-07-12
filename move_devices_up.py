import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

# Find the devices card and spacer
start_idx = content.find('            Card(\n                modifier = Modifier\n                    .fillMaxWidth()\n                    .focusRequester(devicesCardFocusRequester)')
if start_idx == -1:
    print("Not found devices card")
    exit(1)
    
end_idx = content.find('            Spacer(modifier = Modifier.height(16.dp))\n        }\n    }\n\n    if (activeHelpDialog != null)', start_idx)
if end_idx == -1:
    print("Not found end of devices card")
    exit(1)
    
end_idx += len('            Spacer(modifier = Modifier.height(16.dp))\n')

devices_card_block = content[start_idx:end_idx]

# Remove it from the current position
content = content[:start_idx] + content[end_idx:]

# Now find where to insert it.
# We want it right after:
#         Column(
#             modifier = Modifier
#                 .fillMaxSize()
#                 .padding(16.dp)
#                 .verticalScroll(androidx.compose.foundation.rememberScrollState())
#                 .testTag("test_tab_view")
#         ) {
# 

insert_idx = content.find('.testTag("test_tab_view")\n        ) {\n')
if insert_idx == -1:
    print("Not found test tab column")
    exit(1)
    
insert_idx += len('.testTag("test_tab_view")\n        ) {\n')

content = content[:insert_idx] + devices_card_block + content[insert_idx:]

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
print("done")

