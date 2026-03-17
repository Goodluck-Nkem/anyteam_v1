package nkemrocks.anyteam_v1.mapper;

import nkemrocks.anyteam_v1.dto.team.response.Team_Fetch_ResponseDTO;
import nkemrocks.anyteam_v1.projection.Team_Details_Projection;
import org.springframework.stereotype.Component;

@Component
public class Team_Mapper {

    public Team_Fetch_ResponseDTO toFetch_ResponseDTO(Team_Details_Projection teamDetails) {
        return new Team_Fetch_ResponseDTO(
                teamDetails.getTeamId(),
                teamDetails.getTeamName(),
                teamDetails.getDateCreated(),
                teamDetails.getTeamRating()
        );
    }

}
