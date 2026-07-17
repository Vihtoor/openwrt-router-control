import pty
import os

pid, fd = pty.fork()
if pid == 0:
    os.environ['TERM'] = 'xterm'
    os.execvp('sh', ['sh'])
else:
    # Read the prompt
    os.read(fd, 1024)
    # Write a Cyrillic character
    os.write(fd, "привет".encode('utf-8'))
    # read echo
    print("Echo 1:", os.read(fd, 1024))
    
    # move left
    os.write(fd, b'\x1b[D')
    print("Echo 2:", os.read(fd, 1024))
    
    # write something in middle
    os.write(fd, "A".encode('utf-8'))
    print("Echo 3:", os.read(fd, 1024))
