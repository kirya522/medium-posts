package main

import (
	"log"
	"net/http"

	"github.com/gorilla/mux"
	"paralel-vs-async/internal/handler"
)

func main() {
	link := "https://httpbin.org/delay/10"

	r := mux.NewRouter()

	sync := handler.NewSyncHandler(link)
	r.Handle("/sync", sync)

	async := handler.NewAsyncHandler(link)
	r.Handle("/async", async)

	log.Fatal(http.ListenAndServe(":8080", r))
}
