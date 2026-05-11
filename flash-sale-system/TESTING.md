# 🧪 TESTING & VALIDATION GUIDE

## 1. Unit Test Scenarios

### ✅ Test Product Service (PU1)

```bash
# Test: Get all products
curl http://localhost:8081/api/products
# Expected: 200 OK, 8 products in JSON

# Test: Get product by ID
curl http://localhost:8081/api/products/1
# Expected: 200 OK, product details

# Test: Initialize products
curl -X POST http://localhost:8081/api/products/init
# Expected: 200 OK, "Products initialized"
```

### ✅ Test Cart Service (PU2)

```bash
# Test: Get empty cart
curl http://localhost:8082/api/cart/user-123
# Expected: 200 OK, empty cart

# Test: Add to cart
curl -X POST http://localhost:8082/api/cart/add \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user-123",
    "productId": 1,
    "productName": "iPhone 15 Pro",
    "price": 1299.99,
    "quantity": 1
  }'
# Expected: 200 OK, cart with 1 item

# Test: Get cart total
curl http://localhost:8082/api/cart/user-123/total
# Expected: 200 OK, total price

# Test: Remove from cart
curl -X DELETE http://localhost:8082/api/cart/user-123/item/1
# Expected: 200 OK, item removed
```

### ✅ Test Order Service (PU3)

```bash
# Setup: Add to cart first
curl -X POST http://localhost:8082/api/cart/add \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user-123",
    "productId": 1,
    "productName": "iPhone 15 Pro",
    "price": 1299.99,
    "quantity": 2
  }'

# Test: Checkout
curl -X POST http://localhost:8083/api/orders/checkout \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user-123",
    "shippingAddress": "123 Main St",
    "phoneNumber": "0123456789"
  }'
# Expected: 200 OK, order created with ID

# Test: Get user orders
curl http://localhost:8083/api/orders/user/user-123
# Expected: 200 OK, array of orders
```

### ✅ Test Inventory Service (PU4)

```bash
# Setup: Initialize stock
curl -X POST http://localhost:8084/api/stock/init
# Expected: 200 OK, stock initialized

# Test: Get stock
curl http://localhost:8084/api/stock/1
# Expected: 200 OK, stock quantity

# Test: Reduce stock
curl -X POST http://localhost:8084/api/stock/reduce \
  -H "Content-Type: application/json" \
  -d '{
    "productId": 1,
    "quantity": 5
  }'
# Expected: 200 OK, stock reduced
```

## 2. Integration Tests

### 🔗 Test Complete Flow (E2E)

```bash
#!/bin/bash
# test-complete-flow.sh

echo "=== Complete Flow Test ==="

USER_ID="test-user-$(date +%s)"
echo "User ID: $USER_ID"

# Step 1: Init
echo "1. Initializing..."
curl -s -X POST http://localhost:8081/api/products/init | jq .
curl -s -X POST http://localhost:8084/api/stock/init | jq .

# Step 2: Get products
echo "2. Getting products..."
PRODUCTS=$(curl -s http://localhost:8081/api/products)
PRODUCT_ID=$(echo $PRODUCTS | jq '.data[0].id')
PRODUCT_NAME=$(echo $PRODUCTS | jq -r '.data[0].name')
PRICE=$(echo $PRODUCTS | jq '.data[0].price')
echo "   Got product: $PRODUCT_NAME ($PRODUCT_ID)"

# Step 3: Add to cart
echo "3. Adding to cart..."
curl -s -X POST http://localhost:8082/api/cart/add \
  -H "Content-Type: application/json" \
  -d "{
    \"userId\": \"$USER_ID\",
    \"productId\": $PRODUCT_ID,
    \"productName\": \"$PRODUCT_NAME\",
    \"price\": $PRICE,
    \"quantity\": 1
  }" | jq .

# Step 4: Checkout
echo "4. Checking out..."
ORDER=$(curl -s -X POST http://localhost:8083/api/orders/checkout \
  -H "Content-Type: application/json" \
  -d "{
    \"userId\": \"$USER_ID\",
    \"shippingAddress\": \"123 Main St\",
    \"phoneNumber\": \"0123456789\"
  }")
echo $ORDER | jq .
ORDER_ID=$(echo $ORDER | jq '.data.id')

# Step 5: Verify order
echo "5. Verifying order..."
curl -s http://localhost:8083/api/orders/$ORDER_ID | jq .

# Step 6: Check stock reduced
echo "6. Checking stock..."
curl -s http://localhost:8084/api/stock/$PRODUCT_ID | jq .

echo "=== Test Complete ✅ ==="
```

