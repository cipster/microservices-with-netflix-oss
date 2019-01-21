package nl.globalorange.compliancewise.auth.service;

import nl.globalorange.compliancewise.auth.domain.model.User;
import nl.globalorange.compliancewise.auth.domain.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void create(User user) {

        userRepository.findById(user.getUsername()).ifPresent(existingUser -> {
            throw new IllegalArgumentException("user already exists: " + existingUser.getUsername());
        });

        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);

        userRepository.save(user);

        LOGGER.info("New user has been created: {}", user.getUsername());
    }
}