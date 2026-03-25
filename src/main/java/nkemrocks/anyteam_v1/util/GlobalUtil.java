package nkemrocks.anyteam_v1.util;

import jakarta.persistence.EntityManager;

import java.util.List;

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

    /* manually batched saves */
    public static void entityManagerPersistBatch(List<?> records, EntityManager entityManager) {
        System.out.println(
                "hibernate.jdbc.batch_size: " +
                        entityManager.getEntityManagerFactory()
                                .getProperties()
                                .get("hibernate.jdbc.batch_size")
        );
        for (int i = 0; i < records.size(); i++) {
            entityManager.persist(records.get(i));
            if ((i + 1) % BATCH_SIZE == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }
        entityManager.flush();
        entityManager.clear();
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
    public static final int BATCH_SIZE = 50;
    public static String configSessionName = "<config>";
    public static String ADMIN_NAME = "admin";
    public static String ADMIN_PASSWD = "simple_admin_2026";
}
