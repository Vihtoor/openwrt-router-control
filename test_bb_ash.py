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
    
    # Move left by 2 (after 1)
    os.write(fd, b'\x1b[D')
    time.sleep(0.1)
    os.write(fd, b'\x1b[D')
    time.sleep(0.1)
    os.read(fd, 1024)
    
    # Type 'A'
    os.write(fd, b'A')
    time.sleep(0.1)
    print("Type middle BB ash ASCII:", os.read(fd, 1024))
    
    # Type Cyrillic 'п'
    os.write(fd, "п".encode('utf-8'))
    time.sleep(0.1)
    print("Type middle BB ash Cyrillic:", os.read(fd, 1024))
