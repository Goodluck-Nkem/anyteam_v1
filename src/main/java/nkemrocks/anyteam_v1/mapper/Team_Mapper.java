package nkemrocks.anyteam_v1.mapper;

import nkemrocks.anyteam_v1.dto.team.response.Team_Fetch_ResponseDTO;
import nkemrocks.anyteam_v1.entity.Team_Entity;
import org.springframework.stereotype.Component;

@Component
public class Team_Mapper {

    public Team_Fetch_ResponseDTO toFetch_ResponseDTO(Team_Entity team) {
        return new Team_Fetch_ResponseDTO(
                team.getId(),
                team.getTeamName(),
                team.getRating(),
                team.getDateCreated()
        );
    }
}
