package handler

import (
	"net/http"

	"paralel-vs-async/internal/service"
)

func NewAsyncHandler(getLink string) *asyncHandler {
	return &asyncHandler{
		link: getLink,
	}
}

type asyncHandler struct {
	link string
}

func (h asyncHandler) ServeHTTP(w http.ResponseWriter, r *http.Request) {
	go service.GetByLink(h.link)

	w.WriteHeader(201)
	w.Write([]byte("async"))
}
