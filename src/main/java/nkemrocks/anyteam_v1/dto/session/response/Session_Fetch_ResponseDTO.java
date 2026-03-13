package nkemrocks.anyteam_v1.dto.session.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record Session_Fetch_ResponseDTO(
        UUID sessionId,
        String sessionName,
        Integer ttl,
        List<String> requirements,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd, HH:mm:ss", timezone = "UTC")
        Instant dateCreated,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd, HH:mm:ss", timezone = "UTC")
        Instant dateUpdated
) {
}
