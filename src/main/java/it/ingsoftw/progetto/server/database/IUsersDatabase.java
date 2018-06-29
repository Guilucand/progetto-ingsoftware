package it.ingsoftw.progetto.server.database;

import it.ingsoftw.progetto.common.EditableUser;
import it.ingsoftw.progetto.common.User;
import it.ingsoftw.progetto.common.utils.Password;

import java.sql.SQLException;
import java.util.List;

/**
 * Interfaccia di accesso e modifica degli utenti salvati sul database
 */
public interface IUsersDatabase {

    /**
     * Aggiunge un utente al database degli utenti
     * @param user il nuovo utente
     * @return true se aggiunto correttamente
     */
    boolean addUser(User user, Password tempPassword) throws SQLException;

    /**
     * Rimuove un utente
     * @param user l'utente da rimuovere
     * @return true se rimosso con successo
     */
    boolean removeUser(User user) throws SQLException;

    /**
     * Ritorna un utente in base all'id
     * @param id l'id
     * @return l'utente associato o null se non esiste
     */
    User getUser(String id) throws SQLException;

    /**
     * Ritorna un utente in base all'indirizzo email
     * @param email
     * @return l'utente associato o null se non esiste
     */
    User getUserFromEmail(String email) throws SQLException;

    /**
     * Ritorna la lista di tutti gli utenti
     * del sistema
     * @return la lista degli utenti
     */
    List<User> getUserList() throws SQLException;


    /**
     * Ritorna una classe di modifica dell'utente
     * @param id l'id dell'utente
     * @return la classe o null se non esiste
     */
    EditableUser getEditableUser(String id) throws SQLException;

    /**
     * Aggiorna i dati di un utente, modificati tramite la classe di modifica
     * @param updatedUser la classe di modifica
     */
    boolean updateUser(EditableUser updatedUser) throws SQLException;

    /**
     * Aggiorna la password
     * @param id l'id dell'utente
     * @param newPassword la nuova password
     * @return true se cambiata con successo
     */
    boolean updatePassword(String id, Password newPassword) throws SQLException;

    /**
     * Autentica un utente
     * @param id l'id dell'utente
     * @param password la password offuscata
     * @return true se l'utente e' autenticato
     */
    boolean authenticateUser(String id, Password password) throws SQLException;
}
