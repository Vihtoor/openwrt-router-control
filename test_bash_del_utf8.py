import pty
import os
import time

pid, fd = pty.fork()
if pid == 0:
    os.environ['TERM'] = 'xterm'
    os.execvp('bash', ['bash'])
else:
    time.sleep(0.1)
    os.read(fd, 1024)
    os.write(fd, "п".encode('utf-8'))
    time.sleep(0.1)
    os.read(fd, 1024)
    
    # Send ONE DEL
    os.write(fd, b'\x7f')
    time.sleep(0.1)
    print("ONE DEL Bash:", os.read(fd, 1024))
