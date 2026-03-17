package nkemrocks.anyteam_v1.dto.player.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;

public record Player_Result_ResponseDTO(

        String sessionName,
        String teamName,
        String userName,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd, HH:mm:ss", timezone = "UTC")
        Instant dateResultCreated,
        int score,
        int entropy

) {
}
