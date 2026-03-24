package nkemrocks.anyteam_v1.dto.player.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import nkemrocks.anyteam_v1.util.GlobalUtil;
import org.hibernate.validator.constraints.Length;
import tools.jackson.databind.annotation.JsonDeserialize;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static nkemrocks.anyteam_v1.util.GlobalUtil.*;

/* Matches the body of the player_create post request */
public record Player_Create_RequestDTO(

        @NotBlank(message = ERROR_PLAYER_NAME_BLANK)
        @Length(min = 1, max = 255, message = ERROR_PLAYER_LENGTH)
        String playerName,

        @NotBlank(message = ERROR_PASSWORD_BLANK)
        @Length(min = 8, max = 255, message = ERROR_PASSWORD_LENGTH)
        String password,

        @NotBlank(message = ERROR_FIRST_LAST_NAME_BLANK)
        @Length(min = 2, max = 255, message = ERROR_FIRST_LAST_LENGTH)
        String firstName,

        @NotBlank(message = ERROR_FIRST_LAST_NAME_BLANK)
        @Length(min = 2, max = 255, message = ERROR_FIRST_LAST_LENGTH)
        String lastName,

        @JsonDeserialize(as = LinkedHashSet.class)
        @Size(max = 10, message = ERROR_SIZE_FOCUS_SET)
        Set<@NotBlank(message = ERROR_SKILL_NAME_BLANK) String> skillFocus

) {
    private static final String ERROR_FIRST_LAST_NAME_BLANK = "First/Last name can't be blank!";
    private static final String ERROR_FIRST_LAST_LENGTH = "First/Last name length must be between 2 and 255 inclusive";
    private static final String ERROR_PLAYER_LENGTH = "Player's user name length must be between 1 and 255 inclusive";
    private static final String ERROR_SIZE_FOCUS_SET = "Maximum of 10 skills to can't be exceeded!";
    private static final String ERROR_SKILL_NAME_BLANK = "Skill name can't be blank!";
    private static final String ERROR_PASSWORD_BLANK = "Password can't be blank!";
    private static final String ERROR_PLAYER_NAME_BLANK = "Player's playerName can't be blank!";
    private static final String ERROR_PASSWORD_LENGTH = "Password length must be between 8 and 255 inclusive";

    /* setup certain fields before validation checks */
    public Player_Create_RequestDTO {
        playerName = trimAndLower(playerName);
        firstName = trim(firstName);
        lastName = trim(lastName);
        skillFocus = skillFocus == null ?
                new LinkedHashSet<>() :
                skillFocus
                        .stream()
                        .map(GlobalUtil::trimAndLower)
                        .collect(Collectors.toCollection(LinkedHashSet::new));
    }

}
