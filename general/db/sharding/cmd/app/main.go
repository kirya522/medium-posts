package main

import (
	"fmt"
	"sharding-demo/internal"
	"time"

	mathrand "math/rand"

	"github.com/goombaio/namegenerator"
	_ "github.com/lib/pq"
)

func main() {
	// setup cluster
	conns := []string{
		"postgres://user:pass@localhost:5433/shard1?sslmode=disable",
		"postgres://user:pass@localhost:5434/shard2?sslmode=disable",
		"postgres://user:pass@localhost:5435/shard3?sslmode=disable",
	}
	cluster := internal.NewCluster(conns)

	// shard by userName (generate)
	fmt.Print("Users data stored in shards\n")
	fmt.Print("----\n")
	demoUser(cluster)

	fmt.Print("\n")

	// shard by userID
	fmt.Print("Orders data stored in shards\n")
	fmt.Print("----\n")
	demoOrder(cluster)
}

func demoUser(cluster *internal.Cluster) {
	// generate data
	seed := time.Now().UTC().UnixNano()
	nameGenerator := namegenerator.NewNameGenerator(seed)
	if cluster.CountUsers() <= 20000 {
		for i := 0; i < 10000; i++ {
			cluster.InsertUser(fmt.Sprintf("%s_%d", nameGenerator.Generate(), i))
		}
	}

	// info
	for shardId := range cluster.Shards {
		cluster.GetUsers(shardId, true)
	}
}

func demoOrder(cluster *internal.Cluster) {
	// generate data
	seed := time.Now().UTC().UnixNano()
	nameGenerator := namegenerator.NewNameGenerator(seed)

	users := cluster.GetUsers(0, false)

	if cluster.CountOrders() <= 20000 {
		// first user is popular
		for i := 0; i < 10000; i++ {
			cluster.InsertOrder(fmt.Sprintf("%s_%d", nameGenerator.Generate(), i), users[0].UserID)
		}

		for i := 0; i < 20000; i++ {
			cluster.InsertOrder(fmt.Sprintf("%s_%d", nameGenerator.Generate(), i), users[mathrand.Intn(len(users))].UserID)
		}
	}

	// info
	for shardId := range cluster.Shards {
		cluster.GetOrders(shardId)
	}
}
