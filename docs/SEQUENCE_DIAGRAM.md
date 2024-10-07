### 시퀀스 다이어그램
#### 잔액 충전 / 조회
* **잔액 충전**
    1. 클라이언트의 충전 요청
    2. 사용자 정보 및 충전 금액 확인
    3. 충전 처리 후 성공 응답 반환

![](https://github.com/user-attachments/assets/47c24245-1875-4f26-a9c3-2d47bcc5e270)

* **잔액 조회**
    1. 클라이언트의 잔액 조회 요청
    2. 사용자 정보로 잔액 조회 후 반환

![](https://github.com/user-attachments/assets/666bc1a0-c60e-4a92-b3cf-180fa2ec195c)

#### 상품 조회
1. 클라이언트의 상품 조회 요청
2. 상품 목록 및 재고 반환

![](https://github.com/user-attachments/assets/007a9a37-0e07-49d5-ad3c-c9cc4f4cd510)

#### 주문 / 결제
1. 클라이언트가 상품을 주문하고 결제 요청 보냄
2. 사용자의 잔액 확인 후 상품의 재고 확인
3. 결제 성공 시 잔액 차감 및 데이터 플랫폼에 주문 정보 전송

![](https://github.com/user-attachments/assets/f6c22d54-733d-4c2f-9700-7be2d29321b8)

#### 상위 상품 조회
1. 클라이언트가 최근 3일간 가장 많이 팔린 상위 5개의 상품 정보를 요청
2. 통계 데이터에서 상위 5개 상품을 조회하여 반환

![](https://github.com/user-attachments/assets/7ad627f3-b95b-4b48-96a8-9dd08da90534)

#### 장바구니 기능
* **장바구니에 상품 추가/삭제**
    * 사용자가 장바구니에 상품을 추가하거나 삭제
* **장바구니 조회**
    * 사용자가 자신의 장바구니 목록을 조회

![](https://github.com/user-attachments/assets/a6bef6f0-7251-497e-9c53-07b8638e33a1)
