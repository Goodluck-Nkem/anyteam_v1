package nkemrocks.anyteam_v1.dto.session.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import nkemrocks.anyteam_v1.GlobalUtil;
import org.hibernate.validator.constraints.Length;

import java.util.Set;
import java.util.stream.Collectors;

import static nkemrocks.anyteam_v1.GlobalUtil.*;

public record Session_Create_RequestDTO(
        @NotBlank(message = ERROR_BLANK_NAME)
        @Length(max = 255, message = ERROR_NAME_LENGTH)
        String sessionName,

        @NotBlank(message = ERROR_NULL_TTL)
        Integer ttl,

        @NotNull(message = ERROR_NULL_REQUIREMENTS)
        @Size(min = 4, max = 4, message = ERROR_SIZE_REQUIREMENTS)
        Set<@NotBlank(message = ERROR_BLANK_NAME) String> requirements
) {
    private static final String ERROR_NULL_TTL = "Time-To-Live (ttl) field cannot be null!";
    private static final String ERROR_BLANK_NAME = "Session name cannot be blank!";
    private static final String ERROR_NAME_LENGTH = "Length can't exceed 255!";
    private static final String ERROR_NULL_REQUIREMENTS = "Requirements cannot be null!";
    private static final String ERROR_SIZE_REQUIREMENTS = "There must be exactly 4 requirements selected!";

    /* setup some fields just before validation check */
    public Session_Create_RequestDTO {
        sessionName = trimAndLower(sessionName);
        if (requirements != null)
            requirements = requirements
                    .stream()
                    .map(GlobalUtil::trimAndLower)
                    .collect(Collectors.toSet());
    }
}
