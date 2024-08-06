package cache

import (
	"time"

	"github.com/bluele/gcache"
)

type SizedTimedCachedStorage struct {
	gc         gcache.Cache
	expiration time.Duration
}

func NewSizedTimedCachedStorage(size int, expiration time.Duration) *SizedTimedCachedStorage {
	return &SizedTimedCachedStorage{
		gc: gcache.New(size).
			LRU().
			Build(),
		expiration: expiration,
	}
}

func (c *SizedTimedCachedStorage) getData(id int) (*Data, error) {
	rawData, err := c.gc.Get(id)
	if err != nil {
		if err == gcache.KeyNotFoundError {
			return nil, nil
		}
		return nil, err
	}
	ret := rawData.(Data)
	return &ret, nil
}

func (c *SizedTimedCachedStorage) setData(data Data) error {
	err := c.gc.SetWithExpire(data.id, data, c.expiration)
	if err != nil {
		return err
	}
	return nil
}
