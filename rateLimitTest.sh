#!/bin/bash

URL="http://localhost:8080/api/user/events"

for i in {1..1000}
do
  curl -s -o /dev/null -w "%{http_code}\n" \
    -X POST "$URL" \
    -H "accept: */*" \
    -H "Content-Type: application/json" \
    -d '{
      "userId": "test-user",
      "eventType": "stress-test",
      "path": "/test",
      "createdAt": "2026-05-20T21:04:02.248Z",
      "metadataJson": "{}"
    }' &
done

wait