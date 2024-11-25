CREATE TABLE IF NOT EXISTS users (
    id            BIGSERIAL PRIMARY KEY,
    first_name    TEXT NOT NULL,
    last_name     TEXT NOT NULL,
    email         TEXT NOT NULL UNIQUE,
    password      BYTEA NOT NULL,
    role          TEXT NOT NULL,
    created_at    TIMESTAMP WITH TIME ZONE,
    blocked_at    TIMESTAMP WITH TIME ZONE
);
