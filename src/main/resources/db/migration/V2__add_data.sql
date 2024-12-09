INSERT INTO users(first_name, last_name, email, password, role, created_time, blocked_time)
VALUES ('admin', 'admin', 'admin@example.com', '$2a$12$LOJPLgNkMVhalodDC/G5O.UAsIh/cUb06lukL/tsDR5dKZubZ6EjK', 'ROLE_ADMIN', NOW(),null),
       ('user','user','user@example.com','$2a$12$Jx4KI4nKP/WbDPe2PDxA.eTq30CF5HM4FaJNb3ABOH9HgL26Hkn6m', 'ROLE_USER', now(), null);


INSERT INTO categories (id, name, description, parent_id, created_time) VALUES
    (1, 'Смартфоны', 'Современные мобильные устройства для связи, работы и развлечений', NULL, now()),
    (2, 'Apple', 'Смартфоны премиум-класса с инновационным дизайном и передовыми технологиями', 1, now()),
    (3, 'Iphone 14', '', 2, now()),
    (4, 'Iphone 14 Pro', '', 2, now()),
    (5, 'Iphone 14 Pro Max', '', 2, now()),
    (6, 'Iphone 15', '', 2, now()),
    (7, 'Iphone 15 Pro', '', 2, now()),
    (8, 'Iphone 15 Pro Max', '', 2, now()),
    (9, 'Iphone 16', '', 2, now()),
    (10, 'Iphone 16 Pro', '', 2, now()),
    (11, 'Iphone 16 Pro Max', '', 2, now()),
    (12, 'Samsung', 'Смартфоны с высоким качеством сборки и передовыми техническими характеристиками', 1, now()),
    (13, 'Samsung S24', '', 12, now()),
    (14, 'Samsung S24+', '', 12, now()),
    (15, 'Сопутствующие товары', 'Аксессуары для смартфонов, улучшающие их функциональность и удобство использования', 1, now()),
    (16, 'Наушники', 'Беспроводные и проводные наушники для качественного звука и комфортного общения', 15, now()),
    (17, 'Чехлы', 'Защитные и стильные чехлы для смартфонов различных моделей', 15, now()),
    (18, 'Аудио техника', 'Качественные устройства для ценителей звука и меломанов', NULL, now()),
    (19, 'Портативные колонки', 'Компактные и мощные колонки для прослушивания музыки в любом месте', 18, now()),
    (20, 'Наушники', 'Устройства для погружения в музыку с чистым и глубоким звуком', 18, now());


WITH inserted_product1 AS (
    INSERT INTO products (name, description, price, quantity, created_time)
    VALUES (
        'Apple Iphone 14 Blue 128Gb',
        'Apple iPhone 14 128 GB цвета Blue c двумя основными и одной фронтальной камерой поддерживает передачу любых мобильных данных: от 2G до 5G. Корпус прохладного синего цвета выполнен из стекла и алюминия.Смартфон способен записывать видео в качестве 4K, допустимо его погружение в воду на глубину до 6 метров. Вес устройства всего 172 г, дисплей Super Retina XDR (OLED) адаптируется к внешней освещенности, экран отображается ярко и четко при любом уровне света. Также предусмотрена функция Night Shift, которая автоматически снижает нагрузку на глаза при использовании ночью',
        60000,
        50,
        now()
    )
    RETURNING id
),
inserted_category1 AS (
    INSERT INTO product_category (product_id, category_id)
    SELECT id, 3 FROM inserted_product1
),
inserted_parameters1 AS (
    INSERT INTO parameters (name, value)
    VALUES
        ('Цвет', 'Синий'),
        ('Объем памяти', '128GB')
    RETURNING id
),
inserted_product_parameters1 AS (
    INSERT INTO product_parameter (product_id, parameter_id)
    SELECT inserted_product1.id, inserted_parameters1.id
    FROM inserted_product1, inserted_parameters1
),
inserted_images1 AS (
    INSERT INTO product_images (product_id, image_url)
    SELECT inserted_product1.id, urls.image_url
    FROM inserted_product1
    CROSS JOIN (VALUES
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733728530/iphone-14-blue-1_epgo8o.jpg'),
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733728531/iphone-14-blue-2_vnppyy.jpg'),
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733728531/iphone-14-blue-3_o1odss.jpg'),
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733728533/iphone-14-blue-4_xwokwj.png')
    ) AS urls(image_url)
)
SELECT 1;

