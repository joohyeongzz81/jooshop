# 알림 도메인 ERD

## 테이블: notifications

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 알림 ID |
| user_id | BIGINT | FK, NOT NULL | 회원 ID |
| type | VARCHAR(50) | NOT NULL | 알림 타입 |
| title | VARCHAR(255) | NOT NULL | 제목 |
| content | TEXT | NOT NULL | 내용 |
| reference_id | BIGINT | NOT NULL | 참조 ID |
| is_read | BOOLEAN | NOT NULL, DEFAULT false | 읽음 여부 |
| read_at | TIMESTAMP | NULL | 읽은 시각 |
| created_at | TIMESTAMP | NOT NULL | 생성일시 |
| updated_at | TIMESTAMP | NOT NULL | 수정일시 |
| deleted_at | TIMESTAMP | NULL | 삭제일시 |

---

## 알림 타입 (NotificationType)

| 타입 | 설명 | reference_id |
|------|------|--------------|
| PRODUCT_RESTOCK | 상품 재입고 | productId |
| ORDER_SHIPPED | 주문 배송 | orderId |
| ORDER_DELIVERED | 주문 완료 | orderId |

---

## 비즈니스 규칙

- 모든 타입의 알림을 하나의 테이블에서 관리
- type으로 알림 종류 구분
- reference_id로 관련 리소스 참조
- 읽음 처리 시 is_read = true, read_at 기록

---

## 연관관계

```
[users] 1 ─── * [notifications]
```