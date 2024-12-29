---
-- Как написать оптимальный запрос и ускорить его выполнение
---

-- Создание таблицы клиентов

CREATE TABLE customers (
    customer_id SERIAL PRIMARY KEY,
    email VARCHAR(100) UNIQUE, -- уникальный столбец email
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    location GEOMETRY(Point, 4326) -- добавление географической информации
);

-- Создание таблицы заказов
CREATE TABLE orders (
    order_id SERIAL PRIMARY KEY,
    customer_id INT REFERENCES customers(customer_id),
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


-- 1. поиск клиента
EXPLAIN SELECT * FROM customers
WHERE customer_id = 7565;

EXPLAIN ANALYZE SELECT * FROM customers
WHERE customer_id = 7565;

CREATE INDEX idx_customer_customer_id ON customers(customer_id);


-- 2. поиск заказов клиента
EXPLAIN SELECT * FROM orders
WHERE customer_id = 7565;

EXPLAIN ANALYZE SELECT * FROM orders
WHERE customer_id = 7565;

CREATE INDEX idx_customer_id ON orders(customer_id);


-- 3. Сколько заказов по дням
EXPLAIN SELECT COUNT(*), order_date 
FROM orders 
WHERE order_date > '2024-01-01'
GROUP BY order_date;

EXPLAIN ANALYZE SELECT COUNT(*), order_date 
FROM orders 
WHERE order_date > '2024-01-01'
GROUP BY order_date;

CREATE INDEX idx_order_date ON orders(order_date);


-- 4. Поиск клиента по имени или фамилии
EXPLAIN SELECT *
FROM customers 
WHERE first_name like '%Custo%';

EXPLAIN ANALYZE SELECT *
FROM customers 
WHERE first_name like '%Custo%';

SELECT *
FROM customers 
WHERE first_name @@ to_tsquery('english', 'Custo');

CREATE INDEX idx_full_text_name ON customers USING GIN (to_tsvector('english', first_name || ' ' || last_name));


-- 5. Поиск клиентов, которые находятся в радиусе 100 км от Нью-Йорка
EXPLAIN SELECT customer_id, first_name, last_name, email
FROM customers
WHERE ST_DWithin(
    location, 
    ST_SetSRID(ST_MakePoint(-73.935242, 40.730610), 4326),  -- Нью-Йорк (долгота, широта)
    100000  -- Радиус в метрах (100 км = 100000 метров)
);

EXPLAIN ANALYZE SELECT customer_id, first_name, last_name, email
FROM customers
WHERE ST_DWithin(
    location, 
    ST_SetSRID(ST_MakePoint(-73.935242, 40.730610), 4326),  -- Нью-Йорк (долгота, широта)
    100000  -- Радиус в метрах (100 км = 100000 метров)
);

CREATE INDEX idx_location ON customers USING GIST (location);


-- 6. Поиск пользователя по email
EXPLAIN SELECT * FROM customers
WHERE email = 'customer_7565@example.com';

EXPLAIN ANALYZE SELECT * FROM customers
WHERE email = 'customer_7565@example.com';

CREATE INDEX idx_hash_email ON customers USING HASH (email);
-- TODO когда не подойдет


-- 7. Есть индексы на соединяемых таблицах
EXPLAIN SELECT o.order_id, c.first_name 
FROM orders o
INNER JOIN customers c ON o.customer_id = c.customer_id
WHERE o.order_date > '2024-01-01';

EXPLAIN ANALYZE SELECT o.order_id, c.first_name 
FROM orders o
INNER JOIN customers c ON o.customer_id = c.customer_id
WHERE o.order_date > '2024-01-01';


-- 8. Брать только нужные данные
EXPLAIN ANALYZE SELECT *
FROM customers
WHERE email = 'customer_7565@example.com';

EXPLAIN SELECT first_name, last_name 
FROM customers
WHERE email = 'customer_7565@example.com';