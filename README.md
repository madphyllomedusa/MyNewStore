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
```
Http.Post     /product         Добавляет один продукт
              /product/bulk    Добавляет сразу несколько продуктов
Http.Put      /product/{id}    Изменяет продукт по id
Http.Delete   /product/{id}    Удаляет продукт(Реализованно мягкое удаление, при котором продукт не удаляется из бд, а поменчается флагом Deleted
```


### Остановка приложения:

```
docker-compose down
```

### API Документация

#### Автоматически сгенеррированный swager.
```
http://localhost:8080/swagger-ui/index.html
```
