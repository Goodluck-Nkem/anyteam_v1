package nkemrocks.anyteam_v1.dto.session.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record Session_Update_RequestDTO (
        @NotNull(message = ERROR_NULL_SESSION)
        UUID sessionId,

        @NotNull(message = ERROR_NULL_TTL)
        Long ttl
) {
    private static final String ERROR_NULL_SESSION = "Session ID must be provided!";
    private static final String ERROR_NULL_TTL = "Time-To-Live (ttl) field cannot be null!";
}
