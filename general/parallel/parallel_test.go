package parallel

import (
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

func TestDataRaceFix(t *testing.T) {
	tries := 100
	for i := 0; i < tries; i++ {
		counter := parallelCountFix()
		assert.Equal(t, 10, counter)
	}
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

func TestDeadlockFix(t *testing.T) {
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
