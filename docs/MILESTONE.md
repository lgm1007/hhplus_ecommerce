### 프로젝트 Milestone
```mermaid
gantt
    title E-commerce Project Milestone
    dateFormat  YYYY-MM-DD
    section 요구사항 분석 및 설계
    요구사항 분석 및 설계 자료, Mock API 작성           :a1, 2024-10-06, 4d
    section 잔액 충전/조회, 상품 조회, 주문/결제, 상품 통계 기능 구현
    잔액 충전/조회 API 구현          :b1, 2024-10-10, 2d
    상품 조회 API 구현               :b2, after b1, 1d
    주문/결제 API 구현               :b4, after b3, 4d
    상품 통계 API 구현               :b5, after b4, 1d
    section 장바구니 기능 구현
    장바구니 추가 API 구현              :c1, 2024-10-18, 2d
    장바구니 삭제/조회 API 구현         :c2, after c1, 1d
    주문 로직 내 장바구니 정책 추가      :c3, after c2, 2d
```
