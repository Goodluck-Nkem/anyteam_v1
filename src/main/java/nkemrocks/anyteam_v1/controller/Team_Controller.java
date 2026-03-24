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
@RequestMapping("/api/v1/team")
public class Team_Controller {
    private final Team_Service teamService;
    private final Result_Service resultService;

    private static final Map<String, String> teamSortFieldsMap = Map.of(
            "team", "teamName",
            "rating", "rating",
            "date", "id"
    );

    private static final Map<String, String> resultSortFieldsMap = Map.of(
            "score", "score",
            "entropy", "entropy",
            "date", "id",
            "session", "session.sessionName",
            "team", "team.teamName"
    );


    @PostMapping("/create")
    public ResponseEntity<Team_Create_ResponseDTO> create(@Valid @RequestBody Team_Create_RequestDTO data) {
        Team_Create_ResponseDTO response = teamService.createTeam(data);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/play")
    public ResponseEntity<Team_Play_ResponseDTO> play(@Valid @RequestBody Team_Play_RequestDTO data) {
        Team_Play_ResponseDTO response = teamService.play(ValidationUtil.validatePlayRequest(data));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/results")
    public ResponseEntity<Slice<Team_Result_ResponseDTO>> results(
            @RequestParam(required = false) UUID teamId,
            @RequestParam(required = false) UUID sessionId,
            @RequestParam(required = false) String teamName,
            @RequestParam(required = false) String sessionName,
            @RequestParam(required = false) String by,
            Pageable pageable
    ) {
        by = trimAndLower(by);
        teamName = trimAndLower(teamName);
        sessionName = trimAndLower(sessionName);
        if (by == null || (!by.equals("id") && !by.equals("name")))
            throw new PolicyException(
                    HttpStatus.BAD_REQUEST, """
                    You must indicate how to find the results either via ID(recommended) or name. --- \
                    For example, /results?by=id, /results?by=id&teamId='123', \
                    /results?by=name, /results?by=name&sessionName=fall2026, etc"""
            );
        Slice<Team_Result_ResponseDTO> response;
        pageable = validatePageableSort(pageable, resultSortFieldsMap);

        if ((teamId != null || teamName != null) && (sessionId != null || sessionName != null))
            response = by.equals("id") ?
                    resultService.getResults_OneTeamSessionPair(teamId, sessionId, pageable) :
                    resultService.getResults_OneTeamSessionPair(teamName, sessionName, pageable);

        else if (teamId != null || teamName != null)
            response = by.equals("id") ?
                    resultService.getResults_OneTeamAllSessions(teamId, pageable) :
                    resultService.getResults_OneTeamAllSessions(teamName, pageable);

        else if (sessionId != null || sessionName != null)
            response = by.equals("id") ?
                    resultService.getResults_AllTeamsOneSession(sessionId, pageable) :
                    resultService.getResults_AllTeamsOneSession(sessionName, pageable);

        else
            response = resultService.getResults_AllTeamsAllSessions(pageable);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/find")
    public ResponseEntity<Team_Fetch_ResponseDTO> find(
            @RequestParam(required = false) UUID id,
            @RequestParam(required = false) String name
    ) {
        if (id == null && (name == null || name.trim().isBlank()))
            throw new PolicyException(
                    HttpStatus.BAD_REQUEST, """
                    You must provide either the team ID(recommended) or team name to find the team. \
                    For example, /find?id=3, or /find?name=ABC"""
            );
        Team_Fetch_ResponseDTO response = id != null ?
                teamService.findTeam(id) :
                teamService.findTeam(trimAndLower(name));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<Slice<Team_Fetch_ResponseDTO>> search(
            @RequestParam String q,
            Pageable pageable
    ) {
        Slice<Team_Fetch_ResponseDTO> response = teamService.searchForTeams(
                q.toLowerCase(),
                validatePageableSort(pageable, teamSortFieldsMap));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list")
    public ResponseEntity<Slice<Team_Fetch_ResponseDTO>> list(Pageable pageable) {
        Slice<Team_Fetch_ResponseDTO> response = teamService.listAllTeams(
                validatePageableSort(pageable, teamSortFieldsMap)
        );
        return ResponseEntity.ok(response);
    }
}
