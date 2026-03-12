package nkemrocks.anyteam_v1.dto.sysConfig.request;

import jakarta.validation.constraints.NotNull;

public record SysConfig_RequestDTO(
        @NotNull(message = ERROR_NULL_TTL)
        Integer ttl
) {
    private static final String ERROR_NULL_TTL = "Time-To-Live (ttl) field cannot be null!";
}
