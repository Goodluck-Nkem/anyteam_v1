package nkemrocks.anyteam_v1.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.util.Date;

import static nkemrocks.anyteam_v1.util.CookieUtil.TOKEN_AGE_SECONDS;

@Service
public class Jwt_Service {

    private final String SECRET = "anyteam_jwt_secret_1234567890_abcdefghijklm_$$_##_@@_nopqrstuvwxyz_2026";

    public String generateToken(String uniqueName, String role){
        return Jwts.builder()
                .setSubject(uniqueName)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + (TOKEN_AGE_SECONDS * 1000)))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .compact();
    }

    public  String extractUniqueName(String token){
        return Jwts.parserBuilder()
                .setSigningKey(SECRET.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String extractRole(String token){
        return (String) Jwts.parserBuilder()
                .setSigningKey(SECRET.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role");
    }

}
