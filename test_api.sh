#!/bin/bash

BASE_URL="http://localhost:9090/api"

echo "=== 1. Register a new user ==="
USER_RESPONSE=$(curl -s -X POST $BASE_URL/users \
  -H "Content-Type: application/json" \
  -d '{"name": "Test User", "email": "testuser_'$(date +%s)'@example.com", "password": "password123"}')
echo $USER_RESPONSE | jq .

EMAIL=$(echo $USER_RESPONSE | jq -r '.email')
USER_ID=$(echo $USER_RESPONSE | jq -r '.id')
echo ""

echo "=== 2. Log in and get JWT token ==="
LOGIN_RESPONSE=$(curl -s -X POST $BASE_URL/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "'$EMAIL'", "password": "password123"}')
echo $LOGIN_RESPONSE | jq .

TOKEN=$(echo $LOGIN_RESPONSE | jq -r '.token')
echo ""

echo "=== 3. Try fetching tasks WITHOUT token (Expecting 403/401) ==="
curl -i -s -X GET $BASE_URL/users/$USER_ID/tasks
echo -e "\n"

echo "=== 4. Create a task with the JWT token ==="
TASK_RES=$(curl -s -X POST $BASE_URL/users/$USER_ID/tasks \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"title": "Test Task", "description": "This is a test task for live testing."}')
echo $TASK_RES | jq .
TASK_ID=$(echo $TASK_RES | jq -r '.id')
echo ""

echo "=== 5. Update Task Status using PATCH ==="
PATCH_RES=$(curl -s -X PATCH $BASE_URL/users/$USER_ID/tasks/$TASK_ID/status \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"status": "IN_PROGRESS"}')
echo $PATCH_RES | jq .
echo ""

echo "=== 6. Fetch paginated tasks ==="
curl -s -X GET "$BASE_URL/users/$USER_ID/tasks?page=0&size=5" \
  -H "Authorization: Bearer $TOKEN" | jq .
echo ""

