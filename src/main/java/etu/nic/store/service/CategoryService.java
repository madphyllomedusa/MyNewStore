package etu.nic.store.service;

import etu.nic.store.model.dto.CategoryDto;


import java.util.List;

/**
 * Сервис для управления категориями товаров.
 */
public interface CategoryService {

    /**
     * Добавляет новую категорию.
     *
     * @param categoryDto DTO категории, содержащий информацию о ней
     * @return DTO созданной категории
     * @throws etu.nic.store.exceptionhandler.BadRequestException если DTO невалидно (имя категории не указано или пустое)
     */
    CategoryDto addCategory(CategoryDto categoryDto);

    /**
     * Обновляет существующую категорию по её идентификатору.
     *
     * @param id          идентификатор категории
     * @param categoryDto DTO с обновлённой информацией о категории
     * @return DTO обновлённой категории
     * @throws etu.nic.store.exceptionhandler.BadRequestException если DTO невалидно или id некорректен
     * @throws etu.nic.store.exceptionhandler.NotFoundException если категория с указанным id не найдена
     */
    CategoryDto updateCategory(Long id, CategoryDto categoryDto);

    /**
     * "Удаляет" категорию (помечает её удалённой) по идентификатору.
     *
     * @param id идентификатор категории
     * @throws etu.nic.store.exceptionhandler.BadRequestException если id некорректен
     * @throws etu.nic.store.exceptionhandler.NotFoundException если категория с указанным id не найдена
     */
    void deleteCategory(Long id);

    /**
     * Возвращает список дочерних категорий для указанной категории.
     *
     * @param id идентификатор родительской категории
     * @return список DTO дочерних категорий
     * @throws etu.nic.store.exceptionhandler.BadRequestException если id некорректен
     * @throws etu.nic.store.exceptionhandler.NotFoundException если категория с указанным id не найдена
     */
    List<CategoryDto> getCategoryChildren(Long id);

    /**
     * Возвращает список идентификаторов категории и всех её подкатегорий.
     *
     * @param id идентификатор категории
     * @return список идентификаторов категории и её подкатегорий
     * @throws etu.nic.store.exceptionhandler.BadRequestException если id некорректен
     * @throws etu.nic.store.exceptionhandler.NotFoundException если категория с указанным id не найдена
     */
    List<Long> getCategoryAndSubcategoryIds(Long id);
}
