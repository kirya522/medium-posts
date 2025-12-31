CREATE TABLE IF NOT EXISTS users (
    user_id SERIAL PRIMARY KEY,
    name TEXT UNIQUE
);


CREATE TABLE IF NOT EXISTS orders (
    order_id SERIAL PRIMARY KEY,
    details TEXT,
    user_id integer REFERENCES users(user_id)
)
