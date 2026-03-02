package nkemrocks.anyteam_v1.controller;

import nkemrocks.anyteam_v1.entity.Player_Entity;
import nkemrocks.anyteam_v1.dto.player.request.Player_Create_RequestDTO;
import nkemrocks.anyteam_v1.dto.player.response.Player_Create_ResponseDTO;
import nkemrocks.anyteam_v1.dto.player.response.Player_Fetch_ResponseDTO;
import nkemrocks.anyteam_v1.mapper.PlayerStats_Mapper;
import nkemrocks.anyteam_v1.mapper.Player_Mapper;
import nkemrocks.anyteam_v1.service.Player_Service;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/player")
public class Player_Controller {
    private final Player_Service playerService;
    private final Player_Mapper playerMapper;
    private final PlayerStats_Mapper playerStatsMapper;

    @PostMapping
    public ResponseEntity<Player_Create_ResponseDTO> create(@Valid @RequestBody Player_Create_RequestDTO data) {
        Player_Entity player = playerService.createPlayer(data);
        return new ResponseEntity<>(playerMapper.toCreate_ResponseDTO(player, playerStatsMapper), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Player_Fetch_ResponseDTO>> list() {
        List<Player_Entity> players = playerService.listAllPlayers();
        List<Player_Fetch_ResponseDTO> fetchResponseDTOS = players
                .stream()
                .map(player -> playerMapper.toFetch_ResponseDTO(player, playerStatsMapper))
                .toList();
        return ResponseEntity.ok(fetchResponseDTOS);
    }
}
