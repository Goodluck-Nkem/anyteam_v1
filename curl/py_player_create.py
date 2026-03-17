#!/usr/bin/env python

import subprocess
import random
import sys
import json

# Available skills
skills = [
    "art", "biology", "history", "language", "logic",
    "math", "music", "spelling", "sport", "technology"
]

# first names
first_names = [
    "Liam", "Noah", "Oliver", "Elijah", "James",
    "William", "Benjamin", "Lucas", "Henry", "Alexander",
    "Mason", "Michael", "Ethan", "Daniel", "Jacob",
    "Logan", "Jackson", "Levi", "Sebastian", "Mateo",
    "Goodluck", "Iheoma", "Dinta", "Akudo", "Ozioma"
]

# last names
last_names = [
    "Smith", "Johnson", "Williams", "Brown", "Jones",
    "Garcia", "Miller", "Davis", "Rodriguez", "Martinez",
    "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson",
    "Thomas", "Taylor", "Moore", "Jackson", "Martin",
    "Ogbuehi", "Aniekwensi", "Balogun", "Adedeji", "Danjuma"
]

# initialize system
subprocess.run(f"./valid_sysconfig_init.sh {random.randint(10, 10000) * 100}", shell=True)
print()


for user_name in sys.argv[1:]:

    # Random names
    first_name = random.choice(first_names)
    last_name = random.choice(last_names)

    # Random skillFocus length (0–10)
    k = random.randint(0, 10)
    skill_focus = random.sample(skills, k)

    body = json.dumps({
        "userName": user_name,
        "firstName": first_name,
        "lastName": last_name,
        "skillFocus": skill_focus
    })

    cmd = f"""curl -X POST http://localhost:8080/api/v1/player/create \
-H "Content-Type: application/json" \
-d '{body}'"""

    subprocess.run(cmd, shell=True)
    print()

