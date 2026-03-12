package nkemrocks.anyteam_v1.mapper;

import nkemrocks.anyteam_v1.dto.session.response.Session_Create_ResponseDTO;
import nkemrocks.anyteam_v1.dto.session.response.Session_Fetch_ResponseDTO;
import nkemrocks.anyteam_v1.dto.session.response.Session_Update_ResponseDTO;
import nkemrocks.anyteam_v1.entity.Session_Entity;
import nkemrocks.anyteam_v1.projection.Session_Details_Projection;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Session_Mapper {
    public Session_Create_ResponseDTO toCreate_ResponseDTO(Session_Entity session) {
        return new Session_Create_ResponseDTO(
                session.getId(),
                session.getSessionName(),
                session.getTtl(),
                session.getDateCreated()
        );
    }

    public Session_Fetch_ResponseDTO toFetch_ResponseDTO(
            List<Session_Details_Projection> sessionDetails
    ) {
        List<String> requirements = sessionDetails.stream()
                .map(Session_Details_Projection::getSkillName)
                .toList();

        Session_Details_Projection first = sessionDetails.getFirst();
        return new Session_Fetch_ResponseDTO(
                first.getSessionId(),
                first.getSessionName(),
                first.getTtl(),
                requirements,
                first.getDateCreated(),
                first.getDateUpdated()
        );
    }

    public Session_Update_ResponseDTO toUpdate_ResponseDTO(Session_Entity session) {
        return new Session_Update_ResponseDTO(
                session.getId(),
                session.getSessionName(),
                session.getTtl(),
                session.getDateCreated(),
                session.getDateUpdated()
        );
    }
}
