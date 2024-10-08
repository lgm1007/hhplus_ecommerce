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
        String options
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
    User ||--o{ Order : "1:N"
    User ||--o{ Payment : "1:N"
    Order ||--o{ OrderItem : "1:N"
    Order ||--o{ Payment : "1:1"
    User ||--o{ Cart : "1:1"
    Product ||--o{ ProductDetail : "1:1"
    ProductDetail ||--o{ OrderItem : "N:1"
    ProductDetail ||--o{ Cart : "N:1"
```
