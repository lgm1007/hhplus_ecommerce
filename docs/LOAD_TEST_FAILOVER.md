# 부하 테스트 및 가상 장애 대응 문서
## 부하 테스트
### 부하 테스트 대상 및 이유
주문 요청

**이유**: 주문 요청은 여러 사용자들이 접근할 수 있는 상품 재고라는 자원에 대한 요청이므로, 부하에 감당 가능해야 하는 기능이라고 판단했기 때문이다. 

### 부하 테스트 목적
주문 요청 API가 다양한 부하 조건에서 어떻게 동작하는지 평가하고, 시스템의 안정성, 응답 시간, 처리량 등을 확인한다. 이를 통해 시스템이 예상되는 트래픽을 처리할 수 있는지 확인하고, 장애가 발생하기 전에 병목 지점을 찾는 것이 목표이다.

#### 자세한 목표 설정

- **성능 한계 확인**: API가 처리할 수 있는 최대 요청 수 또는 동시 사용자 수를 파악
- **안정성 평가**: 서버가 일정 부하 이상에서 다운되지 않도록 하는 안정성 점검

### 부하 테스트 시나리오
부하 테스트 시나리오는 다양한 트래픽 패턴을 통해 API의 성능을 평가하는 것을 목표로 한다. 주문 요청 API는 실제 사용자 요청을 시뮬레이션하는 것이므로, 아래와 같은 시나리오를 고려할 수 있다.

- **정상 트래픽 시나리오**:
    - 하루 중 정상적인 시간대에 해당하는 트래픽 패턴을 시뮬레이션
    - 예시: 1초에 1건의 주문 요청
- **피크 트래픽 시나리오**:
    - 할인 행사나 특별 이벤트와 같은 특정 시점에 발생할 수 있는 높은 트래픽을 시뮬레이션
    - 예시: 1초에 100건 이상의 주문 요청이 몰리는 경우
- **부하 및 스트레스 테스트 시나리오**:
    - 시스템의 한계를 확인하기 위해 점진적으로 부하를 증가시켜 최대 트래픽을 시뮬레이션
    - 예시: 점차적으로 동시 사용자의 수를 증가시키고, 특정 임계값에서 장애가 발생하는지 확인

### 부하 테스트 스크립트
본 부하 테스트에서는 k6를 사용한다.

```javascript
import http from "k6/http";
import {check, sleep} from "k6";

export const options = {
    scenarios: {
        normal_traffic: {
            executor: 'constant-vus',
            vus: 10,  // 10명의 가상 사용자
            duration: "10s"  // 테스트 시간
        },
        peak_traffic: {
            executor: 'ramping-arrival-rate',  // 요청의 도달률 증가
            startRate: 50,  // timeUnit 당 50 번의 반복 속도로 테스트 진행
            timeUnit: '1s',
            preAllocatedVUs: 50,  // 테스트 진행 전에 50개의 가상 사용자 먼저 할당
            stages: [
                { duration: '10s', target: 500 },  // 10초 동안 timeUnit 당 500번의 반복
                { duration: '20s', target: 1000 },  // 20초 동안 timeUnit 당 1000번 반복 선형 증가
                { duration: '20s', target: 2000 },  // 40초 동안 timeUnit 당 2000번의 반복
                { duration: '10s', target: 0 },
            ],
        },
		stress_test: {
			executor: 'ramping-vus',  // 가상 사용자를 점진적으로 증가
			startVUs: 100,
			stages: [
				{ duration: '10s', target: 1000 },
				{ duration: '20s', target: 2000 },
				{ duration: '20s', target: 4000 },
				{ duration: '10s', target: 0 },
			],
		},
    },
};

export default function() {
    const url = 'http://localhost:8080/api/v1/orders'
    const payload = JSON.stringify({
        userId: 12345,
        orderItemInfos: [
            {
                productDetailId: 1,
                quantity: 1
            },
        ],
    });
    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };
    const res = http.post(url, payload, params);
    check(res, {
        'is status 200': (r) => r.status === 200,
    });
    sleep(1);
}
```

### 부하 테스트 시나리오 별 성능 지표
#### k6 부하테스트 지표
K6 테스트에서 발생하는 지표는 다음과 같은 의미이다.

