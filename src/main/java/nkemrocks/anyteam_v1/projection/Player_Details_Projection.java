package nkemrocks.anyteam_v1.projection;

import java.time.Instant;
import java.util.UUID;

public interface Player_Details_Projection {
    UUID getPlayerId();
    String getUserName();
    String getFirstName();
    String getLastName();
    Instant getDateCreated();
    Instant getDateUpdated();

    String getSessionName();
    Instant getDateSessionCreated();

    Long getSkillId();
    String getSkillName();
    Integer getSkillRating();
}