WITH inserted_product2 AS (
    INSERT INTO products (name, description, price, quantity, created_time)
    VALUES (
        'Apple Iphone 14 Black 128Gb',
        'Компания Apple представила смартфон нового поколения. Встроенный аккумулятор Li-lon стал еще мощнее, обеспечивая 20 часов беспрерывной работы при просмотре видео. Две обновленные основные камеры (12 Мп) позволяют снимать при любом освещении. Предустановлена iOS 16, имеется поддержка 5G, в комплекте с устройством идет кабель с коннекторами Lightning и USB Type-C. Глубокий черный цвет приковывает к себе взгляды, закругленный по краям корпус из стекла и алюминия выглядит лаконично и стильно. Имеется поддержка функции быстрой зарядки, FaceID, Apple Pay.',
        60000,
        22,
        now()
    )
    RETURNING id
),
inserted_category2 AS (
    INSERT INTO product_category (product_id, category_id)
    SELECT id, 3 FROM inserted_product2
),
inserted_parameters2 AS (
    INSERT INTO parameters (name, value)
    VALUES
        ('Цвет', 'Черный'),
        ('Объем памяти', '128GB')
    RETURNING id
),
inserted_product_parameters2 AS (
    INSERT INTO product_parameter (product_id, parameter_id)
    SELECT inserted_product2.id, inserted_parameters2.id
    FROM inserted_product2, inserted_parameters2
),
inserted_images2 AS (
    INSERT INTO product_images (product_id, image_url)
    SELECT inserted_product2.id, urls.image_url
    FROM inserted_product2
    CROSS JOIN (VALUES
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733728531/iphone-14-black-1_hzsujz.jpg'),
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733728530/iphone-14-black-2_nmexfg.jpg'),
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733728530/iphone-14-black-3_kku7h6.jpg'),
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733728533/iphone-14-black-4_zb5hya.png')
    ) AS urls(image_url)
)
SELECT 1;

WITH inserted_product3 AS (
    INSERT INTO products (name, description, price, quantity, created_time)
    VALUES (
        'Apple Iphone 14 Pro Max Space Black 256 Gb',
        'В продукте Apple задействована защита экрана Ceramic Shield, корпус изготовлен из медицинской нержавеющей стали. Заблокированный дисплей показывает необходимую информацию, его не нужно активизировать для просмотра системных уведомлений. Экран Super Retina XDR позволяет пользоваться телефоном при ярком солнечном дне, система камер позволяет устройству стать профессиональным фотоаппаратом. В кинематографическом режиме съемка идет при 24 кадрах в секунду, а экшен-режим позволяет создавать ролики при движении по пересеченной местности.',
        111000,
        3,
        now()
    )
    RETURNING id
),
inserted_category3 AS (
    INSERT INTO product_category (product_id, category_id)
    SELECT id, 5 FROM inserted_product3
),
inserted_parameters3 AS (
    INSERT INTO parameters (name, value)
    VALUES
        ('Цвет', 'Космический Черный'),
        ('Объем памяти', '256GB')
    RETURNING id
),
inserted_product_parameters3 AS (
    INSERT INTO product_parameter (product_id, parameter_id)
    SELECT inserted_product3.id, inserted_parameters3.id
    FROM inserted_product3, inserted_parameters3
),
inserted_images3 AS (
    INSERT INTO product_images (product_id, image_url)
    SELECT inserted_product3.id, urls.image_url
    FROM inserted_product3
    CROSS JOIN (VALUES
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733729139/iphone-14-pro-pro-max-black-1_pjizg7.png'),
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733729137/iphone-14-pro-pro-max-black-2_izrjyb.jpg'),
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733729137/iphone-14-pro-pro-max-black-3_n53sd7.jpg'),
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733729139/iphone-14-pro-pro-max-black-4_bu1cal.png')
    ) AS urls(image_url)
)
SELECT 1;

WITH inserted_product4 AS (
    INSERT INTO products (name, description, price, quantity)
    VALUES (
        'Apple Iphone 14 Pro Deep Purple 512 Gb',
        'IPhone 14 Pro 512 GB в темно-фиолетовом корпусе весит всего 206 г, поддерживает любые способы передачи мобильных данных (включая 5G), оснащен камерой в 48 Мп. Две дополнительные камеры имеют разрешение 12 Мп, оптический и цифровой зум, стабилизатор картинки. Фронтальная камера (7 Мп) теперь с автофокусом. Диагональ экрана — 6,1 дюйма, технология Super Retina XDR (OLED) автоматически регулирует баланс белого, подстраиваясь под освещение. Защищает экран покрытие из стеклокерамики Ceramic Shield. Разрешение OLED-матрицы 2556×1179 пикселей. Аккумулятор Li-lon поддерживает функцию быстрой зарядки. Цвет Deep Purple матовый, притягивающий взгляды, закругленный по краям корпус из стекла и алюминия выглядит лаконично и стильно. Имеется поддержка функции быстрой зарядки, FaceID, Apple Pay.',
        94000,
        7
    )
    RETURNING id
),
inserted_category4 AS (
    INSERT INTO product_category (product_id, category_id)
    SELECT id, 4 FROM inserted_product4
),
inserted_parameters4 AS (
    INSERT INTO parameters (name, value)
    VALUES
        ('Цвет', 'Глубокий Фиолетовый'),
        ('Объем памяти', '512GB')
    RETURNING id
),
inserted_product_parameters4 AS (
    INSERT INTO product_parameter (product_id, parameter_id)
    SELECT inserted_product4.id, inserted_parameters4.id
    FROM inserted_product4, inserted_parameters4
),
inserted_images4 AS (
    INSERT INTO product_images (product_id, image_url)
    SELECT inserted_product4.id, urls.image_url
    FROM inserted_product4
    CROSS JOIN (VALUES
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733730593/iphone-14-pro-pro-max-purple-1_q5m4re.png'),
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733730592/iphone-14-pro-pro-max-purple-2_otnmmd.jpg'),
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733730592/iphone-14-pro-pro-max-purple-3_cnnwco.jpg'),
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733730593/iphone-14-pro-pro-max-purple_ye7et6.png')
    ) AS urls(image_url)
)
SELECT 1;

