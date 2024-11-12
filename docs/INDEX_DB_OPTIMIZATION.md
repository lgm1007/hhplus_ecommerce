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

![](https://github.com/user-attachments/assets/46e055c4-2daa-49fe-8276-27c9d37984cf)

테스트를 위해 사용자 ID가 1부터 30만까지의 데이터에서 사용자 ID가 298,765인 데이터에 해당하는 값을 조회하도록 함

조회 실행 시간: **229 ms**

##### 인덱스 적용 O

![](https://github.com/user-attachments/assets/1a811174-2054-43d1-bd02-fb943d768832)

EXPLAIN 실행 결과: 인덱스 적용된 것 확인

![](https://github.com/user-attachments/assets/9d7d51aa-fa9f-4e45-b4fb-e7d06e8112e0)

조회 실행 시간: **30 ms**

인덱스 적용 후 실행 시간 약 **87%** 개선

#### 상품 ID 목록으로 상품 세부 정보 목록 조회
##### 인덱스 적용 X

![](https://github.com/user-attachments/assets/6fdbd1ef-94b8-4e9c-ba55-fac5779d8ef0)

EXPLAIN 실행 결과: tableScan 으로 인덱스 적용되지 않은 것 확인

![](https://github.com/user-attachments/assets/de281a90-dd87-46f7-a05a-e9a328a390c0)

테스트를 위해 상품 ID가 1부터 30만까지의 데이터에서 상품 ID가 50000,100000,150000,200000,250000인 데이터에 해당하는 값을 조회하도록 함

조회 실행 시간: **220 ms**

##### 인덱스 적용 O

![](https://github.com/user-attachments/assets/da6e206d-fee2-41b3-be6e-d423750cb862)

EXPLAIN 실행 결과: 인덱스 적용된 것 확인

![](https://github.com/user-attachments/assets/03bb1590-3da8-42e5-81dc-ee28e8c015f8)

조회 실행 시간: **42 ms**

인덱스 적용 후 실행 시간 약 **81%** 개선

#### 사용자 ID로 장바구니 조회
##### 인덱스 적용 X

![](https://github.com/user-attachments/assets/3183f367-0d87-4c9c-9723-8af8b3732f00)

EXPLAIN 실행 결과: tableScan 으로 인덱스 적용되지 않은 것 확인

![](https://github.com/user-attachments/assets/624127d5-fbca-47fe-b783-5d126722a4c5)

테스트를 위해 사용자 ID가 1부터 30만까지의 데이터에서 사용자 ID가 298,765인 데이터에 해당하는 값을 조회하도록 함

조회 실행 시간: **213 ms**

##### 인덱스 적용 O

![](https://github.com/user-attachments/assets/49c12826-7038-40d2-b139-594662a02c55)

EXPLAIN 실행 결과: 인덱스 적용된 것 확인

![](https://github.com/user-attachments/assets/20b1bb15-de25-44f4-bc28-267f178f5311)

조회 실행 시간: **46 ms**

인덱스 적용 후 실행 시간 약 **78%** 개선
