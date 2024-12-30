-- Создание таблицы клиентов

CREATE TABLE customers (
    customer_id SERIAL, -- специально без PRIMARY KEY иначе будет индекс
    email VARCHAR(100), -- специально без UNIQUE иначе будет индекс
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    location GEOMETRY(Point, 4326)
);

-- Создание таблицы заказов
CREATE TABLE orders (
    order_id SERIAL, -- специально без PRIMARY KEY иначе будет индекс
    customer_id INT,
    order_date DATE NOT NULL,
    amount NUMERIC(10, 2),
    status VARCHAR(50)
);

-- Генерация клиентов
INSERT INTO customers (first_name, last_name, email, location)
SELECT
    'Customer_' || g.i,
    'LastName_' || g.i,
    'customer_' || g.i || '@example.com',
    ST_SetSRID(
        ST_MakePoint(
            (RANDOM() * 360) - 180,  -- Генерация случайной долготы в диапазоне от -180 до 180
            (RANDOM() * 180) - 90     -- Генерация случайной широты в диапазоне от -90 до 90
        ),
        4326  -- Устанавливаем SRID (система координат WGS 84)
    )
FROM generate_series(1, 10001) AS g(i)

-- Генерация заказов
INSERT INTO orders (customer_id, order_date, amount, status)
SELECT
    (RANDOM() * 10000)::INT + 1,
    '2024-01-01'::DATE + (RANDOM() * 365)::INT,
    (RANDOM() * 500)::NUMERIC(10, 2),
    CASE WHEN RANDOM() > 0.5 THEN 'completed' ELSE 'pending' END
FROM generate_series(1, 1000000);

