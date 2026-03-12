package nkemrocks.anyteam_v1;

/**
 * This is a utility class, so All methods are expected to be public static, so that an instance is not required
 */
public class GlobalUtil {

    /**
     * custom trim utility, returns null or trimmed string
     */
    public static String trim(String s) {
        return s != null ? s.trim() : null;
    }

    /**
     * custom trimAndLower utility, returns null or trimmed lowered string
     */
    public static String trimAndLower(String s) {
        return s != null ? s.trim().toLowerCase() : null;
    }

    /**
     * Hardcoded skillsArray, intended to be fixed and exactly 10 skills
     */
    public static final String ART = "art";
    public static final String BIOLOGY = "biology";
    public static final String HISTORY = "history";
    public static final String LANGUAGE = "language";
    public static final String LOGIC = "logic";
    public static final String MATH = "math";
    public static final String MUSIC = "music";
    public static final String SPELLING = "spelling";
    public static final String SPORT = "sport";
    public static final String TECHNOLOGY = "technology";
    public static String creationSessionName = "<Create>";
}
