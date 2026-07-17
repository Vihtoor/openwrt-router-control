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
    os.write(fd, "1A3".encode('utf-8'))
    time.sleep(0.1)
    os.read(fd, 1024)
    
    # Send left arrow once
    os.write(fd, b'\x1b[D')
    time.sleep(0.1)
    os.read(fd, 1024)
    
    # Send ONE DEL
    os.write(fd, b'\x7f')
    time.sleep(0.1)
    print("DEL mid ASCII:", os.read(fd, 1024))
