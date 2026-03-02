package nkemrocks.anyteam_v1.mapper;

import nkemrocks.anyteam_v1.dto.session.response.Session_Create_ResponseDTO;
import nkemrocks.anyteam_v1.entity.Session_Entity;
import org.springframework.stereotype.Component;

@Component
public class Session_Mapper {
    public Session_Create_ResponseDTO toCreate_ResponseDTO(Session_Entity session){
        return new Session_Create_ResponseDTO(
                session.getId(),
                session.getSessionName(),
                session.getTtl(),
                session.getDateCreated()
        );
    }
}
