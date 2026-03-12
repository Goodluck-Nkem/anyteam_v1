package nkemrocks.anyteam_v1.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nkemrocks.anyteam_v1.dto.session.request.*;
import nkemrocks.anyteam_v1.dto.session.response.*;
import nkemrocks.anyteam_v1.service.Session_Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static nkemrocks.anyteam_v1.GlobalUtil.trimAndLower;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/session")
public class Session_Controller {
    private final Session_Service sessionService;

    @PostMapping("/create")
    public ResponseEntity<Session_Create_ResponseDTO> create(@Valid @RequestBody Session_Create_RequestDTO data) {
        Session_Create_ResponseDTO response = sessionService.createSession(data);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/update")
    public ResponseEntity<Session_Update_ResponseDTO> update(@Valid @RequestBody Session_Update_RequestDTO data) {
        Session_Update_ResponseDTO response = sessionService.updateSession(data);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/find")
    public ResponseEntity<Session_Fetch_ResponseDTO> find(
            @RequestParam(required = false) UUID id,
            @RequestParam(required = false) String name
    ) {
        if (id == null && (name == null || name.trim().isBlank()))
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, """
                    You must provide either the session ID(recommended) or session name to find the session.
                    For example, /find?id=3, or /find?name=ABC
                    """
            );
        Session_Fetch_ResponseDTO response = id != null ?
                sessionService.findSession(id) :
                sessionService.findSession(trimAndLower(name));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Session_Fetch_ResponseDTO>> search(
            @RequestParam String q
    ) {
        List<Session_Fetch_ResponseDTO> response = sessionService.searchForSessions(q.toLowerCase());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Session_Fetch_ResponseDTO>> list() {
        List<Session_Fetch_ResponseDTO> response = sessionService.listAllSessions();
        return ResponseEntity.ok(response);
    }
}
