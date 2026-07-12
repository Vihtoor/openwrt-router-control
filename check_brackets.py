with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

start_idx = content.find("fun DeviceListDialog(")
end_idx = content.find("@Composable\nfun DeviceSpeedDialog(")

dialog_code = content[start_idx:end_idx]

open_b = dialog_code.count("{")
close_b = dialog_code.count("}")

print("Open:", open_b, "Close:", close_b)
