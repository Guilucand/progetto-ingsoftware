package it.ingsoftw.progetto.common;

import it.ingsoftw.progetto.common.messages.IMessagesChangedCallback;
import it.ingsoftw.progetto.common.messages.MessageObject;
import javafx.util.Pair;
import jdk.jfr.Timespan;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

import javax.mail.Message;

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
    Pair<LocalDateTime, MonitorData>[] getMonitorHistory(Period period) throws RemoteException;


    /**
     * Imposta un callback per la ricezione
     * degli allarmi
     * @param callback la funzione da chiamare
     *                 per ogni allarme ricevuto
     * @throws RemoteException
     */
    void setAlarmCallback(IAlarmCallback callback) throws RemoteException;

    void addDrugPrescription() throws RemoteException;

    void addDrugAdministration() throws RemoteException;

    /**
     * Ritorna la lista dei messaggi
     * per il paziente corrente
     * @return lista dei messaggi
     */
    List<MessageObject> getMessages() throws RemoteException;

    /**
     * Imposta il callback che verra' chiamato
     * ogni volta che i messaggi sono aggiornati
     * @param callback interfaccia di callback
     */
    void setMessagesChangedCallback(IMessagesChangedCallback callback) throws RemoteException;
}
