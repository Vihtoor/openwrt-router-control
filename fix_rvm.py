with open('app/src/main/java/com/example/ui/RouterViewModel.kt', 'r') as f:
    content = f.read()

replacement = """
    fun formattedConnectionType(context: android.content.Context): String {
        return when (portMappingConfidence) {
            com.example.data.PortMappingConfidence.EXACT -> connectionType
            com.example.data.PortMappingConfidence.APPROXIMATE -> connectionType + " " + context.getString(com.example.R.string.port_approximate)
            com.example.data.PortMappingConfidence.UNKNOWN -> context.getString(com.example.R.string.port_unknown)
        }
    }
"""

import re
content = re.sub(
    r'val formattedConnectionType: String get\(\) \{\s*return when \(portMappingConfidence\) \{\s*com\.example\.data\.PortMappingConfidence\.EXACT -> connectionType\s*com\.example\.data\.PortMappingConfidence\.APPROXIMATE -> connectionType \+ " " \+ application\.getString\(com\.example\.R\.string\.port_approximate\)\s*com\.example\.data\.PortMappingConfidence\.UNKNOWN -> application\.getString\(com\.example\.R\.string\.port_unknown\)\s*\}\s*\}',
    replacement.strip(),
    content
)

with open('app/src/main/java/com/example/ui/RouterViewModel.kt', 'w') as f:
    f.write(content)
