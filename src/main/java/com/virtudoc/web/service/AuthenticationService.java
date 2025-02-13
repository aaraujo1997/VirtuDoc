package com.virtudoc.web.service;

import com.virtudoc.web.entity.UserAccount;
import com.virtudoc.web.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Provides security and authentication services for user accounts. Although primarily utilized by the Spring
 * Security framework, this class also provides password hashing services. Use this service to change a user's password.
 *
 * @author ARMmaster17
 */
@Service
public class AuthenticationService implements UserDetailsService {
    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private Validator validator;

    /**
     * Used internally by the Spring Security framework. Loads a user by username, and creates a session initiation
     * request using the user data stored in the database. Do not call this method directly.
     * @param username Username of the user requesting a login session.
     * @return User session initiation request.
     * @throws UsernameNotFoundException Username was not found in the database.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserAccount> accounts = userAccountRepository.findByUsername(username);
        if (accounts.size() != 1) {
            throw new UsernameNotFoundException("username not found");
        }
        UserAccount user = accounts.get(0);
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getRole());
        return new User(user.getUsername(), user.getPassword(), Arrays.asList(grantedAuthority));
    }

    /**
     * Takes an unsaved UserAccount object, and saves it into the database. Performs data validation and password
     * hashing.
     * @param user Unsaved UserAccount object.
     * @throws Exception Username already exists.
     */
    public void RegisterNewAccount(UserAccount user) throws Exception {
        if (userAccountRepository.findByUsername(user.getUsername()).size() != 0) {
            throw new Exception("user with specified username already exists");
        }

        validateUserAccount(user);

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        userAccountRepository.save(user);
    }

    /**
     * Updates a UserAccount with a new password. Performs password hashing before saving.
     * @param user Existing UserAccount object with new password stored in password field.
     */
    public void SetNewUserPassword(UserAccount user) throws Exception {
        validateUserAccount(user);

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        userAccountRepository.save(user);
    }

    private void validateUserAccount(UserAccount user) throws Exception {
        Set<ConstraintViolation<UserAccount>> constraintViolations = validator.validate(user);
        if (constraintViolations.size() > 0) throw new Exception("invalid UserAccount object");
    }
}
