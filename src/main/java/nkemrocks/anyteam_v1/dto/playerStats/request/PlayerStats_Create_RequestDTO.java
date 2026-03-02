package nkemrocks.anyteam_v1.dto.playerStats.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class PlayerStats_Create_RequestDTO{
    int art;
    int biology;
    int history;
    int language;
    int logic;
    int math;
    int music;
    int spelling;
    int sport;
    int technology;
    int rating;
}
