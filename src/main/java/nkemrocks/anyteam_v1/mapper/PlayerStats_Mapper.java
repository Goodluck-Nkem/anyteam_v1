package nkemrocks.anyteam_v1.mapper;

import nkemrocks.anyteam_v1.dto.playerStats.response.PlayerStats_Fetch_ResponseDTO;
import nkemrocks.anyteam_v1.entity.PlayerStats_Entity;
import org.springframework.stereotype.Component;

@Component
public class PlayerStats_Mapper {
    public PlayerStats_Fetch_ResponseDTO toFetch_ResponseDTO(PlayerStats_Entity playerStats){
        return new PlayerStats_Fetch_ResponseDTO(
        );
    }
}
