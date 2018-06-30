package it.ingsoftw.progetto.server;

import it.ingsoftw.progetto.common.IMonitor;
import it.ingsoftw.progetto.common.IPatient;
import it.ingsoftw.progetto.server.database.IRecoveryDatabase;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerMonitor extends UnicastRemoteObject implements IMonitor {

    private ClientStatus status;
    private IRecoveryDatabase recoveryDatabase;

    public ServerMonitor(ClientStatus status, IRecoveryDatabase recoveryDatabase) throws RemoteException {
        super(ServerConfig.port);
        this.status = status;

        this.recoveryDatabase = recoveryDatabase;
    }

    //@Override
    public IPatient[] getHospitalizedPatients() {
        return new IPatient[1];
    }

    @Override
    public IPatient getPatientByRoomNumber(int roomNumber) throws RemoteException{
        return new ServerPatient(status, recoveryDatabase, recoveryDatabase.mapRoomToRecovery(String.valueOf(roomNumber)));
    }
}
