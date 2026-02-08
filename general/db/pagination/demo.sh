#!/bin/bash

# db setup
docker compose up -d

# Run app
go run main.go

# OFFSET
curl "localhost:8080/offset?page=0"
curl "localhost:8080/offset?page=1000"
curl "localhost:8080/offset?page=5000"

# CURSOR
curl "localhost:8080/cursor"
curl "localhost:8080/cursor?cursor=..."

# PAGE + CURSOR
curl "localhost:8080/page?page=500"