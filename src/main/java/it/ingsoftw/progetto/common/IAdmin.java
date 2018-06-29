package it.ingsoftw.progetto.common;

import java.util.List;

/**
 * Interfaccia per l'aggiunta e la modifica
 * degli utenti dell'applicazione
 */
public interface IAdmin {

    /**
     * Aggiunge un utente al database
     * @param newUser l'utente da aggiungere
     * @return true se l'aggiunta e' andata a buon fine
     */
    boolean addUser(User newUser);

    /**
     * Rimuove un utente
     * @param userToDelete utente da eliminare
     * @return
     */
    boolean deleteUser(EditableUser userToDelete);

    /**
     * Ritorna la lista di tutti
     * gli utenti del sistema
     * @return lista degli utenti
     * null se non si dispone dei permessi necessari
     */
    List<User> getUsers();

    /**
     * Ottiene una classe con metodi
     * per modificare un utente
     * @param id l'id dell'utente da modificare
     * @return l'utente da editare
     * null se non e' possibile modificare l'utente
     */
    EditableUser getEditableUser(String id);

    /**
     * Salva le modifiche apportate ad un utente
     * @param editedUser utente modificato
     * @return true se la modifica ha avuto successo
     */
    boolean commitUserChanges(EditableUser editedUser);
}
