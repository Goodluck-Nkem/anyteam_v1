package nkemrocks.anyteam_v1.dto.session.response;

import java.time.Instant;
import java.util.UUID;

public record Session_Create_ResponseDTO(
        UUID id,
        String sessionName,
        Integer ttl,
        Instant dateCreated
) {
}
