package nkemrocks.anyteam_v1.util;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;

public class CookieUtil {
    public static void addJwtCookie(HttpServletResponse httpServletResponse, String token){
        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(24 * 3600)
                .sameSite("Strict")
                .build();

        httpServletResponse.addHeader("Set-Cookie", cookie.toString());
    }

    public  static void clearCookie(HttpServletResponse httpServletResponse){
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();

        httpServletResponse.addHeader("Set-Cookie", cookie.toString());
    }

}
