import re

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

new_end = """                        }
                    }
                }
            }
            }
        }
    }
}

@Composable
fun DeviceSpeedDialog("""

content = content.replace(old_end, new_end)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
