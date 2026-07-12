with open("app/build.gradle.kts", "r") as f:
    content = f.read()

if "archivesBaseName" not in content:
    content = content.replace('applicationId = "com.example"', 'applicationId = "com.example"\n        setProperty("archivesBaseName", "OpenWrtRouterControl-1.6.6")')

with open("app/build.gradle.kts", "w") as f:
    f.write(content)
