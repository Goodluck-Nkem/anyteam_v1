package nkemrocks.anyteam_v1.dto.login;

import jakarta.validation.constraints.NotBlank;

import static nkemrocks.anyteam_v1.util.GlobalUtil.trimAndLower;

public record Login_RequestDTO(
        @NotBlank(message = ERROR_BLANK_UNIQUE_NAME)
        String uniqueName,
        @NotBlank(message = ERROR_BLANK_PASSWORD)
        String password
) {
    private static final String ERROR_BLANK_UNIQUE_NAME = "Unique name cannot be blank!";
    private static final String ERROR_BLANK_PASSWORD = "Password cannot be blank!";

    public Login_RequestDTO {
        uniqueName = trimAndLower(uniqueName);
    }
}
