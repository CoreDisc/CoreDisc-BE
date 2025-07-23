# 베이스 이미지 선택 (Java 17 OpenJDK)
FROM openjdk:17-jdk
# 작업 디렉터리 지정
WORKDIR /app
ARG JAR_FILE=./build/libs/coredisc-0.0.1-SNAPSHOT.jar
# Gradle 빌드 결과물 JAR 파일 복사
COPY ${JAR_FILE} app.jar
# 컨테이너 실행 시 실행할 명령어
ENTRYPOINT [ "java", "-jar", "app.jar" ]