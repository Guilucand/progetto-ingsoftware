package it.ingsoftw.progetto.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

import it.ingsoftw.progetto.common.IAlarmCallback;
import it.ingsoftw.progetto.common.IMonitorDataUpdatedCallback;
import it.ingsoftw.progetto.common.IPatient;
import it.ingsoftw.progetto.common.MonitorData;
import it.ingsoftw.progetto.common.PatientData;
import it.ingsoftw.progetto.common.messages.IMessagesChangedCallback;
import it.ingsoftw.progetto.common.messages.MessageObject;
import it.ingsoftw.progetto.server.database.IMessageDatabase;
import it.ingsoftw.progetto.server.database.IRecoveryDatabase;
import javafx.util.Pair;

public class ServerRecovery extends UnicastRemoteObject implements IPatient {

    private ClientStatus status;
    private IRecoveryDatabase database;
    private IMessageDatabase messageDatabase;
    private String recoveryId;
    private IAlarmCallback alarmCallback;
    private IMonitorDataUpdatedCallback monitorDataUpdatedCallback;

    public ServerRecovery(ClientStatus status, IRecoveryDatabase database, IMessageDatabase messageDatabase, String recoveryId) throws RemoteException {
        super(ServerConfig.port);
        this.status = status;

        this.database = database;
        this.messageDatabase = messageDatabase;
        this.recoveryId = recoveryId;
    }

    @Override
    public PatientData getPatientData() {
        return null;
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
    public void addDrugPrescription() {

    }

    @Override
    public void addDrugAdministration() throws RemoteException {

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
}
