package pl.atd.securitytest.formlogin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class AuthProvider implements AuthenticationProvider {
    private static final int ATTEMPTS_LIMIT = 3;

    @Autowired
    private SecurityUserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AttemptsRepository attemptsRepository;
    @Autowired
    private UserRepository userRepository;

    /**
     * returns a fully authenticated object including credentials on successful authentication
     * that will be stored in the SecurityContext
     *
     * @param authentication
     * @return authentication with user name and roles or null when not authenticated
     * @throws AuthenticationException when account locked
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        // extract the user credentials
        String password = (String) authentication.getCredentials();

        // fetch user details from database
        Optional<User> userByUsername = userRepository.findUserByUsername(username);

        // check if user is not locked
        if (userByUsername.isPresent()) {
            if (!userByUsername.get().isAccountNonLocked()) {
                throw new LockedException("Account locked");
            }

            if (passwordEncoder.matches(password, userByUsername.get().getPassword())) {
                processSuccessAttempt(username);
                return new UsernamePasswordAuthenticationToken(username, password, List.of());
            } else {
                processFailedAttempts(username, userByUsername.get());
            }
        }
        return null;
    }

    private void processSuccessAttempt(String username) {
        attemptsRepository.findAttemptsByUsername(username)
                .ifPresent(userAttempts -> {
                    userAttempts.setAttempts(0);
                    attemptsRepository.save(userAttempts);
                });
    }

    private void processFailedAttempts(String username, User user) {
        Optional<Attempts> userAttempts = attemptsRepository.findAttemptsByUsername(username);
        if (userAttempts.isEmpty()) {
            Attempts attempts = new Attempts();
            attempts.setUsername(username);
            attempts.setAttempts(1);
            attemptsRepository.save(attempts);
        } else {
            Attempts attempts = userAttempts.get();
            attempts.setAttempts(attempts.getAttempts() + 1);
            attemptsRepository.save(attempts);

            if (attempts.getAttempts() + 1 > ATTEMPTS_LIMIT) {
                user.setAccountNonLocked(false);
                userRepository.save(user);
                throw new LockedException("Too many invalid attempts. Account is locked!!");
            }
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
