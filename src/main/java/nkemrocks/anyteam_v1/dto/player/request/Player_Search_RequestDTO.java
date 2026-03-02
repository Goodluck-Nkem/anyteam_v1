package nkemrocks.anyteam_v1.dto.player.request;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import static nkemrocks.anyteam_v1.GlobalUtil.*;

/** Matches the body of the player_searchByName post request */
public record Player_Search_RequestDTO(

        @NotBlank(message = ERROR_NAME_BLANK)
        @Length(min = 1, max = 255, message = ERROR_PLAYER_LENGTH)
        String playerName

) {
    private static final String ERROR_NAME_BLANK = "Player name must not be blank!";
    private static final String ERROR_PLAYER_LENGTH = "Player name length must be between 1 and 255 inclusive";

    /* trim certain fields before validation checks */
    public Player_Search_RequestDTO {
        playerName = trim(playerName);
    }

}
