#!/usr/bin/env python

import subprocess
import random
import sys
import json


# initialize system
subprocess.run(f"./valid_sysconfig_init.sh {random.randint(10, 10000) * 100}", shell=True)
print()

for team_name in sys.argv[1:]:


    body = json.dumps({
        "teamName": team_name
        })

    cmd = f"""curl -X POST http://localhost:8080/api/v1/team/create \
            -H "Content-Type: application/json" \
            -d '{body}'"""

    subprocess.run(cmd, shell=True)

    print()

