package parallel

import "sync"

func parallelCount() int {
	var counter int
	var wg sync.WaitGroup

	for i := 0; i < 10; i++ {
		wg.Add(1)
		go func() {
			defer wg.Done()
			counter++ // Гонка данных!
		}()
	}

	wg.Wait()
	return counter
}

func parallelCountFix() int {
	var counter int
	var wg sync.WaitGroup
	var mu sync.Mutex

	for i := 0; i < 10; i++ {
		wg.Add(1)
		go func() {
			defer wg.Done()
			mu.Lock()
			counter++ // Решение через мьютекс
			mu.Unlock()
		}()
	}

	wg.Wait()
	return counter
}
