# User 도메인 설계

## 테이블: users

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 회원 ID |
| email | VARCHAR(255) | UNIQUE, NOT NULL | 이메일 |
| name | VARCHAR(100) | NOT NULL | 이름 |
| created_at | TIMESTAMP | NOT NULL | 생성일시 |
| updated_at | TIMESTAMP | NOT NULL | 수정일시 |
| deleted_at | TIMESTAMP | NULL | 삭제일시 |

---

## 비즈니스 규칙

- 이메일 중복 불가
- 이름: 2-100자
- Soft Delete

---

## 연관관계

```
[users] 1 ─── * [product_restock_subscriptions]
[users] 1 ─── * [notifications]
```