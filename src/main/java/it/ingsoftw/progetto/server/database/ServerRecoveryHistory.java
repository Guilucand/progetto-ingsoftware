package it.ingsoftw.progetto.server.database;

import it.ingsoftw.progetto.common.IRecoveryHistory;
import it.ingsoftw.progetto.common.MonitorData;
import it.ingsoftw.progetto.server.ServerConfig;
import javafx.util.Pair;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.List;

public class ServerRecoveryHistory extends UnicastRemoteObject implements IRecoveryHistory {
    private IMessageDatabase messageDatabase;
    private IRecoveryDatabase recoveryDatabase;

    public ServerRecoveryHistory(IMessageDatabase messageDatabase,
                                    IRecoveryDatabase recoveryDatabase) throws RemoteException {
        super(ServerConfig.port);
        this.messageDatabase = messageDatabase;
        this.recoveryDatabase = recoveryDatabase;
    }

    @Override
    public RecoveryInfo getRecoveriesList(LocalDateTime begin, LocalDateTime end) {
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
