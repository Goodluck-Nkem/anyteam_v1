package nkemrocks.anyteam_v1.dto.sysConfig.response;

import java.util.List;

public record SysConfig_ResponseDTO(
        Integer ttl,
        List<String> skills
) {
}
