package nkemrocks.anyteam_v1.service;

import lombok.RequiredArgsConstructor;
import nkemrocks.anyteam_v1.dto.skillRating.response.SkillRating_ResponseDTO;
import nkemrocks.anyteam_v1.mapper.SkillRating_Mapper;
import nkemrocks.anyteam_v1.projection.Player_Details_Projection;
import nkemrocks.anyteam_v1.repository.SkillRating_Repository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkillRating_Service {

    private final SkillRating_Mapper skillRatingMapper;
    private final SkillRating_Repository skillRatingRepository;

    public List<SkillRating_ResponseDTO> getOnePlayer(UUID playerId) {
        List<Player_Details_Projection> playerDetails =
                skillRatingRepository.getPlayerDetailsByPlayerId(playerId);
        return Collections.singletonList(skillRatingMapper.toResponseDTO(playerDetails));
    }

    public List<SkillRating_ResponseDTO> getAllPlayers() {
        /* get all player details for this session */
        List<Player_Details_Projection> playerDetails =
                skillRatingRepository.getAllPlayerDetails();

        /* group by each player ID */
        Map<UUID, List<Player_Details_Projection>> detailsMap = playerDetails.stream()
                .collect(Collectors.groupingBy(Player_Details_Projection::getPlayerId));

        /* return response */
        return detailsMap.keySet().stream()
                .map(playerId -> skillRatingMapper.toResponseDTO(
                        detailsMap.getOrDefault(playerId, List.of())
                ))
                .toList();
    }

}
