CREATE TABLE IF NOT EXISTS users (
    id              BIGSERIAL PRIMARY KEY,
    first_name      TEXT NOT NULL,
    last_name       TEXT NOT NULL,
    email           TEXT NOT NULL UNIQUE,
    password        BYTEA NOT NULL,
    role            TEXT NOT NULL,
    created_time    TIMESTAMP WITH TIME ZONE,
    blocked_time    TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS categories (
    id              BIGSERIAL PRIMARY KEY,
    name            TEXT NOT NULL,
    description     TEXT,
    parent_id       BIGINT,
    created_time    TIMESTAMP WITH TIME ZONE,
    updated_time    TIMESTAMP WITH TIME ZONE,
    deleted_time    TIMESTAMP WITH TIME ZONE,
    CONSTRAINT fk_parent_category FOREIGN KEY (parent_id) REFERENCES categories(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS products (
    id              BIGSERIAL PRIMARY KEY,
    name            TEXT NOT NULL,
    description     TEXT,
    price           DECIMAL NOT NULL,
    quantity        INTEGER,
    created_time    TIMESTAMP WITH TIME ZONE,
    updated_time    TIMESTAMP WITH TIME ZONE,
    deleted_time    TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS product_images (
    id              BIGSERIAL PRIMARY KEY,
    product_id      BIGINT NOT NULL,
    image_url       TEXT NOT NULL,
    FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE TABLE IF NOT EXISTS product_category (
    product_id      BIGINT NOT NULL,
    category_id     BIGINT NOT NULL,
    PRIMARY KEY (product_id, category_id),
    CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS parameters (
    id              BIGSERIAL PRIMARY KEY,
    name            TEXT NOT NULL,
    value           TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS product_parameter (
    product_id      BIGINT NOT NULL,
    parameter_id    BIGINT NOT NULL,
    PRIMARY KEY (product_id, parameter_id),
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    FOREIGN KEY (parameter_id) REFERENCES parameters(id) ON DELETE CASCADE
);

CREATE TABLE carts (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
    session_id TEXT UNIQUE,
    CONSTRAINT unique_user_or_session UNIQUE (user_id, session_id)
);

CREATE TABLE cart_items (
    id BIGSERIAL PRIMARY KEY,
    cart_id BIGINT REFERENCES carts(id) ON DELETE CASCADE,
    product_id BIGINT REFERENCES products(id) ON DELETE CASCADE,
    quantity INTEGER NOT NULL CHECK (quantity > 0)
);

