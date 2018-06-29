package it.ingsoftw.progetto.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfaccia principale di connessione di un client con il server
 * Fornisce metodi per ottenere interfacce in grado di eseguire azioni sul server
 */
public interface IClientRmiFactory extends Remote {
    /**
     * Ottiene un'interfaccia per controllare il login dell'utente
     * @return l'interfaccia
     * @throws RemoteException
     */
    ILogin getLoginInterface() throws RemoteException;

    /**
     * Ottiene un'interfaccia per il monitoraggio dei pazienti ricoverati
     * @return l'interfaccia
     * @throws RemoteException
     */
    IMonitor getMonitorInterface() throws RemoteException;

    /**
     * Ottiene un'interfaccia per l'amministrazione degli utenti
     * @return l'amministrazione degli utenti
     * @throws RemoteException
     */
    IAdmin getAdminInterface() throws RemoteException;
}
