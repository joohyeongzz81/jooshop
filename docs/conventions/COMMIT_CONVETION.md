# Commit Convention

## 기본 형식 (AngularJS 기준)

```
<type>(<scope>): <subject>

<body>

<footer>
```

---

## Type (필수)

| Type | 설명 | 예시 |
|------|------|------|
| **feat** | 새로운 기능 추가 | 알림 발송 API 구현 |
| **fix** | 버그 수정 | 알림 중복 발송 버그 수정 |
| **refactor** | 코드 리팩토링 | N+1 문제 해결 |
| **perf** | 성능 개선 | 인덱스 추가로 조회 성능 개선 |
| **docs** | 문서 수정 | README 업데이트 |
| **test** | 테스트 코드 | 알림 서비스 단위 테스트 추가 |
| **chore** | 빌드, 설정 | Gradle 의존성 추가 |
| **style** | 코드 포맷팅 | 코드 스타일 정리 |

---

## Scope (선택)

- **domain**: 엔티티
- **service**: 비즈니스 로직
- **controller**: API
- **config**: 설정
- **redis**: Redis 관련
- **rabbitmq**: RabbitMQ 관련
- **db**: 데이터베이스 관련

---

## Subject (필수)

- 50자 이내
- 마침표 없음
- 명령문 사용 ("추가했음" ❌, "추가" ⭕)

---

## Body (선택)

- 상세 설명이 필요할 때 작성
- 72자마다 줄바꿈

---

## Footer (선택)

- Issue 번호 참조: `Relates to #123`, `Resolves #123`

---

## ✅ 좋은 예시

```
feat(domain): User, Notification 엔티티 추가

- User 기본 필드 구성
- Notification과 연관관계 설정

Relates to #1
```

```
perf(service): 알림 조회 쿼리 인덱스 최적화

- (user_id, is_read, created_at) 복합 인덱스 추가
- 조회 시간 850ms → 45ms 개선

Performance documented in docs/performance/01-index.md

Resolves #15
```

```
refactor(repository): N+1 문제 해결

- Fetch Join 적용
- 쿼리 수 101개 → 1개 감소
```

---

## ❌ 나쁜 예시

```
Update code  ❌ (type 없음, 불명확)
```

```
feat: 기능 추가했음  ❌ (scope 없음, 명령문 아님)
```

```
fix(service): 알림 발송 버그 수정, 구독 API도 수정하고, 테스트 코드도 추가함  ❌ (한 커밋에 여러 작업)
```

---

## 💡 Tip

- **한 커밋 = 한 작업**: 여러 작업을 한 커밋에 섞지 않기
- **의미 있는 단위로 커밋**: 너무 작거나 크지 않게
- **리팩토링과 기능 추가 분리**: 기능 추가는 `feat`, 리팩토링은 `refactor`로 구분