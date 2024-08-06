package cache

import (
	"testing"
	"time"

	"github.com/stretchr/testify/assert"
)

func TestJustDatabase(t *testing.T) {
	c := NewDataBaseStorage()
	data := Data{id: 1, name: "test"}
	_ = c.setData(data)

	gotData, _ := c.getData(data.id)
	assert.NotNil(t, gotData)
}

func TestNoCacheSlow(t *testing.T) {
	c := NewDataBaseStorage()
	data := Data{id: 1, name: "test"}
	_ = c.setData(data)

	for i := 0; i < 10; i++ {
		gotData, _ := c.getData(data.id)
		assert.NotNil(t, gotData)
	}
}

func TestCacheForSpeedUpSingle(t *testing.T) {
	c := NewSizedCachedStorage(10)
	db := NewDataBaseStorage()
	st := NewDataBaseStorageWithCache(db, c)
	data := Data{id: 1, name: "test"}
	_ = db.setData(data)

	// 1st
	_, _ = st.getData(data.id)
	// 2nd
	gotData, _ := st.getData(data.id)
	assert.NotNil(t, gotData)
}

func TestCacheForLoad(t *testing.T) {
	c := NewSizedCachedStorage(10)
	db := NewDataBaseStorage()
	st := NewDataBaseStorageWithCache(db, c)
	data := Data{id: 1, name: "test"}
	_ = st.setData(data)

	for i := 0; i < 100000; i++ {
		// can resist load
		gotData, _ := st.getData(data.id)
		assert.NotNil(t, gotData)
	}
}

func TestCacheForOutage(t *testing.T) {
	c := NewSizedCachedStorage(10)
	db := NewDataBaseStorage()
	st := NewDataBaseStorageWithCache(db, c)
	data := Data{id: 2, name: "test"}

	// set to cache
	_ = c.setData(data)
	// verify storage broken
	_, err := db.getData(data.id)
	assert.Error(t, err)

	for i := 0; i < 100000; i++ {
		// can resist load
		gotData, _ := st.getData(data.id)
		assert.NotNil(t, gotData)
	}
}

func TestCacheOverload(t *testing.T) {
	size := 10
	c := NewSizedCachedStorage(size)
	db := NewDataBaseStorage()
	st := NewDataBaseStorageWithCache(db, c)

	// after first loop will slow
	for i := size; i < size*3; i++ {
		newData := Data{i, "name"}
		_ = st.setData(newData)
		_, _ = st.getData(i - size)
	}
}

func TestKeyExpired(t *testing.T) {
	size := 10
	// expires fast
	c := NewSizedTimedCachedStorage(size, 1*time.Nanosecond)
	db := NewDataBaseStorage()
	st := NewDataBaseStorageWithCache(db, c)

	for i := 0; i < 10; i++ {
		data := Data{id: 1}
		_ = st.setData(data)
		_, _ = st.getData(1)
	}
}

func TestKeyNotExpired(t *testing.T) {
	size := 10
	c := NewSizedTimedCachedStorage(size, 2*time.Second)
	db := NewDataBaseStorage()
	st := NewDataBaseStorageWithCache(db, c)

	// after first loop will slow
	for i := 0; i < 10000; i++ {
		data := Data{id: 1}
		_ = st.setData(data)
		_, _ = st.getData(1)
	}
}
