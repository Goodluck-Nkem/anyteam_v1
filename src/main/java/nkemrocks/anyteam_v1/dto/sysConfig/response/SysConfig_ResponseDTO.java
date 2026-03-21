package nkemrocks.anyteam_v1.dto.sysConfig.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;
import java.util.List;

public record SysConfig_ResponseDTO(
        Long ttl,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd, HH:mm:ss", timezone = "UTC")
        Instant dateCreated,
        List<String> skills
) {
}
