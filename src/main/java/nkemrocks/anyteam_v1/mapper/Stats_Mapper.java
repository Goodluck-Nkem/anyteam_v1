package nkemrocks.anyteam_v1.mapper;

import nkemrocks.anyteam_v1.dto.stats.response.Stats_ResponseDTO;
import nkemrocks.anyteam_v1.projection.Team_Details_Projection;
import org.springframework.stereotype.Component;

@Component
public class Stats_Mapper {
    public Stats_ResponseDTO toResponseDTO(
            Team_Details_Projection teamDetails
    ) {
        return new Stats_ResponseDTO(
                teamDetails.getTeamName(),
                teamDetails.getDateCreated(),
                teamDetails.getSessionName(),
                teamDetails.getDateSessionCreated(),
                teamDetails.getTeamScore(),
                teamDetails.getEntropy(),
                teamDetails.getTeamRating(),
                teamDetails.getDateStatsCreated()
        );
    }
}
