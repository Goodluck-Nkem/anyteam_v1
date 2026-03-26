package nkemrocks.anyteam_v1.config;

import nkemrocks.anyteam_v1.util.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity httpSecurity,
            JwtFilter jwtFilter) throws RuntimeException {
        return httpSecurity.csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers("/api/v1/auth/*/login",
                                "/api/v1/team/create",
                                "/api/v1/player/create"
                                )
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exConfig -> exConfig
                        .authenticationEntryPoint((req, res, e)->{
                            res.setStatus(401);
                            res.setContentType("application/json");
                            res.getWriter().write("""
                                    { "error" : "Unauthorized, Invalid or missing token!" }""");
                        })
                        .accessDeniedHandler((req, res, e)->{
                            res.setStatus(403);
                            res.setContentType("application/json");
                            res.getWriter().write("""
                                    { "error" : "Forbidden, access denied!" }""");
                        })
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/auth/*/login",

                                "/api/v1/*/results",
                                "/api/v1/*/find",
                                "/api/v1/*/search",
                                "/api/v1/*/list",

                                "/api/v1/team/create",

                                "/api/v1/player/create"
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/sysconfig").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/v1/sysconfig").hasRole("ADMIN")
                        .requestMatchers("/api/v1/session/create").hasRole("ADMIN")
                        .requestMatchers("/api/v1/session/update").hasRole("ADMIN")

                        .requestMatchers("/api/v1/team/play").hasRole("TEAM")

                        .requestMatchers("/api/v1/player/update").hasRole("PLAYER")

                        .requestMatchers("/api/**").authenticated()

                        .requestMatchers("/hidden/**").denyAll()

                        .anyRequest().permitAll()
                ).addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
