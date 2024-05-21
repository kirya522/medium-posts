package cache

type DataBaseStorageWithCache struct {
	db Storage
	c  Storage
}

func (dbc *DataBaseStorageWithCache) getData(id int) (*Data, error) {
	data, _ := dbc.c.getData(id)
	if data != nil {
		return data, nil
	}

	getData, err := dbc.db.getData(id)
	if err != nil {
		return nil, err
	}

	if getData != nil {
		_ = dbc.c.setData(*getData)
	}

	return getData, nil
}

func (dbc *DataBaseStorageWithCache) setData(data Data) error {
	err := dbc.db.setData(data)
	if err != nil {
		return err
	}
	_ = dbc.c.setData(data)

	return nil
}
