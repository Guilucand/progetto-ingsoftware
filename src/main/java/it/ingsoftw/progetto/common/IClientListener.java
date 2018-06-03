package it.ingsoftw.progetto.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfaccia iniziale di connessione di un client al server
 */
public interface IClientListener extends Remote {
    /**
     * Chiamata dai client per stabilire una connessione con il server
     * @return la factory per ottenere interfacce di interazione con il server
     * @throws RemoteException
     */
    IClientRmiFactory estabilishConnection() throws RemoteException;
}
