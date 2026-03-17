package nkemrocks.anyteam_v1.controller;

import lombok.RequiredArgsConstructor;
import nkemrocks.anyteam_v1.dto.player.request.*;
import nkemrocks.anyteam_v1.dto.player.response.*;
import nkemrocks.anyteam_v1.dto.player.response.Player_Fetch_ResponseDTO;
import nkemrocks.anyteam_v1.exception.PolicyException;
import nkemrocks.anyteam_v1.service.Player_Service;
import jakarta.validation.Valid;
import nkemrocks.anyteam_v1.service.Result_Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static nkemrocks.anyteam_v1.util.GlobalUtil.trimAndLower;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/player")
public class Player_Controller {
    private final Player_Service playerService;
    private final Result_Service resultService;

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
    public ResponseEntity<List<Player_Result_ResponseDTO>> results(
            @RequestParam(required = false) UUID playerId,
            @RequestParam(required = false) UUID sessionId
    ) {
        List<Player_Result_ResponseDTO> response;

        if (playerId != null && sessionId != null)
            response = resultService.getResults_OnePlayerSessionPair(playerId, sessionId);
        else if (playerId != null)
            response = resultService.getResults_OnePlayerAllSessions(playerId);
        else if (sessionId != null)
            response = resultService.getResults_AllPlayersOneSession(sessionId);
        else
            response = resultService.getResults_AllPlayersAllSessions();

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
    public ResponseEntity<List<Player_Fetch_ResponseDTO>> search(
            @RequestParam String q
    ) {
        List<Player_Fetch_ResponseDTO> response = playerService.searchForPlayers(q.toLowerCase());
        return ResponseEntity.ok(response);
    }

    /**
     * Lists all created players in the database
     */
    @GetMapping("/list")
    public ResponseEntity<List<Player_Fetch_ResponseDTO>> list() {
        List<Player_Fetch_ResponseDTO> response = playerService.listAllPlayers();
        return ResponseEntity.ok(response);
    }
}
