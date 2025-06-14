# Event Management System

**Version 1.0**

## Project Description

This project is a role-based **Event Management System** developed in Java. It serves as a platform for organizers to create and manage events, and for guests to discover and register for them. The system is built with a focus on applying Object-Oriented Programming (OOP) principles and a clean, maintainable software architecture that separates data models from business logic.

This system is being developed as a class project for the 2024-2025 academic term.

---

## Team Members

* **[Sun Heng]** - Project Lead
* **[Veat Bunchhour]** - Team Coordinator
* **[Cheav Vichar]** - TBC
* **[Kheang Rachana]** - TBC
* **[Mean Sambathpoty]** - TBC

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

The project follows a standard Java package structure, separating source code from compiled binaries for a clean workspace.

```
EventManagerSystem/
├── .gitignore
├── LICENSE
├── README.md
├── bin/            <-- All compiled .class files go here
└── src/            <-- All source .java files
    └── com/
        └── eventms/
            ├── Main.java
            ├────── model/
            │       ├── User.java
            │       ├── Event.java
            │       └── Registration.java
            └────── service/
                    ├── UserService.java
                    ├── EventService.java
                    └── RegistrationService.java
```

---

## How to Build and Run

### Prerequisites

* Java Development Kit (JDK) 11 or newer.
* An IDE like IntelliJ, Eclipse, or VS Code is recommended for easier development.

### Running from an IDE (Recommended)

1.  Open the project folder (`EventManagerSystem`) in your IDE.
2.  Locate `src/com/eventms/Main.java`.
3.  Click the **Run** button next to the `main` method. The IDE will handle compiling and running automatically.

### Building and Running from the Command Line

#### Step 1: Compile the Code

Compile all `.java` files from the `src` directory and place the output `.class` files into the `bin` directory.

1.  Open a terminal in the root project folder (`EventManagerSystem/`).
2.  Run the `javac` command with the `-d` flag:
    ```bash
    javac -d bin src/com/eventms/model/*.java src/com/eventms/service/*.java src/com/eventms/Main.java
    ```

#### Step 2: Run the Application

Run the compiled application from the `bin` directory using the `-cp` (classpath) flag.

1.  Make sure you are still in the root project folder (`EventManagerSystem/`).
2.  Run the `java` command:
    ```bash
    java -cp bin com.eventms.Main
    ```
You will see the simulation output printed to your terminal.

---

## Core Features Implemented

* **Separation of Concerns:** A clean architecture that separates `model` (data) classes from `service` (logic) classes.
* **User Management:** `UserService` to create, find, and manage users.
* **Event Management:** `EventService` allows organizers to create, update, cancel, and find events.
* **Registration System:** `RegistrationService` handles guest registration for events and enforces capacity limits.
* **Console-Based Simulation:** `Main.java` provides a comprehensive test suite to demonstrate all backend functionalities.