package etu.nic.store.service.impl;

import etu.nic.store.config.JwtService;
import etu.nic.store.exceptionhandler.BadRequestException;
import etu.nic.store.exceptionhandler.NotFoundException;
import etu.nic.store.model.dto.JwtAuthenticationResponse;
import etu.nic.store.model.dto.SignInRequest;
import etu.nic.store.model.dto.SignUpRequest;
import etu.nic.store.model.entity.User;
import etu.nic.store.model.enums.Role;
import etu.nic.store.model.mapper.UserMapper;
import etu.nic.store.repository.UserRepository;
import etu.nic.store.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    @Override
    public JwtAuthenticationResponse login(SignInRequest signInRequest) {
        String email = signInRequest.getEmail();
        String password = signInRequest.getPassword();
        logger.info("Attempting to login user");

        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new NotFoundException("Пользователь не найден"));

        if (user == null) {
            logger.error("User with email {} not found", email);
            throw new BadRequestException("Неверный адрес электронной почты");
        }
        String storedPassword = new String(user.getPassword(), StandardCharsets.UTF_8);

        if (!passwordEncoder.matches(password, storedPassword)) {
            logger.error("Incorrect password for user {}", email);
            throw new BadRequestException("Неверный пароль");
        }

        String token = jwtService.generateToken(user.getEmail(), user.getRole());
        logger.info("User successfully logged in");
        return new JwtAuthenticationResponse(token);
    }

    @Override
    public JwtAuthenticationResponse register(SignUpRequest signUpRequest) {
        logger.info("Attempting to register user");
        isEmailNotUnique(signUpRequest.getEmail());
        boolean isPasswordSame = signUpRequest.getPassword().equals(signUpRequest.getConfirmPassword());

        if (!isPasswordSame) {
            logger.error("Passwords do not match");
            throw new BadRequestException("Пароли не совпадают");
        }

        String hashedPassword = passwordEncoder.encode(signUpRequest.getPassword());
        byte[] hashedPasswordBytes = hashedPassword.getBytes(StandardCharsets.UTF_8);

        User user = userMapper.fromSignUpRequest(signUpRequest);
        user.setPassword(hashedPasswordBytes);
        user.setRole(Role.ROLE_USER);

        User savedUser = userRepository.save(user);
        String token = jwtService.generateToken(savedUser.getEmail(), savedUser.getRole());
        logger.info("User successfully registered at {}", savedUser.getCreatedTime());
        return new JwtAuthenticationResponse(token);
    }

    private void isEmailNotUnique(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            logger.error("User with email {} already exists", email);
            throw new BadRequestException("Пользователь с таким email " + email + " уже зарегистрирован");
        }
    }
}
