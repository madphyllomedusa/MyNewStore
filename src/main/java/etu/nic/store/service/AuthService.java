package etu.nic.store.service;

import etu.nic.store.model.dto.JwtAuthenticationResponse;
import etu.nic.store.model.dto.SignInRequest;
import etu.nic.store.model.dto.SignUpRequest;


/**
 * Сервис для управления аутентификацией и регистрацией пользователей.
 */
public interface AuthService {

    /**
     * Вход пользователя в систему.
     *
     * @param signInRequest объект запроса, содержащий email и пароль пользователя
     * @return JwtAuthenticationResponse с JWT токеном
     * @throws etu.nic.store.exceptionhandler.NotFoundException если пользователь не найден
     * @throws etu.nic.store.exceptionhandler.BadRequestException если введён неверный пароль
     */
    JwtAuthenticationResponse login(SignInRequest signInRequest);

    /**
     * Регистрация нового пользователя.
     *
     * @param signUpRequest объект запроса с данными для регистрации пользователя
     * @return JwtAuthenticationResponse с JWT токеном
     * @throws etu.nic.store.exceptionhandler.BadRequestException если пароли не совпадают
     * @throws etu.nic.store.exceptionhandler.BadRequestException если email уже используется
     */
    JwtAuthenticationResponse register(SignUpRequest signUpRequest);
}
