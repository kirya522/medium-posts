package cache

type Storage interface {
	getData(id int) (*Data, error)
	setData(data Data) error
}
