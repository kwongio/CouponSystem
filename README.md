# **프로젝트 주제: 선착순 쿠폰 발급 시스템**


기간: 2024.12.09 ~ 2024.12.20

## **목차**

1. [아키텍처](#1-아키텍처)
2. [프로젝트 개요](#2-프로젝트-개요)
3. [주요 기능](#3-주요-기능)
4. [개발 목표 및 의도](#4-개발-목표-및-의도)
5. [사용한 기술](#5-사용한-기술)
6. [ERD (Entity-Relationship Model)](#6-erd-entity-relationship-model)
7. [Kafka 설정](#7-kafka-설정)
8. [시퀀스 다이어그램](#8-시퀀스-다이어그램)
9. [패키지 구조](#9-패키지-구조)
10. [API 문서](#10-api-문서)
11. [성능 테스트 및 결과](#11-성능-테스트-및-결과)
    - [JMeter를 사용한 트래픽 시뮬레이션](#jmeter를-사용한-트래픽-시뮬레이션)
    - [시스템 성능 분석](#시스템-성능-분석)
12. [관련 포트 정리](#12-관련-포트-정리)
13. [프로젝트 관련 GitHub 리포지토리](#13-프로젝트-관련-github-리포지토리)
15. [부족한 점](#14-부족한-점)
14. [향후 계획](#15-향후-계획)


## 1. 아키텍처

![스크린샷 2024-12-20 123420](https://github.com/user-attachments/assets/587c7b79-703b-4bf1-92ee-2eff232cf893)

## **2. 프로젝트 개요**


- **프로젝트명**: 선착순 쿠폰 시스템
- **목표**: 짧은 시간 내에 대규모 트래픽을 처리할 수 있는 쿠폰 시스템 설계 및 구현.

## **3. 주요 기능**


1. **선착순 쿠폰 생성**
    - 사용자 요청에 따라 선착순 쿠폰을 생성할 수 있는 기능.
    - **Kafka**를 이용하여 쿠폰 생성 이벤트를 비동기적으로 처리.
2. **쿠폰 조회 및 할당**
    - **쿠폰 조회**: 사용자가 발급한 쿠폰을 조회할 수 있는 기능.
    - **쿠폰 할당**: 선착순 기준으로 쿠폰을 할당하는 기능.
3. **Kafka, Redis 기반 아키텍처**
    - **Kafka**를 이용한 이벤트 기반 시스템 설계.
    - **Redis**를 활용하여 쿠폰의 상태와 카운트를 관리하고 빠른 조회 성능 제공.
4. **성능 모니터링 및 APM 분석**
    - **Grafana**와 **Pinpoint**를 이용한 시스템 모니터링 및 성능 분석.
    - **Prometheus**와 **Grafana**를 통한 실시간 메트릭 수집 및 대시보드 구성.
5. **성능 테스트**
    - **JMeter**를 사용한 부하 테스트와 시스템 성능 분석.
    - 시스템의 병목 구간 파악 및 최적화 수행.



## **4. 개발 목표**


1. **EDA 기반 시스템 설계**
    - **Kafka**와 **Redis**를 활용하여 **Event-Driven Architecture**(EDA) 기반의 시스템 설계 및 구현 학습.
2. **실시간 대규모 트래픽 처리**
    - 선착순 쿠폰 시스템을 통해 단시간 내 발생하는 **대규모 트래픽을 효율적으로 처리**하고, 데이터 흐름과 분산 시스템의 특성을 이해.
3. **실시간 모니터링 및 분석**
    - **Grafana**와 **Pinpoint**를 적용하여 애플리케이션 성능 모니터링 및 분산 시스템 트랜잭션 추적을 실습.
    - Prometheus 기반 **메트릭 수집**과 **APM** 분석 도구 활용 능력 강화.
4. **성능 테스트 및 최적화 학습**
    - **JMeter**를 활용한 부하 테스트 및 병목 구간 분석.
    - 실시간 대규모 요청 처리에 대한 **성능 최적화 방안**을 학습

## **5. 사용한 기술**

| **분류** | **사용한 기술** |
| --- | --- |
| **Backend** | Spring Boot 3.x, Java 17, Spring Data JPA, Lombok, Validation, REST API |
| **Database** | MySQL, Redis |
| **Messaging & Streaming** | Kafka 2.x, Zookeeper, Kafka-UI |
| **Containerization** | Docker |
| **Build & Dependency Management** | Gradle |
| **Monitoring & Observability** | Prometheus, Grafana, Prometheus Micrometer, Spring Boot Actuator |
| **Exporters** | mysql-exporter, redis-exporter, kafka-exporter |
| **APM (Application Performance Management)** | Pinpoint |
| **Testing & API Documentation** | JUnit, JMeter, Swagger (springdoc-openapi) |
| **Logging & Filtering** | Logback (MDC 기반 traceId 설정, UUID를 사용한 요청별 고유 식별자 생성) |

## 6. ERD (Entity-Relationship Model)

![스크린샷 2024-12-20 122358](https://github.com/user-attachments/assets/04e3ec8e-f9ae-41e6-a3b8-08af783222b5)


### **1. ERD 구성**

- **Coupon 테이블**: 쿠폰의 기본 정보를 저장.
    - `ID`: 쿠폰의 기본 키 (자동 증가).
    - `TITLE`: 쿠폰의 제목.
    - `QUANTITY`: 쿠폰의 총 수량.
    - `START_DATE`, `END_DATE`: 쿠폰의 유효 기간.
    - `CREATED_AT`: 쿠폰 생성일.
- **CouponAssignLog 테이블**: 쿠폰 발급 이력을 저장.
    - `ID`: 쿠폰 발급 기록의 기본 키 (자동 증가).
    - `UUID`: **중복 방지 키**, 쿠폰 발급 요청의 고유 식별자.
    - `COUPON_ID`: 쿠폰의 외래 키. (Coupon 테이블의 `ID`와 연결)

### **2. UUID를 `unique key`로 사용한 이유**

- **Kafka Producer에서 `enable.idempotence=true`** 옵션을 통해 멱등성을 활성화하더라도, **중복 요청**이 발생할 가능성이 존재합니다.
- **중복 발급 방지**를 위해, 쿠폰 발급 요청마다 고유한 **UUID**를 생성하여 이를 `unique key`로 지정했습니다.
- UUID를 사용함으로써, 동일한 요청이 여러 번 처리되는 경우에도 **한 번만 발급**되도록 보장할 수 있습니다.

## 7. Kafka 설정


### Kafka 클러스터 구성
![스크린샷 2024-12-20 132546](https://github.com/user-attachments/assets/5edd41ff-b1ac-4ae2-913e-5c5505fa8749)

### 1. Broker 서버 설정

- **Kafka 브로커 서버**: 3대
- **`min.insync.replicas=2`***(최소 2개의 복제본이 동기화된 상태에서만 메시지가 성공적으로 전송)*


### 2. Topic 설정

- **Coupon-Assign** *(쿠폰 발급 이벤트)*
    - 파티션 수: 3
    - 복제본 수: 3
- **Coupon-Assign-DLT** *(쿠폰 발급 실패 이벤트)*
    - 파티션 수: 3
    - 복제본 수: 3


### 3. API 서버 설정

**Producer 설정**

- **데이터 무결성 보장**
    - `enable.idempotence=true` *(멱등성 활성화)*
    - `acks=all` *(모든 복제본 동기화 완료 후 응답)*
- **배치 및 전송 최적화**
    - `linger.ms=100ms` *(배치 처리 대기 시간)*
    - `batch.size=1MB` *(배치 처리 최대 크기)*
- **재시도 및 타임아웃**
    - 최대 재시도 횟수: 5회
    - 재시도 간격: 1초
    - 요청 타임아웃: 10초
- **에러 처리**
    - **쿠폰 발급 실패 시**: 메시지는 Dead Letter Queue(Coupon-Assign-DLT)로 전송.


### 4. Consumer 서버 설정

**Coupon-Assign** *(배치 처리)*

- **Batch Listener 활성화**
    - 동시 처리(Concurrency): 3
    - 최대 폴링 레코드 수: 500
- **Offset 설정**
    - `auto.offset.reset=earliest` *(가장 오래된 메시지부터 소비)*
- **에러 처리**
    - Exponential Backoff *(초기 간격 1초, 2배 증가, 최대 5회 재시도)*
    - 실패 메시지는 Dead Letter Queue(Coupon-Assign-DLT)로 전송


**Coupon-Assign-DLT** *(개별 처리)*

- **Batch Listener 비활성화**
    - 한 번에 하나의 메시지만 처리
- **Offset 설정**
    - `auto.offset.reset=earliest` *(가장 오래된 메시지부터 소비)*

## 8. 시퀀스 다이어그램

### 1. 쿠폰 발급 정상 처리
![선착순쿠폰시스템-정상처리 drawio](https://github.com/user-attachments/assets/8afe0831-60f2-4481-8077-c3a6bc8801a9)

### 2. Redis 쿠폰 개수 부족
![선착순쿠폰시스템-Redis 실패 drawio](https://github.com/user-attachments/assets/af978863-3c9f-4369-aea4-d0df48b8bf57)

### 3. Produce 실패(쿠폰 발급 이벤트 전송 실패)
![선착순쿠폰시스템-Produce 실패 drawio](https://github.com/user-attachments/assets/487e9826-70cd-48c5-8469-14725230d669)

### 4. Consumer 실패(쿠폰 발급 실패)
![선착순쿠폰시스템-Consumer 실패 drawio](https://github.com/user-attachments/assets/a0aad1bc-29af-4e8b-89a3-a96f81411327)


## 9. 패키지 구조

```jsx
CouponSystem
├── docker-compose                                        # Docker Compose 관련 설정 디렉토리.
│   ├── docker-compose-database.yml                       # 데이터베이스 관련 Docker Compose 설정 파일.
│   ├── docker-compose-kafka.yml                          # Kafka 클러스터를 설정하기 위한 Docker Compose 파일.
│   ├── docker-compose-kafka-ui.yml                       # Kafka UI를 설정하기 위한 Docker Compose 파일.
│   ├── docker-compose-monitoring.yml                     # Prometheus와 Grafana 기반의 모니터링을 설정하기 위한 Docker Compose 파일.
│   └── docker-compose-ngrinder.yml                       # 성능 테스트를 위한 nGrinder Docker Compose 파일.
├── pinpoint-agent-2.5.4                                  # Pinpoint 에이전트 디렉토리, 애플리케이션 성능 모니터링 설정을 포함.
├── pinpoint-docker-2.5.3                                 # Pinpoint 서버 설정 및 Docker 관련 파일이 포함된 디렉토리.
└── src                                                   
    ├── main                                             
    │   ├── java                                         
    │   │   └── com
    │   │       └── gio
    │   │           └── couponsystem
    │   │               │  CouponSystemApplication.java  # 애플리케이션의 진입점으로, Spring Boot 실행 클래스.
    │   │               │  
    │   │               ├── config
    │   │               │      CouponConfig.java         # 초기 Redis에 쿠폰 개수를 설정하는 클래스.
    │   │               │      JpaAuditingConfig.java    # JPA의 감사(Auditing) 기능 설정 클래스 (생성일, 수정일 자동 관리).
    │   │               │      KafkaProducerConfig.java  # Kafka 프로듀서의 설정을 정의한 클래스.
    │   │               │      KafkaTopicConfig.java     # Kafka 토픽 설정을 정의한 클래스.
    │   │               │      MDCLoggingFilter.java     # MDC를 사용하여 요청마다 고유 traceId를 생성하고 로그에 추가하는 필터.
    │   │               │      MetricConfig.java         # Prometheus를 위한 메트릭 수집 설정 클래스.
    │   │               │      ProducerConfigDebug.java  # Kafka 프로듀서 설정 디버깅을 위한 클래스.
    │   │               │      SwaggerConfig.java        # Swagger API 문서화를 위한 설정 클래스.
    │   │               │      
    │   │               ├── conpon
    │   │               │   ├── controller
    │   │               │   │      CouponController.java         # 쿠폰과 관련된 API 요청을 처리하는 컨트롤러.
    │   │               │   │      CouponSwaggerController.java  # Swagger 설정을 분리하기 위한 인터페이스로, 쿠폰 API 명세를 정의.
    │   │               │   │      
    │   │               │   ├── converter
    │   │               │   │      ObjectToStringConverter.java  # 객체를 문자열로 변환하는 유틸리티 클래스.
    │   │               │   │      
    │   │               │   ├── domain
    │   │               │   │      Coupon.java                   # 쿠폰 엔티티 클래스.
    │   │               │   │      CouponAssignLog.java          # 쿠폰 할당 로그 엔티티 클래스.
    │   │               │   │      
    │   │               │   ├── dto
    │   │               │   │      CouponCreateRequest.java      # 쿠폰 생성 요청 데이터를 담는 DTO.
    │   │               │   │      CouponCreateResponse.java     # 쿠폰 생성 응답 데이터를 담는 DTO.
    │   │               │   │      CouponQueryResponse.java      # 쿠폰 조회 응답 데이터를 담는 DTO.
    │   │               │   │      
    │   │               │   ├── repository
    │   │               │   │      CouponProducer.java           # Kafka 프로듀서 인터페이스.
    │   │               │   │      CouponRepository.java         # 쿠폰 데이터를 관리하는 JPA 레포지토리.
    │   │               │   │      
    │   │               │   ├── service
    │   │               │   │      CouponService.java            # 쿠폰 비즈니스 로직을 처리하는 서비스 클래스.
    │   │               │   │      
    │   │               │   └── validator
    │   │               │           CouponValidator.java         # 쿠폰 데이터 유효성 검증 로직.
    │   │               │           Validator.java               # Spring의 합성 어노테이션으로, 특정 클래스에 검증 컴포넌트로 등록.
    │   │               │          
    │   │               ├── event
    │   │               │      CouponAssignEvent.java            # 쿠폰 할당 이벤트 클래스.
    │   │               │      
    │   │               ├── exception
    │   │               │      CustomException.java              # 사용자 정의 예외 클래스.
    │   │               │      ExceptionCode.java                # 애플리케이션에서 사용하는 예외 코드 정의.
    │   │               │      GlobalExceptionHandler.java       # 글로벌 예외 처리 핸들러 클래스.
    │   │               │      ServerExceptionResponse.java      # 서버 예외 응답을 정의한 클래스.
    │   │               │      ValidationExceptionResponse.java  # 유효성 검증 실패 응답을 정의한 클래스.
    │   │               │      
    │   │               └── redis
    │   │                       RedisRepository.java             # Redis와 상호작용하는 클래스.
    │   │                      
    │   └── resources
    │           application.yml                                  # 애플리케이션 설정 파일.
    │           data.sql                                         # 초기 데이터를 삽입하는 SQL 파일.
    │           logback-spring.xml                               # Logback 로그 설정 파일.
    │           prometheus.yml                                   # Prometheus 설정 파일.
    │          
    └── test
        ├── java
        │   └── com
        │       └── gio
        │           └── couponsystem
        │               └── conpon
        │                   ├── controller
        │                   │      CouponControllerTest.java     # CouponController에 대한 통합 테스트 클래스.
        │                   │      
        │                   └── service
        │                          CouponServiceTest.java        # CouponService에 대한 통합 테스트 클래스.
        │                          
        └── resources
                application.yml                                  # 테스트 환경의 애플리케이션 설정 파일.

```

## 10. API 문서

![스크린샷 2024-12-20 181240](https://github.com/user-attachments/assets/5de14349-489a-45b2-8ad8-eb8f410c0ccd)


## **11. 성능 테스트 및 결과**

### **JMeter를 사용한 트래픽 시뮬레이션**

- **테스트 구성**:
    - **총 200개의 쓰레드(유저)**: 각 유저가 **100번의 요청**을 수행.
    - **목표**: 20,000개의 쿠폰 발급 요청을 시뮬레이션하여 성능 측정.
- **테스트 결과**:
    - **평균 응답 시간**: **102ms**
    - **에러율**: **0%**
    - **TPS (초당 트랜잭션 처리량)**: **1712.0 TPS**
    - **최대 응답 시간**: **279ms**
    - **표준편차**: **57.89ms**


### **시스템 성능 분석**

- **CPU 사용량**:
    - **System CPU Usage**: 테스트 시 시스템 CPU 사용량 **100%**에 도달. 이는 로컬 환경에서 부하가 발생했기 때문이며, 성능적으로 정확한 측정은 어려운 부분이 있지만, 부하가 예상보다 잘 처리됨.
    - **Spring CPU Usage**: Spring 프로세스 CPU 사용량은 비교적 낮았으며, 예상보다 성능을 잘 유지함.
- **Consumer Group Lag**:
    - **Lag 현상**: 테스트 초기에는 일부 lag이 쌓였지만, **Consumer 서버에서 정상적으로 처리**되어 lag 없이 완료됨.
- **MySQL QPS**:
    - **최대 QPS**: **1.72k**로 MySQL에서의 쿼리 처리량을 안정적으로 처리함.
- **Spring API 요청 처리량**:
    - **최대 요청 처리량**: **216 요청/초**를 처리함. 이는 대규모 트래픽 환경에서 Spring API가 잘 동작하고 있음을 확인.
- **쿠폰 발급 개수**:
    - **Redis**: `coupon:1` 키에서 20,000개의 쿠폰이 정상적으로 0으로 처리됨.
    - **MySQL**: `Coupon_Assign_Log` 테이블에 20,000개의 쿠폰이 정상적으로 저장됨.
- **메트릭**:
    - **쿠폰 발급 개수**: Prometheus 메트릭을 통해 **20,000개**의 쿠폰 발급 확인


### Jmeter 
![스크린샷 2024-12-20 172840](https://github.com/user-attachments/assets/ca3a6456-a677-4fd6-9ec2-227a42cd4e85)

### Consumer Group Lag
![스크린샷 2024-12-20 170457](https://github.com/user-attachments/assets/178ac50d-266c-4936-a8f0-0ec08bc6243e)

### CPU 사용량
![스크린샷 2024-12-20 170418](https://github.com/user-attachments/assets/9c1364e5-0d8a-4933-acb4-64b8007e2e52)

### Spring Requests/Second
![스크린샷 2024-12-20 170450](https://github.com/user-attachments/assets/e35ac263-5618-43eb-8437-045739e0375f)

### MySQL QPS
![스크린샷 2024-12-20 170513](https://github.com/user-attachments/assets/a796414a-35ca-4fbf-a0ab-1275ed92072f)

### Redis 쿠폰 개수
![스크린샷 2024-12-20 173333](https://github.com/user-attachments/assets/6405e26a-5ccd-4ba4-a15d-24b06668487f)

### MySQL Coupon_Assign_Log 테이블 쿠폰 발급 내역
![스크린샷 2024-12-20 173246](https://github.com/user-attachments/assets/04265a7d-e0f0-407d-a925-d9422a84a84a)

### Prometheus 쿠폰 발급 개수 매트릭
![스크린샷 2024-12-20 173229](https://github.com/user-attachments/assets/cd872c3d-b216-4228-87dd-4060d10b90b7)


## 12. 시스템 포트 설정

### **UI 및 애플리케이션 관련 포트**

| **서비스 이름** | **설명** | **주소** |
| --- | --- | --- |
| Spring Swagger | Spring Swagger API 문서 인터페이스 | http://localhost:8080/ui |
| Spring Actuator | Spring Boot Actuator 엔드포인트 | http://localhost:8080/actuator |
| Kafka UI | Kafka 클러스터 관리 인터페이스 | [http://localhost:8989](http://localhost:8989/) |
| Prometheus | 메트릭 수집 및 시각화 | [http://localhost:9090](http://localhost:9090/) |
| Grafana | 메트릭 대시보드 및 모니터링 툴 | [http://localhost:3000](http://localhost:3000/) |
| Pinpoint Web | Pinpoint APM 관리 웹 인터페이스 | [http://localhost:8082](http://localhost:8082/) |


### **애플리케이션 관련 포트**

| **서비스 이름** | **설명** | **주소** |
| --- | --- | --- |
| Prometheus Endpoint | Spring Prometheus 메트릭 엔드포인트 | http://localhost:8080/actuator/prometheus |


### **모니터링 관련 포트**

| **서비스 이름** | **설명** | **주소** |
| --- | --- | --- |
| Redis Exporter | Prometheus와 연동된 Redis 메트릭 | [http://localhost:9121](http://localhost:9121/) |
| MySQL Exporter | Prometheus와 연동된 MySQL 메트릭 | [http://localhost:9104](http://localhost:9104/) |
| Kafka Exporter | Prometheus와 연동된 Kafka 메트릭 | [http://localhost:9308](http://localhost:9308/) |


### **Pinpoint 관련 포트**

| **서비스 이름** | **설명** | **주소** |
| --- | --- | --- |
| Pinpoint Web | Pinpoint APM 관리 웹 인터페이스 | [http://localhost:8082](http://localhost:8082/) |
| Pinpoint Collector | Pinpoint 데이터 수집기 | [http://localhost:9991](http://localhost:9991/) |
| Pinpoint MySQL | Pinpoint 데이터 저장소 | [http://localhost:3308](http://localhost:3308/) |
| Pinpoint HBase | Pinpoint HBase 데이터베이스 서비스 | [http://localhost:16010](http://localhost:16010/) |


### **Kafka 및 Zookeeper 관련 포트**

| **서비스 이름** | **설명** | **주소** |
| --- | --- | --- |
| Kafka Broker 1 | Kafka 첫 번째 브로커 | localhost:9092 |
| Kafka Broker 2 | Kafka 두 번째 브로커 | localhost:9093 |
| Kafka Broker 3 | Kafka 세 번째 브로커 | localhost:9094 |
| Zookeeper | Kafka Zookeeper 서비스 | localhost:32181 |

---

### **데이터베이스 관련 포트**

| **서비스 이름** | **설명** | **주소** |
| --- | --- | --- |
| MySQL Container | MySQL 데이터베이스 서비스 | localhost:3306 |
| MySQL Test Container | MySQL 테스트 데이터베이스 서비스 | localhost:3307 |
| Redis Container | Redis 데이터베이스 서비스 | localhost:6379 |


## 13. 프로젝트 관련 GitHub 리포지토리

| **No** | **Repository Name** | **Description** | **URL** |
| --- | --- | --- | --- |
| 1 | CouponSystem | 선착순 쿠폰 시스템의 메인 애플리케이션 | https://github.com/kwongio/CouponSystem |
| 2 | consumer | Kafka Consumer 관련 서비스 및 처리 로직 | https://github.com/kwongio/consumer |


## **14. 부족한 점**

1. **로그 관리 시스템 부재**
    - **MDC**를 사용하여 트레이스 ID를 설정했지만, **ELK 스택**(Elasticsearch, Logstash, Kibana)이 없어서 **로그 검색**과 **시각화**가 이루어지지 않았습니다. 향후 ELK를 도입하여 **효율적인 로그 관리**와 **빠른 오류 추적**을 개선할 필요가 있습니다.
2. **로컬 테스트 환경에서의 한계**
    - **비용** 문제로 인해 **로컬 환경**에서 테스트를 진행했으나, **테스트 정확도**에 제한이 있었습니다. 로컬 환경에서 발생한 **부하 테스트 결과**는 실제 프로덕션 환경과 다를 수 있기 때문에, **클라우드 환경**에서의 테스트가 필요합니다.
3. **Redis와 MySQL의 Replication 미적용**
    - **Redis**와 **MySQL**에서 **Replication**을 적용하지 않아, 데이터의 **중복성**과 **가용성**에 한계가 있었습니다. 향후 **Redis 클러스터**와 **MySQL Replication**을 도입하여 **가용성**을 높이고, 데이터 처리 성능을 향상시킬 계획입니다.

## **15. 향후 계획**

- **새로운 프로젝트 개발**:
    - **주문 내역 시스템**을 개발하여, **일간 100만 건** 정도의 데이터가 처리되는 시스템을 목표로 설정.