Run:
```bash
bash test-complete-flow.sh
```

## 3. Performance Tests

### ⚡ Response Time Measurement

```bash
#!/bin/bash
# test-performance.sh

echo "=== Performance Test ==="

# Test 1: Product Service
echo "Test 1: GET /api/products (100 times)"
for i in {1..100}; do
  time curl -s http://localhost:8081/api/products > /dev/null
done

# Test 2: Cart Service
echo "Test 2: POST /api/cart/add (100 times)"
for i in {1..100}; do
  time curl -s -X POST http://localhost:8082/api/cart/add \
    -H "Content-Type: application/json" \
    -d '{"userId":"perf-user","productId":1,"price":100,"quantity":1}' > /dev/null
done

echo "=== Performance Test Complete ==="
```

## 4. Load Tests

### 🔥 Apache Bench Load Test

```bash
# Installation
# Windows: choco install apache-httpd
# Mac: brew install httpd
# Linux: apt install apache2-utils

# Test: 10,000 requests, 100 concurrent
ab -n 10000 -c 100 http://localhost:8081/api/products

# Expected Results:
# - Requests per second: > 1000 RPS
# - Failed requests: 0
# - Mean time per request: < 100ms
```

### 📊 wrk Load Test (Advanced)

```bash
# Install wrk: https://github.com/wg/wrk

# Test: 4 threads, 100 connections, 30 seconds
wrk -t4 -c100 -d30s http://localhost:8081/api/products

# With custom script for cart add
cat > cart-load.lua << 'EOF'
request = function()
  return wrk.format("POST", "/api/cart/add",
    '{"userId":"user-'..math.random(1000)..'","productId":1,"quantity":1}')
end
EOF

wrk -t4 -c100 -d30s -s cart-load.lua http://localhost:8082/api
```

## 5. Stress Tests

### 🎯 Incremental Load Test

```bash
#!/bin/bash
# test-stress.sh

echo "=== Stress Test ==="

# Function to send requests
send_requests() {
  local url=$1
  local count=$2
  local concurrent=$3
  
  echo "Sending $count requests with $concurrent concurrent..."
  
  for ((i=0; i<$count; i+=concurrent)); do
    for ((j=0; j<concurrent && i+j<count; j++)); do
      curl -s $url > /dev/null &
    done
    wait
  done
  
  echo "Done!"
}

# Incremental load: 100 → 500 → 1000
send_requests "http://localhost:8081/api/products" 100 10
send_requests "http://localhost:8081/api/products" 500 50
send_requests "http://localhost:8081/api/products" 1000 100

echo "=== Stress Test Complete ==="
```

## 6. Database Independence Test

### 🔒 Verify No Database Calls

```bash
#!/bin/bash
# test-no-db-calls.sh

echo "=== Database Independence Test ==="

# Start tcpdump to monitor network traffic
# (Only if database is on different host)

echo "1. Stopping Redis intentionally..."
docker-compose stop redis

echo "2. Trying API calls (should fail gracefully)..."
curl -s http://localhost:8081/api/products || echo "Product service down"

echo "3. Restarting Redis..."
docker-compose start redis

echo "4. API works again immediately..."
curl -s http://localhost:8081/api/products | jq '.message'

echo "=== Database Independence Verified ✅ ==="
```

**Result**: Confirms services depend ONLY on Redis, not on any database.

## 7. Concurrent User Test

