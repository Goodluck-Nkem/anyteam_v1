package nkemrocks.anyteam_v1.controller;

import lombok.RequiredArgsConstructor;
import nkemrocks.anyteam_v1.dto.skillRating.response.SkillRating_ResponseDTO;
import nkemrocks.anyteam_v1.service.SkillRating_Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/player_ratings")
public class SkillRating_Controller {
    private final SkillRating_Service skillRatingService;

    @GetMapping
    public ResponseEntity<List<SkillRating_ResponseDTO>> info(
            @RequestParam(required = false) UUID playerId
    ) {
        return ResponseEntity.ok(playerId != null ?
                skillRatingService.getOnePlayer(playerId) :
                skillRatingService.getAllPlayers()
        );
    }
}
