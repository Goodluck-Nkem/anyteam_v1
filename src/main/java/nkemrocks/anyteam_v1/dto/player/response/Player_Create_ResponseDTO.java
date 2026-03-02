package nkemrocks.anyteam_v1.dto.player.response;

import java.time.Instant;
import java.util.UUID;

public record Player_Create_ResponseDTO(
        UUID id,
        String playerName,
        String firstName,
        String lastName,
        Instant dateCreated
) {
}
