package gestionPro.backend.net.security;
 
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
 
@Component
public class JwtUtil {
 
	// Clé secrète — change cette valeur en production !
    private static final String SECRET = "gestionProSecretKeyTresLongueEtSecurisee2026!!";
    private static final long EXPIRATION = 1000 * 60 * 60 * 24; // 24 heures
 
    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());
 
    // Générer un token pour un utilisateur
    public String generateToken(String username) {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }
 
    // Extraire le username depuis le token
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }
 
    // Vérifier si le token est valide
    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}