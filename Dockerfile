# 빌드 스테이지
FROM eclipse-temurin:21-alpine AS build
WORKDIR /app
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./
COPY src src
RUN --mount=type=cache,target=/root/.gradle \
    chmod +x ./gradlew && ./gradlew bootJar --no-daemon -x test
RUN java -Djarmode=layertools -jar build/libs/*.jar extract --destination extracted

# 런타임 스테이지
FROM eclipse-temurin:21-jre-alpine AS runtime
RUN apk add --no-cache curl
WORKDIR /app
RUN addgroup -g 10001 worker && adduser -u 10001 -G worker -s /bin/sh -D worker
COPY --from=build --chown=worker:worker /app/extracted/dependencies/ ./
COPY --from=build --chown=worker:worker /app/extracted/spring-boot-loader/ ./
COPY --from=build --chown=worker:worker /app/extracted/snapshot-dependencies/ ./
COPY --from=build --chown=worker:worker /app/extracted/application/ ./
USER worker:worker
ENV JAVA_OPTS="-XX:MinRAMPercentage=70.0 -XX:MaxRAMPercentage=70.0 -Djava.security.egd=file:/dev/./urandom"
ENV PROFILE=${PROFILE}
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS org.springframework.boot.loader.launch.JarLauncher"]