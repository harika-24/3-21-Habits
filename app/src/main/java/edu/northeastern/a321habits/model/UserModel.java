package edu.northeastern.a321habits.model;

public class UserModel {
    private String firstName;
    private String lastName;
    private String profilePic;

    public UserModel(String firstName, String lastName, String profilePic) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePic = profilePic;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public String getFullName() {
        return firstName.concat(" " + lastName);
    }
}
