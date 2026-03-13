package nkemrocks.anyteam_v1;

import com.jayway.jsonpath.JsonPath;
import org.springframework.test.web.servlet.ResultHandler;

import java.util.Locale;

public class TestUtil {
    private static int testCount = 0;

    public static ResultHandler printResponseField(String path) {
        return result -> {
            Object value = JsonPath.read(
                    result.getResponse().getContentAsString(),
                    path
            );
            System.out.printf(Locale.US,
                    "\033[32;1m Field [ %s ] => %s\n\033[0m", path, value);
        };
    }

    public static void printCurrentTestMethodRef() {
        String caller = StackWalker.getInstance()
                .walk(frames -> frames.skip(1)
                        .findFirst()
                        .map(frame -> {
                            String className = frame.getClassName();
                            className = className.substring(className.lastIndexOf('.') + 1);
                            return className + "::" + frame.getMethodName();
                        }))
                .orElse("Unknown Caller");
        System.out.println("\n\033[1m" + (++testCount) + ". \033[33m <" + caller + "> \033[0m");
    }
}
