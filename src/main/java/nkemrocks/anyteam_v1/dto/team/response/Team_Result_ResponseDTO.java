package nkemrocks.anyteam_v1.dto.team.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;
import java.util.List;

public record Team_Result_ResponseDTO(

        String sessionName,
        String teamName,
        List<String> playerNames,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd, HH:mm:ss", timezone = "UTC")
        Instant dateResultCreated,
        int score,
        int entropy

        ) {
}
