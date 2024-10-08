### API 명세
#### 1️⃣ 잔액 충전 / 조회 API
##### 잔액 충전
```
POST /api/v1/balance/charge
```
* Request Header

```
{
  "Authorization": "Bearer {token}",
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
  "message": "금액이 정상적으로 충전되지 않았습니다.",
  "status": 400
}
```

401 Unauthorized (인증 실패)
```
{
  "error": "인증 실패",
  "message": "유효하지 않거나 만료된 토큰입니다.",
  "status": 401
}
```

##### 잔액 조회
```
GET /api/v1/balance/user/{userId}
```

* Request Header

```
{
  "Authorization": "Bearer {token}"
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

401 Unauthorized (인증 실패)
```
{
  "error": "인증 실패",
  "message": "유효하지 않거나 만료된 토큰입니다.",
  "status": 401
}
```

404 Not Found (사용자 없음)
```
{
  "error": "사용자 없음",
  "message": "해당 사용자를 찾을 수 없습니다.",
  "status": 404
}
```

#### 2️⃣ 상품 조회 API
```
GET /api/v1/products
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
[
  {
    "id": 1,
    "name": "상품 A",
    "price": 5000,
    "stockQuantity": 10
  },
  {
    "id": 2,
    "name": "상품 B",
    "price": 3000,
    "stockQuantity": 5
  }
]
```

404 Not Found (상품 없음)
```
{
  "error": "상품 없음",
  "message": "조회할 수 있는 상품이 없습니다.",
  "status": 404
}
```

#### 3️⃣ 주문 / 결제 API
```
POST /api/v1/orders
```

* Request Header

```
{
  "Authorization": "Bearer {token}",
  "Content-Type": "application/json"
}
```

* Request Body

```
{
  "userId": 12345,
  "orderItems": [
    {
      "productId": 1,
      "quantity": 2
    },
    {
      "productId": 2,
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
  "orderDate": "2024-10-06",
  "totalPrice": 13000,
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
  "message": "사용자의 잔액이 부족합니다.",
  "status": 400
}
```

401 Unauthorized (인증 실패)
```
{
  "error": "인증 실패",
  "message": "유효하지 않거나 만료된 토큰입니다.",
  "status": 401
}
```

#### 4️⃣ 상위 상품 조회 API
```
GET /api/v1/products/top
```

* Response Body

200 OK
```
[
  {
    "productId": 1,
    "name": "상품 A",
    "totalSold": 50
  },
  {
    "productId": 2,
    "name": "상품 B",
    "totalSold": 40
  }
]
```

404 Not Found (상품 없음)
```
{
  "error": "상품 없음",
  "message": "조회할 수 있는 상품이 없습니다.",
  "status": 404
}
```

#### 5️⃣ 장바구니 기능
##### 장바구니 상품 추가
```
POST /api/v1/cart
```

* Request Header

```
{
  "Authorization": "Bearer {token}",
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
  "cart": {
    "userId": 12345,
    "items": [
      {
        "productId": 1,
        "quantity": 2
      }
    ]
  }
}
```

400 Bad Request (재고가 없는 상품)
```
{
  "error": "재고가 없는 상품",
  "message": "해당 상품의 재고가 없습니다.",
  "status": 400
}
```

401 Unauthorized (인증 실패)
```
{
  "error": "인증 실패",
  "message": "유효하지 않거나 만료된 토큰입니다.",
  "status": 401
}
```

##### 장바구니 상품 삭제
```
DELETE /api/v1/cart/user/{userId}/product/{productId}
```

* Request Header

```
{
  "Authorization": "Bearer {token}",
  "Content-Type": "application/json"
}
```

* Response Body

200 OK
```
{
  "message": "장바구니에서 상품을 삭제했습니다.",
  "cart": {
    "userId": 12345,
    "items": [
      {
        "productId": 2,
        "quantity": 1
      }
    ]
  }
}
```

401 Unauthorized (인증 실패)
```
{
  "error": "인증 실패",
  "message": "유효하지 않거나 만료된 토큰입니다.",
  "status": 401
}
```

404 Not Found (상품 정보 없음)
```
{
  "error": "상품 없음",
  "message": "장바구니에 담긴 상품의 정보가 존재하지 않습니다.",
  "status": 404
}
```
