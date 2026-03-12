package nkemrocks.anyteam_v1.dto.skillRating.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;
import java.util.Map;

public record SkillRating_ResponseDTO(
        String playerUserName,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd, HH:mm:ss", timezone = "UTC")
        Instant datePlayerCreated,

        String sessionName,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd, HH:mm:ss", timezone = "UTC")
        Instant dateSessionCreated,

        Map<String,Integer> sessionSkillRatings,
        int sessionAverageRating
) {
}
