with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

old_end = """                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DeviceSpeedDialog("""

print(old_end in content)
