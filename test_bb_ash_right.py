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
    
    # Send left arrow twice (to get before п)
    os.write(fd, b'\x1b[D\x1b[D\x1b[D')
    time.sleep(0.1)
    os.read(fd, 1024)
    
    # Send right arrow ONE time
    os.write(fd, b'\x1b[C')
    time.sleep(0.1)
    print("Move right once:", os.read(fd, 1024))
