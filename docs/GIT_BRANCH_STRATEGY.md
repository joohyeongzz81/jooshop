# Git 브랜치 전략

## 전략 개요

**포트폴리오 프로젝트 특성을 고려한 간결한 2-3단계 브랜치 전략**

개인 프로젝트이므로 복잡한 배포 프로세스(QA, Staging 등) 없이
**개발 → 완성**의 명확한 흐름에 집중했습니다.

---

## 브랜치 구조

```
main                    # 안정 버전
 └── develop           # 개발 진행 중
      └── feature/기능명  # 큰 기능 개발 시 (선택)
```

---

## 브랜치별 역할

### `main`
- **안정적으로 동작하는 버전**
- 주요 기능 완성 시점에 병합
- Tag로 버전 관리 (v0.1.0, v0.2.0 등)
- 직접 커밋 ❌

### `develop`
- **실제 개발이 진행되는 메인 작업 브랜치**
- 모든 개발 작업의 기본 브랜치
- 작은 기능은 여기서 바로 작업
- 수시로 커밋 및 푸시

### `feature/기능명` (선택)
- **복잡한 기능 개발 시에만** 사용
- develop에서 분기 → 완료 후 develop에 병합
- 예: Redis 캐싱, RabbitMQ 연동 등 큰 작업

---


## 작업 흐름

### 1. 일반 작업 (작은 기능)
```bash
# develop에서 작업
git checkout develop
git pull origin develop

# 코드 작성 후 커밋
git add .
git commit -m "feat(domain): User 엔티티 추가"
git push origin develop
```

### 2. 큰 기능 작업 (Redis, RabbitMQ 등)
```bash
# feature 브랜치 생성
git checkout develop
git checkout -b feature/redis-cache

# 작업 및 커밋
git add .
git commit -m "feat(config): Redis 캐싱 설정 추가"
git push origin feature/redis-cache

# develop에 병합
git checkout develop
git merge feature/redis-cache
git push origin develop

# feature 브랜치 삭제
git branch -d feature/redis-cache
```

### 3. 주요 기능 완성 → main 병합
```bash
# 기본 기능 완성 시
git checkout main
git merge develop
git tag v0.1.0 -m "기본 CRUD 및 알림 발송 기능 완성"
git push origin main --tags

# 성능 최적화 완료 시
git tag v0.2.0 -m "N+1 문제 해결, 인덱스 최적화 완료"
git push origin main --tags

# 대규모 트래픽 대응 완료 시
git tag v0.3.0 -m "Redis, RabbitMQ, ProxySQL 적용"
git push origin main --tags

# 프로젝트 완성 시
git tag v1.0.0 -m "동시성 제어, 모니터링 완료"
git push origin main --tags
```

---

## 브랜치 네이밍 규칙

```
feature/user-api           # 회원 API
feature/notification       # 알림 기능
```

---

## 버전 관리 (Semantic Versioning)

### 시맨틱 버저닝 규칙

```
v{Major}.{Minor}.{Patch}
  예: v1.2.3
```

| 구분 | 의미 | 예시 |
|------|------|------|
| **Major** | 대규모 기능 추가, 구조 변경 | 1.0.0 → 2.0.0 |
| **Minor** | 새로운 기능 추가 (하위 호환) | 1.0.0 → 1.1.0 |
| **Patch** | 버그 수정, 작은 개선 | 1.0.0 → 1.0.1 |

### 프로젝트 버전 계획

| Tag | 주요 내용 | 변경 사항 |
|-----|----------|-----------|
| v0.1.0 | 기본 CRUD, 알림 발송 기능 | 초기 버전 |
| v0.2.0 | N+1 해결, 인덱스 최적화, 반정규화 | Minor: 성능 최적화 기능 추가 |
| v0.3.0 | Redis, RabbitMQ, ProxySQL 적용 | Minor: 대규모 트래픽 대응 기능 추가 |
| v1.0.0 | 동시성 제어, 모니터링, 프로젝트 완성 | Major: 정식 릴리즈 |

### 태그 생성 예시

```bash
# Minor 버전 (새 기능)
git tag v0.2.0 -m "feat: N+1 문제 해결 및 인덱스 최적화"

# Patch 버전 (버그 수정)
git tag v0.2.1 -m "fix: 알림 중복 발송 버그 수정"

# Major 버전 (프로젝트 완성)
git tag v1.0.0 -m "release: 프로젝트 정식 릴리즈"

# GitHub에 푸시
git push origin --tags
```

