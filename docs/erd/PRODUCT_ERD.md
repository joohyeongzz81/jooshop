# Product 도메인 설계

## 테이블: products

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 상품 ID |
| name | VARCHAR(255) | NOT NULL | 상품명 |
| stock | INT | NOT NULL, DEFAULT 0 | 재고 수량 |
| created_at | TIMESTAMP | NOT NULL | 생성일시 |
| updated_at | TIMESTAMP | NOT NULL | 수정일시 |
| deleted_at | TIMESTAMP | NULL | 삭제일시 |

---

## 비즈니스 규칙

- 재고는 0 이상
- 재고 부족 시 감소 불가
- 재고 추가 시 구독자에게 알림 자동 발송

---

## 연관관계

```
[products] 1 ─── * [product_restock_subscriptions]
[products] 1 ─── * [notifications] (논리적)
```