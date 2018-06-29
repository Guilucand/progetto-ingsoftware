package it.ingsoftw.progetto.common;


/**
 * Classe decoratrice utilizzata per avere accesso alla modifica
 * dei campi di un utente
 */
public class EditableUser extends User {

    private String reference;

    public EditableUser(User user, String reference) {
        super(user.id, user.name, user.surname, user.email, user.userType);
        this.reference = reference;
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

    public String getInternalReference() {
        return reference;
    }
}
