package etu.nic.store.service;

import etu.nic.store.model.dto.ProductDto;
import etu.nic.store.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Сервис для управления товарами.
 */
public interface ProductService {

    /**
     * Добавляет новый продукт.
     *
     * @param productDto DTO продукта с необходимой информацией (название, цена, категории и т.д.)
     * @return DTO созданного продукта
     * @throws etu.nic.store.exceptionhandler.BadRequestException если DTO невалиден (например, цена меньше 0 или пустое имя)
     * @throws etu.nic.store.exceptionhandler.NotFoundException если категории, указанные в DTO, не найдены
     */
    ProductDto addProduct(ProductDto productDto);

    /**
     * Обновляет информацию о существующем продукте.
     *
     * @param id         идентификатор продукта
     * @param productDto DTO с обновлёнными данными о продукте
     * @return DTO обновлённого продукта
     * @throws etu.nic.store.exceptionhandler.BadRequestException если DTO невалиден
     * @throws etu.nic.store.exceptionhandler.NotFoundException если продукт с указанным id не найден или связанные категории не существуют
     */
    ProductDto updateProduct(Long id, ProductDto productDto);

    /**
     * Помечает продукт как удалённый по его идентификатору.
     *
     * @param id идентификатор продукта
     * @return DTO удалённого продукта
     * @throws etu.nic.store.exceptionhandler.NotFoundException если продукт с указанным id не найден
     */
    ProductDto deleteProductById(Long id);

    /**
     * Возвращает продукт по его идентификатору.
     *
     * @param id идентификатор продукта
     * @return DTO продукта
     * @throws etu.nic.store.exceptionhandler.NotFoundException если продукт с указанным id не найден
     */
    ProductDto getProductById(Long id);

    /**
     * Возвращает сущность продукта по его идентификатору.
     * Используется для случаев, когда нужна именно сущность, а не DTO.
     *
     * @param productId идентификатор продукта
     * @return сущность продукта
     * @throws etu.nic.store.exceptionhandler.NotFoundException если продукт с указанным id не найден
     */
    Product getProductEntityById(Long productId);

    /**
     * Возвращает страницу с товарами, принадлежащими указанной категории и её подкатегориям,
     * с возможностью сортировки и постраничного вывода.
     *
     * @param categoryId идентификатор категории
     * @param sortBy     критерий сортировки (например "priceAsc" или "priceDesc")
     * @param pageable   информация о пагинации
     * @return страница DTO товаров
     * @throws etu.nic.store.exceptionhandler.BadRequestException если categoryId некорректен
     * @throws etu.nic.store.exceptionhandler.NotFoundException если категория с указанным id не найдена
     */
    Page<ProductDto> getProductsByCategoryAndSubcategories(Long categoryId, String sortBy, Pageable pageable);
}
