package nkemrocks.anyteam_v1.projection;

import java.time.Instant;
import java.util.UUID;

public interface Session_Details_Projection {
    UUID getSessionId();
    String getSessionName();
    Long getTtl();
    Instant getDateCreated();
    Instant getDateUpdated();

    String getSkillName();
}
