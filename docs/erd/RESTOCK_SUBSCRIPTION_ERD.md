# 재입고 구독 도메인 ERD

## 테이블: product_restock_subscriptions

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 구독 ID |
| user_id | BIGINT | FK, NOT NULL | 회원 ID |
| product_id | BIGINT | FK, NOT NULL | 상품 ID |
| is_active | BOOLEAN | NOT NULL, DEFAULT true | 활성화 여부 |
| created_at | TIMESTAMP | NOT NULL | 생성일시 |
| updated_at | TIMESTAMP | NOT NULL | 수정일시 |
| deleted_at | TIMESTAMP | NULL | 삭제일시 |

---

## 비즈니스 규칙

- 한 유저는 동일 상품에 하나의 활성 구독만 가능
- 구독 취소 시 is_active = false (Soft Delete)
- 재구독 시 기존 구독 재활성화

---

## 연관관계

```
[users] 1 ─── * [product_restock_subscriptions] * ─── 1 [products]
```