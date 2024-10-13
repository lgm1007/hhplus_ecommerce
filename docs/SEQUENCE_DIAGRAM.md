### 시퀀스 다이어그램
#### 잔액 충전 / 조회
##### 잔액 충전
1. 클라이언트의 충전 요청
2. 사용자의 충전 금액 확인
3. 충전 처리 후 성공 응답 반환

```mermaid
sequenceDiagram
    actor Client
    participant BalanceFacade
    participant BalanceService
    participant BalanceRepository

    Client ->> BalanceFacade: 충전 요청 (사용자 ID, 충전 금액)
    BalanceFacade ->> BalanceService: 사용자 잔액 조회
    BalanceService ->> BalanceRepository: 사용자 ID 로 잔액 조회
    BalanceRepository -->> BalanceService: 사용자 ID 별 잔액 정보
    BalanceService -->> BalanceFacade: 사용자 잔액 정보
    BalanceFacade ->> BalanceService: 잔액 충전
    BalanceService ->> BalanceRepository: 사용자 ID 의 잔액 업데이트
    BalanceRepository -->> BalanceService: 사용자 ID 의 잔액 업데이트 성공
    BalanceService -->> BalanceFacade: 잔액 충전 성공
    BalanceFacade -->> Client: 충전 완료 응답
```

###### 잔액 조회
1. 클라이언트의 잔액 조회 요청
2. 사용자 정보로 잔액 조회 후 반환

```mermaid
sequenceDiagram
    actor Client
    participant BalanceFacade
    participant BalanceService
    participant BalanceRepository

    Client ->> BalanceFacade: 잔액 조회 요청 (사용자 ID)
    BalanceFacade ->> BalanceService: 사용자 잔액 조회
    BalanceService ->> BalanceRepository: 사용자 ID 로 잔액 조회
    BalanceRepository -->> BalanceService: 사용자 ID 별 잔액 정보
    BalanceService -->> BalanceFacade: 사용자 잔액 정보
    BalanceFacade -->> Client: 잔액 정보 응답
```

#### 상품 조회
1. 클라이언트의 상품 조회 요청
2. 상품 목록 및 재고 반환

```mermaid
sequenceDiagram
    actor Client
    participant ProductService
    participant ProductRepository
    participant ProductDetailRepository

    Client ->> ProductService: 상품 목록 조회 요청
    ProductService ->> ProductRepository: 상품 목록 조회
    ProductRepository -->> ProductService: 상품 목록 정보
    ProductService ->> ProductDetailRepository: 조회한 상품들의 세부 정보 조회
    ProductDetailRepository -->> ProductService: 조회한 상품들의 세부 정보
    ProductService ->> ProductService: 응답해 줄 상품 정보 가공
    ProductService -->> Client: 상품 정보 (id, 이름, 가격, 잔여수량) 응답
```

#### 주문 / 결제
##### 주문
1. 클라이언트가 상품을 주문하고 결제 요청 보냄
2. 상품의 재고 확인
3. 사용자의 잔액 조회
4. 주문 정보 저장
5. 재고 차감

```mermaid
sequenceDiagram
    actor Client
    participant OrderFacade
    participant ProductDetailService
    participant OrderService

    Client ->> OrderFacade: 주문 요청 (사용자 ID, 상품 ID, 수량)
    OrderFacade ->> ProductDetailService: 상품 재고 조회
    ProductDetailService -->> OrderFacade: 상품 재고 정보
    OrderFacade ->> OrderService: 주문 정보 저장 요청
    OrderService -->> OrderFacade: 주문 정보 저장 완료
    OrderFacade ->> ProductDetailService: 상품 재고 차감 요청
    ProductDetailService -->> OrderFacade: 상품 재고 차감 완료
    OrderFacade -->> Client: 주문 완료 응답
```

##### 결제
1. 주문 정보 조회
2. 사용자의 잔액 차감
3. 결제 정보 저장
4. 주문 정보에서 결제 완료로 상태 업데이트
5. 외부 데이터 플랫폼에 전송

