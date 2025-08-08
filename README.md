# Event Management System

**Version 2.0 - OOP Inheritance Authentication**

## Project Description

This project is a **comprehensive Event Management System** developed in Java with a focus on **Object-Oriented Programming inheritance** and **role-based authentication**. The system serves as a secure platform for administrators to manage events and users to register for them. 

**Key Features:**
- **OOP Inheritance Authentication** - All services inherit authentication capabilities from a base `AuthService` class
- **Role-based Access Control** - Admin and User roles with different privileges
- **Token-based Event Creation** - Secure token system for event management
- **Password Management** - Secure hashing, password changes, and reset functionality
- **Session Management** - Comprehensive login/logout system
- **Clean Build System** - Separate compilation with automated build scripts

This system demonstrates advanced OOP concepts and secure authentication patterns.

---

## System Architecture

### OOP Inheritance Pattern
The system uses **pure inheritance** for authentication, eliminating separate authentication service classes:

- **`AuthService`** - Abstract base class providing authentication capabilities
- **`AdminService`** - Extends AuthService for admin operations (token generation)
- **`EventService`** - Extends AuthService for event management
- **`RegistrationService`** - Extends AuthService for user registrations
- **`UserService`** - Provides authentication without inheritance (prevents circular dependency)

### Authentication Features
- **Secure Password Hashing** - SHA-256 with salt
- **Session Management** - Login/logout with session tracking
- **Role-based Authorization** - Admin vs User privileges
- **Token-based Operations** - Secure event creation tokens
- **Password Recovery** - Reset tokens and secure password changes

---

## Development Workflow

This project follows a collaborative Git workflow to ensure code quality and team synchronization.

1.  **Main Branch:** The `main` branch is the stable, definitive version of the project. Direct pushes to `main` are not allowed.
2.  **Development Branch:** A `dev` branch is used as the primary integration branch for new features.
3.  **Feature Branches:** All new work (features, bug fixes) must be done on a separate feature branch (e.g., `feature/user-login`, `fix/registration-bug`).
4.  **Pull Requests (PRs):** When a feature is complete, the developer opens a Pull Request to merge their feature branch into the `dev` branch.
5.  **Code Review:** At least one other team member must review the Pull Request to check for bugs, style, and correctness before it can be merged. This maintains high code quality.

---

## Project Structure

The project follows a clean architecture with separate source and build directories.

```
Event-ms/
├── .gitignore
├── LICENSE
├── README.md
├── build.ps1           <-- PowerShell build script
├── run.ps1             <-- PowerShell run script
├── build/              <-- Compiled classes (auto-generated)
│   └── classes/
│       └── com/eventms/
│           ├── Main.class
│           ├── model/      <-- All model .class files
│           └── service/    <-- All service .class files
└── src/                <-- Source code
    ├── database/
    │   └── eventms_db.sql
    └── main/java/
        └── com/eventms/
            ├── Main.java
            ├── model/
            │   ├── Admin.java
            │   ├── Event.java
            │   ├── EventStatus.java
            │   ├── EventToken.java
            │   ├── Registration.java
            │   ├── RegistrationStatus.java
            │   ├── Role.java
            │   └── User.java
            └── service/
                ├── AuthService.java      <-- Base authentication class
                ├── AdminService.java     <-- Extends AuthService
                ├── EventService.java     <-- Extends AuthService
                ├── RegistrationService.java <-- Extends AuthService
                └── UserService.java
```

---

## How to Build and Run

### Prerequisites

* Java Development Kit (JDK) 11 or newer
* PowerShell (for Windows build scripts)
* An IDE like IntelliJ, Eclipse, or VS Code is recommended

### Quick Start (Recommended)

#### Using PowerShell Scripts

1. **Run the application** (auto-builds if needed):
   ```powershell
   .\run.ps1
   ```

2. **Build only**:
   ```powershell
   .\build.ps1
   ```

#### Using Command Line

1. **Build the application**:
   ```bash
   # Create build directory
   mkdir -p build/classes
   
   # Compile all classes
   javac -d "build/classes" -cp "src/main/java" src/main/java/com/eventms/model/*.java
   javac -d "build/classes" -cp "build/classes;src/main/java" src/main/java/com/eventms/service/*.java
   javac -d "build/classes" -cp "build/classes" src/main/java/com/eventms/Main.java
   ```

