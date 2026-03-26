#!/usr/bin/env python

import subprocess
import sys
import json

# Parse arguments safely
args = sys.argv[1:]

if len(args) < 1:
    print("Cookie name must be provided!")
    sys.exit(0)

cookie_arg = f"-b {args[0]}"
session_id = args[1] if len(args) >= 2 else None
player_ids = args[2:] if len(args) >= 3 else None

body = json.dumps({
    "sessionId": session_id,
    "playerIds": player_ids
    })

cmd = f"grep XSRF-TOKEN {args[0]}" + " | awk '{print $7}'"
csrf = subprocess.check_output(cmd, shell=True, text=True).strip()
cmd = f"""curl -D - {cookie_arg} -X POST http://localhost:8080/api/v1/team/play \
        -H "Content-Type: application/json" \
        -H "X-XSRF-TOKEN: {csrf}" \
        -d '{body}'"""

subprocess.run(cmd, shell=True)

print()

