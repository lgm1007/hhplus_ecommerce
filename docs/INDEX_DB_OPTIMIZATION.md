# DB 조회 쿼리 최적화를 위한 쿼리 분석 및 인덱스 적용 보고서
### 수행하는 조회 쿼리 수집
#### 주문
* 주문 ID로 주문 정보 조회하기
```sql
SELECT * FROM OrderTable WHERE id = :orderId
```

* 특정 날짜 간 특정 개수만큼 주문 상품 정보 조회하기
```sql
SELECT
    productDetailId,
    SUM(quantity)
FROM
    OrderItem
WHERE
    createdDate >= :startDate
GROUP BY
    productDetailId
ORDER BY
    SUM(quantity) DESC
LIMIT :limit
```

#### 잔액
* 사용자 ID로 잔액 정보 조회하기
```sql
SELECT * FROM Balance WHERE userId = :userId
```

#### 상품
* 상품 ID로 상품 정보 조회하기
```sql
SELECT * FROM Product WHERE id = :productId
```

* 상품 ID 목록으로 상품 정보 목록 조회하기
```sql
SELECT * FROM Product WHERE id in :productIds
```

* 상품 ID로 상품 세부 정보 조회하기
```sql
SELECT * FROM ProductDetail WHERE productId = :productId
```

* 상품 ID 목록으로 상품 세부 정보 목록 조회하기
```sql
SELECT * FROM ProductDetail WHERE productId in :productIds
```

#### 장바구니
* 사용자 ID로 모든 장바구니 상품 목록 조회하기
```sql
SELECT * FROM Cart WHERE userId = :userId
```

### 인덱스 대상 컬럼 선정하기
1. **잔액 (Balance) 테이블의 `userId` 컬럼**
   * 잔액 정보와 사용자 ID (`userId`) 는 1:1 관계로, `userId` 컬럼은 카디널리티가 높은 (데이터 중복이 적은) 컬럼이다.
   * 잔액 테이블은 사용자가 회원가입할 때만 삽입되는 테이블이기 때문에 삽입이 적다.
   * 사용자 ID (`userId`) 는 수정이 불가능한 값으로 수정이 일어나지 않는 컬럼이다.
2. **상품 세부 정보 (ProductDetail) 테이블의 `productId` 컬럼**
   * 상품 세부 정보와 상품 ID (`productId`) 는 1:1 관계로, `productId` 컬럼은 카디널리티가 높은 컬럼이다.
   * 상품 정보는 상품을 등록할 때만 삽입이 일어나므로 삽입이 적은 테이블이다.
   * 상품 ID (`productId`) 는 수정이 불가능한 값으로 수정이 일어나지 않는 컬럼이다.
3. **장바구니 (Cart) 테이블의 `userId` 컬럼**
   * 사용자 ID (`userId`) 는 수정이 불가능한 값으로 수정이 일어나지 않는 컬럼이다.

### 인덱스 적용 전/후 성능 비교하기
#### 사용자 ID로 잔액 조회
##### 인덱스 적용 X

![](https://github.com/user-attachments/assets/1310e820-b470-4783-8eeb-40485e45cfbf)

EXPLAIN 실행 결과: tableScan 으로 인덱스 적용되지 않은 것 확인

![](https://github.com/user-attachments/assets/6edf0289-ba61-46e3-9645-4cbd04229900)

조회 실행 시간: **89 ms**

##### 인덱스 적용 O

![](https://github.com/user-attachments/assets/1a811174-2054-43d1-bd02-fb943d768832)

EXPLAIN 실행 결과: 인덱스 적용된 것 확인

![](https://github.com/user-attachments/assets/e2a378a1-55af-495f-bf44-a260e58edd28)

조회 실행 시간: **71 ms**

인덱스 적용 후 실행 시간 약 **20%** 개선

#### 상품 ID 목록으로 상품 세부 정보 목록 조회
##### 인덱스 적용 X

![](https://github.com/user-attachments/assets/6fdbd1ef-94b8-4e9c-ba55-fac5779d8ef0)

EXPLAIN 실행 결과: tableScan 으로 인덱스 적용되지 않은 것 확인

![](https://github.com/user-attachments/assets/a54a27ba-84ef-447c-a74c-82d7d4d4ac67)

조회 실행 시간: **115 ms**

##### 인덱스 적용 O

![](https://github.com/user-attachments/assets/da6e206d-fee2-41b3-be6e-d423750cb862)

EXPLAIN 실행 결과: 인덱스 적용된 것 확인

![](https://github.com/user-attachments/assets/5a2df57e-6a86-4f31-906e-5ecb3acd7462)

조회 실행 시간: **77 ms**

인덱스 적용 후 실행 시간 약 **33%** 개선

#### 사용자 ID로 장바구니 조회
##### 인덱스 적용 X

![](https://github.com/user-attachments/assets/3183f367-0d87-4c9c-9723-8af8b3732f00)

EXPLAIN 실행 결과: tableScan 으로 인덱스 적용되지 않은 것 확인

![](https://github.com/user-attachments/assets/799dce91-7a51-453b-b373-ac08825e84e9)

조회 실행 시간: **90 ms**

##### 인덱스 적용 O

![](https://github.com/user-attachments/assets/49c12826-7038-40d2-b139-594662a02c55)

EXPLAIN 실행 결과: 인덱스 적용된 것 확인

![](https://github.com/user-attachments/assets/59f81d82-7280-4d8d-8858-86f1bf85cf05)

조회 실행 시간: **75 ms**

인덱스 적용 후 실행 시간 약 **16%** 개선
