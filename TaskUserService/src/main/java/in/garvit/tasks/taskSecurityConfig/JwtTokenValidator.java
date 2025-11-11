package in.garvit.tasks.taskSecurityConfig;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtTokenValidator extends OncePerRequestFilter {

	private static final Logger log = LoggerFactory.getLogger(JwtTokenValidator.class);

	private final JwtProvider jwtProvider;

	public JwtTokenValidator(JwtProvider jwtProvider) {
		this.jwtProvider = jwtProvider;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {
		String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (!StringUtils.hasText(header) || !header.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		try {
			SecurityContextHolder.getContext().setAuthentication(jwtProvider.buildAuthentication(header));
		} catch (BadCredentialsException ex) {
			log.warn("JWT validation failed: {}", ex.getMessage());
			SecurityContextHolder.clearContext();
		}
		filterChain.doFilter(request, response);
	}
}

