---
-- Как написать оптимальный запрос и ускорить его выполнение
---

-- 1.1. поиск по id -- первичный ключ
-- Поиск клиента
EXPLAIN SELECT * FROM customers
WHERE customer_id = 145065;

EXPLAIN ANALYZE SELECT * FROM customers
WHERE customer_id = 145065;

CREATE INDEX idx_customers_customer_id ON customers(customer_id);


-- 1.2. поиск по id -- внешний ключ
-- Поиск заказов клиента
EXPLAIN SELECT * FROM orders
WHERE customer_id = 145065;

EXPLAIN ANALYZE SELECT * FROM orders
WHERE customer_id = 145065;

CREATE INDEX idx_orders_customer_id ON orders(customer_id);


-- 1.3. поиск по email - hash-idx
-- Поиск пользователя по email
EXPLAIN SELECT * FROM customers
WHERE email = 'customer_7565@example.com';

EXPLAIN ANALYZE SELECT * FROM customers
WHERE email = 'customer_7565@example.com';

CREATE INDEX idx_customers_hash_email ON customers USING HASH (email);

EXPLAIN SELECT * FROM customers
WHERE email > 'customer_75';

drop index idx_customers_hash_email;

CREATE INDEX idx_customers_email ON customers(email);

explain ANALYZE SELECT * FROM customers
WHERE email > 'customer_75';


-- 1.4. поиск по тексту
-- Поиск клиента по имени или фамилии
EXPLAIN SELECT *
FROM customers 
WHERE first_name like 'Custo1%';

EXPLAIN ANALYZE SELECT *
FROM customers 
WHERE first_name like 'Custo1%';

CREATE INDEX idx_customers_first_name ON customers(first_name);

EXPLAIN ANALYZE SELECT *
FROM customers 
WHERE first_name like 'Custo1%';

CREATE EXTENSION pg_trgm;
CREATE INDEX idx_customers_full_text_name ON customers  USING gin (first_name gin_trgm_ops);


-- 2. Есть индексы на соединяемых таблицах
EXPLAIN SELECT o.order_id, c.first_name 
FROM orders o
JOIN customers c ON o.customer_id = c.customer_id
WHERE o.order_date > '2024-12-01';

EXPLAIN ANALYZE SELECT o.order_id, c.first_name 
FROM orders o
JOIN customers c ON o.customer_id = c.customer_id
WHERE o.order_date > '2024-12-30';


-- 3. Брать только нужные данные
EXPLAIN ANALYZE SELECT *
FROM customers
WHERE email like 'customer_7565%@example.com';

explain ANALYZE SELECT first_name 
FROM customers
WHERE email like 'customer_7565%@example.com';
