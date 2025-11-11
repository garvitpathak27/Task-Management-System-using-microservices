package in.garvit.tasks.service;

import in.garvit.tasks.exception.UserException;
import in.garvit.tasks.repository.UserRepository;
import in.garvit.tasks.request.SignupRequest;
import in.garvit.tasks.usermodel.User;
import in.garvit.tasks.taskSecurityConfig.JwtProvider;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserServiceImplementation implements UserService {

	private static final Logger log = LoggerFactory.getLogger(UserServiceImplementation.class);

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;

	public UserServiceImplementation(UserRepository userRepository,
		PasswordEncoder passwordEncoder,
		JwtProvider jwtProvider) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtProvider = jwtProvider;
	}

	@Override
	public User register(SignupRequest request) throws UserException {
		if (userRepository.findByEmail(request.getEmail()) != null) {
			throw new UserException("Email is already associated with another account");
		}
		User user = new User();
		user.setFullName(request.getFullName());
		user.setEmail(request.getEmail());
		user.setMobile(request.getMobile());
		String role = StringUtils.hasText(request.getRole()) ? request.getRole() : user.getRole();
		user.setRole(role);
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		log.info("Registering user with email {}", user.getEmail());
		return userRepository.save(user);
	}

	@Override
	public User findUserProfileByJwt(String jwt) throws UserException {
		try {
			String email = jwtProvider.extractEmail(jwt);
			if (!StringUtils.hasText(email)) {
				throw new UserException("Invalid authentication token");
			}
			log.debug("Resolved user email {} from JWT", email);
			return findUserByEmail(email);
		} catch (IllegalArgumentException ex) {
			log.warn("Failed to parse JWT token: {}", ex.getMessage());
			throw new UserException("Invalid authentication token");
		}
	}

	@Override
	public User findUserByEmail(String email) throws UserException {
		log.debug("Looking up user by email {}", email);
		User user = userRepository.findByEmail(email);
		if (user == null) {
			throw new UserException("User not found with email " + email);
		}
		return user;
	}

	@Override
	public User findUserById(String userId) throws UserException {
		log.debug("Looking up user by id {}", userId);
		Optional<User> opt = userRepository.findById(userId);
		return opt.orElseThrow(() -> new UserException("User not found with id " + userId));
	}

	@Override
	public List<User> findAllUsers() {
		log.debug("Fetching all users from repository");
		return userRepository.findAll();
	}
}
