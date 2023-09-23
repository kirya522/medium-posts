package main

import (
	"log"
	"net/http"

	"github.com/gorilla/mux"
	"kirya522.tech/shadow-traffic/internal/handler"
)

func main() {
	r := mux.NewRouter()
	r.HandleFunc("/request/{s1}/{s2}", handler.WithShadowHandler).Methods(http.MethodGet)
	log.Fatal(http.ListenAndServe("localhost:8080", r))
}
