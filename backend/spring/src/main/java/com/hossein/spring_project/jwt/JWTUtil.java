package com.hossein.spring_project.jwt;

import java.time.Instant;
import static java.time.temporal.ChronoUnit.DAYS;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKey;

// import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTUtil {

    // @Value("${jwt.token.secret}")
    private static final String SECRET_KEY = "hkh_is_REAL_!^!#$^!$^!$%!#$%%#%^*$*$%&@#@#%$%!#^$@$&@^*#%(#&(#%^*@&%^!#$^!$#^@%&@$&@$^*@$^&%^@#$%))_hkh_is_REAL_!^!#$^!$^!$%!#$%%#%^*$*$%&@#@#%$%!#^$@$&@^*#%(#&(#%^*@&%^!#$^!$#^@%&@$&@$^*@$^&%^@#$%))_hkh_is_REAL_!^!#$^!$^!$%!#$%%#%^*$*$%&@#@#%$%!#^$@$&@^*#%(#&(#%^*@&%^!#$^!$#^@%&@$&@$^*@$^&%^@#$%))";

    public String issueToken(String subject){
        return issueToken(subject,Map.of());
    }
    public String issueToken(String subject, String ...scopes){
        return issueToken(subject,Map.of("scopes",scopes));
    }
    public String issueToken(String subject, List<String> scopes){
        return issueToken(subject,Map.of("scopes",scopes));
    }
    public String issueToken(
            String subject,
            Map<String,Object> claims){
        String token = Jwts
            .builder()
            .claims(claims)
            .subject(subject)
            .issuer("https://netpick.ir")
            .issuedAt(Date.from(Instant.now()))
            .expiration(Date.from(Instant.now().plus(3,DAYS)))
            .signWith(getSigningKey())
            .compact();

        return token;
    }
    
    public String getSubject(String token){
        return getClaims(token).getSubject();
    }
    private Claims getClaims(String token) {
        Claims claims = Jwts
            .parser()
            .verifyWith((SecretKey)getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
        return claims;
    }

    private Key getSigningKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
    public boolean isTokenValid(String jwt, String username) {
        String subject = getSubject(jwt);
        return subject.equals(username) && !isTokenExpired(jwt);
    }
    private boolean isTokenExpired(String jwt) {
        Date today = Date.from(
            Instant.now()
        );
        return getClaims(jwt).getExpiration().before(today);
    }


}
