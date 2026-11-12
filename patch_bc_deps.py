with open("gradle/libs.versions.toml", "r") as f:
    content = f.read()

content = content.replace('bouncycastle-bcprov = { group = "org.bouncycastle", name = "bcprov-jdk18on", version.ref = "bouncycastle" }', 'bouncycastle-bcprov = { group = "org.bouncycastle", name = "bcprov-jdk15to18", version.ref = "bouncycastle" }')
content = content.replace('bouncycastle-bcpkix = { group = "org.bouncycastle", name = "bcpkix-jdk18on", version.ref = "bouncycastle" }', 'bouncycastle-bcpkix = { group = "org.bouncycastle", name = "bcpkix-jdk15to18", version.ref = "bouncycastle" }')

with open("gradle/libs.versions.toml", "w") as f:
    f.write(content)
