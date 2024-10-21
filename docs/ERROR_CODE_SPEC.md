## Error Code 정의서
해당 서비스에서 관리하는 에러 코드에 대해 정리한 문서이다.

### 400 Error
#### 설명
* 비즈니스에 대해 올바른 요청이 아닐 경우 발생하는 에러

#### 발생 가능한 케이스
* 잔액 API
  * 충전 금액 에러: 요청으로 들어온 충전 금액 값이 음수일 경우
```
HTTP/1.1 400
Content-Type: application/json

{
  "error": "충전 금액 에러",
  "status": 400
}
```

* 주문 API
  * 재고 부족: 재고 차감 시 차감하려는 수보다 재고량이 부족할 경우
```
HTTP/1.1 400
Content-Type: application/json

{
  "error": "재고 부족",
  "status": 400
}
```


* 결제 API
  * 잔액 부족: 잔액 사용 시 사용하려는 금액보다 잔액이 부족할 경우
```
HTTP/1.1 400
Content-Type: application/json

{
  "error": "잔액 부족",
  "status": 400
}
```

### 404 Error
#### 설명
* DB에서 요청한 자원을 찾지 못하는 경우 발생하는 에러

#### 발생 가능한 케이스
* 잔액 API
  * 사용자에 대한 잔액 정보를 찾지 못하는 경우
```
HTTP/1.1 404
Content-Type: application/json

{
  "error": "사용자에 대한 비용 정보 없음",
  "status": 404
}
```

* 상품 API
  * 상품 정보를 찾지 못하는 경우
```
HTTP/1.1 404
Content-Type: application/json

{
  "error": "상품 없음",
  "status": 404
}
```

* 주문 API
  * 상품 정보를 찾지 못하는 경우
* 결제 API
  * 사용자에 대한 잔액 정보를 찾지 못하는 경우
  * 주문 정보를 찾지 못하는 경우
```
HTTP/1.1 404
Content-Type: application/json

{
  "error": "주문 정보 없음",
  "status": 404
}
```

* 장바구니 API
  * 장바구니 정보를 찾지 못하는 경우
```
HTTP/1.1 404
Content-Type: application/json

{
  "error": "장바구니 없음",
  "status": 404
}
```

* 통계 API
  * 상품 정보를 찾지 못하는 경우
