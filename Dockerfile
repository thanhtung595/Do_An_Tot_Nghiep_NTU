# Stage 1: Build ứng dụng
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Image chạy ứng dụng
FROM eclipse-temurin:21-jdk
VOLUME /tmp
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Mặc định port ứng dụng Spring Boot
EXPOSE 8080

# Cấu hình để Java giảm bộ nhớ (có thể tối ưu thêm nếu muốn)
ENTRYPOINT ["java","-XX:+UseContainerSupport","-XX:MaxRAMPercentage=80.0","-jar","app.jar"]
