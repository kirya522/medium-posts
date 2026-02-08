# Демо тормозов пагинации

Как поднять окружение в `demo.sh`

## Limit-offset

demo1
```sql
EXPLAIN ANALYZE
SELECT id, created_at
FROM orders
ORDER BY created_at DESC, id DESC
LIMIT 20 OFFSET 500000;
```

## Cursor

```sql
EXPLAIN ANALYZE
SELECT id, created_at
FROM orders
WHERE (created_at, id) < ('2025-01-10 12:00:00', 812345)
ORDER BY created_at DESC, id DESC
LIMIT 20;
```

## Page + Cursor (jump)

```sql
EXPLAIN ANALYZE
SELECT created_at, id
FROM orders
ORDER BY created_at DESC, id DESC
LIMIT 1 OFFSET 500000;
```