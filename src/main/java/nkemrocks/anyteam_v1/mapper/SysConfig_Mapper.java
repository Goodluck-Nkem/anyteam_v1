package nkemrocks.anyteam_v1.mapper;

import jakarta.annotation.Nullable;
import nkemrocks.anyteam_v1.dto.sysConfig.response.SysConfig_ResponseDTO;
import nkemrocks.anyteam_v1.entity.SysConfig_Entity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SysConfig_Mapper {
    public SysConfig_ResponseDTO toResponseDTO(@Nullable SysConfig_Entity sysConfig, List<String> skills) {
        return new SysConfig_ResponseDTO(
                sysConfig == null ? null : sysConfig.getCreationSession().getTtl(),
                skills
        );
    }
}
