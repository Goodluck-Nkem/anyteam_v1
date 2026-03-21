package nkemrocks.anyteam_v1.util;

import nkemrocks.anyteam_v1.dto.team.request.Team_Play_RequestDTO;
import nkemrocks.anyteam_v1.exception.PolicyException;
import org.springframework.http.HttpStatus;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import static nkemrocks.anyteam_v1.util.GlobalUtil.trimAndLower;

public class ValidationUtil {
    
    public static Team_Play_RequestDTO validatePlayRequest(Team_Play_RequestDTO in){

        final String ERROR_NULL_SESSION = "Either Session ID or Name must be provided, both cannot be null or blank!";
        final String ERROR_NULL_ENTRY_PLAYER_IDS = "No entry in the player ID list can be null when list is provided!";
        final String ERROR_BLANK_ENTRY_USERNAMES = "No entry in the fallback username list can be null or blank when list is provided!";
        final String ERROR_NULL_PLAYER_LIST = "Either player ID list or username list must be provided, both cannot be null!";
        final String ERROR_SIZE_PLAYER_LIST = "Number of players in a team must be inclusively between 2 and 5.";

        UUID outSessionId = null;
        Set<UUID> outPlayerIds = null;
        String outSessionName = null;
        Set<String> outUserNames = null;

        if(in.sessionId() == null){
            outSessionName = trimAndLower(in.sessionName());
            if(outSessionName == null || outSessionName.isBlank())
                throw new PolicyException(HttpStatus.BAD_REQUEST, ERROR_NULL_SESSION);
        }
        else outSessionId = in.sessionId();

        if(in.playerIds() != null){
            for(UUID id : in.playerIds())
                if(id == null)
                    throw new PolicyException(HttpStatus.BAD_REQUEST, ERROR_NULL_ENTRY_PLAYER_IDS);

            outPlayerIds = new LinkedHashSet<>(in.playerIds());
            if(outPlayerIds.size() < 2 || outPlayerIds.size() > 5)
                throw new PolicyException(HttpStatus.BAD_REQUEST, ERROR_SIZE_PLAYER_LIST);
        }
        else {
            if(in.userNames() == null)
                throw new PolicyException(HttpStatus.BAD_REQUEST, ERROR_NULL_PLAYER_LIST);

            outUserNames = new LinkedHashSet<>();
            for(String s: in.userNames()){
                s = trimAndLower(s);
                if(s == null || s.isBlank())
                    throw new PolicyException(HttpStatus.BAD_REQUEST, ERROR_BLANK_ENTRY_USERNAMES);
                outUserNames.add(s);
            }
            if(outUserNames.size() < 2 || outUserNames.size() > 5)
                throw new PolicyException(HttpStatus.BAD_REQUEST, ERROR_SIZE_PLAYER_LIST);
        }

        return new Team_Play_RequestDTO(
                outSessionId,
                outSessionName,
                outPlayerIds,
                outUserNames
        );
    }
}