WITH inserted_product5 AS (
    INSERT INTO products (name, description, price, quantity)
    VALUES (
        'Apple Iphone 15 Blue 128Gb',
        'Apple iPhone 15 на 128GB – это воплощение современных технологий, в устройстве соединены производительный чип A17 Pro, высокая продуктивность. Смартфон идет в титановом корпусе, максимально профессиональный, легкий и крепкий (идеальное сочетание). Смотрится модель элегантно, стильно, подойдет пользователям всех возрастов. За его прочность отвечает титан – сплав с оптимальным соотношением плотности и массы.',
        70000,
        28
    )
    RETURNING id
),
inserted_category5 AS (
    INSERT INTO product_category (product_id, category_id)
    SELECT id, 6 FROM inserted_product5
),
inserted_parameters5 AS (
    INSERT INTO parameters (name, value)
    VALUES
        ('Цвет', 'Голубой'),
        ('Объем памяти', '128GB')
    RETURNING id
),
inserted_product_parameters5 AS (
    INSERT INTO product_parameter (product_id, parameter_id)
    SELECT inserted_product5.id, inserted_parameters5.id
    FROM inserted_product5, inserted_parameters5
),
inserted_images5 AS (
    INSERT INTO product_images (product_id, image_url)
    SELECT inserted_product5.id, urls.image_url
    FROM inserted_product5
    CROSS JOIN (VALUES
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733731051/iphone-15-blue-1_gcklhv.png'),
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733731050/iphone-15-blue-2_rsddb7.png'),
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733731051/iphone-15-blue-3_y8r8wa.png'),
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733731051/iphone-15-all-colors_dpzusm.png')
    ) AS urls(image_url)
)
SELECT 1;

WITH inserted_product6 AS (
    INSERT INTO products (name, description, price, quantity)
    VALUES (
        'Apple Iphone 15 Pink 512Gb',
        'Apple iPhone 15 Pink 512Gb – это воплощение современных технологий, в устройстве соединены производительный чип A17 Pro, высокая продуктивность. Смартфон идет в титановом корпусе, максимально профессиональный, легкий и крепкий (идеальное сочетание). Айфон 15 доступен в глубоком розовом оттенке. Смотрится модель элегантно, стильно, подойдет пользователям всех возрастов. Гарантируем качество и оригинальность смартфона. За его прочность отвечает титан – сплав с оптимальным соотношением плотности и массы.',
        90000,
        1
    )
    RETURNING id
),
inserted_category6 AS (
    INSERT INTO product_category (product_id, category_id)
    SELECT id, 6 FROM inserted_product6
),
inserted_parameters6 AS (
    INSERT INTO parameters (name, value)
    VALUES
        ('Цвет', 'Розовый'),
        ('Объем памяти', '512GB')
    RETURNING id
),
inserted_product_parameters6 AS (
    INSERT INTO product_parameter (product_id, parameter_id)
    SELECT inserted_product6.id, inserted_parameters6.id
    FROM inserted_product6, inserted_parameters6
),
inserted_images6 AS (
    INSERT INTO product_images (product_id, image_url)
    SELECT inserted_product6.id, urls.image_url
    FROM inserted_product6
    CROSS JOIN (VALUES
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733731051/iphone-15-pink-1_crduiq.png'),
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733731049/iphone-15-pink-2_chmwtq.png'),
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733731049/iphone-15-pink-3_xnjw4o.png'),
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733731051/iphone-15-all-colors_dpzusm.png')
    ) AS urls(image_url)
)
SELECT 1;

