package internal

import (
	"fmt"
	"hash/fnv"
	"log"
	"strconv"

	"github.com/bwmarrin/snowflake"
	"gorm.io/driver/postgres"
	"gorm.io/gorm"
)

type Shard struct {
	ID int
	DB *gorm.DB
}

type Cluster struct {
	Shards []*Shard
	Node   *snowflake.Node
}

func (c *Cluster) GetOrder(orderID int) Order {
	for _, shard := range c.Shards {
		var order Order
		err := shard.DB.First(&order, "order_id = ?", orderID).Error
		if err != nil {
			continue
		}

		return order
	}
	return Order{}
}

func (c *Cluster) GetAllUsers() []User {
	var users []User
	for _, shard := range c.Shards {
		var shardUsers []User
		err := shard.DB.Find(&shardUsers).Error
		if err != nil {
			log.Printf("query error shard %d: %v", shard.ID, err)
			continue
		}
		users = append(users, shardUsers...)
	}
	return users
}

func (c *Cluster) GetOrdersByUser(userID int) []Order {
	shard := c.getShard(strconv.Itoa(userID))

	var orders []Order
	err := shard.DB.Find(&orders, "user_id = ?", userID).Error
	if err != nil {
		log.Printf("query error shard %d: %v", shard.ID, err)
		return []Order{}
	}

	return orders
}

func NewCluster(conns []string) *Cluster {
	shards := make([]*Shard, len(conns))
	for i, conn := range conns {
		db, err := gorm.Open(postgres.Open(conn), &gorm.Config{})
		if err != nil {
			log.Fatalf("failed to connect shard %d: %v", i, err)
		}

		shards[i] = &Shard{ID: i, DB: db}
	}
	node, err := snowflake.NewNode(1)
	if err != nil {
		log.Fatalf("failed to create snowflake node: %v", err)
	}

	return &Cluster{Shards: shards, Node: node}
}

func (c *Cluster) getShard(key string) *Shard {
	h := fnv.New32a()
	h.Write([]byte(key))
	shardID := int(h.Sum32()) % len(c.Shards)
	return c.Shards[shardID]
}

func (c *Cluster) countInShard(obj interface{}, shardID int) int64 {
	var n int64
	err := c.Shards[shardID].DB.Model(&obj).Count(&n).Error
	if err != nil {
		log.Printf("query error shard %d: %v", shardID, err)
		return 0
	}
	return n
}

func (c *Cluster) CountOrders() int {
	res := 0
	var order Order
	for i, _ := range c.Shards {
		n := c.countInShard(&order, i)
		res += int(n)
	}
	return res
}

func (c *Cluster) CountUsers() int {
	res := 0
	var user User
	for i, _ := range c.Shards {
		n := c.countInShard(&user, i)
		res += int(n)
	}
	return res
}

func (c *Cluster) InsertUser(name string) {
	id := c.Node.Generate().Int64()
	shard := c.getShard(strconv.FormatInt(id, 10))
	err := shard.DB.Create(&User{UserID: int(id), Name: name}).Error
	if err != nil {
		log.Printf("insert error shard %d: %v", shard.ID, err)
	} else {
		fmt.Printf("User %s → shard %d\n", name, shard.ID)
	}
}

func (c *Cluster) InsertOrder(details string, user int) {
	shard := c.getShard(strconv.Itoa(user))
	err := shard.DB.Create(&Order{Details: details, UserID: user}).Error
	if err != nil {
		log.Printf("insert error shard %d: %v", shard.ID, err)
	} else {
		fmt.Printf("Order %s → shard %d\n", details, shard.ID)
	}
}

func (c *Cluster) GetUser(id int) User {
	shard := c.getShard(strconv.Itoa(id))

	var user User
	err := shard.DB.First(&user, "user_id = ?", id).Error
	if err != nil {
		return User{}
	}

	return user
}

func (c *Cluster) GetUsersInShard(shardID int, echo bool) []User {
	var users []User
	err := c.Shards[shardID].DB.Find(&users).Error
	if err != nil {
		log.Printf("query error shard %d: %v", shardID, err)
		return []User{}
	}

	if echo {
		fmt.Printf("Shard %d users_total: %d\n", shardID, len(users))
	}
	// for _, user := range users {
	// 	fmt.Printf("  %d: %s\n", user.UserID, user.Name)
	// }
	return users
}

func (c *Cluster) GetOrders(shardID int) []Order {
	var orders []Order
	err := c.Shards[shardID].DB.Find(&orders).Error
	if err != nil {
		log.Printf("query error shard %d: %v", shardID, err)
		return []Order{}
	}

	fmt.Printf("Shard %d orders_total: %d\n", shardID, len(orders))
	// for _, order := range orders {
	// 	fmt.Printf("  %d: %s\n", order.OrderID, order.Details)
	// }
	return orders
}
