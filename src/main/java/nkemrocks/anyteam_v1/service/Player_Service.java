package nkemrocks.anyteam_v1.service;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import nkemrocks.anyteam_v1.dto.login.Login_RequestDTO;
import nkemrocks.anyteam_v1.dto.player.request.Player_Create_RequestDTO;
import nkemrocks.anyteam_v1.dto.player.request.Player_Update_RequestDTO;
import nkemrocks.anyteam_v1.dto.player.response.Player_Create_ResponseDTO;
import nkemrocks.anyteam_v1.dto.player.response.Player_Fetch_ResponseDTO;
import nkemrocks.anyteam_v1.dto.player.response.Player_Update_ResponseDTO;
import nkemrocks.anyteam_v1.exception.PolicyException;
import nkemrocks.anyteam_v1.mapper.Player_Mapper;
import nkemrocks.anyteam_v1.projection.Player_Details_Projection;
import nkemrocks.anyteam_v1.entity.*;
import nkemrocks.anyteam_v1.exception.ResourceNotFoundException;
import nkemrocks.anyteam_v1.exception.ServiceUnavailableException;
import nkemrocks.anyteam_v1.exception.SessionExpiredException;
import nkemrocks.anyteam_v1.repository.*;
import nkemrocks.anyteam_v1.util.CookieUtil;
import nkemrocks.anyteam_v1.util.GlobalUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Player_Service {
    private final Player_Mapper playerMapper;
    private final Player_Repository playerRepository;
    private final Skill_Repository skillRepository;
    private final SkillRating_Repository skillRatingRepository;
    private final Session_Repository sessionRepository;
    private final Jwt_Service jwtService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Player_Create_ResponseDTO createPlayer(Player_Create_RequestDTO data) {

        /* STEPS:
         * 1a. Confirm the <config> session has been initialized
         * 1b. Confirm it is still open
         * 2.  Create and save the new player
         * 3.  Generate the new skillRatings and save
         * 4.  Return the player details
         */

        /* 1a. Confirm the <config> session has been initialized */
        Session_Entity configSession = sessionRepository.findBySessionName(GlobalUtil.configSessionName)
                .orElseThrow(() -> new ServiceUnavailableException("System Configuration (sysConfig) session is not yet initialized!"));

        /* 1b. Confirm it is still open */
        if (Instant.now().isAfter(configSession.getDateCreated().plusSeconds(configSession.getTtl())))
            throw new SessionExpiredException("Config session for creating new teams/players/sessions has expired!");

        /* 2. Create and save the new player */
        Player_Entity player = playerRepository.save(
                new Player_Entity(
                        data.userName(),
                        Objects.requireNonNull(passwordEncoder.encode(data.password())),
                        data.firstName(),
                        data.lastName()
                ));

        /* 3.  Generate the new skillRatings and save */

        /* STEPS:
         * 1a.  Init focus fields
         * 1b.  Fetch skills map
         * 1c.  Make sure focus set exists
         * 2a.  Share the allocated points
         * 2b.  Noting that 400pts is assigned for allocation.
         * 2c.  The focus set will take pts W.R.T size
         * -.    1[100], 2[95], 3[90], 4[80], 5[70], 6[60], 7[50], 8[45], 9,10,0[40]
         */

        /* 1a.  Init focus fields */
        final int[] map = {40, 100, 95, 90, 80, 70, 60, 50, 45, 40};
        final int focusSize = data.skillFocus().size() >= 10 ? 0 : data.skillFocus().size();
        final int eachFocus = map[focusSize];
        int remnant = 400 - (eachFocus * focusSize);
        final int ceil = (int) Math.ceil((float) remnant / (10 - focusSize));

        /* 1b.  Fetch skills map */
        Map<String, Skill_Entity> skillsMap = skillRepository.findAll()
                .stream()
                .collect(Collectors.toMap(
                        Skill_Entity::getSkillName,
                        skill -> skill
                ));

        /* 1c.   Make sure focus set exists */
        for (String focus : data.skillFocus()) {
            Skill_Entity skill = skillsMap.get(focus);
            if (skill == null)
                throw new ResourceNotFoundException("Requested focus skill '" + focus + "' doesn't exist!");
        }

        /*  Share the allocated points */
        List<SkillRating_Entity> playerSkillRatings = new ArrayList<>();
        Map<String, Integer> initialSkillRatingsMap = new HashMap<>();
        int aggregate = 0;
        for (Skill_Entity skill : skillsMap.values()) {
            int value = data.skillFocus().contains(skill.getSkillName()) ?
                    eachFocus :
                    ((remnant -= ceil) < 0 ? remnant + ceil : ceil);
            playerSkillRatings.add(
                    new SkillRating_Entity(
                            player,
                            skill,
                            value
                    ));
            initialSkillRatingsMap.put(skill.getSkillName(), value);
            aggregate += value;
        }
        skillRatingRepository.saveAll(playerSkillRatings);

        /* 4. return the player entity */
        return new Player_Create_ResponseDTO(
                player.getId(),
                player.getUserName(),
                player.getFirstName(),
                player.getLastName(),
                player.getDateCreated(),
                initialSkillRatingsMap,
                aggregate / initialSkillRatingsMap.size()
        );
    }

    /**
     * update player by ID
     */
    public Player_Update_ResponseDTO updatePlayer(Player_Update_RequestDTO data) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null)
            throw new PolicyException(
                    HttpStatus.UNAUTHORIZED,
                    "Security context failed for this player, try to re-login or refresh!"
            );

        Player_Entity player = playerRepository.findByUserName(auth.getName())
                .orElseThrow(() -> new PolicyException(HttpStatus.UNAUTHORIZED, """
                        Player with username '%s' not found!, try to re-login or refresh!"""
                        .formatted(auth.getName())
                ));

        player.setFirstName(data.firstName());
        player.setLastName(data.lastName());
        return playerMapper.toUpdate_ResponseDTO(playerRepository.save(player));
    }

    /**
     * find exact player by ID
     */
    public Player_Fetch_ResponseDTO findPlayer(UUID playerId) {
        List<Player_Details_Projection> playerDetails = playerRepository.getDetailsProjectionById(playerId);
        if (playerDetails.isEmpty())
            throw new ResourceNotFoundException(String.format(Locale.US,
                    "Player with UUID='%s' not found!", playerId));
        return playerMapper.toFetch_ResponseDTO(playerDetails);
    }

    /**
     * find exact player by name (UI should have no reason to use this, should instead use ID for speed)
     */
    public Player_Fetch_ResponseDTO findPlayer(String userName) {
        List<Player_Details_Projection> playerDetails = playerRepository.getDetailsProjectionByName(userName);
        if (playerDetails.isEmpty())
            throw new ResourceNotFoundException(String.format(Locale.US,
                    "Player name: '%s' not found!", userName));
        return playerMapper.toFetch_ResponseDTO(playerDetails);
    }

    /**
     * search players with name like this, also with pagination (default 10)
     */
    public List<Player_Fetch_ResponseDTO> searchForPlayers(String nameContent) {
        /* Get all searched players details */
        List<Player_Details_Projection> playersDetails = playerRepository.getDetailsProjectionByManyIds(
                playerRepository.getIds_SearchByNameContaining(nameContent)
        );

        /* group by ID (maintain stream order) */
        Map<UUID, List<Player_Details_Projection>> detailsMap = playersDetails.stream()
                .collect(Collectors.groupingBy(
                        Player_Details_Projection::getPlayerId,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        /* return response list */
        return detailsMap.keySet().stream()
                .map(playerId -> playerMapper.toFetch_ResponseDTO(
                        detailsMap.getOrDefault(playerId, List.of())
                ))
                .toList();
    }

    /**
     * List all players
     */
    public List<Player_Fetch_ResponseDTO> listAllPlayers() {
        /* Get all player details */
        List<Player_Details_Projection> playersDetails = playerRepository.getDetailsProjectionByManyIds(
                playerRepository.getIds_findAll()
        );

        /* group by ID (maintain stream order) */
        Map<UUID, List<Player_Details_Projection>> detailsMap = playersDetails.stream()
                .collect(Collectors.groupingBy(
                        Player_Details_Projection::getPlayerId,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        /* return response list */
        return detailsMap.keySet().stream()
                .map(playerId -> playerMapper.toFetch_ResponseDTO(
                        detailsMap.getOrDefault(playerId, List.of())
                ))
                .toList();
    }

    public Player_Fetch_ResponseDTO loginPlayer(
            Login_RequestDTO data,
            HttpServletResponse httpServletResponse) {

        List<Player_Details_Projection> playerDetails = playerRepository.getDetailsProjectionByName(data.uniqueName());
        if (playerDetails.isEmpty())
            throw new ResourceNotFoundException("""
                    Player with username '%s' not found!"""
                    .formatted(data.uniqueName()));

        if (!passwordEncoder.matches(data.password(), playerDetails.getFirst().getPasswordHash()))
            throw new PolicyException(HttpStatus.UNAUTHORIZED, "Invalid player credentials!");

        String token = jwtService.generateToken(data.uniqueName(), "PLAYER");
        CookieUtil.addJwtCookie(httpServletResponse, token);

        return playerMapper.toFetch_ResponseDTO(playerDetails);
    }
}
