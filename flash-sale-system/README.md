# Flash Sale System - Space-Based Architecture

## Kiến trúc hệ thống
```
┌─────────────────────────────────────────────────────────────┐
│                     FRONTEND (React)                        │
│                    Port: 3000                               │
└─────────────────────────────────────────────────────────────┘
                            ↓↑
        ┌───────────────────┼───────────────────┐
        ↓                   ↓                   ↓
┌──────────────┐  ┌──────────────┐  ┌──────────────┐
│ PU1-Product  │  │  PU2-Cart    │  │ PU3-Order    │
│  :8081       │  │  :8082       │  │  :8083       │
└──────────────┘  └──────────────┘  └──────────────┘
        ↑                   ↑                   ↑
        └───────────────────┼───────────────────┘
                            ↓
                     ┌─────────────┐
                     │ PU4-Inventory│
                     │  :8084      │
                     └─────────────┘
                            ↓↑
                     ┌──────────────┐
                     │   Redis DG   │
                     │  :6379       │
                     │(Data Grid)   │
                     └──────────────┘
```

## Yêu cầu chính
- ✅ Chịu tải cao (1000+ req/s)
- ✅ Tránh DB bottleneck
- ✅ Low latency xử lý
- ✅ Data Grid (Redis)
- ✅ Memory-based cache

## Cấu trúc thư mục
```
flash-sale-system/
├── docker-compose.yml          # Redis + Infrastructure
├── common/                      # Shared models/configs
├── pu1-product-service/        # Product Processing Unit
├── pu2-cart-service/           # Cart Processing Unit
├── pu3-order-service/          # Order Processing Unit
├── pu4-inventory-service/      # Inventory Processing Unit
├── frontend/                   # React Frontend
└── postman-collection.json    # Load test collection
```

## Cách chạy hệ thống

### 1. Start Redis (Data Grid)
```bash
docker-compose up -d redis
```

### 2. Build & Run các Services
```bash
# Terminal 1: PU1 - Product Service
cd pu1-product-service
mvn clean package
java -jar target/pu1-product-service.jar

# Terminal 2: PU2 - Cart Service
cd pu2-cart-service
mvn clean package
java -jar target/pu2-cart-service.jar

# Terminal 3: PU3 - Order Service
cd pu3-order-service
mvn clean package
java -jar target/pu3-order-service.jar

# Terminal 4: PU4 - Inventory Service
cd pu4-inventory-service
mvn clean package
java -jar target/pu4-inventory-service.jar

# Terminal 5: Frontend
cd frontend
npm install
npm start
```

## API Endpoints

### PU1 - Product Service (8081)
- `GET /api/products` - Danh sách sản phẩm
- `GET /api/products/{id}` - Chi tiết sản phẩm

### PU2 - Cart Service (8082)
- `GET /api/cart/{userId}` - Lấy giỏ hàng
- `POST /api/cart/add` - Thêm vào giỏ
- `POST /api/cart/remove` - Xóa khỏi giỏ
- `POST /api/cart/clear` - Xóa toàn bộ giỏ

### PU3 - Order Service (8083)
- `POST /api/orders/checkout` - Đặt hàng
- `GET /api/orders/{userId}` - Lấy đơn hàng

### PU4 - Inventory Service (8084)
- `GET /api/stock/{productId}` - Lấy tồn kho
- `POST /api/stock/reduce` - Giảm tồn kho

## Kịch bản Test
1. ✅ Load danh sách sản phẩm từ Redis
2. ✅ Add to cart
3. ✅ Checkout
4. ✅ Stock giảm real-time
5. ✅ Load test (1000+ req/s)

## Bonus
- [ ] Dùng Hazelcast thay Redis
- [ ] Implement locking (SETNX)
- [ ] Queue xử lý async
- [ ] Simulate load test (Postman)

## Tiêu chí chấm điểm
- ✅ Đúng Space-Based (3 điểm)
- ✅ Không phụ thuộc DB (2.5 điểm)
- ✅ Dùng Data Grid đúng (2 điểm)
- ✅ Flow nhanh, không nghẽn (1.5 điểm)
- ✅ Demo scale clone PU (1 điểm)

## Công nghệ sử dụng
- **Backend**: Spring Boot 3.x
- **Data Grid**: Redis
- **Frontend**: React 18
- **Build**: Maven
- **Container**: Docker
