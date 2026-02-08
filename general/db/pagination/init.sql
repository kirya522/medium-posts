CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL
);

-- индекс специально правильный под cursor pagination
CREATE INDEX idx_orders_created_id
ON orders (created_at DESC, id DESC);