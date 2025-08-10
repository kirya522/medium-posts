//go:generate mockgen -package ${GOPACKAGE} -source $GOFILE -destination mock.go

package structs

import "fmt"

type Starter interface {
	Start()
}

type Engine struct {
	Power int
}

func (e Engine) String() string {
	return fmt.Sprintf("Engine with power %d", e.Power)
}

func (e Engine) Start() {
	fmt.Println("Engine started with power", e.Power)
}

type Car struct {
	Engine // композиция — встраивание типа

	Model string
}
