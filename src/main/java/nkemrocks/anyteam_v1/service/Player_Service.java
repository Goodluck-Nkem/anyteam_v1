package nkemrocks.anyteam_v1.service;

import jakarta.transaction.Transactional;
import nkemrocks.anyteam_v1.dto.player.request.Player_Create_RequestDTO;
import nkemrocks.anyteam_v1.dto.playerStats.request.PlayerStats_Create_RequestDTO;
import nkemrocks.anyteam_v1.entity.SysConfig_Entity;
import nkemrocks.anyteam_v1.entity.PlayerStats_Entity;
import nkemrocks.anyteam_v1.entity.Player_Entity;
import nkemrocks.anyteam_v1.entity.Session_Entity;
import nkemrocks.anyteam_v1.exception.ServiceUnavailableException;
import nkemrocks.anyteam_v1.exception.SessionExpiredException;
import nkemrocks.anyteam_v1.repository.SysConfig_Repository;
import nkemrocks.anyteam_v1.repository.PlayerStats_Repository;
import nkemrocks.anyteam_v1.repository.Player_Repository;
import lombok.AllArgsConstructor;
import nkemrocks.anyteam_v1.service.util.PlayerStats_ServiceUtil;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class Player_Service {
    private final Player_Repository playerRepository;
    private final PlayerStats_Repository playerStatsRepository;
    private final SysConfig_Repository sysConfigRepository;

    @Transactional
    public Player_Entity createPlayer(Player_Create_RequestDTO data) {

        /* STEPS:
         * 1a. Confirm the <Creation> session has been initialized
         * 1b. Confirm it is still open
         * 2.  Create the new player
         * 3a. Generate the new playerStats
         * 3b. Then save it
         * 4.  Update the new player currentStats then return
         */

        /* 1a. Confirm the <Creation> session has been initialized */
        SysConfig_Entity sysConfig = sysConfigRepository.findById(1L)
                .orElseThrow(() -> new ServiceUnavailableException("System Configuration (sysConfig) record is not yet initialized, hence service unavailable!"));
        Session_Entity creationSession = sysConfig.getCreationSession();

        /* 1b. Confirm it is still open */
        if (Instant.now().isAfter(creationSession.getDateCreated().plusSeconds(creationSession.getTtl())))
            throw new SessionExpiredException("Session for creating new teams/players/sessions have expired!");

        /* 2. Create the new player */
        Player_Entity player = new Player_Entity(
                data.playerName(),
                data.firstName(),
                data.lastName()
        );
        player = playerRepository.save(player);

        /* 3a. Generate the new playerStats */
        PlayerStats_Create_RequestDTO playerStatsData = PlayerStats_ServiceUtil.generateForCreationSession(data);

        /* 3b. Then save it */
        PlayerStats_Entity currentPlayerStats = new PlayerStats_Entity(
                player,
                creationSession
        );
        currentPlayerStats.setArt(playerStatsData.art());
        currentPlayerStats.setBiology(playerStatsData.biology());
        currentPlayerStats.setHistory(playerStatsData.history());
        currentPlayerStats.setLanguage(playerStatsData.language());
        currentPlayerStats.setLogic(playerStatsData.logic());
        currentPlayerStats.setMath(playerStatsData.math());
        currentPlayerStats.setMusic(playerStatsData.music());
        currentPlayerStats.setSpelling(playerStatsData.spelling());
        currentPlayerStats.setSport(playerStatsData.sport());
        currentPlayerStats.setTechnology(playerStatsData.technology());
        currentPlayerStats.setRating(playerStatsData.rating());
        currentPlayerStats = playerStatsRepository.save(currentPlayerStats);

        /* 4. Update the new player currentStats */
        player.setCurrentPlayerStats(currentPlayerStats);
        return playerRepository.save(player);
    }

    /**
     * update player by ID
     */
    public Player_Entity updatePlayer() {
        return playerRepository.save();
    }

    public List<Player_Entity> listAllPlayers() {
        return playerRepository.findAll();
    }

    /**
     * find exact player by ID
     */
    public Player_Entity findPlayer() {
        return playerRepository.findById();
    }

    /**
     * find exact player by name (UI should have no reason to use this, should instead use ID for speed)
     */
    public Player_Entity findPlayer() {
        return playerRepository.findByPlayerName();
    }

    /**
     * search players with name like this, also with pagination (default 10)
     */
    public List<Player_Entity> searchForPlayer() {
        return playerRepository.findByPlayerNameContainingIgnoreCase();
    }

}
