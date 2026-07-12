with open('app/src/main/java/com/example/ui/RouterViewModel.kt', 'r') as f:
    content = f.read()

content = content.replace('val formattedConnectionType: String get() {', 'fun formattedConnectionType(context: android.content.Context): String {')
content = content.replace('application.getString(com.example.R.string', 'context.getString(com.example.R.string')

with open('app/src/main/java/com/example/ui/RouterViewModel.kt', 'w') as f:
    f.write(content)
