package nkemrocks.anyteam_v1.mapper;

import nkemrocks.anyteam_v1.dto.sysConfig.response.SysConfig_ResponseDTO;
import nkemrocks.anyteam_v1.entity.Session_Entity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SysConfig_Mapper {
    public SysConfig_ResponseDTO toResponseDTO(
            Session_Entity configSession,
            List<String> skills
    ) {
        return new SysConfig_ResponseDTO(
                configSession.getTtl(),
                configSession.getDateCreated(),
                skills
        );
    }
}
