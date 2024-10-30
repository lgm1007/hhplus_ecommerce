# 동시성 제어 시나리오 분석
해당 서비스의 시나리오에서 동시성 제어를 하기 위해 알아보고 적용해 본 방식들을 정리한 문서이다.

## 동시성 제어 방식 종류
### DB Lock
비관적 락 또는 낙관적 락을 사용하여 동시성 제어를 하는 방식으로 가장 기본적인 동시성 제어 방식이라고 볼 수 있다.

가장 간단하게 동시성 제어를 할 수 있는 방식이지만 DB Lock을 사용하는 경우에는 DB 서버에서 락을 잡아 커넥션이 대기하게 되므로, 락을 잡고 있는 트랜잭션 작업이 길어질수록 커넥션 수가 고갈되어 서비스가 지연될 수 있다는 문제가 있다.

### Redis 분산락
#### Lettuce
Lettuce는 Netty 기반의 Redis Client로, 요청을 논블로킹 방식으로 처리하기 때문에 높은 성능을 가진다. `SETNX`를 이용해 스핀락 (Spin Lock) 형태로 구현한다.

스핀란은 Lock 획득 실패하면 일정 시간/횟수 동안 Lock 획득을 재시도하는 방식이다. 주의할 점은 지속적인 재시도로 인해 네트워크 비용이 발생할 수 있다는 점과, 재시도에 지속적으로 실패할 경우 스레드 점유와 같은 문제가 발생할 수 있다는 점이 있다.

또한 Lettuce는 기본적으로 만료 시간을 제공하고 있지 않아 Lock을 점유한 서버에서 장애가 발생하면 무한 대기에 빠질 수 있다는 점도 주의해야 할 부분이다.

Lettuce는 `spring-data-redis`를 이용하면 기본적으로 제공되므로 구현이 간단하다는 장점이 있다.

#### Redisson
Redisson은 Redis의 Pub/Sub 기능을 제공해주어, 이를 사용하여 분산락을 구현할 수 있는 Redis Client이다.

Redis Pub/Sub 기능을 사용한 분산락 방식은 락 획득에 실패했을 때 `구독`하고 차례가 될 때까지 락 획득 이벤트를 기다리는 식으로 구현된다. `구독` 중인 구독자들 중 먼저 선점한 작업만 락 해제가 가능하므로 안정적으로 원자적 처리가 가능하다는 장점이 있다.

Redisson을 사용하기 위해서는 `redisson-spring-boot-starter` 의존성을 추가해줘야 한다.

### Kafka MQ
Kafka의 Message Queue 기능을 이용하여 순서 보장하게끔 구현하여 동시성 이슈를 해결할 수도 있다.

Kafka의 파티션 키를 활용하는 방식이다. 한 토픽의 파티션에서 동일한 key를 가진 메세지는 동일한 파티션에 발행되도록 보장한다. 때문에 컨슈머가 메시지를 순서대로 처리할 수 있다.

Kafka를 활용하면 트랜잭션 범위를 좁히고 순차 처리가 가능해지므로 성능적으로 우위를 가질 수 있다는 장점이 있다.

하지만 Kafka는 비동기로 동작하므로 비동기 처리를 위한 로직의 설계가 바탕이 되어야 한다. 또한 Kafka의 고가용성이나 컨슈머 Scale-out 등 고려해야 할 점이 많다는 점이 구현 시 단점으로 꼽힐 수 있다.

## 동시성 문제 발생 시나리오 및 해결 방안 고려
1. 주문 시 상품 재고 차감 시나리오

```
시나리오: 한 상품에 대해 여러 사용자가 동시에 주문 요청을 하게 될 경우

예상 동시성 이슈: 상품의 재고 차감 동작이 동시에 수행하게 되면 실제 재고량과 달라지는 동시성 이슈가 발생할 수 있다. 

발생 가능성: 높음

해결: Kafka

이유: 같은 상품에 대해 주문이 몰리는 시나리오는 유명한 제품일 경우 굉장히 많은 부하까지 발생할 수 있는 시나리오라고 판단했다.
대량의 트래픽이 발생하는 환경에서 DB Lock, 그 중 비관적 락을 사용하게 되면 락 획득할 때까지 대기하게 되면서 데드락이 발생할 수 있다.
그렇다고 낙관적 락은 트랜잭션 충돌이 많이 발생하는 해당 시나리오에서는 부적합하다고 판단했다.
분산락 방식 중 스핀락은 재시도로 인해 지속적인 재시도 실패 시 스레드 점유에 대한 문제가 있다.
Pub/Sub 방식으로 그나마 MQ 기능 비슷하게 활용 가능하지만, Kafka가 성능의 이점이 있기 때문에 Kafka를 선택했다.
```

