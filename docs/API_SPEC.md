### API 명세
#### 1️⃣ 잔액 충전 / 조회 API
##### 잔액 충전
```
POST /api/v1/balances
```
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

* Request Header

```
{
  "Content-Type": "application/json"
}
```

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
##### 상품 목록 조회
```
GET /api/v1/products?page=1&size=10
```

* Request Header

```
{
  "Content-Type": "application/json"
}
```

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

##### 특정 상품 조회
```
GET /api/v1/products/{productId}
```

* Request Header

```
{
  "Content-Type": "application/json"
}
```

* Response Body

200 OK
```
{
  "id": 1,
  "name": "상품 A",
  "price": 5000,
  "stockQuantity": 10
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

* Response Body

200 OK
```
{
  "orderId": 98765,
  "userId": 12345,
  "orderDate": "2024-10-06T12:00:01",
  "totalPrice": 23000,
  "status": "ORDER_COMPLETE",
  "orderItems": [
    {
      "productDetailId": 1,
      "quantity": 2,
      "price": 10000
    },
    {
      "productDetailId": 2,
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

* Request Header

```
{
  "Content-Type": "application/json"
}
```

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

* Request Header

```
{
  "Content-Type": "application/json"
}
```

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

* Request Header

```
{
  "Content-Type": "application/json"
}
```

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
