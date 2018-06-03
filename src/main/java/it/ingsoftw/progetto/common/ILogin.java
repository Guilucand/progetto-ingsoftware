package it.ingsoftw.progetto.common;


import java.rmi.Remote;
import java.rmi.RemoteException;

import it.ingsoftw.progetto.common.utils.Password;

/**
 * Interfaccia per eseguire azioni di login sul server
 */
public interface ILogin extends Remote {

    /**
     * Enumerazione dei possibili stati di login
     */
    enum LoginStatus {
        /**
         * Nessun utente loggato
         */
        NOTLOGGED,
        /**
         * Medico loggato
         */
        MEDIC_LOGGED,
        /**
         * Infermiere loggato
         */
        NURSE_LOGGED,
        /**
         * Primario loggato
         */
        PRIMARY_LOGGED,
        /**
         * Amministratore loggato
         */
        ADMIN_LOGGED
    }

    /**
     * Tentativo di login al server
     * @param userId user id
     * @param password password offuscata
     * @return lo stato del login
     * @throws RemoteException
     */
    LoginStatus doLogin(String userId, Password password) throws RemoteException;

    /**
     * Ritorna lo stato del login, senza effettuare alcuna azione
     * @return lo stato del login
     * @throws RemoteException
     */
    LoginStatus isLogged() throws RemoteException;

    /**
     * Effettua il logout dell'utente connesso
     * @return true se la disconnessione ha avuto successo o nessun utente e' loggato
     * @throws RemoteException
     */
    boolean doLogout() throws RemoteException;

    /**
     * Comunica al server una richiesta di recupero password
     * @param email l'email di recupero
     * @return true se la richiesta e' andata a buon fine
     * @throws RemoteException
     */
    boolean passwordForgotten(String email) throws RemoteException;

    /**
     * Richiesta di cambio password dell'utente correntemente loggato
     * @param oldPassword la vecchia password
     * @param newPassword la nuova password
     * @return true se la richiesta e' andata a buon fine
     * @throws RemoteException
     */
    boolean changePassword(Password oldPassword, Password newPassword) throws RemoteException;
}
