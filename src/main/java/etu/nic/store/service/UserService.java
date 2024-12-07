package etu.nic.store.service;

/**
 * Сервис для получения информации о пользователях.
 */
public interface UserService {

    /**
     * Извлекает идентификатор текущего пользователя из контекста безопасности.
     * Если пользователь не авторизован, возвращает null.
     *
     * @return идентификатор пользователя или null, если пользователь не авторизован
     */
    Long extractUserIdFromContext();

    /**
     * Находит идентификатор пользователя по его email.
     *
     * @param email email пользователя
     * @return идентификатор пользователя
     * @throws etu.nic.store.exceptionhandler.NotFoundException если пользователь с указанным email не найден
     */
    Long findUserIdByEmail(String email);
}

