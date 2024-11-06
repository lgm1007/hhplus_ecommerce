## 캐시(Cache) 및 캐싱(Caching)이란?

- **캐시**: 사용자 입장에서 데이터를 더 빠르게 더 효율적으로 접근할 수 있는 임시 데이터 저장소이다. 보통 캐시는 데이터베이스에 접근하는 속도보다 빠른 저장소를 사용하는 것이 효과적이라, 보통 메모리를 사용하는 저장소를 사용한다.
- **캐싱**: 최근 웹 서비스 환경에서 시스템 성능 향상을 위한 기술로, 캐시는 메모리를 사용하기 때문에 디스크 기반의 데이터베이스보다 더 빠르게 데이터에 접근할 수 있어, 더 빠르게 서비스를 제공하게 해줄 수 있는 주요한 기술이다.
    - `application level` **메모리 캐시**
        - 애플리케이션의 메모리에 데이터를 저장해두고 요청에 대해 데이터를 빠르게 접근해 변환하여 성능 향상 효과를 얻는 방법
    - `external level` **별도의 캐시 서비스**
        - 별도의 캐시 저장소 또는 이를 담당하는 API 서버를 통해 캐시 환경을 제공하는 방법
          ex) Redis, Nginx 캐시, CDN

![](https://user-images.githubusercontent.com/63634505/131674277-f0f6036e-7115-423e-855f-96284e2abc25.png)

## 캐싱을 적용해야 하는 경우

- 동일한 데이터에 반복적으로 접근하는 상황이 많을 때 사용하는 것이 효과적이다. 즉 데이터 재사용 횟수가 한 번 이상이어야 캐싱의 의미가 있다.
- 잘 변하지 않는 데이터일수록 캐시를 사용하는 것이 효과적이다.
- 데이터에 접근 시 복잡한 로직이 필요한 경우 사용하면 효과적이다.

## 캐싱 적용 시 주의사항

- 캐시는 데이터 갱신 (Update) 은 자주 일어나지 않지만 참조 (READ) 는 자주 일어날 경우 고려해볼 만 하다.
- 캐시는 주로 데이터를 휘발성 메모리에 저장하기 때문에, 영구적으로 보관되어야 하는 중요한 데이터를 캐시에 두는 것은 바람직하지 않다.
- 적절한 만료 정책이 필요하다. 만료된 데이터는 캐시에서 삭제되어야 한다. 만료 정책이 없다면 데이터는 계속 캐시에 남아있어 메모리 용량이 부족해지는 상황이 올 수도 있다. 그렇다고 너무 짧은 만료 기한은 캐시에 데이터가 캐싱되지 않기 때문에 성능 향상을 위해 캐시를 사용하는 효과를 별로 얻지 못할 것이다.

## 시나리오에서 캐싱을 적용할 로직
### 상위 상품 통계 조회 데이터

- **로직 설명**
    - 가장 많은 주문을 한 상품 상위 5개의 통계 데이터를 조회합니다.

- **캐싱 적용 이유**
    - 해당 데이터를 참조하기 위해서는 **복잡한 로직**이 필요하다.
    - 해당 데이터는 모든 사용자에게 공통적으로 보여지는 데이터이다. 즉 다른 데이터보다 상대적으로 **조회가 잦다**고 판단했다.
    - 주문 상위 5개의 통계가 사용자에게 실시간으로 정확하게 보여야 하는 데이터는 아니라고 판단했다.
        - 캐시 만료 시간만큼 동일한 통계 데이터를 사용자에게 제공해도 무방하다는 것

- **데이터 조회 세부 로직**

    ```kotlin
    /*
     * 주문 상세 내용에서 상품 상세 ID 별 주문 수를 더한 데이터를 DTO로 그룹핑하여 조회한다.
     * 이 때 가장 많은 주문을 한 데이터 최대 5개까지 조회한다. (Pageable)
     */
    @Query("SELECT new com.example.hhplus_ecommerce.domain.order.dto.OrderQuantityStatisticsInfo(oi.productDetailId, SUM(oi.quantity)) " +
    	"FROM OrderItem oi " +
    	"WHERE oi.createdDate >= :standardDate " +
    	"GROUP BY oi.productDetailId " +
    	"ORDER BY SUM(oi.quantity) DESC")
    fun findTopQuantityByCreatedDateMoreThan(@Param("standardDate") standardDate: LocalDateTime, pageable: Pageable): List<OrderQuantityStatisticsInfo>
    
    /*
     * 위 조회된 주문 상세 데이터에서 상품 상세 ID 리스트를 가져와 해당 리스트로 상품 정보를 조회한다.
     */
    fun findAllByIdIn(ids: List<Long>): List<ProductDetail>
    ```

- **캐시 TTL (Time To Live) 설정**
  - 설정 시간: 5분
  - 이유: 실시간 인기 상품 성격으로, 나름 최신 상태로의 갱신이 자주 일어나야 하는 데이터라고 판단했다.

#### 캐싱 적용 테스트
##### 캐시 존재하지 않을 경우 조회 동작

![](https://github.com/user-attachments/assets/41ee4c02-2fba-4e81-9f2c-07ba94b62a68)

- Redis Monitor: 해당 키에 캐시 데이터 Setting

![](https://github.com/user-attachments/assets/78ce5aa1-8053-4829-afdf-1dab44d76d02)

- 메서드 실행 시간: **185 ms**

##### 캐시 존재하는 경우 조회 동작

![](https://github.com/user-attachments/assets/ed779b0e-332c-4c6f-89b8-9c03a8f993b7)

- Redis Monitor: 캐시의 키에서 데이터 가져옴

![](https://github.com/user-attachments/assets/a64e6a8e-c046-495f-b5d9-4c64c497a81d)

- 메서드 실행 시간: **71 ms**

- 무려 실행 시간이 **2배 이상 감소**된 점을 확인할 수 있었다.

### 상품 메인 정보 조회

- **로직 설명**
    - 상품 정보는 메인 정보와 세부 정보로 나뉘어진다.
    - 메인 정보: 상품 ID, 이름, 설명, 등록일자
    - 세부 정보: 가격, 재고량, 카테고리

- **캐싱 적용 이유**
    - 상품 메인 정보는 변경이 잘 일어나지 않는 데이터이다.
    - 상품 메인 정보는 여러 사용자들이 상품 정보를 조회해볼 수 있기 때문에 히트율이 많은 데이터라고 판단했다.

- **캐시 TTL (Time To Live) 설정**
    - 설정 시간: 1일
    - 이유: 변경이 일어나지 않는 데이터로 비교적 오랜 시간동안 캐시에 저장해도 무방할 것이라고 판단했다.

#### 캐싱 적용 테스트
##### 캐시 존재하지 않을 경우 조회 동작

![](https://github.com/user-attachments/assets/acb89b66-2e31-49d8-817b-e735bcd3715c)

- Redis Monitor: 해당 키에 캐시 데이터 Setting

![](https://github.com/user-attachments/assets/420be155-2adf-4344-9e79-47de48d4ad4e)

- 메서드 실행 시간: **190 ms**

##### 캐시 존재하는 경우 조회 동작

![](https://github.com/user-attachments/assets/b3562f0e-ffe6-4817-b931-2f192bbb996c)

- Redis Monitor: 캐시의 키에서 데이터 가져옴

![](https://github.com/user-attachments/assets/c5e63e41-1fbf-4d6f-931a-542e27c9b62c)

- 메서드 실행 시간: **97 ms**

- 해당 메서드도 실행 시간이 **2배 이상 감소**된 점을 확인할 수 있었다.
