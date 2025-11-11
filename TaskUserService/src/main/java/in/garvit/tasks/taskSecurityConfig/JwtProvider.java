package in.garvit.tasks.taskSecurityConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.crypto.SecretKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JwtProvider {

    private static final Logger log = LoggerFactory.getLogger(JwtProvider.class);

    private final JwtProperties jwtProperties;

    private SecretKey key;

    public JwtProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @PostConstruct
    void initializeKey() {
        String secret = jwtProperties.getSecret();
        if (!StringUtils.hasText(secret) || secret.length() < 32) {
            throw new IllegalStateException("task.jwt.secret must be at least 32 characters long");
        }
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        log.info("JWT provider initialised with configurable secret and TTL of {} minutes",
            jwtProperties.getAccessTokenTtlMinutes());
    }

    public String generateToken(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String roles = populateAuthorities(authorities);
        Instant now = Instant.now();
        return Jwts.builder()
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plus(jwtProperties.getAccessTokenTtlMinutes(), ChronoUnit.MINUTES)))
            .subject(authentication.getName())
            .claim("email", authentication.getName())
            .claim("authorities", roles)
            .signWith(key)
            .compact();
    }

    public String extractEmail(String token) {
        Claims claims = parseClaims(resolveToken(token));
        return claims.get("email", String.class);
    }

    public Authentication buildAuthentication(String rawToken) {
        Claims claims = parseClaims(resolveToken(rawToken));
        String email = claims.get("email", String.class);
        String authorities = claims.get("authorities", String.class);
        return new UsernamePasswordAuthenticationToken(email, null,
            AuthorityUtils.commaSeparatedStringToAuthorityList(authorities != null ? authorities : ""));
    }

    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<String> auths = new HashSet<>();
        for (GrantedAuthority authority : authorities) {
            auths.add(authority.getAuthority());
        }
        return String.join(",", auths);
    }

    private String resolveToken(String token) {
        if (!StringUtils.hasText(token)) {
            throw new BadCredentialsException("Missing JWT token");
        }
        return token.startsWith("Bearer ") ? token.substring(7) : token;
    }

    private Claims parseClaims(String jwt) {
        try {
            return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
        } catch (JwtException ex) {
            log.debug("Failed to parse JWT token", ex);
            throw new BadCredentialsException("Invalid JWT token", ex);
        }
    }
}
