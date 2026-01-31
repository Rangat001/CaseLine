# 🗂️ CaseLine - Case Management System

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

> A comprehensive case management system for organizations to efficiently track, manage, and collaborate on cases with role-based access control and real-time updates.

## 📋 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Getting Started](#-getting-started)
- [API Documentation](#-api-documentation)
- [Database Schema](#-database-schema)
- [Authentication & Authorization](#-authentication--authorization)
- [Project Structure](#-project-structure)
- [Configuration](#-configuration)
- [Usage](#-usage)
- [Contributing](#-contributing)
- [License](#-license)
- [Support](#-support)

## ✨ Features

### 🔐 Multi-Level Authentication
- **Organization Owner Login** - Full system access and organization management
- **Admin Login** - Case creation and team management
- **Employee Login** - Case participation and updates
- JWT-based secure authentication

### 👥 Role-Based Access Control
- **Owner** - Organization-level management
- **Admin** - Create cases, assign members, manage teams
- **Editor** - Edit case details and posts
- **Reporter** - View and report on cases

### 📊 Case Management
- Create and manage cases (Criminal, Political, Legal, Event, Accident)
- Case status tracking (Ongoing, Closed, Archived)
- Team member assignment with role-based permissions
- Case-specific posts and updates
- Real-time collaboration

### 📈 Dashboard & Analytics
- **Admin Dashboard**
    - Total cases, posts, and team members
    - Active/Closed case ratios
    - Organization overview
- **Employee Dashboard**
    - Assigned cases overview
    - Personal statistics
    - Task tracking

### 💬 Post & Update System
- Create, edit, and delete posts within cases
- Permission-based post editing (Owner or Editor role)
- Author tracking for accountability
- Timeline of case activities

## 🛠️ Tech Stack

### Backend
- **Framework**: Spring Boot 3.5.3
- **Language**: Java 21
- **Security**: Spring Security + JWT (JSON Web Tokens)
- **Database**: MySQL 8.0.33
- **ORM**: Spring Data JPA / Hibernate
- **API Documentation**: Swagger/OpenAPI 3.0

### Frontend
- **Template Engine**: Thymeleaf
- **Markup**: HTML5

### Additional Libraries
- **Lombok** - Reduce boilerplate code
- **BCrypt** - Password encryption
- **DevTools** - Hot reload during development

## 🚀 Getting Started

### Prerequisites

Ensure you have the following installed:
- Java 21 or higher
- Maven 3.6+
- MySQL 8.0+
- Git

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/Rangat001/CaseLine.git
   cd CaseLine
   ```

2. **Configure Database**

   Create a MySQL database:
   ```sql
   CREATE DATABASE caseline_db;
   ```

3. **Update Application Properties**

   Create `src/main/resources/application.properties`:
   ```properties
   # Database Configuration
   spring.datasource.url=jdbc:mysql://localhost:3306/caseline_db
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

   # JPA/Hibernate Configuration
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
   spring.jpa.properties.hibernate.format_sql=true

   # JWT Configuration
   jwt.secret=your_secret_key_here_minimum_256_bits
   jwt.expiration=86400000

   # Server Configuration
   server.port=8080

   # Thymeleaf Configuration
   spring.thymeleaf.cache=false
   spring.thymeleaf.enabled=true
   spring.thymeleaf.prefix=classpath:/templates/
   spring.thymeleaf.suffix=.html
   ```

4. **Build the Project**
   ```bash
   mvn clean install
   ```

5. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

### Quick Start

1. **Register an Organization**
    - Navigate to `/CaseLine/org-signup`
    - Fill in organization details
    - Owner account will be created automatically

2. **Login as Owner**
    - Go to `/CaseLine/org-login`
    - Use owner credentials

3. **Create Admin/Employee Users**
    - Use `/auth/signup` endpoint
    - Assign appropriate roles

4. **Start Managing Cases**
    - Create cases via Admin dashboard
    - Assign team members
    - Track progress

## 📚 API Documentation

### Access Swagger UI
Once the application is running, access the interactive API documentation:
```
http://localhost:8080/swagger-ui/index.html
```

### API Endpoints Overview

#### Authentication (`/auth`)
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/auth/signup` | Create new user/employee | Yes (Admin/Owner) |
| POST | `/auth/login` | Employee login | No |
| POST | `/auth/org_signup` | Register organization | No |
| POST | `/auth/org_login` | Organization owner login | No |

#### Admin Operations (`/Admin`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/Admin/create_case` | Create new case |
| GET | `/Admin/cases` | Get all admin cases |
| GET | `/Admin/data` | Get dashboard statistics |
| POST | `/Admin/add_member` | Add member to case |
| GET | `/Admin/Case/{id}/members` | Get case members |
| PUT | `/Admin/Case/{caseId}/member/{userId}/role` | Update member role |
| PUT | `/Admin/Case/{id}` | Update case details |
| DELETE | `/Admin/Case/{caseId}/member/{userId}` | Remove member from case |

#### Case Operations (`/Case`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/Case/{id}` | Get case details |
| GET | `/Case/{caseId}/check-membership` | Check case membership |
| POST | `/Case/post/{caseId}` | Create post in case |
| PUT | `/Case/post/{Case_id}` | Update post |
| DELETE | `/Case/post/{caseId}/{postId}` | Delete post |
| GET | `/Case/editablility/{caseId}/{postId}` | Check edit permission |
| GET | `/Case/deletability/{caseId}/{postId}` | Check delete permission |
| GET | `/Case/{caseId}/posts` | Get all case posts |

#### Employee Operations (`/emp`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/emp/dashboard` | Get employee dashboard |
| GET | `/emp/cases` | Get employee cases |

#### User Operations (`/User`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/User/checkrole/{CaseId}` | Check user role in case |
| GET | `/User/checkrole` | Check user role in organization |

## 🗄️ Database Schema

### Main Entities

#### Organization
```sql
- org_id (PK, Auto Increment)
- owner_name
- org_name
- email (Unique)
- org_type (LAW_FIRM, POLICE_DEPT, NEWS_AGENCY, etc.)
- description
- created_at
```

#### User
```sql
- user_id (PK, Auto Increment)
- org_id (FK)
- name
- email (Unique)
- password (Encrypted)
- role (owner, admin, editor, reporter)
- isActive
- created_at
```

#### Case
```sql
- case_id (PK, Auto Increment)
- org_id (FK)
- title
- case_type (criminal, political, legal, event, accident)
- status (ongoing, closed, archived)
- description
- created_by (FK to User)
- created_at
- updated_at
```

#### Case_Group (Team Assignment)
```sql
- case_id (PK, FK)
- user_id (PK, FK)
- group_id
- role (reporter, editor, admin)
```

#### Post
```sql
- post_id (PK, Auto Increment)
- case_id (FK)
- posted_by (FK to User)
- content
- created_at
- updated_at
```

## 🔒 Authentication & Authorization

### JWT Token Flow
1. Login → Receive JWT token
2. Add token to requests → `Authorization: Bearer <token>`
3. Token validation → Access granted based on role

### Role Hierarchy
```
Owner (Highest)
  ↓
Admin
  ↓
Editor
  ↓
Reporter (Lowest)
```

### Permission Matrix

| Action | Owner | Admin | Editor | Reporter |
|--------|-------|-------|--------|----------|
| Create Organization | ✅ | ❌ | ❌ | ❌ |
| Create User | ✅ | ✅ | ❌ | ❌ |
| Create Case | ✅ | ✅ | ❌ | ❌ |
| Assign Members | ✅ | ✅ | ❌ | ❌ |
| Edit Case | ✅ | ✅ | ✅ | ❌ |
| Create Post | ✅ | ✅ | ✅ | ✅ |
| Edit Own Post | ✅ | ✅ | ✅ | ✅ |
| Edit Any Post | ✅ | ✅ | ✅ | ❌ |
| Delete Own Post | ✅ | ✅ | ✅ | ✅ |
| View Cases | ✅ | ✅ | ✅ | ✅ |

## 📁 Project Structure

```
CaseLine/
├── src/
│   ├── main/
│   │   ├── java/com/example/rgt/CaseLine/
│   │   │   ├── config/              # Configuration classes
│   │   │   │   ├── OpenApiConfig.java
│   │   │   │   └── SecurityConfig.java
│   │   │   ├── Controller/          # REST Controllers
│   │   │   │   ├── AdminController.java
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── CaseController.java
│   │   │   │   ├── empController.java
│   │   │   │   ├── UserController.java
│   │   │   │   └── WebController.java
│   │   │   ├── DTO/                 # Data Transfer Objects
│   │   │   │   ├── AdminDashboardDTO.java
│   │   │   │   ├── Add_memberDTO.java
│   │   │   │   ├── Case_Member_DTO.java
│   │   │   │   ├── OrgLoginDTO.java
│   │   │   │   └── Post_DTO.java
│   │   │   ├── entity/              # JPA Entities
│   │   │   │   ├── Case.java
│   │   │   │   ├── Case_Group.java
│   │   │   │   ├── Organization.java
│   │   │   │   ├── Post.java
│   │   │   │   └── User.java
│   │   │   ├── Repository/          # JPA Repositories
│   │   │   │   ├── CaseRepository.java
│   │   │   │   ├── Case_GrupRepository.java
│   │   │   │   ├── OrgRepository.java
│   │   │   │   ├── PostRepository.java
│   │   │   │   └── UserRepository.java
│   │   │   ├── Service/             # Business Logic
│   │   │   │   ├── AdminService.java
│   │   │   │   ├── CaseService.java
│   │   │   │   ├── empService.java
│   │   │   │   ├── OrgService.java
│   │   │   │   └── UserService.java
│   │   │   ├── util/                # Utility classes
│   │   │   │   └── JWTutil.java
│   │   │   └── CaseLineApplication.java
│   │   └── resources/
│   │       ├── templates/           # Thymeleaf templates
│   │       └── application.properties
│   └── test/                        # Test files
├── pom.xml
└── README.md
```

## ⚙️ Configuration

### Security Configuration
The application uses Spring Security with JWT authentication. Key security features:
- Password encryption using BCrypt
- JWT token generation and validation
- Role-based access control
- CORS configuration

### Database Configuration
Hibernate is configured to auto-update the schema. For production:
```properties
spring.jpa.hibernate.ddl-auto=validate
```

## 📖 Usage

### Example: Create a Case via API
```bash
curl -X POST http://localhost:8080/Admin/create_case \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Investigation Case 2024",
    "caseType": "criminal",
    "status": "ongoing",
    "description": "Detailed case description"
  }'
```

### Example: Add Member to Case
```bash
curl -X POST http://localhost:8080/Admin/add_member \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "case_id": 1,
    "email": "member@example.com",
    "role": "editor"
  }'
```

### Example: Create Post in Case
```bash
curl -X POST http://localhost:8080/Case/post/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "content": "Investigation update: New evidence found"
  }'
```

## 🔍 Key Features Explained

### Case Workflow
1. Admin creates case → Case created with "ongoing" status
2. Admin assigns team → Members added with specific roles
3. Team collaborates → Posts/updates added to case
4. Case progresses → Status updated (ongoing → closed → archived)

### Dashboard Analytics
- Real-time case statistics
- Active vs. closed case ratios
- Team member counts
- Post activity tracking

### Permission System
- Automatic permission checks on all endpoints
- Role-based access at method level
- Custom permission validators for post editing/deletion

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Developer

**Rangat001**
- GitHub: [@Rangat001](https://github.com/Rangat001)
- Repository: [CaseLine](https://github.com/Rangat001/CaseLine)

## 📧 Support

For support and questions:
- **GitHub Issues**: [Create an issue](https://github.com/Rangat001/CaseLine/issues)

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- Spring Security for robust authentication
- MySQL for reliable data persistence
- Swagger/OpenAPI for API documentation

---

<div align="center">
  <p>Built with ❤️ using Spring Boot</p>
  <p>⭐ Star this repository if you find it helpful!</p>
</div>