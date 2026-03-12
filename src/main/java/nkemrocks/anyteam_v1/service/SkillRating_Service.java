package nkemrocks.anyteam_v1.service;

import lombok.RequiredArgsConstructor;
import nkemrocks.anyteam_v1.dto.skillRating.response.SkillRating_ResponseDTO;
import nkemrocks.anyteam_v1.mapper.SkillRating_Mapper;
import nkemrocks.anyteam_v1.projection.Player_Details_Projection;
import nkemrocks.anyteam_v1.repository.Skill_Repository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkillRating_Service {

    private final SkillRating_Mapper skillRatingMapper;
    private final Skill_Repository skillRepository;

    public List<SkillRating_ResponseDTO> getInfo_OnePlayerSessionPair(UUID playerId, UUID sessionId) {
        List<Player_Details_Projection> playerDetails =
                skillRepository.getPlayerDetailsByPlayerIdAndSessionId(playerId, sessionId);
        return Collections.singletonList(skillRatingMapper.toResponseDTO(playerDetails));
    }

    public List<SkillRating_ResponseDTO> getInfo_OnePlayerAllSessions(UUID playerId) {
        /* get this player's details for every session */
        List<Player_Details_Projection> playerDetails =
                skillRepository.getPlayerDetailsByPlayerId(playerId);

        /* group by session name */
        Map<String, List<Player_Details_Projection>> detailsMap = playerDetails.stream()
                .collect(Collectors.groupingBy(Player_Details_Projection::getSessionName));

        /* return response */
        return detailsMap.keySet().stream()
                .map(sessionName -> skillRatingMapper.toResponseDTO(
                        detailsMap.getOrDefault(sessionName, List.of())
                ))
                .toList();
    }

    public List<SkillRating_ResponseDTO> getInfo_AllPlayersOneSession(UUID sessionId) {
        /* get all player details for this session */
        List<Player_Details_Projection> playerDetails =
                skillRepository.getPlayerDetailsBySessionId(sessionId);

        /* group by each player userName */
        Map<String, List<Player_Details_Projection>> detailsMap = playerDetails.stream()
                .collect(Collectors.groupingBy(Player_Details_Projection::getUserName));

        /* return response */
        return detailsMap.keySet().stream()
                .map(userName -> skillRatingMapper.toResponseDTO(
                        detailsMap.getOrDefault(userName, List.of())
                ))
                .toList();
    }

    public List<SkillRating_ResponseDTO> getAllPlayersAllSessions() {
        /* get all player details for all sessions */
        List<Player_Details_Projection> playerDetails =
                skillRepository.getAllPlayerDetails();

        /* first group by sessionName */
        Map<String, List<Player_Details_Projection>> detailsMappedBySession = playerDetails.stream()
                .collect(Collectors.groupingBy(Player_Details_Projection::getSessionName));

        List<SkillRating_ResponseDTO> response = new ArrayList<>();
        for (String sessionName : detailsMappedBySession.keySet()) {
            /* then group sessionGroup by player userName */
            Map<String, List<Player_Details_Projection>> innerMappedByPlayer = detailsMappedBySession.get(sessionName)
                    .stream()
                    .collect(Collectors.groupingBy(Player_Details_Projection::getUserName));

            /* populate list */
            for (String userName : innerMappedByPlayer.keySet())
                response.add(skillRatingMapper.toResponseDTO(innerMappedByPlayer.get(userName)));
        }

        return response;
    }
}
