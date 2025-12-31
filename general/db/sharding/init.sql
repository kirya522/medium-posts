CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT PRIMARY KEY,
    name TEXT UNIQUE
);


CREATE TABLE IF NOT EXISTS orders (
    order_id BIGSERIAL PRIMARY KEY,
    details TEXT,
    user_id BIGINT REFERENCES users(user_id)
)
