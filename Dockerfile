# Use an official Maven image to build the application
FROM maven:3.8.6-eclipse-temurin-17 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml and source code into the container
COPY pom.xml .
COPY src ./src

# Package the application
RUN mvn clean package -DskipTests

# Use an official Eclipse Temurin runtime as a parent image
FROM eclipse-temurin:17-jdk-alpine

# Install dnsutils for nslookup and tcpdump
RUN apk add --no-cache bind-tools tcpdump tcptraceroute

# Install tcpping
RUN wget -P /usr/bin http://www.vdberg.org/~richard/tcpping && \
    chmod 755 /usr/bin/tcpping
WORKDIR /app


# Create the dumps directory
RUN mkdir -p /app/dumps

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/networktester-1.0.0.jar app.jar

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]