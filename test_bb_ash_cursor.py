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
    os.write(fd, "п".encode('utf-8'))
    time.sleep(0.1)
    os.read(fd, 1024)
    
    # Send one left arrow
    os.write(fd, b'\x1b[D')
    time.sleep(0.1)
    print("Move left once:", os.read(fd, 1024))
