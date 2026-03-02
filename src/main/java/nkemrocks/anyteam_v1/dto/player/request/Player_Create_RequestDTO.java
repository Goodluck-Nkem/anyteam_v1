package nkemrocks.anyteam_v1.dto.player.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import static nkemrocks.anyteam_v1.GlobalUtil.*;

/* Matches the body of the player_create post request */
public record Player_Create_RequestDTO(

        @NotBlank(message = ERROR_NAME_BLANK)
        @Length(min = 1, max = 255, message = ERROR_PLAYER_LENGTH)
        String playerName,

        @NotBlank(message = ERROR_NAME_BLANK)
        @Length(min = 2, max = 255, message = ERROR_FIRST_LAST_LENGTH)
        String firstName,

        @NotBlank(message = ERROR_NAME_BLANK)
        @Length(min = 2, max = 255, message = ERROR_FIRST_LAST_LENGTH)
        String lastName

) {
    private static final String ERROR_NAME_BLANK = "Name must not be blank!";
    private static final String ERROR_FIRST_LAST_LENGTH = "First/Last name length must be between 2 and 255 inclusive";
    private static final String ERROR_PLAYER_LENGTH = "Player name length must be between 1 and 255 inclusive";

    /* trim certain fields before validation checks */
    public Player_Create_RequestDTO {
        playerName = trim(playerName);
        firstName = trim(firstName);
        lastName = trim(lastName);
    }

}
