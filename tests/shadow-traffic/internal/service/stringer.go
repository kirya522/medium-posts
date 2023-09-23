package service

import (
	"math/rand"
)

type Result struct {
	S1     string
	S2     string
	Result string
}

func StringJoiner1(str1, str2 string) Result {
	return Result{Result: str1 + str2, S1: str1, S2: str2}
}

func StringJoiner2(str1, str2 string) Result {
	// портим половину ответов
	if rand.Intn(100) < 50 {
		str1 += "50"
	}

	return Result{Result: str1 + str2, S1: str1, S2: str2}
}
