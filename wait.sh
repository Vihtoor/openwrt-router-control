while kill -0 $(cat app.pid 2>/dev/null) 2>/dev/null; do sleep 1; done
