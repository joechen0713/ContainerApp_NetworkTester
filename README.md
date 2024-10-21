# Network Tester

Network Tester is a Spring Boot application designed to help users test various network functionalities such as DNS lookups, HTTPS connections, TCP connections, and TCP dumps. This application provides a user-friendly interface to perform these tests and view the results.

## Features

- **DNS Lookup (dig)**: Perform DNS lookups and view the results.
- **HTTPS Connection Test**: Test HTTPS connections to specified URLs.
- **TCP Connection Test**: Test TCP connections to specified IP addresses and ports.
- **TCP Dump**: Run TCP dumps and download the resulting files.

## Getting Started

### Prerequisites

- Java 17
- Maven
- Docker (optional, for containerized deployment)

### Installation

1. **Clone the repository:**

    ```sh
    git clone https://github.com/yourusername/network-tester.git
    cd network-tester
    ```

2. **Build the project:**

    ```sh
    ./mvnw clean package
    ```

3. **Run the application:**

    ```sh
    java -jar target/networktester-1.0.0.jar
    ```

4. **Access the application:**

    Open your web browser and navigate to `http://localhost:8080`.

### Docker Deployment

1. **Build the Docker image:**

    ```sh
    docker build -t network-tester .
    ```

2. **Run the Docker container:**

    ```sh
    docker run -p 8080:8080 network-tester
    ```

## Usage

### Home Page

The home page provides links to various network tests:

- **Test HTTPS Connection (Layer 7)**
- **Test TCP Connection (Layer 4)**
- **Run TCP Dump**
- **Download TCP Dump Files**
- **Test dig**

### DNS Lookup (dig)

Navigate to `/test-dig` to perform a DNS lookup. Enter the Fully Qualified Domain Name (FQDN) and view the results.

### HTTPS Connection Test

Navigate to `/test-https-connection` to test HTTPS connections. Enter the URL and view the connection status.

### TCP Connection Test

Navigate to `/test-tcp-connection` to test TCP connections. Enter the IP address and port number to check the connection.

### TCP Dump

Navigate to `/test-tcpdump` to run a TCP dump. The results can be downloaded from the `/download-tcpdump` page.

## Project Structure

- **src/main/java/com/containerapp/networktester**: Contains the main application and service classes.
- **src/main/resources/templates**: Contains Thymeleaf templates for the web interface.
- **src/main/resources/static/css**: Contains CSS styles for the web interface.
- **Dockerfile**: Docker configuration for containerized deployment.
- **pom.xml**: Maven configuration file.

## Contributing

Contributions are welcome! Please open an issue or submit a pull request for any improvements or bug fixes.

## License

This project is licensed under the Apache License 2.0. See the [LICENSE](LICENSE) file for details.
