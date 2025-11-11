package in.garvit.tasks.taskSecurityConfig;

import org.springframework.http.HttpHeaders;

public final class JwtConstant {

    private JwtConstant() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static final String JWT_HEADER = HttpHeaders.AUTHORIZATION;
}
