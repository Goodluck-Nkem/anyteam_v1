package nkemrocks.anyteam_v1.mapper;

import nkemrocks.anyteam_v1.dto.player.response.Player_Result_ResponseDTO;
import nkemrocks.anyteam_v1.dto.team.response.Team_Result_ResponseDTO;
import nkemrocks.anyteam_v1.projection.Result_Details_Projection;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Result_Mapper {
    public Team_Result_ResponseDTO toTeamResult_ResponseDTO(
            List<Result_Details_Projection> resultDetails
    ) {
        List<String> playerNames = resultDetails.stream()
                .map(Result_Details_Projection::getPlayerName)
                .toList();

        Result_Details_Projection first = resultDetails.getFirst();
        return new Team_Result_ResponseDTO(
                first.getSessionName(),
                first.getTeamName(),
                playerNames,
                first.getDateResultCreated(),
                first.getScore(),
                first.getEntropy()
        );
    }

    public Player_Result_ResponseDTO toPlayerResult_ResponseDTO(Result_Details_Projection detail) {
        return new Player_Result_ResponseDTO(
                detail.getSessionName(),
                detail.getTeamName(),
                detail.getPlayerName(),
                detail.getDateResultCreated(),
                detail.getScore(),
                detail.getEntropy()
        );
    }
}
