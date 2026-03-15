package nkemrocks.anyteam_v1.dto.team.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import static nkemrocks.anyteam_v1.util.GlobalUtil.trimAndLower;

public record Team_Create_RequestDTO(
        @NotBlank(message = ERROR_NAME_BLANK)
        @Length(min = 1, max = 255, message = ERROR_NAME_LENGTH)
        String teamName
) {
    private static final String ERROR_NAME_BLANK = "Team name can't be blank!";
    private static final String ERROR_NAME_LENGTH = "Team name length must be inclusively between 1 and 255";

    /* setup certain fields before validation checks */
    public Team_Create_RequestDTO {
        teamName = trimAndLower(teamName);
    }
}
