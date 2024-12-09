# Руководство пользователя для Прототипа интернет-магазина.


## Для локального запуска проекта необходимы следующие инструменты:

- **Docker**: версия 20.10 или выше ([скачать Docker](https://www.docker.com/get-started))
- **Docker Compose**: обычно включен в состав Docker Desktop
- **Git**: для клонирования репозитория

Проверьте установку с помощью команд:

```
docker --version
docker-compose --version
```

#### Скопируйте репозиторий:

```
git clone http://git.nic.etu/mpasechnik/1911-online-store-prototipe.git
cd 1911-online-store-prototipe
```
#### Запустите приложение с помощью Docker Compose:

```
docker-compose up --build
```

### Доступ к api предоставлятся по этому адресу:

```
http://localhost:8080
```
По умолчанию при запуске приложения происходит небольшое заполнение базы данных

#### Вы можете авторизоваться пол двумя ролями на 
```
http://localhost:8080/auth/login
```
``` 
{
    "email": "admin@example.com", //admin
    "password": "admin"
}
```
или
```
{
    "email": "user@example.com", //user
    "password": "user"
}
```
При успешной авторизации будет выведен токен в формате

<img width="996" alt="image" src="https://github.com/user-attachments/assets/8c93b38e-84a8-48a7-b81a-deb7636be41f">

### Использование некоторых контроллеров ограничено ролью "ROLE_ADMIN" для того чтобы обычные пользователи не могли влиять на магазин изнутри.
Для этого нужно передавать Authorization Bearer Token
```
Http.Post     /product         Добавляет один продукт
              /product/bulk    Добавляет сразу несколько продуктов
Http.Put      /product/{id}    Изменяет продукт по id
Http.Delete   /product/{id}    Удаляет продукт(Реализованно мягкое удалениe)
```
```
Http.Post     /category         Добавляет одну категорию
              /category/bulk    Добавляет сразу несколько категорий
Http.Put      /category/{id}    Изменяет категорию по id
Http.Delete   /category/{id}    Удаляет продукт(Реализованно мягкое удалениe)
```
Также поддерживается доступ к загрузке изображений
```
http://localhost:8080/product/image
```
Необходимо будет загрузить файл в body form-data
<img width="998" alt="image" src="https://github.com/user-attachments/assets/1fce3eb0-5e96-402e-a59c-1f273bcd25d0">
А также добавить токен авторизации
<img width="1009" alt="image" src="https://github.com/user-attachments/assets/408dcf06-9ceb-463b-84e1-e97c17dd9f9b">
При попытке пользователя обратиться к закрытому методу будет выведена ошибка 403 Forbidden (Доступ запрещен).
<img width="1006" alt="image" src="https://github.com/user-attachments/assets/e4fcc0d7-5ebb-4361-b82e-af494804c82e">

Два главных эндпоинта проекта

```
http://localhost:8080/product/category/{id}    // Получение внутри продукта катеторию и под категорию
```
<img width="981" alt="image" src="https://github.com/user-attachments/assets/390df63a-5da1-4b7d-bc38-e359e48a1d4e">

также метод поддерживает сортировку по цене (По дефолту стоит по возрастанию)
```
http://localhost:8080/product/category/{id}?sortBy=priceDesc
```
<img width="1053" alt="image" src="https://github.com/user-attachments/assets/fda05aad-081c-44ba-8fd4-1da55c703143">

```
http://localhost:8080/cart/item                 // Добавления товара в существующую корзину или создание корзины и добавление в нее
```
<img width="996" alt="image" src="https://github.com/user-attachments/assets/98a96aa1-c5f4-4f25-8b41-0e2c2e088e13">


### Остановка приложения:

```
docker-compose down
```

### API Документация

#### Автоматически сгенеррированный swagger.
```
http://localhost:8080/swagger-ui/index.html
```
