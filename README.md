# e-커머스 서비스
### ⚓ 프로젝트 개요
* e-커머스 상품 주문 서비스를 구현한다.
* 상품 주문에 필요한 메뉴 정보들을 구성하고 조회가 가능하도록 구현한다.
* 사용자는 상품을 여러 개 선택해 주문할 수 있고, 미리 충전한 잔액을 이용한다.
* 상품 주문 내역을 통해 판매량이 가장 높은 상품을 추천한다.

### ⚙️ 프로젝트 구성 기술스택
* Kotlin: 1.9.25
* Spring Boot: 2.7.18
* JPA: 1.9.24
* DBMS: H2
* Testing: JUnit
* Caching: Redis
* Event Broker: Kafka

### 🗃️ 프로젝트 구조
```
hhplusecommerce
├─api
│  ├─balance
│  ├─cart
│  ├─order
│  ├─product
│  └─statistic
├─domain
│  ├─balance
│  ├─cart
│  ├─order
│  ├─product
│  └─statistic
├─infrastructure
│  ├─balance
│  ├─cart
│  ├─order
│  ├─product
│  └─statistic
└─usecase
    ├─balance
    ├─cart
    ├─order
    ├─product
    └─statistic
```

### 📜 API 스펙
1️⃣ **잔액 충전 / 조회 API**
* 결제에 사용될 금액을 충전하는 API 를 작성한다.
* 사용자 식별자 및 충전할 금액을 받아 잔액을 충전한다.
* 사용자 식별자를 통해 해당 사용자의 잔액을 조회한다.

2️⃣ **상품 조회 API**
* 상품 정보 ( ID, 이름, 가격, 잔여수량 ) 을 조회하는 API 를 작성한다.
* (조회시점의 상품별 잔여수량이 정확하면 좋다.)

3️⃣ **주문 / 결제 API**
* 사용자 식별자와 (상품 ID, 수량) 목록을 입력받아 주문하고 결제를 수행하는 API 를 작성한다.
* 결제는 기 충전된 잔액을 기반으로 수행하며 성공할 시 잔액을 차감해야 한다.
* 데이터 분석을 위해 결제 성공 시에 실시간으로 주문 정보를 데이터 플랫폼에 전송해야 한다.

4️⃣ **상위 상품 조회 API**
* 최근 3일간 가장 많이 팔린 상위 5개 상품 정보를 제공하는 API 를 작성한다.

5️⃣ **장바구니 기능**
* 사용자는 구매 이전에 관심 있는 상품들을 장바구니에 적재할 수 있다.
* 이 기능을 제공하기 위해 `장바구니에 상품 추가/삭제` API 와 `장바구니 조회` API 가 필요하다.
* 위 두 기능을 제공하기 위해 어떤 요구사항의 비즈니스 로직을 설계해야할 지 고민해본다.

### 🔎 API 명세
- [API 명세서](docs/API_SPEC.md)

### 💫 요구사항 별 시퀀스 다이어그램
- [시퀀스 다이어그램 문서](docs/SEQUENCE_DIAGRAM.md)

### 📊 요구사항 별 플로우 차트
- [플로우 차트 문서](docs/FLOW_CHART.md)

### 💽 ERD
- [ERD 문서](docs/ERD.md)

### 🔒 동시성 제어 시나리오 분석
- [동시성 제어 시나리오 분석 보고서](docs/CONCURRENCY_CONTROL.md)

### 💾 캐시
- [캐시 및 캐시 적용 분석 보고서](docs/CACHING.md)

### 🪄 DB 조회 성능 최적화
- [DB 조회 쿼리 최적화를 위한 쿼리 분석 및 인덱스 적용 보고서](docs/INDEX_DB_OPTIMIZATION.md)

### ⛓️ 트랜잭션 범위 및 서비스 확장 대응 보고서
- [시나리오 트랜잭션 범위 분석 및 서비스 확장 대비 보고서](docs/DISTRIBUTED_TRANSACTION.md)
