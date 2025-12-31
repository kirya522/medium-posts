package main

type User struct {
	UserID uint    `gorm:"primaryKey;column:user_id"`
	Name   string  `gorm:"column:name"`
	Orders []Order `gorm:"foreignKey:UserID"`
}

func (User) TableName() string { return "users" }

type Order struct {
	OrderID uint   `gorm:"primaryKey;column:order_id"`
	Details string `gorm:"column:details"`
	UserID  uint   `gorm:"column:user_id"`
	User    *User  `gorm:"constraint:OnUpdate:CASCADE,OnDelete:SET NULL;foreignKey:UserID"`
}

func (Order) TableName() string { return "orders" }
