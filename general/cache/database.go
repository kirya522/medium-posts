package cache

import (
	"time"

	"github.com/bluele/gcache"
)

const requestDurationMs = 400

type DataBaseStorage struct {
	gc gcache.Cache
}

func NewDataBaseStorage(size int) *DataBaseStorage {
	return &DataBaseStorage{
		gc: gcache.New(size).
			LFU().
			Build(),
	}
}

func (c *DataBaseStorage) getData(id int) (*Data, error) {
	time.Sleep(requestDurationMs * time.Millisecond)
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
