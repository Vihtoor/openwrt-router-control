import re

with open("gradle/libs.versions.toml", "r") as f:
    content = f.read()

content = content.replace('jsch = "0.2.20"\n', 'jsch = "0.2.20"\nbouncycastle = "1.78.1"\n')
content = content.replace('jsch = { group = "com.github.mwiede", name = "jsch", version.ref = "jsch" }\n', 'jsch = { group = "com.github.mwiede", name = "jsch", version.ref = "jsch" }\nbouncycastle-bcprov = { group = "org.bouncycastle", name = "bcprov-jdk18on", version.ref = "bouncycastle" }\n')

with open("gradle/libs.versions.toml", "w") as f:
    f.write(content)

with open("app/build.gradle.kts", "r") as f:
    app_content = f.read()

app_content = app_content.replace('implementation(libs.jsch)', 'implementation(libs.jsch)\n  implementation(libs.bouncycastle.bcprov)')

with open("app/build.gradle.kts", "w") as f:
    f.write(app_content)
