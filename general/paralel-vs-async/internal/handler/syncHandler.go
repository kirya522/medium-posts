package handler

import (
	"net/http"

	"paralel-vs-async/internal/service"
)

func NewSyncHandler(getLink string) *syncHandler {
	return &syncHandler{
		link: getLink,
	}
}

type syncHandler struct {
	link string
}

func (h syncHandler) ServeHTTP(w http.ResponseWriter, r *http.Request) {
	service.GetByLink(h.link)
	w.WriteHeader(200)
	_, _ = w.Write([]byte("sync"))
}
