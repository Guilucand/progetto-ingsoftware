package it.ingsoftw.progetto.server.database;

import it.ingsoftw.progetto.common.IRecoveryHistory;
import it.ingsoftw.progetto.common.MonitorData;
import it.ingsoftw.progetto.common.PatientData;
import it.ingsoftw.progetto.server.ServerConfig;
import javafx.util.Pair;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class ServerRecoveryHistory extends UnicastRemoteObject implements IRecoveryHistory {
    private IMessageDatabase messageDatabase;
    private IRecoveryDatabase recoveryDatabase;
    private IPatientsDatabase patientsDatabase;

    public ServerRecoveryHistory(IMessageDatabase messageDatabase,
                                    IRecoveryDatabase recoveryDatabase,
                                     IPatientsDatabase patientsDatabase) throws RemoteException {
        super(ServerConfig.port);
        this.messageDatabase = messageDatabase;
        this.recoveryDatabase = recoveryDatabase;
        this.patientsDatabase = patientsDatabase;
    }

    @Override
    public RecoveryInfo getRecoveryFromKey(int recoveryKey) {
        return recoveryDatabase.getRecoveryInfoFromKey(recoveryKey);
    }

    @Override
    public List<RecoveryInfo> getRecoveriesList(LocalDateTime begin, LocalDateTime end) {
        return recoveryDatabase.getRecoveryInfos(begin, end);
    }

    @Override
    public PatientData getPatientData(String patientCode) {
        try {
            return patientsDatabase.getPatientByCode(patientCode);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Pair<LocalDateTime, String>> getEventsBetween(int recoveryKey, LocalDateTime begin, LocalDateTime end) {
        return messageDatabase.getMessagesForRecovery(recoveryKey, begin, end);
    }

    @Override
    public List<Pair<LocalDateTime, MonitorData>> getMonitorsBetween(int recoveryKey, LocalDateTime begin, LocalDateTime end) {
        return recoveryDatabase.getMonitorData(recoveryKey, begin, end);
    }
}
