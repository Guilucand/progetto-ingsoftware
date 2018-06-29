package it.ingsoftw.progetto.server;

import javafx.util.Pair;
import it.ingsoftw.progetto.common.IPatient;
import it.ingsoftw.progetto.common.MonitorData;
import it.ingsoftw.progetto.common.PatientData;
import it.ingsoftw.progetto.server.database.IRecoveryDatabase;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.Period;
import java.util.Date;

public class ServerPatient extends UnicastRemoteObject implements IPatient {

    private ClientStatus status;
    private IRecoveryDatabase database;
    private String patientId;

    public ServerPatient(ClientStatus status, IRecoveryDatabase database, String patientId) throws RemoteException {
        super(ServerConfig.port);
        this.status = status;

        this.database = database;
        this.patientId = patientId;
    }

    @Override
    public PatientData getPatientData() {
        return null;
    }

    @Override
    public MonitorData getCurrentMonitorData() {
        return database.getCurrentVsData(patientId);
    }

    @Override
    public Pair<Date, MonitorData>[] getMonitorHistory(Period period) {
        Pair<Date, MonitorData>[] data = new Pair[1];
        data[0] = new Pair<>(new Date(), getCurrentMonitorData());
        return data;
    }
}
