### 시퀀스 다이어그램
#### 잔액 충전 / 조회
##### 잔액 충전
1. 클라이언트의 충전 요청
2. 사용자의 충전 금액 확인
3. 충전 처리 후 성공 응답 반환

```mermaid
sequenceDiagram
    actor Client
    participant BalanceService
    participant BalanceRepository

    Client ->> BalanceService: 충전 요청 (사용자 ID, 충전 금액)
    activate BalanceService
    BalanceService ->> BalanceRepository: 사용자 ID 로 잔액 조회
    activate BalanceRepository
    alt 잔액 정보를 찾지 못한 경우
        BalanceRepository -->> BalanceService: throw NotFoundException
    else 잔액 정상 조회
        BalanceRepository -->> BalanceService: 사용자 ID 별 잔액 정보
    end
    deactivate BalanceRepository
    BalanceService ->> BalanceRepository: 사용자 ID 의 잔액 업데이트
    activate BalanceRepository
    BalanceRepository -->> BalanceService: 사용자 ID 의 잔액 업데이트 성공
    deactivate BalanceRepository
    BalanceService -->> Client: 충전 완료 응답
    deactivate BalanceService
```

###### 잔액 조회
1. 클라이언트의 잔액 조회 요청
2. 사용자 정보로 잔액 조회 후 반환

```mermaid
sequenceDiagram
    actor Client
    participant BalanceService
    participant BalanceRepository

    Client ->> BalanceService: 잔액 조회 요청 (사용자 ID)
    activate BalanceService
    BalanceService ->> BalanceRepository: 사용자 ID 로 잔액 조회
    activate BalanceRepository
    alt 잔액 정보를 찾지 못한 경우
        BalanceRepository -->> BalanceService: throw NotFoundException
    else 잔액 정보 정상 조회
        BalanceRepository -->> BalanceService: 사용자 ID 별 잔액 정보
    end
    deactivate BalanceRepository
    BalanceService -->> Client: 잔액 정보 응답
    deactivate BalanceService
```

#### 상품 조회
##### 상품 목록 조회
1. 클라이언트의 상품 목록 조회 요청
2. 상품 목록 및 재고 반환

```mermaid
sequenceDiagram
    actor Client
    participant ProductService
    participant ProductRepository
    participant ProductDetailRepository

    Client ->> ProductService: 상품 목록 조회 요청
    activate ProductService
    ProductService ->> ProductRepository: 상품 목록 조회
    activate ProductRepository
    alt 상품 목록이 없는 경우
        ProductRepository -->> ProductService: throw NotFoundException
    else 상품 목록 정상 조회
        ProductRepository -->> ProductService: 상품 목록 정보
    end
    deactivate ProductRepository
    ProductService ->> ProductDetailRepository: 조회한 상품들의 세부 정보 조회
    activate ProductDetailRepository
    alt 상품 세부 정보를 찾지 못하는 경우
        ProductDetailRepository -->> ProductService: throw NotFoundException
    else 상품 세부 정보 정상 조회
        ProductDetailRepository -->> ProductService: 조회한 상품들의 세부 정보
    end
    deactivate ProductDetailRepository
    ProductService ->> ProductService: 응답해 줄 상품 목록 정보 가공
    ProductService -->> Client: 상품 목록 정보 (id, 이름, 가격, 잔여수량) 응답
    deactivate ProductService
```

##### 특정 상품 조회
1. 클라이언트의 특정 상품 조회 요청
2. 특정 상품 정보 및 재고 반환

```mermaid
sequenceDiagram
    actor Client
    participant ProductService
    participant ProductRepository
    participant ProductDetailRepository

    Client ->> ProductService: 특정 상품 조회 요청
    activate ProductService
    ProductService ->> ProductRepository: 특정 상품 조회
    activate ProductRepository
    alt 상품 정보가 존재하지 않을 경우
        ProductRepository -->> ProductService: throw NotFoundException
    else 상품 정보 정상 조회
        ProductRepository -->> ProductService: 특정 상품 정보
    end
    deactivate ProductRepository
    ProductService ->> ProductDetailRepository: 조회한 상품의 세부 정보 조회
    activate ProductDetailRepository
    alt 상품 세부 정보가 존재하지 않을 경우
        ProductDetailRepository -->> ProductService: throw NotFoundException
    else 상품 세부 정보 정상 조회
        ProductDetailRepository -->> ProductService: 조회한 상품의 세부 정보
    end
    deactivate ProductDetailRepository
    ProductService ->> ProductService: 응답해 줄 상품 정보 가공
    ProductService -->> Client: 상품 정보 (id, 이름, 가격, 잔여수량) 응답
    deactivate ProductService
```

#### 주문 / 결제
##### 주문
1. 클라이언트가 상품을 주문하고 결제 요청 보냄
2. 상품의 재고 확인
3. 주문 정보 저장
4. 장바구니 상품 삭제
5. 재고 차감 이벤트 발행

