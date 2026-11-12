with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

import_bc = "import java.security.Security\nimport org.bouncycastle.jce.provider.BouncyCastleProvider\n"
if "import org.bouncycastle.jce.provider.BouncyCastleProvider" not in content:
    content = content.replace("import android.os.Bundle", import_bc + "import android.os.Bundle")

init_bc = """        super.onCreate(savedInstanceState)
        try {
            Security.removeProvider("BC")
            Security.insertProviderAt(BouncyCastleProvider(), 1)
        } catch (e: Exception) {
            e.printStackTrace()
        }"""
        
content = content.replace("        super.onCreate(savedInstanceState)", init_bc)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
