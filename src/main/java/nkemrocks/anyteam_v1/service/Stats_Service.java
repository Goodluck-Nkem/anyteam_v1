package nkemrocks.anyteam_v1.service;

import lombok.RequiredArgsConstructor;
import nkemrocks.anyteam_v1.dto.stats.response.Stats_ResponseDTO;
import nkemrocks.anyteam_v1.mapper.Stats_Mapper;
import nkemrocks.anyteam_v1.projection.Team_Details_Projection;
import nkemrocks.anyteam_v1.repository.Stats_Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class Stats_Service {

    private final Stats_Mapper statsMapper;
    private final Stats_Repository statsRepository;

    public List<Stats_ResponseDTO> getInfo_OneTeamSessionPair(UUID teamId, UUID sessionId) {
        List<Team_Details_Projection> teamDetails =
                statsRepository.getTeamDetailsByTeamIdAndSessionId(teamId, sessionId);
        return teamDetails.stream()
                .map(statsMapper::toResponseDTO)
                .toList();
    }

    public List<Stats_ResponseDTO> getInfo_OneTeamAllSessions(UUID teamId) {
        List<Team_Details_Projection> teamDetails =
                statsRepository.getTeamDetailsByTeamId(teamId);
        return teamDetails.stream()
                .map(statsMapper::toResponseDTO)
                .toList();
    }

    public List<Stats_ResponseDTO> getInfo_AllTeamsOneSession(UUID sessionId) {
        List<Team_Details_Projection> teamDetails =
                statsRepository.getTeamDetailsBySessionId(sessionId);
        return teamDetails.stream()
                .map(statsMapper::toResponseDTO)
                .toList();
    }

    public List<Stats_ResponseDTO> getAllTeamsAllSessions() {
        List<Team_Details_Projection> teamDetails =
                statsRepository.getAllTeamDetails();
        return teamDetails.stream()
                .map(statsMapper::toResponseDTO)
                .toList();
    }

}
