package handler

import (
	"encoding/json"
	"math/rand"
	"net/http"

	"github.com/gorilla/mux"
	"kirya522.tech/shadow-traffic/internal/service"
	"kirya522.tech/shadow-traffic/internal/utils"
)

var (
	percentOfTraffic = 50
)

func WithShadowHandler(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	s1 := vars["s1"]
	s2 := vars["s2"]

	result := service.StringJoiner1(s1, s2)

	go func() {
		if rand.Intn(100) < percentOfTraffic {
			result2 := service.StringJoiner2(s1, s2)
			utils.CompareAndAnalyze(result, result2)
		}
	}()

	w.Header().Set("Content-Type", "application/json")
	// resp
	marshal, err := json.Marshal(result)
	if err != nil {
		w.WriteHeader(http.StatusInternalServerError)
		return
	}
	_, _ = w.Write(marshal)
}
