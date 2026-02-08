package main

import (
	"database/sql"
	"encoding/base64"
	"encoding/json"
	"log"
	"math/rand"
	"net/http"
	"strconv"
	"time"

	_ "github.com/lib/pq"
)

const (
	dbURL    = "postgres://demo:demo@localhost/demo?sslmode=disable"
	dataSize = 1_000_000
	pageSize = 20
)

/*
Domain models
*/

type Order struct {
	ID        int64     `json:"id"`
	CreatedAt time.Time `json:"created_at"`
}

// Cursor — составной ключ для стабильной сортировки
// (created_at + id решают проблему одинаковых timestamp)
type Cursor struct {
	CreatedAt time.Time `json:"created_at"`
	ID        int64     `json:"id"`
}

func main() {
	rand.Seed(time.Now().UnixNano())

	db := mustOpenDB()
	defer db.Close()

	// Генерируем данные один раз
	initDataIfNeeded(db)

	http.HandleFunc("/offset", offsetHandler(db))       // популярный
	http.HandleFunc("/cursor", cursorHandler(db))       // правильный
	http.HandleFunc("/page", pageWithAnchorHandler(db)) // виртуальные страницы

	log.Println("Listening on :8080")
	log.Fatal(http.ListenAndServe(":8080", nil))
}

/*
Infrastructure
*/

func mustOpenDB() *sql.DB {
	db, err := sql.Open("postgres", dbURL)
	if err != nil {
		log.Fatal(err)
	}
	return db
}

/*
Data initialization
*/

func initDataIfNeeded(db *sql.DB) {
	count := countOrders(db)
	if count >= dataSize {
		log.Printf("Data already exists: %d rows", count)
		return
	}

	log.Printf("Generating %d rows...", dataSize)
	generateOrders(db)
	log.Println("Data generation finished")
}

func countOrders(db *sql.DB) int {
	var count int
	if err := db.QueryRow(`SELECT count(*) FROM orders`).Scan(&count); err != nil {
		log.Fatal(err)
	}
	return count
}

func generateOrders(db *sql.DB) {
	tx := mustBegin(db)
	defer tx.Rollback()

	stmt := mustPrepare(tx, `
		INSERT INTO orders (user_id, created_at)
		VALUES ($1, $2)
	`)
	defer stmt.Close()

	now := time.Now()

	for i := 0; i < dataSize; i++ {
		_, err := stmt.Exec(
			rand.Int63n(100_000),
			now.Add(-time.Duration(rand.Intn(180*24))*time.Hour),
		)
		if err != nil {
			log.Fatal(err)
		}

		if i > 0 && i%100_000 == 0 {
			log.Printf("Inserted %d rows", i)
		}
	}

	if err := tx.Commit(); err != nil {
		log.Fatal(err)
	}
}

func mustBegin(db *sql.DB) *sql.Tx {
	tx, err := db.Begin()
	if err != nil {
		log.Fatal(err)
	}
	return tx
}

func mustPrepare(tx *sql.Tx, q string) *sql.Stmt {
	stmt, err := tx.Prepare(q)
	if err != nil {
		log.Fatal(err)
	}
	return stmt
}

/*
Handlers
*/

// ❌ OFFSET pagination
// Чем больше page — тем больше строк Postgres вынужден пропускать
func offsetHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		page := parseInt(r, "page", 0)
		offset := page * pageSize

		rows := mustQuery(db, `
			SELECT id, created_at
			FROM orders
			ORDER BY created_at DESC, id DESC
			LIMIT $1 OFFSET $2
		`, pageSize, offset)
		defer rows.Close()

		writeOrders(w, rows)
	}
}

// ✅ Чистый cursor pagination
// Самый быстрый и стабильный вариант
func cursorHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		cursor := decodeCursor(r.URL.Query().Get("cursor"))

		var rows *sql.Rows
		if cursor == nil {
			// Первая страница — без WHERE
			rows = mustQuery(db, `
				SELECT id, created_at
				FROM orders
				ORDER BY created_at DESC, id DESC
				LIMIT $1
			`, pageSize)
		} else {
			rows = mustQuery(db, `
				SELECT id, created_at
				FROM orders
				WHERE (created_at, id) < ($1, $2)
				ORDER BY created_at DESC, id DESC
				LIMIT $3
			`, cursor.CreatedAt, cursor.ID, pageSize)
		}
		defer rows.Close()

		items, next := scanOrdersWithCursor(rows)
		writeJSON(w, map[string]any{
			"items":       items,
			"next_cursor": next,
		})
	}
}

// ⚠️ Page → Anchor → Cursor
// OFFSET используется ОДИН раз, дальше — cursor
func pageWithAnchorHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		page := parseInt(r, "page", 0)

		anchor := loadAnchor(db, page*pageSize)

		rows := mustQuery(db, `
			SELECT id, created_at
			FROM orders
			WHERE (created_at, id) < ($1, $2)
			ORDER BY created_at DESC, id DESC
			LIMIT $3
		`, anchor.CreatedAt, anchor.ID, pageSize)
		defer rows.Close()

		writeOrders(w, rows)
	}
}

/*
Query helpers
*/

func loadAnchor(db *sql.DB, offset int) Cursor {
	var c Cursor
	err := db.QueryRow(`
		SELECT created_at, id
		FROM orders
		ORDER BY created_at DESC, id DESC
		LIMIT 1 OFFSET $1
	`, offset).Scan(&c.CreatedAt, &c.ID)

	if err != nil {
		log.Fatal(err)
	}
	return c
}

func mustQuery(db *sql.DB, q string, args ...any) *sql.Rows {
	rows, err := db.Query(q, args...)
	if err != nil {
		log.Fatal(err)
	}
	return rows
}

/*
Response helpers
*/

func writeOrders(w http.ResponseWriter, rows *sql.Rows) {
	var res []Order
	for rows.Next() {
		var o Order
		rows.Scan(&o.ID, &o.CreatedAt)
		res = append(res, o)
	}
	writeJSON(w, res)
}

func scanOrdersWithCursor(rows *sql.Rows) ([]Order, *string) {
	var (
		items []Order
		last  *Order
	)

	for rows.Next() {
		var o Order
		rows.Scan(&o.ID, &o.CreatedAt)
		items = append(items, o)
		last = &o
	}

	if last == nil {
		return items, nil
	}

	c := encodeCursor(Cursor{
		CreatedAt: last.CreatedAt,
		ID:        last.ID,
	})

	return items, &c
}

func writeJSON(w http.ResponseWriter, v any) {
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(v)
}

/*
Utils
*/

func parseInt(r *http.Request, key string, def int) int {
	v := r.URL.Query().Get(key)
	if v == "" {
		return def
	}
	i, err := strconv.Atoi(v)
	if err != nil {
		return def
	}
	return i
}

func encodeCursor(c Cursor) string {
	b, _ := json.Marshal(c)
	return base64.StdEncoding.EncodeToString(b)
}

func decodeCursor(s string) *Cursor {
	if s == "" {
		return nil
	}
	b, err := base64.StdEncoding.DecodeString(s)
	if err != nil {
		return nil
	}
	var c Cursor
	json.Unmarshal(b, &c)
	return &c
}
