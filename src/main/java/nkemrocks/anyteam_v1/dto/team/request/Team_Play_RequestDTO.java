package nkemrocks.anyteam_v1.dto.team.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

public record Team_Play_RequestDTO(

        UUID sessionId,
        String sessionName,

        @JsonDeserialize(as = LinkedHashSet.class)
        Set<UUID> playerIds,
        @JsonDeserialize(as = LinkedHashSet.class)
        Set<String> playerNames
) {
}
