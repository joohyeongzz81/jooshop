# User 도메인 ERD

## 테이블: users

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 회원 ID |
| email | VARCHAR(255) | UNIQUE, NOT NULL | 이메일 (로그인 ID) |
| password | VARCHAR(255) | NOT NULL | 비밀번호 (BCrypt 암호화) |
| name | VARCHAR(100) | NOT NULL | 이름 |
| role | VARCHAR(20) | NOT NULL | 역할 (BUYER, SELLER, ADMIN) |
| created_at | TIMESTAMP | NOT NULL | 생성일시 |
| updated_at | TIMESTAMP | NOT NULL | 수정일시 |
| deleted_at | TIMESTAMP | NULL | 삭제일시 |

---

## 역할 (Role)

| 역할 | 설명 |
|------|------|
| BUYER | 구매자 (일반 회원) |
| SELLER | 판매자 (상품 등록/재고 관리 가능) |
| ADMIN | 관리자 (모든 권한) |

---

## 비즈니스 규칙

- 이메일 중복 불가
- 비밀번호는 BCrypt로 암호화하여 저장
- 이름: 2-100자
- 회원가입 시 기본 role은 BUYER
- Soft Delete

---

## 연관관계

```
[users] 1 ─── * [product_restock_subscriptions]
[users] 1 ─── * [notifications]
```