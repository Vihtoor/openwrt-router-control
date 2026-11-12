sed -i 's/lastOpenVpnState = status.isOpenVpnActive//g' app/src/main/java/com/example/RouterWidgetProvider.kt
sed -i 's/lastWireGuardState = status.isWireGuardActive//g' app/src/main/java/com/example/RouterWidgetProvider.kt
sed -i 's/lastOpenVpnState = resultStatus.isOpenVpnActive//g' app/src/main/java/com/example/RouterWidgetProvider.kt
sed -i 's/lastWireGuardState = resultStatus.isWireGuardActive//g' app/src/main/java/com/example/RouterWidgetProvider.kt
sed -i 's/lastOpenVpnState = true//g' app/src/main/java/com/example/RouterWidgetProvider.kt
sed -i 's/lastWireGuardState = true//g' app/src/main/java/com/example/RouterWidgetProvider.kt
sed -i 's/lastOpenVpnState = false//g' app/src/main/java/com/example/RouterWidgetProvider.kt
sed -i 's/lastWireGuardState = false//g' app/src/main/java/com/example/RouterWidgetProvider.kt
sed -i 's/lastOpenVpnState = previousOpenVpnState//g' app/src/main/java/com/example/RouterWidgetProvider.kt
sed -i 's/lastWireGuardState = previousWireGuardState//g' app/src/main/java/com/example/RouterWidgetProvider.kt
sed -i 's/var previousOpenVpnState = lastOpenVpnState//g' app/src/main/java/com/example/RouterWidgetProvider.kt
sed -i 's/var previousWireGuardState = lastWireGuardState//g' app/src/main/java/com/example/RouterWidgetProvider.kt
sed -i 's/previousOpenVpnState = lastOpenVpnState//g' app/src/main/java/com/example/RouterWidgetProvider.kt
sed -i 's/previousWireGuardState = lastWireGuardState//g' app/src/main/java/com/example/RouterWidgetProvider.kt
sed -i 's/val currentOpenVpn = lastOpenVpnState ?: false/val currentOpenVpn = false/g' app/src/main/java/com/example/RouterWidgetProvider.kt
sed -i 's/val currentWireGuard = lastWireGuardState ?: false/val currentWireGuard = false/g' app/src/main/java/com/example/RouterWidgetProvider.kt
sed -i 's/lastOpenVpnState/null/g' app/src/main/java/com/example/RouterWidgetProvider.kt
sed -i 's/lastWireGuardState/null/g' app/src/main/java/com/example/RouterWidgetProvider.kt
