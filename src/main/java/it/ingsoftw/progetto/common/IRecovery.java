package it.ingsoftw.progetto.common;

import it.ingsoftw.progetto.common.messages.MessageObject;
import javafx.util.Pair;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interfaccia di monitoraggio di un paziente attualmente ricoverato
 */
public interface IRecovery extends Remote {

    /**
     * Aggiunge la diagnosi
     * @return true se aggiunta con successo
     * @throws RemoteException
     */
    boolean addDiagnosis(String diagnosis) throws RemoteException;

    /**
     * Dimette un paziente
     * @param dimissionLetter la lettera di dimissioni
     * @return true se il paziente e' stato dimesso
     */
    boolean leavePatient(String dimissionLetter) throws RemoteException;

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
     * Aggiunge una prescrizione
     * @param prescription la prescrizione
     * @throws RemoteException
     */
    boolean addDrugPrescription(DrugPrescription prescription) throws RemoteException;

    /**
     * Aggiunge una somministrazione
     * @param administration la somministrazione
     * @throws RemoteException
     */
    void addDrugAdministration(DrugAdministration administration) throws RemoteException;

    /**
     * Ritorna le prescrizioni correnti
     * @return lista di prescrizioni
     * @throws RemoteException
     */
    List<DrugPrescription> getCurrentPrescriptions() throws RemoteException;

    /**
     * Returns last vs data
     * @param maxMinutes maximum minutes to return
     * @return
     * @throws RemoteException
     */
    List<Pair<LocalDateTime, MonitorData>> getLastVsData(int maxMinutes) throws RemoteException;
}
