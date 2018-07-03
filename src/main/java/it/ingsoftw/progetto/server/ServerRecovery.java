package it.ingsoftw.progetto.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

import it.ingsoftw.progetto.common.DrugAdministration;
import it.ingsoftw.progetto.common.DrugPrescription;
import it.ingsoftw.progetto.common.IRecovery;
import it.ingsoftw.progetto.common.MonitorData;
import it.ingsoftw.progetto.common.PatientData;
import it.ingsoftw.progetto.common.messages.MessageObject;
import it.ingsoftw.progetto.server.database.IMessageDatabase;
import it.ingsoftw.progetto.server.database.IPatientsDatabase;
import it.ingsoftw.progetto.server.database.IPrescriptionDatabase;
import it.ingsoftw.progetto.server.database.IRecoveryDatabase;
import javafx.util.Pair;

public class ServerRecovery extends UnicastRemoteObject implements IRecovery {

    private ClientStatus status;
    private IRecoveryDatabase database;
    private IMessageDatabase messageDatabase;
    private IPatientsDatabase patientsDatabase;
    private final IPrescriptionDatabase prescriptionDatabase;
    private int recoveryKey;

    public ServerRecovery(ClientStatus status,
                          IRecoveryDatabase database,
                          IMessageDatabase messageDatabase,
                          IPatientsDatabase patientsDatabase,
                          IPrescriptionDatabase prescriptionDatabase, int recoveryKey) throws RemoteException {
        super(ServerConfig.port);
        this.status = status;

        this.database = database;
        this.messageDatabase = messageDatabase;
        this.patientsDatabase = patientsDatabase;
        this.prescriptionDatabase = prescriptionDatabase;
        this.recoveryKey = recoveryKey;
    }

    @Override
    public boolean addDiagnosis(String diagnosis) {
        if (diagnosis == null)
            return false;

        if (this.status.getLoggedUser() != null) {
            switch (this.status.getLoggedUser().getUserType()) {
                case Primary:
                case Medic:
                case Admin:
                    return database.addDiagnosis(recoveryKey, diagnosis);
            }
        }
        return false;
    }

    @Override
    public boolean leavePatient(String dimissionLetter) {
        if (dimissionLetter == null)
            return false;

        if (this.status.getLoggedUser() != null) {
            switch (this.status.getLoggedUser().getUserType()) {
                case Primary:
                    return database.leaveRecovery(recoveryKey, dimissionLetter);
            }
        }
        return false;
    }

    @Override
    public PatientData getPatientData() {
        try {
            return patientsDatabase.getPatientByCode(database.mapRecoveryToPatient(recoveryKey));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public MonitorData getCurrentMonitorData() {
        return database.getCurrentVsData(recoveryKey);
    }

    @Override
    public boolean addDrugPrescription(DrugPrescription prescription) throws RemoteException {
        return prescriptionDatabase.addPrescription(recoveryKey, status.getLoggedUser(), prescription);
    }

    @Override
    public void addDrugAdministration(DrugAdministration administration) throws RemoteException {
        prescriptionDatabase.addAdministration(recoveryKey, status.getLoggedUser(), administration);
    }

    @Override
    public List<DrugPrescription> getCurrentPrescriptions() throws RemoteException {
        return prescriptionDatabase.getPrescriptions(recoveryKey);
    }

    @Override
    public List<Pair<LocalDateTime, MonitorData>> getLastVsData(int maxMinutes) {
        return database.getLastMonitorData(recoveryKey, maxMinutes);
    }
}