2. 결제 시 사용자 잔액 차감 시나리오

```
시나리오: 같은 결제 요청이 동시에 중복 요청이 발생하는 경우

예상 동시성 이슈: 하나의 주문 건에 대해 중복 결제가 되어 잔액이 중복으로 차감되는 동시성 이슈가 발생할 수 있다.

발생 가능성: 낮음

해결: 낙관적 락

이유: 중복 요청을 방지해야 하는 시나리오이기 때문에 낙관적 락을 통해 한 번만 수행되도록 해야겠다고 판단했다.
```

3. 사용자 잔액 충전 시나리오

```
시나리오: 같은 잔액 충전 요청이 동시에 중복 요청이 발생하는 경우

예상 동시성 이슈: 같은 잔액 충전을 동시에 요청하여 원래 충전하려는 잔액보다 더 많은 금액이 충전되는 동시성 이슈가 발생할 수 있다.

발생 가능성: 낮음

해결: 낙관적 락

이유: 중복 요청을 방지해야 하는 시나리오이기 때문에 낙관적 락을 통해 한 번만 수행되도록 해야겠다고 판단했다.
```

## 각 제어 방식 비교해보기
비교에 앞서, 방식의 선택이 비교적 다양하게 선택할 수 있는 주문 요청 시나리오에 대하여 각 동시성 제어 방식으로 구현해보고 **성능, 구현의 복잡성, 효율성** 등을 비교해보겠다.

### DB Lock
본 시나리오에서 DB Lock 으로는 비관적 락의 베타락을 사용했다. 락을 거는 부분은 다음과 같이 구현했다.

```kotlin
@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("SELECT pd FROM ProductDetail pd WHERE pd.id = :id")
fun findByIdWithLock(@Param("id") id: Long): ProductDetail?
```

상품의 상세 정보 (ProductDetail) 테이블의 레코드에 대해 락을 걸었고, 재고를 차감하는 로직은 다음과 같다.

```kotlin
@Transactional
override fun updateProductQuantityDecreaseWithLock(id: Long, orderQuantity: Int): ProductDetail {
    val productDetail = (productDetailJpaRepository.findByIdWithLock(id)
        ?: throw NotFoundException(ErrorStatus.NOT_FOUND_PRODUCT))

    productDetail.decreaseQuantity(orderQuantity)

    return productDetailJpaRepository.save(productDetail)
}
```

DB Lock을 사용한 방식의 주문 요청 전체 로직은 다음과 같다.

```kotlin
@Transactional
fun productOrder(userId: Long, orderItemInfos: List<OrderItemInfo>): OrderInfo {
    val productDetailIds = orderItemInfos.map { orderItemInfo -> orderItemInfo.productDetailId }
    val productDetails = productService.getAllProductDetailsByDetailIdsInWithLock(productDetailIds)
    val orderItemDetailInfos = OrderItemDetailInfo.ofList(productDetails, orderItemInfos)
    // 주문 정보 등록
    val (savedOrder, savedOrderItems) = orderService.doOrder(userId, orderItemDetailInfos)

    // 재고 확인 및 재고 차감
    orderItemInfos.forEach { orderItemInfo ->
        productService.updateProductQuantityDecreaseWithLock(
            orderItemInfo.productDetailId, orderItemInfo.quantity
        )
    }

    // 장바구니 삭제
    cartService.deleteCartByUser(userId)

    return OrderInfo.of(savedOrder, savedOrderItems)
}
```

성능 비교를 위한 통합 테스트는 다음과 같이 작성하였다.

