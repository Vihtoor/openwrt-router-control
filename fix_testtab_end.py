with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

# The inserted block was:
block_to_remove = """
    if (showDevices) {
        DeviceListDialog(
            title = context.getString(R.string.devices_title),
            config = state.config,
            devices = state.deviceSpeeds,
            deviceHistory = state.deviceSpeedHistory,
            onDismiss = { showDevices = false }
        )
    }
"""

if block_to_remove in content:
    content = content.replace(block_to_remove, "")
    
# Now find the end of TestTab, which is marked by:
#         }
#     }
# }
# 
# @Composable
# fun Text(

end_marker = """        }
    }
}

@Composable
fun Text("""

new_end = """        }
    }

    if (showDevices) {
        DeviceListDialog(
            title = androidx.compose.ui.res.stringResource(R.string.devices_title),
            config = state.config,
            devices = state.deviceSpeeds,
            deviceHistory = state.deviceSpeedHistory,
            onDismiss = { showDevices = false }
        )
    }
}

@Composable
fun Text("""

content = content.replace(end_marker, new_end)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
print("done")
