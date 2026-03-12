package nkemrocks.anyteam_v1.dto.team.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;
import java.util.UUID;

public record Team_Fetch_ResponseDTO(
        UUID teamId,
        String teamName,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd, HH:mm:ss", timezone = "UTC")
        Instant dateCreated,

        int teamScore,
        int entropy,
        int teamRating,

        String sessionName,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd, HH:mm:ss", timezone = "UTC")
        Instant sessionDateCreated
) {
}
