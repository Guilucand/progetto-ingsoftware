package it.ingsoftw.progetto.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfaccia che fornisce metodi per il monitoraggio dei pazienti ricoverati
 */
public interface IMonitor extends Remote {

    /**
     * Ritorna un array dei pazienti ricoverati in questo momento
     * @return la lista di pazienti
     */
//    IPatient[] getHospitalizedPatients();

    /**
     * Ritorna una stanza a partire dal suo numero
     * @param roomNumber il numero di stanza
     * @return la stanza o null se la stanza non esiste
     */
    IRoom getRoomByNumber(int roomNumber) throws RemoteException;
}
