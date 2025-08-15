<div align="center">

<!-- logo -->
<img width="250" height="250" alt="KakaoTalk_20250812_205531414" src="https://github.com/user-attachments/assets/d3cdcdd4-71e0-45fe-93fc-8fe9afe81fdc" />

### COREDISC-BE README
[<img src="https://img.shields.io/badge/release-v0.0.0-yellow?style=flat&logo=google-chrome&logoColor=white" />]() 
<br/> [<img src="https://img.shields.io/badge/프로젝트 기간-2025.07.01~현재-green?style=flat&logo=&logoColor=white" />]()

</div> 


## 프로젝트 소개 — Coredisc

**Coredisc**는 하루 4개의 질문에 답하며 나만의 ‘코어’를 찾아가는 기록 서비스입니다.
단순한 메모를 넘어, 나의 생각과 패턴을 분석하여 더 깊은 자기 이해를 돕습니다.



<br />

<img width="3840" height="2160" alt="image" src="https://github.com/user-attachments/assets/62939b61-30d4-4bd9-82cd-ac435de7cc2d" />


<br />

## 👥 팀원 소개

<table>
  <tr>
    <th>Backend</th>
    <th>Backend</th>
    <th>Backend</th>
    <th>Backend</th>
    <th>Backend</th>
  </tr>
  <tr>
    <td align="center">
      <a href="https://github.com/kimyuchan-k1">
        <img src="https://github.com/kimyuchan-k1.png" width="180px;" alt="김유찬"/><br />
        <sub><b>김유찬</b></sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/dokyung-kang">
        <img src="https://github.com/dokyung-kang.png" width="180px;" alt="강도경"/><br />
        <sub><b>강도경</b></sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/ParkSenn">
        <img src="https://github.com/ParkSenn.png" width="180px;" alt="박세은"/><br />
        <sub><b>박세은</b></sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/Jieun13">
        <img src="https://github.com/Jieun13.png" width="180px;" alt="백지은"/><br />
        <sub><b>백지은</b></sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/hmj6589">
        <img src="https://github.com/hmj6589.png" width="180px;" alt="황민지"/><br />
        <sub><b>황민지</b></sub>
      </a>
    </td>
  </tr>
</table>

<br />

## 프로젝트 구조

```
src/main/java/com/coredisc
├── presentation            # Presentation Layer: 클라이언트와 상호작용
│   ├── controller          # API Endpoints (HTTP 요청 처리)
│   └── dto                 # 데이터 전송 객체 (Request/Response)
│
├── application             # Application Layer: 유스케이스 처리
│   ├── service             # 비즈니스 로직 흐름 제어, 트랜잭션 관리
│   └── schedule            # 스케줄링 작업
│
├── domain                  # Domain Layer: 핵심 비즈니스 로직
│   ├── model               # 도메인 모델 (Entity, VO)
│   └── repository          # 데이터 영속성 인터페이스 정의
│
├── infrastructure          # Infrastructure Layer: 외부 시스템 연동, 기술 구현
│   ├── repository          # 데이터 영속성 구현체 (JPA, etc.)
│   └── aws.s3              # 외부 서비스(S3) 연동 구현
│
├── common                  # Common: 프로젝트 전반에서 사용되는 공통 모듈
│   ├── apiPayload          # 공통 API 응답 형식
│   ├── exception           # 커스텀 예외 처리
│   └── util                # 유틸리티 클래스
│
└── security                # Security: 인증/인가 관련
    ├── auth                # 인증 관련 로직
    └── jwt                 # JWT 토큰 처리
```



## 🗂️ APIs
작성한 API는 아래에서 확인할 수 있습니다.

👉🏻 [API 바로보기](https://regular-snowdrop-139.notion.site/API-223790b4dd9581fe84e9e52b2b21b0d5?pvs=74)


<br />

## ⚙ 기술 스택
### Back-end
<div>
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/Java.png?raw=true" width="80">
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/SpringBoot.png?raw=true" width="80">
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/SpringSecurity.png?raw=true" width="80">
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/SpringDataJPA.png?raw=true" width="80">
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/Mysql.png?raw=true" width="80">
</div>

### Infra
<div>
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/AWSEC2.png?raw=true" width="80">
</div>

### Tools
<div>
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/Github.png?raw=true" width="80">
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/Notion.png?raw=true" width="80">
</div>

<br />

## 🛠️ 서버 아키텍쳐
<img width="700" height="570" alt="image" src="https://github.com/user-attachments/assets/c3ef32af-0169-4021-bedd-5ca3a7fdb7c8" />

## Branch Guideline
- main브랜치(배포용)
- develop 브랜치(개발용)
- feature/#이슈번호(각자 기능 개발할 때 생성할 브랜치 이름 양식)
