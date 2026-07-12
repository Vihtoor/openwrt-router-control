with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

import re
# Only replace stringResource inside translateText. We can just replace all of them from line 7765 to 9370
lines = content.split('\n')
for i in range(7765, min(9375, len(lines))):
    if 'stringResource' in lines[i]:
        lines[i] = lines[i].replace('stringResource', 'context.getString')

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write('\n'.join(lines))