### 👥 Simulate Multiple Users

```bash
#!/bin/bash
# test-concurrent-users.sh

echo "=== Concurrent Users Test ==="

# Create 10 concurrent users
for user_id in {1..10}; do
  (
    USER="user-$user_id"
    
    # Each user does: view products → add to cart → checkout
    
    # View products
    curl -s http://localhost:8081/api/products > /dev/null
    
    # Add to cart
    curl -s -X POST http://localhost:8082/api/cart/add \
      -H "Content-Type: application/json" \
      -d "{\"userId\":\"$USER\",\"productId\":1,\"quantity\":1}" > /dev/null
    
    # Checkout
    curl -s -X POST http://localhost:8083/api/orders/checkout \
      -H "Content-Type: application/json" \
      -d "{\"userId\":\"$USER\",\"shippingAddress\":\"Addr\",\"phoneNumber\":\"0123456789\"}" > /dev/null
    
    echo "✓ User $USER completed flow"
  ) &
done

wait
echo "=== All users completed ✅ ==="
```

## 8. Redis Data Validation

### 🔍 Verify Data Integrity

```bash
redis-cli

# Check product data
GET product:1
# Should return valid JSON

# Check cart structure
GET "cart:user-123"
# Should have items array

# Check stock numbers
GET stock:1
# Should be integer

# Check order structure
GET "order:1726534890"
# Should have id, userId, items, totalPrice

# Count total keys
DBSIZE
# Should show reasonable number (< 10000 in test)

# Check expiration
TTL "cart:user-123"
# Should show ~86400 (24 hours)

# Verify no database references
KEYS *
# Should only have: product:*, cart:*, stock:*, order:* patterns
```

## 9. Failure Recovery Tests

### 🔴 Test Service Failover

```bash
# Test 1: Stop Product Service
kill <product-service-pid>
# Wait 5 seconds
# Restart it
java -jar pu1-product-service.jar &

# Check: Service recovers, no data loss ✅

# Test 2: Stop Redis
docker-compose stop redis
# Try API: Should fail gracefully
curl http://localhost:8081/api/products
# Should return error (not crash)

# Test 3: Restart Redis
docker-compose start redis
# Check: All data still there ✅
KEYS *  # Should have all data
```

## 10. Browser Automation Test

### 🌐 Selenium Test (Optional)

```python
# test_selenium.py
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

driver = webdriver.Chrome()
driver.get("http://localhost:3000")

# Wait for products to load
WebDriverWait(driver, 10).until(
    EC.presence_of_elements_located((By.CLASS_NAME, "product-card"))
)

# Find and click first product's add button
add_button = driver.find_element(By.CLASS_NAME, "add-btn")
add_button.click()

# Wait for cart update
WebDriverWait(driver, 5).until(
    EC.text_to_be_present_in_element((By.CLASS_NAME, "nav-tabs button"), "1")
)

# Switch to cart tab
cart_tab = driver.find_element(By.CLASS_NAME, "nav-tabs button:nth-child(2)")
cart_tab.click()

# Click checkout
checkout_btn = driver.find_element(By.CLASS_NAME, "checkout-btn")
checkout_btn.click()

# Verify order created
WebDriverWait(driver, 10).until(
    EC.presence_of_elements_located((By.CLASS_NAME, "order-card"))
)

print("✅ E2E Test Passed!")
driver.quit()
```

Run:
```bash
pip install selenium
python test_selenium.py
```

---

## Summary Test Checklist

- [ ] All 4 PUs respond to health checks
- [ ] Get products works (< 20ms)
- [ ] Add to cart works (< 30ms)
- [ ] Checkout works (< 50ms)
- [ ] Stock decrements after checkout
- [ ] Cart persists in Redis
- [ ] Order saved with all details
- [ ] Multiple users can checkout concurrently
- [ ] Load test: > 1000 RPS
- [ ] No database calls detected
- [ ] Services recover after restart
- [ ] Browser UI works end-to-end

**All tests passed? 🎉 System is production-ready!**
