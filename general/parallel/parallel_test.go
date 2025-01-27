package parallel

import (
	"math/rand"
	"sync"
	"testing"
	"time"
)

// Race

func TestDataRace(t *testing.T) {
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
	t.Logf("Final counter: %d", counter)
	// Запустите этот тест с флагом -race: go test -race
}

func TestDataRaceSolution(t *testing.T) {
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
	t.Logf("Final counter: %d", counter)
}

// Deadlock

func TestDeadlock(t *testing.T) {
	var mu1, mu2 sync.Mutex
	var wg sync.WaitGroup

	wg.Add(2)

	go func() {
		defer wg.Done()
		mu1.Lock()
		defer mu1.Unlock()

		time.Sleep(1 * time.Second)

		mu2.Lock()
		defer mu2.Unlock()

		t.Log("Goroutine 1 finished")
	}()

	go func() {
		defer wg.Done()
		mu2.Lock()
		defer mu2.Unlock()

		time.Sleep(1 * time.Second)

		mu1.Lock()
		defer mu1.Unlock()

		t.Log("Goroutine 2 finished")
	}()

	wg.Wait()
	// Этот тест застрянет из-за deadlock
}

func TestDeadlockSolution(t *testing.T) {
	var mu1, mu2 sync.Mutex
	var wg sync.WaitGroup

	wg.Add(2)

	go func() {
		defer wg.Done()
		mu1.Lock()
		defer mu1.Unlock()

		time.Sleep(1 * time.Second)

		mu2.Lock()
		defer mu2.Unlock()

		t.Log("Goroutine 1 finished")
	}()

	go func() {
		defer wg.Done()
		mu1.Lock()
		defer mu1.Unlock()

		time.Sleep(1 * time.Second)

		mu2.Lock()
		defer mu2.Unlock()

		t.Log("Goroutine 2 finished")
	}()

	wg.Wait()
	// Этот тест застрянет из-за deadlock
}

// Голодание потоков

func TestStarvation(t *testing.T) {
	var mu sync.Mutex
	var wg sync.WaitGroup

	wg.Add(2)

	go func() {
		defer wg.Done()
		mu.Lock()
		defer mu.Unlock()
		t.Log("Long operation started")
		// Долгая операция
		time.Sleep(3 * time.Second)
		t.Log("Long operation finished")
	}()

	go func() {
		defer wg.Done()
		time.Sleep(500 * time.Millisecond) // Небольшая задержка перед попыткой захвата
		mu.Lock()
		defer mu.Unlock()
		t.Log("Short operation finished")
	}()

	start := time.Now()
	wg.Wait()
	t.Logf("Execution completed in %v", time.Since(start))
}

func TestStarvationSolution(t *testing.T) {
	var mu sync.Mutex
	var cond = sync.NewCond(&mu)
	var wg sync.WaitGroup
	isLongDone := false

	wg.Add(2)

	go func() {
		defer wg.Done()
		mu.Lock()
		t.Log("Long operation started")
		// Долгая операция
		time.Sleep(3 * time.Second)
		isLongDone = true
		cond.Broadcast() // Уведомляем, что операция завершена
		mu.Unlock()
		t.Log("Long operation finished")
	}()

	go func() {
		defer wg.Done()
		mu.Lock()
		for !isLongDone {
			cond.Wait() // Ждём завершения долгой операции
		}
		t.Log("Short operation finished")
		mu.Unlock()
	}()

	start := time.Now()
	wg.Wait()
	t.Logf("Execution completed in %v", time.Since(start))
}

// performance issues

func TestLatencyAndInstability(t *testing.T) {
	sharedMap := make(map[int]int)
	var mu sync.Mutex
	var wg sync.WaitGroup

	for i := 0; i < 10; i++ {
		wg.Add(1)
		go func(id int) {
			defer wg.Done()
			for j := 0; j < 100; j++ {
				mu.Lock()
				sharedMap[rand.Intn(100)] = id
				mu.Unlock()
				time.Sleep(time.Millisecond * time.Duration(rand.Intn(10)))
			}
		}(i)
	}

	for i := 0; i < 5; i++ {
		wg.Add(1)
		go func(id int) {
			defer wg.Done()
			for j := 0; j < 100; j++ {
				mu.Lock()
				_ = sharedMap[rand.Intn(100)]
				mu.Unlock()
				time.Sleep(time.Millisecond * time.Duration(rand.Intn(10)))
			}
		}(i)
	}

	start := time.Now()
	wg.Wait()
	t.Logf("Execution completed in %v", time.Since(start))
}

func TestLatencyAndInstabilitySolution(t *testing.T) {
	sharedMap := make(map[int]int)
	var rwMu sync.RWMutex
	var wg sync.WaitGroup

	for i := 0; i < 10; i++ {
		wg.Add(1)
		go func(id int) {
			defer wg.Done()
			for j := 0; j < 100; j++ {
				rwMu.Lock()
				sharedMap[rand.Intn(100)] = id
				rwMu.Unlock()
				time.Sleep(time.Millisecond * time.Duration(rand.Intn(10)))
			}
		}(i)
	}

	for i := 0; i < 5; i++ {
		wg.Add(1)
		go func(id int) {
			defer wg.Done()
			for j := 0; j < 100; j++ {
				rwMu.RLock()
				_ = sharedMap[rand.Intn(100)]
				rwMu.RUnlock()
				time.Sleep(time.Millisecond * time.Duration(rand.Intn(10)))
			}
		}(i)
	}

	start := time.Now()
	wg.Wait()
	t.Logf("Execution completed in %v", time.Since(start))
}
