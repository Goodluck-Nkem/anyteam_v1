package nkemrocks.anyteam_v1.projection;

import java.time.Instant;

public interface Result_Details_Projection {

    Long getResultId();
    Integer getScore();
    Integer getEntropy();
    Instant getDateResultCreated();

    String getSessionName();

    String getTeamName();

    String getPlayerName();

}
