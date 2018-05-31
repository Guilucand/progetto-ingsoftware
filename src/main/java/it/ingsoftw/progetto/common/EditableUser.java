package it.ingsoftw.progetto.common;

public class EditableUser extends User {

    public EditableUser(String id, String name, String surname, String email, UserType userType) {
        super(id, name, surname, email, userType);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
