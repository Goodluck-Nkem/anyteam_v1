package nkemrocks.anyteam_v1.dto.team.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;
import java.util.List;

public record Team_Play_ResponseDTO(
        String sessionName,
        String TeamName,
        int score,
        int entropy,
        int oldRating,
        int newRating,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd, HH:mm:ss", timezone = "UTC")
        Instant creationDate,
        List<Team_PlayerSummary_DTO> playerSummaries
) {
}
