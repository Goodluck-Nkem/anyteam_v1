package nkemrocks.anyteam_v1.dto.player.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import nkemrocks.anyteam_v1.util.GlobalUtil;
import org.hibernate.validator.constraints.Length;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static nkemrocks.anyteam_v1.util.GlobalUtil.*;

/* Matches the body of the player_create post request */
public record Player_Create_RequestDTO(

        @NotBlank(message = ERROR_NAME_BLANK)
        @Length(min = 1, max = 255, message = ERROR_PLAYER_LENGTH)
        String userName,

        @NotBlank(message = ERROR_NAME_BLANK)
        @Length(min = 2, max = 255, message = ERROR_FIRST_LAST_LENGTH)
        String firstName,

        @NotBlank(message = ERROR_NAME_BLANK)
        @Length(min = 2, max = 255, message = ERROR_FIRST_LAST_LENGTH)
        String lastName,

        @Size(max = 10, message = ERROR_SIZE_FOCUS_SET)
        Set<@NotBlank(message = ERROR_SKILL_NAME_BLANK) String> skillFocus

) {
    private static final String ERROR_NAME_BLANK = "Name can't be blank!";
    private static final String ERROR_FIRST_LAST_LENGTH = "First/Last name length must be between 2 and 255 inclusive";
    private static final String ERROR_PLAYER_LENGTH = "Player's user name length must be between 1 and 255 inclusive";
    private static final String ERROR_SIZE_FOCUS_SET = "Maximum of 10 skills to can't be exceeded!";
    private static final String ERROR_SKILL_NAME_BLANK = "Skill name can't be blank!";

    /* setup certain fields before validation checks */
    public Player_Create_RequestDTO {
        userName = trimAndLower(userName);
        firstName = trim(firstName);
        lastName = trim(lastName);
        skillFocus = skillFocus == null ?
                new HashSet<>() :
                skillFocus
                        .stream()
                        .map(GlobalUtil::trimAndLower)
                        .collect(Collectors.toSet());
    }

}
