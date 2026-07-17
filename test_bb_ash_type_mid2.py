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
    os.write(fd, "1п3".encode('utf-8'))
    time.sleep(0.1)
    os.read(fd, 1024)
    
    # Send left arrow until we are at 1
    os.write(fd, b'\x1b[D' * 4)
    time.sleep(0.1)
    os.read(fd, 1024)
    
    # Type 'A'
    os.write(fd, b'A')
    time.sleep(0.1)
    print("Type A:", os.read(fd, 1024))
