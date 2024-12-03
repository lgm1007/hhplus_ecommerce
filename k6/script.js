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