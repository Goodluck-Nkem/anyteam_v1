package nkemrocks.anyteam_v1.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import nkemrocks.anyteam_v1.exception.PolicyException;
import nkemrocks.anyteam_v1.service.Jwt_Service;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final Jwt_Service jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String token = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token != null) {
            try {
                var auth = new UsernamePasswordAuthenticationToken(
                        jwtService.extractUniqueName(token),
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + jwtService.extractRole(token)))
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception e) {
                throw new PolicyException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "JWT filter context failed!"
                );
            }
        }

        filterChain.doFilter(request, response);
    }
}