```mermaid
sequenceDiagram
    actor Client
    participant PaymentFacade
    participant BalanceService
    participant PaymentService
    participant OrderService
    participant ExternalDataPlatform

    Client ->> PaymentFacade: 결제 요청 (사용자 ID, 주문 ID)
    PaymentFacade ->> BalanceService: 사용자 잔액 조회
    BalanceService -->> PaymentFacade: 사용자 잔액 정보
    PaymentFacade ->> BalanceService: 사용자 잔액 차감 요청
    BalanceService -->> PaymentFacade: 사용자 잔액 차감 완료
    PaymentFacade ->> PaymentService: 결제 정보 저장 요청
    PaymentService -->> PaymentFacade: 결제 정보 저장 완료
    PaymentFacade ->> OrderService: 주문 상태를 결제 완료로 업데이트 요청
    OrderService -->> PaymentFacade: 주문 상태 업데이트 완료
    PaymentFacade ->> ExternalDataPlatform: 외부 데이터 플랫폼 전송 이벤트 (AFTER_COMMIT Event)
    PaymentFacade -->> Client: 결제 완료 응답
```

#### 상위 상품 통계
1. 클라이언트가 최근 3일간 가장 많이 팔린 상위 5개의 상품 정보를 요청
2. OrderItem 테이블에서 최근 3일간 가장 많이 팔린 상품 디테일 목록을 상위 5개 조회
3. ProductDetail 테이블에서 상위 5개에 해당하는 상품 디테일 정보 조회
4. Product 테이블에서 상위 5개에 해당하는 상품 정보 조회
5. 상위 상품 목록 응답

```mermaid
sequenceDiagram
    actor Client
    participant StatisticService
    participant OrderItemRepository
    participant ProductDetailRepository
    participant ProductRepository

    Client ->> StatisticService: 최근 3일간 판매량 상위 5개 상품 조회 요청
    StatisticService ->> OrderItemRepository: 최근 3일간 판매량 상위 5개의 productDetailId 목록 조회
    OrderItemRepository -->> StatisticService: productDetailId 목록 정보
    StatisticService ->> ProductDetailRepository: productDetailId 목록으로 ProductDetail 목록 조회
    ProductDetailRepository -->> StatisticService: ProductDetail 목록 정보
    StatisticService ->> ProductRepository: ProductId 목록으로 Product 목록 조회
    ProductRepository -->> StatisticService: Product 목록 정보
    StatisticService -->> Client: 상위 5개 상품 정보 응답
```

#### 장바구니 기능
##### 장바구니에 상품 추가
1. 상품의 재고 확인
2. 재고 존재하면 상품 장바구니에 추가

```mermaid
sequenceDiagram
    actor Client
    participant CartFacade
    participant ProductDetailService
    participant CartService

    Client ->> CartFacade: 장바구니 상품 추가 요청 (사용자 ID, 상품 디테일 ID)
    CartFacade ->> ProductDetailService: 상품 재고 조회
    ProductDetailService -->> CartFacade: 상품 재고 정보
    CartFacade ->> CartService: 장바구니에 상품 저장 요청
    CartService -->> CartFacade: 장바구니에 상품 저장 완료
    CartFacade -->> Client: 장바구니 상품 추가 완료 응답
```

##### 장바구니 상품 삭제
1. 상품을 장바구니에서 삭제

```mermaid
sequenceDiagram
    actor Client
    participant CartService

    Client ->> CartService: 장바구니 상품 삭제 요청 (사용자 ID, 장바구니 ID)
    CartService -->> Client: 장바구니 상품 삭제 완료 응답
```

##### 장바구니 조회
1. 현재 장바구니에 담긴 상품 조회

```mermaid
sequenceDiagram
    actor Client
    participant CartService

    Client ->> CartService: 장바구니 목록 조회 요청 (사용자 ID)
    CartService -->> Client: 장바구니 목록 조회 응답
```
