# 1단계: 빌드 (builder)
FROM gradle:8.5-jdk21-alpine AS builder

WORKDIR /workspace/app

# 종속성 캐싱
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
RUN gradle dependencies --no-daemon || return 0

# 전체 복사 및 빌드
COPY . .
RUN gradle bootJar --no-daemon

# 2단계: 런타임
FROM eclipse-temurin:21-jre-alpine

# (Optional) 한국 시간 설정
ENV TZ=Asia/Seoul

# 빌드된 JAR 복사
COPY --from=builder /workspace/app/build/libs/*.jar app.jar

# 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]
