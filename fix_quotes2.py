with open("app/src/main/java/com/example/data/RouterRepository.kt", "r") as f:
    repo = f.read()

repo = repo.replace('parts[macIdx].replace(""", "").lowercase()', 'parts[macIdx].replace("\\"", "").lowercase()')
repo = repo.replace('parts[rxIdx].replace(""", "").toLongOrNull()', 'parts[rxIdx].replace("\\"", "").toLongOrNull()')
repo = repo.replace('parts[txIdx].replace(""", "").toLongOrNull()', 'parts[txIdx].replace("\\"", "").toLongOrNull()')
repo = repo.replace('parts[i].replace(""", "").lowercase()', 'parts[i].replace("\\"", "").lowercase()')

with open("app/src/main/java/com/example/data/RouterRepository.kt", "w") as f:
    f.write(repo)
