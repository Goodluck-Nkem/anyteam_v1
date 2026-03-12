package nkemrocks.anyteam_v1.projection;

import java.time.Instant;
import java.util.UUID;

public interface Team_Details_Projection {

    UUID getTeamId();
    String getTeamName();
    Instant getDateCreated();

    String getSessionName();
    Instant getDateSessionCreated();

    Long getStatsId();
    Integer getTeamScore();
    Integer getEntropy();
    Integer getTeamRating();
    Instant getDateStatsCreated();

}
