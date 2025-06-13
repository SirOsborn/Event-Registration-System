package com.eventms.model;

public class User {
    private static int idCounter = 0;

    public int id;
    public String fullName;         // guest profile
    private String email;
    private String contactNumber;    // guest contact
    private String password;         // for login
    public String language;         // language preference
    public String time;             // preferred time
    public String occupation;       // job
    private String dob;              // date of birth
    private role role;            // ORGANIZER (true), GUEST (false)
    public String pastEvent;        // for event in the past 
    public String eventId;          // ID of the event registered for
    public char gender;           // gender identity
  

    //E
    public String getEmail() {
        return email;
    }       
     public String setEmail(String email) {
        return email;
    }

    //N
    public String getContactNumber() {
        return contactNumber;
    }
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }


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


    // Dob
    public String getDob() {
        return dob;
    }
    public void setDob(String dob) {
        this.dob = dob;
    }
    
    //R
    public role getRole() {
        return role;
    }
    public role setRole(){
        return role;
    }

    //G  
    public char getGender() {
        return gender;
    }
    public char setGender() {
        return gender;
    }

   

    public void setGender(char gender) {
        this.gender = gender;
    }

     public User(String fullName, String email, String contactNumber, String password,
                String language, String time, String occupation, String dob,
                role role, String eventId, String pastEvent, char gender) {

        this.id = ++idCounter;
        this.fullName = fullName;
        this.email = email;
        this.contactNumber = contactNumber;
        this.password = password;
        this.language = language;
        this.time = time;
        this.occupation = occupation;
        this.dob = dob;
        this.role = role;
        this.pastEvent = pastEvent;
        this.eventId = eventId;
        this.gender = gender;
    }

    // Placeholder method (not yet implemented)(Because we will learn in the mean time XD)
    public static void registerUser() {
        // Implementation goes here bla bla blaaaaaaaaaaaaaaaaaaa
    }
}
