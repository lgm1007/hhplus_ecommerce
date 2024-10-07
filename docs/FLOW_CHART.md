### 플로우 차트
#### 잔액 충전 / 조회
* **잔액 충전**
    1. 클라이언트의 충전 요청
    2. 사용자 정보 및 충전 금액 확인
    3. 충전 처리 후 성공 응답 반환

![](https://github.com/user-attachments/assets/645585af-c3a8-4ef9-acd8-f8c92c53f2c4)

* **잔액 조회**
    1. 클라이언트의 잔액 조회 요청
    2. 사용자 정보로 잔액 조회 후 반환

![](https://github.com/user-attachments/assets/e7984ace-3b30-4835-bfb1-8c0ae156c784)

#### 상품 조회
1. 클라이언트의 상품 조회 요청
2. 상품 목록 및 재고 반환

![](https://github.com/user-attachments/assets/11880263-77e2-48d7-9d5d-8cd20a110e5e)

#### 주문 / 결제
1. 클라이언트가 상품을 주문하고 결제 요청 보냄
2. 사용자의 잔액 확인 후 상품의 재고 확인
3. 결제 성공 시 잔액 차감 및 데이터 플랫폼에 주문 정보 전송

![](https://github.com/user-attachments/assets/167f0232-ae8e-4764-95af-b17685f870b1)

#### 상위 상품 조회
1. 클라이언트가 최근 3일간 가장 많이 팔린 상위 5개의 상품 정보를 요청
2. 통계 데이터에서 상위 5개 상품을 조회하여 반환

![](https://github.com/user-attachments/assets/aa3da949-c4d2-4612-861f-fa19fbb80d9a)

#### 장바구니 기능
* **장바구니에 상품 추가/삭제**
    * 사용자가 장바구니에 상품을 추가하거나 삭제
* **장바구니 조회**
    * 사용자가 자신의 장바구니 목록을 조회

![](https://github.com/user-attachments/assets/8b44f122-db92-4e58-a9d9-1ccf80453935)
