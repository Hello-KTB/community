# 빌드 스테이지
FROM eclipse-temurin:21-alpine AS build
RUN apk add --no-cache bash
WORKDIR /app
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./
RUN chmod +x ./gradlew && ./gradlew dependencies --no-daemon
COPY src src
RUN ./gradlew build --no-daemon -x test

# 런타임 스테이지
FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app
RUN addgroup -g 1000 worker && adduser -u 1000 -G worker -s /bin/sh -D worker
COPY --from=build --chown=worker:worker /app/build/libs/*.jar ./main.jar
USER worker:worker
ENV PROFILE=${PROFILE}
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILE}", "-jar", "main.jar"]