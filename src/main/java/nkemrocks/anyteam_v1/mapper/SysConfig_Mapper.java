package nkemrocks.anyteam_v1.mapper;

import nkemrocks.anyteam_v1.dto.sysConfig.SysConfig_DTO;
import nkemrocks.anyteam_v1.entity.SysConfig_Entity;
import org.springframework.stereotype.Component;

@Component
public class SysConfig_Mapper {
    public SysConfig_DTO toResponseDTO(SysConfig_Entity sysConfig){
        return new SysConfig_DTO(sysConfig.getCreationSession().getTtl());
    }
}
