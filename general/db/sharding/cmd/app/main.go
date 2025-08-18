package main

import (
	"database/sql"
	"fmt"
	"hash/fnv"
	"log"

	_ "github.com/lib/pq"
)

type Shard struct {
	ID int
	DB *sql.DB
}

type Cluster struct {
	Shards []*Shard
}

func NewCluster(conns []string) *Cluster {
	shards := make([]*Shard, len(conns))
	for i, conn := range conns {
		db, err := sql.Open("postgres", conn)
		if err != nil {
			log.Fatalf("failed to connect shard %d: %v", i, err)
		}
		shards[i] = &Shard{ID: i, DB: db}
	}
	return &Cluster{Shards: shards}
}

func (c *Cluster) getShard(key string) *Shard {
	h := fnv.New32a()
	h.Write([]byte(key))
	shardID := int(h.Sum32()) % len(c.Shards)
	return c.Shards[shardID]
}

func (c *Cluster) InsertUser(name string) {
	shard := c.getShard(name)
	_, err := shard.DB.Exec("INSERT INTO users(name) VALUES($1)", name)
	if err != nil {
		log.Printf("insert error shard %d: %v", shard.ID, err)
	} else {
		fmt.Printf("User %s → shard %d\n", name, shard.ID)
	}
}

func (c *Cluster) GetUsers(shardID int) {
	rows, err := c.Shards[shardID].DB.Query("SELECT id, name FROM users")
	if err != nil {
		log.Printf("query error shard %d: %v", shardID, err)
		return
	}
	defer rows.Close()

	fmt.Printf("Shard %d users:\n", shardID)
	for rows.Next() {
		var id int
		var name string
		rows.Scan(&id, &name)
		fmt.Printf("  %d: %s\n", id, name)
	}
}

func main() {
	conns := []string{
		"postgres://user:pass@localhost:5433/shard1?sslmode=disable",
		"postgres://user:pass@localhost:5434/shard2?sslmode=disable",
		"postgres://user:pass@localhost:5435/shard3?sslmode=disable",
	}

	cluster := NewCluster(conns)

	cluster.InsertUser("Alice")
	cluster.InsertUser("Bob")
	cluster.InsertUser("Charlie")
	cluster.InsertUser("Dave")

	for i := range cluster.Shards {
		cluster.GetUsers(i)
	}
}
