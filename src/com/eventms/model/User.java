package com.eventms.model;

import java.util.List;

public class User {
    private static int idCounter = 0;

    public int UserId;
    public String fullName;         // guest profile
    private String email;
    private String contactNumber;    // guest contact
    private String password;         // for login
    public String language;         // language preference
    public String createdAt;          
    public String occupation;       // job
    private String dob;              // date of birth
    private String role;            // ORGANIZER (true), GUEST (false)
    public String pastEvent;        // for event in the past 
    public String eventId;          // ID of the event registered for
    public char gender;           // gender identity
  

    //P
    public String getPassword(String inputPassword) {
        if (inputPassword.equals(this.password)) {
            return this.password;
        } else {
            System.out.println("Wrong password");
            return null;
        }
    }
    public void setPassword(String newPassword, String oldPassword) {
        if (oldPassword.equals(this.password)) {
            this.password = newPassword;
            System.out.println("Change successful");
        } else {
            System.out.println("Wrong password");
        }
    }

    // Constructor to registor a account
    public User(int userId, String fullName, String email, String contactNumber, String password, String language,
            String occupation, String dob, char gender) {
        UserId = userId;
        this.fullName = fullName;
        this.email = email;
        this.contactNumber = contactNumber;
        this.password = password;
        this.language = language;
        this.occupation = occupation;
        this.dob = dob;
        this.gender = gender;
    }
    
    public static void createUser(){

    }

    public static void findUserById(){

    }
    public static void updateUserProfile(){

    }
    public List getPasEvenList(){

    }
    public String changeLanguage(){
        
    }
}
