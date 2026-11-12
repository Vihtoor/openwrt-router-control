with open("app/src/main/java/com/example/data/SshKeyManager.kt", "r") as f:
    content = f.read()

import_bc = "import java.security.Security\nimport org.bouncycastle.jce.provider.BouncyCastleProvider\n"
if "import org.bouncycastle.jce.provider.BouncyCastleProvider" not in content:
    content = content.replace("import android.content.Context", import_bc + "import android.content.Context")

init_bc = """    init {
        try {
            if (Security.getProvider("BC") !is BouncyCastleProvider) {
                Security.removeProvider("BC")
                Security.insertProviderAt(BouncyCastleProvider(), 1)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        initKeystore()
    }"""
    
content = content.replace("    init {\n        initKeystore()\n    }", init_bc)

with open("app/src/main/java/com/example/data/SshKeyManager.kt", "w") as f:
    f.write(content)
