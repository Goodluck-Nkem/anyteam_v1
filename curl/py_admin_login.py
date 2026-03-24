#!/usr/bin/env python

import subprocess
import random
import sys
import json


if len(sys.argv) < 4:
    print("Cookie file, Unique Name and Password must be provided!")
    sys.exit(0)

body = json.dumps({
        "uniqueName": sys.argv[2],
        "password": sys.argv[3]
        })

cmd = f"""curl -D - -c {sys.argv[1]} -X POST http://localhost:8080/api/v1/auth/admin/login \
            -H "Content-Type: application/json" \
            -d '{body}'"""

print(cmd)
subprocess.run(cmd, shell=True)

print(f"\033[32m\nCookie in {sys.argv[1]}\n\033[0m")