2. **Run the application**:
   ```bash
   java -cp "build/classes" com.eventms.Main
   ```

### Running from an IDE

1. Open the project folder in your IDE
2. Locate `src/main/java/com/eventms/Main.java`
3. Click the **Run** button - the IDE handles compilation automatically

---

## Core Features Implemented

### Authentication & Security
- **OOP Inheritance Authentication** - All services extend `AuthService` base class
- **Secure Password Hashing** - SHA-256 with salt for password storage
- **Session Management** - Comprehensive login/logout system with session tracking
- **Role-based Authorization** - Admin vs User privileges with proper access control
- **Password Management** - Change passwords and reset with secure token system

### User Management
- **User Creation & Verification** - Create users with email verification
- **Role Promotion** - Promote users to admin status
- **Profile Management** - Update user information and preferences

### Event Management
- **Token-based Event Creation** - Admins generate secure tokens for event creation
- **Event Lifecycle** - Create, update, cancel, and manage events
- **Event Discovery** - Users can browse and search for events
- **Capacity Management** - Automatic registration limits and waitlists

### Registration System
- **Authenticated Registration** - Users must be logged in to register
- **Registration Status Tracking** - Confirmed, pending, cancelled states
- **Event Capacity Enforcement** - Automatic capacity limit management

### System Features
- **Clean Build System** - Automated PowerShell build and run scripts
- **Comprehensive Testing** - Full authentication flow demonstration in Main.java
- **Error Handling** - Proper exception handling with user-friendly messages
- **Separation of Concerns** - Clean architecture separating models, services, and main logic

### Demo Capabilities
The system includes a comprehensive demo that showcases:
- ✅ User creation and verification
- ✅ Secure login with password hashing
- ✅ Role-based user promotion (USER → ADMIN)
- ✅ Admin-only token generation
- ✅ Token-based event creation
- ✅ Authenticated event registration
- ✅ Password change and reset functionality
- ✅ Token reusability for multiple events
- ✅ Secure logout and session management
- ✅ Authorization checks for all operations

---

## OOP Concepts Demonstrated

### Inheritance
- **Base Class**: `AuthService` provides common authentication functionality
- **Derived Classes**: `AdminService`, `EventService`, `RegistrationService` extend `AuthService`
- **Method Inheritance**: All services inherit `requireAuthentication()`, `requireAdmin()`, `isAuthenticated()`

### Encapsulation
- **Protected Methods**: Authentication methods are protected, allowing inheritance but preventing external access
- **Private Fields**: User credentials and session data are encapsulated within service classes
- **Static References**: Shared `UserService` reference through static field in base class

### Polymorphism
- **Method Overriding**: Services can override base authentication behavior if needed
- **Interface Implementation**: Models implement interfaces like `Displayable` for consistent behavior

### Abstraction
- **Abstract Base Class**: `AuthService` cannot be instantiated directly
- **Service Layer**: Business logic abstracted from data models
- **Clean Interfaces**: Services provide simple method interfaces hiding complex authentication logic

---

## Team Members

* **[Sun Heng]** - Project Lead & System Architect
* **[Veat Bunchhour]** - Team Coordinator
* **[Cheav Vichar]** - Member
* **[Kheang Rachana]** - Member
* **[Mean Sambathpoty]** - Member

---

## Development Workflow

This project follows a collaborative Git workflow to ensure code quality and team synchronization.

1. **Main Branch:** The `main` branch is the stable, production-ready version
2. **Development Branch:** The `dev` branch is used for integration and testing
3. **Feature Branches:** All new work done on separate feature branches
4. **Pull Requests:** Code review required before merging to `dev` or `main`
5. **Clean Builds:** Use automated build scripts to ensure consistent compilation

### Code Standards
- Use `//` comments instead of `/* */` 
- Follow OOP inheritance patterns for authentication
- Maintain clean separation between models and services
- Include comprehensive error handling

---

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## Future Enhancements

- **Database Integration** - Connect to actual database instead of in-memory storage
- **Web Interface** - Add REST API and web frontend
- **Email Notifications** - Send confirmation emails for registrations
- **Advanced Search** - Filter events by category, date, location
- **Payment Integration** - Handle paid events and transactions
- **Mobile App** - Cross-platform mobile application

---

*Event Management System v2.0 - Demonstrating advanced OOP concepts with secure authentication*