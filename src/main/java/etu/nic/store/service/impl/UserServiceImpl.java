package etu.nic.store.service.impl;

import etu.nic.store.exceptionhandler.NotFoundException;
import etu.nic.store.repository.UserRepository;
import etu.nic.store.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;


    @Override
    public Long extractUserIdFromContext() {
        Object principal = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        if (!(principal instanceof String)) {
            logger.info("User not logged in");
            return null;
        }

        String email = (String) principal;

        if ("anonymousUser".equalsIgnoreCase(email)) {
            logger.info("Анонимный пользователь");
            return null;
        }

        return findUserIdByEmail(email);
    }

    @Override
    public Long findUserIdByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Пользователь с email " + email + " не найден"))
                .getId();
    }
}
