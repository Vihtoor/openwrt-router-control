with open("app/src/main/java/com/example/data/SshClientManager.kt", "r") as f:
    content = f.read()

import_bc = "import java.security.Security\nimport org.bouncycastle.jce.provider.BouncyCastleProvider\n"
if "import org.bouncycastle.jce.provider.BouncyCastleProvider" not in content:
    content = content.replace("import android.content.Context", import_bc + "import android.content.Context")

init_bc = """class SshClientManager(private val context: Context) {

    init {
        try {
            if (Security.getProvider("BC") !is BouncyCastleProvider) {
                Security.removeProvider("BC")
                Security.insertProviderAt(BouncyCastleProvider(), 1)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
"""
    
content = content.replace("class SshClientManager(private val context: Context) {", init_bc)

with open("app/src/main/java/com/example/data/SshClientManager.kt", "w") as f:
    f.write(content)