WITH inserted_product7 AS (
    INSERT INTO products (name, description, price, quantity)
    VALUES (
        'Apple Iphone 15 Pro Natural Titanium 256 Gb',
        'Apple iPhone 15 Pro – новинка, которая сочетает в себе привлекательный дизайн, инновационные технологии и высокий уровень производительности. Телефон в цвете натуральный титан стильно смотрится, подойдет мужчинам и женщинам. Он оснащен мощным чипом A17 Pro, качественной камерой и большим дисплеем (диагональ экрана составляет 6.1 дюйм). Телефон образует иммерсивное широкое пространство для просмотра контента. В разделе представлена версия Про с объемом памяти 256 Гб.',
        115000,
        13
    )
    RETURNING id
),
inserted_category7 AS (
    INSERT INTO product_category (product_id, category_id)
    SELECT id, 7 FROM inserted_product7
),
inserted_parameters7 AS (
    INSERT INTO parameters (name, value)
    VALUES
        ('Цвет', 'Натуральный титан'),
        ('Объем памяти', '256GB')
    RETURNING id
),
inserted_product_parameters7 AS (
    INSERT INTO product_parameter (product_id, parameter_id)
    SELECT inserted_product7.id, inserted_parameters7.id
    FROM inserted_product7, inserted_parameters7
),
inserted_images7 AS (
    INSERT INTO product_images (product_id, image_url)
    SELECT inserted_product7.id, urls.image_url
    FROM inserted_product7
    CROSS JOIN (VALUES
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733731407/iphone-15-natural-titanium-pro-pro-max-1_hhi7tv.png'),
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733731412/iphone-15-natural-titanium-pro-pro-max-2_f86fl1.png'),
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733731405/iphone-15-natural-titanium-pro-pro-max-3_shme97.png'),
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733731405/iphone-15-natural-titanium-pro-pro-max-4_fvmsn2.png')
    ) AS urls(image_url)
)
SELECT 1;

WITH inserted_product8 AS (
    INSERT INTO products (name, description, price, quantity)
    VALUES (
        'Apple Iphone 15 Pro Max White Titanium 512 Gb',
        'Apple iPhone 15 Pro Max предоставляет широкие возможности для отдыха, работы и творчества. Дисплей яркий, оперативная память увеличенная, как и объем встроенного хранилища. Новинка 2023 года идет с дисплеем ProMotion, частота обновлений которого достигает 120 Гц. Диагональ экрана составляет 6.7 дюймов, технология Super Retina XDR (OLED) автоматически регулирует баланс белого, подстраиваясь под освещение. Защищает экран покрытие из стеклокерамики Ceramic Shield. Разрешение OLED-матрицы 2556×1179 пикселей. Аккумулятор Li-lon поддерживает функцию быстрой зарядки. Цвет Deep Purple матовый, притягивающий взгляды, закругленный по краям корпус из стекла и алюминия выглядит лаконично и стильно. Имеется поддержка функции быстрой зарядки, FaceID, Apple Pay.',
        134000,
        2
    )
    RETURNING id
),
inserted_category8 AS (
    INSERT INTO product_category (product_id, category_id)
    SELECT id, 8 FROM inserted_product8
),
inserted_parameters8 AS (
    INSERT INTO parameters (name, value)
    VALUES
        ('Цвет', 'Белый титан'),
        ('Объем памяти', '512GB')
    RETURNING id
),
inserted_product_parameters8 AS (
    INSERT INTO product_parameter (product_id, parameter_id)
    SELECT inserted_product8.id, inserted_parameters8.id
    FROM inserted_product8, inserted_parameters8
),
inserted_images8 AS (
    INSERT INTO product_images (product_id, image_url)
    SELECT inserted_product8.id, urls.image_url
    FROM inserted_product8
    CROSS JOIN (VALUES
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733731686/iphone-15-pro-pro-max-white-titanium-1_ozyhfo.png'),
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733731686/iphone-15-pro-pro-max-white-titanium-2_ayosmk.png'),
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733731685/iphone-15-pro-pro-max-white-titanium-3_k0jqek.png'),
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733731685/iphone-15-pro-pro-max-allcolors_xe0j4d.png')
    ) AS urls(image_url)
)
SELECT 1;

