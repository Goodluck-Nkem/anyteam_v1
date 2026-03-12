package nkemrocks.anyteam_v1.mapper;

import nkemrocks.anyteam_v1.projection.Player_Details_Projection;
import nkemrocks.anyteam_v1.dto.player.response.Player_Fetch_ResponseDTO;
import nkemrocks.anyteam_v1.dto.player.response.Player_Update_ResponseDTO;
import nkemrocks.anyteam_v1.entity.Player_Entity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Player_Mapper {

    public Player_Fetch_ResponseDTO toFetch_ResponseDTO(
            List<Player_Details_Projection> playerDetails
    ) {
        Map<String, Integer> skillRatingsMap = new HashMap<>();
        int aggregate = 0;
        for (Player_Details_Projection detail : playerDetails) {
            skillRatingsMap.put(detail.getSkillName(), detail.getSkillRating());
            aggregate += detail.getSkillRating();
        }

        Player_Details_Projection first = playerDetails.getFirst();
        return new Player_Fetch_ResponseDTO(
                first.getPlayerId(),
                first.getUserName(),
                first.getFirstName(),
                first.getLastName(),
                first.getDateCreated(),
                first.getDateUpdated(),
                first.getSessionName(),
                first.getDateSessionCreated(),
                skillRatingsMap,
                aggregate / skillRatingsMap.size()
        );
    }

    public Player_Update_ResponseDTO toUpdate_ResponseDTO(Player_Entity player) {
        return new Player_Update_ResponseDTO(
                player.getId(),
                player.getUserName(),
                player.getFirstName(),
                player.getLastName(),
                player.getDateCreated(),
                player.getDateUpdated()
        );
    }
}