```kotlin
@Test
@DisplayName("주문 - 한 상품에 대한 동시 주문 요청 동시성 제어 테스트")
fun productOrderConcurrency() {
    // 재고 20개 존재하는 상품에 대해 30번 주문 요청
    // 예상 성공 카운트 20, 실패 카운트 10, 남은 재고량 0
    val productId = productRepository.save(Product("상품 A", "A 상품")).id
    val detailId = productDetailRepository.save(ProductDetail(productId, 1000, 20, ProductCategory.CLOTHES)).id

    val executor = Executors.newFixedThreadPool(30)
    val countDownLatch = CountDownLatch(30)
    val successCount = AtomicInteger(0) // 성공 카운트
    val failCount = AtomicInteger(0)    // 실패 카운트

    try {
        val startTime = System.currentTimeMillis()
        repeat(30) {
            executor.submit {
                try {
                    orderFacade.productOrder(1L, listOf(OrderItemInfo(detailId, 1)))
                    successCount.incrementAndGet()
                } catch (e: BadRequestException) {
                    failCount.incrementAndGet()
                } finally {
                    countDownLatch.countDown()
                }
            }
        }

        countDownLatch.await()

        val endTime = System.currentTimeMillis()
        logger.info("실행 시간: ${endTime - startTime} milliseconds")

        val actual = productService.getProductInfoById(productId)

        assertThat(actual.stockQuantity).isEqualTo(0)
        assertThat(successCount.get()).isEqualTo(20)
        assertThat(failCount.get()).isEqualTo(10)
    } finally {
        executor.shutdown()
    }
}
```

#### 성능
실행 시간을 측정한 결과는 다음과 같다.