WITH inserted_product9 AS (
    INSERT INTO products (name, description, price, quantity)
    VALUES (
        'Apple Iphone 16 Black 256Gb',
        'iPhone 16 создан вместе с Apple Intelligence, персональной интеллектуальной системой, которая помогает вам писать, выражать себя и выполнять задачи без усилий. Благодаря новаторской защите конфиденциальности вы можете быть уверены, что никто другой не сможет получить доступ к вашим данным — даже Apple. Apple Intelligence разработан для защиты вашей конфиденциальности на каждом этапе . Он интегрирован в ядро ​​iPhone посредством обработки на устройстве. Поэтому он знает о вашей личной информации, не собирая ее.',
        94000,
        1
    )
    RETURNING id
),
inserted_category9 AS (
    INSERT INTO product_category (product_id, category_id)
    SELECT id, 1 FROM inserted_product9
),
inserted_parameters9 AS (
    INSERT INTO parameters (name, value)
    VALUES
        ('Цвет', 'Черный'),
        ('Объем памяти', '256GB')
    RETURNING id
),
inserted_product_parameters9 AS (
    INSERT INTO product_parameter (product_id, parameter_id)
    SELECT inserted_product9.id, inserted_parameters9.id
    FROM inserted_product9, inserted_parameters9
),
inserted_images9 AS (
    INSERT INTO product_images (product_id, image_url)
    SELECT inserted_product9.id, urls.image_url
    FROM inserted_product9
    CROSS JOIN (VALUES
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733732381/iphone-16-black-1_yq9zpo.jpg'),
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733732388/iphone-16-black-2_owsxub.jpg'),
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733732387/iphone-16-black-3_tm4xva.jpg'),
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733732386/iphone-16-black-4_txxr2h.jpg')
    ) AS urls(image_url)
)
SELECT 1;

WITH inserted_product10 AS (
    INSERT INTO products (name, description, price, quantity)
    VALUES (
        'Apple Iphone 16 Pro Desert Titanium 256 Gb',
        'iPhone 16 Pro создан вместе с Apple Intelligence, персональной интеллектуальной системой, которая помогает вам писать, выражать себя и выполнять задачи без усилий. Благодаря новаторской защите конфиденциальности вы можете быть уверены, что никто другой не сможет получить доступ к вашим данным — даже Apple. Apple Intelligence разработан для защиты вашей конфиденциальности на каждом этапе . Он интегрирован в ядро ​​iPhone посредством обработки на устройстве. Поэтому он знает о вашей личной информации, не собирая ее.',
        120000,
        13
    )
    RETURNING id
),
inserted_category10 AS (
    INSERT INTO product_category (product_id, category_id)
    SELECT id, 10 FROM inserted_product10
),
inserted_parameters10 AS (
    INSERT INTO parameters (name, value)
    VALUES
        ('Цвет', 'Песчанный титан'),
        ('Объем памяти', '256GB')
    RETURNING id
),
inserted_product_parameters10 AS (
    INSERT INTO product_parameter (product_id, parameter_id)
    SELECT inserted_product10.id, inserted_parameters10.id
    FROM inserted_product10, inserted_parameters10
),
inserted_images10 AS (
    INSERT INTO product_images (product_id, image_url)
    SELECT inserted_product10.id, urls.image_url
    FROM inserted_product10
    CROSS JOIN (VALUES
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733732385/iphone-16-pro-pro-max-desert-titanium1_utl1cl.jpg'),
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733732385/iphone-16-pro-pro-max-desert-titanium2_axhek9.jpg'),
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733732384/iphone-16-pro-pro-max-desert-titanium3_jmsasm.jpg'),
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733732384/iphone-16-pro-pro-max-allcolors_xe0j4d.jpg')
    ) AS urls(image_url)
)
SELECT 1;

WITH inserted_product11 AS (
    INSERT INTO products (name, description, price, quantity)
    VALUES (
        'Apple Iphone 16 Pro Max White Titanium 512 Gb',
        'iPhone 16 Pro Max создан вместе с Apple Intelligence, персональной интеллектуальной системой, которая помогает вам писать, выражать себя и выполнять задачи без усилий. Благодаря новаторской защите конфиденциальности вы можете быть уверены, что никто другой не сможет получить доступ к вашим данным — даже Apple. Apple Intelligence разработан для защиты вашей конфиденциальности на каждом этапе . Он интегрирован в ядро ​​iPhone посредством обработки на устройстве. Поэтому он знает о вашей личной информации, не собирая ее.',
        134000,
        2
    )
    RETURNING id
),
inserted_category11 AS (
    INSERT INTO product_category (product_id, category_id)
    SELECT id, 11 FROM inserted_product11
),
inserted_parameters11 AS (
    INSERT INTO parameters (name, value)
    VALUES
        ('Цвет', 'Белый титан'),
        ('Объем памяти', '512GB')
    RETURNING id
),
inserted_product_parameters11 AS (
    INSERT INTO product_parameter (product_id, parameter_id)
    SELECT inserted_product11.id, inserted_parameters11.id
    FROM inserted_product11, inserted_parameters11
),
inserted_images11 AS (
    INSERT INTO product_images (product_id, image_url)
    SELECT inserted_product11.id, urls.image_url
    FROM inserted_product11
    CROSS JOIN (VALUES
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733732383/iphone-16-pro-pro-max-white-titanium1_xkmibv.jpg'),
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733732383/iphone-16-pro-pro-max-white-titanium2_idxdhe.jpg'),
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733732381/iphone-16-pro-pro-max-white-titanium3_k0jqek.jpg'),
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733732384/iphone-16-pro-pro-max-allcolors_xe0j4d.jpg')
    ) AS urls(image_url)
)
SELECT 1;

