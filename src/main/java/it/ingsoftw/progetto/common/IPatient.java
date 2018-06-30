package it.ingsoftw.progetto.common;

import javafx.util.Pair;
import jdk.jfr.Timespan;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.Period;
import java.util.Date;

/**
 * Interfaccia di monitoraggio di un paziente attualmente ricoverato
 */
public interface IPatient extends Remote {

    /**
     * Ottiene i dati anagrafici del paziente
     * @return i dati del paziente
     */
    PatientData getPatientData() throws RemoteException;

    /**
     * Ottiene i dati di monitoraggio correnti
     * @return struttura contenente i dati
     */
    MonitorData getCurrentMonitorData() throws RemoteException;

    /**
     * Imposta un callback che viene chiamato
     * ogni volta che i parametri vitali cambiano
     * @param callback il callback da chiamare
     */
    void setMonitorDataUpdatedCallback(IMonitorDataUpdatedCallback callback) throws RemoteException;

    /**
     * Ottiene lo storico dei dati del paziente
     * @return storico dei dati
     */
    Pair<Date, MonitorData>[] getMonitorHistory(Period period) throws RemoteException;


    /**
     * Imposta un callback per la ricezione
     * degli allarmi
     * @param callback la funzione da chiamare
     *                 per ogni allarme ricevuto
     * @throws RemoteException
     */
    void setAlarmCallback(IAlarmCallback callback) throws RemoteException;

    void addDrugPrescription();
}
