package in.garvit.tasks.taskSecurityConfig;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityConfig {

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http,
		JwtTokenValidator jwtTokenValidator,
		CorsConfigurationSource corsConfigurationSource) throws Exception {
		http.csrf(csrf -> csrf.disable())
			.cors(cors -> cors.configurationSource(corsConfigurationSource))
			.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/auth/**", "/", "/health", "/api/users/health", "/actuator/**").permitAll()
				.requestMatchers("/api/**").authenticated()
				.anyRequest().permitAll())
			.addFilterBefore(jwtTokenValidator, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource(
		@Value("${task.security.allowed-origins:http://localhost:3000}") String allowedOrigins,
		@Value("${task.security.allowed-headers:*}") String allowedHeaders,
		@Value("${task.security.allowed-methods:GET,POST,PUT,PATCH,DELETE,OPTIONS}") String allowedMethods,
		@Value("${task.security.allow-credentials:true}") boolean allowCredentials,
		@Value("${task.security.max-age:3600}") long maxAge
	) {
		return request -> {
			CorsConfiguration configuration = new CorsConfiguration();
			configuration.setAllowedOrigins(splitAndTrim(allowedOrigins));
			configuration.setAllowedHeaders(splitAndTrim(allowedHeaders));
			configuration.setAllowedMethods(splitAndTrim(allowedMethods));
			configuration.setAllowCredentials(allowCredentials);
			configuration.setExposedHeaders(List.of(HttpHeaders.AUTHORIZATION));
			configuration.setMaxAge(maxAge);
			return configuration;
		};
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	private List<String> splitAndTrim(String raw) {
		if (!StringUtils.hasText(raw)) {
			return List.of();
		}
		if ("*".equals(raw.trim())) {
			return List.of("*");
		}
		return Arrays.stream(raw.split(","))
			.map(String::trim)
			.filter(StringUtils::hasText)
			.collect(Collectors.toList());
	}
}
