with open("app/src/main/res/xml/file_paths.xml", "r") as f:
    content = f.read()

content = content.replace("</paths>", '    <files-path name="ssh_keys" path=".ssh/" />\n</paths>')

with open("app/src/main/res/xml/file_paths.xml", "w") as f:
    f.write(content)
