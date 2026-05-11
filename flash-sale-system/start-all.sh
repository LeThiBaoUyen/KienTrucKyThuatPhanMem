#!/bin/bash
# start-all.sh - Khởi động tất cả services

echo "🚀 Starting Flash Sale System..."
echo ""

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 1. Start Redis
echo -e "${BLUE}[1] Starting Redis...${NC}"
docker-compose up -d redis
sleep 3
echo -e "${GREEN}✓ Redis started${NC}"
echo ""

# 2. Build Common
echo -e "${BLUE}[2] Building Common Library...${NC}"
cd common
mvn clean package -q
cd ..
echo -e "${GREEN}✓ Common built${NC}"
echo ""

# 3. Function to start a service
start_service() {
    local name=$1
    local port=$2
    local dir=$3
    
    echo -e "${BLUE}[$((4 + $(echo $port | cut -d: -f2 | sed 's/^0*//') / 1000))] Starting $name (Port $port)...${NC}"
    cd "$dir"
    mvn clean package -q &
    wait $!
    java -jar target/*.jar &
    sleep 2
    cd ..
    echo -e "${GREEN}✓ $name started${NC}"
    echo ""
}

# 4. Start Services
start_service "PU1 - Product" "8081" "pu1-product-service"
start_service "PU2 - Cart" "8082" "pu2-cart-service"
start_service "PU3 - Order" "8083" "pu3-order-service"
start_service "PU4 - Inventory" "8084" "pu4-inventory-service"

# 5. Start Frontend
echo -e "${BLUE}[8] Starting Frontend...${NC}"
cd frontend
npm install -q
npm start &
cd ..
sleep 2
echo -e "${GREEN}✓ Frontend started${NC}"
echo ""

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}✅ All services started!${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo "Services URLs:"
echo "  📦 Product (PU1):   http://localhost:8081"
echo "  🛒 Cart (PU2):      http://localhost:8082"
echo "  💳 Order (PU3):     http://localhost:8083"
echo "  📊 Inventory (PU4): http://localhost:8084"
echo "  💻 Frontend:        http://localhost:3000"
echo "  📍 Redis:           localhost:6379"
echo ""
echo "Open Frontend: http://localhost:3000 in your browser 🎉"
