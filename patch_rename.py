import sys

with open("app/build.gradle.kts", "r") as f:
    content = f.read()

bad_block = """
val androidExtension = extensions.getByType(com.android.build.gradle.internal.dsl.BaseAppModuleExtension::class.java)
androidExtension.applicationVariants.all {
    val variant = this
    variant.outputs.all {
        val outputImpl = this as com.android.build.gradle.internal.api.BaseVariantOutputImpl
        outputImpl.outputFileName = "OpenWrtRouterControl-${variant.versionName}.apk"
    }
}
"""

content = content.replace(bad_block, "")

with open("app/build.gradle.kts", "w") as f:
    f.write(content)