WITH inserted_product12 AS (
    INSERT INTO products (name, description, price, quantity)
    VALUES (
        'AirPods Pro',
        'Гарнитура AirPods Pro MagSafe подойдет для тех, кто ценит качественное звучание музыки и любит заниматься спортом под треки. Благодаря хорошо продуманной форме и силиконовым вкладышам (в комплект входит несколько разных по размеру) наушники не выпадают и не давят. В них есть функция шумоподавления и режим прозрачности, за счет этого звучание всегда чистое, а на улице можно слышать те сигналы, которые помогут адекватно реагировать на дорожную обстановку.',
        18000,
        100
    )
    RETURNING id
),
inserted_category12 AS (
    INSERT INTO product_category (product_id, category_id)
    SELECT id, 16 FROM inserted_product12
    UNION ALL
    SELECT id, 20 FROM inserted_product12
),
inserted_parameters12 AS (
    INSERT INTO parameters (name, value)
    VALUES
        ('MagSafe', 'Есть MagSafe')
    RETURNING id
),
inserted_product_parameters12 AS (
    INSERT INTO product_parameter (product_id, parameter_id)
    SELECT inserted_product12.id, inserted_parameters12.id
    FROM inserted_product12, inserted_parameters12
),
inserted_images12 AS (
    INSERT INTO product_images (product_id, image_url)
    SELECT inserted_product12.id, 'https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733733000/airpods-pro-2021_ttws1p.jpg'
    FROM inserted_product12
)
SELECT 1;

WITH inserted_product13 AS (
    INSERT INTO products (name, description, price, quantity)
    VALUES (
        'AirPods Pro 2',
        'Apple Airpods Pro 2 отличаются хорошо продуманной конструкцией, качественным чистым звуком и системой вентиляции — в эйрподсах комфортно даже при самом длительном их ношении. В наушниках предусмотрено шумоподавление, режим прозрачности, что позволяет оставаться в курсе происходящих вокруг событий. Музыку от басов до самых высоких частот гаджет воспроизводит чисто, работает на протяжении 7 часов без остановки.',
        21500,
        100
    )
    RETURNING id
),
inserted_category13 AS (
    INSERT INTO product_category (product_id, category_id)
    SELECT id, 16 FROM inserted_product13
    UNION ALL
    SELECT id, 20 FROM inserted_product13
),
inserted_parameters13 AS (
    INSERT INTO parameters (name, value)
    VALUES
        ('MagSafe', 'Есть MagSafe')
    RETURNING id
),
inserted_product_parameters13 AS (
    INSERT INTO product_parameter (product_id, parameter_id)
    SELECT inserted_product13.id, inserted_parameters13.id
    FROM inserted_product13, inserted_parameters13
),
inserted_images13 AS (
    INSERT INTO product_images (product_id, image_url)
    SELECT inserted_product13.id, 'https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733733000/airpods-pro-2021_ttws1p.jpg'
    FROM inserted_product13
)
SELECT 1;

WITH inserted_product14 AS (
    INSERT INTO products (name, description, price, quantity)
    VALUES (
        'Remark Чехол Iphone 15 Pro',
        ' ',
        1500,
        20
    )
    RETURNING id
),
inserted_category14 AS (
    INSERT INTO product_category (product_id, category_id)
    SELECT id, 17 FROM inserted_product14
),
inserted_parameters14 AS (
    INSERT INTO parameters (name, value)
    VALUES
        ('MagSafe', 'Есть MagSafe'),
        ('Материал', 'Пластик')
    RETURNING id
),
inserted_product_parameters14 AS (
    INSERT INTO product_parameter (product_id, parameter_id)
    SELECT inserted_product14.id, inserted_parameters14.id
    FROM inserted_product14, inserted_parameters14
),
inserted_images14 AS (
    INSERT INTO product_images (product_id, image_url)
    SELECT inserted_product14.id, 'https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733734987/case-iphone-15-pro_hzk5sq.jpg'
    FROM inserted_product14
)
SELECT 1;

