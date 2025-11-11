package in.garvit.tasks.service;

import in.garvit.tasks.repository.UserRepository;
import in.garvit.tasks.usermodel.User;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CustomerServiceImplementation implements UserDetailsService {

	private static final Logger log = LoggerFactory.getLogger(CustomerServiceImplementation.class);

	private final UserRepository userRepository;

	public CustomerServiceImplementation(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = Optional.ofNullable(userRepository.findByEmail(username))
			.orElseThrow(() -> new UsernameNotFoundException("User not found with this email " + username));
		String role = StringUtils.hasText(user.getRole()) ? user.getRole() : "ROLE_CUSTOMER";
		List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
		log.debug("Loaded user {} with authorities {}", username, authorities);
		return new org.springframework.security.core.userdetails.User(
			user.getEmail(),
			user.getPassword(),
			authorities);
	}
}
