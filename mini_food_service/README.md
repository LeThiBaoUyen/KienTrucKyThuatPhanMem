# Mini Food Service (Service-Based Architecture)

Food Service cho bai toan Mini Food Ordering System.

## 1. Tech Stack

- Java 17
- Spring Boot 3
- Spring Web, Spring Data JPA, Validation
- H2 Database (in-memory)

## 2. Chay ung dung

```bash
mvn spring-boot:run
```

Mac dinh chay o cổng: `8082`

## 3. Cau truc response chuan

Tat ca API deu tra ve theo format:

```json
{
  "timestamp": "2023-10-27T10:00:00Z",
  "success": true,
  "code": 200,
  "message": "Thong bao",
  "data": {},
  "errors": null
}
```

Neu loi:

```json
{
  "timestamp": "2023-10-27T10:00:00Z",
  "success": false,
  "code": 400,
  "message": "Validation Error",
  "data": null,
  "errors": [
    { "field": "name", "message": "name khong duoc de trong" }
  ]
}
```

## 4. Endpoints

### 4.1 Lay danh sach mon an

- Method: `GET`
- URL: `/api/foods`

### 4.2 Lay chi tiet mon an

- Method: `GET`
- URL: `/api/foods/{id}`

### 4.3 Them mon moi (ADMIN)

- Method: `POST`
- URL: `/api/foods`
- Header bat buoc: `X-ROLE: ADMIN`
- Body:

```json
{
  "name": "Pho Bo",
  "price": 50000,
  "description": "Pho bo tai",
  "image": "url_anh_3"
}
```

### 4.4 Sua mon an (ADMIN)

- Method: `PUT`
- URL: `/api/foods/{id}`
- Header bat buoc: `X-ROLE: ADMIN`
- Body:

```json
{
  "name": "Bun Bo Dac Biet",
  "price": 55000,
  "description": "Cap nhat mon",
  "image": "url_moi"
}
```

### 4.5 Xoa mon an (ADMIN)

- Method: `DELETE`
- URL: `/api/foods/{id}`
- Header bat buoc: `X-ROLE: ADMIN`

## 5. Cac kich ban loi da xu ly

1. Validation Error
- HTTP 400
- `message`: `Validation Error`
- `errors`: danh sach field sai

2. Not Found
- HTTP 404
- Vi du: `Mon an co ID 999 khong ton tai`

3. Business Logic Error
- HTTP 422
- Vi du: trung ten mon an

4. Service Unavailable
- HTTP 503
- Vi du test nhanh: `GET /api/foods/101?simulateServiceDown=true`

5. Unauthorized
- HTTP 401
- Khi thao tac ADMIN nhung khong gui `X-ROLE: ADMIN`

## 6. Du lieu mau

Khi start app, he thong co san 2 mon:
- ID 101: Com Tam
- ID 102: Bun Bo

## 7. Postman test tu dong

File da tao san:
- `postman/Mini_Food_Service.postman_collection.json`
- `postman/Mini_Food_Service.local.postman_environment.json`

Cach chay:
1. Start service: `mvn spring-boot:run`
2. Import collection va environment vao Postman.
3. Chon environment `Mini Food Service Local`.
4. Chay Collection Runner theo thu tu request 01 -> 12.

Bo test bao gom ca luong thanh cong va cac loi:
- 200, 201
- 400 Validation
- 401 Unauthorized
- 404 Not Found
- 422 Business Logic
- 503 Service Unavailable