WITH inserted_product15 AS (
    INSERT INTO products (name, description, price, quantity)
    VALUES (
        'Apple Чехол Iphone 15 Pro',
        ' ',
        1500,
        20
    )
    RETURNING id
),
inserted_category15 AS (
    INSERT INTO product_category (product_id, category_id)
    SELECT id, 17 FROM inserted_product15
),
inserted_parameters15 AS (
    INSERT INTO parameters (name, value)
    VALUES
        ('MagSafe', 'Есть MagSafe'),
        ('Материал', 'Кожа')
    RETURNING id
),
inserted_product_parameters15 AS (
    INSERT INTO product_parameter (product_id, parameter_id)
    SELECT inserted_product15.id, inserted_parameters15.id
    FROM inserted_product15, inserted_parameters15
),
inserted_images15 AS (
    INSERT INTO product_images (product_id, image_url)
    SELECT inserted_product15.id, 'https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733734986/case-iphone-15-pro-2_xbnfpj.jpg'
    FROM inserted_product15
)
SELECT 1;

WITH inserted_product16 AS (
    INSERT INTO products (name, description, price, quantity)
    VALUES (
        'Apple Чехол Iphone 16',
        ' ',
        1500,
        20
    )
    RETURNING id
),
inserted_category16 AS (
    INSERT INTO product_category (product_id, category_id)
    SELECT id, 17 FROM inserted_product16
),
inserted_parameters16 AS (
    INSERT INTO parameters (name, value)
    VALUES
        ('MagSafe', 'Есть MagSafe'),
        ('Материал', 'Силикон')
    RETURNING id
),
inserted_product_parameters16 AS (
    INSERT INTO product_parameter (product_id, parameter_id)
    SELECT inserted_product16.id, inserted_parameters16.id
    FROM inserted_product16, inserted_parameters16
),
inserted_images16 AS (
    INSERT INTO product_images (product_id, image_url)
    SELECT inserted_product16.id, 'https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733734983/case-iphone-16_hvixr8.jpg'
    FROM inserted_product16
)
SELECT 1;

WITH inserted_product17 AS (
    INSERT INTO products (name, description, price, quantity)
    VALUES (
        'Remark Чехол Iphone 14',
        ' ',
        1500,
        20
    )
    RETURNING id
),
inserted_category17 AS (
    INSERT INTO product_category (product_id, category_id)
    SELECT id, 17 FROM inserted_product17
),
inserted_parameters17 AS (
    INSERT INTO parameters (name, value)
    VALUES
        ('MagSafe', 'Нет MagSafe'),
        ('Материал', 'Пластик')
    RETURNING id
),
inserted_product_parameters17 AS (
    INSERT INTO product_parameter (product_id, parameter_id)
    SELECT inserted_product17.id, inserted_parameters17.id
    FROM inserted_product17, inserted_parameters17
),
inserted_images17 AS (
    INSERT INTO product_images (product_id, image_url)
    SELECT inserted_product17.id, 'https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733734984/case-iphone-14-1_iedy0o.png'
    FROM inserted_product17
)
SELECT 1;

WITH inserted_product18 AS (
    INSERT INTO products (name, description, price, quantity,created_time)
    VALUES (
        'Умная колонка Яндекс.Станция Лайт 2 фиолетовый',
        'Умная колонка Яндекс. Станция Лайт 2 фиолетового цвета ориентирована на детей. Она использует нейросеть YandexGPT, поэтому может отвечать на любые вопросы, находить информацию, рассказывать сказки и включать музыку по настроению. Ассистент реагирует на эмоции, по интонации определяет, в каком настроении находится ребенок. Все «эмоции» Алисы отображаются на информативном экране. Он включает в себя 36 элементов и демонстрирует температуру, время, параметры громкости и другие показатели.Яндекс. Станция Лайт 2 подключается к Wi-Fi-сетям, запоминает несколько сетей, автоматически устанавливает соединение с ними. Для управления колонкой при помощи смартфона через приложение Дом с Алисой она подключается через Bluetooth. На верхней части корпуса расположена панель управления с индикатором. Здесь же находится два микрофона для взаимодействия с ассистентом. Устройство получает питание от сети через USB Type C.',
        6300,
        12,
        NOW()
    )
    RETURNING id
),
inserted_category18 AS (
    INSERT INTO product_category (product_id, category_id)
    SELECT id, 19 FROM inserted_product18
),
inserted_parameters18 AS (
    INSERT INTO parameters (name, value)
    VALUES
        ('Цвет', 'Фиолетовый'),
        ('Мощность', '6вт')
    RETURNING id
),
inserted_product_parameters18 AS (
    INSERT INTO product_parameter (product_id, parameter_id)
    SELECT inserted_product18.id, inserted_parameters18.id
    FROM inserted_product18, inserted_parameters18
),
inserted_images18 AS (
    INSERT INTO product_images (product_id, image_url)
    SELECT inserted_product18.id, urls.image_url
    FROM inserted_product18
    CROSS JOIN (VALUES
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733735544/alice-1_iimi0f.webp'),
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733735545/alice-2_uh51hi.jpg')
    ) AS urls(image_url)
)
SELECT 1;

