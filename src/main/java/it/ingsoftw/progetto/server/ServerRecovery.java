package it.ingsoftw.progetto.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

import it.ingsoftw.progetto.common.DrugAdministration;
import it.ingsoftw.progetto.common.DrugPrescription;
import it.ingsoftw.progetto.common.IAlarmCallback;
import it.ingsoftw.progetto.common.IMonitorDataUpdatedCallback;
import it.ingsoftw.progetto.common.IPatient;
import it.ingsoftw.progetto.common.MonitorData;
import it.ingsoftw.progetto.common.PatientData;
import it.ingsoftw.progetto.common.messages.IMessagesChangedCallback;
import it.ingsoftw.progetto.common.messages.MessageObject;
import it.ingsoftw.progetto.server.database.IMessageDatabase;
import it.ingsoftw.progetto.server.database.IPatientsDatabase;
import it.ingsoftw.progetto.server.database.IPrescriptionDatabase;
import it.ingsoftw.progetto.server.database.IRecoveryDatabase;
import javafx.util.Pair;

public class ServerRecovery extends UnicastRemoteObject implements IPatient {

    private ClientStatus status;
    private IRecoveryDatabase database;
    private IMessageDatabase messageDatabase;
    private IPatientsDatabase patientsDatabase;
    private final IPrescriptionDatabase prescriptionDatabase;
    private String recoveryId;
    private IAlarmCallback alarmCallback;
    private IMonitorDataUpdatedCallback monitorDataUpdatedCallback;

    public ServerRecovery(ClientStatus status,
                          IRecoveryDatabase database,
                          IMessageDatabase messageDatabase,
                          IPatientsDatabase patientsDatabase,
                          IPrescriptionDatabase prescriptionDatabase, String recoveryId) throws RemoteException {
        super(ServerConfig.port);
        this.status = status;

        this.database = database;
        this.messageDatabase = messageDatabase;
        this.patientsDatabase = patientsDatabase;
        this.prescriptionDatabase = prescriptionDatabase;
        this.recoveryId = recoveryId;
    }

    @Override
    public PatientData getPatientData() {
        try {
            return patientsDatabase.getPatientById(database.mapRecoveryToPatient(recoveryId));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public MonitorData getCurrentMonitorData() {
        return database.getCurrentVsData(recoveryId);
    }

    @Override
    public void setMonitorDataUpdatedCallback(IMonitorDataUpdatedCallback callback) {
        monitorDataUpdatedCallback = callback;
        database.addMonitorDataUpdatedCallbackHook(recoveryId, callback);
    }

    @Override
    public Pair<LocalDateTime, MonitorData>[] getMonitorHistory(Period period) {
        Pair<LocalDateTime, MonitorData>[] data = new Pair[1];
        data[0] = new Pair<>(LocalDateTime.now(), getCurrentMonitorData());
        return data;
    }

    @Override
    public void setAlarmCallback(IAlarmCallback callback) throws RemoteException {
        if (alarmCallback != null)
            database.removeAlarmHook(recoveryId, alarmCallback);

        alarmCallback = callback;
        database.addAlarmHook(recoveryId, alarmCallback);
    }

    @Override
    public boolean addDrugPrescription(DrugPrescription prescription) throws RemoteException {
        return prescriptionDatabase.addPrescription(recoveryId, status.getLoggedUser(), prescription);
    }

    @Override
    public void addDrugAdministration(DrugAdministration administration) throws RemoteException {
        prescriptionDatabase.addAdministration(recoveryId, status.getLoggedUser(), administration);
    }

    @Override
    public List<DrugPrescription> getCurrentPrescriptions() throws RemoteException {
        return prescriptionDatabase.getPrescriptions(recoveryId);
    }

    @Override
    public List<MessageObject> getMessages() {
        return messageDatabase.getMessagesForRecovery(recoveryId);
    }

    @Override
    public void setMessagesChangedCallback(IMessagesChangedCallback callback) {
        messageDatabase.addMessagesChangedCallback(recoveryId, callback);
        try {
            callback.messagesChanged();
        } catch (RemoteException ignored) {
        }
    }

    @Override
    public List<Pair<LocalDateTime, MonitorData>> getLastVsData(int maxMinutes) {
        return database.getLastMonitorData(recoveryId, maxMinutes);
    }
}
