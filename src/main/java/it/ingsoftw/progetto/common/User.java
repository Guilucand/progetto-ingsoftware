package it.ingsoftw.progetto.common;

import java.io.Serializable;

/**
 * Classe che rappresenta un utente del programma
 */
public class User implements Serializable {

    /**
     * La tipologia di utente
     */
    public enum UserType {
        /**
         * L'utente e' un medico
         */
        Medic,
        /**
         * L'utente e' un infermiere
         */
        Nurse,
        /**
         * L'utente e' un primario
         */
        Primary,
        /**
         * L'utente e' un amministratore
         */
        Admin,


    }

    public User(String id, String name, String surname, String email, UserType userType) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.userType = userType;
    }

    public User(User other) {
        this.id = other.id;
        this.name = other.name;
        this.surname = other.surname;
        this.email = other.email;
        this.userType = other.userType;
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

    public String toString() {return ""+this.name+" "+this.surname;}


    protected String id;
    protected String name;
    protected String surname;
    protected String email;
    protected UserType userType;
}
