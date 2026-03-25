package nkemrocks.anyteam_v1.service;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import nkemrocks.anyteam_v1.dto.login.Login_RequestDTO;
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
import nkemrocks.anyteam_v1.repository.*;
import nkemrocks.anyteam_v1.util.CookieUtil;
import nkemrocks.anyteam_v1.util.GlobalUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final Result_Repository resultRepository;
    private final Session_Repository sessionRepository;
    private final SkillSelection_Repository skillSelectionRepository;
    private final SkillRating_Repository skillRatingRepository;
    private final Skill_Repository skillRepository;
    private final Junction_Team_Session_Player_Repository junctionTeamSessionPlayerRepository;
    private final PasswordEncoder passwordEncoder;
    private final Jwt_Service jwtService;

    @Transactional
    public Team_Create_ResponseDTO createTeam(Team_Create_RequestDTO data) {

        /* STEPS:
         * 1a. Confirm the <Creation> session has been initialized
         * 1b. Confirm it is still open
         * 2.  Create and save the new team
         * 3.  Then return the response
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
                        Objects.requireNonNull(passwordEncoder.encode(data.password())),
                        60
                ));

        /* 3.  Then return the response */
        return new Team_Create_ResponseDTO(
                team.getId(),
                team.getTeamName(),
                team.getRating(),
                team.getDateCreated()
        );
    }

    @Transactional
    public Team_Play_ResponseDTO play(Team_Play_RequestDTO data) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null)
            throw new PolicyException(
                    HttpStatus.UNAUTHORIZED,
                    "Security context failed for this team, try to re-login or refresh!"
            );

        /* STEPS:
         * 1.   Confirm session is not <config>, exists and is still open
         * 2.   Fetch skill selections for this session
         * 3.   Confirm team exists, then Generate entropy and score using each compatible player
         * 4.   Save new result and update team rating
         * 5.   For each player, update skillRatings, append data to playerSummary list
         * 6.   Create and return the play response.
         */

        /* 1.  Confirm session is not <config>, exists and is still open */
        Session_Entity session = (data.sessionId() != null ?
                sessionRepository.findById(data.sessionId()) :
                sessionRepository.findBySessionName(data.sessionName()))
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Locale.US,
                        "Session with UUID='%s' or name='%s' not found!",
                        data.sessionId(),
                        data.sessionId() == null ? data.sessionName() : "<IGNORED_WHEN_ID_PROVIDED>"
                )));
        if (session.getSessionName().equalsIgnoreCase(GlobalUtil.configSessionName))
            throw new PolicyException(
                    HttpStatus.FORBIDDEN,
                    "You can't use the config session to play!"
            );
        Instant expiryDate = session.getDateCreated().plusSeconds(session.getTtl());
        if (Instant.now().isAfter(expiryDate))
            throw new SessionExpiredException("Session for this event expired at " + expiryDate);

        /* 2.  Fetch skill selections for this session */
        List<Long> sessionSkillIds = skillSelectionRepository.getSkillIds(session.getId());

        /* 3a.   Confirm team exists, then Generate entropy and score using each compatible player */
        Team_Entity team = teamRepository.findByTeamName(auth.getName())
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Locale.US,
                        "Team with name='%s' not found, try to re-login or refresh!",
                        auth.getName()
                )));
        int entropy = ThreadLocalRandom.current().nextInt(20, 101);
        int aggregate = entropy;
        final int oldTeamRating = team.getRating();
        int n = 1;

        /* Fetch and group each player details */
        Map<UUID, List<Player_Details_Projection>> playerDetailsMap =
                (data.playerIds() != null ?
                        playerRepository.getDetailsProjectionByManyIds(new ArrayList<>(data.playerIds())) :
                        playerRepository.getDetailsProjectionByManyNames(new ArrayList<>(data.playerNames())))
                        .stream()
                        .collect(Collectors.groupingBy(Player_Details_Projection::getPlayerId));

        /* make sure each player exists */
        if (data.playerIds() != null) {
            for (UUID id : data.playerIds())
                if (!playerDetailsMap.containsKey(id))
                    throw new ResourceNotFoundException(String.format(Locale.US,
                            "Player with UUID='%s' not found!", id));
        } else {
            for (String name : data.playerNames()) {
                boolean found = false;
                for (UUID keyId : playerDetailsMap.keySet()) {
                    if (playerDetailsMap.get(keyId).getFirst().getPlayerName().equals(name)) {
                        found = true;
                        break;
                    }
                }
                if (!found)
                    throw new ResourceNotFoundException(String.format(Locale.US,
                            "Player with playerName='%s' not found!", name));
            }
        }

        /* accumulate result stats */
        Map<UUID, Map<Long, Integer>> playerMapOfRatingsMap = new HashMap<>();
        Map<UUID, Integer> oldRatingsSumMap = new HashMap<>();
        for (UUID playerId : playerDetailsMap.keySet()) {

            /* get player details from group */
            List<Player_Details_Projection> playerDetails = playerDetailsMap.get(playerId);

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
                throw new PolicyException(
                        HttpStatus.FORBIDDEN, """
                        Player with (playerId = '%s', playerName = '%s') has an average \
                        rating of %d that exceeds team's rating of %d, hence play is rejected!"""
                        .formatted(
                                playerId,
                                playerDetails.getFirst().getPlayerName(),
                                average,
                                oldTeamRating
                        ));

            /* Aggregate for the selected skill using current result */
            for (Long skillId : sessionSkillIds) {
                n++;
                aggregate += ratingsMap.getOrDefault(skillId, 0);
            }

        }

        /* 4.   Save new result and update team rating */
        final int score = Math.ceilDiv(aggregate, n);
        final int newTeamRating = Math.round((oldTeamRating + score) / 2.0f);
        Result_Entity result = resultRepository.save(
                new Result_Entity(
                        score,
                        entropy,
                        session,
                        team
                ));

        if (1 != teamRepository.updateTeamRating(team.getId(), newTeamRating))
            throw new PersistenceException("""
                    Team with ID '%s' couldn't update its rating!"""
                    .formatted(team.getId()));

        /* 5.   For each player, update skillRatings, append data to playerSummary list */
        List<Team_PlayerSummary_DTO> playerSummaries = new ArrayList<>();
        for (UUID playerId : playerDetailsMap.keySet()) {

            List<Player_Details_Projection> playerDetails = playerDetailsMap.get(playerId);
            int oldPlayerRatingsSum = oldRatingsSumMap.get(playerId);
            int newPlayerRatingsSum = oldPlayerRatingsSum;
            Map<Long, Integer> ratingsMap = playerMapOfRatingsMap.get(playerId);

            int oldRatingsCount = playerDetails.size();
            int newRatingsCount = playerDetails.size();
            for (Long skillId : sessionSkillIds) {
                Integer oldSkillValue = ratingsMap.get(skillId);
                if (oldSkillValue == null) {
                    oldSkillValue = 0;
                    newRatingsCount++;
                }
                int skillRatingValue = Math.round((oldSkillValue + score) / 2.0f);
                newPlayerRatingsSum += (skillRatingValue - oldSkillValue);

                /* select and update skill rating */
                SkillRating_Entity skillRating = skillRatingRepository.findByPlayerIdAndSkillId(playerId, skillId)
                        .orElse(new SkillRating_Entity(
                                playerRepository.getReferenceById(playerId),
                                skillRepository.getReferenceById(skillId),
                                skillRatingValue
                        ));
                skillRating.setSkillRating(skillRatingValue);
                skillRatingRepository.save(skillRating);

            }

            /* add the player summary */
            playerSummaries.add(
                    new Team_PlayerSummary_DTO(
                            playerDetails.getFirst().getPlayerName(),
                            oldPlayerRatingsSum / oldRatingsCount,
                            newPlayerRatingsSum / newRatingsCount
                    )
            );

            /* insert relation to junction table (try-catch then rethrow) */
            try {
                junctionTeamSessionPlayerRepository.save(
                        new Junction_Result_Session_Player(
                                result,
                                session,
                                playerRepository.getReferenceById(playerId)
                        ));
            } catch (Exception e) {
                throw new PolicyException(
                        HttpStatus.FORBIDDEN, """
                        Requested Player with (playerId = '%s', playerName = '%s') \
                        have already registered a result with another team for this session!"""
                        .formatted(
                                playerDetails.getFirst().getPlayerId(),
                                playerDetails.getFirst().getPlayerName()
                        ));
            }

        }

        /* 6.   Create and return the Play response. */
        return new Team_Play_ResponseDTO(
                session.getSessionName(),
                team.getTeamName(),
                result.getScore(),
                result.getEntropy(),
                oldTeamRating,
                newTeamRating,
                result.getDateCreated(),
                playerSummaries
        );
    }

    public Team_Fetch_ResponseDTO findTeam(UUID teamId) {
        Team_Entity team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Locale.US,
                        "Team with UUID='%s' not found!", teamId)));
        return teamMapper.toFetch_ResponseDTO(team);
    }

    public Team_Fetch_ResponseDTO findTeam(String teamName) {
        Team_Entity team = teamRepository.findByTeamName(teamName)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Locale.US,
                        "Team name: '%s' not found!", teamName)));
        return teamMapper.toFetch_ResponseDTO(team);
    }


    public Slice<Team_Fetch_ResponseDTO> searchForTeams(String nameContent, Pageable pageable) {
        /* Get a slice of all matched teams */
        Slice<Team_Entity> teams =
                teamRepository.findByTeamNameContaining(nameContent, pageable);

        /* return response list */
        return new SliceImpl<>(
                teams.stream()
                        .map(teamMapper::toFetch_ResponseDTO)
                        .toList(),
                pageable,
                teams.hasNext()
        );
    }

    public Slice<Team_Fetch_ResponseDTO> listAllTeams(Pageable pageable) {
        /* Get a slice of all teams */
        Slice<Team_Entity> teams = teamRepository.findAllTeams(pageable);

        /* return response list */
        return new SliceImpl<>(
                teams.stream()
                        .map(teamMapper::toFetch_ResponseDTO)
                        .toList(),
                pageable,
                teams.hasNext()
        );
    }

    public Team_Fetch_ResponseDTO loginTeam(Login_RequestDTO data, HttpServletResponse httpServletResponse) {

        Team_Entity team = teamRepository.findByTeamName(data.uniqueName())
                .orElseThrow(() -> new ResourceNotFoundException("""
                        Team with name '%s' not found!"""
                        .formatted(data.uniqueName())));

        if (!passwordEncoder.matches(data.password(), team.getPasswordHash()))
            throw new PolicyException(HttpStatus.UNAUTHORIZED, "Invalid team credentials!");

        String token = jwtService.generateToken(data.uniqueName(), "TEAM");
        CookieUtil.addJwtCookie(httpServletResponse, token);

        return teamMapper.toFetch_ResponseDTO(team);
    }
}
