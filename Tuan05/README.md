# Docker Compose - Tuan 05

Da hoan thanh cac bai: 1, 2, 4, 5, 7, 8, 9, 11, 12, 13, 14, 15.
Khong lam theo yeu cau: 3, 6, 10.

## Cach chay tung bai

Di chuyen vao thu muc bai can chay, vi du:

```bash
cd Bai01
docker compose up -d
```

Dung bai do:

```bash
docker compose down
```

## Danh sach bai da tao

- Bai01: Nginx map cong 8080 -> 80
- Bai02: MySQL 8.0 voi user/password/mydb
- Bai04: Ung dung Node.js Express
- Bai05: Redis cong 6379
- Bai07: MongoDB + Mongo Express
- Bai08: Node.js ket noi MySQL
- Bai09: Flask don gian
- Bai11: PostgreSQL + Adminer (8083)
- Bai12: Prometheus + Grafana + Node Exporter
- Bai13: React build va phuc vu boi Nginx
- Bai14: 2 container giao tiep trong mang rieng
- Bai15: Redis gioi han CPU va RAM

## Cong truy cap nhanh

- Bai01 Nginx: http://localhost:8080
- Bai04 Node.js: http://localhost:3000
- Bai07 Mongo Express: http://localhost:8081
- Bai08 Node + MySQL: http://localhost:3008
- Bai09 Flask: http://localhost:5000
- Bai11 Adminer: http://localhost:8083
- Bai12 Prometheus: http://localhost:9090
- Bai12 Grafana: http://localhost:3001 (mac dinh admin/admin)
- Bai13 React + Nginx: http://localhost:8082
