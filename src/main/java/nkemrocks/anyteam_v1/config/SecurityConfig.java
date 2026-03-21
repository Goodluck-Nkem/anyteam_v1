package nkemrocks.anyteam_v1.config;

import nkemrocks.anyteam_v1.util.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/auth/**",

                                "/api/v1/session/find",
                                "/api/v1/session/search",
                                "/api/v1/session/list",

                                "/api/v1/team/create",
                                "/api/v1/team/results",
                                "/api/v1/team/find",
                                "/api/v1/team/search",
                                "/api/v1/team/list",

                                "/api/v1/player/create",
                                "/api/v1/player/results",
                                "/api/v1/player/find",
                                "/api/v1/player/search",
                                "/api/v1/player/list"
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/sysconfig").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/v1/sysconfig").hasRole("ADMIN")
                        .requestMatchers("/api/v1/session/create").hasRole("ADMIN")
                        .requestMatchers("/api/v1/session/update").hasRole("ADMIN")

                        .requestMatchers("/api/v1/team/play").hasRole("TEAM")

                        .requestMatchers("/api/v1/player/update").hasRole("PLAYER")

                        .anyRequest().authenticated()
                ).addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
