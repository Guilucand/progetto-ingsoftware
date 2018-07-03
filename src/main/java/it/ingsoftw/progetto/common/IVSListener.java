package it.ingsoftw.progetto.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfaccia di connessione e interazione di una macchina di monitoraggio al server
 */
public interface IVSListener extends Remote {

    /**
     * Connette la macchina al server
     * @param id l'id della macchina
     * @return la chiave della macchina, se la registrazione e' andata a buon fine
     * -1 altrimenti
     * @throws RemoteException
     */
    int connectVS(String id) throws RemoteException;

    /**
     *
     * @param id
     * @param connection
     * @return
     * @throws RemoteException
     */
//    boolean disconnectVS(String id, IVSConnection connection) throws RemoteException;


    /**
     * Inizia un allarme
     * @param key la chiave della macchina
     * @param description la descrizione dell'allarme
     * @param level la gravita' dell'allarme
     * @return l'id dell'allarme lato server o -1 in caso di problemi
     * @throws RemoteException
     */
    int startAlarm(int key, String description, AlarmLevel level) throws RemoteException;

    /**
     * Ferma un allarme
     * @param key la chiave della macchina
     * @param alarmId l'id dell'allarme ritornato da startAlarm
     * @return true se l'allarme e' stato spento con successo
     * @throws RemoteException
     */
    boolean stopAlarm(int key, int alarmId) throws RemoteException;

    /**
     * Notifica un cambiamento nei parametri del monitor
     * @param key la chiave del monitor
     * @param monitorData i dati aggiornati
     * @return
     * @throws RemoteException
     */
    void notifyMonitorUpdate(int key, MonitorData monitorData) throws RemoteException;


}
