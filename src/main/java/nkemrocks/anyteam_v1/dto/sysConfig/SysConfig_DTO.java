package nkemrocks.anyteam_v1.dto.sysConfig;

import jakarta.validation.constraints.NotNull;

public record SysConfig_DTO(
        @NotNull(message = ERROR_NULL_TTL)
        Integer TTL
) {
    private static final String ERROR_NULL_TTL = "Time-To-Live (TTL) field cannot be null!";
}
