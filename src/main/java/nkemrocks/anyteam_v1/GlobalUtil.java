package nkemrocks.anyteam_v1;

import java.time.Instant;

/** This is a utility class, so All methods are expected to be public static, so that an instance is not required */
public class GlobalUtil {

    /** custom trim utility, returns null or trimmed string */
    public static String trim(String s) {
        return s != null ? s.trim() : null;
    }

    /** Hardcoded skillsArray, intended to be fixed and exactly 10 skills */
    public static final String[] skillsArray = {
            "Math",
            "Music",
            "Art",
            "History",
            "Sport",
            "Language",
            "Technology",
            "Spelling",
            "Logic",
            "Biology"
    };

    public static String creationSessionName = "<Create>";
}