- **data_received**:
  - 서버로부터 수신된 데이터의 총량
- **data_sent**:
  - 클라이언트에서 서버로 보낸 데이터의 총량
- **http_req_blocked**:
  - 요청이 네트워크에서 차단된 시간. 예를 들어, DNS 조회나 연결 대기와 같은 초기 네트워크 지연 시간 포함
- **http_req_connecting**:
  - 서버와 연결을 설정하는 데 걸린 시간. 네트워크 지연이나 DNS 문제 등으로 인해 병목이 발생할 수 있는 부분.
- **http_req_duration**:
  - 한 HTTP 요청의 전체 지속 시간. `(http_req_connecting + waiting + receiving)`
- **http_req_failed**:
  - 실패한 HTTP 요청의 비율.
- **http_req_receiving**:
  - 응답 데이터를 클라이언트가 수신하는 데 걸린 시간.
- **http_req_sending**:
  - 요청 본문을 서버에 전송하는 데 걸린 시간.
- **http_req_waiting**:
  - 서버로 요청을 보낸 후 응답을 기다리는 시간. 백엔드 처리 시간과 유사하며, API 성능의 핵심 지표
- **http_reqs**:
  - 요청 수를 측정. 부하 테스트를 통해 얼마나 많은 HTTP 요청이 발생했는지 확인할 수 있다.
- **iteration_duration**:
  - 각 반복(iteration)이 완료되는데 걸리는 시간.
- **vus**:
  - 실행 중인 가상 사용자의 수

**성능 지표로 병목 탐색하기:**

- **HTTP 요청 실패율 (http_req_failed)**
  - **병목 현상**: 만약 요청 실패율이 급격히 증가한다면, 서버가 요청을 제대로 처리하지 못하고 있다는 신호이다. 이는 서버의 **자원 부족**이나 **코드 오류**, **네트워크 문제** 등으로 인해 발생할 수 있다.
  - **탐색 방법**: 요청 실패율이 높다면, 서버 로그나 인프라 상태를 점검해보고, 리소스가 과부하 상태인지, 시스템 구성에 문제가 있는지를 확인해야 한다.
- **응답 시간 (http_req_duration)**
  - **병목 현상**: 응답 시간이 너무 길다면, 이는 **서버의 처리 능력** 또는 **데이터베이스 쿼리**, **네트워크 지연** 등의 병목을 나타낼 수 있다. 응답 시간이 너무 길면 사용자는 불만을 가질 수 있으며, 시스템의 확장성을 고려해야 한다.
  - **탐색 방법**: 평균 응답 시간, 95백분위수, 최대 응답 시간 등을 분석하여 성능 문제를 구체적으로 파악한다. 예를 들어, **95th percentile**(95백분위수)을 기준으로 응답 시간이 급격히 늘어난다면, 특정 요청이 시스템을 과부하시키는 경우일 수 있다.
- **네트워크 연결 시간 (http_req_connecting)**
  - **병목 현상**: `http_req_connecting` 시간이 길면 네트워크 연결에서 병목이 발생하고 있음을 나타낸다. 이는 DNS 해결 문제, 서버와의 연결 지연, 네트워크 구성 오류 등이 원인일 수 있다.
  - **탐색 방법**: 연결 시간 자체가 길다면 서버의 네트워크 환경을 점검하고, 클라이언트와 서버 간의 물리적 거리가 너무 멀어지지 않았는지, DNS 서비스가 적절히 작동하는지 등을 확인할 필요가 있다.
- **대기 시간 (http_req_waiting)**
  - **병목 현상**: 대기 시간이 길다면, 이는 서버가 요청을 처리하는 데 시간이 걸리고 있다는 신호이다. 이는 **서버의 CPU/메모리 자원 부족**, **DB 쿼리 성능 문제**, **서버의 병목 지점** 등에서 발생할 수 있다.
  - **탐색 방법**: 대기 시간이 길다면 서버의 리소스 사용률을 확인하고, DB 쿼리 성능을 튜닝하거나 서버의 성능을 확장할 필요가 있다. 예를 들어, 특정 API나 데이터베이스가 병목이 될 수 있다.
