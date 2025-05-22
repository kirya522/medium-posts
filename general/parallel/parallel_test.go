package parallel

import (
	"fmt"
	"sync"
	"testing"
	"time"

	"github.com/stretchr/testify/assert"
)

// Race
func TestDataRace(t *testing.T) {
	tries := 100
	for i := 0; i < tries; i++ {
		counter := parallelCount()
		assert.Equal(t, 10, counter)
	}
}

func TestDataRaceFixMutex(t *testing.T) {
	tries := 100
	for i := 0; i < tries; i++ {
		counter := parallelCountFix()
		assert.Equal(t, 10, counter)
	}
}

func TestDataRaceFixAtomic(t *testing.T) {
	tries := 100
	for i := 0; i < tries; i++ {
		counter := parallelCountAtomicFix()
		assert.Equal(t, 10, counter)
	}
}

// Deadlock

func TestDeadlock(t *testing.T) {
	var mu1, mu2 sync.Mutex
	var wg sync.WaitGroup

	wg.Add(2)

	// чтобы не останавливалось
	go func() {
		for {
			t.Log("running")
			time.Sleep(1 * time.Second)
		}
	}()

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

func TestDeadlockFixTryLock(t *testing.T) {
	var mu1, mu2 sync.Mutex
	var wg sync.WaitGroup

	wg.Add(2)

	// чтобы не останавливалось
	go func() {
		for {
			t.Log("running")
			time.Sleep(1 * time.Second)
		}
	}()

	go func() {
		defer wg.Done()
		mu1.Lock()
		defer mu1.Unlock()

		time.Sleep(1 * time.Second)

		if !mu2.TryLock() {
			t.Log("Goroutine 1 break")
			return
		}
		defer mu2.Unlock()

		t.Log("Goroutine 1 finished")
	}()

	go func() {
		defer wg.Done()
		mu2.Lock()
		defer mu2.Unlock()

		time.Sleep(1 * time.Second)

		if !mu1.TryLock() {
			t.Log("Goroutine 2 break")
			return
		}
		defer mu1.Unlock()

		t.Log("Goroutine 2 finished")
	}()

	wg.Wait()
	// Этот тест застрянет из-за deadlock
}

func TestDeadlockFixCanonical(t *testing.T) {
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
}

func TestStarvation(t *testing.T) {
	var mu sync.Mutex
	var wg sync.WaitGroup
	wg.Add(1)

	// "Жадная" горутина
	go func() {
		fmt.Println("Long operation started")
		defer wg.Done()
		mu.Lock()
		defer mu.Unlock()
		time.Sleep(3 * time.Second)
		fmt.Println("Long operation finished")
	}()

	time.Sleep(10 * time.Millisecond)

	// Голодные горутины
	for i := 0; i < 5; i++ {
		wg.Add(1)
		go func(id int) {
			fmt.Printf("Goroutine %d started\n", id)
			mu.Lock()
			defer mu.Unlock()
			defer wg.Done()
			fmt.Printf("Goroutine %d executed\n", id)
		}(i)
	}

	wg.Wait()
}

func TestLiveLock(t *testing.T) {
	var mu sync.Mutex
	var wg sync.WaitGroup

	wg.Add(2)

	go func() {
		for {
			if mu.TryLock() {
				fmt.Println("Goroutine 1 acquired lock")
				mu.Unlock()
				wg.Done()
			}
			time.Sleep(1000 * time.Millisecond)
		}
	}()

	go func() {
		defer wg.Done()
		for {
			if mu.TryLock() {
				fmt.Println("Goroutine 2 acquired lock")
				mu.Unlock()
				wg.Done()
			}
			time.Sleep(1000 * time.Millisecond)
		}
	}()

	wg.Wait()
	fmt.Println("Program finished")
}
