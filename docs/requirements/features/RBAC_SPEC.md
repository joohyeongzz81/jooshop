# RBAC (Role-Based Access Control) 명세서

## 역할별 권한

### BUYER (구매자)
**할 수 있는 것:**
- 상품 조회 (목록, 상세)
- 재입고 알림 구독/취소
- 내 알림 조회/읽음 처리
- 내 정보 조회

**할 수 없는 것:**
- 상품 등록
- 재고 추가/감소
- 다른 회원 정보 조회

---

### SELLER (판매자)
**할 수 있는 것:**
- BUYER의 모든 권한
- 상품 등록
- 재고 추가/감소

**할 수 없는 것:**
- 다른 회원 정보 조회

---

### ADMIN (관리자)
**할 수 있는 것:**
- 모든 권한
- 모든 회원 정보 조회
- 모든 상품 관리

---

## API 권한 매트릭스

| API | BUYER | SELLER | ADMIN |
|-----|-------|--------|-------|
| POST /api/auth/signup | ✅ | ✅ | ❌ |
| POST /api/auth/login | ✅ | ✅ | ✅ |
| GET /api/users/{id} | 본인만 | 본인만 | ✅ |
| POST /api/products | ❌ | ✅ | ✅ |
| GET /api/products | ✅ | ✅ | ✅ |
| GET /api/products/{id} | ✅ | ✅ | ✅ |
| PATCH /api/products/{id}/stock | ❌ | ✅ | ✅ |
| POST /api/restock-subscriptions | ✅ | ✅ | ✅ |
| DELETE /api/restock-subscriptions | ✅ | ✅ | ✅ |
| GET /api/notifications | ✅ | ✅ | ✅ |
| GET /api/notifications/unread | ✅ | ✅ | ✅ |
| GET /api/notifications/unread/count | ✅ | ✅ | ✅ |
| PATCH /api/notifications/{id}/read | ✅ | ✅ | ✅ |