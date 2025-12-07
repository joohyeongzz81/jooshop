# REST API 컨벤션

## 기본 원칙

1. **명사 사용**: 동사 ❌, 명사 ⭕
2. **복수형 사용**: 컬렉션 리소스는 복수형
3. **소문자 + 하이픈**: kebab-case 사용
4. **계층 구조**: URL로 리소스 관계 명확히 표현
5. **HTTP 메서드**: CRUD에 적절한 메서드 사용

---

## URL 설계 규칙

### 1. 리소스 타입별 네이밍

#### Document (단일 리소스)
- 단수형 사용
- 특정 하나의 객체

```
✅ /api/users/123
✅ /api/products/456/detail
✅ /api/admin/settings

❌ /api/user/123
❌ /api/getUser/123
```

#### Collection (리소스 집합)
- 복수형 사용
- 여러 개의 리소스

```
✅ /api/users
✅ /api/products
✅ /api/notifications

❌ /api/user
❌ /api/userList
❌ /api/get-users
```

#### Store (클라이언트 관리 리소스)
- 복수형 사용
- 클라이언트가 리소스를 추가/삭제

```
✅ /api/users/123/favorites
✅ /api/users/123/cart-items
✅ /api/playlists/456/songs

❌ /api/users/123/favorite
❌ /api/addToCart
```

#### Controller (동작/프로세스)
- 동사 사용 가능
- 특정 작업 수행

```
✅ /api/users/123/activate
✅ /api/orders/456/cancel
✅ /api/notifications/send

❌ /api/users/123/activation
❌ /api/cancelOrder/456
```

---

## 계층 구조 설계

### 1. 기본 구조
```
/api/{collection}/{document}/{collection}
```

### 2. 실제 예시
```
✅ /api/users/123/orders
   └── users(collection) → 123(document) → orders(collection)

✅ /api/products/456/reviews/789
   └── products(collection) → 456(document) → reviews(collection) → 789(document)

✅ /api/users/123/cart-items/add
   └── users(collection) → 123(document) → cart-items(store) → add(controller)
```

### 3. 깊이 제한
- **최대 3-4단계** 권장
- 너무 깊으면 쿼리 파라미터 사용

```
✅ /api/users/123/orders?status=pending
❌ /api/users/123/orders/pending/items/active
```

---

## HTTP 메서드

| 메서드 | 용도 | Idempotent | Safe |
|--------|------|-----------|------|
| **GET** | 조회 | ✅ | ✅ |
| **POST** | 생성, 프로세스 실행 | ❌ | ❌ |
| **PUT** | 전체 수정 (없으면 생성) | ✅ | ❌ |
| **PATCH** | 부분 수정 | ❌ | ❌ |
| **DELETE** | 삭제 | ✅ | ❌ |

---

## URI 네이밍 규칙

### 1. 소문자 사용
```
✅ /api/order-items
❌ /api/OrderItems
❌ /api/orderItems
```

### 2. 하이픈(-) 사용, 언더스코어(_) 지양
```
✅ /api/order-items
✅ /api/user-profiles

❌ /api/order_items
❌ /api/userProfiles
```

### 3. 파일 확장자 사용 금지
```
✅ /api/users/123
❌ /api/users/123.json
```

### 4. 동사 사용 금지 (Controller 제외)
```
✅ /api/users
✅ /api/orders

❌ /api/getUsers
❌ /api/createOrder
```

### 5. CRUD 함수명 사용 금지
```
✅ GET /api/users/123
❌ /api/users/delete/123
❌ /api/users/update/123
```

---

## 리소스 관계 표현

### 1. 종속 관계 (Has-Many)
```
# 특정 사용자의 주문 목록
GET /api/users/123/orders

# 특정 상품의 리뷰 목록
GET /api/products/456/reviews
```

### 2. 독립 관계
```
# 주문 자체 조회 (사용자 정보 불필요)
GET /api/orders/789

# 리뷰 자체 조회 (상품 정보 불필요)
GET /api/reviews/321
```

### 3. 다대다 관계
```
# 사용자가 좋아요한 상품들
GET /api/users/123/liked-products

# 상품을 좋아요한 사용자들
GET /api/products/456/liked-users
```

---

## 쿼리 파라미터

### 1. 필터링
```
GET /api/products?category=electronics&status=active
```

### 2. 정렬
```
GET /api/products?sort=price&order=desc
GET /api/users?sort=-createdAt  # - 는 내림차순
```

### 3. 페이징
```
GET /api/products?page=2&size=20
GET /api/products?offset=20&limit=20
```

### 4. 필드 선택
```
GET /api/users?fields=id,name,email
```

---

## HTTP 상태 코드

### 성공 (2xx)
| 코드 | 의미 | 사용 시점 |
|------|------|----------|
| 200 | OK | GET, PUT, PATCH 성공 |
| 201 | Created | POST 성공 (리소스 생성) |
| 204 | No Content | DELETE 성공 |

### 클라이언트 오류 (4xx)
| 코드 | 의미 | 사용 시점 |
|------|------|----------|
| 400 | Bad Request | 잘못된 요청 |
| 401 | Unauthorized | 인증 실패 |
| 403 | Forbidden | 권한 없음 |
| 404 | Not Found | 리소스 없음 |
| 409 | Conflict | 중복, 충돌 |

### 서버 오류 (5xx)
| 코드 | 의미 | 사용 시점 |
|------|------|----------|
| 500 | Internal Server Error | 서버 오류 |
| 503 | Service Unavailable | 서비스 이용 불가 |

---

## 버전 관리

### 1. URI 버전
```
✅ /api/v1/users
✅ /api/v2/users
```

### 2. Header 버전
```
GET /api/users
Accept: application/vnd.api.v1+json
```

---

## 예시 모음

### Collection + Document
```
GET    /api/users              # Collection 조회
GET    /api/users/123          # Document 조회
POST   /api/users              # Document 생성
PUT    /api/users/123          # Document 전체 수정
PATCH  /api/users/123          # Document 부분 수정
DELETE /api/users/123          # Document 삭제
```

### 계층 구조
```
GET /api/users/123/orders                    # 사용자의 주문 목록
GET /api/users/123/orders/456                # 사용자의 특정 주문
GET /api/users/123/orders/456/items          # 주문의 상품 목록
```

### Store
```
POST   /api/users/123/favorites/456          # 즐겨찾기 추가
DELETE /api/users/123/favorites/456          # 즐겨찾기 삭제
GET    /api/users/123/favorites              # 즐겨찾기 목록
```

### Controller
```
POST /api/users/123/activate                 # 계정 활성화
POST /api/orders/456/cancel                  # 주문 취소
POST /api/notifications/send                 # 알림 발송
```
