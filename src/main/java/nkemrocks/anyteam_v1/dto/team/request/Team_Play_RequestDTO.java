package nkemrocks.anyteam_v1.dto.team.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;
import java.util.UUID;

public record Team_Play_RequestDTO(
        @NotNull(message = ERROR_NULL_TEAM)
        UUID teamId,

        @NotNull(message = ERROR_NULL_SESSION)
        UUID sessionId,

        @NotNull(message = ERROR_NULL_PLAYER_LIST)
        @Size(min = 2, max = 5, message = ERROR_SIZE_PLAYER_LIST)
        Set<@NotNull(message = ERROR_NULL_PLAYER) UUID> playerIds
) {
    private final static String ERROR_NULL_TEAM = "Team ID must be provided and cannot be null!";
    private final static String ERROR_NULL_SESSION = "Session ID must be provided and cannot be null!";
    private final static String ERROR_NULL_PLAYER = "No player ID entry in the list can be null!";
    private final static String ERROR_NULL_PLAYER_LIST = "The player ID list must be provided and cannot be null!";
    private final static String ERROR_SIZE_PLAYER_LIST = "Number of players in a team must be inclusively between 2 and 5.";
}
