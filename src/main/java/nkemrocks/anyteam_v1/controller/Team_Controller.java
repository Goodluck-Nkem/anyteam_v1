package nkemrocks.anyteam_v1.controller;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nkemrocks.anyteam_v1.dto.team.response.Team_Result_ResponseDTO;
import nkemrocks.anyteam_v1.dto.team.request.Team_Create_RequestDTO;
import nkemrocks.anyteam_v1.dto.team.request.Team_Play_RequestDTO;
import nkemrocks.anyteam_v1.dto.team.response.Team_Create_ResponseDTO;
import nkemrocks.anyteam_v1.dto.team.response.Team_Fetch_ResponseDTO;
import nkemrocks.anyteam_v1.dto.team.response.Team_Play_ResponseDTO;
import nkemrocks.anyteam_v1.exception.PolicyException;
import nkemrocks.anyteam_v1.service.Result_Service;
import nkemrocks.anyteam_v1.service.Team_Service;
import nkemrocks.anyteam_v1.util.ValidationUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static nkemrocks.anyteam_v1.util.GlobalUtil.trimAndLower;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/team")
public class Team_Controller {
    private final Team_Service teamService;
    private final Result_Service resultService;

    @PostMapping("/create")
    public ResponseEntity<Team_Create_ResponseDTO> create(@Valid @RequestBody Team_Create_RequestDTO data){
        Team_Create_ResponseDTO response = teamService.createTeam(data);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/play")
    public  ResponseEntity<Team_Play_ResponseDTO> play(@Valid @RequestBody Team_Play_RequestDTO data){
        Team_Play_ResponseDTO response = teamService.play(ValidationUtil.validatePlayRequest(data));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/results")
    public ResponseEntity<List<Team_Result_ResponseDTO>> results(
            @RequestParam(required = false) UUID teamId,
            @RequestParam(required = false) UUID sessionId
    ) {
        List<Team_Result_ResponseDTO> response;

        if (teamId != null && sessionId != null)
            response = resultService.getResults_OneTeamSessionPair(teamId, sessionId);
        else if (teamId != null)
            response = resultService.getResults_OneTeamAllSessions(teamId);
        else if (sessionId != null)
            response = resultService.getResults_AllTeamsOneSession(sessionId);
        else
            response = resultService.getResults_AllTeamsAllSessions();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/find")
    public ResponseEntity<Team_Fetch_ResponseDTO> find(
          @RequestParam(required = false) UUID id,
          @RequestParam(required = false) String name
    ){
        if (id == null && (name == null || name.trim().isBlank()))
            throw new PolicyException(
                    HttpStatus.BAD_REQUEST, """
                    You must provide either the team ID(recommended) or team name to find the team. \
                    For example, /find?id=3, or /find?name=ABC"""
            );
        Team_Fetch_ResponseDTO response =  id != null ?
                teamService.findTeam(id) :
                teamService.findTeam(trimAndLower(name));
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Team_Fetch_ResponseDTO>> search(
            @RequestParam String q
    ){
        List<Team_Fetch_ResponseDTO> response = teamService.searchForTeams(q.toLowerCase());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Team_Fetch_ResponseDTO>> list(){
        List<Team_Fetch_ResponseDTO> response = teamService.listAllTeams();
        return ResponseEntity.ok(response);
    }
}
