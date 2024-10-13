### ERD
```mermaid
erDiagram
    User {
        Long id PK
        String name
        String email
        Date createdDate
    }

    Balance {
        Long id PK
        Long userId
        Integer amount
        Date createdDate
        Date lastModifiedDate
    }
    
    BalanceHistory {
        Long id PK
        Long balanceId
        Long userId
        Integer updateAmount
        Date createdDate
    }

    Product {
        Long id PK
        String name
        Integer price
        String description
        Date createdDate
    }

    ProductDetail {
        Long id PK
        Long productId
        Integer stockQuantity
        String productCategory
        Date createdDate
        Date lastModifiedDate
    }

    Order {
        Long id PK
        Long userId
        Date orderDate
        Integer totalPrice
        String orderStatus
        Date createdDate
        Date lastModifiedDate
    }

    OrderItem {
        Long id PK
        Long orderId
        Long productDetailId
        Integer quantity
        Integer price
        Date createdDate
    }

    Payment {
        Long id PK
        Long userId
        Long orderId
        Integer price
        String paymentStatus
        Date createdDate
    }

    Cart {
        Long id PK
        Long userId
        Long productDetailId
        Integer quantity
        Date createdDate
    }

    User ||--o{ Balance : "1:1"
    Balance ||--o{ BalanceHistory : "1:N"
    User ||--o{ Order : "1:N"
    User ||--o{ Payment : "1:N"
    Order ||--o{ OrderItem : "1:N"
    Order ||--o{ Payment : "1:1"
    OrderItem ||--o{ ProductDetail : "1:1"
    User ||--o{ Cart : "1:1"
    Product ||--o{ ProductDetail : "1:1"
    ProductDetail ||--o{ Cart : "N:1"
```
