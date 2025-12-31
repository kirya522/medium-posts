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
		//"postgres://user:pass@localhost:5436/shard4?sslmode=disable",
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

	// demo requests
	demoRequests(cluster)
}

func demoUser(cluster *internal.Cluster) {
	// generate data
	seed := time.Now().UTC().UnixNano()
	nameGenerator := namegenerator.NewNameGenerator(seed)
	if cluster.CountUsers() <= 10000 {
		for i := 0; i < 12000; i++ {
			cluster.InsertUser(fmt.Sprintf("%s_%d", nameGenerator.Generate(), i))
		}
	}

	// info
	for shardId := range cluster.Shards {
		cluster.GetUsersInShard(shardId, true)
	}
}

func demoOrder(cluster *internal.Cluster) {
	// generate data
	seed := time.Now().UTC().UnixNano()
	nameGenerator := namegenerator.NewNameGenerator(seed)

	users := cluster.GetAllUsers()

	if cluster.CountOrders() <= 20000 {
		// first user is popular
		for i := 0; i < 5000; i++ {
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

func demoRequests(cluster *internal.Cluster) {
	// users of shard
	fmt.Print("\nGet all users\n")
	users := cluster.GetAllUsers()
	fmt.Printf("Total users: %d\n", len(users))
	fmt.Print("----\n")

	// get single user
	fmt.Print("\nGet single user\n")
	fmt.Print("----\n")
	user := cluster.GetUser(users[mathrand.Intn(len(users))].UserID)
	if user.UserID != 0 {
		fmt.Printf("UserID: %d, Name: %s\n", user.UserID, user.Name)
	} else {
		fmt.Printf("User not found\n")
	}

	// get orders of popular user
	fmt.Print("\nGet orders of user\n")
	fmt.Print("----\n")
	orders := cluster.GetOrdersByUser(users[0].UserID)
	fmt.Printf("UserID: %d has %d orders\n", users[0].UserID, len(orders))

	// get order by orderID
	if len(orders) == 0 {
		fmt.Printf("No orders found for user, cant request by OrderID %d\n", users[0].UserID)
	} else {
		fmt.Print("\nGet order by OrderID\n")
		fmt.Print("----\n")
		order := cluster.GetOrder(orders[mathrand.Intn(len(orders))].OrderID)
		fmt.Printf("OrderID: %d, Details: %s, UserID: %d\n", order.OrderID, order.Details, order.UserID)
	}
}
