package internal

type User struct {
	UserID int     `gorm:"primaryKey;column:user_id"`
	Name   string  `gorm:"column:name"`
	Orders []Order `gorm:"foreignKey:UserID"`
}

func (User) TableName() string { return "users" }

type Order struct {
	OrderID int    `gorm:"primaryKey;column:order_id"`
	Details string `gorm:"column:details"`
	UserID  int    `gorm:"column:user_id"`
	User    *User  `gorm:"constraint:OnUpdate:CASCADE,OnDelete:SET NULL;foreignKey:UserID"`
}

func (Order) TableName() string { return "orders" }
