package it.ingsoftw.progetto.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.Period;
import java.util.Date;

import it.ingsoftw.progetto.common.IAlarmCallback;
import it.ingsoftw.progetto.common.IMonitorDataUpdatedCallback;
import it.ingsoftw.progetto.common.IPatient;
import it.ingsoftw.progetto.common.MonitorData;
import it.ingsoftw.progetto.common.PatientData;
import it.ingsoftw.progetto.server.database.IRecoveryDatabase;
import javafx.util.Pair;

public class ServerPatient extends UnicastRemoteObject implements IPatient {

    private ClientStatus status;
    private IRecoveryDatabase database;
    private String recoveryId;
    private IAlarmCallback alarmCallback;
    private IMonitorDataUpdatedCallback monitorDataUpdatedCallback;

    public ServerPatient(ClientStatus status, IRecoveryDatabase database, String recoveryId) throws RemoteException {
        super(ServerConfig.port);
        this.status = status;

        this.database = database;
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
    public Pair<Date, MonitorData>[] getMonitorHistory(Period period) {
        Pair<Date, MonitorData>[] data = new Pair[1];
        data[0] = new Pair<>(new Date(), getCurrentMonitorData());
        return data;
    }

    @Override
    public void setAlarmCallback(IAlarmCallback callback) throws RemoteException {
        if (alarmCallback != null)
            database.removeAlarmHook(recoveryId, alarmCallback);

        alarmCallback = callback;
        database.addAlarmHook(recoveryId, alarmCallback);
    }
}
