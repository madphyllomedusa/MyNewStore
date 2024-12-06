package etu.nic.store.service.impl;

import etu.nic.store.exceptionhandler.NotFoundException;
import etu.nic.store.model.entity.User;
import etu.nic.store.repository.UserRepository;
import etu.nic.store.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;


    public Long extractUserIdFromContext() {
        Object principal = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        if (principal instanceof String) {
            String email = (String) principal;
            return findUserIdByEmail(email);
        }
        return null;
    }

    public Long findUserIdByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Пользователь с email " + email + " не найден"));
        return user.getId();
    }
}
