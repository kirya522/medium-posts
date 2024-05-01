package service

import (
	"fmt"
	"io"
	"log"
	"net/http"
	"os"
)

func GetByLink(link string) {
	response, err := http.Get(link)

	if err != nil {
		fmt.Print(err.Error())
		os.Exit(1)
	}

	_, err = io.ReadAll(response.Body)
	if err != nil {
		log.Fatal(err)
	}
	fmt.Println("executed")
}
