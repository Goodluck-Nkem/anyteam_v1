package nkemrocks.anyteam_v1.dto.stats.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;

public record Stats_ResponseDTO(
        String teamName,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd, HH:mm:ss", timezone = "UTC")
        Instant dateTeamCreated,

        String sessionName,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd, HH:mm:ss", timezone = "UTC")
        Instant dateSessionCreated,

        int sessionTeamScore,
        int sessionTeamEntropy,
        int sessionTeamRating,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd, HH:mm:ss", timezone = "UTC")
        Instant dateStatsCreated
) {
}
