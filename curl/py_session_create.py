#!/usr/bin/env python

import subprocess
import random
import sys
import json

skills = [ "art", "biology", "history", "language", "logic",
          "math", "music", "spelling", "sport", "technology" ]

# initialize system
subprocess.run(f"./valid_sysconfig_init.sh {random.randint(10, 10000) * 100}", shell=True)
print()

for session_name in sys.argv[1:]:

    requirements = random.sample(skills, 4)
    ttl = random.randint(10, 1000) * 100

    body = json.dumps({
        "ttl": ttl,
        "sessionName": session_name,
        "requirements": requirements
        })

    cmd = f"""curl -X POST http://localhost:8080/api/v1/session/create \
            -H "Content-Type: application/json" \
            -d '{body}'"""

    subprocess.run(cmd, shell=True)

    print()

