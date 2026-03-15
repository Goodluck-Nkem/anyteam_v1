package nkemrocks.anyteam_v1.dto.player.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import java.util.UUID;

import static nkemrocks.anyteam_v1.util.GlobalUtil.trim;

public record Player_Update_RequestDTO(

        @NotNull(message = ERROR_NULL_PLAYER)
        UUID playerId,

        @NotBlank(message = ERROR_NAME_BLANK)
        @Length(min = 2, max = 255, message = ERROR_FIRST_LAST_LENGTH)
        String firstName,

        @NotBlank(message = ERROR_NAME_BLANK)
        @Length(min = 2, max = 255, message = ERROR_FIRST_LAST_LENGTH)
        String lastName
) {
    private static final String ERROR_NULL_PLAYER = "Player ID must be provided!";
    private static final String ERROR_NAME_BLANK = "First/Last name can't be blank!";
    private static final String ERROR_FIRST_LAST_LENGTH = "First/Last name length must be between 2 and 255 inclusive";

    /* setup certain fields before validation checks */
    public Player_Update_RequestDTO {
        firstName = trim(firstName);
        lastName = trim(lastName);
    }
}
