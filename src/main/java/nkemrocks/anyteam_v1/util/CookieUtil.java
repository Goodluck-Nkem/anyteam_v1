package nkemrocks.anyteam_v1.util;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;

public class CookieUtil {

    /* 12hrs */
    public static final long TOKEN_AGE_SECONDS = 12 * 3600;

    public static void addJwtCookie(HttpServletResponse httpServletResponse, String token){
        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(TOKEN_AGE_SECONDS)
                .sameSite("Strict")
                .build();

        httpServletResponse.addHeader("Set-Cookie", cookie.toString());
    }

    public  static void clearCookie(HttpServletResponse httpServletResponse){
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        httpServletResponse.addHeader("Set-Cookie", cookie.toString());
    }

}
