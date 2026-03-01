FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app

# Copy only necessary files
COPY product-service/ product-service/
COPY shared/ shared/
COPY product-service/build.pom.xml pom.xml

RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -pl product-service -am dependency:go-offline

COPY shared/src shared/src
COPY product-service/src product-service/src

RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -pl product-service -am -DskipTests package

# Runtime
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

COPY --from=builder /app/product-service/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
