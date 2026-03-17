package nkemrocks.anyteam_v1.util;

import java.util.Map;

public class ConstraintRelatedStrings {

    /* lowercased short names */
    public static final String UK__sessions__session_name = "uk_sessions_sname";
    public static final String UK__players__user_name = "uk_players_uname";
    public static final String UK__skills__skill_name = "uk_skills_sname";
    public static final String UK__skill_ratings__player_id__skill_id = "uk_skrat_pid_skid";
    public static final String UK__skill_selections__session_id__skill_id = "uk_sksel_sid_skid";
    public static final String UK__results__team_id__session_id = "uk_results_tid_sid";
    public static final String UK__teams__team_name = "uk_teams_tname";
    public static final String UK__junction_result_session_player__player_id__session_id =
            "uk_j_rsp_pid_sid";

    public static final Map<String, String> map = Map.of(
            UK__sessions__session_name, "Session name already exists!",
            UK__players__user_name, "Player username already exists!",
            UK__skills__skill_name, "Skill name already exists!",
            UK__skill_ratings__player_id__skill_id, "Player-Skill rating duplicates not allowed!",
            UK__skill_selections__session_id__skill_id, "A requested Session-Skill selection is already selected!",
            UK__results__team_id__session_id, "Team-Session result entry already exists, replays are forbidden!",
            UK__teams__team_name, "Team name already exists!",
            UK__junction_result_session_player__player_id__session_id, "Requested Player(s) have already been registered with another team for this session!"
    );
}
