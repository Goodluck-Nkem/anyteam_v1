### Overview
>Anyteam is a simulated team gameplay of player skills.

#### system config
The system a has a number of skills and system TTL configuration.

The config TTL allows a timeframe for creating new players, teams and gameplay sessions.

The system has an admin that can update the configuration and create sessions for gameplay.

#### sessions
Each session must have a sessionName and Time-To-Live(TTL) from when it was created.

Sessions are created/updated by the admin, once a session TTL has expired, teams can no longer play for that session.

Each session must select 4 skills, which teams can use as a hint to select players based on their skill ratings in order to get a good score.

Sessions are independent so that a team could re-use same players for another session if they so wish.

But a player/team can only feature once in a session.

#### players
Every player must have a unique playerName, a password, a firstName and a lastName.

On player creation, they get a number of points to share among the skills in the system.

A player is allowed to select a maximum of 10 skills to focus on, so that points are not evenly shared.

#### teams
Every team must have a unique teamName and a password.

#### gameplay
- A team must select 2-5 players to play in a session.
- A team cannot select a player whose average rating is higher than the team's rating.
- After a successful gameplay, a result is returned.
- This result is derived from each player skill ratings that the session hinted and an entropy value to introduce a bit of randomness.
- The score and entropy will be saved in the result, so that users can confirm why they had that score.
- Each of the 4 Player skill ratings involved for each player is updated by averaging with score.
- The team rating is also updated by averaging with score.

#### math
> <b><i>result</i></b> = rounded up AVG([each player skills for that session] + entropy) 
- <b><u><i>Example</i></u></b>:
- <b>session S1 skill selections:</b> <i>art, math, logic, biology</i>
- <b>player P1 relevant skill ratings:</b> <i>art=80, math=70, logic=60, biology=40</i>
- <b>player P2 relevant skill ratings:</b> <i>art=40, math=50, logic=50, biology=60</i>
- <b>entropy:</b> <i>30</i>
- <b>total:</b> (80+70+60+40)+(40+50+50+60)+30 = 480, n = 9
- <b>AVG:</b> Ceil(480/9) = Ceil(53.33) = 54

> <b><i>new skill rating</i></b> = Rounded nearest AVG = Round((old + score) / 2)

> <b><i>new team rating</i></b> = Rounded nearest AVG = Round((old + score) / 2)

> <b><i>player average rating</i></b> = Rounded nearest AVG([all player skills])

