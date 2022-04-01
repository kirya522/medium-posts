#!/usr/local/bin/bash

curl -v -X PUT 'http://localhost:8080/event' \
  -H 'Content-Type: application/json' \
  -d '{"id":1, "name":"test", "startDate":1648845477, "endDate":1648875477 }'

curl -v 'http://localhost:8080/event/1'