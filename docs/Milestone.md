### 프로젝트 Milestone
```mermaid
gantt
    title E-commerce Project Milestone
    dateFormat  YYYY-MM-DD
    section 요구사항 분석 및 설계
    요구사항 분석 및 설계 자료, Mock API 작성           :a1, 2024-10-06, 4d
    section 잔액 충전/조회, 상품 조회, 주문/결제, 상품 통계 기능 구현
    요구사항 기능 단위테스트 작성           :b1, 2024-10-10, 2d
    요구사항 비즈니스 로직 구현             :b2, after b1, 8d
    구현 사항에 대한 통합테스트 작성         :b3, after b2, 2d
    section 장바구니 기능 구현
    요구사항 기능 단위테스트 작성           :c1, 2024-10-22, 2d
    요구사항 비즈니스 로직 구현             :c2, after c1, 8d
    구현 사항에 대한 통합테스트 작성         :c3, after c2, 2d
```
