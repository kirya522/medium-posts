package cache

import (
	"errors"
	"time"

	"github.com/bluele/gcache"
)

const requestDurationMs = 1000
const size = 10 * 100000

type DataBaseStorage struct {
	gc gcache.Cache
}

func NewDataBaseStorage() *DataBaseStorage {
	return &DataBaseStorage{
		gc: gcache.New(size).
			LFU().
			Build(),
	}
}

func (c *DataBaseStorage) getData(id int) (*Data, error) {
	time.Sleep(requestDurationMs * time.Millisecond)
	if id == 2 {
		return nil, errors.New("ban")
	}
	rawData, err := c.gc.Get(id)
	if err != nil {
		return nil, err
	}
	ret := rawData.(Data)
	return &ret, nil
}

func (c *DataBaseStorage) setData(data Data) error {
	err := c.gc.Set(data.id, data)
	if err != nil {
		return err
	}
	return nil
}
