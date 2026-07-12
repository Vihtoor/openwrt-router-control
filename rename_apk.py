with open("app/build.gradle.kts", "r") as f:
    content = f.read()

rename_block = """
  applicationVariants.all {
    val variant = this
    variant.outputs.all {
      val outputImpl = this as com.android.build.gradle.internal.api.BaseVariantOutputImpl
      outputImpl.outputFileName = "OpenWrtRouterControl-${variant.versionName}.apk"
    }
  }
"""

if "applicationVariants.all" not in content:
    # insert before the last closing brace of android block
    import re
    # find the android block
    match = re.search(r'android\s*\{', content)
    if match:
        # find the matching closing brace
        idx = match.end()
        braces = 1
        while braces > 0 and idx < len(content):
            if content[idx] == '{': braces += 1
            elif content[idx] == '}': braces -= 1
            idx += 1
        
        # idx is now right after the closing brace of android block
        # we can insert right before idx-1
        content = content[:idx-1] + rename_block + content[idx-1:]

with open("app/build.gradle.kts", "w") as f:
    f.write(content)
