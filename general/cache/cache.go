package cache

import "github.com/bluele/gcache"

type SizedCachedStorage struct {
	gc gcache.Cache
}

func NewSizedCachedStorage(size int) *SizedCachedStorage {
	return &SizedCachedStorage{
		gc: gcache.New(size).
			LFU().
			Build(),
	}
}

func (c *SizedCachedStorage) getData(id int) (*Data, error) {
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

func (c *SizedCachedStorage) setData(data Data) error {
	err := c.gc.Set(data.id, data)
	if err != nil {
		return err
	}
	return nil
}
