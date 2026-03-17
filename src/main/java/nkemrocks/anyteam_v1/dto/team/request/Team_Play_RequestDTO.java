package nkemrocks.anyteam_v1.dto.team.request;

import nkemrocks.anyteam_v1.exception.PolicyException;
import org.springframework.http.HttpStatus;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static nkemrocks.anyteam_v1.util.GlobalUtil.trimAndLower;

public record Team_Play_RequestDTO(
        UUID teamId,
        String teamName,

        UUID sessionId,
        String sessionName,

        Set<UUID> playerIds,
        Set<String> userNames
) {
    private final static String ERROR_NULL_TEAM = "Either Team ID or Name must be provided, both cannot be null or blank!";
    private final static String ERROR_NULL_SESSION = "Either Session ID or Name must be provided, both cannot be null or blank!";
    private final static String ERROR_NULL_ENTRY_PLAYER_IDS = "No entry in the player ID list can be null when list is provided!";
    private final static String ERROR_BLANK_ENTRY_USERNAMES = "No entry in the fallback username list can be null or blank when list is provided!";
    private final static String ERROR_NULL_PLAYER_LIST = "Either player ID list or username list must be provided, both cannot be null!";
    private final static String ERROR_SIZE_PLAYER_LIST = "Number of players in a team must be inclusively between 2 and 5.";

    public Team_Play_RequestDTO{

        if(teamId == null){
            teamName = trimAndLower(teamName);
            if(teamName == null || teamName.isBlank())
                throw new PolicyException(HttpStatus.BAD_REQUEST, ERROR_NULL_TEAM);
        }

        if(sessionId == null){
            sessionName = trimAndLower(sessionName);
            if(sessionName == null || sessionName.isBlank())
                throw new PolicyException(HttpStatus.BAD_REQUEST, ERROR_NULL_SESSION);
        }

        if(playerIds != null){
            for(UUID id : playerIds)
                if(id == null)
                    throw new PolicyException(HttpStatus.BAD_REQUEST, ERROR_NULL_ENTRY_PLAYER_IDS);
            if(playerIds.size() < 2 || playerIds.size() > 5)
                throw new PolicyException(HttpStatus.BAD_REQUEST, ERROR_SIZE_PLAYER_LIST);
        }
        else {
            if(userNames == null)
                throw new PolicyException(HttpStatus.BAD_REQUEST, ERROR_NULL_PLAYER_LIST);

            Set<String> newSet = new HashSet<>();
            for(String s: userNames){
                s = trimAndLower(s);
                if(s == null || s.isBlank())
                    throw new PolicyException(HttpStatus.BAD_REQUEST, ERROR_BLANK_ENTRY_USERNAMES);
                newSet.add(s);
            }
            if(newSet.size() < 2 || newSet.size() > 5)
                throw new PolicyException(HttpStatus.BAD_REQUEST, ERROR_SIZE_PLAYER_LIST);
            userNames = newSet;
        }

    }
}
