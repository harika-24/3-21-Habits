package edu.northeastern.a321habits.model;

public class UserModel {
//    private String firstName;
//    private String lastName;
    private String profilePic;
    private String name;

    public UserModel(String name, String profilePic) {
        this.name = name;
//        this.firstName = firstName;
//        this.lastName = lastName;
        this.profilePic = profilePic;
    }

//    public String getFirstName() {
//        return firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }

    public String getProfilePic() {
        return profilePic;
    }

    public String getName() { return name;}

//    public String getFullName() {
//        return firstName.concat(" " + lastName);
//    }
}
