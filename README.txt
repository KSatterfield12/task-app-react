HOUSEHOLD TASK LIST APPLICATION
=====================
A secure, role-based Task Management Web Application built with React and Spring Boot,
featuring JWT authentication, granular access control, and a clean service/repository architecture.
This application allows Admins and Users to create, update, complete, and delete tasks through a responsive web interface.


--FEATURES--
Core Functionality:
Task CRUD: Create, read, update, delete tasks.

Role-based Access Control:
ADMIN: Full task management.
USER: Limited access (task deletion denied).
User Management: Create and retrieve users with normalized usernames.

REST API: Secure endpoints consumed by the React frontend.

Security:
JWT Authentication:
JwtUtils generates, validates, and parses tokens.
JwtAuthenticationFilter validates tokens on incoming requests.
JwtCustomDSL injects the JWT filter into the Spring Security chain.

Custom Handlers:
JwtAccessDeniedHandler: Returns JSON for forbidden requests.
JwtAuthenticationEntryPoint: Returns JSON for unauthorized requests.

User Loading:
UserModelDetailsService loads users from the database with role mapping.
UserDetailsImpl wraps user data for Spring Security.


--ARCHITECTURE OVERVIEW--
Layer: Repository Classes TaskRepository, UserRepository
Responsibilities: Data access with custom queries (find by creator, incomplete tasks, case-insensitive title).

Layer: Service Classes TaskService, UserServiceImpl, AuthService
Responsibilities: Business logic, role checks, password encoding, authentication delegation.

Layer: Security Classes JwtUtils, JwtAuthenticationFilter, JwtCustomDSL, JwtAccessDeniedHandler, JwtAuthenticationEntryPoint, UserModelDetailsService, UserDetailsImpl
Responsibilities: Token management, authentication, authorization, and security context population.

Layer: Model Classes Task, User, UserRole
Responsibilities: Domain entities and enums.


--INSTALLATION & SETUP--
Clone the repository:
git clone https://github.com/yourusername/task-list-app.git
cd task-list-app

Configure application properties (application.properties):
jwt.base64-secret=REPLACE_ME
jwt.token-validity-in-seconds=3600
spring.datasource.url=jdbc:postgresql://localhost:5432/tasklistdb
spring.datasource.username=REPLACE_ME
spring.datasource.password=REPLACE_ME

Build & Run Backend:
./mvnw spring-boot:run

Run Frontend:
cd frontend
npm install
npm run dev

Frontend .env Configuration:
VITE_API_BASE_URL=http://localhost:8080/api


--SECURITY FLOW--
Login: AuthService authenticates credentials via AuthenticationManager.

Token Generation: JwtUtils.generateToken() issues a signed JWT.

Request Filtering: JwtAuthenticationFilter checks token validity for protected endpoints.

Access Control: TaskService enforces role-based restrictions.

Error Handling:
Unauthorized → JwtAuthenticationEntryPoint

Forbidden → JwtAccessDeniedHandler


--TESTING--
The project includes JUnit 5 tests with Mockito for:

Repositories: Query correctness.

Services: Role enforcement, CRUD logic, exception handling.

Security: Token validation, filter behavior, authentication flow.

Handlers: JSON error responses.

Run tests:
./mvnw test


--KEY CLASSES (LATEST UPDATES)--
TaskRepository – Custom finders for creator, incomplete tasks, and case-insensitive title.

JwtCustomDSL – Injects JwtAuthenticationFilter into the Spring Security chain.

JwtAccessDeniedHandler – JSON response for forbidden access.

JwtAuthenticationEntryPoint – JSON response for unauthorized access.

JwtAuthenticationFilter – Skips public paths, validates JWT, sets authentication.

JwtUtils – Generates, validates, and parses JWTs; retrieves current username.

UserModelDetailsService – Loads users with normalized usernames and mapped roles.

AuthService – Delegates authentication to AuthenticationManager.

TaskService – Enforces role-based task operations.

UserDetailsImpl – Wraps user data for Spring Security.

UserServiceImpl – Creates users with encoded passwords and normalized usernames.

--LICENSE--
This project is licensed under the MIT License.