#!/usr/bin/env python

import subprocess
import random
import sys
import json


if len(sys.argv) < 2:
    print("Unique Name must be provided!")
    sys.exit(0)


cookie_name = "cookie/admin.txt" if sys.argv[1].lower() == "admin" else f"cookie/{sys.argv[1]}.txt"
cmd = f"grep XSRF-TOKEN {cookie_name}" + " | awk '{print $7}'"
csrf = subprocess.check_output(cmd, shell=True, text=True).strip()
cmd = f"""curl -D - -b {cookie_name} -c {cookie_name} -X POST http://localhost:8080/api/v1/auth/logout \
        -H "Content-Type: application/json" \
        -H "X-XSRF-TOKEN: {csrf}" \
        """

subprocess.run(cmd, shell=True)

print(f"\033[32m\nCookie in {cookie_name}\n\033[0m")
