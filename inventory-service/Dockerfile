FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app

# Copy only necessary files
COPY inventory-service/ inventory-service/
COPY shared/ shared/
COPY inventory-service/build.pom.xml pom.xml

RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -pl inventory-service -am dependency:go-offline

COPY shared/src shared/src
COPY inventory-service/src inventory-service/src

RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -pl inventory-service -am -DskipTests package

# Runtime
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

COPY --from=builder /app/inventory-service/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
