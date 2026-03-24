package nkemrocks.anyteam_v1.util;

import nkemrocks.anyteam_v1.dto.team.request.Team_Play_RequestDTO;
import nkemrocks.anyteam_v1.exception.PolicyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import java.util.*;

import static nkemrocks.anyteam_v1.util.GlobalUtil.trimAndLower;

public class ValidationUtil {

    public static Pageable validatePageableSort(
            Pageable pageable,
            Map<String, String> allowedFieldsMap
    ) {
        List<Sort.Order> newOrders = new ArrayList<>();

        for (Sort.Order order : pageable.getSort()) {
            String mappedProperty = allowedFieldsMap.get(order.getProperty());
            if (mappedProperty == null)
                throw new PolicyException(
                        HttpStatus.BAD_REQUEST, """
                        Sorting not allowed on the '%s' field --- (Allowed => [%s])"""
                        .formatted(order.getProperty(), String.join(", ", allowedFieldsMap.keySet()))
                );
            newOrders.add(new Sort.Order(order.getDirection(), mappedProperty));
        }

        return PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(newOrders)
        );
    }

    public static Team_Play_RequestDTO validatePlayRequest(Team_Play_RequestDTO in) {

        final String ERROR_NULL_SESSION = "Either Session ID or Name must be provided, both cannot be null or blank!";
        final String ERROR_NULL_ENTRY_PLAYER_IDS = "No entry in the player ID list can be null when list is provided!";
        final String ERROR_BLANK_ENTRY_PLAYER_NAMES = "No entry in the fallback playerName list can be null or blank when list is provided!";
        final String ERROR_NULL_PLAYER_LIST = "Either playerID list or playerName list must be provided, both cannot be null!";
        final String ERROR_SIZE_PLAYER_LIST = "Number of players in a team must be inclusively between 2 and 5.";

        UUID outSessionId = null;
        Set<UUID> outPlayerIds = null;
        String outSessionName = null;
        Set<String> outPlayerNames = null;

        if (in.sessionId() == null) {
            outSessionName = trimAndLower(in.sessionName());
            if (outSessionName == null || outSessionName.isBlank())
                throw new PolicyException(HttpStatus.BAD_REQUEST, ERROR_NULL_SESSION);
        } else outSessionId = in.sessionId();

        if (in.playerIds() != null) {
            for (UUID id : in.playerIds())
                if (id == null)
                    throw new PolicyException(HttpStatus.BAD_REQUEST, ERROR_NULL_ENTRY_PLAYER_IDS);

            outPlayerIds = new LinkedHashSet<>(in.playerIds());
            if (outPlayerIds.size() < 2 || outPlayerIds.size() > 5)
                throw new PolicyException(HttpStatus.BAD_REQUEST, ERROR_SIZE_PLAYER_LIST);
        } else {
            if (in.playerNames() == null)
                throw new PolicyException(HttpStatus.BAD_REQUEST, ERROR_NULL_PLAYER_LIST);

            outPlayerNames = new LinkedHashSet<>();
            for (String s : in.playerNames()) {
                s = trimAndLower(s);
                if (s == null || s.isBlank())
                    throw new PolicyException(HttpStatus.BAD_REQUEST, ERROR_BLANK_ENTRY_PLAYER_NAMES);
                outPlayerNames.add(s);
            }
            if (outPlayerNames.size() < 2 || outPlayerNames.size() > 5)
                throw new PolicyException(HttpStatus.BAD_REQUEST, ERROR_SIZE_PLAYER_LIST);
        }

        return new Team_Play_RequestDTO(
                outSessionId,
                outSessionName,
                outPlayerIds,
                outPlayerNames
        );
    }
}
