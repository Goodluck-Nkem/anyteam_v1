package nkemrocks.anyteam_v1.controller;

import lombok.RequiredArgsConstructor;
import nkemrocks.anyteam_v1.dto.stats.response.Stats_ResponseDTO;
import nkemrocks.anyteam_v1.service.Stats_Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/team_stats")
public class Stats_Controller {

    private final Stats_Service statsService;

    @GetMapping
    public ResponseEntity<List<Stats_ResponseDTO>> info(
            @RequestParam(required = false) UUID teamId,
            @RequestParam(required = false) UUID sessionId
    ) {
        List<Stats_ResponseDTO> response;

        if (teamId != null && sessionId != null)
            response = statsService.getInfo_OneTeamSessionPair(teamId, sessionId);
        else if (teamId != null)
            response = statsService.getInfo_OneTeamAllSessions(teamId);
        else if (sessionId != null)
            response = statsService.getInfo_AllTeamsOneSession(sessionId);
        else
            response = statsService.getAllTeamsAllSessions();

        return ResponseEntity.ok(response);
    }
}
