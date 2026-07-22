package com.prayana.prayana.securityconfig;


import com.prayana.prayana.entity.MyUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class JWTUtils {

    private static final String secretKey = "12924D8E77907FDE5D5FEF873E304F74B204061DB3F1C03925619EDEFAB6A888D29E7E1DC86EFD5804AB20B18D3B1240653D39E5FB4FD98A58678A8C651A0FA1";
    private static final long validTime = TimeUnit.MINUTES.toMillis(5);


    public String generateToken(MyUser myUser) {

        //claims are used to store additional details in JWT
        // like userRoles userNames and many more
        Map<String, Set<String>> claims = new HashMap<>();
        Set<String> myUserRoles =  myUser.getRoles().stream().map(
                user -> user.getRoleName()
        ).collect(Collectors.toSet());
        claims.put("UserRoles", myUserRoles);

        return Jwts.builder()
                .claims(claims)
                .subject(myUser.getEmail())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(validTime)))
                .signWith(generateKey())
                .compact();
    }

    private SecretKey generateKey() {

        byte[] decodeKey = Base64.getDecoder().decode(secretKey);

        return Keys.hmacShaKeyFor(decodeKey);
    }

    public String extractUsername(String jwt) {

        Claims claims = this.getClaims(jwt);
        return claims.getSubject();
    }

    public Claims getClaims(String jwt) {

        return Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }

    public boolean isTokenValid(String jwt) {

        Claims claims = this.getClaims(jwt);
        return claims.getExpiration().after(Date.from(Instant.now()));
    }
}
