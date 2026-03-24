package nkemrocks.anyteam_v1.dto.player.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record Player_Create_ResponseDTO(
        UUID playerId,
        String playerName,
        String firstName,
        String lastName,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd, HH:mm:ss", timezone = "UTC")
        Instant dateCreated,

        Map<String, Integer> initialSkillRatings,
        Integer averageRating
) {
}
