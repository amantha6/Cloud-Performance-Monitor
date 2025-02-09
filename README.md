
# Cloud Performance Monitor

A real-time performance monitoring system for cloud services built with Spring Boot, Prometheus, and Grafana.

## Features

- Real-time monitoring dashboard with sub-second refresh rates
- Custom metric collectors for:
  - CPU utilization
  - Memory usage
  - Network latency
  - Network throughput
- Historical performance data analysis with trend visualization
- Automated anomaly detection
- Customizable alerting system
- RESTful API for metrics collection and analysis

## Architecture

```
                   ┌──────────────┐
                   │   Metrics    │
                   │  Collectors  │
                   └──────┬───────┘
                         │
                         ▼
┌──────────────┐  ┌──────────────┐  ┌──────────────┐
│    Spring    │  │  Prometheus  │  │   Grafana    │
│     Boot     │◄─┤    Server    │◄─┤  Dashboard   │
│     API      │  │             │  │              │
└──────────────┘  └──────────────┘  └──────────────┘
```

## Prerequisites

- Java 17 or higher
- Docker and Docker Compose
- Maven 3.6+
- Git

## Quick Start

1. Clone the repository:
```bash
git clone https://github.com/your-username/cloud-performance-monitor.git
cd cloud-performance-monitor
```

2. Build the application:
```bash
mvn clean package
```

3. Start the services using Docker Compose:
```bash
docker-compose up -d
```

4. Access the services:
- Spring Boot API: http://localhost:8080
- Prometheus: http://localhost:9090
- Grafana: http://localhost:3000

## Project Structure

```
cloud-performance-monitor/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/monitor/
│   │   │       ├── config/
│   │   │       ├── controller/
│   │   │       ├── service/
│   │   │       ├── model/
│   │   │       └── collector/
│   │   └── resources/
│   └── test/
├── docker/
├── grafana/
│   └── dashboards/
├── prometheus/
│   └── prometheus.yml
├── docker-compose.yml
└── pom.xml
```

## Configuration

### Application Properties

Configure the application in `src/main/resources/application.yml`:

```yaml
server:
  port: 8080

management:
  endpoints:
    web:
      exposure:
        include: prometheus,health,info,metrics
```

### Prometheus Configuration

Configure Prometheus in `prometheus/prometheus.yml`:

```yaml
scrape_configs:
  - job_name: 'spring-boot-app'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['app:8080']
```

## API Endpoints

### Metrics Collection

- `GET /api/v1/metrics` - Get all current metrics
- `GET /api/v1/metrics/{type}` - Get specific metric type
- `POST /api/v1/metrics/threshold` - Configure metric thresholds

### System Health

- `GET /actuator/health` - System health information
- `GET /actuator/prometheus` - Prometheus metrics endpoint

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
