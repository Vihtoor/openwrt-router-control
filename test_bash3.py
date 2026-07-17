import pty
import os
import time

pid, fd = pty.fork()
if pid == 0:
    os.environ['TERM'] = 'xterm'
    os.execvp('sh', ['sh'])
else:
    time.sleep(0.1)
    os.read(fd, 1024)
    os.write(fd, "123".encode('utf-8'))
    time.sleep(0.1)
    os.read(fd, 1024)
    
    # Move left
    os.write(fd, b'\x1b[D')
    time.sleep(0.1)
    os.read(fd, 1024)
    
    # Type A
    os.write(fd, b'A')
    time.sleep(0.1)
    print("Type middle:", os.read(fd, 1024))