- **처리된 초당 요청 수 (throughput)**
  - **병목 현상**: 처리할 수 있는 초당 요청 수가 줄어들면 서버의 처리 성능이 한계에 도달한 상태일 수 있다. 이는 서버 자원이 부족하거나 시스템 아키텍처가 최적화되지 않았을 때 발생할 수 있다.
  - **탐색 방법**: `throughput`이 예상보다 적다면, 서버의 **CPU**, **메모리**, **네트워크 대역폭** 등을 점검하여 병목이 있는 지점을 파악한다. 특히 리소스 사용률을 모니터링하여 성능을 개선할 수 있다.
- **가상 사용자 수 (vus)**
  - **병목 현상**: 가상 사용자 수가 급격히 증가하면서 성능 저하가 발생한다면, 이는 **서버의 동시 처리 능력** 또는 **리소스 한계**를 나타낸다.
  - **탐색 방법**: 가상 사용자 수가 많을 때 시스템 성능이 저하된다면, 서버의 **동시성 처리 능력**이나 **애플리케이션 아키텍처**에 문제가 있을 수 있다. 예를 들어, 애플리케이션에서 병렬 처리나 캐싱 전략을 최적화할 필요가 있다.
- **에러율 (error_rate)**
  - **병목 현상**: 에러율이 높아진다면 시스템이 과부하를 일으키고 있거나 코드 수준에서 문제가 있을 수 있다. 이는 서버가 처리할 수 있는 요청 수를 초과하거나 잘못된 요청을 받았을 때 발생할 수 있다.
  - **탐색 방법**: 에러율이 높다면, 시스템의 로깅 기능을 활용하여 실패한 요청에 대한 원인 분석을 해야 한다. 예를 들어, 코드 오류, 잘못된 API 호출, 잘못된 파라미터 등이 원인일 수 있다.

##### 1. 정상 트래픽 시나리오

