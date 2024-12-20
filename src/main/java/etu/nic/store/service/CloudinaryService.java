package etu.nic.store.service;

import org.springframework.web.multipart.MultipartFile;


/**
 * Сервис для загрузки изображений в облачное хранилище (Cloudinary).
 */
public interface CloudinaryService {

    /**
     * Загружает файл в облачное хранилище и возвращает URL загруженного изображения.
     *
     * @param file загружаемый файл
     * @return URL загруженного изображения
     * @throws etu.nic.store.exceptionhandler.BadRequestException если файл пуст или не может быть загружен
     */
    String upload(MultipartFile file);
}
