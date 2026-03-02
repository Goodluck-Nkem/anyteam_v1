package nkemrocks.anyteam_v1.mapper;

import nkemrocks.anyteam_v1.dto.player.response.Player_Create_ResponseDTO;
import nkemrocks.anyteam_v1.dto.player.response.Player_Fetch_ResponseDTO;
import nkemrocks.anyteam_v1.entity.Player_Entity;
import org.springframework.stereotype.Component;

@Component
public class Player_Mapper {

    public Player_Fetch_ResponseDTO toFetch_ResponseDTO(Player_Entity player, PlayerStats_Mapper playerStatsMapper){
        return new Player_Fetch_ResponseDTO(
                player.getId(),
                player.getPlayerName(),
                player.getFirstName(),
                player.getLastName(),
                playerStatsMapper.toFetch_ResponseDTO(player.getCurrentPlayerStats()),
                player.getDateCreated(),
                player.getDateUpdated()
        );
    }

    public Player_Create_ResponseDTO toCreate_ResponseDTO(Player_Entity player, PlayerStats_Mapper playerStatsMapper){
        return new Player_Create_ResponseDTO(
                player.getId()
        );
    }
}
