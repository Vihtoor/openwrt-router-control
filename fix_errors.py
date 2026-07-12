import re

with open("app/src/main/java/com/example/data/RouterRepository.kt", "r") as f:
    content = f.read()

content = content.replace('split("\s+".toRegex())', 'split("""\\s+""".toRegex())')

with open("app/src/main/java/com/example/data/RouterRepository.kt", "w") as f:
    f.write(content)

with open("app/src/main/java/com/example/ui/RouterViewModel.kt", "r") as f:
    content = f.read()

bad_block = """currentState.copy(
                speedHistory = updatedHistory,
                currentDownloadSpeed = download,
                currentUploadSpeed = upload,
                cpuUsage = cpu,
                memoryUsage = mem,
                uptime = uptime,
                deviceSpeeds = devices
            )"""

good_block = """currentState.copy(
                cpuUsage = cpu,
                memoryUsage = mem,
                uptime = uptime,
                deviceSpeeds = devices
            )"""

content = content.replace(bad_block, good_block)
# it was replaced in both places, wait, I need to make sure the first one stays!
# Actually, the bad block might appear twice now!
# Let's check where it appears.
