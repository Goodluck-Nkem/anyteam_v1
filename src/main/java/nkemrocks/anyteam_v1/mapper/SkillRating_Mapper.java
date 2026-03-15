package nkemrocks.anyteam_v1.mapper;

import nkemrocks.anyteam_v1.dto.skillRating.response.SkillRating_ResponseDTO;
import nkemrocks.anyteam_v1.projection.Player_Details_Projection;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SkillRating_Mapper {
    public SkillRating_ResponseDTO toResponseDTO(
            List<Player_Details_Projection> playerDetails
    ) {
        Map<String, Integer> skillRatingsMap = new HashMap<>();
        int aggregate = 0;
        for (Player_Details_Projection detail : playerDetails) {
            skillRatingsMap.put(detail.getSkillName(), detail.getSkillRating());
            aggregate += detail.getSkillRating();
        }

        Player_Details_Projection first = playerDetails.getFirst();
        return new SkillRating_ResponseDTO(
                first.getUserName(),
                first.getDateCreated(),
                skillRatingsMap,
                aggregate / skillRatingsMap.size()
        );
    }
}
