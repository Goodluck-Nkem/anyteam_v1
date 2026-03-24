package nkemrocks.anyteam_v1.dto.player.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record Player_Fetch_ResponseDTO(
        UUID playerId,
        String playerName,
        String firstName,
        String lastName,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd, HH:mm:ss", timezone = "UTC")
        Instant dateCreated,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd, HH:mm:ss", timezone = "UTC")
        Instant dateUpdated,

        Map<String, Integer> skillRatings,
        Integer averageRating
) {

}
