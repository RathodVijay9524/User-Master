# Spring Boot User Master Application

## 📋 Project Overview

The **User Master Application** is a comprehensive user management system built with Spring Boot that provides enterprise-level user administration capabilities. This application implements JWT-based authentication, role-based access control, and advanced user management features including soft delete, pagination, filtering, and asynchronous processing.

## 🚀 Key Features

### 🔐 Authentication & Security
- **JWT Token Authentication**: Stateless authentication using JSON Web Tokens
- **Role-Based Access Control (RBAC)**: Four distinct user roles with different permissions
- **Password Encryption**: Secure password storage using BCrypt hashing
- **Account Status Management**: Control user account activation and verification

### 👥 User Management
- **Complete CRUD Operations**: Create, Read, Update, Delete users
- **Soft Delete Pattern**: Mark users as deleted without losing data
- **Asynchronous Processing**: Non-blocking operations using CompletableFuture
- **Advanced Filtering**: Filter users by status, activity, and keywords
- **Pagination & Sorting**: Efficient data retrieval with customizable pagination
- **Account Status Control**: Activate/deactivate user accounts

### 🏗️ Architecture & Design Patterns
- **Layered Architecture**: Clean separation of concerns (Controller → Service → Repository → Entity)
- **DTO Pattern**: Data Transfer Objects for API communication
- **Auditing**: Automatic tracking of creation and modification timestamps
- **Exception Handling**: Centralized error handling and response formatting

## 🛠️ Technology Stack

- **Framework**: Spring Boot 3.x
- **Security**: Spring Security with JWT
- **Database**: JPA/Hibernate with MySQL/PostgreSQL
- **Build Tool**: Gradle
- **Logging**: Log4j2
- **Documentation**: Lombok for boilerplate code reduction

## 📊 Database Schema

### User Roles
- `ROLE_ADMIN` - Full system administration access
- `ROLE_SUPER_USER` - Extended user management capabilities
- `ROLE_NORMAL` - Standard user permissions
- `ROLE_WORKER` - Specialized worker-specific access

### Core Entities
- **User**: Main user entity with profile information and relationships
- **Role**: User permission levels and access control
- **AccountStatus**: Account verification and activation status
- **Worker**: Specialized user type for worker management

## 🌐 API Endpoints

### Authentication Endpoints
```
POST /api/auth/login          - User login (returns JWT token)
POST /api/auth/users          - User registration
```

### User Management Endpoints
```
POST   /api/users             - Create new user
GET    /api/users             - Get all users with pagination and filters
GET    /api/users/filter      - Advanced filtering with keyword search
GET    /api/users/active      - Get only active users
PUT    /api/users/{id}        - Update user information
DELETE /api/users/{id}        - Soft delete user
```

### Role Management Endpoints (Admin Only)
```
GET    /api/roles             - Get all roles
POST   /api/roles             - Create new role
GET    /api/roles/active      - Get all active roles
PATCH  /api/roles/{id}        - Update role details
PATCH  /api/roles/{id}/activate   - Activate role
PATCH  /api/roles/{id}/deactivate - Deactivate role
DELETE /api/roles/{id}        - Delete role

# User Role Assignment
POST   /api/roles/assign      - Assign roles to user
POST   /api/roles/remove      - Remove roles from user  
PUT    /api/roles/replace     - Replace all user roles
GET    /api/roles/user/{userId} - Get user's roles
GET    /api/roles/{id}/exists - Check if role exists
```

### Filtering Options
- **All users**: `/api/users/filter`
- **Active users**: `/api/users/filter?isDeleted=false&isActive=true`
- **Deleted users**: `/api/users/filter?isDeleted=true`
- **Expired users**: `/api/users/filter?isDeleted=false&isActive=false`

## 🔧 Configuration

### Application Properties
```properties
server.port=9091
spring.datasource.url=jdbc:mysql://localhost:3306/user_master
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

### JWT Configuration
- Token expiration: 7 days
- Algorithm: HS384
- Automatic token validation on protected endpoints

## 🚦 Getting Started

### Prerequisites
- Java 17 or higher
- MySQL/PostgreSQL database
- Gradle 7.x or higher

### Installation Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/RathodVijay9524/User-Master.git
   cd User-Master
   ```

2. **Configure database**
   - Update `application.properties` with your database credentials
   - Create database: `CREATE DATABASE user_master;`

3. **Build and run**
   ```bash
   ./gradlew build
   ./gradlew bootRun
   ```

4. **Access the application**
   - Base URL: `http://localhost:9091`
   - API Documentation: `http://localhost:9091/swagger-ui.html` (if configured)

## 📝 Usage Examples

### User Login
```bash
POST http://localhost:9091/api/auth/login
Content-Type: application/json

{
  "username": "rathod",
  "password": "rathod"
}
```

### Response Example
```json
{
    "responseStatus": "OK",
    "status": "success",
    "message": "success",
    "data": {
        "jwtToken": "eyJhbGciOiJIUzM4NCJ9...",
        "user": {
            "id": 4,
            "name": "Vijay Rathod",
            "username": "rathod",
            "email": "rathod@gmail.com",
            "roles": [
                {
                    "name": "ROLE_USER",
                    "id": 2,
                    "active": true,
                    "deleted": false
                }
            ],
            "active": true,
            "deleted": false
        }
    }
}
```

### Create User
```bash
POST http://localhost:9091/api/users
Authorization: Bearer {jwt_token}
Content-Type: application/json

{
  "name": "John Doe",
  "username": "johndoe",
  "email": "john@example.com",
  "password": "securePassword123"
}
```

### Get Users with Filters
```bash
GET http://localhost:9091/api/users/filter?isActive=true&pageNumber=0&pageSize=10&sortBy=name&sortDir=asc
Authorization: Bearer {jwt_token}
```

## 🏆 Advanced Features

### Asynchronous Processing
- Non-blocking user creation and updates
- Improved application performance and scalability
- CompletableFuture implementation for async operations

### Soft Delete Implementation
- Users are marked as deleted rather than physically removed
- Maintains data integrity and audit trails
- Ability to restore deleted users

### Comprehensive Filtering
- Filter by active/inactive status
- Filter by deleted/non-deleted users
- Keyword search across user fields
- Combinable filters for complex queries

### Pagination & Sorting
- Configurable page size and number
- Multi-field sorting capabilities
- Performance optimized for large datasets

## 🔍 Project Structure

```
src/main/java/com/vijay/User_Master/
├── controller/          # REST API controllers
├── service/            # Business logic layer
├── repository/         # Data access layer
├── entity/            # JPA entities
├── dto/               # Data Transfer Objects
├── config/            # Configuration classes
├── exceptions/        # Custom exception handling
└── Helper/            # Utility classes
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Vijay Rathod**
- GitHub: [@RathodVijay9524](https://github.com/RathodVijay9524)
- Email: rathod@gmail.com

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- Spring Security for robust authentication mechanisms
- All contributors who helped improve this project
