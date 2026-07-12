with open('app/src/main/java/com/example/ui/RouterViewModel.kt', 'r') as f:
    content = f.read()

content = content.replace('"$connectionType (предположительно)"', 'connectionType + " " + application.getString(com.example.R.string.port_approximate)')
content = content.replace('"Подключено по Ethernet, порт не определён"', 'application.getString(com.example.R.string.port_unknown)')
content = content.replace('"Переключение..."', 'application.getString(com.example.R.string.status_switching)')
content = content.replace('"Применение изменений..."', 'application.getString(com.example.R.string.status_applying_changes)')
content = content.replace('"Включение..."', 'application.getString(com.example.R.string.status_turning_on)')
content = content.replace('"Выключение..."', 'application.getString(com.example.R.string.status_turning_off)')
content = content.replace('"Перезапуск..."', 'application.getString(com.example.R.string.status_restarting)')
content = content.replace('"Ожидание соединения..."', 'application.getString(com.example.R.string.status_waiting_conn)')
content = content.replace('"Пожалуйста, подождите..."', 'application.getString(com.example.R.string.status_please_wait)')

with open('app/src/main/java/com/example/ui/RouterViewModel.kt', 'w') as f:
    f.write(content)
