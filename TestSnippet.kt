import android.view.KeyEvent

fun test(keyEvent: KeyEvent) {
    val isVirtual = keyEvent.device?.isVirtual == true || keyEvent.deviceId <= 0
}
