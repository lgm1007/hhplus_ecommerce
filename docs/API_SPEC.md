### API 명세
#### 1️⃣ 잔액 충전 / 조회 API
##### 잔액 충전
```
POST /api/v1/balances
```

![Swagger-잔액충전-Request](https://github.com/user-attachments/assets/800df643-0858-4300-812c-0c4d26534de3)

* Request Header

```
{
  "Content-Type": "application/json"
}
```

* Request Body

```
{
  "userId": 12345,
  "amount": 10000
}
```

![Swagger-잔액충전-Response](https://github.com/user-attachments/assets/7ddbf785-3957-4463-a4e0-a292a7c3ff68)

* Response Body

200 OK
```
{
  "userId": 12345,
  "chargedAmount": 10000,
  "currentBalance": 30000
}
```

400 Bad Request (충전 실패)
```
{
  "error": "충전 금액 에러",
  "status": 400
}
```

##### 잔액 조회
```
GET /api/v1/balances/users/{userId}
```

![Swagger-잔액조회-Request](https://github.com/user-attachments/assets/443fbc9c-f5a6-49d5-ac8f-f5b013b1168b)

* Request Header

```
{
  "Content-Type": "application/json"
}
```

![Swagger-잔액조회-Response](https://github.com/user-attachments/assets/9caee68b-ef67-4f4d-bcaa-7db5613638f8)

* Response Body

200 OK
```
{
  "userId": 12345,
  "currentBalance": 30000
}
```

404 Not Found (사용자 없음)
```
{
  "error": "사용자 없음",
  "status": 404
}
```

#### 2️⃣ 상품 조회 API
```
GET /api/v1/products
```

![Swagger-상품조회-Request](https://github.com/user-attachments/assets/d5b622cc-577b-400a-b160-d7fa0f5773a7)

* Request Header

```
{
  "Content-Type": "application/json"
}
```

![Swagger-상품조회-Response](https://github.com/user-attachments/assets/175c3fd3-28f8-4b9e-8326-9930c977554b)

* Response Body

200 OK
```
{
  "items": [
    {
      "id": 1,
      "name": "상품 A",
      "price": 5000,
      "stockQuantity": 10
    },
    {
      "id": 2,
      "name": "상품 B",
      "price": 10000,
      "stockQuantity": 5
    },
  ]
}
```

404 Not Found (상품 없음)
```
{
  "error": "상품 없음",
  "status": 404
}
```

#### 3️⃣ 주문 / 결제 API
##### 주문
```
POST /api/v1/orders
```

![Swagger-주문-Request](https://github.com/user-attachments/assets/4bc0d16d-a5fd-4b5f-af28-f54befa01a06)

* Request Header

```
{
  "Content-Type": "application/json"
}
```

* Request Body

```
{
  "userId": 12345,
  "orderItems": [
    {
      "productDetailId": 1,
      "quantity": 2
    },
    {
      "productDetailId": 2,
      "quantity": 1
    }
  ]
}
```

![Swagger-주문-Response](https://github.com/user-attachments/assets/a3d54f0b-316a-4caa-965b-9090e5269eb3)

* Response Body

200 OK
```
{
  "orderId": 98765,
  "userId": 12345,
  "orderDate": "2024-10-06T12:00:01",
  "totalPrice": 13000,
  "status": "ORDER_COMPLETE",
  "orderItems": [
    {
      "productId": 1,
      "quantity": 2,
      "price": 10000
    },
    {
      "productId": 2,
      "quantity": 1,
      "price": 3000
    }
  ]
}
```

400 Bad Request (잔액 부족)
```
{
  "error": "잔액 부족",
  "status": 400
}
```

400 Bad Request (재고 부족)
```
{
  "error": "재고 부족",
  "status": 400
}
```

##### 결제
```
POST /api/v1/payments
```

![Swagger-결제-Request](https://github.com/user-attachments/assets/1fdec8a6-57f0-43a1-9e86-7b1cb96c5c56)

* Request Header

```
{
  "Content-Type": "application/json"
}
```

* Request Body

```
{
  "userId": 12345,
  "orderId": 98765
}
```

![Swagger-결제-Response](https://github.com/user-attachments/assets/1fe9864e-8b37-48c7-9c4a-9283f6155ac9)

* Response Body

200 OK
```
{
  "paymentId": 123,
  "orderId": 98765,
  "currentBalance": 17000,
  "paymentDate": "2024-10-06T12:01:10"
}
```

400 Bad Request (잔액 부족)
```
{
  "error": "잔액 부족",
  "status": 400
}
```

404 Not Found (주문 정보 없음)
```
{
  "error": "주문 정보 없음",
  "status": 404
}
```

#### 4️⃣ 상위 상품 조회 API
```
GET /api/v1/statistics/products/top
```

![Swagger-상위상품조회-Request](https://github.com/user-attachments/assets/781a75f0-2ef6-475a-b2b3-ca3488a078b3)

* Request Header

```
{
  "Content-Type": "application/json"
}
```

![Swagger-상위상품조회-Response](https://github.com/user-attachments/assets/5d9d653c-0d7d-4d18-8ca1-5adcbde132d2)

* Response Body

200 OK
```
{
  "items": [
    {
      "productId": 1,
      "name": "상품 A",
      "totalSold": 50
    }
    {
      "productId": 2,
      "name": "상품 B",
      "totalSold": 40
    }
  ]
}
```

404 Not Found (상품 없음)
```
{
  "error": "상품 없음",
  "status": 404
}
```

#### 5️⃣ 장바구니 기능
##### 장바구니 상품 추가
```
POST /api/v1/carts
```

![Swagger-장바구니추가-Request](https://github.com/user-attachments/assets/8095a508-b04a-4616-b02b-df6774be377d)

* Request Header

```
{
  "Content-Type": "application/json"
}
```

* Request Body

```
{
  "userId": 12345,
  "productId": 1,
  "quantity": 2
}
```

![Swagger-장바구니추가-Response](https://github.com/user-attachments/assets/8c4c2036-24d6-4b54-b1c7-b6cdfa5fb9e7)

* Response Body

200 OK
```
{
  "message": "상품을 장바구니에 추가했습니다.",
  "userId": 12345,
  "cart": {
    "productDetailId": 1,
    "quantity": 2
  }
}
```

400 Bad Request (재고가 없는 상품)
```
{
  "error": "재고 부족",
  "status": 400
}
```

##### 장바구니 상품 삭제
```
DELETE /api/v1/carts/users/{userId}/products/{productId}
```

![Swager-장바구니삭제-Request](https://github.com/user-attachments/assets/461838d7-bdf1-414a-b8f0-92d8c9881b36)

* Request Header

```
{
  "Content-Type": "application/json"
}
```

![Swagger-장바구니삭제-Response](https://github.com/user-attachments/assets/9e0aae16-5f18-43a3-9276-eb606d4e0289)

* Response Body

200 OK
```
{
  "message": "장바구니에서 상품을 삭제했습니다.",
  "userId": 12345,
  "cart": {
    "productDetailId": 2,
    "quantity": 1
  }
}
```

404 Not Found (사용자 정보 없음)
```
{
  "error": "사용자 없음",
  "status": 404
}
```

404 Not Found (상품 정보 없음)
```
{
  "error": "상품 없음",
  "status": 404
}
```

##### 장바구니 조회
```
GET /api/v1/carts/users/{userId}
```

![Swagger-장바구니조회-Request](https://github.com/user-attachments/assets/05274300-68e1-4ae3-b587-8d846c65939b)

* Request Header

```
{
  "Content-Type": "application/json"
}
```

![Swagger-장바구니조회-Response](https://github.com/user-attachments/assets/a95d8379-1bb6-4cba-b101-cd1404f54ea5)

* Response Body

200 OK
```
{
  "userId": 12345,
  "carts": [
    {
      "productDetailId": 1,
      "quantity": 2
    },
    {
      "productDetailId": 2,
      "quantity": 1
    }
  ],
}
```

404 Not Found (장바구니 정보 없음)
```
{
  "error": "장바구니 없음",
  "status": 404
}
```
