package it.ingsoftw.progetto.common;

public class User {

    public enum UserType {
        Medic,
        Nurse
    }

    public User(String id, String name, String surname, String email, UserType userType) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.userType = userType;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public UserType getUserType() {
        return userType;
    }

    protected String id;
    protected String name;
    protected String surname;
    protected String email;
    protected UserType userType;
}
