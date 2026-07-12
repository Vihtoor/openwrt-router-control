import re

with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

replacement = """
val strKb = stringResource(R.string.format_bytes_kb)
val strMb = stringResource(R.string.format_bytes_mb)
val strGb = stringResource(R.string.format_bytes_gb)
val formatBytes = { bytes: Long ->
    if (bytes < 1024 * 1024) {
        String.format(strKb, bytes / 1024f)
    } else if (bytes < 1024 * 1024 * 1024) {
        String.format(strMb, bytes / (1024f * 1024f))
    } else {
        String.format(strGb, bytes / (1024f * 1024f * 1024f))
    }
}
"""

content = re.sub(
    r'val formatBytes = \{ bytes: Long ->\s*if \(bytes < 1024 \* 1024\) \{\s*String\.format\(stringResource\(R\.string\.format_bytes_kb\), bytes / 1024f\)\s*\} else if \(bytes < 1024 \* 1024 \* 1024\) \{\s*String\.format\(stringResource\(R\.string\.format_bytes_mb\), bytes / \(1024f \* 1024f\)\)\s*\} else \{\s*String\.format\(stringResource\(R\.string\.format_bytes_gb\), bytes / \(1024f \* 1024f \* 1024f\)\)\s*\}\s*\}',
    replacement.strip(),
    content,
    flags=re.DOTALL
)

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(content)
