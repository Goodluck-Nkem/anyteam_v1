#!/usr/bin/env python

import subprocess
import random
import sys
import json

skills = [ "art", "biology", "history", "language", "logic",
          "math", "music", "spelling", "sport", "technology" ]


cmd = f"grep XSRF-TOKEN cookie/admin.txt" + " | awk '{print $7}'"
csrf = subprocess.check_output(cmd, shell=True, text=True).strip()
print(csrf)


print()
for session_name in sys.argv[1:]:

    print("\033[33m*** ---+S+--- ***\033[0m")

    requirements = random.sample(skills, 4)
    ttl = random.randint(10, 1000) * 100

    body = json.dumps({
        "ttl": ttl,
        "sessionName": session_name,
        "requirements": requirements
        })

    cmd = f"""curl -b cookie/admin.txt -X POST http://localhost:8080/api/v1/session/create \
            -H "Content-Type: application/json" \
            -H "X-XSRF-TOKEN: {csrf}" \
            -d '{body}'"""

    #print(cmd)

    subprocess.run(cmd, shell=True)

    print("\033[33m\n*** ---+E+--- ***\n\033[0m")

