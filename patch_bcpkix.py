with open("gradle/libs.versions.toml", "r") as f:
    content = f.read()

content = content.replace('bouncycastle-bcprov = { group = "org.bouncycastle", name = "bcprov-jdk18on", version.ref = "bouncycastle" }\n', 'bouncycastle-bcprov = { group = "org.bouncycastle", name = "bcprov-jdk18on", version.ref = "bouncycastle" }\nbouncycastle-bcpkix = { group = "org.bouncycastle", name = "bcpkix-jdk18on", version.ref = "bouncycastle" }\n')

with open("gradle/libs.versions.toml", "w") as f:
    f.write(content)

with open("app/build.gradle.kts", "r") as f:
    app_content = f.read()

app_content = app_content.replace('implementation(libs.bouncycastle.bcprov)\n', 'implementation(libs.bouncycastle.bcprov)\n  implementation(libs.bouncycastle.bcpkix)\n')

with open("app/build.gradle.kts", "w") as f:
    f.write(app_content)
