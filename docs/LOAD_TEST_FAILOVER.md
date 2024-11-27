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

- **http_reqs (HTTP Requests)**:
    - 요청 수를 측정합니다. 부하 테스트를 통해 얼마나 많은 HTTP 요청이 발생했는지 확인할 수 있습니다.
- **http_req_duration (HTTP Request Duration)**:
    - 요청이 발생하고 응답을 받기까지 걸린 시간의 분포입니다. 주로 **평균 응답 시간**, **최고/최저 응답 시간**, **백분위수**를 확인할 수 있습니다.
- **http_req_failed (HTTP Request Failed)**:
    - 실패한 요청의 수입니다. 실패한 요청이 많을수록 시스템이 트래픽을 제대로 처리하지 못하고 있다는 신호일 수 있습니다.
- **http_req_waiting (Waiting Time)**:
    - 요청을 보낸 후 서버가 응답을 준비하는 데 걸린 시간입니다. 서버의 처리 지연을 확인할 수 있습니다.
- **http_req_connecting (Connecting Time)**:
    - 서버와 연결을 설정하는 데 걸린 시간입니다. 네트워크 지연이나 DNS 문제 등으로 인해 병목이 발생할 수 있는 부분입니다.
- **vus (Virtual Users)**:
    - 테스트에서 사용된 가상 사용자의 수입니다. 이 수치를 통해 얼마나 많은 사용자가 동시에 시스템을 테스트했는지 알 수 있습니다.
- **duration (Test Duration)**:
    - 테스트가 지속된 시간입니다. 테스트가 얼마나 길게 진행되었는지 확인할 수 있습니다.
- **throughput (Throughput)**:
    - 초당 처리된 요청 수를 나타냅니다. 시스템이 얼마나 많은 요청을 처리했는지 평가하는 데 유용합니다.
- **error_rate (Error Rate)**:
    - 테스트 중 발생한 오류 비율입니다. 오류가 많이 발생하면 시스템이 트래픽을 처리하는 데 문제가 있다는 신호입니다.
- **response_time (Response Time)**:
    - 요청과 응답 사이의 시간이 평균, 95백분위수 등으로 계산됩니다. 시스템의 **응답 속도**를 확인하는 데 중요합니다.

##### 1. 정상 트래픽 시나리오

![normal_traffic_k6](https://github.com/user-attachments/assets/8a62fceb-c9e6-408a-852b-11bed75f307c)


##### 2. 피크 트래픽 시나리오

![peak_traffic_k6](https://github.com/user-attachments/assets/770b3282-8deb-44ed-862f-e9d8e0405528)


##### 3. 부하 및 스트레스 테스트 시나리오

![stress_test_k6](https://github.com/user-attachments/assets/9fcbf1e7-148e-460a-842d-55a606c6d474)


#### JVM Monitor 지표
##### 1. 정상 트래픽 시나리오

![normal_traffic_grafana](https://github.com/user-attachments/assets/ed39ee5e-584f-4d08-8b8c-a90754b53910)


##### 2. 피크 트래픽 시나리오

![peak_traffic_grafana](https://github.com/user-attachments/assets/bb7ba1b5-3b55-49a4-8edb-9b5f780a4757)


##### 3. 부하 및 스트레스 테스트 시나리오

![stress_test_grafana](https://github.com/user-attachments/assets/0a7350cd-70c5-4310-9bdf-b5819f98cd01)

