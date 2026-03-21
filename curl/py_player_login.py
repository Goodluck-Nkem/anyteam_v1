#!/usr/bin/env python

import subprocess
import random
import sys
import json


if len(sys.argv) < 3:
    print("Unique Name and Password must be provided!")
    sys.exit(0)

body = json.dumps({
        "uniqueName": sys.argv[1],
        "password": sys.argv[2]
        })

cookie_name = f"cookie/{sys.argv[1]}.txt"
cmd = f"""curl -D - -c {cookie_name} -X POST http://localhost:8080/api/v1/auth/player/login \
            -H "Content-Type: application/json" \
            -d '{body}'"""

subprocess.run(cmd, shell=True)

print(f"\033[32m\nCookie in {cookie_name}\n\033[0m")