![](https://github.com/user-attachments/assets/ba877364-c3a8-4653-8f0e-116a07f9bb28)

실행 시간은 **527 ms** 가 소요됐다.

#### 복잡성
JPA의 `@Lock` 어노테이션을 사용하여 간단하게 락을 걸 수 있었기에, 복잡성은 굉장히 낮다.

#### 효율성
DB 락 방식의 치명적인 단점으로는 앞서 설명한 것처럼 락 점유를 위해 대기할 때 DB 서버에서 락을 잡아 커넥션이 대기하게 되므로, 락을 잡고 있는 트랜잭션 작업이 길어질수록 커넥션 수가 고갈되어 서비스가 지연될 수 있다는 문제가 있다.

따라서 낮은 트래픽이 예상되는 서비스에서는 차용해도 괜찮겠지만, 그렇지 않고 병렬 수행이 많이 일어나는 서비스라면 성능 저하 위험이 있다.

### Redis 분산락 - 스핀락 (Lettuce)
Redis 분산락을 활용한 방식은 Redis 클러스터와 함께 사용하므로 다중 인스턴스 환경에서 유리하다는 장점이 있다.

Lettuce를 활용하여 락을 획득하는 부분은 다음과 같이 구현했다. 락 획득에 실패하여 재시도할 때 대기 시간을 10 ms로 설정하였다. 스레드 점유를 최소한으로 가져가고 싶어 대기 시간을 통합 테스트가 통과하는 최소한의 시간으로 설정했기 때문이다.

```kotlin
@Repository
class RedisLockRepository(private val redisTemplate: RedisTemplate<String, String>) : LockRepository {
	/**
	 * Lettuce 사용하여 락 습득, 해제 구현
	 */
	override fun lock(key: Any, timeout: Long): Boolean {
		// key: key.toString(), value: lock, ttl: 3000 millis
		return redisTemplate
			.opsForValue()
			.setIfAbsent(key.toString(), "lock", Duration.ofMillis(timeout))
			?: false
	}

	override fun unlock(key: Any): Boolean {
		return redisTemplate.delete(key.toString())
	}
}

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val productDetailRepository: ProductDetailRepository,
    private val lockRepository: LockRepository,
) {
    fun updateProductQuantityDecreaseWithLettuce(productDetailId: Long, orderQuantity: Int): ProductDetailDto {
        while (!lockRepository.lock(productDetailId)) {
            LockSupport.parkNanos(10_000_000)   // 10 ms, 10 * 1_000_000 ns
        }

        try {
            return ProductDetailDto.from(
                productDetailRepository.updateProductQuantityDecrease(
                    productDetailId, orderQuantity
                )
            )
        } finally {
            lockRepository.unlock(productDetailId)
        }
    }
}
```

Lettuce를 활용하여 락을 습득하는 방식의 주문 요청 전체 로직은 다음과 같다.

```kotlin
fun productOrderWithLettuce(userId: Long, orderItemInfos: List<OrderItemInfo>): OrderInfo {
    val productDetailIds = orderItemInfos.map { orderItemInfo -> orderItemInfo.productDetailId }
    val productDetails = productService.getAllProductDetailsByIdsIn(productDetailIds)
    val orderItemDetailInfos = OrderItemDetailInfo.ofList(productDetails, orderItemInfos)
    // 주문 정보 등록
    val (savedOrder, savedOrderItems) = orderService.doOrder(userId, orderItemDetailInfos)

    // 재고 확인 및 재고 차감
    for (orderItemInfo in orderItemInfos) {
        productService.updateProductQuantityDecreaseWithLettuce(
            orderItemInfo.productDetailId, orderItemInfo.quantity
        )
    }

    // 장바구니 삭제
    cartService.deleteCartByUser(userId)

    return OrderInfo.of(savedOrder, savedOrderItems)
}
```

성능 비교를 위한 통합 테스트는 다음과 같이 작성하였다.

```kotlin
@Test
@DisplayName("주문에 대한 동시 주문 요청 동시성 제어 테스트 Lettuce 사용하여 락 획득 기능 구현")
fun productOrderConcurrencyWithLettuce() {
    // 재고 20개 존재하는 상품에 대해 30번 주문 요청
    // 예상 성공 카운트 20, 실패 카운트 10, 남은 재고량 0
    val productId = productRepository.save(Product("상품 A", "A 상품")).id
    val detailId = productDetailRepository.save(ProductDetail(productId, 1000, 20, ProductCategory.CLOTHES)).id

    val executor = Executors.newFixedThreadPool(30)
    val countDownLatch = CountDownLatch(30)
    val successCount = AtomicInteger(0) // 성공 카운트
    val failCount = AtomicInteger(0)    // 실패 카운트

    try {
        val startTime = System.currentTimeMillis()
        repeat(30) {
            executor.submit {
                try {
                    orderFacade.productOrderWithLettuce(1L, listOf(OrderItemInfo(detailId, 1)))
                    successCount.incrementAndGet()
                } catch (e: BadRequestException) {
                    failCount.incrementAndGet()
                } finally {
                    countDownLatch.countDown()
                }
            }
        }

        countDownLatch.await()

        val endTime = System.currentTimeMillis()
        logger.info("실행 시간: ${endTime - startTime} milliseconds")

        val actual = productService.getProductInfoById(productId)

        assertThat(actual.stockQuantity).isEqualTo(0)
        assertThat(successCount.get()).isEqualTo(20)
        assertThat(failCount.get()).isEqualTo(10)
    } finally {
        executor.shutdown()
    }
}
```

#### 성능
실행 시간을 측정한 결과는 다음과 같다.

![](https://github.com/user-attachments/assets/c21a264b-90e0-4e08-a3dd-239d93f00daa)

실행 시간은 **1129 ms** 가 소요됐다.

#### 복잡성
Lettuce는 `spring-boot-starter-data-redis` 의존성만 추가하면 기본적으로 제공되므로 구현이 간단하다. 따라서 복잡성은 Redis 분산락 방식 중에서 상대적으로 낮은 느낌이다.

#### 효율성
Lettuce는 스핀락 방식으로 락이 해제되었는지 주기적으로 retry를 하는 방식이다. 따라서 이 retry를 하는 부분에서 지속적인 실패 시 스레드 점유 등 부하가 발생할 수 있다.

락을 획득하고 해제하는 과정이 짧다면 Lettuce를 활용하는 방식이 괜찮을 수 있지만, 그렇지 않은 경우에는 부하를 고려해야 한다.

### Redis 분산락 - Pub/Sub (Redisson)
Redisson 활용하여 락을 획득하는 부분은 다음과 같이 구현했다. 락을 획득하기 위해 구독하는 시간을 10초, 락 점유 시간을 최대 1초로 설정했다.

일반적으로 TTL은 작업이 수백 ms 정도 걸리는 경우 1초로 설정하므로 락 점유 최대 시간을 1초로 설정했고, 주문 요청 대기 시간을 최대 10초로 설정하기 위해 락 획득 구독 시간을 10초로 설정했다.

```kotlin
@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val productDetailRepository: ProductDetailRepository,
    private val redissonClient: RedissonClient
) {
    fun updateProductQuantityDecreaseWithRedisson(productDetailId: Long, orderQuantity: Int): ProductDetailDto {
        val rLock = redissonClient.getLock(productDetailId.toString())

        try {
            val acquireLock = rLock.tryLock(10, 1, TimeUnit.SECONDS)
            if (!acquireLock) {
                throw InterruptedException("Lock 획득 실패")
            }
            return ProductDetailDto.from(
                productDetailRepository.updateProductQuantityDecrease(productDetailId, orderQuantity)
            )
        } finally {
            rLock.unlock()
        }
    }
}
```

Redisson를 활용하여 락을 습득하는 방식의 주문 요청 전체 로직은 다음과 같다.

```kotlin
fun productOrderWithRedisson(userId: Long, orderItemInfos: List<OrderItemInfo>): OrderInfo {
    val productDetailIds = orderItemInfos.map { orderItemInfo -> orderItemInfo.productDetailId }
    val productDetails = productService.getAllProductDetailsByIdsIn(productDetailIds)
    val orderItemDetailInfos = OrderItemDetailInfo.ofList(productDetails, orderItemInfos)
    // 주문 정보 등록
    val (savedOrder, savedOrderItems) = orderService.doOrder(userId, orderItemDetailInfos)

    // 재고 확인 및 재고 차감
    for (orderItemInfo in orderItemInfos) {
        productService.updateProductQuantityDecreaseWithRedisson(
            orderItemInfo.productDetailId, orderItemInfo.quantity
        )
    }

    // 장바구니 삭제
    cartService.deleteCartByUser(userId)

    return OrderInfo.of(savedOrder, savedOrderItems)
}
```

성능 비교를 위한 통합 테스트는 다음과 같이 작성하였다.

```kotlin
@Test
@DisplayName("주문에 대한 동시 주문 요청 동시성 제어 테스트 Redisson 사용하여 락 획득 기능 구현")
fun productOrderConcurrencyWithRedisson() {
    // 재고 20개 존재하는 상품에 대해 30번 주문 요청
    // 예상 성공 카운트 20, 실패 카운트 10, 남은 재고량 0
    val productId = productRepository.save(Product("상품 A", "A 상품")).id
    val detailId = productDetailRepository.save(ProductDetail(productId, 1000, 20, ProductCategory.CLOTHES)).id

    val executor = Executors.newFixedThreadPool(30)
    val countDownLatch = CountDownLatch(30)
    val successCount = AtomicInteger(0) // 성공 카운트
    val failCount = AtomicInteger(0)    // 실패 카운트

    try {
        val startTime = System.currentTimeMillis()
        repeat(30) {
            executor.submit {
                try {
                    orderFacade.productOrderWithRedisson(1L, listOf(OrderItemInfo(detailId, 1)))
                    successCount.incrementAndGet()
                } catch (e: BadRequestException) {
                    failCount.incrementAndGet()
                } finally {
                    countDownLatch.countDown()
                }
            }
        }

        countDownLatch.await()

        val endTime = System.currentTimeMillis()
        logger.info("실행 시간: ${endTime - startTime} milliseconds")

        val actual = productService.getProductInfoById(productId)

        assertThat(actual.stockQuantity).isEqualTo(0)
        assertThat(successCount.get()).isEqualTo(20)
        assertThat(failCount.get()).isEqualTo(10)
    } finally {
        executor.shutdown()
    }
}
```

#### 성능
실행 시간을 측정한 결과는 다음과 같다.

![](https://github.com/user-attachments/assets/053ae925-265d-40a1-b258-cdf79fca9273)

실행 시간은 **584 ms** 가 소요됐다.

#### 복잡성
Redisson을 사용하기 위해서는 `redisson-spring-boot-starter` 의존성을 추가하고 `RedissonConfig` 설정도 추가해줘야 하기 때문에 Redis 분산락 방식 중에서는 상대적으로 복잡도가 조금 높다고 볼 수 있다.

#### 효율성
Redisson은 Pub/Sub 방식이기 때문에 락이 해제되면 락 해제를 기다리고 있던 스레드들에게 알려주는 구조로, 굉장히 효율적인 방식으로 락 관리가 가능하다.

락에 대해 구독하는 스레드들 중 먼저 점유한 작업만 락 해제가 가능하기 때문에 안정적으로 원자적 처리가 가능하다.

### Kafka MQ 기능 사용
주문 로직 중 상품 재고 차감 부분을 Kafka로 컨슈밍할 이벤트로 지정했다.

우선 토픽은 다음과 같이 만들어줬다.

![](https://github.com/user-attachments/assets/d1722084-695c-41db-a17b-dcf23a20ae04)

파티션은 3개로 생성해줬고, 이벤트를 발행할 때 같은 상품에 대한 재고 차감 이벤트를 순서 보장을 지키며 처리하기 위해 파티션 key로 상품 정보의 id 값을 넣어줬다.

```kotlin
fun sendProductOrderMessage(message: ProductMessage) {
    kafkaTemplate.send(PRODUCT_ORDER_TOPIC, message.productDetailId.toString(), message)
}
```

해당 토픽으로 발행한 이벤트를 컨슈밍하면 상품 재고 차감 로직을 수행한다.

```kotlin
@KafkaListener(groupId = "\${spring.kafka.consumer.group-id}", topics = [PRODUCT_ORDER_TOPIC])
fun listenProductOrderEvent(@Payload message: ProductMessage) {
    productService.updateProductQuantityDecrease(message.productDetailId, message.orderQuantity)
}
```

해당 Kafka 이벤트의 발행은 트랜잭션 이벤트로 주문 정보 저장 로직의 트랜잭션이 커밋된 이후 (AFTER_COMMIT) 발행해주도록 했다.

```kotlin
@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
fun productOrderEventListen(event: ProductOrderMessageEvent) {
    for (orderItemInfo in event.orderItemInfos) {
        messageProducer.sendProductOrderMessage(
            ProductMessage(
                orderItemInfo.productDetailId,
                orderItemInfo.quantity
            )
        )
    }
}
```

위 방식의 성능을 측정하려고 통합 테스트를 작성했다. Kafka 통합 테스트는 `spring-kafka-test` 의존성을 받아 테스트 환경에서 Kafka의 Producer와 Consumer가 정상적으로 동작하도록 구성했다.

```kotlin
@Test
@DisplayName("주문에 대한 동시 주문 요청 Kafka 이벤트 발행 기능 테스트")
fun productOrderConcurrencyWithKafka() {
    val productId = productRepository.save(Product("상품 A", "A 상품")).id
    val detailId = productDetailRepository.save(ProductDetail(productId, 1000, 20, ProductCategory.CLOTHES)).id

    val executor = Executors.newFixedThreadPool(30)
    val countDownLatch = CountDownLatch(30)

    try {
        val startTime = System.currentTimeMillis()
        repeat(30) {
            executor.submit {
                try {
                    orderFacade.productOrderWithKafka(1L, listOf(OrderItemInfo(detailId, 1)))
                } catch (e: Exception) {
                    logger.info("예외 발생!")
                } finally {
                    countDownLatch.countDown()
                }
            }
        }

        countDownLatch.await()

        val endTime = System.currentTimeMillis()
        logger.info("실행 시간: ${endTime - startTime} milliseconds")

        Thread.sleep(2000)

        val actual = productService.getProductInfoById(productId)

        assertThat(actual.stockQuantity).isEqualTo(0)
    } finally {
        executor.shutdown()
    }
}
```

#### 성능
상품 주문 기능을 수행하고 재고 차감 이벤트 발행하는 부분까지 실행 시간을 측정해본 결과는 다음과 같다.

![](https://github.com/user-attachments/assets/97785588-4b50-4455-a738-012abf144423)

실행 시간은 **377 ms** 가 소요됐다.

#### 복잡성
이번에 구현해 본 방법들 중 가장 복잡했다.

특히 Producer와 Consumer에 대해 설정해줘야 하는 사항들이 굉장히 많고 복잡해서 처음 환경을 구축할 때 꽤 많은 리소스가 들 것으로 예상된다.

또한 Kafka가 워낙 범용성이 뛰어나다보니 적절하게 사용하는 전략을 잘 세워야 하는 것도 중요하다. 이번 실습에서는 단순히 한 동작에 대한 이벤트만 발행해봤는데, 어떻게 이벤트의 신뢰성을 보장할 수 있을지 등 잘 사용하기 위한 방법들을 고려해봐야 한다.

#### 효율성
한 번에 많은 트래픽이 몰리는 케이스에서는 우수한 효율성을 보여준다.

환경 구축이 완료되고 사용 전략만 잘 구상된다면 다양한 시나리오에서 응용할 수 있는 방식이라고 생각한다.
