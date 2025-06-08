package parallel

import (
	"fmt"
	"sync"
	"sync/atomic"
	"testing"
	"time"

	"github.com/stretchr/testify/assert"
)

// Race
func TestDataRace(t *testing.T) {
	// Проблема: Аномалии в данных, не сходится результат
	// Как найти: go test -v --race

	tries := 100
	for i := 0; i < tries; i++ {
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

		assert.Equal(t, 10, counter)
	}
}

func TestDataRaceFixMutex(t *testing.T) {
	// Проблема: Аномалии в данных, не сходится результат
	// Решение: Синхронизация, мьютексы, блокировки, etc..,

	tries := 100
	for i := 0; i < tries; i++ {
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

		assert.Equal(t, 10, counter)
	}
}

func TestDataRaceFixAtomic(t *testing.T) {
	// Проблема: Аномалии в данных, не сходится результат
	// Решение: Lock-free алгоритмы и cas

	tries := 100
	for i := 0; i < tries; i++ {
		var counter atomic.Int64
		var wg sync.WaitGroup

		for i := 0; i < 10; i++ {
			wg.Add(1)
			go func() {
				defer wg.Done()
				counter.Add(1)
			}()
		}
		wg.Wait()

		assert.Equal(t, 10, int(counter.Load()))
	}
}

// Deadlock

func TestDeadlock(t *testing.T) {
	// Проблема: Выполнение зависает, взаимные ресурсы заблокированы
	// Этот тест застрянет из-за deadlock

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
}

func TestDeadlockFixTryLock(t *testing.T) {
	// Проблема: Выполнение зависает, взаимные ресурсы заблокированы
	// Этот тест не застрянет из-за deadlock, добавлены таймауты

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
}

func TestDeadlockFixCanonical(t *testing.T) {
	// Проблема: Выполнение зависает, взаимные ресурсы заблокированы
	// Этот тест не застрянет из-за deadlock, добавлена сортировка ресурсов

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
	// Проблема: Голодание потоков
	// Один из потоков постоянно ждет

	var lock sync.Mutex

	go greedyWorker(&lock)
	go greedyWorker(&lock)
	go starvingWorker(&lock)

	time.Sleep(5 * time.Second)
}

func greedyWorker(lock *sync.Mutex) {
	for {
		lock.Lock()
		// Долго держим лок
		fmt.Println("Greedy worker: получил доступ!")
		time.Sleep(100 * time.Millisecond)
		lock.Unlock()
		time.Sleep(10 * time.Millisecond)
	}
}

func starvingWorker(lock *sync.Mutex) {
	for {
		lock.Lock()
		fmt.Println("Starving worker: наконец-то получил доступ!")
		lock.Unlock()
		time.Sleep(500 * time.Millisecond)
	}
}

func TestLiveLock(t *testing.T) {
	// Проблема: Система работает, но реальных действий не происходит
	// Два почтальона должны взять письмо с одного и того же стола.
	// Каждый раз, когда один видит, что другой пытается взять письмо, он уступает, чтобы не мешать. В итоге — бесконечное уступание.

	var mailbox Mailbox
	mailbox.available = 1

	var busyA, busyB int32

	go politePostman("Почтальон A", &mailbox, &busyB)
	go politePostman("Почтальон B", &mailbox, &busyA)

	time.Sleep(3 * time.Second)
}

type Mailbox struct {
	available int32 // 1 — письмо есть, 0 — нет
}

func politePostman(name string, mailbox *Mailbox, partnerBusy *int32) {
	for {
		if atomic.LoadInt32(&mailbox.available) == 0 {
			fmt.Printf("%s: письмо уже забрали, ухожу\n", name)
			return
		}

		if atomic.LoadInt32(partnerBusy) == 1 {
			fmt.Printf("%s: вижу, что партнер занят, жду...\n", name)
			time.Sleep(100 * time.Millisecond)
			continue
		}

		// Говорим: я занят
		atomic.StoreInt32(partnerBusy, 1)
		fmt.Printf("%s: начинает брать\n", name)

		// Проверим еще раз перед тем как взять, тут нет синхронизации!
		if atomic.LoadInt32(&mailbox.available) == 1 {
			// Вежливо уступаем снова
			fmt.Printf("%s: вдруг партнер тоже начал брать, уступаю...\n", name)
			atomic.StoreInt32(partnerBusy, 0)
			time.Sleep(100 * time.Millisecond)
			continue
		}

		// Берем письмо
		atomic.StoreInt32(&mailbox.available, 0)
		fmt.Printf("%s: взял письмо и пошел доставлять!\n", name)
		atomic.StoreInt32(partnerBusy, 0)
		return
	}
}

func confidentPostman(name string, mailbox *Mailbox) {
	for {
		if atomic.LoadInt32(&mailbox.available) == 1 {
			fmt.Printf("%s: решительно забираю письмо!\n", name)
			atomic.StoreInt32(&mailbox.available, 0)
			return
		}
		fmt.Printf("%s: письма нет, ухожу\n", name)
		return
	}
}

func TestLiveLockFix(t *testing.T) {
	// Проблема: Система работает, но реальных действий не происходит
	// Теперь один почтальон берет всегда и не проверяет, возможны дубли обработок
	// Дополнительная защита как идемпотентность по id письма защитит от конфликтов

	var mailbox Mailbox
	mailbox.available = 1

	var busyB int32

	go politePostman("Почтальон A", &mailbox, &busyB)
	go confidentPostman("Почтальон B", &mailbox)

	time.Sleep(3 * time.Second)
}
