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
    os.write(fd, "привет".encode('utf-8'))
    time.sleep(0.1)
    os.read(fd, 1024)
    
    # Send DEL
    os.write(fd, b'\x7f')
    time.sleep(0.1)
    print("DEL 1:", os.read(fd, 1024))
    
    os.write(fd, b'\x7f')
    time.sleep(0.1)
    print("DEL 2:", os.read(fd, 1024))