```mermaid
sequenceDiagram
    actor Client
    participant OrderFacade
    participant ProductDetailService
    participant OrderService
    participant CartService
    participant KafkaProducer

    Client ->> OrderFacade: 주문 요청 (사용자 ID, 상품 ID, 수량)
    activate OrderFacade
    OrderFacade ->> ProductDetailService: 상품 재고 조회
    activate ProductDetailService
    alt 상품 정보를 찾지 못하는 경우
        ProductDetailService -->> OrderFacade: throw NotFoundException
    else 재고가 부족할 경우
        ProductDetailService -->> OrderFacade: throw BadRequestException
    else 상품 정보 정상 조회
        ProductDetailService -->> OrderFacade: 상품 재고 정보
    end
    deactivate ProductDetailService
    OrderFacade ->> OrderService: 주문 정보 저장 요청
    activate OrderService
    OrderService -->> OrderFacade: 주문 정보 저장 완료
    deactivate OrderService
    OrderFacade ->> CartService: 장바구니 상품 삭제 요청
    activate CartService
    CartService -->> OrderFacade: 장바구니 상품 삭제 완료
    deactivate CartService
    OrderFacade ->> KafkaProducer: 상품 재고 차감 이벤트 발행
    OrderFacade -->> Client: 주문 완료 응답
    deactivate OrderFacade
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
    activate PaymentFacade
    PaymentFacade ->> BalanceService: 사용자 잔액 조회
    activate BalanceService
    alt 잔액 정보를 찾지 못하는 경우
        BalanceService -->> PaymentFacade: throw NotFoundException
    else 잔액이 부족한 경우
        BalanceService -->> PaymentFacade: throw BadRequestException
    else 잔액 정보 정상 조회
        BalanceService -->> PaymentFacade: 사용자 잔액 정보
    end
    deactivate BalanceService
    PaymentFacade ->> BalanceService: 사용자 잔액 차감 요청
    activate BalanceService
    BalanceService -->> PaymentFacade: 사용자 잔액 차감 완료
    deactivate BalanceService
    PaymentFacade ->> PaymentService: 결제 정보 저장 요청
    activate PaymentService
    PaymentService -->> PaymentFacade: 결제 정보 저장 완료
    deactivate PaymentService
    PaymentFacade ->> OrderService: 주문 상태를 결제 완료로 업데이트 요청
    activate OrderService
    OrderService -->> PaymentFacade: 주문 상태 업데이트 완료
    deactivate OrderService
    PaymentFacade ->> ExternalDataPlatform: 외부 데이터 플랫폼 전송 이벤트 (AFTER_COMMIT Event)
    PaymentFacade -->> Client: 결제 완료 응답
    deactivate PaymentFacade
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
    participant StatisticFacade
    participant OrderService
    participant ProductService

    Client ->> StatisticFacade: 최근 3일간 판매량 상위 5개 상품 조회 요청
    activate StatisticFacade
    StatisticFacade ->> OrderService: 최근 3일간 판매량 상위 5개의 productDetailId 목록 조회 (OrderItem 내 데이터)
    activate OrderService
    OrderService -->> StatisticFacade: productDetailId 목록 정보
    deactivate OrderService
    StatisticFacade ->> ProductService: productDetailId 목록으로 ProductDetail 목록 조회
    activate ProductService
    alt ProductDetail 목록이 없는 경우
        ProductService -->> StatisticFacade: throw NotFoundException
    else ProductDetail 목록 정상 조회
        ProductService -->> StatisticFacade: ProductDetail 목록 정보
    end
    deactivate ProductService
    StatisticFacade ->> ProductService: ProductId 목록으로 Product 목록 조회
    activate ProductService
    ProductService -->> StatisticFacade: Product 목록 정보
    deactivate ProductService
    StatisticFacade -->> Client: 상위 5개 상품 정보 응답
    deactivate StatisticFacade
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
    activate CartFacade
    CartFacade ->> ProductDetailService: 상품 재고 조회
    activate ProductDetailService
    alt 상품 정보가 존재하지 않을 경우
        ProductDetailService -->> CartFacade: throw NotFoundException
    else 상품 재고가 존재하지 않을 경우
        ProductDetailService -->> CartFacade: throw BadRequestException
    else 상품 정보 정상 조회
        ProductDetailService -->> CartFacade: 상품 재고 정보
    end
    deactivate ProductDetailService
    CartFacade ->> CartService: 장바구니에 상품 저장 요청
    activate CartService
    CartService -->> CartFacade: 장바구니에 상품 저장 완료
    deactivate CartService
    CartFacade -->> Client: 장바구니 상품 추가 완료 응답
    deactivate CartFacade
```

##### 장바구니 상품 삭제
1. 상품을 장바구니에서 삭제

```mermaid
sequenceDiagram
    actor Client
    participant CartService

    Client ->> CartService: 장바구니 상품 삭제 요청 (사용자 ID, 장바구니 ID)
    activate CartService
    alt 장바구니 정보가 존재하지 않을 경우
        CartService -->> Client: throw NotFoundException
    else
        CartService -->> Client: 장바구니 상품 삭제 완료 응답
    end
    deactivate CartService
```

##### 장바구니 조회
1. 현재 장바구니에 담긴 상품 조회

```mermaid
sequenceDiagram
    actor Client
    participant CartService

    Client ->> CartService: 장바구니 목록 조회 요청 (사용자 ID)
    activate CartService
    alt 장바구니 목록이 존재하지 않을 경우
        CartService -->> Client: throw NotFoundException
    else 장바구니 목록 정상 조회
        CartService -->> Client: 장바구니 목록 조회 응답
    end
    deactivate CartService
```
