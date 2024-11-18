# DB 조회 쿼리 최적화를 위한 쿼리 분석 및 인덱스 적용 보고서
### 인덱스 톺아보기
#### 인덱스란?
인덱스란 테이블의 검색 속도를 향상시키기 위해 사용하는 자료구조로, 데이터와 데이터의 위치를 포함한 자료구조이다.

![](https://blog.kakaocdn.net/dn/Al7wD/btrE3Y39iW6/bA2KK5zqVlPSWflA37956K/img.png)

인덱스를 활용하면 데이터를 조회하는 SELECT 쿼리 동작 외에도 UPDATE나 DELETE 성능도 함께 향상된다. 데이터를 수정, 삭제하기 위해서는 해당 대상을 조회해야 하기 때문이다.

만약 인덱스를 사용하지 않은 컬럼을 조회한다고 하면 Full Scan을 수행하게 된다. Full Scan은 보통 테이블에 저장된 순서대로 전체를 비교하며 대상을 찾기 때문에 처리 속도가 떨어진다.

#### 인덱스 관리
DBMS는 인덱스를 항상 최신 정렬된 상태로 유지해야 빠르게 탐색할 수 있다. 따라서 인덱스가 적용된 컬럼에 INSERT, UPDATE, DELETE가 수행된다면 각각 다음의 작업을 해줘야 한다.

* **INSERT**: 새로운 데이터에 대한 인덱스 추가
* **DELETE**: 삭제하려는 데이터의 인덱스를 사용하지 않게 작업
* **UPDATE**: 기존의 인덱스를 사용하지 않고, 갱신된 데이터에 대한 인덱스 추가

#### 인덱스 장점과 단점
* **장점**
  * 테이블을 조회하는 속도와 그에 따른 성능을 향상시킬 수 있다.
  * 전반적인 시스템의 부하를 줄일 수 있다.
* **단점**
  * 인덱스를 관리하기 위해 DB의 약 10%에 해당하는 저장공간이 필요하다.
  * 인덱스를 관리하기 위해 추가 작업이 필요하다.
  * 인덱스를 잘못 사용할 경우 오히려 성능이 저하되는 역효과가 발생할 수 있다.


만약 INSERT, UPDATE, DELETE 가 빈번하게 발생하는 컬럼에 인덱스를 사용하게 되면 오히려 성능이 저하되는 역효과가 발생할 수 있다. UPDATE와 DELETE는 기존의 인덱스를 삭제하지 않고 `사용하지 않게` 처리하는 작업을 하기 때문에 인덱스가 점점 많아져 비대해지기 때문이다.

#### 인덱스를 사용하면 좋은 경우
* 규모가 작지 않은 테이블
* INSERT, UPDATE, DELETE 가 자주 발생하지 않는 컬럼
* JOIN 이나 WHERE, 또는 ORDER BY에 자주 사용되는 컬럼
* 데이터의 카디널리티가 높은 (중복도가 낮은) 컬럼

#### 인덱스의 구조
일반적으로 자주 사용하는 B-Tree 인덱스 구조에 대해 알아보자.

B-Tree 자료구조는 N개의 자식을 가지는 트리 구조이며, 좌우 자식 간의 균형이 맞지 않은 경우 매우 비효율적이라 항상 균형을 맞춘다는 의미에서 균형 트리 (Balanced Tree) 라고 불린다. B-Tree는 최상위에 단 하나의 노드만이 존재하는데, 이를 루트 노트 (Root Node) 라고 한다. 그리고 중간 노드를 브랜치 노드 (Branch Node), 최하위 노드를 리프 노드 (Leaf Node) 라고 한다.

![](https://hudi.blog/static/b-tree-search-008af18fe34f881eed12cc302d49daf2.gif)

#### 인덱스의 동작 원리
인덱스의 저장 방식을 이해하려면 페이지 또는 블럭이라고 하는 것에 대해 알아야 한다. 페이지란 디스크와 메모리에 데이터를 읽고 쓰는 최소 작업 단위이다. 일반적으로 인덱스를 포함하여 PK, 테이블 등은 모두 페이지 단위로 관리된다. 즉 1개의 레코드를 조회한다 해도 최소한 하나의 페이지를 읽어야 한다.

따라서 페이지에 저장되는 각 데이터의 크기를 최대한 작게 유지하여 1개의 페이지에 최대한 많은 데이터들을 저장할 수 있도록 하는 것이 중요하다. 만약 레코드를 찾는 데 1개의 페이지만으로 처리가 안된다면, 추가 페이지를 읽는 I/O로 인해 성능이 떨어지게 된다. 이는 메모리의 효율을 위해서라도 중요하다. 결국 DB 성능 개선과 쿼리 튜닝은 디스크 I/O 자체를 줄이는 것이 핵심인 경우가 많다.

인덱스는 페이지 단위로 저장이 되며, 인덱스 키를 바탕으로 **항상 정렬된 상태**를 유지하고 있다. 정렬된 인덱스 키를 따라서 리프 노드에 도달하면 **(인덱스 키, PK)** 쌍 형태로 저장되어 있다.

![](https://github.com/user-attachments/assets/46ec2ddc-67f5-473b-93fe-f4ce5a0f3535)

위 그림은 B-Tree 인덱스 영역 (왼쪽 녹색 영역) 과 Primary 키 인덱스 = 클러스터 인덱스 영역 (오른쪽 주황색 영역) 으로 나뉘어져 있다. User의 name 컬럼에 인덱스가 적용되어 있는 경우, B-Tree 인덱스에서 인덱스 키가 name 값을 기준으로 정렬되어 있고 데이터를 따라 리프 노드에 도달하면 인덱스 키에 해당하는 레코드의 PK 값이 저장되어 있다.

인덱스는 테이블과 독립적인 저장 공간이므로 인덱스를 통해 데이터를 조회하려면 PK를 찾아야 한다. B-Tree 인덱스 영역에서 클러스터 인덱스 영역으로 넘어가 PK로 레코드를 조회할 때는 해당하는 PK가 어느 페이지에 저장되어 있는지 알 수 없으므로 랜덤 I/O가 발생한다 (위 그림에서 루트 노드 부분). 이후 PK를 따라 리프 노드에서 실제 레코드를 읽어 온다.

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
   * 잔액 정보와 사용자 ID (`userId`) 는 1:1 관계로, `userId` 컬럼은 카디널리티가 높은 컬럼이다.
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
