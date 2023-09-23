package utils

import (
	"fmt"

	"github.com/wI2L/jsondiff"
)

func CompareAndAnalyze(a, b interface{}) {
	// сравниваем
	// к библиотеке можно прикрутить свой парсер и ответы переконвертить в метрики
	compare, err := jsondiff.Compare(a, b)
	if err != nil {
		log(err.Error())
		return
	}

	if compare == nil {
		log("same")
		return
	}
	log(compare.String())
}

func log(msg string) {
	fmt.Println(msg)
}
