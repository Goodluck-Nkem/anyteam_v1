package nkemrocks.anyteam_v1.projection;

import java.time.Instant;
import java.util.UUID;

public interface Team_Details_Projection {

    UUID getTeamId();
    String getTeamName();
    String getPasswordHash();
    Instant getDateCreated();

    Integer getTeamRating();

}
