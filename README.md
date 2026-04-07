# MailForge — AuthService

Authentication and authorization microservice for the MailForge platform. Handles user registration, OTP-based email verification, JWT login, device fingerprinting, and two-factor authentication (2FA) via device trust.

## Tech Stack

| Layer | Technology |
|---|---|
| Framework | Spring Boot 4.0.2 |
| Language | Java 17 |
| Database | MySQL (Aiven Cloud) |
| Caching | Redis (OTP storage) |
| Messaging | Apache Kafka |
| Auth | Spring Security + JWT (JJWT 0.11.5) |
| Real-time | WebSocket (for 2FA device approval) |
| Build | Maven |
| Other | Lombok, ModelMapper, UA-Parser (device detection) |

## Project Structure

```
AuthService/
├── config/
│   ├── Beans.java                   # ModelMapper, PasswordEncoder beans
│   ├── RedisConfig.java             # Redis template for OTP storage
│   └── device/
│       └── DeviceInfoExtractor.java # Extracts browser, OS, IP, device type from User-Agent
├── controller/
│   ├── UserRegistrationController.java   # POST /create, PATCH /verify, GET /resend-otp
│   └── publicController/
│       └── LoginController.java          # POST /login (WIP)
├── dro/
│   ├── Otp.java                     # OTP data object (cached in Redis)
│   ├── request/
│   │   ├── UserDto.java             # Registration payload (validated)
│   │   ├── LoginDto.java            # Login payload
│   │   ├── DeviceDto.java           # Device registration payload
│   │   └── VerificationDto.java     # OTP verification payload
│   └── response/
│       ├── ApiResponseDto.java      # Generic API response wrapper
│       ├── LoginResponseDto.java    # Login response (JWT or WAITING_APPROVAL)
│       └── user/
│           └── UserResponseDto.java
├── entity/
│   ├── User.java                    # User entity (JPA)
│   ├── LoginAttempts.java           # Login attempt tracking (for 2FA approval)
│   └── tfa/
│       └── Device.java              # Device entity (JPA)
├── enums/
│   ├── AccountStatus.java           # ACTIVE, PENDING_VERIFICATION, LOCKED, etc.
│   ├── Roles.java                   # ROLE_USER, ROLE_ADMIN
│   └── Status.java                  # Login attempt status: PENDING, APPROVED, DENIED, etc.
├── error/
│   ├── ErrorHandler.java            # Global exception handler
│   └── errors/                      # Custom exceptions
├── repo/
│   ├── UserRepo.java
│   ├── DeviceRepo.java
│   └── LoginAttemptsRepo.java
├── security/
│   ├── SecurityConfig.java          # Security filter chain, stateless sessions
│   ├── filter/
│   │   └── JwtAuthFilter.java       # JWT authentication filter
│   └── jwt/
│       └── JwtUtil.java             # JWT generation with HMAC-SHA256
│   └── user/
│       ├── CustomUserDetailService.java
│       └── CustomUserDetails.java
└── service/
    ├── UserService.java             # Login logic + device trust evaluation
    ├── login/
    │   └── DeviceRegistration.java  # Device registration service
    └── registration/
        ├── UserRegistration.java    # User signup + OTP generation
        └── otp/
            └── OtpService.java      # OTP generate, store (Redis), validate
```

## API Endpoints

### Registration — `/app/v1/user/`

| Method | Path | Description |
|---|---|---|
| `POST` | `/create` | Register a new user. Sends OTP for verification. |
| `PATCH` | `/verify` | Verify user account with OTP. |
| `GET` | `/resend-otp?username=` | Resend OTP to user. |

### Login — `/app/v1/public/`

| Method | Path | Description |
|---|---|---|
| `POST` | `/login` | Authenticate and get JWT. Triggers 2FA if needed. |

## What's Been Built

### ✅ User Registration & Verification
- User signup with validated input (password strength rules enforced)
- OTP generated via `SecureRandom`, stored in Redis with 5-min TTL
- Account starts as `PENDING_VERIFICATION` → becomes `ACTIVE` after OTP verify
- Duplicate username check, resend OTP support

### ✅ Authentication
- Stateless JWT auth with Spring Security
- `CustomUserDetailService` loads users from MySQL
- `JwtUtil` generates HMAC-SHA256 signed tokens with user ID, roles, and configurable expiry
- `JwtAuthFilter` for protected routes

### ✅ Device Fingerprinting
- `DeviceInfoExtractor` parses `User-Agent` header via UA-Parser library
- Extracts: browser name, OS, device type (MOBILE/TABLET/DESKTOP), IP address
- Device ID stored as a cookie (`device_id`) on the client

### ✅ Device Registration
- Devices are persisted per user (One-to-Many)
- First device on login auto-registered as **primary device**
- Stores: deviceId, deviceName (e.g. "Chrome on Windows"), deviceType, ipAddress

### ✅ Login Attempt Tracking
- `LoginAttempts` entity records each login with device info, status, JWT token
- Statuses: `PENDING` → `APPROVED` / `DENIED` / `EXPIRED`
- Auto-expires after 2 minutes

### 🚧 Two-Factor Authentication (In Progress)
- `User.tfe` flag controls whether 2FA is enabled
- Login flow partially implemented — checks if devices list is empty (new user case)
- Device trust evaluation logic is designed but not yet fully coded
- WebSocket dependency added for real-time 2FA approval push (not yet wired)
- Planned: trusted devices, "remember this device" with 30-day rolling window, 2FA approval via primary device

## Prerequisites

- **Java 17**
- **Maven**
- **MySQL** — configured via `application.properties`
- **Redis** — running locally on `localhost:6379`

## Running Locally

```bash
# Clone the repo
git clone https://gitlab.com/mailforge1/AuthService.git
cd AuthService

# Build
mvn clean install

# Run
mvn spring-boot:run
```

The service starts on the default Spring Boot port. Make sure MySQL and Redis are accessible before starting.

## Environment Configuration

Key properties in `application.properties`:

| Property | Purpose |
|---|---|
| `spring.datasource.*` | MySQL connection (Aiven Cloud) |
| `spring.data.redis.*` | Redis connection for OTP cache |
| `jwt.key` | HMAC secret key for JWT signing |
| `spring.jpa.hibernate.ddl-auto` | Set to `update` (auto-migrate schema) |
