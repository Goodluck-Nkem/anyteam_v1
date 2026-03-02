package nkemrocks.anyteam_v1.dto.player.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import nkemrocks.anyteam_v1.dto.playerStats.response.PlayerStats_Fetch_ResponseDTO;

import java.time.Instant;
import java.util.UUID;

public record Player_Fetch_ResponseDTO(
        UUID id,
        String playerName,
        String firstName,
        String lastName,
        PlayerStats_Fetch_ResponseDTO lastPlayerStats,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd, HH:mm:ss", timezone = "UTC")
        Instant dateCreated,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd, HH:mm:ss", timezone = "UTC")
        Instant dateUpdated) {

}
