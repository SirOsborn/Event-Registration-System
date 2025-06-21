package com.eventms;

import com.eventms.model.Event;
import com.eventms.model.Role;
import com.eventms.model.User;
import com.eventms.service.EventService;
import com.eventms.service.RegistrationService;
import com.eventms.service.UserService;
import java.util.List;
import java.util.Scanner;

/**
 * The main entry point for the Event Management System application.
 * This class provides an interactive Command-Line Interface (CLI) to test the system.
 */
public class Main {

    // Make services and scanner available to all methods in this class
    private static final UserService userService = new UserService();
    private static final EventService eventService = new EventService();
    private static final RegistrationService registrationService = new RegistrationService();
    private static final Scanner scanner = new Scanner(System.in);

    // Keep track of the logged-in user
    private static User loggedInUser = null;

    public static void main(String[] args) {
        System.out.println("--- Welcome to the Event Management System ---");
        // Pre-populate some data for easy testing, including an ADMIN user.
        setupInitialData();

        // Main application loop
        boolean running = true;
        while (running) {
            showMainMenu();
            int choice = getUserChoice();
            switch (choice) {
                case 1:
                    handleEventActions();
                    break;
                case 2:
                    handleUserActions();
                    break;
                case 3:
                    // This option will only be shown if an admin is logged in
                    if (loggedInUser != null && loggedInUser.getRole() == Role.ADMIN) {
                        handleAdminActions();
                    } else {
                        System.out.println("Invalid choice. Please try again.");
                    }
                    break;
                case 9:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        System.out.println("Thank you for using the Event Management System. Goodbye!");
        scanner.close();
    }

    private static void showMainMenu() {
        System.out.println("\n--- Main Menu ---");
        if (loggedInUser != null) {
            System.out.println("Logged in as: " + loggedInUser.getFullName() + " (Role: " + loggedInUser.getRole() + ")");
        } else {
            System.out.println("You are not logged in.");
        }
        System.out.println("1. View/Manage Events");
        System.out.println("2. User Account Actions (Login/Register/Logout)");
        // ** NEW: Only show the admin menu if an admin is logged in **
        if (loggedInUser != null && loggedInUser.getRole() == Role.ADMIN) {
            System.out.println("3. Admin Dashboard");
        }
        System.out.println("9. Exit");
        System.out.print("Enter your choice: ");
    }
    
    // --- NEW: Admin-specific actions ---
    private static void handleAdminActions() {
        System.out.println("\n--- Admin Dashboard ---");
        System.out.println("1. Approve Pending Events");
        System.out.println("2. Approve User Verification Requests");
        System.out.println("3. Return to Main Menu");
        System.out.print("Enter your choice: ");
        int choice = getUserChoice();

        switch (choice) {
            case 1:
                approvePendingEvents();
                break;
            case 2:
                approveUserVerifications();
                break;
            case 3:
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }
    
    private static void approvePendingEvents() {
        // We need a method in EventService to get pending events, let's assume it exists
        // List<Event> pendingEvents = eventService.getPendingEvents();
        System.out.println("\n--- Approving Pending Events (Feature Placeholder) ---");
        System.out.println("In a real system, you would list events with status PENDING_APPROVAL here.");
        System.out.print("Enter the ID of the event to approve: ");
        int eventId = getUserChoice();
        eventService.approveEvent(loggedInUser, eventId);
    }
    
    private static void approveUserVerifications() {
        // We need a method in UserService to get users pending verification
        // List<User> pendingUsers = userService.getPendingVerificationUsers();
        System.out.println("\n--- Approving User Verifications (Feature Placeholder) ---");
        System.out.println("In a real system, you would list users with status PENDING_REVIEW here.");
        System.out.print("Enter the ID of the user to verify: ");
        int userId = getUserChoice();
        userService.approveVerification(loggedInUser, userId);
    }

    // --- Existing methods from before (some with minor changes) ---
    private static void handleEventActions() {
        System.out.println("\n--- Event Menu ---");
        System.out.println("1. List all open events");
        System.out.println("2. Register for an event");
        System.out.println("3. Create a new event");
        System.out.println("4. Return to Main Menu");
        System.out.print("Enter your choice: ");
        int choice = getUserChoice();

        switch (choice) {
            case 1:
                listOpenEvents();
                break;
            case 2:
                registerForEvent();
                break;
            case 3:
                createEvent();
                break;
            case 4:
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void handleUserActions() {
        if (loggedInUser != null) {
            System.out.println("\n--- User Account Menu ---");
            System.out.println("1. Logout");
            System.out.println("2. Request Verification");
            System.out.print("Enter your choice: ");
            int choice = getUserChoice();
            if(choice == 1) {
                loggedInUser = null;
                System.out.println("You have been logged out.");
            } else if (choice == 2) {
                System.out.print("Enter the URL of your organization ID card for verification: ");
                String idUrl = scanner.nextLine();
                userService.requestVerification(loggedInUser.getUserId(), idUrl);
            }
            return;
        }

        System.out.println("\n--- User Account Menu ---");
        System.out.println("1. Login");
        System.out.println("2. Create a new account");
        System.out.print("Enter your choice: ");
        int choice = getUserChoice();

        switch (choice) {
            case 1:
                login();
                break;
            case 2:
                loggedInUser = createUser();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void listOpenEvents() {
        System.out.println("\n--- Open Events ---");
        List<Event> openEvents = eventService.getOpenEvents();
        if (openEvents.isEmpty()) {
            System.out.println("There are no open events at the moment.");
        } else {
            for (Event event : openEvents) {
                System.out.printf("ID: %d | Title: %s | Status: %s%n",
                        event.getEventId(), event.getTitle(), event.getEventStatus());
            }
        }
    }

    private static void registerForEvent() {
        if (loggedInUser == null) {
            System.out.println("Error: You must be logged in to register for an event.");
            return;
        }
        listOpenEvents();
        System.out.print("Enter the ID of the event you want to register for: ");
        int eventId = getUserChoice();
        
        String registrationDate = java.time.LocalDate.now().toString();
        registrationService.registerGuest(loggedInUser.getUserId(), eventId, registrationDate, eventService);
    }

    private static void createEvent() {
        if (loggedInUser == null) {
            System.out.println("Error: You must be logged in to create an event.");
            return;
        }
        System.out.print("Enter event title: ");
        String title = scanner.nextLine();
        System.out.print("Enter event description: ");
        String desc = scanner.nextLine();
        System.out.print("Enter event capacity: ");
        int capacity = getUserChoice();

        eventService.createEvent(title, desc, "Online", "TBD", "TBD", capacity, "Any", "General", "English", loggedInUser);
    }

    private static User createUser() {
        System.out.print("Enter your full name: ");
        String name = scanner.nextLine();
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        System.out.print("Enter a password: ");
        String password = scanner.nextLine();
        
        return userService.createUser(name, email, "N/A", password, "English", "N/A", "N/A", 'N');
    }

    private static void login() {
        System.out.print("Enter your User ID to login: ");
        int userId = getUserChoice();
        User user = userService.findUserById(userId);
        if (user != null) {
            loggedInUser = user;
            System.out.println("Login successful. Welcome, " + user.getFullName());
        } else {
            System.out.println("Login failed: User not found.");
        }
    }

    private static int getUserChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void setupInitialData() {
        // ** NEW: Create a specific ADMIN user for your team to use **
        User admin = userService.createUser("Admin User", "admin@eventms.com", "N/A", "admin123", "English", "Admin", "N/A", 'A');
        admin.setRole(Role.ADMIN); // Manually set the role to ADMIN for this special user
        admin.setVerified(true); // Admins are always verified
        System.out.println(">>> Admin account created with ID: " + admin.getUserId() + " <<<");

        // Create a regular user for testing
        User user = userService.createUser("Test User", "test@user.com", "N/A", "pass123", "Khmer", "Student", "N/A", 'F');
        // This user creates an event that will be PENDING_APPROVAL
        eventService.createEvent("Test Event by Unverified User", "This should be pending", "Online", "TBD", "TBD", 10, "Any", "Test", "English", user);
    }
}