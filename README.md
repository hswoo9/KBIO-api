# K-BIO API

## 소스 품질 검사 가이드

### 개발 환경 설정

#### 1. IntelliJ IDEA 플러그인 설치
1. PMD 플러그인
   - Settings > Plugins > Marketplace에서 "PMD" 검색
   - "PMD" 플러그인 설치
   - IDE 재시작

2. SpotBugs 플러그인
   - Settings > Plugins > Marketplace에서 "SpotBugs" 검색
   - "SpotBugs IntelliJ IDEA Plugin" 설치
   - IDE 재시작

3. SonarLint 플러그인
   - Settings > Plugins > Marketplace에서 "SonarLint" 검색
   - "SonarLint" 플러그인 설치
   - IDE 재시작

#### 2. 플러그인 설정
1. PMD 설정
   - Settings > Tools > PMD
   - Ruleset 파일 위치: `config/pmd/ruleset.xml`
   - 자동 검사 활성화: "Run PMD on the fly" 체크

2. SpotBugs 설정
   - Settings > Tools > SpotBugs
   - "Analyze affected files after compile" 체크
   - Security 분석 활성화: "Security" 체크

3. SonarLint 설정
   - Settings > Tools > SonarLint
   - "Automatically trigger analysis" 체크

### 품질 검사 실행 방법

#### 1. IDE 내 검사
1. PMD 검사
   - 프로젝트 우클릭 > PMD > Check Code
   - 또는 단축키: `Ctrl+Shift+P` (Mac: `Cmd+Shift+P`)

2. SpotBugs 검사
   - 프로젝트 우클릭 > Analyze > Run Inspection by Name
   - "SpotBugs" 입력 후 실행

3. SonarLint 검사
   - 프로젝트 우클릭 > SonarLint > Analyze All Files

#### 2. Maven 명령어로 검사
```bash
# PMD 검사
mvn pmd:check

# SpotBugs 검사
mvn spotbugs:check

# 전체 품질 검사 (PMD + SpotBugs)
mvn verify
```

### 검사 결과 확인

#### 1. IDE 내 결과 확인
- PMD: Problems 탭에서 "PMD" 카테고리 확인
- SpotBugs: Problems 탭에서 "SpotBugs" 카테고리 확인
- SonarLint: SonarLint 탭에서 확인

#### 2. HTML 리포트 확인
- PMD 리포트: `target/site/pmd.html`
- SpotBugs 리포트: `target/site/spotbugs.html`
- 종합 품질 리포트: `target/site/quality-security-report.html`

### 품질 기준

#### 1. 심각도 기준
- 높음: 보안 취약점, 심각한 버그 가능성
- 중간: 성능 이슈, 잠재적 버그
- 낮음: 코딩 컨벤션, 가독성 이슈

#### 2. 필수 개선 항목
- 모든 높은 심각도 이슈
- 보안 관련 이슈
- 성능에 직접적 영향을 주는 이슈

### 개선 프로세스

#### 1. 일일 점검
- 작성한 코드 커밋 전 IDE 내 검사 실행
- 발견된 이슈 즉시 수정

#### 2. 주간 리뷰
- 팀 단위 코드 품질 리뷰
- 주요 이슈 및 개선사항 논의

#### 3. 월간 품질 관리
- 전체 코드 품질 검사 실행
- 품질 메트릭 추이 분석
- 개선 계획 수립 및 조정

### 문의 및 지원
- 품질 관리 담당자: [담당자명](mailto:email@example.com)
- 품질 관리 위키: [위키 페이지 URL] 