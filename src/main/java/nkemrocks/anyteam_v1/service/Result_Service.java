package nkemrocks.anyteam_v1.service;

import lombok.RequiredArgsConstructor;
import nkemrocks.anyteam_v1.dto.player.response.Player_Result_ResponseDTO;
import nkemrocks.anyteam_v1.dto.team.response.Team_Result_ResponseDTO;
import nkemrocks.anyteam_v1.mapper.Result_Mapper;
import nkemrocks.anyteam_v1.projection.Result_Details_Projection;
import nkemrocks.anyteam_v1.repository.Result_Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Result_Service {

    private final Result_Mapper resultMapper;
    private final Result_Repository resultRepository;

    /* -- TEAM-FOCUSED -- */

    public Slice<Team_Result_ResponseDTO> getResults_OneTeamSessionPair(
            UUID teamId,
            UUID sessionId,
            Pageable pageable
    ) {
        /* get one projection as a slice */
        Slice<Result_Details_Projection> resultDetailsSlice =
                resultRepository.getResultDetailsByTeamIdAndSessionId(
                        teamId,
                        sessionId,
                        pageable
                );

        /* return response slice */
        return new SliceImpl<>(
                Collections.singletonList(resultMapper.toTeamResult_ResponseDTO(
                        resultDetailsSlice.getContent()
                )),
                pageable,
                resultDetailsSlice.hasNext()
        );
    }

    public Slice<Team_Result_ResponseDTO> getResults_OneTeamSessionPair(
            String teamName,
            String sessionName,
            Pageable pageable
    ) {
        /* get one projection as a slice */
        Slice<Result_Details_Projection> resultDetailsSlice =
                resultRepository.getResultDetailsByTeamNameAndSessionName(
                        teamName,
                        sessionName,
                        pageable
                );

        /* return response slice */
        return new SliceImpl<>(
                Collections.singletonList(resultMapper.toTeamResult_ResponseDTO(
                        resultDetailsSlice.getContent()
                )),
                pageable,
                resultDetailsSlice.hasNext()
        );
    }

    private Slice<Team_Result_ResponseDTO> getResultResponseSliceForTeams(
            Slice<Long> resultIdsSlice,
            Pageable pageable
    ) {

        List<Long> resultIds = resultIdsSlice.getContent();
        if (resultIds.isEmpty())
            return new SliceImpl<>(List.of(), pageable, false);

        /* Get the result details for the specific session */
        List<Result_Details_Projection> resultsDetails = resultRepository.getResultDetailsByManyIds(
                resultIds
        );

        /* group by result ID */
        Map<Long, List<Result_Details_Projection>> detailsMap = resultsDetails.stream()
                .collect(Collectors.groupingBy(
                        Result_Details_Projection::getResultId,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        /* return response slice */
        return new SliceImpl<>(
                resultIds.stream()
                        .map(resultId -> resultMapper.toTeamResult_ResponseDTO(
                                detailsMap.get(resultId)
                        ))
                        .toList(),
                pageable,
                resultIdsSlice.hasNext()
        );
    }

    public Slice<Team_Result_ResponseDTO> getResults_OneTeamAllSessions(
            UUID teamId,
            Pageable pageable
    ) {
        /* return the response slice */
        return getResultResponseSliceForTeams(
                resultRepository.getIds_findAllByTeamId(teamId, pageable),
                pageable
        );
    }

    public Slice<Team_Result_ResponseDTO> getResults_OneTeamAllSessions(
            String teamName,
            Pageable pageable
    ) {
        /* return the response slice */
        return getResultResponseSliceForTeams(
                resultRepository.getIds_findAllByTeamName(teamName, pageable),
                pageable
        );
    }

    public Slice<Team_Result_ResponseDTO> getResults_AllTeamsOneSession(
            UUID sessionId,
            Pageable pageable
    ) {
        /* return the response slice */
        return getResultResponseSliceForTeams(
                resultRepository.getIds_findAllBySessionId(sessionId, pageable),
                pageable
        );
    }

    public Slice<Team_Result_ResponseDTO> getResults_AllTeamsOneSession(
            String sessionName,
            Pageable pageable
    ) {
        /* return the response slice */
        return getResultResponseSliceForTeams(
                resultRepository.getIds_findAllBySessionName(sessionName, pageable),
                pageable
        );
    }

    public Slice<Team_Result_ResponseDTO> getResults_AllTeamsAllSessions(
            Pageable pageable
    ) {
        /* return the response slice */
        return getResultResponseSliceForTeams(
                resultRepository.getIds_findAll(pageable),
                pageable
        );
    }


    /* -- PLAYER-FOCUSED -- */

    public Slice<Player_Result_ResponseDTO> getResults_OnePlayerSessionPair(
            UUID playerId,
            UUID sessionId,
            Pageable pageable
    ) {
        Slice<Result_Details_Projection> resultDetailsSlice =
                resultRepository.getResultDetailsByPlayerIdAndSessionId(
                        playerId,
                        sessionId,
                        pageable
                );
        return new SliceImpl<>(
                resultDetailsSlice.stream()
                        .map(resultMapper::toPlayerResult_ResponseDTO)
                        .toList(),
                pageable,
                resultDetailsSlice.hasNext()
        );
    }

    public Slice<Player_Result_ResponseDTO> getResults_OnePlayerSessionPair(
            String playerName,
            String sessionName,
            Pageable pageable
    ) {
        Slice<Result_Details_Projection> resultDetailsSlice =
                resultRepository.getResultDetailsByPlayerNameAndSessionName(
                        playerName,
                        sessionName,
                        pageable
                );
        return new SliceImpl<>(
                resultDetailsSlice.stream()
                        .map(resultMapper::toPlayerResult_ResponseDTO)
                        .toList(),
                pageable,
                resultDetailsSlice.hasNext()
        );
    }

    public Slice<Player_Result_ResponseDTO> getResults_OnePlayerAllSessions(
            UUID playerId,
            Pageable pageable
    ) {
        Slice<Result_Details_Projection> resultDetailsSlice =
                resultRepository.getResultDetailsByPlayerId(playerId, pageable);
        return new SliceImpl<>(
                resultDetailsSlice.stream()
                        .map(resultMapper::toPlayerResult_ResponseDTO)
                        .toList(),
                pageable,
                resultDetailsSlice.hasNext()
        );
    }

    public Slice<Player_Result_ResponseDTO> getResults_OnePlayerAllSessions(
            String playerName,
            Pageable pageable
    ) {
        Slice<Result_Details_Projection> resultDetailsSlice =
                resultRepository.getResultDetailsByPlayerName(playerName, pageable);
        return new SliceImpl<>(
                resultDetailsSlice.stream()
                        .map(resultMapper::toPlayerResult_ResponseDTO)
                        .toList(),
                pageable,
                resultDetailsSlice.hasNext()
        );
    }

    public Slice<Player_Result_ResponseDTO> getResults_AllPlayersOneSession(
            UUID sessionId,
            Pageable pageable
    ) {
        Slice<Result_Details_Projection> resultDetailsSlice =
                resultRepository.getResultDetailsBySessionId(sessionId, pageable);
        return new SliceImpl<>(
                resultDetailsSlice.stream()
                        .map(resultMapper::toPlayerResult_ResponseDTO)
                        .toList(),
                pageable,
                resultDetailsSlice.hasNext()
        );
    }

    public Slice<Player_Result_ResponseDTO> getResults_AllPlayersOneSession(
            String sessionName,
            Pageable pageable
    ) {
        Slice<Result_Details_Projection> resultDetailsSlice =
                resultRepository.getResultDetailsBySessionName(sessionName, pageable);
        return new SliceImpl<>(
                resultDetailsSlice.stream()
                        .map(resultMapper::toPlayerResult_ResponseDTO)
                        .toList(),
                pageable,
                resultDetailsSlice.hasNext()
        );
    }

    public Slice<Player_Result_ResponseDTO> getResults_AllPlayersAllSessions(
            Pageable pageable
    ) {
        Slice<Result_Details_Projection> resultDetailsSlice =
                resultRepository.getAllResultDetails(pageable);
        return new SliceImpl<>(
                resultDetailsSlice.stream()
                        .map(resultMapper::toPlayerResult_ResponseDTO)
                        .toList(),
                pageable,
                resultDetailsSlice.hasNext()
        );
    }

}
