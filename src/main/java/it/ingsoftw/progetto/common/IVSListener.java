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
     * @param connection l'interfaccia di connessione della macchina
     * @return true se la registrazione e' andata a buon fine
     * @throws RemoteException
     */
    boolean connectVS(String id, IVSConnection connection) throws RemoteException;

    /**
     * Inizia un allarme
     * @param id l'id della macchina
     * @param description la descrizione dell'allarme
     * @param level la gravita' dell'allarme
     * @return l'id dell'allarme lato server o -1 in caso di problemi
     * @throws RemoteException
     */
    int startAlarm(String id, String description, AlarmLevel level) throws RemoteException;

    /**
     * Ferma un allarme
     * @param id l'id della macchina
     * @param alarmId l'id dell'allarme ritornato da startAlarm
     * @return true se l'allarme e' stato spento con successo
     * @throws RemoteException
     */
    boolean stopAlarm(String id, int alarmId) throws RemoteException;

    /**
     * Notifica un cambiamento nei parametri del monitor
     * @param id l'id del monitor
     * @param monitorData i dati aggiornati
     * @return
     * @throws RemoteException
     */
    void notifyMonitorUpdate(String id, MonitorData monitorData) throws RemoteException;
}