![normal_traffic_k6_log](https://github.com/user-attachments/assets/db9a3abb-8df9-490b-893e-bf82f661fa2e)
![normal_traffic_k6](https://github.com/user-attachments/assets/ab4251b7-cb02-4450-af65-245d080d662d)

- **data_received**: 55 KB
- **data_sent**: 22 KB
- **http_req_blocked**:
  - avg: 190.32 us
  - p(90): 554.13 us
  - p(95): 2 ms
  - max: 2 ms
- **http_req_connecting**: 
  - avg: 99.99 us
  - p(90): 99.99 us
  - p(95): 999.9 us
  - max: 999.9 us
- **http_req_duration**:
  - avg: 5.13 ms
  - p(90): 8.08 ms
  - p(95): 9.13 ms
  - max: 11.47 ms
- **http_req_failed**: 0.0 %
- **http_req_receiving**:
  - avg: 141.4 us
  - p(90): 513.62 us
  - p(95): 524.42 us
  - max: 1.02 ms
- **http_req_sending**:
  - avg: 2.81 us
  - p(90): 0 s
  - p(95): 0 s
  - max: 281.7 us
- **http_req_waiting**:
  - avg: 4.98 ms
  - p(90): 8.08 ms
  - p(95): 9.13 ms
  - max: 11.47 ms
- **http_reqs**: 100,  9.895/s
- **iteration_duration**:
  - avg: 1 s
  - p(90): 1.01 s
  - p(95): 1.01 s
  - max: 1.02 s
- **vus**: 10

##### 2. 피크 트래픽 시나리오

![peak_traffic_k6_log](https://github.com/user-attachments/assets/3d96d9b2-96ad-4361-bb82-0c00aadd780d)
![peak_traffic_k6](https://github.com/user-attachments/assets/81b92ea8-5bc8-4a49-9d0e-eeff1efa800b)

- **data_received**: 1.6 MB
- **data_sent**: 638 KB
- **http_req_blocked**:
  - avg: 15.23 us (정상 트래픽 대비 -175.09 us)
  - p(90): 0 s
  - p(95): 0 s
  - max: 2.83 ms (+0.83 ms)
- **http_req_connecting**:
  - avg: 10.95 us (-89.04 us)
  - p(90): 0 s (-99.99 us)
  - p(95): 0 s (-999.9 us)
  - max: 2.08 ms (+1.07 ms)
- **http_req_duration**:
  - avg: 4.59 ms (-0.54 ms)
  - p(90): 9.56 ms (+1.48 ms)
  - p(95): 13.21 ms (+4.08 ms)
  - max: 55.4 ms (+43.93 ms)
- **http_req_failed**: 0.0 %
- **http_req_receiving**:
  - avg: 181.51 us (+40.11 us)
  - p(90): 604.97 us (+91.35 us)
  - p(95): 965.79 us (+441.37 us)
  - max: 6.28 ms (+5.26 ms)
- **http_req_sending**:
  - avg: 16.06 us (+13.25 us)
  - p(90): 0 s
  - p(95): 0 s
  - max: 1.02 us (-280.68 us)
- **http_req_waiting**:
  - avg: 4.39 ms (-0.59 ms)
  - p(90): 9.08 ms (+1 ms)
  - p(95): 12.98 ms (+3.85 ms)
  - max: 54.81 ms (+43.34 ms)
- **http_reqs**: 2952,  48.386/s
- **iteration_duration**:
  - avg: 1 s
  - p(90): 1.01 s
  - p(95): 1.02 s (+0.01 s)
  - max: 1.06 s (+0.04 s)
- **vus**: 50

##### 3. 부하 및 스트레스 테스트 시나리오

![load_test_k6_log](https://github.com/user-attachments/assets/70820339-942f-431f-bc16-7c032159986a)
![load_test_k6](https://github.com/user-attachments/assets/383dc483-393a-4d1a-b605-e701982f00a6)

- **data_received**: 40 MB
- **data_sent**: 16 MB
- **http_req_blocked**:
  - avg: 60.59 us (정상 트래픽 대비 -129.73 us)
  - p(90): 0 s
  - p(95): 518.59 us (-1.48 ms)
  - max: 5.53 ms (+3.53 ms)
- **http_req_connecting**:
  - avg: 53.76 us (-46.23 us)
  - p(90): 0 s
  - p(95): 513.2 us (-486.7 us)
  - max: 5.53 ms (+4.53 ms)
- **http_req_duration**:
  - avg: 663.94 ms (+658.81 ms)
  - p(90): 1.65 s (+1.64 s)
  - p(95): 1.71 s (+1.7 s)
  - max: 3.08 s (+3.07 s)
- **http_req_failed**: 0.0 %
- **http_req_receiving**:
  - avg: 111.32 us (-30.08 us)
  - p(90): 517.1 us (+3.48 us)
  - p(95): 525.2 us (+0.78 us)
  - max: 7.33 ms (+6.31 ms)
- **http_req_sending**:
  - avg: 12.69 us (+9.88 us)
  - p(90): 0 s
  - p(95): 0 s
  - max: 41.01 ms (+40.7 ms)
- **http_req_waiting**:
  - avg: 663.81 ms (+658.83 ms)
  - p(90): 1.65 s (+1.64 s)
  - p(95): 1.71 s (+1.7 s)
  - max: 3.07 s (+3.06 s)
- **http_reqs**: 71553,  1177.624/s
- **iteration_duration**:
  - avg: 1.66 s (+0.66 s)
  - p(90): 2.65 s (+1.64 s)
  - p(95): 2.71 s (+1.7 s)
  - max: 4.08 s (+3.06 s)
- **vus**:
  - min: 189
  - max: 3999

#### JVM Monitor 지표
##### 1. 정상 트래픽 시나리오

![normal_traffic_grafana](https://github.com/user-attachments/assets/ed39ee5e-584f-4d08-8b8c-a90754b53910)


##### 2. 피크 트래픽 시나리오

![peak_traffic_grafana](https://github.com/user-attachments/assets/bb7ba1b5-3b55-49a4-8edb-9b5f780a4757)


##### 3. 부하 및 스트레스 테스트 시나리오

![stress_test_grafana](https://github.com/user-attachments/assets/0a7350cd-70c5-4310-9bdf-b5819f98cd01)

