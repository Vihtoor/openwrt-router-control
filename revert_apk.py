with open("app/build.gradle.kts", "r") as f:
    content = f.read()

bad_block = """
  applicationVariants.all {
    val variant = this
    variant.outputs.all {
      val outputImpl = this as com.android.build.gradle.internal.api.BaseVariantOutputImpl
      outputImpl.outputFileName = "OpenWrtRouterControl-${variant.versionName}.apk"
    }
  }
"""

content = content.replace(bad_block, "")

# Instead we can try this inside android block:
#     buildTypes.all {
#         resValue("string", "app_version", "1.6.6")
#     }
# No, let's try just:
# androidComponents.onVariants { ... } but it might be too complex and break again.

with open("app/build.gradle.kts", "w") as f:
    f.write(content)