WITH inserted_product19 AS (
    INSERT INTO products (name, description, price, quantity)
    VALUES (
        'Samsung S24 Blue',
        'Смартфон получил приятный дизайн с матовым покрытием с эффектом сатина. Galaxy S24 имеет 6,2-дюймовый Dynamic AMOLED 2X экран с разрешением 2340 x 1080 пикселей. Ультратонкие рамки обеспечивают максимальное погружение в игры и фильмы.',
        63300,
        2
    )
    RETURNING id
),
inserted_category19 AS (
    INSERT INTO product_category (product_id, category_id)
    SELECT id, 13 FROM inserted_product19
),
inserted_parameters19 AS (
    INSERT INTO parameters (name, value)
    VALUES
        ('Цвет', 'Голубой'),
        ('Объем памяти', '128GB')
    RETURNING id
),
inserted_product_parameters19 AS (
    INSERT INTO product_parameter (product_id, parameter_id)
    SELECT inserted_product19.id, inserted_parameters19.id
    FROM inserted_product19, inserted_parameters19
),
inserted_images19 AS (
    INSERT INTO product_images (product_id, image_url)
    SELECT inserted_product19.id, urls.image_url
    FROM inserted_product19
    CROSS JOIN (VALUES
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1730114401/xyncahtgiupcmtks3g1m.webp'),
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1730114906/SamsungGalaxyS24Blue3.png'),
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1730114906/SamsungGalaxyS24Blue2.png')
    ) AS urls(image_url)
)
SELECT 1;

WITH inserted_product20 AS (
    INSERT INTO products (name, description, price, quantity)
    VALUES (
        'Samsung S24+ Purple',
        'Смартфон получил приятный дизайн с матовым покрытием с эффектом сатина. Galaxy S24+ имеет 6,7-дюймовый QHD+ Dynamic AMOLED 2X экран с разрешением 3120 x 1440 пикселей. Ультратонкие рамки обеспечивают максимальное погружение в игры и фильмы. Galaxy S24+ получил более емкий аккумулятор на 4900 мАч. Его хватит до 31 часа воспроизведения видео и до 92 часов прослушивания аудио.',
        76300,
        15
    )
    RETURNING id
),
inserted_category20 AS (
    INSERT INTO product_category (product_id, category_id)
    SELECT id, 14 FROM inserted_product20
),
inserted_parameters20 AS (
    INSERT INTO parameters (name, value)
    VALUES
        ('Цвет', 'Фиолетовый'),
        ('Объем памяти', '128GB')
    RETURNING id
),
inserted_product_parameters20 AS (
    INSERT INTO product_parameter (product_id, parameter_id)
    SELECT inserted_product20.id, inserted_parameters20.id
    FROM inserted_product20, inserted_parameters20
),
inserted_images20 AS (
    INSERT INTO product_images (product_id, image_url)
    SELECT inserted_product20.id, 'https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733737496/s24_purple-1_clrzyj.png'
    FROM inserted_product20
)
SELECT 1;

WITH inserted_product21 AS (
    INSERT INTO products (name, description, price, quantity)
    VALUES (
        'Смартфон HUAWEI P60 Green',
        'Смартфон HUAWEI P60 получил изогнутый LTPO-экран 6,67 дюйма с разрешением 2700x1220 пикселей с поддержкой адаптивной регулировки частоты обновления от 1 до 120 Гц. «Сердцем» HUAWEI P60 является восьмиядерный процессор Snapdragon 8+ Gen 1 (4G). Мощности смартфона хватает как на выполнение повседневных задач, так и на запуск современных игр и приложений.',
        42000,
        15
    )
    RETURNING id
),
inserted_category21 AS (
    INSERT INTO product_category (product_id, category_id)
    SELECT id, 1 FROM inserted_product21
),
inserted_parameters21 AS (
    INSERT INTO parameters (name, value)
    VALUES
        ('Цвет', 'Зеленый'),
        ('Объем памяти', '256GB')
    RETURNING id
),
inserted_product_parameters21 AS (
    INSERT INTO product_parameter (product_id, parameter_id)
    SELECT inserted_product21.id, inserted_parameters21.id
    FROM inserted_product21, inserted_parameters21
),
inserted_images21 AS (
    INSERT INTO product_images (product_id, image_url)
    SELECT inserted_product21.id, urls.image_url
    FROM inserted_product21
    CROSS JOIN (VALUES
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733737496/p60-1_etrnky.jpg'),
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733737495/p60-2_njbchr.jpg'),
        ('https://res.cloudinary.com/dwzjwh6tb/image/upload/v1733737493/p-60-3_vladrd.jpg')
    ) AS urls(image_url)
)
SELECT 1;


