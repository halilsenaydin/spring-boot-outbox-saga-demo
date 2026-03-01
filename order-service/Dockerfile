FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app

# Copy only necessary files
COPY order-service/ order-service/
COPY shared/ shared/
COPY order-service/build.pom.xml pom.xml

RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -pl order-service -am dependency:go-offline

COPY shared/src shared/src
COPY order-service/src order-service/src

RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -pl order-service -am -DskipTests package

# Runtime
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

COPY --from=builder /app/order-service/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
