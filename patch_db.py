import re

with open("app/src/main/java/com/example/data/RouterDatabase.kt", "r") as f:
    content = f.read()

content = content.replace("@Database(entities = [RouterConfig::class, ConsoleLog::class], version = 9, exportSchema = false)",
                          "@Database(entities = [RouterConfig::class, ConsoleLog::class, SshKeyEntity::class], version = 10, exportSchema = false)")

content = content.replace("abstract fun routerDao(): RouterDao",
                          "abstract fun routerDao(): RouterDao\n    abstract fun sshKeyDao(): SshKeyDao")

with open("app/src/main/java/com/example/data/RouterDatabase.kt", "w") as f:
    f.write(content)
