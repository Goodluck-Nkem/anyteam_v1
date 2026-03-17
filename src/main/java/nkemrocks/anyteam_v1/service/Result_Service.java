package nkemrocks.anyteam_v1.service;

import lombok.RequiredArgsConstructor;
import nkemrocks.anyteam_v1.dto.player.response.Player_Result_ResponseDTO;
import nkemrocks.anyteam_v1.dto.team.response.Team_Result_ResponseDTO;
import nkemrocks.anyteam_v1.mapper.Result_Mapper;
import nkemrocks.anyteam_v1.projection.Result_Details_Projection;
import nkemrocks.anyteam_v1.repository.Result_Repository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Result_Service {

    private final Result_Mapper resultMapper;
    private final Result_Repository resultRepository;

    /* -- TEAM-FOCUSED -- */

    public List<Team_Result_ResponseDTO> getResults_OneTeamSessionPair(UUID teamId, UUID sessionId) {
        List<Result_Details_Projection> resultDetails =
                resultRepository.getResultDetailsByTeamIdAndSessionId(teamId, sessionId);
        return Collections.singletonList(resultMapper.toTeamResult_ResponseDTO(resultDetails));
    }

    public List<Team_Result_ResponseDTO> getResults_OneTeamAllSessions(UUID teamId) {
        /* Get all result details for a specific team */
        List<Result_Details_Projection> resultsDetails = resultRepository.getResultDetailsByManyIds(
                resultRepository.getIds_findAllByTeamId(teamId)
        );

        /* group by resultID (maintain order) */
        Map<Long, List<Result_Details_Projection>> detailsMap = resultsDetails.stream()
                .collect(Collectors.groupingBy(
                        Result_Details_Projection::getResultId,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        /* return response list */
        return detailsMap.keySet().stream()
                .map(resultId -> resultMapper.toTeamResult_ResponseDTO(
                        detailsMap.getOrDefault(resultId, List.of())
                ))
                .toList();
    }

    public List<Team_Result_ResponseDTO> getResults_AllTeamsOneSession(UUID sessionId) {
        /* Get all result details for a specific session */
        List<Result_Details_Projection> resultsDetails = resultRepository.getResultDetailsByManyIds(
                resultRepository.getIds_findAllBySessionId(sessionId)
        );

        /* group by resultID (maintain order) */
        Map<Long, List<Result_Details_Projection>> detailsMap = resultsDetails.stream()
                .collect(Collectors.groupingBy(
                        Result_Details_Projection::getResultId,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        /* return response list */
        return detailsMap.keySet().stream()
                .map(resultId -> resultMapper.toTeamResult_ResponseDTO(
                        detailsMap.getOrDefault(resultId, List.of())
                ))
                .toList();
    }

    public List<Team_Result_ResponseDTO> getResults_AllTeamsAllSessions() {
        /* Get all result details */
        List<Result_Details_Projection> resultsDetails = resultRepository.getResultDetailsByManyIds(
                resultRepository.getIds_findAll()
        );

        /* group by resultID (maintain order) */
        Map<Long, List<Result_Details_Projection>> detailsMap = resultsDetails.stream()
                .collect(Collectors.groupingBy(
                        Result_Details_Projection::getResultId,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        /* return response list */
        return detailsMap.keySet().stream()
                .map(resultId -> resultMapper.toTeamResult_ResponseDTO(
                        detailsMap.getOrDefault(resultId, List.of())
                ))
                .toList();
    }


    /* -- PLAYER-FOCUSED -- */

    public List<Player_Result_ResponseDTO> getResults_OnePlayerSessionPair(UUID playerId, UUID sessionId) {
        List<Result_Details_Projection> resultDetails =
                resultRepository.getResultDetailsByPlayerIdAndSessionId(playerId, sessionId);
        return resultDetails.stream()
                .map(resultMapper::toPlayerResult_ResponseDTO)
                .toList();
    }

    public List<Player_Result_ResponseDTO> getResults_OnePlayerAllSessions(UUID playerId) {
        List<Result_Details_Projection> resultDetails =
                resultRepository.getResultDetailsByPlayerId(playerId);
        return resultDetails.stream()
                .map(resultMapper::toPlayerResult_ResponseDTO)
                .toList();
    }

    public List<Player_Result_ResponseDTO> getResults_AllPlayersOneSession(UUID sessionId) {
        List<Result_Details_Projection> resultDetails =
                resultRepository.getResultDetailsBySessionId(sessionId);
        return resultDetails.stream()
                .map(resultMapper::toPlayerResult_ResponseDTO)
                .toList();
    }

    public List<Player_Result_ResponseDTO> getResults_AllPlayersAllSessions() {
        List<Result_Details_Projection> resultDetails =
                resultRepository.getAllResultDetails();
        return resultDetails.stream()
                .map(resultMapper::toPlayerResult_ResponseDTO)
                .toList();
    }

}
