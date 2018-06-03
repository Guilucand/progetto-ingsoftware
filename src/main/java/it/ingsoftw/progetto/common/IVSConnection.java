package it.ingsoftw.progetto.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfaccia di interazione con una macchina di monitoraggio
 * dei parametri vitali
 */
public interface IVSConnection extends Remote {

    /**
     * Ritorna i bpm attuali
     * @return i bpm
     * @throws RemoteException
     */
    int getBpm() throws RemoteException;

    /**
     * Ritorna la pressione massima attuale
     * @return la sbp
     * @throws RemoteException
     */
    int getSbp() throws RemoteException;

    /**
     * Ritorna la pressione minima attuale
     * @return la dbp
     * @throws RemoteException
     */
    int getDbp() throws RemoteException;

    /**
     * Ritorna la temperatura attuale
     * @return la temperatura
     * @throws RemoteException
     */
    float getTemp() throws RemoteException;
}
