with open("app/src/main/java/com/example/ui/RouterViewModel.kt", "r") as f:
    content = f.read()

bad_clean_host = """        return host.trim()
    }"""
good_clean_host = """        host = host.trim().removePrefix("[").removeSuffix("]")
        return host
    }"""

content = content.replace(bad_clean_host, good_clean_host)

with open("app/src/main/java/com/example/ui/RouterViewModel.kt", "w") as f:
    f.write(content)
