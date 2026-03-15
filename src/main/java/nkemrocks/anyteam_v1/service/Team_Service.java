package nkemrocks.anyteam_v1.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import nkemrocks.anyteam_v1.dto.team.request.Team_Create_RequestDTO;
import nkemrocks.anyteam_v1.dto.team.request.Team_Play_RequestDTO;
import nkemrocks.anyteam_v1.dto.team.response.Team_Create_ResponseDTO;
import nkemrocks.anyteam_v1.dto.team.response.Team_Fetch_ResponseDTO;
import nkemrocks.anyteam_v1.dto.team.response.Team_Play_ResponseDTO;
import nkemrocks.anyteam_v1.dto.team.response.Team_PlayerSummary_DTO;
import nkemrocks.anyteam_v1.entity.*;
import nkemrocks.anyteam_v1.exception.*;
import nkemrocks.anyteam_v1.mapper.Team_Mapper;
import nkemrocks.anyteam_v1.projection.Player_Details_Projection;
import nkemrocks.anyteam_v1.projection.Team_Details_Projection;
import nkemrocks.anyteam_v1.repository.*;
import nkemrocks.anyteam_v1.util.GlobalUtil;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Team_Service {
    private final Team_Mapper teamMapper;
    private final Team_Repository teamRepository;
    private final Player_Repository playerRepository;
    private final Stats_Repository statsRepository;
    private final Session_Repository sessionRepository;
    private final SkillSelection_Repository skillSelectionRepository;
    private final SkillRating_Repository skillRatingRepository;
    private final Skill_Repository skillRepository;

    @Transactional
    public Team_Create_ResponseDTO createTeam(Team_Create_RequestDTO data) {
        /* STEPS:
         * 1a. Confirm the <Creation> session has been initialized
         * 1b. Confirm it is still open
         * 2.  Create and save the new team
         * 3.  Generate the default stats and save
         * 4.  Then return the response
         */

        /* 1a. Confirm the <config> session has been initialized */
        Session_Entity configSession = sessionRepository.findBySessionName(GlobalUtil.configSessionName)
                .orElseThrow(() -> new ServiceUnavailableException("System Configuration (sysConfig) session is not yet initialized!"));

        /* 1b. Confirm it is still open */
        if (Instant.now().isAfter(configSession.getDateCreated().plusSeconds(configSession.getTtl())))
            throw new SessionExpiredException("Config session for creating new teams/players/sessions has expired!");

        /* 2. Create and save the new team */
        Team_Entity team = teamRepository.save(
                new Team_Entity(
                        data.teamName(),
                        configSession
                ));

        /* 3.  Generate the default stats and save */
        Stats_Entity stats = statsRepository.save(
                new Stats_Entity(
                        0, 0, 50,
                        configSession,
                        team,
                        new HashSet<>()
                )
        );

        /* 4.  Then return the response */
        return new Team_Create_ResponseDTO(
                team.getId(),
                team.getTeamName(),
                stats.getTeamRating(),
                team.getDateCreated()
        );
    }

    @Transactional
    public Team_Play_ResponseDTO play(Team_Play_RequestDTO data) {
        /* STEPS:
         * 1.   Confirm session is still open
         * 2.   Fetch skill selections for this session
         * 3.   Confirm team exists, then Generate entropy and score using each compatible player
         * 4.   Save new Stats containing entropy, teamScore, teamRatings, lastActiveSession, team and member players
         * 5.   For each player, update skillRatings, append data to playerSummary list
         * 6.   Create and return the play response.
         */

        /* 1a.  Confirm session is still open */
        Session_Entity session = sessionRepository.findById(data.sessionId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Locale.US, "Session with UUID='%s' not found!", data.sessionId())));
        Instant expiryDate = session.getDateCreated().plusSeconds(session.getTtl());
        if (Instant.now().isAfter(expiryDate))
            throw new SessionExpiredException("Session for this event expired at " + expiryDate);

        /* 2.  Fetch skill selections for this session */
        List<Long> sessionSkillIds = skillSelectionRepository.getSkillIds(data.sessionId());

        /* 3.   Confirm team exists, then Generate entropy and score using each compatible player */
        Team_Details_Projection teamDetails = teamRepository.getDetailsProjectionById(data.teamId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Locale.US,
                        "Team with UUID='%s' not found!", data.teamId())));
        int entropy = ThreadLocalRandom.current().nextInt(10, 101);
        int aggregate = entropy;
        int teamScore;
        final int oldTeamRating = teamDetails.getTeamRating();
        int n = 1;

        /* Fetch and group each player details */
        Map<UUID, List<Player_Details_Projection>> playerDetailsMap = playerRepository
                .getDetailsProjectionByManyIds(new ArrayList<>(data.playerIds()))
                .stream()
                .collect(Collectors.groupingBy(Player_Details_Projection::getPlayerId));

        Map<UUID, Map<Long, Integer>> playerMapOfRatingsMap = new HashMap<>();
        Map<UUID, Integer> oldRatingsSumMap = new HashMap<>();
        for (UUID playerId : data.playerIds()) {
            /* get player details from group */
            List<Player_Details_Projection> playerDetails = playerDetailsMap.get(playerId);
            if (playerDetails == null)
                throw new ResourceNotFoundException(String.format(Locale.US,
                        "Player with UUID='%s' not found!", playerId));

            /* Map <skill,rating>, <player,average>, <player,ratingsMap> for quick lookup */
            Map<Long, Integer> ratingsMap = new HashMap<>();
            int ratingsSum = 0;
            for (Player_Details_Projection detail : playerDetails) {
                ratingsMap.put(detail.getSkillId(), detail.getSkillRating());
                ratingsSum += detail.getSkillRating();
            }
            oldRatingsSumMap.put(playerId, ratingsSum);
            playerMapOfRatingsMap.put(playerId, ratingsMap);

            /* Reject play if player's average is above team's rating (i.e, not compatible) */
            int average = ratingsSum / playerDetails.size();
            if (average > oldTeamRating)
                throw new RatingLimitException("""
                        Player with ID '%s' has an average rating of %d that \
                        exceeds this team's rating of %d, hence play is rejected!
                        """.formatted(playerId, average, oldTeamRating));

            /* Aggregate for the selected skill using current stats */
            for (Long skillId : sessionSkillIds) {
                n++;
                aggregate += ratingsMap.getOrDefault(skillId, 0);
            }

        }

        /* 4.   Save new Stats containing entropy, teamScore, teamRatings, lastActiveSession, team and member players */
        teamScore = aggregate / n;
        final int newTeamRating = (oldTeamRating + teamScore) / 2;
        Stats_Entity stats = statsRepository.save(
                new Stats_Entity(
                        teamScore,
                        entropy,
                        newTeamRating,
                        session,
                        teamRepository.getReferenceById(data.teamId()),
                        data.playerIds().stream()
                                .map(playerRepository::getReferenceById)
                                .collect(Collectors.toSet())
                )
        );

        if (1 != teamRepository.updateActiveSession(data.teamId(), data.sessionId()))
            throw new PersistenceException("""
                    Team with ID '%s' couldn't update its last active session reference!
                    """.formatted(data.teamId()));

        /* 5.   For each player, update skillRatings, append data to playerSummary list */
        List<Team_PlayerSummary_DTO> playerSummaries = new ArrayList<>();
        for (UUID playerId : data.playerIds()) {

            List<Player_Details_Projection> playerDetails = playerDetailsMap.get(playerId);
            int oldPlayerRatingsSum = oldRatingsSumMap.get(playerId);
            int newPlayerRatingsSum = oldPlayerRatingsSum;
            Map<Long, Integer> ratingsMap = playerMapOfRatingsMap.get(playerId);

            int oldRatingsCount = playerDetails.size();
            int newRatingsCount = playerDetails.size();
            for (Long skillId : sessionSkillIds) {
                Integer oldSkillValue = ratingsMap.get(skillId);
                if(oldSkillValue == null){
                    oldSkillValue = 0;
                    newRatingsCount++;
                }
                int skillRatingValue = (oldSkillValue + teamScore) / 2;
                newPlayerRatingsSum += (skillRatingValue - oldSkillValue);
                skillRatingRepository.save(
                        new SkillRating_Entity(
                                playerRepository.getReferenceById(playerId),
                                skillRepository.getReferenceById(skillId),
                                skillRatingValue
                        )
                );
            }

            playerSummaries.add(
                    new Team_PlayerSummary_DTO(
                            playerDetails.getFirst().getUserName(),
                            oldPlayerRatingsSum / oldRatingsCount,
                            newPlayerRatingsSum / newRatingsCount
                    )
            );
        }

        /* 6.   Create and return the Play response. */
        return new Team_Play_ResponseDTO(
                session.getSessionName(),
                teamDetails.getTeamName(),
                stats.getTeamScore(),
                stats.getEntropy(),
                oldTeamRating,
                stats.getTeamRating(),
                stats.getDateCreated(),
                playerSummaries
        );
    }

    public Team_Fetch_ResponseDTO findTeam(UUID teamId) {
        Team_Details_Projection teamDetails = teamRepository.getDetailsProjectionById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Locale.US,
                        "Team with UUID='%s' not found!", teamId)));
        return teamMapper.toFetch_ResponseDTO(teamDetails);
    }

    public Team_Fetch_ResponseDTO findTeam(String teamName) {
        Team_Details_Projection teamDetails = teamRepository.getDetailsProjectionByName(teamName)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Locale.US,
                        "Team name: '%s' not found!", teamName)));
        return teamMapper.toFetch_ResponseDTO(teamDetails);
    }


    public List<Team_Fetch_ResponseDTO> searchForTeams(String nameContent) {
        /* Get all searched team details */
        List<Team_Details_Projection> teamsDetails =
                teamRepository.getDetailsProjectionByNameContaining(nameContent);

        /* return response list */
        return teamsDetails.stream()
                .map(teamMapper::toFetch_ResponseDTO)
                .toList();
    }

    public List<Team_Fetch_ResponseDTO> listAllTeams() {
        /* Get all teams details */
        List<Team_Details_Projection> teamsDetails = teamRepository.getAllDetailsProjection();

        /* return response list */
        return teamsDetails.stream()
                .map(teamMapper::toFetch_ResponseDTO)
                .toList();
    }

}
