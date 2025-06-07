# Event Management System

**Version 1.0**

## Project Description

This project is a role-based **Event Management System** developed in Java. It serves as a platform for organizers to create and manage events, and for guests to discover and register for them. The system supports functionalities like event categorization, waiting lists for full events, attendance tracking, and automated email notifications.

This system is being developed as a class project, focusing on applying Object-Oriented Programming (OOP) principles and clean software architecture.

---

## Team Members

* [Sun Heng] - Role : TBC
* [Veat Bunchhour] - Role : TBC
* [Cheav Vichar] - Role : TBC
* [Kheang Rachana] - Role : TBC
* [Mean Sambathpoty] - Role : TBC

---

## Project Structure

The project follows a standard Java package structure to keep the code organized and maintainable.

```
Event-Manager-System/
├── .gitignore
├── README.md
└── src/
└── com/
└── eventms/
├── model/
│   ├── User.java
│   ├── Event.java
│   ├── Role.java
│   └── EventCategory.java
│
└── Main.java
```

---

## How to Build and Run

1.  **Prerequisites**:
    * Java Development Kit (JDK) 11 or newer.
    * An IDE like IntelliJ, Eclipse, or VS Code is recommended.

2.  **Clone the Repository**:
    ```bash
    git clone [https://github.com/SirOsborn/Event-Registration-System.git]
    ```

3.  **Compile and Run from the Command Line**:
    * Navigate to the `src` directory.
    * Compile all Java files:
        ```bash
        javac com/eventms/model/*.java com/eventms/Main.java
        ```
    * Run the main application from the `src` directory:
        ```bash
        java com.eventms.Main
        ```

4.  **Running from an IDE**:
    * Open the project folder in your IDE.
    * Locate `src/com/eventms/Main.java`.
    * Run the `main` method directly from the IDE.

---

## Core Features Implemented

* **User Management**: Create `ORGANIZER` and `GUEST` user roles.
* **Event Creation**: Organizers can create events with details like capacity, category, and date.
* **Registration System**: Guests can register for events.
* **Waiting List**: Automatically manages a waiting list when an event is full.
