package etu.nic.store.service;

import etu.nic.store.model.dto.CartDto;

/**
 * Сервис для управления корзиной покупок.
 */
public interface CartService {

    /**
     * Получает текущую корзину для указанного пользователя или по идентификатору сессии (если пользователь не авторизован).
     *
     * @param userId    идентификатор пользователя (может быть null или 0, если пользователь не авторизован)
     * @param sessionId идентификатор сессии для неавторизованного пользователя
     * @return DTO объекта корзины, содержащий список товаров и общую информацию
     * @throws etu.nic.store.exceptionhandler.NotFoundException если корзина или пользователь не найдены
     * @throws etu.nic.store.exceptionhandler.BadRequestException если параметры запроса некорректны
     */
    CartDto getCart(Long userId, String sessionId);

    /**
     * Добавляет товар в корзину. Если товар уже есть в корзине, увеличивает его количество.
     *
     * @param userId    идентификатор пользователя (может быть null или 0, если пользователь не авторизован)
     * @param sessionId идентификатор сессии для неавторизованного пользователя
     * @param productId идентификатор добавляемого товара
     * @param quantity  количество товара для добавления
     * @throws etu.nic.store.exceptionhandler.NotFoundException если указанный товар не найден
     * @throws etu.nic.store.exceptionhandler.BadRequestException если количество товара некорректно (например, меньше 1)
     */
    void addProductToCart(Long userId, String sessionId, Long productId, Integer quantity);

    /**
     * Удаляет товар из корзины.
     *
     * @param userId    идентификатор пользователя (может быть null или 0, если пользователь не авторизован)
     * @param sessionId идентификатор сессии для неавторизованного пользователя
     * @param productId идентификатор удаляемого товара
     * @throws etu.nic.store.exceptionhandler.NotFoundException если указанный товар не найден в корзине
     */
    void removeProductFromCart(Long userId, String sessionId, Long productId);
}
