with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

content = content.replace('String.format("%.2f Мбит/с"', 'String.format(stringResource(R.string.format_speed_mbps)')
content = content.replace('String.format("%.1f °C"', 'String.format(stringResource(R.string.format_temp_celsius)')

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(content)
