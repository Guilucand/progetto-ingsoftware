package it.ingsoftw.progetto.common;


/**
 * Classe decoratrice utilizzata per avere accesso alla modifica
 * dei campi di un utente
 */
public class EditableUser extends User {

    public EditableUser(String id, String name, String surname, String email, UserType userType) {
        super(id, name, surname, email, userType);
    }

    /**
     * Imposta l'id dell'utente
     * @param id id dell'utente
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Imposta il nome dell'utente
     * @param name nome dell'utente
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Imposta il cognome dell'utente
     * @param surname cognome dell'utente
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Imposta l' email dell'utente
     * @param email email dell'utente
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Imposta il tipo dell'utente
     * @param userType tipo dell'utente
     */
    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
