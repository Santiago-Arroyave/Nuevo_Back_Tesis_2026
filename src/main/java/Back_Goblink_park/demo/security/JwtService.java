package Back_Goblink_park.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    // =========================================
    // PROPERTIES
    // =========================================

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    // =========================================
    // GENERAR TOKEN
    // =========================================

    public String generateToken(String username) {

        return Jwts.builder()

                .setSubject(username)

                .setIssuedAt(new Date())

                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                        + jwtExpiration
                        )
                )

                .signWith(
                        getSignInKey(),
                        SignatureAlgorithm.HS256
                )

                .compact();
    }

    // =========================================
    // EXTRAER USERNAME
    // =========================================

    public String extractUsername(String token) {

        return extractClaim(
                token,
                Claims::getSubject
        );
    }

    // =========================================
    // EXTRAER CLAIM
    // =========================================

    public <T> T extractClaim(
            String token,
            Function<Claims, T> resolver
    ) {

        final Claims claims =
                extractAllClaims(token);

        return resolver.apply(claims);
    }

    // =========================================
    // EXTRAER CLAIMS
    // =========================================

    private Claims extractAllClaims(String token) {

        return Jwts.parserBuilder()

                .setSigningKey(getSignInKey())

                .build()

                .parseClaimsJws(token)

                .getBody();
    }

    // =========================================
    // VALIDAR TOKEN
    // =========================================

    public boolean isTokenValid(
            String token,
            UserDetails userDetails
    ) {

        final String username =
                extractUsername(token);

        return username.equals(
                userDetails.getUsername()
        );
    }

    // =========================================
    // SIGNING KEY
    // =========================================

    private Key getSignInKey() {

        byte[] keyBytes =
                Decoders.BASE64.decode(secretKey);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}