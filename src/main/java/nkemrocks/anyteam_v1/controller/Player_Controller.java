package nkemrocks.anyteam_v1.controller;

import lombok.RequiredArgsConstructor;
import nkemrocks.anyteam_v1.dto.player.request.*;
import nkemrocks.anyteam_v1.dto.player.response.*;
import nkemrocks.anyteam_v1.dto.player.response.Player_Fetch_ResponseDTO;
import nkemrocks.anyteam_v1.exception.PolicyException;
import nkemrocks.anyteam_v1.service.Player_Service;
import jakarta.validation.Valid;
import nkemrocks.anyteam_v1.service.Result_Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

import static nkemrocks.anyteam_v1.util.GlobalUtil.trimAndLower;
import static nkemrocks.anyteam_v1.util.ValidationUtil.validatePageableSort;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/player")
public class Player_Controller {
    private final Player_Service playerService;
    private final Result_Service resultService;

    private static final Map<String, String> playerSortFieldsMap = Map.of(
            "player", "playerName",
            "date", "id"
    );

    private static final Map<String, String> resultSortFieldsMap = Map.of(
            "score", "result.score",
            "entropy", "result.entropy",
            "date", "result.id",
            "team", "result.team.teamName",
            "session", "session.sessionName",
            "player", "player.playerName"
    );

    @PostMapping("/create")
    public ResponseEntity<Player_Create_ResponseDTO> create(@Valid @RequestBody Player_Create_RequestDTO data) {
        Player_Create_ResponseDTO response = playerService.createPlayer(data);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/update")
    public ResponseEntity<Player_Update_ResponseDTO> update(@Valid @RequestBody Player_Update_RequestDTO data) {
        Player_Update_ResponseDTO response = playerService.updatePlayer(data);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/results")
    public ResponseEntity<Slice<Player_Result_ResponseDTO>> results(
            @RequestParam(required = false) UUID playerId,
            @RequestParam(required = false) UUID sessionId,
            @RequestParam(required = false) String playerName,
            @RequestParam(required = false) String sessionName,
            @RequestParam(required = false) String by,
            Pageable pageable
    ) {
        by = trimAndLower(by);
        playerName = trimAndLower(playerName);
        sessionName = trimAndLower(sessionName);
        if (by == null || (!by.equals("id") && !by.equals("name")))
            throw new PolicyException(
                    HttpStatus.BAD_REQUEST, """
                    You must indicate how to find the results either via ID(recommended) or name. --- \
                    For example, /results?by=id, /results?by=id&playerId='123', \
                    /results?by=name, /results?by=name&sessionName=fall2026, etc"""
            );
        Slice<Player_Result_ResponseDTO> response;
        pageable = validatePageableSort(pageable, resultSortFieldsMap);

        if ((playerId != null || playerName != null) && (sessionId != null || sessionName != null))
            response = by.equals("id") ?
                    resultService.getResults_OnePlayerSessionPair(playerId, sessionId, pageable) :
                    resultService.getResults_OnePlayerSessionPair(playerName, sessionName, pageable);

        else if (playerId != null || playerName != null)
            response = by.equals("id") ?
                    resultService.getResults_OnePlayerAllSessions(playerId, pageable) :
                    resultService.getResults_OnePlayerAllSessions(playerName, pageable);

        else if (sessionId != null || sessionName != null)
            response = by.equals("id") ?
                    resultService.getResults_AllPlayersOneSession(sessionId, pageable) :
                    resultService.getResults_AllPlayersOneSession(sessionName, pageable);

        else
            response = resultService.getResults_AllPlayersAllSessions(pageable);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/find")
    public ResponseEntity<Player_Fetch_ResponseDTO> find(
            @RequestParam(required = false) UUID id,
            @RequestParam(required = false) String name
    ) {
        if (id == null && (name == null || name.trim().isBlank()))
            throw new PolicyException(
                    HttpStatus.BAD_REQUEST, """
                    You must provide either the player ID(recommended) or player name to find the player. \
                    For example, /find?id=3, or /find?name=ABC""");
        Player_Fetch_ResponseDTO response = id != null ?
                playerService.findPlayer(id) :
                playerService.findPlayer(trimAndLower(name));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<Slice<Player_Fetch_ResponseDTO>> search(
            @RequestParam String q,
            Pageable pageable
    ) {
        Slice<Player_Fetch_ResponseDTO> response = playerService.searchForPlayers(
                q.toLowerCase(), validatePageableSort(pageable, playerSortFieldsMap));
        return ResponseEntity.ok(response);
    }

    /**
     * Lists a slice of all created players in the database
     */
    @GetMapping("/list")
    public ResponseEntity<Slice<Player_Fetch_ResponseDTO>> list(Pageable pageable) {
        Slice<Player_Fetch_ResponseDTO> response = playerService.listAllPlayers(
                validatePageableSort(pageable, playerSortFieldsMap));
        return ResponseEntity.ok(response);
    }
}
