package in.garvit.tasks.taskSecurityConfig;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "task.jwt")
public class JwtProperties {

	@NotBlank
	private String secret = "change-me-change-me-change-me-change-me-123456";

	@Min(1)
	private long accessTokenTtlMinutes = 1440L;

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public long getAccessTokenTtlMinutes() {
		return accessTokenTtlMinutes;
	}

	public void setAccessTokenTtlMinutes(long accessTokenTtlMinutes) {
		this.accessTokenTtlMinutes = accessTokenTtlMinutes;
	}
}
