import pty
import os
import time

pid, fd = pty.fork()
if pid == 0:
    os.environ['TERM'] = 'xterm'
    os.execvp('./busybox', ['./busybox', 'ash'])
else:
    time.sleep(0.1)
    os.read(fd, 1024)
    os.write(fd, "123".encode('utf-8'))
    time.sleep(0.1)
    os.read(fd, 1024)
    
    # Move left by 2
    os.write(fd, b'\x1b[D\x1b[D')
    time.sleep(0.1)
    os.read(fd, 1024)
    
    # Type 'п'
    os.write(fd, "п".encode('utf-8'))
    time.sleep(0.1)
    print("Type Cyrillic in middle:", os.read(fd, 1024))
